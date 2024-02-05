package edu.school21.chat.models;


import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Getter
@Setter
public class Message {

    private long id;
    private User author;
    private ChatRoom room;
    private String text;
    private Timestamp dateTime;

    public Message(long id, User author, ChatRoom room, String text, Timestamp dateTime) {
        this.id = id;
        this.author = author;
        this.room = room;
        this.text = text;
        this.dateTime = dateTime;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, author, room, text, dateTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Message)) {
            return false;
        }
        Message obj1 = (Message) obj;
        return this.id == obj1.id &&
                Objects.equals(this.author, obj1.author) &&
                Objects.equals(this.room, obj1.room) &&
                Objects.equals(this.text, obj1.text) &&
                Objects.equals(this.dateTime, obj1.dateTime);
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return String.format("Message : {%n\tid=%d,%n\tauthor={%s},%n\troom={%s},%n\ttext=\"%s\",%n\tdateTime=%s%n}",
                id,
                Objects.toString(author, "null"),
                Objects.toString(room, "null"),
                text,
                dateTime != null ? dateFormat.format(dateTime) : "null");
    }
}
