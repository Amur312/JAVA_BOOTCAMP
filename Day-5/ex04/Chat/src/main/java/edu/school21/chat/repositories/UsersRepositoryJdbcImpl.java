package edu.school21.chat.repositories;

import edu.school21.chat.models.ChatRoom;
import edu.school21.chat.models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersRepositoryJdbcImpl implements UsersRepository{
    private final DataSource dataSource;
    private final Map<Long, User> idToUser = new HashMap<>();
    private final Map<Long, ChatRoom> idToRoom = new HashMap<>();
    private static final String SELECT_USERS_QUERY =
            "WITH userToOwnRooms AS (" +
                    "   SELECT " +
                    "       u.id AS user_id, " +
                    "       u.login AS user_login, " +
                    "       u.password AS user_passwd, " +
                    "       cr.id AS own_room_id, " +
                    "       cr.name AS own_room_name " +
                    "   FROM \"user\" u " +
                    "   FULL JOIN chatroom cr ON u.id = cr.owner" +
                    "), userToAllRooms AS (" +
                    "   SELECT " +
                    "       userToOwnRooms.user_id, " +
                    "       userToOwnRooms.user_login, " +
                    "       userToOwnRooms.user_passwd, " +
                    "       userToOwnRooms.own_room_id, " +
                    "       userToOwnRooms.own_room_name, " +
                    "       m.room AS part_of_room_id, " +
                    "       cr.name AS part_of_room_name " +
                    "   FROM userToOwnRooms " +
                    "   FULL JOIN message m ON userToOwnRooms.user_id = m.author " +
                    "   FULL JOIN chatroom cr ON m.room = cr.id " +
                    ") " +
                    "SELECT * FROM userToAllRooms " +
                    "WHERE user_id IS NOT NULL;";

    public UsersRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> findAll(int page, int size) {
        if (page < 0 || size < 1) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        List<User> result = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USERS_QUERY)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long user_id = resultSet.getLong("user_id");
                    String user_login = resultSet.getString("user_login");
                    String user_passwd = resultSet.getString("user_passwd");
                    long own_room_id = resultSet.getLong("own_room_id");
                    String own_room_name = resultSet.getString("own_room_name");
                    long part_room_id = resultSet.getLong("part_of_room_id");
                    String part_room_name = resultSet.getString("part_of_room_name");

                    User user = idToUser.computeIfAbsent(user_id, id ->
                            new User(user_id, user_login, user_passwd, new ArrayList<>(), new ArrayList<>())
                    );

                    ChatRoom ownRoom = idToRoom.computeIfAbsent(own_room_id, id ->
                            new ChatRoom(own_room_id, own_room_name, user, new ArrayList<>())
                    );

                    ChatRoom partOfRoom = idToRoom.computeIfAbsent(part_room_id, id ->
                            new ChatRoom(part_room_id, part_room_name, null, new ArrayList<>())
                    );

                    handleUserRooms(user, ownRoom, partOfRoom);
                }
            }

            result.addAll(idToUser.values());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result.subList(page * size, Math.min((page + 1) * size, result.size()));
    }

    private void handleUserRooms(User user, ChatRoom ownRoom, ChatRoom partOfRoom) {
        List<ChatRoom> createdRooms = user.getCreatedRooms();
        List<ChatRoom> rooms = user.getRooms();

        if (ownRoom.getId() != 0) {
            if (ownRoom.getChatOwner() == null) {
                ownRoom.setChatOwner(user);
            }
            createdRooms.add(ownRoom);
            rooms.add(ownRoom);
        }

        if (partOfRoom.getId() != 0) {
            rooms.add(partOfRoom);
        }
    }

}
