package ex01;

public class User {
    private Integer identifier;
    private String name;
    private Integer balance;

    public User(String name, Integer balance) {
        if (balance < 0) {
            System.out.println("Баланс не может быть отрицательным");
            System.exit(1);
        } else {
            this.identifier = UserIdsGenerator.getInstance().generateId();
            this.balance = balance;
            this.name = name;
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

    @Override
    public String toString() {
        return "User {" +
                "\n  Identifier: " + identifier +
                "\n  Name: '" + name + '\'' +
                "\n  Balance: " + balance +
                "\n}";
    }
}
