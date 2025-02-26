import java.util.InputMismatchException;
import java.util.Scanner;

class ATM {
    private double balance;
    private int userId;
    private int pin;
    private static final double DAILY_WITHDRAWAL_LIMIT = 500.0;
    private double dailyWithdrawnAmount = 0;
    
    public ATM(int userId, int pin, double initialBalance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = initialBalance;
    }
    
    public boolean authenticate(int enteredPin) {
        return this.pin == enteredPin;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit successful! New balance: $" + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }
    
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.");
        } else if (amount > balance) {
            System.out.println("Insufficient funds.");
        } else if (dailyWithdrawnAmount + amount > DAILY_WITHDRAWAL_LIMIT) {
            System.out.println("Withdrawal limit exceeded. You can withdraw up to $" + (DAILY_WITHDRAWAL_LIMIT - dailyWithdrawnAmount) + " today.");
        } else {
            balance -= amount;
            dailyWithdrawnAmount += amount;
            System.out.println("Withdrawal successful! New balance: $" + balance);
        }
    }
}

public class ATMSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ATM[] users = {new ATM(1, 1234, 500.0), new ATM(2, 5678, 1000.0)}; // Example users
        int failedAttempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        try {
            System.out.print("Enter User ID (1 or 2): ");
            int userId = scanner.nextInt();
            
            if (userId < 1 || userId > users.length) {
                System.out.println("Invalid User ID.");
                return;
            }
            
            ATM currentUser = users[userId - 1];
            
            while (failedAttempts < MAX_ATTEMPTS) {
                System.out.print("Enter PIN: ");
                int enteredPin = scanner.nextInt();
                
                if (currentUser.authenticate(enteredPin)) {
                    break;
                } else {
                    failedAttempts++;
                    System.out.println("Incorrect PIN. Attempts remaining: " + (MAX_ATTEMPTS - failedAttempts));
                    if (failedAttempts == MAX_ATTEMPTS) {
                        System.out.println("Too many failed attempts. Access denied.");
                        return;
                    }
                }
            }
            
            boolean running = true;
            
            while (running) {
                System.out.println("\nATM Menu:");
                System.out.println("1. Check Balance");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");
                
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter numbers only.");
                    scanner.next(); // Clear invalid input
                    continue;
                }
                
                int choice = scanner.nextInt();
                
                switch (choice) {
                    case 1:
                        System.out.println("Current balance: $" + currentUser.getBalance());
                        break;
                    case 2:
                        System.out.print("Enter deposit amount: ");
                        if (!scanner.hasNextDouble()) {
                            System.out.println("Invalid input. Please enter a valid amount.");
                            scanner.next(); // Clear invalid input
                            continue;
                        }
                        double depositAmount = scanner.nextDouble();
                        currentUser.deposit(depositAmount);
                        break;
                    case 3:
                        System.out.print("Enter withdrawal amount: ");
                        if (!scanner.hasNextDouble()) {
                            System.out.println("Invalid input. Please enter a valid amount.");
                            scanner.next(); // Clear invalid input
                            continue;
                        }
                        double withdrawAmount = scanner.nextDouble();
                        currentUser.withdraw(withdrawAmount);
                        break;
                    case 4:
                        running = false;
                        System.out.println("Thank you for using the ATM.");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numbers only.");
        } finally {
            scanner.close();
        }
    }
}
