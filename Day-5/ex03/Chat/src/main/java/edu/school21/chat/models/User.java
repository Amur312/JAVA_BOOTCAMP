package edu.school21.chat.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Getter
@Setter
public class User {
    private long id;
    private String login;
    private String password;
    private List<ChatRoom> createdRooms;
    private List<ChatRoom> rooms;

    public User(long id, String login, String password, List<ChatRoom> createdRooms, List<ChatRoom> rooms) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.createdRooms = createdRooms;
        this.rooms = rooms;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, createdRooms, rooms);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User obj1 = (User) obj;
        return this.id == obj1.id &&
                Objects.equals(this.login, obj1.login) &&
                Objects.equals(this.password, obj1.password) &&
                Objects.equals(this.createdRooms, obj1.createdRooms) &&
                Objects.equals(this.rooms, obj1.rooms);
    }

    @Override
    public String toString() {
        StringJoiner sb = new StringJoiner(", ", "User {", "}");

        sb.add("id=" + id);
        sb.add("login=\"" + login + "\"");
        sb.add("password=\"" + password + "\"");
        sb.add("createdRooms=" + roomListToString(createdRooms));
        sb.add("rooms=" + roomListToString(rooms));

        return sb.toString();
    }

    private String roomListToString(List<ChatRoom> rooms) {
        if (rooms == null) {
            return "null";
        }

        StringJoiner sj = new StringJoiner(", ", "[", "]");
        for (ChatRoom room : rooms) {
            sj.add("\"" + room.getChatName() + "\"");
        }
        return sj.toString();
    }

}
