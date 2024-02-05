package edu.school21.chat.app;


import com.zaxxer.hikari.HikariDataSource;
import edu.school21.chat.models.ChatRoom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;
import edu.school21.chat.repositories.MessagesRepository;
import edu.school21.chat.repositories.MessagesRepositoryJdbcImpl;
import edu.school21.chat.repositories.NotSavedSubEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Program {
    private final MessagesRepository messagesRepository;

    public Program(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public static void main(String[] args) {
        HikariDataSource hikariDataSource = DatabaseConnect.connectDb();

        User author = new User(1L, "test", "test", new ArrayList(), new ArrayList());
        ChatRoom room = new ChatRoom(2L, "room", author, new ArrayList());
        long messageId = generateUniqueMessageId();
        Message message = new Message(messageId, author, room, "Hello World!", Timestamp.valueOf(LocalDateTime.now()));
        MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(hikariDataSource);
        try {
            messagesRepository.save(message);
            System.out.println("Сообщение успешно сохранено. ID сообщения: " + message.getId());


            printMessageDetails(message);

            messagesRepository.delete(message.getId());
            System.out.println("Сообщение успешно удалено. ID сообщения: " + message.getId());
        } catch (NotSavedSubEntityException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void printMessageDetails(Message message) {
        System.out.println("Информация о сообщении:");
        System.out.println("ID: " + message.getId());
        System.out.println("Текст: " + message.getText());
        System.out.println("Автор: " + message.getAuthor().getLogin());
        System.out.println("Комната чата: " + message.getChatRoom());
        System.out.println("Время создания: " + message.getDateTime());
    }

    private static long generateUniqueMessageId() {
        return System.currentTimeMillis();
    }

}
