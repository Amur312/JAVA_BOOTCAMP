package edu.school21.chat.app;

import com.zaxxer.hikari.HikariDataSource;
import edu.school21.chat.models.ChatRoom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;
import edu.school21.chat.repositories.MessagesRepository;
import edu.school21.chat.repositories.MessagesRepositoryJdbcImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;


public class Program {
    private static final Logger LOG = LoggerFactory.getLogger(Program.class);

    public static void main(String[] args) {
        try (HikariDataSource hikariDataSource = DatabaseConnect.connectDb()) {
            MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(hikariDataSource);
            Optional<Message> messageOptionalBefore = messagesRepository.findById(1L);

            if (messageOptionalBefore.isPresent()) {
                Message messageBefore = messageOptionalBefore.get();
                printMessageDetails("До изменений:", messageBefore);
                System.out.println();
            }

            User newAuthor = new User(2L, "Ivan", "pass312", new ArrayList<>(), new ArrayList<>());
            ChatRoom room = new ChatRoom(4L, "Hello room", newAuthor, new ArrayList<>());
            Optional<Message> messageOptionalAfter = messagesRepository.findById(1L);

            if (messageOptionalAfter.isPresent()) {
                Message messageAfter = messageOptionalAfter.get();
                messageAfter.setText("Hello, world Test");
                messageAfter.setDateTime(Timestamp.valueOf(LocalDateTime.now()));
                messageAfter.setAuthor(newAuthor);
                messageAfter.setChatRoom(room);

                try {
                    messagesRepository.update(messageAfter);
                    System.out.println("После изменений:");
                    printMessageDetails("", messageAfter);
                } catch (RuntimeException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }


    }

    private static void printMessageDetails(String prefix, Message message) {
        System.out.println(prefix);
        System.out.println("ID сообщения: " + message.getId());
        System.out.println("Автор: " + message.getAuthor().getLogin());
        System.out.println("Комната: " + message.getChatRoom().getChatName());
        System.out.println("Текст: " + message.getText());
        System.out.println("Дата и время: " + message.getDateTime());
    }

}
