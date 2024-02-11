package app;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Program {
    private static final Logger LOGGER = LoggerFactory.getLogger(Program.class);

    public static void main(String[] args) {
        try {
            Program program = new Program();
            program.execute();
        } catch (RuntimeException e) {
            LOGGER.error("Error occurred: {}", e.getMessage(), e);
            System.exit(-1);
        }
    }

    private void execute() {
        OrmManager ormManager = new OrmManager();
        ormManager.createTables();

        User user1 = new User(1L, "John", "Doe", 25);
        User user2 = new User(2L, "Alice", "Johnson", 30);
        User user3 = new User(3L, "Bob", "Smith", 40);

        ormManager.save(user1);
        ormManager.save(user2);
        ormManager.save(user3);

        ormManager.update(new User(2L, "Alice", "Anderson", 32));
        ormManager.update(new User(3L, "Robert", "Brown", 45));

        LOGGER.info("User with id 123: {}", ormManager.findById(123L, User.class));
        LOGGER.info("User with id 1: {}", ormManager.findById(1L, User.class));
        LOGGER.info("User with id 2: {}", ormManager.findById(2L, User.class));
        LOGGER.info("User with id 3: {}", ormManager.findById(3L, User.class));
    }
}