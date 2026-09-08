# Test Plan: Customer (User Story 2)

## 1. Introduction

Defines the test scope, approach, environment, and entry/exit criteria for the Customer functionality of the XYZ Bank App. Test cases trace to every User Story 2 acceptance criterion and are managed in Jira via the Xray plugin.

## 2. Project Overview

This plan covers the customer role: viewing transactions, depositing funds, and withdrawing money against an account a manager has already created.

> **User Story 2:** As a Customer, I want to view my transactions, deposit funds, and withdraw money, so that I can manage my finances effectively.

Testing runs against the live site in Chrome. Each test case is executed step by step; the on-screen result is recorded as the actual result, and any deviation from an acceptance criterion is raised as a defect.

## 3. Test Objectives

- Verify every User Story 2 acceptance criterion against the live site's behavior.
- Produce reproducible test cases another tester can rerun and get the same result.
- Validate the explicit input constraints: deposit/withdrawal amounts must be positive; withdrawals are additionally bounded by the available balance.
- Confirm the User Story 1 dependency: a customer can't access or transact on an account until a manager creates it, and a deleted account is no longer accessible.
- Confirm transaction integrity: no UI path lets a customer reset or alter their transaction history.
- Weight coverage toward negative and edge cases, where defects are more likely to surface.
- Trace every acceptance criterion to at least one Xray test case, and produce a Requirement Traceability Matrix.
- Log defects as Jira bugs (repro steps, expected/actual result, severity, priority) linked to their Xray test and requirement.
- Use Jira and Xray as the single system for test design, execution, traceability, and defect tracking.

## 4. Scope

### 4.1 In Scope

- **Login/account access**: selecting a name and account from the login dropdowns; an account not yet created isn't selectable.
- **Transactions**: the transaction list's content and ordering after new activity.
- **Deposit**: field validation (positive, blank, zero, negative, non-numeric, decimal) and the resulting balance update.
- **Withdraw**: field validation (positive, blank, zero, negative, non-numeric, decimal, exceeding balance) and the resulting balance update.
- **Transaction integrity**: whether any UI path lets a customer reset, edit, or delete their history.
- Negative and edge testing of every Customer field, using equivalence partitioning, boundary value analysis, and negative-input design.

### 4.2 Out of Scope

- **Manager-side functionality** (adding customers, opening/deleting accounts): covered by the Bank Manager test plan (User Story 1); an account is assumed to already exist.
- **Authentication/session security**: login is dropdown-only, no password; only dropdown behavior is tested.
- **Load, stress, and scalability**: no performance target is specified, so only a single observed load time is recorded.
- **Real currency conversion/settlement**: currency is checked for display behavior only.
- **Backend, database, and API-level verification**: only UI-observable outcomes are checked.
- **Cross-browser/mobile**: testing is Chrome, desktop only.

## 5. Test Strategy

### 5.1 Testing Types

| Type | Description |
|---|---|
| Functional | Checks each User Story 2 acceptance criterion against the live site; the main body of testing. |
| Positive | Confirms a valid deposit/withdrawal (positive, within balance) is accepted. |
| Negative | Confirms invalid input is rejected: blank, zero, negative, non-numeric, decimal amounts, or a withdrawal exceeding balance. |
| Edge | Covers boundaries: the smallest positive unit, withdrawing the exact balance, withdrawing one unit over. |
| Usability | Confirms success/validation messages are clear and don't block the next action. |
| Security (input/data) | Confirms account access is gated correctly, and no UI path resets or alters transaction history. |
| Regression | Reruns test cases after fixes; each retest is tracked against its Xray Test Execution. |

### 5.2 Test Design Techniques

Test cases derive from each acceptance criterion using:

- **Equivalence partitioning**: valid vs. invalid amounts.
- **Boundary value analysis**: zero, smallest positive amount, exact balance, one unit above balance.
- **Decision-table design** for the withdrawal rule, which depends on both amount and current balance.
- **State-based design** for the manager-then-customer dependency: an account must be Created before a customer can select it at login.

## 6. Test Environment and Tools

| Item | Detail |
|---|---|
| Application | [XYZ Bank App](https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login) |
| Browser | Chrome |
| OS | Windows (WSL2) |
| Network | Standard connection, no throttling |
| Test management | Jira + Xray (Test, Test Plan, Test Execution, Bug issue types) |
| Data | A Customer view is tested against an account created beforehand in the Manager view; dummy data only. |

> The automated regression suite (Selenium/TestNG) runs separately in Docker and GitHub Actions on Ubuntu Linux. That CI environment is distinct from the manual testing machine described above.

## 7. Test Deliverables

| Deliverable | Description |
|---|---|
| Test Case (Xray Test) | Identifier, linked requirement, scenario, data, steps, expected result, type. |
| Test Plan / Test Execution (Xray) | Groups Customer tests per cycle; records Pass/Fail/Blocked per execution. |
| Requirement Traceability Matrix | Maps each acceptance criterion to its test cases, execution status, and linked defects. |
| Bug Report (Jira Bug) | Steps, expected/actual result, environment, severity, priority, status, assignee, linked to its test. |

## 8. Entry and Exit Criteria

**Entry**

- User Story 2 and its acceptance criteria are approved.
- The application is reachable in the test environment.
- A manager-created customer account exists to transact against.
- The Jira/Xray Test Plan for Customer is set up.

**Exit**

- Every planned test case has run at least once.
- Every acceptance criterion traces to at least one test case in the Traceability Matrix.
- Every defect has a severity and priority.
- No open Critical/High defect remains without a documented mitigation or retest plan.

## 9. Test Schedule

| Phase | Timeline | Coverage | Notes |
|---|---|---|---|
| Planning and design | Day 1 | User Story 2 | Test cases authored in Xray before execution starts. |
| Execution | Day 2 to 3 | View Transactions, Deposit, Withdraw | Most validation-heavy area; runs after the Bank Manager cycle since it depends on a manager-created account. |
| Defect logging and traceability | Day 3 | All Customer areas | Each defect logged and linked to its Xray test as found. |
| Reporting and sign-off | Day 3 | Full cycle | Traceability Matrix generated; unexpected results re-checked by hand before sign-off. |

## 10. Defect Management

Defects are logged as Jira Bugs linked to their Xray test: identifier, description, repro steps, expected/actual result, environment, severity, priority, status, assignee. Lifecycle: New, then In Progress, then Fixed, then Retest, then Closed (or Reopened if the retest fails).

### 10.1 Severity

| Level | Definition |
|---|---|
| Critical | App unusable, crashes, produces an incorrect balance, or allows unauthorized account access, with no workaround. |
| High | A core function (deposit/withdraw) fails, possibly with no workaround. |
| Medium | A function is wrong but has a workaround, or affects a secondary feature (e.g. transaction list display). |
| Low | Cosmetic/usability issue with little effect on completing a task. |

### 10.2 Priority

| Level | Definition |
|---|---|
| High | Fix before next release; affects balance accuracy or input validation. |
| Medium | Fix in an upcoming release; a workaround exists. |
| Low | Schedule at convenience; cosmetic/usability. |

## 11. Risks and Mitigations

| Risk | Mitigation |
|---|---|
| GlobalSQA is a shared public demo, so data created by one tester can be changed or deleted by another between runs. | Use a timestamped naming convention for test customers; record state at execution time; re-verify state-dependent cases before sign-off. |
| Neither role requires a password, so anyone can select any customer's account. | Treat as an accepted demo limitation, not a defect; authentication strength is out of scope. |
| No visible persistent backend, so a refresh or restart can lose account/transaction state. | Complete each dependent case (manager creates account, then customer transacts) within one session; rerun setup if state is missing. |
| Testing is UI-only, so backend/data-store defects can go undetected. | Watch the browser console for errors during execution; state plainly that backend/DB layers weren't tested directly. |
| One person designed, executed, and judged every test case. | Trace every case to its acceptance criterion in the Traceability Matrix; submit for stakeholder review before sign-off. |

## 12. Traceability Matrix

Maps each User Story 2 acceptance criterion to the automated test that covers it (`src/test/java/com/xyzbank/tests/customer`). Requirement: [TMLD3-4](https://amali-tech.atlassian.net/browse/TMLD3-4). Automated coverage is grouped under the Xray Test Set [TMLD3-99](https://amali-tech.atlassian.net/browse/TMLD3-99).

| Acceptance Criterion | Automated Test | Status | Jira Key |
|---|---|---|---|
| Customers can view a list of recent transactions | `ViewTransactionsTest.shouldListTransactionAfterDeposit` | Passing | [TMLD3-99](https://amali-tech.atlassian.net/browse/TMLD3-99) |
| Deposit amount is entered and validated as a positive value | `DepositTest.shouldHandleDepositAmount` | Passing | [TMLD3-99](https://amali-tech.atlassian.net/browse/TMLD3-99) |
| A successful deposit updates the account balance | `DepositTest.shouldHandleDepositAmount` | Passing | [TMLD3-99](https://amali-tech.atlassian.net/browse/TMLD3-99) |
| Withdrawal amount is entered and validated as positive and within balance | `WithdrawTest.shouldHandleWithdrawalAmount` | Passing | [TMLD3-99](https://amali-tech.atlassian.net/browse/TMLD3-99) |
| A successful withdrawal updates the account balance | `WithdrawTest.shouldHandleWithdrawalAmount` | Passing | [TMLD3-99](https://amali-tech.atlassian.net/browse/TMLD3-99) |
| Customers cannot reset or alter their transaction history | `ViewTransactionsTest.shouldNotExposeTransactionResetControl` | Passing | [TMLD3-99](https://amali-tech.atlassian.net/browse/TMLD3-99) |
| Non-numeric deposit/withdrawal amounts (edge case, not in acceptance criteria) | `DepositTest.shouldHandleDepositAmount`, `WithdrawTest.shouldHandleWithdrawalAmount` | Passing | [TMLD3-99](https://amali-tech.atlassian.net/browse/TMLD3-99) |
