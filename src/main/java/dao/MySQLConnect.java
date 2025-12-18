package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnect {
    public static void main(String[] args) {
        // 1. Thông tin cấu hình
        String host = "localhost";
        String port = "3306"; // Cổng mặc định của MySQL
        String dbName = "water_management";
        String user = "root"; // Mặc định thường là root
        String password = "1234";

        // 2. Chuỗi kết nối (Thêm múi giờ serverTimezone để tránh lỗi)
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC";

        // 3. Thực hiện kết nối
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Kết nối MySQL thành công!");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối: " + e.getMessage());
        }
    }
}