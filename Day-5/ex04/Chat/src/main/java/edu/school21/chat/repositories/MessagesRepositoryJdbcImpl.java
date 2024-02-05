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
    private static final String INSERT_QUERY = "insert into message(author, room, text, datetime) values(?, ?, ?, ?);";
    private static final String SELECT_ID_QUERY = "select id from message where datetime = ?;";
    private static final String UPDATE_MESSAGE_QUERY = "update message set author = ?, room = ?, text = ?, datetime = ? where id = ?;";
    private static final String DELETE_MESSAGE_QUERY = "delete from message where id = ?;";
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
            LOG.error("Error while executing query: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void save(Message message) {

        Connection connection = null;

        try {
            connection = dataSource.getConnection();

            saveMessage(message, connection);

            updateMessageId(message, connection);

        } catch (SQLException e) {
            throw new NotSavedSubEntityException(e.getMessage());
        } finally {
            closeConnection(connection);
        }
    }

    private void saveMessage(Message message, Connection connection) throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement(INSERT_QUERY)) {

            ps.setLong(1, message.getAuthor().getId());
            ps.setLong(2, message.getChatRoom().getId());
            ps.setString(3, message.getText());
            ps.setTimestamp(4, message.getDateTime());

            ps.executeUpdate();
        }
    }

    private void updateMessageId(Message message, Connection connection) throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement(SELECT_ID_QUERY)) {

            ps.setTimestamp(1, message.getDateTime());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    message.setId(rs.getLong("id"));
                } else {
                    throw new NotSavedSubEntityException("Message not saved");
                }
            }
        }
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("Error closing connection", e);
            }
        }
    }

    @Override
    public void delete(Long messageId) throws NotSavedSubEntityException {

        try (Connection connection = dataSource.getConnection()) {

            String sql = DELETE_MESSAGE_QUERY;

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setLong(1, messageId);

                int deletedRowsCount = preparedStatement.executeUpdate();

                if (deletedRowsCount == 0) {
                    throw new NotSavedSubEntityException("Message with ID " + messageId + " not found");
                }

            }

        } catch (SQLException e) {
            throw new NotSavedSubEntityException("Error deleting message with ID " + messageId + ": " + e);
        }
    }

    @Override
    public void update(Message message) {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(UPDATE_MESSAGE_QUERY)) {
            preparedStatement.setLong(1, message.getAuthor().getId());
            preparedStatement.setLong(2, message.getChatRoom().getId());
            preparedStatement.setString(3, message.getText());
            preparedStatement.setTimestamp(4, message.getDateTime());
            preparedStatement.setLong(5, message.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Error updating message", e);
            throw new NotSavedSubEntityException("Error updating message for id: " + message.getId());
        }
    }
}
