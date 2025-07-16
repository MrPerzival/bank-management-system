package service;

import dao.*;
import model.*;
import model.Account;
import model.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BankingService {
    private final UserDAO userDAO = new UserDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final LoanDAO loanDAO = new LoanDAO();

    public boolean registerUser(String name, String email, String password, String idProof) {
        int userId = userDAO.insertUser(name, email, password, idProof);
        return userId > 0 && accountDAO.createAccountForUser(userId);
    }

    public User loginUser(String email, String password) {
        return userDAO.validateUser(email, password);
    }

    public Account getAccountByUserId(int userId) {
        return accountDAO.getAccountByUserId(userId);
    }

    public boolean deposit(int accId, double amount) {
        boolean result = accountDAO.deposit(accId, amount);
        if (result) {
            transactionDAO.insertTransaction(new Transaction(0, accId, "DEPOSIT", amount, LocalDateTime.now()));
        }
        return result;
    }

    public boolean withdraw(int accId, double amount) {
        boolean result = accountDAO.withdraw(accId, amount);
        if (result) {
            transactionDAO.insertTransaction(new Transaction(0, accId, "WITHDRAW", amount, LocalDateTime.now()));
        }
        return result;
    }

    public boolean transfer(int fromAccId, int toAccId, double amount) {
        if (withdraw(fromAccId, amount)) {
            if (deposit(toAccId, amount)) {
                transactionDAO.insertTransaction(new Transaction(0, fromAccId, "TRANSFER_OUT", amount, LocalDateTime.now()));
                transactionDAO.insertTransaction(new Transaction(0, toAccId, "TRANSFER_IN", amount, LocalDateTime.now()));
                return true;
            } else {
                deposit(fromAccId, amount); // rollback withdraw
            }
        }
        return false;
    }

    public boolean applyLoan(int userId, double amount, String type, int duration) {
        Account acc = accountDAO.getAccountByUserId(userId);
        if (acc == null) return false;

        double emi = (type.equals("REGULAR")) ? amount / duration : 0;
        Loan loan = new Loan(0, userId, amount, type, "PENDING",
                LocalDate.now(), duration, emi, amount);
        return loanDAO.insertLoan(loan);
    }

    public boolean updateLoanStatus(int loanId, String status) {
        return loanDAO.updateStatus(loanId, status);
    }

    public boolean updateAccountStatus(int accId, String status) {
        return accountDAO.updateStatus(accId, status);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public List<Loan> getAllLoans() {
        return loanDAO.getAllLoans();
    }

    public List<Transaction> getAccountHistory(int accId) {
        return transactionDAO.getTransactionsByAccountId(accId);
    }

    public void processAutoEMI(int userId) {
        List<Loan> loans = loanDAO.getApprovedLoansByUserId(userId);
        Account acc = accountDAO.getAccountByUserId(userId);

        for (Loan loan : loans) {
            if (loan.getType().equals("REGULAR") && loan.getRemainingAmount() > 0) {
                boolean deducted = withdraw(acc.getId(), loan.getEmi());
                if (deducted) {
                    double newRemaining = loan.getRemainingAmount() - loan.getEmi();
                    loanDAO.updateRemainingAmount(loan.getId(), newRemaining);
                    transactionDAO.insertTransaction(new Transaction(0, acc.getId(), "EMI_PAYMENT", loan.getEmi(), LocalDateTime.now()));
                }
            }
        }
    }

    public boolean repayLoan(int accId, int loanId, double amount) {
        Account acc = accountDAO.getAccountById(accId);
        if (acc == null || acc.getBalance() < amount) return false;

        boolean deducted = withdraw(accId, amount);
        if (!deducted) return false;

        List<Loan> allLoans = loanDAO.getAllLoans();
        for (Loan loan : allLoans) {
            if (loan.getId() == loanId && loan.getRemainingAmount() > 0) {
                double newRem = loan.getRemainingAmount() - amount;
                if (newRem < 0) newRem = 0;
                loanDAO.updateRemainingAmount(loanId, newRem);
                transactionDAO.insertTransaction(new Transaction(0, accId, "LOAN_REPAY", amount, LocalDateTime.now()));
                return true;
            }
        }
        return false;
    }
}
