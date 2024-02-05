package edu.school21.chat.repositories;


import edu.school21.chat.models.ChatRoom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {
    private static final String ERROR_MSG = "Error while executing SQL query";
    private final DataSource dataSource;
    private static final Logger LOG = LoggerFactory.getLogger(MessagesRepositoryJdbcImpl.class);
    private static final String SELECT_MESSAGE_QUERY = "SELECT message.id AS m_id, " +
            "message.text AS m_text, " +
            "message.datetime AS m_datetime, " +
            "\"user\".id AS u_id, " +
            "\"user\".login AS u_login, " +
            "\"user\".password AS u_password, " +
            "chatroom.id AS r_id, " +
            "chatroom.name AS r_name " +
            "FROM message " +
            "LEFT JOIN \"user\" ON message.author = \"user\".id " +
            "LEFT JOIN chatroom ON message.room = chatroom.id " +
            "WHERE message.id = ?;";

    public MessagesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Message> findById(Long id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_MESSAGE_QUERY)
        ) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new RuntimeException("Query result is empty");
                }

                User user = new User(
                        resultSet.getLong("u_id"),
                        resultSet.getString("u_login"),
                        resultSet.getString("u_password"),
                        null,
                        null
                );
                ChatRoom chatRoom = new ChatRoom(
                        resultSet.getLong("r_id"),
                        resultSet.getString("r_name"),
                        null,
                        null
                );
                Message message = new Message(
                        resultSet.getLong("m_id"),
                        user,
                        chatRoom,
                        resultSet.getString("m_text"),
                        resultSet.getTimestamp("m_datetime")
                );

                return Optional.of(message);
            }
        } catch (SQLException e) {
            LOG.error(ERROR_MSG + e.getMessage());
            return Optional.empty();
        }
    }
}
