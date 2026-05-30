import java.util.Scanner;

// ============================================================
//  BankAccount Class - THE DATA VAULT
//  Handles all business logic with strict encapsulation.
//  Knows NOTHING about Scanner, menus, or console output.
// ============================================================
class BankAccount {

    private String accountHolder;
    private String accountNumber;
    private double balance;

    public BankAccount(String accountHolder, String accountNumber, double initialBalance) {
        this.accountHolder   = accountHolder;
        this.accountNumber   = accountNumber;
        this.balance         = initialBalance;
    }

    // ---------- Getters (read-only access) ----------
    public double getBalance()        { return balance;        }
    public String getAccountHolder()  { return accountHolder;  }
    public String getAccountNumber()  { return accountNumber;  }

    // ---------- deposit() - Security Checkpoint ----------
    public boolean deposit(double amount) {
        if (amount <= 0) return false;
        balance += amount;
        return true;
    }

    // ---------- withdraw() - Enforces overdraft rules ----------
    public boolean withdraw(double amount) {
        if (amount <= 0)       return false;   // invalid amount
        if (amount > balance)  return false;   // insufficient funds
        balance -= amount;
        return true;
    }
}


// ============================================================
//  ATM Class - THE CUSTOMER LOBBY (UI Layer)
//  Handles ALL user interaction: Scanner, menus, and display.
//  Delegates ALL financial logic to BankAccount.
// ============================================================
class ATM {

    private BankAccount account;
    private Scanner     scanner;

    public ATM(BankAccount account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
    }

    // ---------- Main run loop ----------
    public void run() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       Welcome to DecodeLabs ATM      ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("  Account Holder : " + account.getAccountHolder());
        System.out.println("  Account Number : " + account.getAccountNumber());
        System.out.println();

        boolean running = true;

        while (running) {
            displayMenu();
            int choice = getMenuChoice();

            switch (choice) {
                case 1: checkBalance();  break;
                case 2: depositMoney();  break;
                case 3: withdrawMoney(); break;
                case 4:
                    running = false;
                    System.out.println("\n✅ Thank you for using DecodeLabs ATM. Goodbye!\n");
                    break;
                default:
                    System.out.println("\n❌ Invalid option. Please choose 1–4.\n");
            }
        }

        scanner.close();
    }

    // ---------- Display Menu ----------
    private void displayMenu() {
        System.out.println("════════════════════════════════════════");
        System.out.println("              ATM MAIN MENU             ");
        System.out.println("════════════════════════════════════════");
        System.out.println("  1.  Check Balance");
        System.out.println("  2.  Deposit");
        System.out.println("  3.  Withdraw");
        System.out.println("  4.  Exit");
        System.out.println("════════════════════════════════════════");
        System.out.print("Enter your choice: ");
    }

    // ---------- Safe menu input (no crash on "abc") ----------
    private int getMenuChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid input. Please enter a number (1–4).");
            System.out.print("Enter your choice: ");
            scanner.next();                     // clear bad token
        }
        int choice = scanner.nextInt();
        System.out.println();
        return choice;
    }
    // ---------- Check Balance ----------
    private void checkBalance() {
        System.out.println("────────────────────────────────────────");
        System.out.printf ("  💰 Current Balance: EGP %.2f%n", account.getBalance());
        System.out.println("────────────────────────────────────────\n");
    }

    // ---------- Deposit ----------
    private void depositMoney() {
        System.out.print("  Enter deposit amount: EGP ");
        double amount = getValidAmount();

        if (account.deposit(amount)) {
            System.out.println("────────────────────────────────────────");
            System.out.printf ("  ✅ Successfully deposited EGP %.2f%n", amount);
            System.out.printf ("  💰 New Balance: EGP %.2f%n", account.getBalance());
            System.out.println("────────────────────────────────────────\n");
        } else {
            System.out.println("  ❌ Invalid amount. Deposit must be greater than zero.\n");
        }
    }

    // ---------- Withdraw ----------
    private void withdrawMoney() {
        System.out.printf ("  Current Balance: EGP %.2f%n", account.getBalance());
        System.out.print  ("  Enter withdrawal amount: EGP ");
        double amount = getValidAmount();

        if (account.withdraw(amount)) {
            System.out.println("────────────────────────────────────────");
            System.out.printf ("  ✅ Successfully withdrew EGP %.2f%n", amount);
            System.out.printf ("  💰 Remaining Balance: EGP %.2f%n", account.getBalance());
            System.out.println("────────────────────────────────────────\n");
        } else {
            System.out.println("────────────────────────────────────────");
            if (amount <= 0) {
                System.out.println("  ❌ Invalid amount. Must be greater than zero.");
            } else {
                System.out.println("  ❌ Insufficient Funds!");
                System.out.printf ("  Requested: EGP %.2f  |  Available: EGP %.2f%n",
                                   amount, account.getBalance());
            }
            System.out.println("────────────────────────────────────────\n");
        }
    }

    // ---------- Safe amount input (no crash on "abc" or "-50") ----------
    private double getValidAmount() {
        while (!scanner.hasNextDouble()) {
            System.out.println("  ❌ Invalid input. Please enter a valid number.");
            System.out.print  ("  Amount: EGP ");
            scanner.next();                     // clear bad token
        }
        return scanner.nextDouble();
    }
}


// ============================================================
//  Main Class - Entry Point
//  Creates the BankAccount and passes it to the ATM.
// ============================================================
public class ATM_Interface {

    public static void main(String[] args) {

        // Create the account (Data Vault)
        BankAccount myAccount = new BankAccount("Ahmed Hassan", "ACC-2026-001", 5000.00);

        // Create the ATM and inject the account (Lobby)
        ATM atm = new ATM(myAccount);

        // Start the ATM
        atm.run();
    }
}