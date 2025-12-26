package dao;

import java.sql.*;

public class MySQLConnect {
    Connection con = null;
    Statement stmt = null;

    public MySQLConnect() {
        try {
            // 1. Thông tin cấu hình
            String host = "localhost";
            String port = "3306"; // Cổng mặc định của MySQL
            String dbName = "water_management";
            String user = "root"; // Mặc định thường là root
            String password = "1234";

            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC";

            con = DriverManager.getConnection(url, user, password);

            if (con != null) {
                System.out.println("Kết nối thành công đến cơ sở dữ liệu MySQL!");
            }
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println("Kết nối thất bại: " + e.getMessage());
            throw new RuntimeException(e);

        }
    }

    // Upate insert delete
    public int ExcuteDB(String query) {
        int result = 0;
        try {
            stmt = con.createStatement();
            result = stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Lỗi thực thi câu lệnh SQL: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Lỗi đóng Statement: " + e.getMessage());
            }
        }
        return result;
    }

    // Select
    public ResultSet getDB(String query) {
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Lỗi thực thi câu lệnh SQL: " + e.getMessage());
        }
        return rs;
    }

    public static void main(String[] args) {
        new MySQLConnect();
    }
}