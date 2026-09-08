# Test Plan: Bank Manager (User Story 1)

## 1. Introduction

Defines the test scope, approach, environment, and entry/exit criteria for the Bank Manager functionality of the XYZ Bank App. Test cases trace to every User Story 1 acceptance criterion and are managed in Jira via the Xray plugin.

## 2. Project Overview

The XYZ Bank App serves two roles: customers and bank managers. This plan covers the manager role: adding customers, creating accounts, and deleting accounts.

> **User Story 1:** As a Bank Manager, I want to add customers, create accounts, and delete accounts, so that I can manage customer accounts efficiently.

Testing runs against the live site in Chrome. Each test case is executed step by step; the on-screen result is recorded as the actual result, and any deviation from an acceptance criterion is raised as a defect.

## 3. Test Objectives

- Verify every User Story 1 acceptance criterion against the live site's behavior.
- Produce reproducible test cases another tester can rerun and get the same result.
- Validate the two explicit input constraints: names accept letters only; postal codes accept digits only.
- Confirm account-provisioning rules: an account can only be opened for an existing customer, and a deleted account is no longer accessible.
- Weight coverage toward negative and edge cases, where defects are more likely to surface.
- Trace every acceptance criterion to at least one Xray test case, and produce a Requirement Traceability Matrix.
- Log defects as Jira bugs (repro steps, expected/actual result, severity, priority) linked to their Xray test and requirement.
- Use Jira and Xray as the single system for test design, execution, traceability, and defect tracking.

## 4. Scope

### 4.1 In Scope

- **Add Customer**: form validation (First Name, Last Name, Post Code), letters-only/numeric-only enforcement, the Customers list and search after adding, and the confirmation dialog.
- **Open Account**: account creation for an existing customer, currency selection, the returned account number, and rejection when the customer doesn't exist.
- **Delete Customer/Account**: removal from the list, and confirming the customer can no longer log in to or view a deleted account.
- Negative and edge testing of every Add Customer / Open Account field, using equivalence partitioning, boundary value analysis, and negative-input design.

### 4.2 Out of Scope

- **Customer-side functionality** (transactions, deposits, withdrawals): covered by the Customer test plan (User Story 2).
- **Authentication/session security**: login is dropdown-only, no password; only dropdown and account-gating behavior is tested.
- **Load, stress, and scalability**: no performance target is specified, so only a single observed load time is recorded.
- **Real currency conversion/settlement**: currency selection is checked for presence and behavior only.
- **Backend, database, and API-level verification**: only UI-observable outcomes are checked.
- **Cross-browser/mobile**: testing is Chrome, desktop only.

## 5. Test Strategy

### 5.1 Testing Types

| Type | Description |
|---|---|
| Functional | Checks each User Story 1 acceptance criterion against the live site; the main body of testing. |
| Positive | Confirms valid input (well-formed name, numeric postal code, existing customer) is accepted. |
| Negative | Confirms invalid input is rejected: digits in a name, letters in a postal code, or an account request for a nonexistent customer. |
| Edge | Covers boundaries and unusual conditions: blank fields, oversized input, duplicate customers, deleting a customer's only account. |
| Usability | Confirms confirmation dialogs are clear and don't block the next action. |
| Security (input/data) | Confirms a customer can't access an account before creation or after deletion. |
| Regression | Reruns test cases after fixes; each retest is tracked against its Xray Test Execution. |

### 5.2 Test Design Techniques

Test cases derive from each acceptance criterion using:

- **Equivalence partitioning**: valid vs. invalid name characters and postal codes.
- **Boundary value analysis**: blank fields, maximum field length.
- **Negative-input design.**
- **State-based design** for the account lifecycle: Not Created, then Created, then Deleted, with customer access checked at each state.

## 6. Test Environment and Tools

| Item | Detail |
|---|---|
| Application | [XYZ Bank App](https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login) |
| Browser | Chrome |
| OS | Windows (WSL2) |
| Network | Standard connection, no throttling |
| Test management | Jira + Xray (Test, Test Plan, Test Execution, Bug issue types) |
| Data | Dummy customer/account data only; no real personal or financial data |

> The automated regression suite (Selenium/TestNG) runs separately in Docker and GitHub Actions on Ubuntu Linux. That CI environment is distinct from the manual testing machine described above.

## 7. Test Deliverables

| Deliverable | Description |
|---|---|
| Test Case (Xray Test) | Identifier, linked requirement, scenario, data, steps, expected result, type. |
| Test Plan / Test Execution (Xray) | Groups Bank Manager tests per cycle; records Pass/Fail/Blocked per execution. |
| Requirement Traceability Matrix | Maps each acceptance criterion to its test cases, execution status, and linked defects. |
| Bug Report (Jira Bug) | Steps, expected/actual result, environment, severity, priority, status, assignee, linked to its test. |

## 8. Entry and Exit Criteria

**Entry**

- User Story 1 and its acceptance criteria are approved.
- The application is reachable in the test environment.
- The Jira/Xray Test Plan for Bank Manager is set up.

**Exit**

- Every planned test case has run at least once.
- Every acceptance criterion traces to at least one test case in the Traceability Matrix.
- Every defect has a severity and priority.
- No open Critical/High defect remains without a documented mitigation or retest plan.

## 9. Test Schedule

| Phase | Timeline | Coverage | Notes |
|---|---|---|---|
| Planning and design | Day 1 | User Story 1 | Test cases authored in Xray before execution starts. |
| Execution | Day 2 | Add Customer, Open Account, Delete Account | Runs first; the Customer story depends on accounts created here. |
| Defect logging and traceability | Day 2 | All Bank Manager areas | Each defect logged and linked to its Xray test as found. |
| Reporting and sign-off | Day 3 | Full cycle | Traceability Matrix generated; unexpected results re-checked by hand before sign-off. |

## 10. Defect Management

Defects are logged as Jira Bugs linked to their Xray test: identifier, description, repro steps, expected/actual result, environment, severity, priority, status, assignee. Lifecycle: New, then In Progress, then Fixed, then Retest, then Closed (or Reopened if the retest fails).

### 10.1 Severity

| Level | Definition |
|---|---|
| Critical | App unusable, crashes, or allows unauthorized account access, with no workaround. |
| High | A core function (add customer, open/delete account) fails, possibly with no workaround. |
| Medium | A function is wrong but has a workaround, or affects a secondary feature (e.g. customer list display). |
| Low | Cosmetic/usability issue with little effect on completing a task. |

### 10.2 Priority

| Level | Definition |
|---|---|
| High | Fix before next release; affects provisioning or input validation. |
| Medium | Fix in an upcoming release; a workaround exists. |
| Low | Schedule at convenience; cosmetic/usability. |

## 11. Risks and Mitigations

| Risk | Mitigation |
|---|---|
| GlobalSQA is a shared public demo, so data created by one tester can be changed or deleted by another between runs. | Use a timestamped naming convention for test customers; record state at execution time; re-verify state-dependent cases before sign-off. |
| Neither role requires a password, so anyone can act as manager. | Treat as an accepted demo limitation, not a defect; authentication strength is out of scope. |
| No visible persistent backend, so a refresh or restart can lose session state. | Complete dependent test cases within one continuous session; rerun setup if state is missing. |
| Testing is UI-only, so backend-only defects can go undetected. | Watch the browser console for errors during execution; state plainly that backend/DB layers weren't tested directly. |
| One person designed, executed, and judged every test case. | Trace every case to its acceptance criterion in the Traceability Matrix; submit for stakeholder review before sign-off. |

## 12. Traceability Matrix

Maps each User Story 1 acceptance criterion to the automated test that covers it (`src/test/java/com/xyzbank/tests/manager`). Requirement: [TMLD3-2](https://amali-tech.atlassian.net/browse/TMLD3-2). Automated coverage is grouped under the Xray Test Set [TMLD3-79](https://amali-tech.atlassian.net/browse/TMLD3-79).

| Acceptance Criterion | Automated Test | Status | Jira Key |
|---|---|---|---|
| Bank managers can add new customers | `AddCustomerTest.shouldAddCustomerSuccessfully` | Passing | [TMLD3-79](https://amali-tech.atlassian.net/browse/TMLD3-79) |
| Customer names must be alphabetic only | `AddCustomerTest.shouldRejectInvalidCustomer` | **Failing: known defect, app does not enforce this** | [TMLD3-88](https://amali-tech.atlassian.net/browse/TMLD3-88) |
| Postal codes must be numeric only | `AddCustomerTest.shouldRejectInvalidCustomer` | **Failing: known defect, app does not enforce this** | [TMLD3-88](https://amali-tech.atlassian.net/browse/TMLD3-88) |
| Bank managers can create accounts for existing customers | `CreateAccountTest.shouldCreateAccountForExistingCustomer` | Passing | [TMLD3-79](https://amali-tech.atlassian.net/browse/TMLD3-79) |
| Customers cannot access an account until a manager creates one | `CreateAccountTest.shouldBlockCustomerAccessBeforeAccountIsCreated` | Passing | [TMLD3-79](https://amali-tech.atlassian.net/browse/TMLD3-79) |
| Bank managers can delete customer accounts | `DeleteAccountTest.shouldDeleteCustomerAndRevokeAccess` | Passing | [TMLD3-79](https://amali-tech.atlassian.net/browse/TMLD3-79) |
| A deleted account is no longer accessible to that customer | `DeleteAccountTest.shouldDeleteCustomerAndRevokeAccess` | Passing | [TMLD3-79](https://amali-tech.atlassian.net/browse/TMLD3-79) |
| Blank required fields are rejected (edge case, not in acceptance criteria) | `AddCustomerTest.shouldRejectInvalidCustomer` | Passing | [TMLD3-79](https://amali-tech.atlassian.net/browse/TMLD3-79) |
| The Customers list search filters correctly (in scope, not in acceptance criteria) | `AddCustomerTest.shouldFilterCustomerListBySearch` | Passing | [TMLD3-79](https://amali-tech.atlassian.net/browse/TMLD3-79) |
| An oversized alphabetic name is not truncated (edge case, not in acceptance criteria) | `AddCustomerTest.shouldAcceptOversizedAlphabeticName` | Passing | [TMLD3-79](https://amali-tech.atlassian.net/browse/TMLD3-79) |
| Duplicate customer submissions are rejected (edge case, not in acceptance criteria) | `AddCustomerTest.shouldRejectDuplicateCustomerSubmission` | Passing | [TMLD3-79](https://amali-tech.atlassian.net/browse/TMLD3-79) |
