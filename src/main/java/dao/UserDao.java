package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import model.LoaiCustomer;

public class UserDao {
    private MySQLConnect mySQLConnect = new MySQLConnect();

    // lấy danh sách người dùng
    public ArrayList<Customer> getUsers() {
        ArrayList<Customer> userList = new ArrayList<>();
        try {
            String query = "SELECT * FROM %s".formatted(DB_CustomerCol.TableName);
            ResultSet rs = mySQLConnect.executeQuery(query);
            while (rs.next()) {
                Customer user = new Customer();
                user.setIdCustomer(rs.getInt(DB_CustomerCol.ID));
                user.setLoaiCustomer(rs.getInt(DB_CustomerCol.Loai));
                user.setNameCustomer(rs.getString(DB_CustomerCol.Name));
                user.setCCCD(rs.getString(DB_CustomerCol.CCCD));
                user.setPhoneCustomer(rs.getString(DB_CustomerCol.PhoneNumber));
                user.setEmail(rs.getString(DB_CustomerCol.Email));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy người dùng: " + e.getMessage());
        }
        return userList;
    }

    // thêm người dùng
    public boolean addUser(Customer user) {
        int result = 0;
        String query = """
                INSERT INTO %s (%s, %s, %s, %s, %s)
                VALUES ('%d', '%s', '%s', '%s', '%s')
                """.formatted(
                DB_CustomerCol.TableName, DB_CustomerCol.Loai, DB_CustomerCol.Name, DB_CustomerCol.CCCD,
                DB_CustomerCol.PhoneNumber, DB_CustomerCol.Email,
                user.getLoaiCustomer(), user.getNameCustomer(), user.getCCCD(),
                user.getPhoneCustomer(), user.getEmail());

        try {
            result = mySQLConnect.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi thêm người dùng: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    // cập nhật người dùng
    public boolean updateUser(Customer user) {
        int result = 0;
        String query = """
                UPDATE %s SET
                        %s = '%s',
                        %s = '%s',
                        %s = '%s',
                        %s = '%s',
                        %s = '%s'
                WHERE %s = %d""".formatted(
                DB_CustomerCol.TableName,
                DB_CustomerCol.Loai, user.getLoaiCustomer(),
                DB_CustomerCol.Name, user.getNameCustomer(),
                DB_CustomerCol.CCCD, user.getCCCD(),
                DB_CustomerCol.PhoneNumber, user.getPhoneCustomer(),
                DB_CustomerCol.Email, user.getEmail(),
                DB_CustomerCol.ID, user.getIdCustomer());
        System.out.println(query);
        try {
            result = mySQLConnect.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật người dùng: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    // tìm người dùng theo ID
    public Customer getUserById(int idUser) {
        Customer user = null;
        try {
            String query = "SELECT * FROM %s WHERE %s = %d"
                    .formatted(DB_CustomerCol.TableName, DB_CustomerCol.ID, idUser);
            ResultSet rs = mySQLConnect.executeQuery(query);
            if (rs.next()) {
                user = new Customer();
                user.setIdCustomer(rs.getInt(DB_CustomerCol.ID));
                user.setLoaiCustomer(rs.getInt(DB_CustomerCol.Loai));
                user.setNameCustomer(rs.getString(DB_CustomerCol.Name));
                user.setCCCD(rs.getString(DB_CustomerCol.CCCD));
                user.setPhoneCustomer(rs.getString(DB_CustomerCol.PhoneNumber));
                user.setEmail(rs.getString(DB_CustomerCol.Email));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy người dùng theo ID: " + e.getMessage());
        }
        return user;
    }

    // xóa người dùng theo ID
    public boolean deleteUserById(int idUser) {
        int result = 0;
        String query = "DELETE FROM %s WHERE %s = %d"
                .formatted(DB_CustomerCol.TableName, DB_CustomerCol.ID, idUser);
        try {
            result = mySQLConnect.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi xóa người dùng: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    public List<LoaiCustomer> getLoaiKhachHang() {
        List<LoaiCustomer> loaiNguoiDungList = new ArrayList<>();
        try {
            String query = "SELECT * FROM %s"
                    .formatted(DB_CustomerTypeCol.TableName);
            ResultSet rs = mySQLConnect.executeQuery(query);
            while (rs.next()) {
                LoaiCustomer loaiNguoiDung = new LoaiCustomer(
                        rs.getInt(DB_CustomerTypeCol.ID),
                        rs.getString(DB_CustomerTypeCol.Name));
                loaiNguoiDungList.add(loaiNguoiDung);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy loại người dùng: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return loaiNguoiDungList;
    }
}
