# 🏦 Bank Management System (Java CLI)

This is a **Java-based Command-Line Interface (CLI) Bank Management System** that simulates key banking functionalities such as user registration, fund transfers, and loan management. It uses **MySQL** for data persistence and **JDBC** for database access, following object-oriented design principles.

## 📌 Features

### 👤 User Functions
- Register and login with secure credentials
- View current account balance and status
- Deposit and withdraw money
- Transfer funds to other accounts
- Apply for loans (Regular & Balloon type)
- View formatted transaction history
- Auto EMI deduction for regular loans on login
- Manual EMI repayment
- Logout and exit

### 🛠️ Admin Functions
- Admin login  
  `Username:` `Banked`  
  `Password:` `ProjectDone`
- View list of all users
- View and manage loan applications
- Approve or reject loans
- Update account status (`ACTIVE`, `BLOCKED`, `SUSPICIOUS`)
- Logout and exit

## 🧭 Planned Features
- PDF generation for statements
- JavaFX/Swing GUI interface
- Email notifications for events like loan approval
- Interest calculation on savings
- Fixed Deposits and complaint resolution

---

## 🗃️ Database Schema (MySQL)

### `users`
| Column | Type | Description |
|--------|------|-------------|
| id | INT, PK | Unique user ID |
| name | VARCHAR | User name |
| email | VARCHAR (UNIQUE) | Login email |
| password | VARCHAR | Encrypted |
| pan_or_aadhaar | VARCHAR | KYC document |

### `accounts`
| Column | Type | Description |
|--------|------|-------------|
| id | INT, PK | Account ID |
| user_id | INT, FK | Linked user |
| balance | DOUBLE | Current balance |
| status | VARCHAR | ACTIVE / BLOCKED / SUSPICIOUS |

### `transactions`
| Column | Type | Description |
|--------|------|-------------|
| id | INT, PK | Transaction ID |
| account_id | INT, FK | Linked account |
| type | VARCHAR | Deposit / Withdraw / Transfer |
| amount | DOUBLE | Transaction amount |
| timestamp | TIMESTAMP | Transaction time |

### `loans`
| Column | Type | Description |
|--------|------|-------------|
| id | INT, PK | Loan ID |
| user_id | INT, FK | Borrower |
| amount | DOUBLE | Loan amount |
| type | VARCHAR | Regular / Balloon |
| status | VARCHAR | Approved / Pending / Rejected |
| start_date | DATE | Start date |
| duration_months | INT | Loan term |
| emi | DOUBLE | Calculated EMI |
| remaining_amount | DOUBLE | Remaining balance |

---

## 🧮 Key Calculations

### 💸 EMI (Equated Monthly Installment)
```math
EMI = [P × R × (1 + R)^N] / [(1 + R)^N – 1]
P = Principal loan amount

R = Monthly interest rate

N = Loan term in months

🎈 Balloon Repayment
Partial payments in early months

Large lump-sum final payment

Interest recalculated monthly on remaining principal

🚀 Getting Started
🔧 Prerequisites
Java JDK 8 or later

MySQL Server

MySQL JDBC Connector

IDE (Eclipse/IntelliJ) or Terminal

🧰 Setup Steps
Clone the Repository
git clone https://github.com/MrPerzival/bank-management-system.git
cd bank-management-system
Create the MySQL Database

Import the SQL schema file from schema.sql (if provided) or create tables as per schema above.

Configure Database Connection

Edit the database credentials in DBConnection.java:
private static final String URL = "jdbc:mysql://localhost:3306/YOUR_DB";
private static final String USER = "root";
private static final String PASSWORD = "your_password";

Compile and Run
javac *.java
java Main

🛠 Technologies Used
Java (Object-Oriented Programming)

JDBC (Java Database Connectivity)

MySQL (Relational Database)

Git & GitHub (Version Control)
