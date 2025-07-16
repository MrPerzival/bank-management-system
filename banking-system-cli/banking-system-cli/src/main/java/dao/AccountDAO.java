package dao;

import model.Account;
import util.DBConnection;

import java.sql.*;

public class AccountDAO {

    public boolean createAccountForUser(int userId) {
        String sql = "INSERT INTO accounts (user_id, balance, status) VALUES (?, 0.0, 'ACTIVE')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Account getAccountByUserId(int userId) {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToAccount(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account getAccountById(int accId) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToAccount(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deposit(int accId, double amount) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, amount);
            stmt.setInt(2, accId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean withdraw(int accId, double amount) {
        String sql = "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, amount);
            stmt.setInt(2, accId);
            stmt.setDouble(3, amount);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBalance(int accId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatus(int accId, String status) {
        String sql = "UPDATE accounts SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, accId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Account mapRowToAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getDouble("balance"),
                rs.getString("status")
        );
    }
}
