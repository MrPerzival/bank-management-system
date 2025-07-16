package main;

import model.*;
import service.BankingService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BankingService service = new BankingService();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==== Welcome to CLI Banking System ====");
            System.out.println("1. User Login\n2. User Register\n3. Admin Login\n4. Exit");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> userLogin();
                case 2 -> userRegister();
                case 3 -> {
                    if (validateAdmin()) {
                        System.out.println("✅ Admin login successful.");
                        adminMenu();
                    } else {
                        System.out.println("❌ Invalid admin credentials.");
                    }
                }
                case 4 -> {
                    System.out.println("Thank you. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static boolean validateAdmin() {
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();
        return username.equals("Banked") && password.equals("ProjectDone");
    }

    private static void userRegister() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter PAN or Aadhaar: ");
        String id = scanner.nextLine().toUpperCase();

        boolean success = service.registerUser(name, email, password, id);
        System.out.println(success ? "✅ Registration successful." : "❌ Registration failed.");
    }

    private static void userLogin() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = service.loginUser(email, password);
        if (user != null) {
            System.out.println("✅ Login successful. Welcome, " + user.getName());
            service.processAutoEMI(user.getId());
            userMenu(user);
        } else {
            System.out.println("❌ Login failed.");
        }
    }

    private static void userMenu(User user) {
        while (true) {
            System.out.println("\n==== User Menu ====");
            System.out.println("1. View Account\n2. Deposit\n3. Withdraw\n4. Transfer\n5. Apply Loan\n6. Logout\n7. View Account History\n8. Repay Loan (Manual)");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            Account acc = service.getAccountByUserId(user.getId());

            switch (choice) {
                case 1 -> System.out.println("Account ID: " + acc.getId() + ", Balance: " + acc.getBalance() + ", Status: " + acc.getStatus());
                case 2 -> {
                    System.out.print("Enter deposit amount: ");
                    double amt = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println(service.deposit(acc.getId(), amt) ? "✅ Deposit successful." : "❌ Failed to deposit.");
                }
                case 3 -> {
                    System.out.print("Enter withdraw amount: ");
                    double amt = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println(service.withdraw(acc.getId(), amt) ? "✅ Withdraw successful." : "❌ Failed to withdraw.");
                }
                case 4 -> {
                    System.out.print("Enter recipient account ID: ");
                    int toAcc = scanner.nextInt();
                    System.out.print("Enter amount: ");
                    double amt = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println(service.transfer(acc.getId(), toAcc, amt) ? "✅ Transfer successful." : "❌ Transfer failed.");
                }
                case 5 -> {
                    System.out.print("Enter loan amount: ");
                    double amt = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter loan type (REGULAR/BALLOON): ");
                    String type = scanner.nextLine();
                    System.out.print("Enter duration in months: ");
                    int months = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println(service.applyLoan(user.getId(), amt, type, months) ? "✅ Loan request submitted." : "❌ Failed to apply loan.");
                }
                case 6 -> {
                    System.out.println("Logging out...");
                    return;
                }
                case 7 -> {
                    List<Transaction> txns = service.getAccountHistory(acc.getId());
                    if (txns.isEmpty()) {
                        System.out.println("📭 No transactions found.");
                    } else {
                        System.out.println("\n📜 Transaction History:");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd MMM yyyy");
                        for (Transaction t : txns) {
                            System.out.println("ID: " + t.getId() +
                                    ", Type: " + t.getType() +
                                    ", Amount: " + t.getAmount() +
                                    ", Time: " + t.getTimestamp().format(formatter));
                        }
                    }
                }
                case 8 -> {
                    System.out.print("Enter Loan ID to repay: ");
                    int loanId = scanner.nextInt();
                    System.out.print("Enter amount to repay: ");
                    double repayAmt = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println(service.repayLoan(acc.getId(), loanId, repayAmt) ? "✅ Repayment successful." : "❌ Repayment failed.");
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n==== Admin Menu ====");
            System.out.println("1. View All Users\n2. View Loan Applications\n3. Update Account Status\n4. Approve/Reject Loan\n5. Exit");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    List<User> users = service.getAllUsers();
                    users.forEach(u -> System.out.println(u.getId() + ": " + u.getName() + " | " + u.getEmail()));
                }
                case 2 -> {
                    List<Loan> loans = service.getAllLoans();
                    loans.forEach(l -> System.out.println("LoanID: " + l.getId() + ", UserID: " + l.getUserId() + ", Amt: " + l.getAmount() + ", Type: " + l.getType() + ", Status: " + l.getStatus()));
                }
                case 3 -> {
                    System.out.print("Enter Account ID: ");
                    int accId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter status (ACTIVE/BLOCKED/SUSPICIOUS): ");
                    String status = scanner.nextLine();
                    System.out.println(service.updateAccountStatus(accId, status) ? "✅ Status updated." : "❌ Update failed.");
                }
                case 4 -> {
                    System.out.print("Enter Loan ID: ");
                    int loanId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter status (APPROVED/REJECTED): ");
                    String status = scanner.nextLine();
                    System.out.println(service.updateLoanStatus(loanId, status) ? "✅ Loan updated." : "❌ Update failed.");
                }
                case 5 -> {
                    System.out.println("Exiting admin mode.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
