package gui.User;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dao.UserDao;
import model.LoaiCustomer;
import model.Customer;

public class AddCustomerForm extends JFrame {
    JLabel lbAdd = new JLabel("Thêm Khách Hàng");
    JLabel lbName = new JLabel("Name:");
    JLabel lbLoaiUser = new JLabel("Loại Khách Hàng:");
    JLabel lbCCCD = new JLabel("CCCD:");
    JLabel lbPhone = new JLabel("Phone Number:");
    JLabel lbEmail = new JLabel("Email:");

    JTextField tfName = new JTextField();
    JComboBox<LoaiCustomer> cbLoaiUser;
    JTextField tfCCCD = new JTextField();
    JTextField tfPhone = new JTextField();
    JTextField tfEmail = new JTextField();

    JButton btnSave = new JButton("Save");
    JButton btnCancel = new JButton("Cancel");

    UserDao userDao = new UserDao();

    public AddCustomerForm() {
        setTitle("Thêm Khách Hàng");
        init();

        btnSave.addActionListener(e -> saveUser());
        btnCancel.addActionListener(e -> dispose());
    }

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
            LoaiCustomer lnd = cbLoaiUser.getItemAt(i);
            if (lnd.getIdLoaiCustomer() == user.getIdCustomer()) { // So sánh ID
                cbLoaiUser.setSelectedIndex(i);
                break;
            }
        }

        btnSave.addActionListener(e -> saveUser(user));
        btnCancel.addActionListener(e -> dispose());
    }

    private void saveUser() {
        // Lấy LoaiCustomer đã chọn
        LoaiCustomer selectedLoaiCustomer = (LoaiCustomer) cbLoaiUser.getSelectedItem();
        if (selectedLoaiCustomer == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại người dùng hợp lệ.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer user = new Customer(
                0,
                tfName.getText(),
                selectedLoaiCustomer.getIdLoaiCustomer(), // Lưu ID thay vì tên
                tfCCCD.getText(),
                tfPhone.getText(),
                tfEmail.getText());

        if (userDao.addUser(user)) {
            JOptionPane.showMessageDialog(this, "User added successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveUser(Customer ID_KhachHang) {
        // Lấy LoaiCustomer đã chọn
        LoaiCustomer selectedLoaiCustomer = (LoaiCustomer) cbLoaiUser.getSelectedItem();
        if (selectedLoaiCustomer == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại người dùng hợp lệ.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer user = new Customer(
                ID_KhachHang.getIdCustomer(),
                tfName.getText(),
                selectedLoaiCustomer.getIdLoaiCustomer(), // Lưu ID thay vì tên
                tfCCCD.getText(),
                tfPhone.getText(),
                tfEmail.getText());

        if (userDao.updateUser(user)) {
            JOptionPane.showMessageDialog(this, "User updated successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void init() {
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        cbLoaiUser = new JComboBox<>();
        List<LoaiCustomer> loaiNguoiDung = userDao.getLoaiKhachHang();
        for (LoaiCustomer lnd : loaiNguoiDung) {
            cbLoaiUser.addItem(lnd);
        }

        int height = 30;
        int x_lb = 50;
        int x_tf_cb = 150;

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

class run_test {
    public static void main(String[] args) {
        new AddCustomerForm().setVisible(true);
    }
}