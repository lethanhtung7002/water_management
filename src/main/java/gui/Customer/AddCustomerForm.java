/**
 * Form thêm mới hoặc chỉnh sửa thông tin khách hàng.
 * 
 * Chức năng:
 * - Thêm khách hàng mới vào hệ thống
 * - Chỉnh sửa thông tin khách hàng đã có
 * - Validate dữ liệu trước khi lưu
 * 
 * Cách sử dụng:
 * - new AddCustomerForm() - Tạo form thêm mới
 * - new AddCustomerForm(customer) - Tạo form chỉnh sửa với dữ liệu có sẵn
 * 
 * @author Your Name
 * @version 1.0
 */

package gui.Customer;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dao.CustomerDao;
import model.Customer;
import model.loaiCustomer;

public class AddCustomerForm extends JFrame {

    // ===== LABELS =====
    private JLabel lbAdd = new JLabel("Thêm Khách Hàng");
    private JLabel lbName = new JLabel("Name:");
    private JLabel lbLoaiUser = new JLabel("Loại Khách Hàng:");
    private JLabel lbCCCD = new JLabel("CCCD:");
    private JLabel lbPhone = new JLabel("Phone Number:");
    private JLabel lbEmail = new JLabel("Email:");

    // ===== INPUT FIELDS =====
    private JTextField tfName = new JTextField();
    private JComboBox<loaiCustomer> cbLoaiUser;
    private JTextField tfCCCD = new JTextField();
    private JTextField tfPhone = new JTextField();
    private JTextField tfEmail = new JTextField();

    // ===== BUTTONS =====
    private JButton btnSave = new JButton("Save");
    private JButton btnCancel = new JButton("Cancel");

    // ===== DAO =====
    private CustomerDao userDao = new CustomerDao();

    /**
     * Constructor cho form thêm khách hàng mới.
     * Khởi tạo form trống để người dùng nhập thông tin mới.
     */
    public AddCustomerForm() {
        setTitle("Thêm Khách Hàng");
        init();

        // Lưu khách hàng mới
        btnSave.addActionListener(e -> saveUser());
        btnCancel.addActionListener(e -> dispose());
    }

    /**
     * Constructor cho form chỉnh sửa thông tin khách hàng.
     * Tự động điền thông tin khách hàng hiện tại vào form.
     * 
     * @param user Đối tượng Customer cần chỉnh sửa
     */
    public AddCustomerForm(Customer user) {
        setTitle("Sửa thông tin Khách Hàng");
        init();

        // Điền thông tin người dùng vào form
        tfName.setText(user.getNameCustomer());
        tfCCCD.setText(user.getCCCD());
        tfPhone.setText(user.getPhoneCustomer());
        tfEmail.setText(user.getEmail());

        // Chọn loại người dùng dựa trên ID (không phải tên)
        for (int i = 0; i < cbLoaiUser.getItemCount(); i++) {
            loaiCustomer lnd = cbLoaiUser.getItemAt(i);
            if (lnd.getIdLoaiCustomer() == user.getIdCustomer()) { // So sánh ID
                cbLoaiUser.setSelectedIndex(i);
                break;
            }
        }

        // Cập nhật khách hàng đã có
        btnSave.addActionListener(e -> saveUser(user));
        btnCancel.addActionListener(e -> dispose());
    }

    /**
     * Lưu khách hàng mới vào database.
     * Validate dữ liệu trước khi lưu và hiển thị thông báo kết quả.
     */
    private void saveUser() {
        // Lấy loại khách hàng đã chọn từ ComboBox
        loaiCustomer selectedLoaiCustomer = (loaiCustomer) cbLoaiUser.getSelectedItem();

        // Validate: Kiểm tra đã chọn loại khách hàng chưa
        if (selectedLoaiCustomer == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn loại người dùng hợp lệ.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo đối tượng Customer mới với ID = 0 (auto increment)
        Customer user = new Customer(
                0, // ID sẽ được tự động tạo bởi database
                tfName.getText(),
                selectedLoaiCustomer.getIdLoaiCustomer(), // Lưu ID thay vì tên
                tfCCCD.getText(),
                tfPhone.getText(),
                tfEmail.getText());

        // Thực hiện thêm vào database
        if (userDao.addCustomer(user)) {
            JOptionPane.showMessageDialog(this, "User added successfully!");
            dispose(); // Đóng form sau khi thêm thành công
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add user.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cập nhật thông tin khách hàng đã có trong database.
     * 
     * @param ID_KhachHang Đối tượng Customer cũ (chứa ID cần update)
     */
    private void saveUser(Customer ID_KhachHang) {
        // Lấy loại khách hàng đã chọn từ ComboBox
        loaiCustomer selectedLoaiCustomer = (loaiCustomer) cbLoaiUser.getSelectedItem();

        // Validate: Kiểm tra đã chọn loại khách hàng chưa
        if (selectedLoaiCustomer == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn loại người dùng hợp lệ.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo đối tượng Customer với thông tin mới nhưng giữ nguyên ID
        Customer user = new Customer(
                ID_KhachHang.getIdCustomer(), // Giữ nguyên ID để update
                tfName.getText(),
                selectedLoaiCustomer.getIdLoaiCustomer(), // Lưu ID thay vì tên
                tfCCCD.getText(),
                tfPhone.getText(),
                tfEmail.getText());

        // Thực hiện cập nhật trong database
        if (userDao.updateCustomer(user)) {
            JOptionPane.showMessageDialog(this, "User updated successfully!");
            dispose(); // Đóng form sau khi cập nhật thành công
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update user.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Khởi tạo giao diện form.
     * Sử dụng Absolute Layout (null layout) để đặt vị trí các component.
     * 
     * Layout:
     * - Load danh sách loại khách hàng từ database vào ComboBox
     * - Đặt vị trí các label, textfield, button theo tọa độ cố định
     * - Thiết lập kích thước form 400x400
     */
    public void init() {
        setSize(400, 400);
        setLayout(null); // Sử dụng absolute positioning
        setLocationRelativeTo(null); // Căn giữa màn hình

        // Load danh sách loại khách hàng từ database
        cbLoaiUser = new JComboBox<>();
        List<loaiCustomer> loaiNguoiDung = userDao.getLoaiKhachHang();
        for (loaiCustomer lnd : loaiNguoiDung) {
            cbLoaiUser.addItem(lnd);
        }

        // Định nghĩa vị trí và kích thước các component
        int height = 30; // Chiều cao chung cho các component
        int x_lb = 50; // Vị trí x của labels
        int x_tf_cb = 150; // Vị trí x của textfields và combobox

        // Đặt vị trí các component (x, y, width, height)
        lbAdd.setBounds(x_tf_cb, 10, 200, 30);
        lbName.setBounds(x_lb, 60, 100, height);
        tfName.setBounds(x_tf_cb, 60, 200, height);
        lbLoaiUser.setBounds(x_lb, 100, 100, height);
        cbLoaiUser.setBounds(x_tf_cb, 100, 200, height);
        lbCCCD.setBounds(x_lb, 140, 100, height);
        tfCCCD.setBounds(x_tf_cb, 140, 200, height);
        lbPhone.setBounds(x_lb, 180, 100, height);
        tfPhone.setBounds(x_tf_cb, 180, 200, height);
        lbEmail.setBounds(x_lb, 220, 100, height);
        tfEmail.setBounds(x_tf_cb, 220, 200, height);
        btnSave.setBounds(80, 280, 100, 30);
        btnCancel.setBounds(220, 280, 100, 30);

        // Thêm các component vào form
        add(lbAdd);
        add(lbName);
        add(tfName);
        add(lbLoaiUser);
        add(cbLoaiUser);
        add(lbCCCD);
        add(tfCCCD);
        add(lbPhone);
        add(tfPhone);
        add(lbEmail);
        add(tfEmail);
        add(btnSave);
        add(btnCancel);
    }
}

/**
 * Class test để chạy thử AddCustomerForm độc lập.
 */
class run_test {
    public static void main(String[] args) {
        // Mở form thêm khách hàng mới
        new AddCustomerForm().setVisible(true);

        // Test form chỉnh sửa (cần có Customer object)
        // Customer testCustomer = new Customer(1, "Test", 1, "123456", "0123456789",
        // "test@email.com");
        // new AddCustomerForm(testCustomer).setVisible(true);
    }
}