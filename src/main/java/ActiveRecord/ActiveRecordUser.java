package ActiveRecord;

import java.sql.*;

public class ActiveRecordUser {
    private Integer id;
    private String name;
    private String email;

    public ActiveRecordUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void save(Connection conn) throws SQLException {
        if (id == null) {
            String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.executeUpdate();
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    this.id = keys.getInt(1);
                }
            }
        } else {
            String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setInt(3, id);
                stmt.executeUpdate();
            }
        }
    }

    public void delete(Connection conn) throws SQLException {
        if (id != null) {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            id = null;
        }
    }

    public static ActiveRecordUser findById(Connection conn, int id) throws SQLException {
        String sql = "SELECT id, name, email FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ActiveRecordUser user = new ActiveRecordUser(rs.getString("name"), rs.getString("email"));
                user.id = rs.getInt("id");
                return user;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getId() {
        return id;
    }

}
