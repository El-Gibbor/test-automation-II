# XYZ Bank - Selenium WebDriver Automation

Automated test suite for the [XYZ Bank demo app](https://www.globalsqa.com/angularJs-protractor/BankingProject/),
covering the Manager (add customer / create account / delete account) and Customer
(view transactions / deposit / withdraw) user stories. Built with **Java 17, Selenium WebDriver,
TestNG, and Allure Report**, following the Page Object Model.

## Test Plans

- [Bank Manager](test-plan-manager.md) - User Story 1
- [Customer](test-plan-customer.md) - User Story 2

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

Business data lives in `src/test/resources/testdata/*.json`, read into typed POJOs via
`TestDataReader` and served to tests through `DataProviders`. Fixed edge cases live in JSON;
data that must be unique per run (e.g. account creation) comes from `TestDataFactory` instead.

## Reporting

Tests carry `@Epic`/`@Feature`/`@Story`/`@Severity`/`@Description` so Allure reads as a
traceable document, not a flat pass/fail list. `AllureTestListener` attaches a screenshot and
page source on failure; `allure/categories.json` buckets failures by cause (app defect,
locator/UI change, environment issue).

## CI/CD

`.github/workflows/ci.yml` runs the suite on every push/PR to `main`, uploads `allure-results`
as a build artifact, and publishes the report to GitHub Pages.
