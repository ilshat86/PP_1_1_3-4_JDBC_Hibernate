package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection;

    private static final String SELECT_USER_SQL_TEMPLATE = "SELECT id, name, lastName, age from User";
    private static final String DROP_USER_SQL_TEMPLATE = "DROP TABLE IF EXISTS User;";
    private static final String CREATE_USER_SQL_TEMPLATE = "CREATE TABLE IF NOT EXISTS User (id INT auto_increment primary key, name VARCHAR(255) NOT NULL, lastName VARCHAR(255) NOT NULL, age TINYINT);";
    private static final String INSERT_USER_SQL_TEMPLATE = "INSERT INTO User (name, lastName, age) values (?, ?, ?)";
    private static final String DELETE_USER_SQL_TEMPLATE = "DELETE FROM User where id = ?";
    private static final String TRUNCATE_USER_SQL_TEMPLATE = "TRUNCATE TABLE User;";

    public UserDaoJDBCImpl() {
        this.connection = DbConnectionManager.getMySQLConnection();
    }

    public void createUsersTable() {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_USER_SQL_TEMPLATE)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {

        try (PreparedStatement statement = connection.prepareStatement(DROP_USER_SQL_TEMPLATE)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_USER_SQL_TEMPLATE)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);

            statement.execute();
            System.out.printf("User с именем – %s добавлен в базу данных\n", name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_SQL_TEMPLATE)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_USER_SQL_TEMPLATE)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    User user = new User();
                    user.setId(resultSet.getLong(1));
                    user.setName(resultSet.getString(2));
                    user.setLastName(resultSet.getString(3));
                    user.setAge(resultSet.getByte(4));
                    result.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void cleanUsersTable() {
        try (PreparedStatement statement = connection.prepareStatement(TRUNCATE_USER_SQL_TEMPLATE)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
