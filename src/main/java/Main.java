import ActiveRecord.ActiveRecordUser;
import DataMapper.DataMapperUser;
import DataMapper.User;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite::memory:")) {
            try (Statement stmt = conn.createStatement()) {
                String createTableSQL = "CREATE TABLE users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "email TEXT NOT NULL)";
                stmt.execute(createTableSQL);
            }

            ActiveRecordUser user1 = new ActiveRecordUser("Alecsia", "adresa@gmail.com");
            user1.save(conn);
            System.out.println("Saved User with ID: " + user1.getId());

            ActiveRecordUser fetchedUser = ActiveRecordUser.findById(conn, user1.getId());
            System.out.println("Fetched User: " + fetchedUser.getName());

            DataMapperUser userMapper = new DataMapperUser();
            User user2 = new User("George", "george@gmail.com");
            userMapper.insert(user2, conn);
            System.out.println("Saved User with ID: " + user2.getId());

            User fetchedUser2 = userMapper.findById(user2.getId(), conn);
            System.out.println("Fetched User: " + fetchedUser2.getName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
