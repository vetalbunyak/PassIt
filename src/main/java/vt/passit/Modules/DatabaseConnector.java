package vt.passit.Modules;

import vt.passit.Role;

import java.sql.*;

public class DatabaseConnector {
    private DatabaseConnector(){

    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            String dbUrl = ConfigManager.getDbUrl();
            String dbUser = ConfigManager.getDbUser();
            String dbPassword = ConfigManager.getDbPassword();

            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            System.out.println("Успішне підключення до бази даних!");
        } catch(SQLException e){
            System.err.println("Помилка підключення до бази даних:");
            e.printStackTrace();
        }
        return connection;
    }

    public static boolean addUser(String name, String pass, String email) {
        String hashedPassword = HashData.hashPassword(pass);
        String last_name = "";
        String profile_image_url = "";
        Connection connection = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            connection = getConnection();
            if (connection != null) {
                String sql = "INSERT INTO Accounts (name, password_hash, email, last_name, profile_image_url, role) VALUES (?, ?, ?, ?, ?, ?)";
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setString(2, hashedPassword);
                pstmt.setString(3, email);
                pstmt.setString(4, last_name);
                pstmt.setString(5, profile_image_url);
                pstmt.setString(6, String.valueOf(Role.USER));

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Користувача '" + email + "' успішно додано.");
                    success = true;
                } else {
                    System.out.println("Не вдалося додати користувача '" + email + "'.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при додаванні користувача '" + email + "':");
            e.printStackTrace();
            if (e.getSQLState().startsWith("23")) {
                System.err.println("Можливо, користувач з таким іменем або email вже існує.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Помилка хешування пароля: " + e.getMessage());
        } finally {
            closeResources(null, pstmt, connection);
        }
        return success;
    }

    public static boolean userExistByEmail(String email) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            connection = getConnection();
            if (connection != null) {
                String sql = "SELECT COUNT(*) FROM Accounts WHERE email = ?";
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, email);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        exists = true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при перевірці існування користувача за email '" + email + "':");
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return exists;
    }

    public static User getUserByEmail(String email) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            connection = getConnection();
            if (connection != null) {
                String sql = "SELECT id, name, password_hash, email, last_name, profile_image_url, role, created_at FROM Accounts WHERE email = ?";
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, email);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String passwordHash = rs.getString("password_hash");
                    String fetchedEmail = rs.getString("email");
                    String roleString = rs.getString("role");
                    String fetchedLastName = rs.getString("last_name");
                    String fetchedProfileImage = rs.getString("profile_image_url");

                    Role roleEnum;
                    try {
                        roleEnum = Role.valueOf(roleString.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Помилка: Невідома роль '" + roleString + "' для користувача " + name + ". Встановлення ролі за замовчуванням USER.");
                        roleEnum = Role.USER;
                    }
                    Timestamp createdAt = rs.getTimestamp("created_at");

                    user = new User(id, name, passwordHash, fetchedEmail, fetchedLastName, fetchedProfileImage, roleEnum, createdAt);
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при отриманні користувача за email '" + email + "':");
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return user;
    }

    public static void updateUser(User user) throws SQLException {
        String sql = "UPDATE Accounts SET " +
                "name = ?, " +
                "password_hash = ?, " +
                "email = ?, " +
                "last_name = ?, " +
                "profile_image_url = ?, " +
                "role = ? " +
                "WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getLastName());
            pstmt.setString(5, user.getProfilePicturePath());
            pstmt.setString(6, user.getRole().name());
            pstmt.setInt(7, user.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Користувач з ID " + user.getId() + " успішно оновлений.");
            } else {
                System.out.println("Користувача з ID " + user.getId() + " не знайдено.");
            }

        } catch (SQLException e) {
            System.err.println("SQL Помилка під час оновлення: " + e.getMessage());
            throw e;
        }
    }

    public static User authenticateUser(String email, String rawPassword) {
        User user = getUserByEmail(email);

        if (user == null) {
            System.out.println("Користувача з email '" + email + "' не знайдено.");
            return null;
        }

        boolean passwordMatches = HashData.verifyPassword(rawPassword, user.getPasswordHash());

        if (passwordMatches) {
            System.out.println("Аутентифікація успішна для користувача '" + user.getName() + "'. Роль: " + user.getRole());
            return user;
        } else {
            System.out.println("Невірна комбінація email/пароля для '" + email + "'.");
            return null;
        }
    }

    public static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
            System.out.println("Ресурси бази даних закриті.");
        } catch (SQLException e) {
            System.err.println("Помилка закриття ресурсів бази даних:");
            e.printStackTrace();
        }
    }
}
