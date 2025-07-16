package model;

public class Account {
    private int id;
    private int userId;
    private double balance;
    private String status;

    public Account(int id, int userId, double balance, String status) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
