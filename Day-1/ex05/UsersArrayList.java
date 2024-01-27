package ex05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsersArrayList implements UsersList {
    private static final int DEFAULT_SIZE = 10;
    private User[] users;
    private int size;

    public UsersArrayList() {
        this.users = new User[DEFAULT_SIZE];
        this.size = 0;
    }

    @Override
    public void addUser(User user) {
        ensureSize();
        users[size++] = user;
    }

    @Override
    public User getUserById(int userId) {
        for (int i = 0; i < size; i++){
            User user = users[i];
            if(user != null && user.getIdentifier() == userId){
                return user;
            }
        }
        throw new CustomUserNotFoundException("Пользователь с идентификатором " + userId + " не найден.");
    }

    @Override
    public User getUserByIndex(int index) {
        if(index >= 0 && index < size){
            return users[index];
        }
        throw new CustomUserNotFoundException("Invalid index: " + index);
    }

    @Override
    public int getNumberOfUsers() {
        return size;
    }

    @Override
    public User[] toArray() {
        return Arrays.copyOf(users, size);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(Arrays.asList(Arrays.copyOf(users, size)));
    }

    private void ensureSize() {
        if (size == users.length) {
            int newSize = users.length + (users.length >> 1);
            User[] newArray = new User[newSize];
            for (int i = 0; i < size; i++) {
                newArray[i] = users[i];
            }
            users = newArray;
        }
    }
}
