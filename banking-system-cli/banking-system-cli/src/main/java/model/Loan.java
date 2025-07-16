package model;

import java.time.LocalDate;

public class Loan {
    private int id;
    private int userId;
    private double amount;
    private String type;
    private String status;
    private LocalDate startDate;
    private int durationMonths;
    private double emi;
    private double remainingAmount;

    public Loan(int id, int userId, double amount, String type, String status,
                LocalDate startDate, int durationMonths, double emi, double remainingAmount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.startDate = startDate;
        this.durationMonths = durationMonths;
        this.emi = emi;
        this.remainingAmount = remainingAmount;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public double getEmi() {
        return emi;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 
