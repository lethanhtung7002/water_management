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

public class AddUserForm extends JFrame {
    JLabel lbAdd = new JLabel("Thêm Khách Hàng");

    JLabel lbName = new JLabel("Name:");
    JLabel lbLoaiUser = new JLabel("Loại Khách Hàng:");
    JLabel lbCCCD = new JLabel("CCCD:");
    JLabel lbPhone = new JLabel("Phone Number:");
    JLabel lbEmail = new JLabel("Email:");

    JTextField tfName = new JTextField();
    JComboBox<LoaiCustomer> tfLoaiUser;
    JTextField tfCCCD = new JTextField();
    JTextField tfPhone = new JTextField();
    JTextField tfEmail = new JTextField();

    JButton btnSave = new JButton("Save");
    JButton btnCancel = new JButton("Cancel");

    UserDao userDao = new UserDao();

    public AddUserForm() {
        setTitle("Thêm Khách Hàng");
        init();

        btnSave.addActionListener(e -> saveUser());
        btnCancel.addActionListener(e -> dispose());
    }

    public AddUserForm(Customer user) {
        setTitle("Sửa thông tin Khách Hàng");
        init();

        // diền thông tin người dùng vào form
        tfName.setText(user.getNameCustomer());
        tfCCCD.setText(user.getCCCD());
        tfPhone.setText(user.getPhoneCustomer());
        tfEmail.setText(user.getEmail());

        // chọn loại người dùng dựa trên thông tin hiện có
        for (int i = 0; i < tfLoaiUser.getItemCount(); i++) {
            LoaiCustomer lnd = tfLoaiUser.getItemAt(i);
            if (lnd.getTenLoaiCustomer().equals(user.getLoaiCustomer())) {
                tfLoaiUser.setSelectedIndex(i);
                break;
            }
        }

        btnSave.addActionListener(e -> saveUser(user));
        btnCancel.addActionListener(e -> dispose());
    }

    private void saveUser() {
        // Lấy LoaiCustomer đã chọn
        LoaiCustomer selectedLoaiCustomer = (LoaiCustomer) tfLoaiUser.getSelectedItem();
        if (selectedLoaiCustomer == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại người dùng hợp lệ.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer user = new Customer(
                0,
                tfName.getText(),
                selectedLoaiCustomer.getTenLoaiCustomer(),
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

    private void saveUser(Customer ID_KhachHang){
        // Lấy LoaiCustomer đã chọn
        LoaiCustomer selectedLoaiCustomer = (LoaiCustomer) tfLoaiUser.getSelectedItem();
        if (selectedLoaiCustomer == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại người dùng hợp lệ.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer user = new Customer(
                ID_KhachHang.getIdCustomer(),
                tfName.getText(),
                selectedLoaiCustomer.getTenLoaiCustomer(),
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

    private void loadLoaiNguoiDung() {
        List<LoaiCustomer> loaiNguoiDung = userDao.getLoaiKhachHang();
        for (LoaiCustomer lnd : loaiNguoiDung) {
            tfLoaiUser.addItem(lnd);
        }
    }

    public void init() {
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        tfLoaiUser = new JComboBox<>();
        loadLoaiNguoiDung();

        lbAdd.setBounds(150, 10, 200, 30);
        lbName.setBounds(50, 60, 100, 25);
        tfName.setBounds(150, 60, 200, 25);
        lbLoaiUser.setBounds(50, 100, 100, 25);
        tfLoaiUser.setBounds(150, 100, 200, 25);
        lbCCCD.setBounds(50, 140, 100, 25);
        tfCCCD.setBounds(150, 140, 200, 25);
        lbPhone.setBounds(50, 180, 100, 25);
        tfPhone.setBounds(150, 180, 200, 25);
        lbEmail.setBounds(50, 220, 100, 25);
        tfEmail.setBounds(150, 220, 200, 25);
        btnSave.setBounds(80, 280, 100, 30);
        btnCancel.setBounds(220, 280, 100, 30);

        add(lbAdd);
        add(lbName);
        add(tfName);
        add(lbLoaiUser);
        add(tfLoaiUser);
        add(lbCCCD);
        add(tfCCCD);
        add(lbPhone);
        add(tfPhone);
        add(lbEmail);
        add(tfEmail);
        add(btnSave);
        add(btnCancel);

    }

    // public static void main(String[] args) {
    //     AddUserForm addUserForm = new AddUserForm();
    //     addUserForm.setVisible(true);
    // }
}
