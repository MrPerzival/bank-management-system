# 🏦 CLI-Based Banking Management System in Java

This is a **Java-based CLI Banking Management System** that simulates core functionalities of a real-world banking application. It supports user and admin roles, enables secure transactions, and is integrated with a relational database (MySQL) using JDBC. Designed to demonstrate object-oriented programming, database integration, and financial operations such as loans and fund transfers.

---

## 📌 Features

### 👤 User Features
- Register and log in securely using email and password
- View account balance and current account status
- Deposit and withdraw funds
- Transfer money to other registered users
- Apply for loans (Regular and Balloon repayment types)
- View formatted transaction history
- Repay loans manually
- Auto EMI deduction for active regular loans at login
- Logout and Exit functionality

### 🛠️ Admin Features
- Admin login using default credentials  
  `Username:` `Banked`  
  `Password:` `ProjectDone`
- View list of all registered users
- View and manage all loan applications
- Update account status: `ACTIVE`, `BLOCKED`, or `SUSPICIOUS`
- Approve or reject user loan requests
- Logout and Exit

---

## 🚧 Planned Features (Future Scope)
- PDF generation for statements and transaction history
- GUI implementation using Java Swing/JavaFX
- Email notifications for approvals and alerts
- Interest calculation on deposits and outstanding loans
- Support for Fixed Deposits and customer complaint management

---

## 🗃️ Database Schema (MySQL)

### `users`
| Field | Type | Description |
|-------|------|-------------|
| id | INT (PK) | Unique user ID |
| name | VARCHAR | Full name |
| email | VARCHAR | Unique email address |
| password | VARCHAR | Encrypted password |
| pan_or_aadhaar | VARCHAR | KYC verification |

### `accounts`
| Field | Type | Description |
|-------|------|-------------|
| id | INT (PK) | Account ID |
| user_id | INT (FK) | Link to `users.id` |
| balance | DOUBLE | Current balance |
| status | VARCHAR | `ACTIVE`, `BLOCKED`, or `SUSPICIOUS` |

### `transactions`
| Field | Type | Description |
|-------|------|-------------|
| id | INT (PK) | Transaction ID |
| account_id | INT (FK) | Linked account |
| type | VARCHAR | Deposit, Withdrawal, Transfer |
| amount | DOUBLE | Transaction amount |
| timestamp | TIMESTAMP | Date/time of transaction |

### `loans`
| Field | Type | Description |
|-------|------|-------------|
| id | INT (PK) | Loan ID |
| user_id | INT (FK) | Loan applicant |
| amount | DOUBLE | Loan amount |
| type | VARCHAR | `Regular`, `Balloon` |
| status | VARCHAR | `Approved`, `Pending`, `Rejected` |
| start_date | DATE | Loan start |
| duration_months | INT | Duration |
| emi | DOUBLE | EMI per month |
| remaining_amount | DOUBLE | Outstanding balance |

---

## 🔧 Tech Stack

- **Language**: Java (OOP principles)
- **Database**: MySQL
- **Database Access**: JDBC
- **IDE**: IntelliJ IDEA / Eclipse
- **Version Control**: Git

---

## 🧮 Calculation Logic

- **EMI (Equated Monthly Installment)**  
EMI = [P × R × (1 + R)^N] / [(1 + R)^N – 1]
Where:  
`P` = Loan Amount, `R` = Monthly Interest Rate, `N` = Number of Months

- **Balloon Loan Repayment**  
Flexible schedule with smaller early payments and one large final payment. Interest recalculates monthly.

---

## 🚀 Getting Started

### 🔄 Prerequisites
- Java 8 or later
- MySQL Server
- JDBC Connector for MySQL

### 🧰 Setup Instructions
1. **Clone this repository**
 ```bash
 git clone https://github.com/your-repo/banking-management-java.git
 cd banking-management-java
Import SQL schema into MySQL

Use banking_schema.sql from the project folder to create necessary tables.

Configure Database in Code

Update DB URL, username, and password in DBConnection.java.

Compile and Run
javac *.java
java Main
