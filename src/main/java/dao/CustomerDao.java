package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import model.loaiCustomer;

public class CustomerDao {
    private MySQLConnect mySQLConnect = new MySQLConnect();

    // lấy danh sách người dùng
    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + qlnTableName.Customer;
            ResultSet rs = mySQLConnect.executeQuery(query);
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setIdCustomer(rs.getInt(qlnIDName.CustomerID));
                customer.setLoaiCustomer(rs.getInt(qlnIDName.CustomerTypeID));
                customer.setNameCustomer(rs.getString(qlnCustomerCol.Name));
                customer.setCCCD(rs.getString(qlnCustomerCol.CCCD));
                customer.setPhoneCustomer(rs.getString(qlnCustomerCol.PhoneNumber));
                customer.setEmail(rs.getString(qlnCustomerCol.Email));
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách người dùng: " + e.getMessage());
        }
        return customers;
    }

    // thêm người dùng
    public boolean addCustomer(Customer user) {
        int result = 0;
        String query = """
                INSERT INTO %s (%s, %s, %s, %s, %s)
                VALUES ('%d', '%s', '%s', '%s', '%s')
                """.formatted(
                qlnTableName.Customer, qlnIDName.CustomerTypeID, qlnCustomerCol.Name, qlnCustomerCol.CCCD,
                qlnCustomerCol.PhoneNumber, qlnCustomerCol.Email,
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
    public boolean updateCustomer(Customer user) {
        int result = 0;
        String query = """
                UPDATE %s SET
                        %s = '%s',
                        %s = '%s',
                        %s = '%s',
                        %s = '%s',
                        %s = '%s'
                WHERE %s = %d""".formatted(
                qlnTableName.Customer,
                qlnIDName.CustomerTypeID, user.getLoaiCustomer(),
                qlnCustomerCol.Name, user.getNameCustomer(),
                qlnCustomerCol.CCCD, user.getCCCD(),
                qlnCustomerCol.PhoneNumber, user.getPhoneCustomer(),
                qlnCustomerCol.Email, user.getEmail(),
                qlnIDName.CustomerID, user.getIdCustomer());
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
    public Customer getCustomerById(int idCustomer) {
        Customer user = null;
        try {
            String query = "SELECT * FROM %s WHERE %s = %d"
                    .formatted(qlnTableName.Customer, qlnIDName.CustomerID, idCustomer);
            ResultSet rs = mySQLConnect.executeQuery(query);
            if (rs.next()) {
                user = new Customer();
                user.setIdCustomer(rs.getInt(qlnIDName.CustomerID));
                user.setLoaiCustomer(rs.getInt(qlnIDName.CustomerTypeID));
                user.setNameCustomer(rs.getString(qlnCustomerCol.Name));
                user.setCCCD(rs.getString(qlnCustomerCol.CCCD));
                user.setPhoneCustomer(rs.getString(qlnCustomerCol.PhoneNumber));
                user.setEmail(rs.getString(qlnCustomerCol.Email));
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
                .formatted(qlnTableName.Customer, qlnIDName.CustomerID, idUser);
        try {
            result = mySQLConnect.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi xóa người dùng: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    public List<loaiCustomer> getLoaiKhachHang() {
        List<loaiCustomer> loaiNguoiDungList = new ArrayList<>();
        try {
            String query = "SELECT * FROM %s"
                    .formatted(qlnTableName.CustomerType);
            ResultSet rs = mySQLConnect.executeQuery(query);
            while (rs.next()) {
                loaiCustomer loaiNguoiDung = new loaiCustomer(
                        rs.getInt(qlnIDName.CustomerTypeID),
                        rs.getString(qlnCustomerTypeCol.Name));
                loaiNguoiDungList.add(loaiNguoiDung);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy loại người dùng: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return loaiNguoiDungList;
    }
}
