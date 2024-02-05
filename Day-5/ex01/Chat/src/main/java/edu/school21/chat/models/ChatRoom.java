package edu.school21.chat.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ChatRoom {
    private long id;
    private String chatName;
    private User chatOwner;
    private List<Message> messages;

    public ChatRoom(long id, String chatName, User chatOwner, List<Message> messages) {
        this.id = id;
        this.chatName = chatName;
        this.chatOwner = chatOwner;
        this.messages = messages;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChatRoom chatRoom = (ChatRoom) obj;
        return id == chatRoom.id &&
                Objects.equals(chatName, chatRoom.chatName) &&
                Objects.equals(chatOwner, chatRoom.chatOwner) &&
                Objects.equals(messages, chatRoom.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatName, chatOwner, messages);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("id=").append(id)
                .append(", ChatName=\"").append(chatName).append("\"")
                .append(", creator=").append(Objects.toString(chatOwner, "null"))
                .append(", messages=");

        if (messages == null) {
            result.append("null");
        } else {
            result.append("[");
            for (int i = 0; i < messages.size(); i++) {
                result.append(messages.get(i).getText());
                if (i + 1 != messages.size()) {
                    result.append(",");
                }
            }
            result.append("]");
        }

        return result.toString();
    }
}
