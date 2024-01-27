package ex03;

import java.util.UUID;

public class Program {
    public static void main(String[] args) {
        UsersArrayList usersList = new UsersArrayList();

        User user1 = new User("Ivan", 1500);
        User user2 = new User("Pavel", 3500);
        User user3 = new User("Inna", 10000);
        User user4 = new User("Masha", 8999);

        usersList.addUser(user1);
        usersList.addUser(user2);
        usersList.addUser(user3);
        usersList.addUser(user4);

        System.out.println("Users:");
        for (int i = 0; i < usersList.getNumberOfUsers(); i++) {
            System.out.println(usersList.getUserByIndex(i));
        }

        TransactionsList transactionsList = new TransactionsLinkedList();

        Transaction transaction1 = new Transaction(user1, user2, 500, Transaction.TransactionCategory.DEBIT);
        Transaction transaction2 = new Transaction(user3, user1, 2000, Transaction.TransactionCategory.CREDIT);
        Transaction transaction3 = new Transaction(user2, user4, 1000, Transaction.TransactionCategory.DEBIT);

        transactionsList.addTransaction(transaction1);
        transactionsList.addTransaction(transaction2);
        transactionsList.addTransaction(transaction3);

        System.out.println("\nTransactions:");
        for (Transaction transaction : transactionsList.toArray()) {
            System.out.println(transaction);
        }

        UUID transactionIdToRemove = transaction2.getIdentifier();
        transactionsList.removeTransaction(transactionIdToRemove);

        System.out.println("\nTransactions after removal:");
        for (Transaction transaction : transactionsList.toArray()) {
            System.out.println(transaction);
        }

        System.out.println("\nUsers after transactions:");
        for (int i = 0; i < usersList.getNumberOfUsers(); i++) {
            System.out.println(usersList.getUserByIndex(i));
        }

    }
}
