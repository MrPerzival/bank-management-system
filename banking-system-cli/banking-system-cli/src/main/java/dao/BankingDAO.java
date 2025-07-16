package dao;

import model.User;
import model.Account;
import model.Loan;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankingDAO {

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (name, email, password, pan_or_aadhaar) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getIdProof());  // ✅ fixed here
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error registering user: " + e.getMessage());
            return false;
        }
    }



    // ✅ Login user
    public User loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("pan_or_aadhaar")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Error during login: " + e.getMessage());
        }
        return null;
    }

    // ✅ Create account for user
    public boolean createAccount(int userId) {
        String sql = "INSERT INTO accounts (user_id, balance, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, 0.0);
            stmt.setString(3, "ACTIVE");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error creating account: " + e.getMessage());
            return false;
        }
    }

    // ✅ Get account by user ID
    public Account getAccountByUserId(int userId) {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDouble("balance"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching account: " + e.getMessage());
        }
        return null;
    }

    // ✅ Deposit
    public boolean deposit(int accountId, double amount) {
        String updateSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
        String insertTxn = "INSERT INTO transactions (account_id, type, amount, timestamp) VALUES (?, 'DEPOSIT', ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement txnStmt = conn.prepareStatement(insertTxn)) {
            conn.setAutoCommit(false);
            updateStmt.setDouble(1, amount);
            updateStmt.setInt(2, accountId);
            txnStmt.setInt(1, accountId);
            txnStmt.setDouble(2, amount);
            txnStmt.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            updateStmt.executeUpdate();
            txnStmt.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Deposit failed: " + e.getMessage());
            return false;
        }
    }

    // ✅ Withdraw
    public boolean withdraw(int accountId, double amount) {
        String checkSql = "SELECT balance FROM accounts WHERE id = ?";
        String updateSql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        String insertTxn = "INSERT INTO transactions (account_id, type, amount, timestamp) VALUES (?, 'WITHDRAW', ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement txnStmt = conn.prepareStatement(insertTxn)) {

            checkStmt.setInt(1, accountId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getDouble("balance") >= amount) {
                conn.setAutoCommit(false);
                updateStmt.setDouble(1, amount);
                updateStmt.setInt(2, accountId);
                txnStmt.setInt(1, accountId);
                txnStmt.setDouble(2, amount);
                txnStmt.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
                updateStmt.executeUpdate();
                txnStmt.executeUpdate();
                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Withdraw failed: " + e.getMessage());
        }
        return false;
    }

    // ✅ Transfer
    public boolean transfer(int fromAccountId, int toAccountId, double amount) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            if (withdraw(fromAccountId, amount) && deposit(toAccountId, amount)) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            System.out.println("❌ Transfer failed: " + e.getMessage());
        }
        return false;
    }

    // ✅ Apply for loan
    public boolean applyLoan(Loan loan) {
        String sql = "INSERT INTO loans (user_id, amount, type, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loan.getUserId());
            stmt.setDouble(2, loan.getAmount());
            stmt.setString(3, loan.getType());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error applying for loan: " + e.getMessage());
            return false;
        }
    }

    // ✅ Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("pan_or_aadhaar")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching users: " + e.getMessage());
        }
        return users;
    }

    // ✅ Get all loan applications
    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                loans.add(new Loan(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDouble("amount"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                        rs.getInt("duration_months"),
                        rs.getDouble("emi"),
                        rs.getDouble("remaining_amount")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching loans: " + e.getMessage());
        }
        return loans;
    }

    // ✅ Update account status (Admin)
    public boolean updateAccountStatus(int accountId, String status) {
        String sql = "UPDATE accounts SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, accountId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error updating account status: " + e.getMessage());
            return false;
        }
    }

    // ✅ Approve or Reject Loan (Admin)
    public boolean updateLoanStatus(int loanId, String status) {
        String sql = "UPDATE loans SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, loanId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error updating loan status: " + e.getMessage());
            return false;
        }
    }
}
