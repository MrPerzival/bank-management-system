package dao;

import model.Transaction;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void insertTransaction(Transaction txn) {
        String sql = "INSERT INTO transactions (account_id, type, amount, timestamp) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, txn.getAccountId());
            stmt.setString(2, txn.getType());
            stmt.setDouble(3, txn.getAmount());
            stmt.setTimestamp(4, Timestamp.valueOf(txn.getTimestamp()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getTransactionsByAccountId(int accId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY timestamp DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Transaction(
                        rs.getInt("id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
