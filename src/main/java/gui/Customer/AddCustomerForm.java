package gui.Customer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import dao.CustomerDao;
import dao.HoSuDungDao;
import gui.GUIConstants;
import model.Customer;
import model.HoSuDung;
import model.LoaiCustomer;

/**
 * Form quản lý khách hàng và hộ sử dụng.
 * 
 * Chức năng:
 * - Thêm mới khách hàng
 * - Sửa thông tin khách hàng
 * - Quản lý hộ sử dụng của khách hàng (khi sửa)
 * 
 * Cách sử dụng:
 * - new AddCustomerForm() → Thêm mới khách hàng
 * - new AddCustomerForm(Customer) → Sửa khách hàng + quản lý hộ sử dụng
 * 
 * @author Lê Thanh Tùng
 * @version 2.0
 */
public class AddCustomerForm extends JFrame {

    // ===== LABELS =====
    private JLabel lbName = new JLabel("Họ Tên:");
    private JLabel lbLoaiKH = new JLabel("Loại Khách Hàng:");
    private JLabel lbCCCD = new JLabel("CCCD:");
    private JLabel lbPhone = new JLabel("Số điện thoại:");
    private JLabel lbEmail = new JLabel("Email:");

    // ===== INPUT FIELDS =====
    private JTextField tfName = new JTextField();
    private JComboBox<LoaiCustomer> cbLoaiUser = new JComboBox<>();
    private JTextField tfCCCD = new JTextField();
    private JTextField tfPhone = new JTextField();
    private JTextField tfEmail = new JTextField();

    // ===== BUTTONS - Main Form =====
    private JButton btnSave = new JButton("Lưu");
    private JButton btnUpdate = new JButton("Cập nhật");
    private JButton btnCancel = new JButton("Hủy");

    // ===== BUTTONS - Hộ sử dụng Management =====
    private JButton btnRefresh = new JButton("Làm mới");
    private JButton btnAddHoSuDung = new JButton("Thêm hộ sử dụng");
    private JButton btnEditHoSuDung = new JButton("Sửa hộ sử dụng");
    private JButton btnDeleteHoSuDung = new JButton("Xóa hộ sử dụng");

    // ===== DAO =====
    private CustomerDao customerDao = new CustomerDao();
    private HoSuDungDao hoSuDungDao = new HoSuDungDao();

    // ===== TABLE =====
    private JScrollPane hoSuDungScrollPane;
    private JTable hoSuDungTable;
    private DefaultTableModel hoSuDungTableModel;
    private ArrayList<HoSuDung> hoSuDungList = new ArrayList<>();

    private Customer customer;

    /**
     * Constructor cho chế độ THÊM MỚI khách hàng.
     * Hiển thị form trống để nhập thông tin khách hàng mới.
     */
    public AddCustomerForm() {
        setTitle("Thêm Khách Hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load dữ liệu cho ComboBox
        loadCustomerTypes();

        // Tạo giao diện
        add(createFormPanel(false), BorderLayout.CENTER);
        add(createButtonPanel(true), BorderLayout.SOUTH);

        // Gắn sự kiện
        btnSave.addActionListener(e -> saveCustomer());
        btnCancel.addActionListener(e -> dispose());

        // Hiển thị form
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Constructor cho chế độ SỬA khách hàng.
     * Hiển thị thông tin khách hàng hiện có và cho phép quản lý hộ sử dụng.
     * 
     * @param customer Đối tượng Customer cần chỉnh sửa
     */
    public AddCustomerForm(Customer customer) {
        this.customer = customer;
        setTitle("Sửa thông tin Khách Hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load dữ liệu cho ComboBox
        loadCustomerTypes();

        // Tạo panel bên trái (form khách hàng)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(createFormPanel(true), BorderLayout.CENTER);
        leftPanel.add(createButtonPanel(false), BorderLayout.SOUTH);

        // Tạo panel bên phải (bảng hộ sử dụng)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(createHoSuDungButtonPanel(customer.getIdCustomer()), BorderLayout.NORTH);
        rightPanel.add(createHoSuDungTable(customer.getIdCustomer()), BorderLayout.CENTER);

        // Ghép 2 panel vào frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Điền dữ liệu vào form
        fillFormData(customer);

        // Gắn sự kiện
        btnUpdate.addActionListener(e -> updateCustomer(customer.getIdCustomer()));
        btnCancel.addActionListener(e -> dispose());

        // Hiển thị form
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ========================================
    // PHẦN 1: LOAD DỮ LIỆU
    // ========================================

    /**
     * Load danh sách loại khách hàng từ database vào ComboBox.
     */
    private void loadCustomerTypes() {
        cbLoaiUser.removeAllItems();
        List<LoaiCustomer> loaiNguoiDung = customerDao.getLoaiKhachHang();
        for (LoaiCustomer lnd : loaiNguoiDung) {
            cbLoaiUser.addItem(lnd);
        }
    }

    /**
     * Điền dữ liệu từ object Customer vào form (dùng cho chế độ sửa).
     * 
     * @param customer Đối tượng Customer chứa dữ liệu cần điền
     */
    private void fillFormData(Customer customer) {
        tfName.setText(customer.getNameCustomer());
        tfCCCD.setText(customer.getCCCD());
        tfPhone.setText(customer.getPhoneCustomer());
        tfEmail.setText(customer.getEmail());

        // Chọn loại khách hàng tương ứng
        for (int i = 0; i < cbLoaiUser.getItemCount(); i++) {
            LoaiCustomer lnd = cbLoaiUser.getItemAt(i);
            if (lnd.getIdLoaiCustomer() == customer.getLoaiCustomer()) {
                cbLoaiUser.setSelectedIndex(i);
                break;
            }
        }
    }

    // ========================================
    // PHẦN 2: TẠO GIAO DIỆN
    // ========================================

    /**
     * Tạo panel chứa form nhập liệu khách hàng.
     * 
     * @return JPanel chứa form
     */
    private JPanel createFormPanel(boolean b) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // ===== TIÊU ĐỀ =====
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JLabel lbTitle = new JLabel(getTitle());
        lbTitle.setFont(GUIConstants.Fonts.TieuDe);
        panel.add(lbTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = row++;
        JLabel lbTenKH = new JLabel(customer.getNameCustomer());
        lbTenKH.setFont(GUIConstants.Fonts.TieuDe);
        panel.add(lbTenKH, gbc);

        gbc.gridwidth = 1;

        // hiện thông tin cho phần edit
        if (b) {
            JLabel lbIDCustomer = new JLabel("ID Khách Hàng:");
            JTextField tfIDCustomer = new JTextField(String.valueOf(this.customer.getIdCustomer()));
            tfIDCustomer.setEditable(false);

            gbc.gridx = 0;
            gbc.gridy = row;
            lbIDCustomer.setFont(GUIConstants.Fonts.TieuDePhu);
            panel.add(lbIDCustomer, gbc);

            gbc.gridx = 1;
            tfIDCustomer.setPreferredSize(GUIConstants.Sizes.tf);
            tfIDCustomer.setFont(GUIConstants.Fonts.TieuDePhu);
            panel.add(tfIDCustomer, gbc);
            row++;
        }

        // ===== HỌ TÊN =====
        gbc.gridx = 0;
        gbc.gridy = row;
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
        lbLoaiKH.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbLoaiKH, gbc);

        gbc.gridx = 1;
        cbLoaiUser.setPreferredSize(GUIConstants.Sizes.tf);
        cbLoaiUser.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(cbLoaiUser, gbc);
        row++;

        // ===== CCCD =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbCCCD.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbCCCD, gbc);

        gbc.gridx = 1;
        tfCCCD.setPreferredSize(GUIConstants.Sizes.tf);
        tfCCCD.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(tfCCCD, gbc);
        row++;

        // ===== SỐ ĐIỆN THOẠI =====
        gbc.gridx = 0;
        gbc.gridy = row;
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
        lbEmail.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbEmail, gbc);

        gbc.gridx = 1;
        tfEmail.setPreferredSize(GUIConstants.Sizes.tf);
        tfEmail.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(tfEmail, gbc);

        return panel;
    }

    /**
     * Tạo panel chứa các nút Save/Update và Cancel.
     * 
     * @param isAddMode true = hiển thị nút "Lưu", false = hiển thị nút "Cập nhật"
     * @return JPanel chứa các nút
     */
    private JPanel createButtonPanel(boolean isAddMode) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        btnSave.setPreferredSize(GUIConstants.Sizes.btn);
        btnUpdate.setPreferredSize(GUIConstants.Sizes.btn);
        btnCancel.setPreferredSize(GUIConstants.Sizes.btn);

        btnSave.setFont(GUIConstants.Fonts.TieuDePhu);
        btnUpdate.setFont(GUIConstants.Fonts.TieuDePhu);
        btnCancel.setFont(GUIConstants.Fonts.TieuDePhu);

        if (isAddMode) {
            panel.add(btnSave);
        } else {
            panel.add(btnUpdate);
        }
        panel.add(btnCancel);

        return panel;
    }

    /**
     * Tạo panel chứa các nút quản lý hộ sử dụng.
     * 
     * @param customerId ID của khách hàng
     * @return JPanel chứa các nút quản lý
     */
    private JPanel createHoSuDungButtonPanel(int customerId) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));

        TitledBorder titledBorder = BorderFactory.createTitledBorder("Quản lý hộ sử dụng");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));
        panel.setBorder(titledBorder);

        panel.add(btnRefresh);
        panel.add(btnAddHoSuDung);
        panel.add(btnEditHoSuDung);
        panel.add(btnDeleteHoSuDung);

        // Gắn sự kiện
        btnRefresh.addActionListener(e -> refreshHoSuDungTable(customerId));

        btnAddHoSuDung.addActionListener(e -> {
            new AddHoSuDungForm(customerId);
        });

        btnEditHoSuDung.addActionListener(e -> {
            int selectedRow = hoSuDungTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn hộ sử dụng cần sửa!",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            HoSuDung selected = hoSuDungList.get(selectedRow);
            new AddHoSuDungForm(selected);
        });

        btnDeleteHoSuDung.addActionListener(e -> {
            int selectedRow = hoSuDungTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn hộ sử dụng cần xóa!",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa hộ sử dụng này?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                HoSuDung selected = hoSuDungList.get(selectedRow);
                if (hoSuDungDao.delete_HoSuDung(selected.getID_HoSuDung())) {
                    showSuccess("Xóa hộ sử dụng thành công!");
                    refreshHoSuDungTable(customerId);
                } else {
                    showError("Xóa hộ sử dụng thất bại!");
                }
            }
        });

        return panel;
    }

    /**
     * Tạo bảng hiển thị các hộ sử dụng.
     * 
     * @param customerId ID của khách hàng
     * @return JScrollPane chứa bảng
     */
    private JScrollPane createHoSuDungTable(int customerId) {
        String[] columns = { "Mã hộ sử dụng", "Địa chỉ", "Khu vực", "Trạng thái" };

        hoSuDungTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        hoSuDungTable = new JTable(hoSuDungTableModel);
        hoSuDungTable.setRowHeight(25);
        hoSuDungTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        hoSuDungTable.getTableHeader().setReorderingAllowed(false);
        hoSuDungTable.getTableHeader().setBackground(new Color(130, 204, 130));

        // Load dữ liệu
        loadHoSuDungData(customerId);

        hoSuDungScrollPane = new JScrollPane(hoSuDungTable);
        hoSuDungScrollPane.setPreferredSize(new Dimension(500, 300));

        return hoSuDungScrollPane;
    }

    /**
     * Load dữ liệu hộ sử dụng từ database vào bảng.
     * 
     * @param customerId ID của khách hàng
     */
    private void loadHoSuDungData(int customerId) {
        hoSuDungTableModel.setRowCount(0);

        try {
            this.hoSuDungList = hoSuDungDao.getHoSuDungByCustomerId(customerId);

            for (HoSuDung hsd : hoSuDungList) {
                String trangThai = hsd.getTrangThai() == 1 ? "Đang sử dụng" : "Ngừng sử dụng";
                Object[] row = {
                        hsd.getID_HoSuDung(),
                        hsd.getDiaChi(),
                        hsd.getKhuVuc(),
                        trangThai
                };
                hoSuDungTableModel.addRow(row);
            }

            System.out.println("Đã load " + hoSuDungList.size() + " hộ sử dụng");

        } catch (Exception e) {
            System.out.println("Lỗi khi load hộ sử dụng: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu hộ sử dụng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Làm mới bảng hộ sử dụng.
     * 
     * @param customerId ID của khách hàng
     */
    private void refreshHoSuDungTable(int customerId) {
        loadHoSuDungData(customerId);
        JOptionPane.showMessageDialog(this,
                "Đã làm mới dữ liệu hộ sử dụng!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ========================================
    // PHẦN 3: XỬ LÝ LOGIC
    // ========================================

    /**
     * Lưu khách hàng mới vào database.
     */
    private void saveCustomer() {
        try {
            System.out.println("=== SAVE CUSTOMER ===");

            // Validate loại khách hàng
            LoaiCustomer selectedLoaiCustomer = (LoaiCustomer) cbLoaiUser.getSelectedItem();
            if (selectedLoaiCustomer == null) {
                showError("Vui lòng chọn loại khách hàng!");
                return;
            }

            // Validate các field
            String name = tfName.getText().trim();
            if (name.isEmpty()) {
                showError("Vui lòng nhập họ tên!");
                return;
            }

            String cccd = tfCCCD.getText().trim();
            if (cccd.isEmpty()) {
                showError("Vui lòng nhập CCCD!");
                return;
            }

            String phone = tfPhone.getText().trim();
            if (phone.isEmpty()) {
                showError("Vui lòng nhập số điện thoại!");
                return;
            }

            String email = tfEmail.getText().trim();
            if (email.isEmpty()) {
                showError("Vui lòng nhập email!");
                return;
            }

            // Tạo object Customer
            Customer customer = new Customer(
                    0, // ID tự động tăng
                    name,
                    selectedLoaiCustomer.getIdLoaiCustomer(),
                    cccd,
                    phone,
                    email);

            // Lưu vào database
            boolean success = customerDao.addCustomer(customer);
            System.out.println("Kết quả: " + success);

            if (success) {
                showSuccess("Thêm khách hàng thành công!");
                dispose();
            } else {
                showError("Thêm khách hàng thất bại!");
            }

        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            showError("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Cập nhật thông tin khách hàng.
     * 
     * @param customerId ID của khách hàng cần cập nhật
     */
    private void updateCustomer(int customerId) {
        try {
            // Validate loại khách hàng
            LoaiCustomer selectedLoaiCustomer = (LoaiCustomer) cbLoaiUser.getSelectedItem();
            if (selectedLoaiCustomer == null) {
                showError("Vui lòng chọn loại khách hàng!");
                return;
            }

            // Validate các field
            String name = tfName.getText().trim();
            if (name.isEmpty()) {
                showError("Vui lòng nhập họ tên!");
                return;
            }

            String cccd = tfCCCD.getText().trim();
            if (cccd.isEmpty()) {
                showError("Vui lòng nhập CCCD!");
                return;
            }

            String phone = tfPhone.getText().trim();
            if (phone.isEmpty()) {
                showError("Vui lòng nhập số điện thoại!");
                return;
            }

            String email = tfEmail.getText().trim();
            if (email.isEmpty()) {
                showError("Vui lòng nhập email!");
                return;
            }

            // Tạo object Customer
            Customer customer = new Customer(
                    customerId, // Giữ nguyên ID
                    name,
                    selectedLoaiCustomer.getIdLoaiCustomer(),
                    cccd,
                    phone,
                    email);

            // Cập nhật database
            boolean success = customerDao.updateCustomer(customer);
            System.out.println("Kết quả: " + success);

            if (success) {
                showSuccess("Cập nhật khách hàng thành công!");
                dispose();
            } else {
                showError("Cập nhật khách hàng thất bại!");
            }

        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            showError("Lỗi: " + e.getMessage());
        }
    }

    // ========================================
    // PHẦN 4: HELPER METHODS
    // ========================================

    /**
     * Hiển thị dialog thông báo lỗi.
     * 
     * @param message Nội dung lỗi
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Hiển thị dialog thông báo thành công.
     * 
     * @param message Nội dung thông báo
     */
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Main method để test form.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test thêm mới
            // new CustomerForm();

            // Test sửa
            Customer test = new Customer(1, "Nguyễn Văn A", 1, "123456789", "0901234567", "test@email.com");
            new AddCustomerForm(test);
        });
    }
}