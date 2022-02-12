package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionManager {

    public static Connection getMySQLConnection() {
        String hostName = "localhost";

        String dbName = "jm";
        String userName = "root";
        String password = "MySqlPass1!";
        try {
            return getMySQLConnection(hostName, dbName, userName, password);
        } catch (Exception ex) {
            System.out.println("Ошибка подключения к базе...");
            ex.printStackTrace();
        }
        return null;
    }

    public static Connection getMySQLConnection(String hostName, String dbName,
                                                String userName, String password) throws
            ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;

        return DriverManager.getConnection(connectionURL, userName, password);
    }
}
