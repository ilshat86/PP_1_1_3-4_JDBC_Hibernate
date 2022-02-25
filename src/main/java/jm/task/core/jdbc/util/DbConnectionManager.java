package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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

    public static SessionFactory getSessionFactory() {
        String hostName = "localhost";

        String dbName = "jm";
        String userName = "root";
        String password = "MySqlPass1!";

        SessionFactory hibernateConnection = getSessionFactory(hostName, dbName, userName, password);
        return hibernateConnection;
    }

    private static SessionFactory getSessionFactory(String hostName, String dbName,
                                                    String userName, String password) {
        Properties properties = new Properties();
        properties.put(Environment.URL, "jdbc:mysql://" + hostName + ":3306/" + dbName);

        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");

        properties.put(Environment.USER, userName);
        properties.put(Environment.PASS, password);
        properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

        Configuration configuration = new Configuration();
        configuration.setProperties(properties);

        configuration.addAnnotatedClass(User.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

}
