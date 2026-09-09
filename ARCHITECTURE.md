# Architecture

A high-level map of how a test runs, how Allure captures it, and how the report reaches Slack and GitHub Pages. Three diagrams: the components and how they call each other, one concrete test executing end to end, and the CI/CD + reporting pipeline.

## 1. Component Map

```mermaid
flowchart TB
    subgraph Tests["Test Layer (src/test/java)"]
        BaseTest["BaseTest\n@BeforeMethod / @AfterMethod"]
        DataProviders
        ManagerTests["AddCustomerTest\nCreateAccountTest\nDeleteAccountTest"]
        CustomerTests["DepositTest\nWithdrawTest\nViewTransactionsTest"]
    end

    subgraph Pages["Page Object Layer (src/main/java/pages)"]
        BasePage["BasePage\nclick / type / getText / findAll / acceptAlert"]
        HomePage --> ManagerDashboardPage
        HomePage --> CustomerLoginPage
        ManagerDashboardPage --> AddCustomerPage
        ManagerDashboardPage --> OpenAccountPage
        ManagerDashboardPage --> CustomersListPage
        CustomerLoginPage --> AccountPage
        AccountPage --> DepositPage
        AccountPage --> WithdrawPage
        AccountPage --> TransactionsPage
    end

    subgraph Framework["Framework (src/main/java/utils, models)"]
        DriverFactory
        ConfigReader
        TestDataFactory
        TestDataReader
    end

    subgraph Cross["Cross-cutting (listeners, wired via testng.xml)"]
        AllureTestListener["AllureTestListener\nscreenshot + page source on failure"]
        AllureEnvironmentWriter["AllureEnvironmentWriter\nwrites environment.properties"]
        RetryTransformer["RetryTransformer\npicks RetryAnalyzer or\nEnvironmentFlakeRetryAnalyzer"]
    end

    ManagerTests --> BaseTest
    CustomerTests --> BaseTest
    ManagerTests -. "@DataProvider" .-> DataProviders
    CustomerTests -. "@DataProvider" .-> DataProviders
    DataProviders --> TestDataReader
    BaseTest --> DriverFactory
    BaseTest --> HomePage
    ManagerTests --> TestDataFactory
    CustomerTests --> TestDataFactory
    HomePage -.-> BasePage
    AccountPage -.-> BasePage
    BasePage --> ConfigReader
    DriverFactory -->|Selenium WebDriver| Browser[("Chrome / Firefox")]
    Browser --> App["XYZ Bank demo app\n(AngularJS SPA)"]
```

- **Tests** never touch Selenium directly — they call page objects and assert on what comes back.
- **Page objects** form a navigation chain: each action method returns the *next* page object, so a test reads like the user journey it's automating (`homePage.goToManagerLogin().openAddCustomerTab().addCustomer(customer)`).
- **`BasePage`** (in its own `base` package, a sibling of `pages` — mirroring `BaseTest`) holds the only code that talks to Selenium's `WebDriverWait`/`By` primitives; every concrete page extends it instead of repeating wait logic.
- **Listeners** aren't called by any of the above directly — TestNG invokes them automatically via `testng.xml`'s `<listeners>` block, on every test regardless of which class it's in.

## 2. One Test, End to End

`AddCustomerTest.shouldAddCustomerSuccessfully` — chosen because it touches every layer once.

```mermaid
sequenceDiagram
    participant TestNG
    participant Base as BaseTest
    participant Test as AddCustomerTest
    participant Home as HomePage
    participant Mgr as ManagerDashboardPage
    participant AddCust as AddCustomerPage
    participant List as CustomersListPage
    participant Sel as Selenium / Browser
    participant Allure as Allure result (via AspectJ)

    TestNG->>Base: @BeforeMethod setUp()
    Base->>Sel: DriverFactory.getDriver()
    Base->>Home: new HomePage(driver).open()
    Home->>Sel: driver.get(baseUrl)

    TestNG->>Test: shouldAddCustomerSuccessfully()
    Test->>Test: TestDataFactory.uniqueValidCustomer()
    Test->>Home: goToManagerLogin()
    Home->>Sel: click(MANAGER_LOGIN_BUTTON)
    Home-->>Test: ManagerDashboardPage
    Note over Home,Allure: every @Step method call above<br/>is recorded as a report step

    Test->>Mgr: openAddCustomerTab()
    Mgr-->>Test: AddCustomerPage
    Test->>AddCust: addCustomer(customer)
    AddCust->>Sel: type first/last/postcode, click submit
    AddCust->>Sel: acceptAlert()
    AddCust-->>Test: Optional<String> confirmation

    Test->>Test: Assert.assertTrue(...)
    Test->>Mgr: openCustomersTab()
    Mgr-->>Test: CustomersListPage
    Test->>List: isCustomerListed(first, last)
    List-->>Test: true
    Test->>Test: Assert.assertTrue(...)

    TestNG->>Base: @AfterMethod tearDown(result)
    Base->>Sel: DriverFactory.quitDriver()

    alt any assertion above failed
        TestNG->>Allure: AllureTestListener.onTestFailure()
        Allure->>Sel: screenshot + page source
        Note over Allure: attached to the report as @Attachment
    end
```

- Every `@Step`-annotated method call (almost every page-object action) is intercepted by the AspectJ weaver and recorded into the in-progress Allure result — that's how a failure's report shows exactly which UI step broke, not just which test method.
- The `alt` block only fires on failure; on a pass, `AllureTestListener` never runs and no screenshot is taken.
- Data-driven tests (`shouldRejectInvalidCustomer`, `shouldHandleDepositAmount`, `shouldHandleWithdrawalAmount`) repeat this same shape once per `@DataProvider` row, each becoming its own entry in the report.

## 3. Reporting & CI/CD Pipeline

```mermaid
flowchart LR
    Push["git push to main"] --> Trigger["GitHub Actions: ci.yml"]
    Trigger --> Setup

    subgraph CIJob["CI job (ubuntu-latest)"]
        direction TB
        Setup["Checkout + JDK 17"] --> RunTests["mvn test\n(AspectJ weaving + allure-testng\nwrite target/allure-results/*.json)"]
        RunTests --> Upload["Upload allure-results\nas build artifact"]
        RunTests --> FetchHistory["Checkout gh-pages branch's\nhistory/ folder"]
        FetchHistory --> SeedHistory["Copy history into\ntarget/allure-results/history"]
        SeedHistory --> GenReport["mvn allure:report\n(allure-maven plugin)"]
        GenReport --> Deploy["Deploy report to\ngh-pages branch"]
        RunTests --> Summarize["Parse testng-results.xml\n+ build Slack payload (jq)"]
    end

    Deploy --> Pages["GitHub Pages\n(live Allure report)"]
    Summarize --> Slack["Slack notification\nresults + failed-test list + buttons"]
    Slack -->|View Report| Pages
    Slack -->|Create Jira Issue| Jira["Jira (amali-tech.atlassian.net)"]
    Annotations["@Link / @Issue annotations\non test classes"] -.->|traceability| Jira
```

- **`mvn test`** is the only step that touches Selenium/Chrome; every step after it just processes files that step produced (`target/allure-results`, `target/surefire-reports/testng-results.xml`).
- **History seeding** is why the deployed report's trend graphs (History/Duration/Retries/Categories Trend) show a multi-build line — each CI run pulls forward the previous run's history before generating, so trend data accumulates across builds instead of resetting. A local `mvn allure:report` skips this step, so those same trend graphs show "There is nothing to show" when generated locally with no history to draw a line through.
- **Slack and the report are independent outputs** of the same run — the report publishes to `gh-pages` regardless of whether the Slack step succeeds, and vice versa (`if: always()` on every post-test step).
- **Jira traceability** is static, not generated at CI time: `@Link`/`@Issue` annotations on the test classes point at `JiraLinks` constants, which Allure renders as clickable links using the `allure.link.issue.pattern`/`allure.link.tms.pattern` configured in `allure.properties`.

## 4. Directory Reference

| Path | Responsibility |
|---|---|
| `src/main/java/com/xyzbank/base/BasePage.java` | Shared Selenium primitives every page object extends |
| `src/main/java/com/xyzbank/pages/` | One class per screen/sub-view |
| `src/main/java/com/xyzbank/models/` | POJOs mirroring the JSON test-data fixtures |
| `src/main/java/com/xyzbank/utils/` | `DriverFactory`, `ConfigReader`, `TestDataReader`, `TestDataFactory` |
| `src/main/java/com/xyzbank/listeners/` | Allure screenshot/environment listeners, retry analyzers |
| `src/test/java/com/xyzbank/base/BaseTest.java` | Driver lifecycle + shared multi-step preconditions |
| `src/test/java/com/xyzbank/providers/` | `@DataProvider` methods (read fixtures, never hardcode data) |
| `src/test/java/com/xyzbank/jira/JiraLinks.java` | Central Jira/Xray keys referenced by `@Link`/`@Issue` |
| `src/test/java/com/xyzbank/tests/` | `manager/` and `customer/` test classes |
| `src/test/resources/testdata/*.json` | Business/domain test data |
| `src/test/resources/suites/testng.xml` | Suite entry point Maven Surefire runs; registers the listeners |
| `.github/workflows/ci.yml` | The pipeline in §3 |
