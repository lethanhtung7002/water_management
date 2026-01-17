package gui.GiaNuoc;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

import dao.*;
import data.KhuVucLoader;
import gui.GUIConstants;
import model.*;

/**
 * Form quản lý chính sách giá nước.
 * 
 * Chức năng:
 * - Thêm mới chính sách giá nước
 * - Sửa chính sách giá nước hiện có
 * - Quản lý bậc giá nước (tier pricing)
 * 
 * Cách sử dụng:
 * - new AddWaterPriceForm() → Thêm mới
 * - new AddWaterPriceForm(GiaNuoc) → Sửa
 * 
 * @author Lê Thanh Tùng
 * @version 2.0
 */
public class AddWaterPriceForm extends JFrame {

    // ===== LABELS =====
    private JLabel lbCustomerType = new JLabel("Loại Khách Hàng");
    private JLabel lbKhuVuc = new JLabel("Khu Vực");
    private JLabel lbThue = new JLabel("Thuế (%)");

    // ===== INPUT FIELDS =====
    private JComboBox<loaiCustomer> cbCustomerType = new JComboBox<>();
    private JComboBox<String> cbKhuVuc = new JComboBox<>();
    private JTextField tfThue = new JTextField();

    // ===== BUTTONS - Main Form =====
    private JButton btnSave = new JButton("Lưu");
    private JButton btnUpdate = new JButton("Cập nhật");
    private JButton btnCancel = new JButton("Hủy");

    // ===== BUTTONS - Water Price Tier Management =====
    private JButton btnRefresh = new JButton("Làm mới");
    private JButton btnAddTier = new JButton("Thêm bậc giá");
    private JButton btnEditTier = new JButton("Sửa bậc giá");
    private JButton btnDeleteTier = new JButton("Xóa bậc giá");

    // ===== DAO =====
    private GiaNuocDao gnDao = new GiaNuocDao();
    private CustomerDao cDao = new CustomerDao();

    // ===== TABLE =====
    private JScrollPane wptScrollPane;
    private JTable wptTable;
    private DefaultTableModel wptTableModel;
    private ArrayList<WaterPriceTier> waterPriceTiers = new ArrayList<>();

    /**
     * Constructor cho chế độ THÊM MỚI.
     * Hiển thị form trống để nhập thông tin chính sách giá nước mới.
     */
    public AddWaterPriceForm() {
        setTitle("Thêm chính sách giá nước");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load dữ liệu cho ComboBox
        loadCustomerTypes();
        loadKhuVuc();

        // Tạo giao diện
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(true), BorderLayout.SOUTH);

        // Gắn sự kiện
        btnSave.addActionListener(e -> saveWaterPrice());
        btnCancel.addActionListener(e -> dispose());

        // Hiển thị form
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Constructor cho chế độ SỬA.
     * Hiển thị thông tin giá nước hiện có và cho phép chỉnh sửa.
     * Bao gồm cả quản lý bậc giá nước.
     * 
     * @param gn Đối tượng GiaNuoc cần chỉnh sửa
     */
    public AddWaterPriceForm(GiaNuoc gn) {
        setTitle("Sửa chính sách giá nước");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load dữ liệu cho ComboBox
        loadCustomerTypes();
        loadKhuVuc();

        // Tạo panel bên trái (form chính)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(createFormPanel(), BorderLayout.CENTER);
        leftPanel.add(createButtonPanel(false), BorderLayout.SOUTH);

        // Tạo panel bên phải (bảng bậc giá nước)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(createTierButtonPanel(gn.getIdDonGia()), BorderLayout.NORTH);
        rightPanel.add(createTierTable(gn.getIdDonGia()), BorderLayout.CENTER);

        // Ghép 2 panel vào frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Điền dữ liệu vào form
        fillFormData(gn);

        // Gắn sự kiện
        btnUpdate.addActionListener(e -> updateWaterPrice(gn.getIdDonGia()));
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
     * Ví dụ: Sinh hoạt, Sản xuất, Kinh doanh, Hành chính
     */
    private void loadCustomerTypes() {
        cbCustomerType.removeAllItems(); // Xóa dữ liệu cũ
        List<loaiCustomer> loaiNguoiDung = cDao.getLoaiKhachHang();
        for (loaiCustomer lnd : loaiNguoiDung) {
            cbCustomerType.addItem(lnd);
        }
    }

    /**
     * Load danh sách khu vực (tỉnh/thành phố) từ file vào ComboBox.
     * File: src/main/java/data/khu_vuc.txt
     */
    private void loadKhuVuc() {
        cbKhuVuc.removeAllItems(); // Xóa dữ liệu cũ
        new KhuVucLoader().loadKhuVuc(cbKhuVuc);
    }

    /**
     * Điền dữ liệu từ object GiaNuoc vào form (dùng cho chế độ sửa).
     * 
     * @param gn Đối tượng GiaNuoc chứa dữ liệu cần điền
     */
    private void fillFormData(GiaNuoc gn) {
        // Tìm và chọn loại khách hàng tương ứng
        for (int i = 0; i < cbCustomerType.getItemCount(); i++) {
            loaiCustomer lc = cbCustomerType.getItemAt(i);
            if (lc.getIdLoaiCustomer() == gn.getIdLoaiCustomer()) {
                cbCustomerType.setSelectedIndex(i);
                break;
            }
        }

        // Chọn khu vực
        cbKhuVuc.setSelectedItem(gn.getKhuVuc());

        // Hiển thị thuế
        tfThue.setText(String.valueOf(gn.getThue()));
    }

    // ========================================
    // PHẦN 2: TẠO GIAO DIỆN
    // ========================================

    /**
     * Tạo panel chứa form nhập liệu (loại KH, khu vực, thuế).
     * Sử dụng GridBagLayout để căn chỉnh đẹp mắt.
     * 
     * @return JPanel chứa form
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Khoảng cách giữa các component
        gbc.anchor = GridBagConstraints.WEST; // Căn trái

        int row = 0;

        // ===== TIÊU ĐỀ =====
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2; // Chiếm 2 cột
        JLabel lbTitle = new JLabel(getTitle());
        lbTitle.setFont(GUIConstants.Fonts.TieuDe);
        panel.add(lbTitle, gbc);

        // Reset gridwidth về 1 cho các component tiếp theo
        gbc.gridwidth = 1;

        // ===== LOẠI KHÁCH HÀNG =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbCustomerType.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbCustomerType, gbc);

        gbc.gridx = 1;
        cbCustomerType.setPreferredSize(GUIConstants.Sizes.tf);
        cbCustomerType.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(cbCustomerType, gbc);
        row++;

        // ===== KHU VỰC =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbKhuVuc.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbKhuVuc, gbc);

        gbc.gridx = 1;
        cbKhuVuc.setPreferredSize(GUIConstants.Sizes.tf);
        cbKhuVuc.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(cbKhuVuc, gbc);
        row++;

        // ===== THUẾ =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbThue.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbThue, gbc);

        gbc.gridx = 1;
        tfThue.setPreferredSize(GUIConstants.Sizes.tf);
        tfThue.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(tfThue, gbc);

        return panel;
    }

    /**
     * Tạo panel chứa các nút Save/Update và Cancel.
     * 
     * @param isAddMode true = hiển thị nút "Lưu", false = hiển thị nút "Cập nhật"
     * @return JPanel chứa các nút
     */
    private JPanel createButtonPanel(boolean isAddMode) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        // Cấu hình kích thước và font cho các nút
        btnSave.setPreferredSize(GUIConstants.Sizes.btn);
        btnUpdate.setPreferredSize(GUIConstants.Sizes.btn);
        btnCancel.setPreferredSize(GUIConstants.Sizes.btn);

        btnSave.setFont(GUIConstants.Fonts.TieuDePhu);
        btnUpdate.setFont(GUIConstants.Fonts.TieuDePhu);
        btnCancel.setFont(GUIConstants.Fonts.TieuDePhu);

        // Chỉ hiển thị nút phù hợp với chế độ
        if (isAddMode) {
            panel.add(btnSave);
        } else {
            panel.add(btnUpdate);
        }
        panel.add(btnCancel);

        return panel;
    }

    /**
     * Tạo panel chứa các nút quản lý bậc giá nước.
     * Chỉ hiển thị trong chế độ sửa.
     * 
     * @param waterPriceId ID của chính sách giá nước
     * @return JPanel chứa các nút quản lý
     */
    private JPanel createTierButtonPanel(int waterPriceId) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Quản lý bậc giá nước"));

        panel.add(btnRefresh);
        panel.add(btnAddTier);
        panel.add(btnEditTier);
        panel.add(btnDeleteTier);

        // Gắn sự kiện
        btnRefresh.addActionListener(e -> loadTierData(waterPriceId));

        btnAddTier.addActionListener(e -> {
            // Mở form thêm bậc giá nước
            new AddWaterPriceTierForm(waterPriceId);
        });

        btnEditTier.addActionListener(e -> {
            int selectedRow = wptTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn bậc giá cần sửa!",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Lấy WaterPriceTier từ danh sách
            WaterPriceTier selectedTier = waterPriceTiers.get(selectedRow);

            // Mở form sửa
            new AddWaterPriceTierForm(selectedTier);
        });

        btnDeleteTier.addActionListener(e -> {
            int selectedRow = wptTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn bậc giá cần xóa!",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa bậc giá này?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Lấy ID bậc giá
                WaterPriceTier selectedTier = waterPriceTiers.get(selectedRow);
                BacGiaDao bacGiaDao = new BacGiaDao();

                // Xóa khỏi database
                if (bacGiaDao.deleteBacGiaById(selectedTier.getIdWaterPriceTier())) {
                    showSuccess("Xóa bậc giá nước thành công!");
                    loadTierData(waterPriceId); // Refresh bảng
                } else {
                    showError("Xóa bậc giá nước thất bại!");
                }
            }
        });

        return panel;
    }

    /**
     * Tạo bảng hiển thị các bậc giá nước.
     * Bảng gồm 4 cột: Bậc giá, Từ mức nước, Đến mức nước, Giá
     * 
     * @param waterPriceId ID của chính sách giá nước
     * @return JScrollPane chứa bảng
     */
    private JScrollPane createTierTable(int waterPriceId) {
        // Định nghĩa tên cột
        String[] columns = { "Bậc Giá", "Từ mức nước (m³)", "Đến mức nước (m³)", "Giá (VNĐ/m³)" };

        // Tạo model cho bảng
        wptTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép edit trực tiếp trên bảng
            }
        };

        // Tạo bảng
        wptTable = new JTable(wptTableModel);
        wptTable.setRowHeight(25);
        wptTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Chỉ chọn 1 dòng
        wptTable.getTableHeader().setReorderingAllowed(false); // Không cho di chuyển cột

        // Load dữ liệu
        loadTierData(waterPriceId);

        // Tạo scroll pane
        wptScrollPane = new JScrollPane(wptTable);
        wptScrollPane.setPreferredSize(new Dimension(500, 300));

        return wptScrollPane;
    }

    /**
     * Load dữ liệu bậc giá nước từ database vào bảng.
     * 
     * @param waterPriceId ID của chính sách giá nước
     */
    private void loadTierData(int waterPriceId) {
        // Xóa dữ liệu cũ
        wptTableModel.setRowCount(0);

        try {
            // Lấy dữ liệu từ database
            this.waterPriceTiers = gnDao.getBacGiaNuocByIdGiaNuoc(waterPriceId);

            // Thêm từng dòng vào bảng
            for (WaterPriceTier tier : waterPriceTiers) {
                Object[] row = {
                        tier.getTier(),
                        tier.getMinConsumption(),
                        tier.getMaxConsumption(),
                        String.format("%,.0f", tier.getPrice()) // Format giá có dấu phẩy
                };
                wptTableModel.addRow(row);
            }

            System.out.println("Đã load " + waterPriceTiers.size() + " bậc giá nước");

        } catch (Exception e) {
            System.out.println("Lỗi khi load bậc giá nước: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu bậc giá nước: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ========================================
    // PHẦN 3: XỬ LÝ LOGIC
    // ========================================

    /**
     * Lưu chính sách giá nước mới vào database.
     * Thực hiện validation trước khi lưu.
     */
    private void saveWaterPrice() {
        try {
            System.out.println("=== SAVE WATER PRICE ===");

            // Validate loại khách hàng
            loaiCustomer loaiKH = (loaiCustomer) cbCustomerType.getSelectedItem();
            if (loaiKH == null) {
                showError("Vui lòng chọn loại khách hàng!");
                return;
            }

            // Validate khu vực
            String khuVuc = (String) cbKhuVuc.getSelectedItem();
            if (khuVuc == null || khuVuc.equals("-- Chọn tỉnh ---")) {
                showError("Vui lòng chọn khu vực!");
                return;
            }

            // Validate thuế
            String thueText = tfThue.getText().trim();
            if (thueText.isEmpty()) {
                showError("Vui lòng nhập thuế!");
                return;
            }

            double thue;
            try {
                thue = Double.parseDouble(thueText);
                if (thue < 0 || thue > 100) {
                    showError("Thuế phải từ 0 đến 100!");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Thuế phải là số hợp lệ!");
                return;
            }

            // In ra thông tin debug
            System.out.println("Loại KH: " + loaiKH.getIdLoaiCustomer() + " - " + loaiKH.getTenLoaiCustomer());
            System.out.println("Khu vực: " + khuVuc);
            System.out.println("Thuế: " + thue + "%");

            // Tạo object GiaNuoc
            GiaNuoc giaNuoc = new GiaNuoc(
                    0, // ID tự động tăng
                    loaiKH.getIdLoaiCustomer(),
                    khuVuc,
                    thue);

            // Lưu vào database
            boolean success = gnDao.addGiaNuoc(giaNuoc);
            System.out.println("Kết quả lưu: " + success);

            if (success) {
                showSuccess("Thêm chính sách giá nước thành công!");
                dispose(); // Đóng form
            } else {
                showError("Thêm chính sách giá nước thất bại!");
            }

        } catch (Exception e) {
            System.out.println("Lỗi không mong muốn: " + e.getMessage());
            e.printStackTrace();
            showError("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Cập nhật chính sách giá nước đã có trong database.
     * Thực hiện validation trước khi cập nhật.
     * 
     * @param waterPriceId ID của chính sách cần cập nhật
     */
    private void updateWaterPrice(int waterPriceId) {
        try {
            System.out.println("=== UPDATE WATER PRICE ===");
            System.out.println("ID: " + waterPriceId);

            // Validate loại khách hàng
            loaiCustomer loaiKH = (loaiCustomer) cbCustomerType.getSelectedItem();
            if (loaiKH == null) {
                showError("Vui lòng chọn loại khách hàng!");
                return;
            }

            // Validate khu vực
            String khuVuc = (String) cbKhuVuc.getSelectedItem();
            if (khuVuc == null || khuVuc.equals("-- Chọn tỉnh ---")) {
                showError("Vui lòng chọn khu vực!");
                return;
            }

            // Validate thuế
            String thueText = tfThue.getText().trim();
            if (thueText.isEmpty()) {
                showError("Vui lòng nhập thuế!");
                return;
            }

            double thue;
            try {
                thue = Double.parseDouble(thueText);
                if (thue < 0 || thue > 100) {
                    showError("Thuế phải từ 0 đến 100!");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Thuế phải là số hợp lệ!");
                return;
            }

            // In ra thông tin debug
            System.out.println("Loại KH: " + loaiKH.getIdLoaiCustomer() + " - " + loaiKH.getTenLoaiCustomer());
            System.out.println("Khu vực: " + khuVuc);
            System.out.println("Thuế: " + thue + "%");

            // Tạo object GiaNuoc với ID cũ
            GiaNuoc giaNuoc = new GiaNuoc(
                    waterPriceId, // Giữ nguyên ID
                    loaiKH.getIdLoaiCustomer(),
                    khuVuc,
                    thue);

            // In ra object để kiểm tra
            System.out.println("Object GiaNuoc:");
            System.out.println("  - ID: " + giaNuoc.getIdDonGia());
            System.out.println("  - ID Loại KH: " + giaNuoc.getIdLoaiCustomer());
            System.out.println("  - Khu vực: " + giaNuoc.getKhuVuc());
            System.out.println("  - Thuế: " + giaNuoc.getThue() + "%");

            // Cập nhật trong database
            boolean success = gnDao.updateGiaNuoc(giaNuoc);
            System.out.println("Kết quả cập nhật: " + success);

            if (success) {
                showSuccess("Cập nhật chính sách giá nước thành công!");
                dispose(); // Đóng form
            } else {
                showError("Cập nhật chính sách giá nước thất bại!");
            }

        } catch (Exception e) {
            System.out.println("Lỗi không mong muốn: " + e.getMessage());
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
     * @param message Nội dung thông báo
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
}