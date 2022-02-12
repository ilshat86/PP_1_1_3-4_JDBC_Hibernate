package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Ivan", "Ivanov", (byte) 15);
        userService.saveUser("Ivan1", "Ivanov1", (byte) 16);
        userService.saveUser("Ivan2", "Ivanov2", (byte) 26);
        userService.saveUser("Ivan3", "Ivanov3", (byte) 36);

        List<User> list = userService.getAllUsers();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        userService.cleanUsersTable();
        userService.dropUsersTable();

    }
}
