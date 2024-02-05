package edu.school21.chat.app;


import com.zaxxer.hikari.HikariDataSource;
import edu.school21.chat.models.User;
import edu.school21.chat.repositories.UsersRepositoryJdbcImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class Program {
    private static final Logger LOG = LoggerFactory.getLogger(Program.class);

    public static void main(String[] args) {
        try (HikariDataSource hikariDataSource = DatabaseConnect.connectDb();) {
            UsersRepositoryJdbcImpl usersRepositoryJdbc = new UsersRepositoryJdbcImpl(hikariDataSource);
            try {
                List<User> users = usersRepositoryJdbc.findAll(0, 99);
                for (User user : users) {
                    System.out.println(user);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
