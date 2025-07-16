package model;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private int accountId;
    private String type;
    private double amount;
    private LocalDateTime timestamp;

    public Transaction(int id, int accountId, String type, double amount, LocalDateTime timestamp) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
