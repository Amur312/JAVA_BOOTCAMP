package ex03;

public class User {
    private Integer identifier;
    private String name;
    private Integer balance;
    private TransactionsLinkedList transactionsList;

    public User(String name, Integer balance) {
        if (balance < 0) {
            System.out.println("Баланс не может быть отрицательным");
            System.exit(1);
        } else {
            this.identifier = UserIdsGenerator.getInstance().generateId();
            this.balance = balance;
            this.name = name;
            this.transactionsList = new TransactionsLinkedList();
        }
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public TransactionsList getTransactionsList() {
        return transactionsList;
    }

    @Override
    public String toString() {
        return String.format("User { Identifier: %d, Name: '%s', Balance: %d }", identifier, name, balance);
    }
}
