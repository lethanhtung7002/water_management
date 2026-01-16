package gui.Customer;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import dao.CustomerDao;
import model.Customer;
import model.loaiCustomer;

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
 * @author LTT
 * @version 2.0
 */
public class AddCustomerForm extends JFrame {

    // ===== INPUT FIELDS =====
    private JTextField tfName = new JTextField();
    private JComboBox<loaiCustomer> cbLoaiUser = new JComboBox<>();
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
     */
    public AddCustomerForm() {
        setTitle("Thêm Khách Hàng");
        init();

        // Lưu khách hàng mới
        btnSave.addActionListener(e -> saveUser());
        btnCancel.addActionListener(e -> dispose());

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
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

        // Chọn loại người dùng dựa trên ID
        for (int i = 0; i < cbLoaiUser.getItemCount(); i++) {
            loaiCustomer lnd = cbLoaiUser.getItemAt(i);
            if (lnd.getIdLoaiCustomer() == user.getLoaiCustomer()) {
                cbLoaiUser.setSelectedIndex(i);
                break;
            }
        }

        // Cập nhật khách hàng đã có
        btnSave.addActionListener(e -> saveUser(user));
        btnCancel.addActionListener(e -> dispose());

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Khởi tạo giao diện form với GridBagLayout.
     */
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load danh sách loại khách hàng từ database
        List<loaiCustomer> loaiNguoiDung = userDao.getLoaiKhachHang();
        for (loaiCustomer lnd : loaiNguoiDung) {
            cbLoaiUser.addItem(lnd);
        }

        // Thêm form panel vào center
        add(createFormPanel(), BorderLayout.CENTER);

        // Thêm button panel vào south
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    /**
     * Tạo form panel với GridBagLayout.
     * 
     * @return JPanel chứa form input
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30)); // Tăng vùng đệm

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Khoảng cách các hàng

        int row = 0;

        // ===== TITLE =====
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JLabel lbTitle = new JLabel(getTitle());
        lbTitle.setFont(GUIConstants.Fonts.TieuDe);
        panel.add(lbTitle, gbc);

        // Reset settings
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST; // căn lề cho tiêu đề

        // ===== NAME =====
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lbName = new JLabel("Họ Tên:");
        lbName.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbName, gbc);

        gbc.gridx = 1;
        tfName.setPreferredSize(GUIConstants.Sizes.tf);
        tfName.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(tfName, gbc);
        row++;

        // ===== LOẠI KHÁCH HÀNG =====
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lbLoai = new JLabel("Loại Khách Hàng:");
        lbLoai.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbLoai, gbc);

        gbc.gridx = 1;
        cbLoaiUser.setPreferredSize(GUIConstants.Sizes.tf);
        cbLoaiUser.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(cbLoaiUser, gbc);
        row++;

        // ===== CCCD =====
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lbCCCD = new JLabel("CCCD:");
        lbCCCD.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbCCCD, gbc);

        gbc.gridx = 1;
        tfCCCD.setPreferredSize(GUIConstants.Sizes.tf);
        tfCCCD.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(tfCCCD, gbc);
        row++;

        // ===== PHONE NUMBER =====
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lbPhone = new JLabel("Phone Number:");
        lbPhone.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbPhone, gbc);

        gbc.gridx = 1;
        tfPhone.setPreferredSize(GUIConstants.Sizes.tf);
        tfPhone.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(tfPhone, gbc);
        row++;

        // ===== EMAIL =====
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lbEmail = new JLabel("Email:");
        lbEmail.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbEmail, gbc);

        gbc.gridx = 1;
        tfEmail.setPreferredSize(GUIConstants.Sizes.tf);
        tfEmail.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(tfEmail, gbc);

        return panel;
    }

    /**
     * Tạo panel chứa các nút Save và Cancel.
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        btnSave.setPreferredSize(GUIConstants.Sizes.btn); // kích thước button
        btnCancel.setPreferredSize(GUIConstants.Sizes.btn);

        btnSave.setFont(GUIConstants.Fonts.TieuDePhu); // font button
        btnCancel.setFont(GUIConstants.Fonts.TieuDePhu);

        panel.add(btnSave);
        panel.add(btnCancel);

        return panel;
    }

    /**
     * Lưu khách hàng mới vào database.
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

        // Tạo đối tượng Customer mới
        Customer user = new Customer(
                0, // ID sẽ được tự động tạo
                tfName.getText(),
                selectedLoaiCustomer.getIdLoaiCustomer(),
                tfCCCD.getText(),
                tfPhone.getText(),
                tfEmail.getText());

        // Thực hiện thêm vào database
        if (userDao.addCustomer(user)) {
            JOptionPane.showMessageDialog(this, "User added successfully!");
            dispose();
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
        // Lấy loại khách hàng đã chọn
        loaiCustomer selectedLoaiCustomer = (loaiCustomer) cbLoaiUser.getSelectedItem();

        // Validate
        if (selectedLoaiCustomer == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn loại người dùng hợp lệ.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo đối tượng Customer với thông tin mới
        Customer user = new Customer(
                ID_KhachHang.getIdCustomer(), // Giữ nguyên ID
                tfName.getText(),
                selectedLoaiCustomer.getIdLoaiCustomer(),
                tfCCCD.getText(),
                tfPhone.getText(),
                tfEmail.getText());

        // Thực hiện cập nhật
        if (userDao.updateCustomer(user)) {
            JOptionPane.showMessageDialog(this, "User updated successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update user.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Method main để test form.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AddCustomerForm();
        });
    }
}