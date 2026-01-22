package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import model.LoaiCustomer;
import static dao.MySQLConnect.ConnectQLN;
import static dao.QLNdbConstants.*;
import static dao.SharesDao.sharesDao;

/**
 * Data Access Object (DAO) cho bảng khách hàng.
 * 
 * Cung cấp các phương thức (Create, Read, Update, Delete)
 * để thao tác với dữ liệu khách hàng trong database.
 * 
 * Các chức năng chính:
 * - Lấy danh sách tất cả khách hàng
 * - Thêm khách hàng mới
 * - Cập nhật thông tin khách hàng
 * - Tìm kiếm khách hàng theo ID
 * - Xóa khách hàng theo ID
 * - Lấy danh sách loại khách hàng
 * 
 */
public class CustomerDao {

    /**
     * Lấy danh sách tất cả khách hàng từ database.
     * 
     * @return ArrayList chứa tất cả khách hàng, trả về list rỗng nếu có lỗi
     */
    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + Tables.Customer;
            ResultSet rs = ConnectQLN.executeQuery(query);

            // Duyệt qua từng dòng kết quả
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setIdCustomer(rs.getInt(Id.CustomerID));
                customer.setLoaiCustomer(rs.getInt(Id.CustomerTypeID));
                customer.setNameCustomer(rs.getString(CustomerCol.Name));
                customer.setCCCD(rs.getString(CustomerCol.CCCD));
                customer.setPhoneCustomer(rs.getString(CustomerCol.PhoneNumber));
                customer.setEmail(rs.getString(CustomerCol.Email));
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách người dùng: " + e.getMessage());
        }
        return customers;
    }

    /**
     * Thêm khách hàng mới vào database.
     * 
     * @param user Đối tượng Customer cần thêm (ID sẽ tự động tăng)
     * @return true nếu thêm thành công, false nếu thất bại
     * @throws RuntimeException nếu có lỗi khi thực thi câu lệnh SQL
     */
    public boolean addCustomer(Customer user) {
        int result = 0;

        // Tạo câu lệnh INSERT với String.formatted() để dễ đọc
        String query = """
                INSERT INTO %s (%s, %s, %s, %s, %s)
                VALUES ('%d', '%s', '%s', '%s', '%s')
                """.formatted(
                Tables.Customer,
                Id.CustomerTypeID, CustomerCol.Name, CustomerCol.CCCD,
                CustomerCol.PhoneNumber, CustomerCol.Email,
                user.getLoaiCustomer(), user.getNameCustomer(), user.getCCCD(),
                user.getPhoneCustomer(), user.getEmail());

        try {
            result = ConnectQLN.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi thêm người dùng: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    /**
     * Cập nhật thông tin khách hàng đã có trong database.
     * 
     * @param user Đối tượng Customer chứa thông tin mới (phải có ID hợp lệ)
     * @return true nếu cập nhật thành công, false nếu thất bại
     * @throws RuntimeException nếu có lỗi khi thực thi câu lệnh SQL
     */
    public boolean updateCustomer(Customer user) {
        int result = 0;

        // Tạo câu lệnh UPDATE
        String query = """
                UPDATE %s SET
                        %s = %d,
                        %s = '%s',
                        %s = '%s',
                        %s = '%s',
                        %s = '%s'
                WHERE %s = %d""".formatted(
                Tables.Customer,
                Id.CustomerTypeID, user.getLoaiCustomer(),
                CustomerCol.Name, user.getNameCustomer(),
                CustomerCol.CCCD, user.getCCCD(),
                CustomerCol.PhoneNumber, user.getPhoneCustomer(),
                CustomerCol.Email, user.getEmail(),
                Id.CustomerID, user.getIdCustomer());

        try {
            result = ConnectQLN.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật người dùng: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    /**
     * Tìm kiếm khách hàng theo ID.
     * 
     * @param idCustomer ID của khách hàng cần tìm
     * @return Đối tượng Customer nếu tìm thấy, null nếu không tìm thấy hoặc có lỗi
     */
    public Customer getCustomerById(int idCustomer) {
        Customer user = null;
        try {
            String query = "SELECT * FROM %s WHERE %s = %d"
                    .formatted(Tables.Customer, Id.CustomerID, idCustomer);
            ResultSet rs = ConnectQLN.executeQuery(query);

            // Chỉ lấy kết quả đầu tiên (nếu có)
            if (rs.next()) {
                user = new Customer();
                user.setIdCustomer(rs.getInt(Id.CustomerID));
                user.setLoaiCustomer(rs.getInt(Id.CustomerTypeID));
                user.setNameCustomer(rs.getString(CustomerCol.Name));
                user.setCCCD(rs.getString(CustomerCol.CCCD));
                user.setPhoneCustomer(rs.getString(CustomerCol.PhoneNumber));
                user.setEmail(rs.getString(CustomerCol.Email));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy người dùng theo ID: " + e.getMessage());
        }
        return user;
    }

    /**
     * Xóa khách hàng khỏi database theo ID.
     * 
     * Lưu ý: Phương thức này sẽ xóa vĩnh viễn khách hàng khỏi database.
     * Cần kiểm tra ràng buộc khóa ngoại trước khi xóa.
     * 
     * @param idUser ID của khách hàng cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     * @throws RuntimeException nếu có lỗi khi thực thi câu lệnh SQL
     */
    public boolean deleteUserById(int idUser) {
        return sharesDao.deleteByCol(idUser, Tables.Customer, Id.CustomerID);
    }

    /**
     * Lấy danh sách tất cả loại khách hàng từ database.
     * Ví dụ: Sinh hoạt, Sản xuất, Kinh doanh, Hành chính...
     * 
     * @return List chứa các loại khách hàng
     * @throws RuntimeException nếu có lỗi khi thực thi câu lệnh SQL
     */
    public List<LoaiCustomer> getLoaiKhachHang() {
        List<LoaiCustomer> loaiNguoiDungList = new ArrayList<>();
        try {
            String query = "SELECT * FROM %s"
                    .formatted(Tables.CustomerType);
            ResultSet rs = ConnectQLN.executeQuery(query);

            // Duyệt qua từng loại khách hàng
            while (rs.next()) {
                LoaiCustomer loaiNguoiDung = new LoaiCustomer(
                        rs.getInt(Id.CustomerTypeID),
                        rs.getString(CustomerTypeCol.Name));
                loaiNguoiDungList.add(loaiNguoiDung);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy loại người dùng: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return loaiNguoiDungList;
    }
}