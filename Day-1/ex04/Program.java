package ex04;


public class Program {
    public static void main(String[] args) {

        User user1 = new User("Ivan", 5000);
        User user2 = new User("Pavel", 2200);

        System.out.println("Баланс пользователя " + user1.getName() + ": " + user1.getBalance());
        System.out.println("Баланс пользователя " + user2.getName() + ": " + user2.getBalance());

        UsersArrayList usersList = new UsersArrayList();
        TransactionsService transactionsService = new TransactionsService(usersList);

        transactionsService.addUser(user1);
        transactionsService.addUser(user2);

        try {
            transactionsService.performTransfer(user2.getIdentifier(), user1.getIdentifier(), 2000);
            System.out.println("Транзакция выполнена успешно.");
        } catch (IllegalTransactionException e) {
            System.out.println("Ошибка во время выполнения транзакции: " + e.getMessage());
        }

        System.out.println("Баланс пользователя " + user1.getName() + " после транзакции: " + user1.getBalance());
        System.out.println("Баланс пользователя " + user2.getName() + " после транзакции: " + user2.getBalance());

        Transaction[] user1Transactions = transactionsService.getUserTransactions(user1.getIdentifier());
        System.out.println("Транзакции пользователя " + user1.getName() + ":");
        for (Transaction transaction : user1Transactions) {
            System.out.println(transaction);
        }

    }
}
