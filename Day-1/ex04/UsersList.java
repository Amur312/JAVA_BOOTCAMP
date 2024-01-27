package ex04;

import java.util.List;

public interface UsersList {
    void addUser(User user);
    User getUserById(int userId);
    User getUserByIndex(int index);
    int getNumberOfUsers();
    User[] toArray();
    List<User> getAllUsers();
}
