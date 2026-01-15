package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnect {
    Connection con = null;
    Statement stmt = null;

    public MySQLConnect() {
        this("localhost", "3306", NameDB.DB_quanlynuoc);
    }

    public MySQLConnect(String host, String port, String dbName) {
        try {
            String user = "root";
            String password = "1234";

            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            con = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            System.out.println("Kết nối thất bại: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // INSERT, UPDATE, DELETE
    public int executeUpdate(String sql) {
        int result = 0;
        try (Statement stmt = con.createStatement()) {
            result = stmt.executeUpdate(sql);
            System.out.println("""
                    Cập nhật dữ liệu thành công
                    Lệnh thực hiện: %s\n """.formatted(sql));
        } catch (SQLException e) {
            System.out.println("Lỗi SQL (Update): " + e.getMessage());
        }
        return result;
    }

    // SELECT
    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            System.out.println("""
                    Lấy dữ liệu thành công
                    Lệnh thực hiện: %s\n """.formatted(sql));
        } catch (SQLException e) {
            System.out.println("Lỗi SQL (Query): " + e.getMessage());
        }
        return rs;
    }
}

// class test{
//     public static void main(String[] args) {
//         new MySQLConnect();
//     }
// }