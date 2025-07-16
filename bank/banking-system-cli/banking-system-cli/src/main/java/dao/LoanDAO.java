package dao;

import model.Loan;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    // Insert new loan request
    public boolean insertLoan(Loan loan) {
        String sql = "INSERT INTO loans (user_id, amount, type, status, start_date, duration_months, emi, remaining_amount) " +
                     "VALUES (?, ?, ?, 'PENDING', ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loan.getUserId());
            stmt.setDouble(2, loan.getAmount());
            stmt.setString(3, loan.getType());
            stmt.setDate(4, Date.valueOf(loan.getStartDate()));
            stmt.setInt(5, loan.getDurationMonths());
            stmt.setDouble(6, loan.getEmi());
            stmt.setDouble(7, loan.getRemainingAmount());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error inserting loan: " + e.getMessage());
            return false;
        }
    }

    // Get all approved loans for a user
    public List<Loan> getApprovedLoansByUserId(int userId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE user_id = ? AND status = 'APPROVED' AND remaining_amount > 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                loans.add(mapRowToLoan(rs));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching approved loans: " + e.getMessage());
        }
        return loans;
    }

    // Update remaining amount after EMI/repayment
    public boolean updateRemainingAmount(int loanId, double newRemaining) {
        String sql = "UPDATE loans SET remaining_amount = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newRemaining);
            stmt.setInt(2, loanId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error updating remaining loan amount: " + e.getMessage());
            return false;
        }
    }

    // Fetch all loans (admin)
    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                loans.add(mapRowToLoan(rs));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching all loans: " + e.getMessage());
        }
        return loans;
    }

    // Admin approval or rejection
    public boolean updateStatus(int loanId, String status) {
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

    // Map ResultSet row to Loan model
    private Loan mapRowToLoan(ResultSet rs) throws SQLException {
        LocalDate startDate = rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null;
        return new Loan(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getDouble("amount"),
                rs.getString("type"),
                rs.getString("status"),
                startDate,
                rs.getInt("duration_months"),
                rs.getDouble("emi"),
                rs.getDouble("remaining_amount")
        );
    }
}
