package ex00;

public class Program {
    public static void main(String[] args) {
        User user1 = new User(1, "Ivan", 800);
        User user2 = new User(2, "Pavel", 400);
        System.out.println(user1);
        System.out.println(user2);
        Transaction trans1 = new Transaction(user1, user2, 130, Transaction.TransactionCategory.CREDIT);
        System.out.println(trans1);

    }
}
