package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class quản lý kết nối và thực thi câu lệnh SQL với MySQL database.
 * 
 * Sử dụng Singleton pattern để đảm bảo chỉ có một kết nối duy nhất.
 * 
 * Các chức năng chính:
 * - Tạo và quản lý kết nối đến MySQL database
 * - Thực thi câu lệnh INSERT, UPDATE, DELETE
 * - Thực thi câu lệnh SELECT và trả về ResultSet
 * 
 * Cách sử dụng:
 * - MySQLConnect.ConnectQLN.executeQuery("SELECT * FROM khachhang")
 * - MySQLConnect.ConnectQLN.executeUpdate("INSERT INTO ...")
 * 
 * @author ltt
 * @version 1.0
 */
public class MySQLConnect {

    // ===== DATABASE CONNECTION =====
    /** Đối tượng Connection để kết nối với database */
    private Connection con = null;

    /** Statement để thực thi các câu lệnh SQL */
    private Statement stmt = null;

    // ===== SINGLETON INSTANCE =====
    /**
     * Instance duy nhất của MySQLConnect (Singleton pattern).
     * Sử dụng ConnectQLN để truy cập các phương thức kết nối database.
     */
    public static final MySQLConnect ConnectQLN = new MySQLConnect();

    /**
     * Constructor mặc định.
     * Tạo kết nối đến database "quanlynuoc" trên localhost:3306.
     */
    public MySQLConnect() {
        this("localhost", "3306", NameDB.DB_quanlynuoc);
    }

    /**
     * Constructor với tham số tùy chỉnh.
     * 
     * @param host   Địa chỉ host của MySQL server (vd: localhost, 127.0.0.1)
     * @param port   Port của MySQL server (mặc định: 3306)
     * @param dbName Tên database cần kết nối
     * @throws RuntimeException nếu kết nối thất bại
     */
    public MySQLConnect(String host, String port, String dbName) {
        try {
            // Thông tin đăng nhập MySQL
            String user = "root";
            String password = "1234";

            // Tạo URL kết nối với các tham số:
            // - useSSL=false: Tắt SSL (chỉ dùng trong development)
            // - allowPublicKeyRetrieval=true: Cho phép lấy public key từ server
            // - serverTimezone=UTC: Đặt timezone là UTC
            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            // Tạo kết nối
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối database thành công!");

        } catch (SQLException e) {
            System.out.println("Kết nối thất bại: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Thực thi các câu lệnh INSERT, UPDATE, DELETE.
     * 
     * @param sql Câu lệnh SQL cần thực thi (INSERT, UPDATE, DELETE)
     * @return Số dòng bị ảnh hưởng (affected rows), 0 nếu có lỗi
     */
    public int executeUpdate(String sql) {
        int result = 0;
        try (Statement stmt = con.createStatement()) {
            result = stmt.executeUpdate(sql);
            System.out.println("""
                    Cập nhật dữ liệu thành công
                    Lệnh thực hiện: %s
                    """.formatted(sql));
        } catch (SQLException e) {
            System.out.println("lenh: "+sql);
            System.out.println("Lỗi SQL (Update): " + e.getMessage());
        }
        return result;
    }

    /**
     * Thực thi câu lệnh SELECT và trả về ResultSet.
     * 
     * Lưu ý:
     * - ResultSet cần được đóng sau khi sử dụng xong
     * 
     * @param sql Câu lệnh SELECT cần thực thi
     * @return ResultSet chứa kết quả truy vấn, null nếu có lỗi
     */
    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            System.out.println("""
                    Lấy dữ liệu thành công
                    Lệnh thực hiện: %s
                    """.formatted(sql));
        } catch (SQLException e) {
            System.out.println("Lỗi SQL (Query): " + e.getMessage());
        }
        return rs;
    }

    /**
     * Đóng kết nối database.
     * Nên gọi method này khi ứng dụng kết thúc.
     */
    public void closeConnection() {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
            System.out.println("Đóng kết nối database thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi đóng kết nối: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra xem kết nối còn hoạt động không.
     * 
     * @return true nếu kết nối còn hoạt động, false nếu đã đóng hoặc null
     */
    public boolean isConnected() {
        try {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Lấy đối tượng Connection hiện tại.
     * Dùng khi cần thực hiện các thao tác phức tạp với Connection.
     * 
     * @return Connection object
     */
    public Connection getConnection() {
        return con;
    }
}
