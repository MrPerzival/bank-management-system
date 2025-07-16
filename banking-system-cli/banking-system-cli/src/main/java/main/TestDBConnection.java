package main;

import java.sql.Connection;

import dao.DBConnection;

public class TestDBConnection {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("✅ Connected to the database successfully!");
        } else {
            System.out.println("❌ Failed to connect to the database.");
        }
    }
}
