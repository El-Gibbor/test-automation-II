# XYZ Bank - Selenium WebDriver Automation

Automated test suite for the [XYZ Bank demo app](https://www.globalsqa.com/angularJs-protractor/BankingProject/),
covering the Manager (add customer / create account / delete account) and Customer
(view transactions / deposit / withdraw) user stories. Built with **Java 17, Selenium WebDriver,
TestNG, and Allure Report**, following the Page Object Model.

## Project layout

```
src/main/java/com/xyzbank/
  pages/       Page objects (one class per screen/sub-view)
  models/      POJOs mirroring the JSON test-data fixtures
  utils/       DriverFactory, ConfigReader, TestDataReader, TestDataFactory
  listeners/   Allure screenshot/env listeners, retry analyzer

src/test/java/com/xyzbank/
  base/        BaseTest - driver lifecycle + shared preconditions
  providers/   TestNG @DataProvider methods (read fixtures, never hardcode data)
  tests/       manager/ and customer/ test classes

src/test/resources/
  config.properties     environment/runtime config (base URL, browser, timeouts)
  testdata/*.json        business/domain test data - see "Test data" below
  suites/testng.xml       the suite entry point Maven Surefire runs
  allure/categories.json  custom failure buckets shown in the report
```

## Running locally

```bash
# full suite, headless Chrome (default)
mvn test

# a specific browser / headed mode
mvn test -Dbrowser=firefox -Dheadless=false

# just the smoke subset
mvn test -Dgroups=smoke

# generate and open the Allure report after a run
mvn allure:serve
```

## Running in Docker

```bash
docker compose up --build
# Allure results land in ./target/allure-results on the host
mvn allure:serve
```

## Test data

Business/domain data never lives inside a test method - it's read from
`src/test/resources/testdata/*.json` into typed POJOs (`Customer`, `TransactionAmount`) via
`TestDataReader`, and handed to tests through TestNG `@DataProvider`s in `DataProviders`. Fixed
edge cases (invalid names, boundary amounts) live in JSON since they need to be exact and
reviewable; cases that must be unique per run (e.g. account creation) use `TestDataFactory`
to generate them instead of a shared fixture. See `config.properties` vs `testdata/` for the
config-vs-data split.

## Reporting

Every `@Test` carries `@Epic`/`@Feature`/`@Story`/`@Severity`/`@Description` so the Allure
report reads as a requirements-traceable document, not a flat pass/fail list. Page-object action
methods are annotated `@Step`, so a failure's report shows exactly which UI step broke. On any
failure, `AllureTestListener` attaches a screenshot and the page source automatically.
`AllureEnvironmentWriter` records the browser/OS/URL used for the run, and
`allure/categories.json` buckets failures (application defect vs. locator/UI change vs.
environment issue) instead of leaving them all as generic "failed".

## CI/CD

`.github/workflows/ci.yml` runs the suite headless on every push/PR to `main`, uploads
`allure-results` as a build artifact, and publishes the rendered report to GitHub Pages
(`gh-pages` branch - enable Pages once under repo Settings for the live link to work).

## Review checklist

Before calling this done, cross-check against the test plan:
- [ ] Every planned test case has automated coverage, or an explicit documented reason it doesn't
- [ ] Every acceptance criterion in the two user stories maps to at least one `@Story`
- [ ] Grading rubric items are all satisfied: test plan, automation (setup/POM/test data/CI/Docker), Allure report
