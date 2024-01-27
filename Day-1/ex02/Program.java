package ex02;

public class Program {
    public static void main(String[] args) {
        User user1 = new User("Ivan", 1500);
        User user2 = new User("Pavel", 3500);

        UsersList list = new UsersArrayList();
        list.addUser(user1);
        list.addUser(user2);

        System.out.println("User at index 0: " + list.getUserByIndex(0) + " == " + user1);
        System.out.println("User at index 1: " + list.getUserByIndex(1) + " == " + user2);
        System.out.println("Total users count: " + list.getNumberOfUsers());

        User user3 = new User("Inna", 10000);
        User user4 = new User("Masha", 8999);

        list.addUser(user3);
        list.addUser(user4);

        System.out.println("User with ID 3: " + list.getUserById(3) + " == " + user3);
        System.out.println("User with ID 4: " + list.getUserById(4) + " == " + user4);

    }
}
