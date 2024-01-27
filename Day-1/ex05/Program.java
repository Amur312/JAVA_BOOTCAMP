package ex05;


public class Program {
    public static void main(String[] args) {
        UsersList usersList = new UsersArrayList();
        TransactionsService transactionsService = new TransactionsService(usersList);
        boolean isDevMode = false;
        if (args.length > 0 && args[0].equals("--profile=dev")) {
            isDevMode = true;
            System.out.println("Dev mode is enabled.");
        } else {
            System.out.println("Dev mode is not enabled.");
        }

        Menu menu = new Menu(transactionsService, isDevMode);
        menu.start();
    }
}
