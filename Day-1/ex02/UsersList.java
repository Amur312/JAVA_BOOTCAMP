package ex02;

public interface UsersList {
    void addUser(User user);
    User getUserById(int userId);
    User getUserByIndex(int index);
    int getNumberOfUsers();
}
