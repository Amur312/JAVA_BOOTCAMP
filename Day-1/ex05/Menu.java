package ex05;

import java.util.Scanner;
import java.util.UUID;

public class Menu {
    private TransactionsService transactionsService;
    private Scanner scanner;
    private boolean isDevMode;

    public Menu(TransactionsService transactionsService, boolean isDevMode) {
        this.transactionsService = transactionsService;
        this.scanner = new Scanner(System.in);
        this.isDevMode = isDevMode;
    }

    public void start() {
        boolean exit = false;

        while (!exit) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    viewUserBalances();
                    break;
                case 3:
                    performTransfer();
                    break;
                case 4:
                    viewUserTransactions();
                    break;
                case 5:
                    removeTransferById();
                    break;
                case 6:
                    checkTransferValidity();
                    break;
                case 7:
                    exit = true;
                    System.out.println("Завершение работы программы.");
                    break;
                default:
                    System.out.println("Некорректный ввод. Пожалуйста, повторите попытку.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("1. Add a user");
        System.out.println("2. View user balances");
        System.out.println("3. Perform a transfer");
        System.out.println("4. View all transactions for a specific user");
        System.out.println("5. DEV – remove a transfer by ID");
        System.out.println("6. DEV – check transfer validity");
        System.out.println("7. Finish execution");
        System.out.print("-> ");
    }

    private int getUserChoice() {
        return scanner.nextInt();
    }

    private void addUser() {
        scanner.nextLine();
        System.out.println("Enter a user name and a balance");
        System.out.print("-> ");
        String userName = scanner.next();
        int balance = scanner.nextInt();
        User user = new User(userName, balance);
        transactionsService.addUser(user);
        System.out.println("User with id = " + user.getIdentifier() + " is added");
        System.out.println("---------------------------------------------------------");
    }

    private void viewUserBalances() {
        System.out.println("Enter a user ID");
        System.out.print("-> ");
        int userId = scanner.nextInt();
        Integer balance = transactionsService.getUserBalance(userId);
        String name = transactionsService.getUserName(userId);
        if (balance != null) {
            System.out.println(name + " " + transactionsService.getUserBalance(userId));
        }
        System.out.println("---------------------------------------------------------");
    }

    private void performTransfer() {
        System.out.println("Enter a sender ID, a recipient ID, and a transfer amount");
        System.out.print("-> ");
        int userIdSend = scanner.nextInt();
        int userIdRecipient = scanner.nextInt();
        int transferAmount = scanner.nextInt();
        try {
            transactionsService.performTransfer(userIdSend, userIdRecipient, transferAmount);
            System.out.println("The transfer is completed");
        } catch (IllegalTransactionException e) {
            System.out.println("Error during transaction: " + e.getMessage());
        }

    }

    private void viewUserTransactions() {
        System.out.println("Enter a user ID");
        System.out.print("-> ");
        int userId = scanner.nextInt();

        Transaction[] userTransactions = transactionsService.getUserTransactions(userId);

        if (userTransactions.length > 0) {
            System.out.println("Transactions for user " + transactionsService.getUserName(userId) + ":");
            for (Transaction transaction : userTransactions) {
                System.out.println(transaction);
            }
        } else {
            System.out.println("No transactions found for user with ID " + userId);
        }

        System.out.println("---------------------------------------------------------");
    }


    private void removeTransferById() {
        if (isDevMode) {
            System.out.println("Enter a user ID and a transfer ID");
            System.out.print("-> ");
            int userId = scanner.nextInt();
            String transactionIdString = scanner.next();
            try {
                UUID transactionId = UUID.fromString(transactionIdString);
                int amount = 0;
                Transaction[] userTransactions = transactionsService.getUserTransactions(userId);
                for (Transaction trans : userTransactions) {
                    if (trans.getIdentifier().equals(transactionId)) {
                        amount = trans.getTransferAmount();
                    }
                }

                transactionsService.removeTransaction(userId, transactionId);
                System.out.printf("Transfer To %s(id = %d) %d removed\n",
                        transactionsService.getUserName(userId), userId, amount);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format. Please enter a valid UUID.");
            }
        } else {
            System.out.println("This operation is allowed only in dev mode.");
        }

        System.out.println("---------------------------------------------------------");
    }




    private void checkTransferValidity() {
        if (isDevMode) {
            System.out.println("Check results:");
            Transaction[] unpairedTransactions = transactionsService.checkValidityOfTransactions();

            for (Transaction transaction : unpairedTransactions) {
                System.out.println(transaction.getRecipient().getName() + "(id = " + transaction.getRecipient().getIdentifier() +
                        ") has an unacknowledged transfer id = " + transaction.getIdentifier() +
                        " from " + transaction.getSender().getName() + "(id = " + transaction.getSender().getIdentifier() +
                        ") for " + transaction.getTransferAmount());
            }
        } else {
            System.out.println("This operation is allowed only in dev mode.");
        }

        System.out.println("---------------------------------------------------------");
    }
}
