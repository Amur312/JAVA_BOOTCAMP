package edu.school21.chat.app;

import com.zaxxer.hikari.HikariDataSource;
import edu.school21.chat.models.Message;
import edu.school21.chat.repositories.MessagesRepository;
import edu.school21.chat.repositories.MessagesRepositoryJdbcImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;


public class Program {
    private static final Logger LOG = LoggerFactory.getLogger(Program.class);

    private static final String PROMPT_MESSAGE = "Enter a message ID: ";
    private static final String ERROR_INVALID_ID = "Invalid ID provided!";
    private static final String ERROR_MESSAGE_INVALID_ID = "Error fetching message by ID: ";
    private static final String ERROR_FETCHING_MESSAGE = "Error fetching message by ID: ";
    private final MessagesRepository messagesRepository;

    public Program(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public static void main(String[] args) {
        HikariDataSource dataSource = DatabaseConnect.connectDb();
        MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(dataSource);
        Program program = new Program(messagesRepository);
        program.start();
    }

    private void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            long id = getIdFromInput(scanner);
            Optional<Message> message = messagesRepository.findById(id);
            System.out.println(message.orElseThrow(NoSuchElementException::new));
        } catch (NoSuchElementException e) {
            LOG.error(ERROR_FETCHING_MESSAGE, e);
            System.err.println(ERROR_MESSAGE_INVALID_ID + e.getMessage());
        }
    }

    private long getIdFromInput(Scanner scanner) {
        System.out.print(PROMPT_MESSAGE);
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                LOG.error(ERROR_INVALID_ID, e);
                System.err.println(ERROR_INVALID_ID);
            }
        }
    }

}
