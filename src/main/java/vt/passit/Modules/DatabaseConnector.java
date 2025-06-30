package vt.passit.Modules;

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

    public static boolean addUser(String username, String pass, String email) {
        String hashedPassword = HashData.hashPassword(pass);

        Connection connection = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            connection = getConnection();
            if (connection != null) {
                String sql = "INSERT INTO Accounts (username, password_hash, email) VALUES (?, ?, ?)";
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, hashedPassword);
                pstmt.setString(3, email);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Користувача '" + username + "' успішно додано.");
                    success = true;
                } else {
                    System.out.println("Не вдалося додати користувача '" + username + "'.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при додаванні користувача '" + username + "':");
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
