package gui.Customer;

import java.awt.BorderLayout;
import java.awt.*;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import gui.GUIConstants;

import dao.*;
import data.KhuVucLoader;
import model.*;

/**
 * Trang quản lý Hộ sử dụng.
 * 
 * Chức năng:
 * - Hiển thị danh sách toàn bộ hộ sử dụng
 * - Lọc theo loại khách hàng và khu vực
 * - Thêm hộ sử dụng dựa trên ID khách hàng
 * - Sửa, xóa hộ sử dụng
 * - Xem thông tin chi tiết hộ sử dụng
 * 
 * @author Lê Thanh Tùng
 * @version 1.0
 */
public class HoSuDungPage extends JPanel {

    // ===== TABLE =====
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    // ===== PANELS =====
    private JPanel topPanel = new JPanel();

    // ===== BUTTONS =====
    private JButton btnAdd = new JButton("Thêm");
    private JButton btnEdit = new JButton("Sửa Thông Tin");
    private JButton btnDelete = new JButton("Xóa");
    private JButton btnRefresh = new JButton("Làm mới và lọc");

    // ===== FILTERS =====
    private JComboBox<String> cbKhuVuc = new JComboBox<>();
    private JComboBox<loaiCustomer> cbCustomerType = new JComboBox<>();
    private JTextField tfSearchCustomerId = new JTextField(10);

    // ===== DATA =====
    private ArrayList<hoSuDung> hsdArr = new ArrayList<>();

    // ===== DAO =====
    private HoSuDungDao hsdDao = new HoSuDungDao();
    private CustomerDao customerDao = new CustomerDao();

    /**
     * Constructor - Khởi tạo trang quản lý hộ sử dụng
     */
    public HoSuDungPage() {
        setLayout(new BorderLayout(5, 5));
        setBackground(GUIConstants.Colors.BACKGROUND);

        // Tạo giao diện
        initTopPanel();
        initTable();

        // Load dữ liệu
        loadFilterData();
        showAllHoSuDung();

        // Gắn sự kiện
        attachEventHandlers();
    }

    /**
     * Khởi tạo panel trên cùng chứa buttons và filters
     */
    private void initTopPanel() {
        topPanel.setLayout(new GridLayout(2, 1, 5, 5)); // 2 hàng, 1 cột
        topPanel.setBackground(GUIConstants.Colors.BACKGROUND);

        // ===== HÀNG 1: Panel chứa các nút thao tác =====
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBtn.setBackground(GUIConstants.Colors.BACKGROUND);
        panelBtn.add(btnAdd);
        panelBtn.add(btnEdit);
        panelBtn.add(btnDelete);

        // ===== HÀNG 2: Panel chứa bộ lọc =====
        JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFilter.setBackground(GUIConstants.Colors.BACKGROUND);

        // Label và combobox loại khách hàng
        JLabel lblLoaiKH = new JLabel("Loại KH:");
        lblLoaiKH.setForeground(Color.WHITE); // Đổi màu chữ thành trắng
        panelFilter.add(lblLoaiKH);
        cbCustomerType.setPreferredSize(new Dimension(150, 25));
        panelFilter.add(cbCustomerType);

        // Label và combobox khu vực
        JLabel lblKhuVuc = new JLabel("Khu vực:");
        lblKhuVuc.setForeground(Color.WHITE); // Đổi màu chữ thành trắng
        panelFilter.add(lblKhuVuc);
        cbKhuVuc.setPreferredSize(new Dimension(150, 25));
        panelFilter.add(cbKhuVuc);

        // Textfield tìm theo ID khách hàng
        JLabel lblIDKH = new JLabel("ID KH:");
        lblIDKH.setForeground(Color.WHITE); // Đổi màu chữ thành trắng
        panelFilter.add(lblIDKH);
        tfSearchCustomerId.setToolTipText("Nhập ID khách hàng để tìm kiếm");
        panelFilter.add(tfSearchCustomerId);

        panelFilter.add(btnRefresh);

        // Thêm 2 panel vào topPanel
        topPanel.add(panelBtn);
        topPanel.add(panelFilter);

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Khởi tạo bảng hiển thị dữ liệu
     */
    private void initTable() {
        // Định nghĩa các cột
        String[] columnNames = {
                "Mã HSD",
                "ID Khách hàng",
                "Tên Khách hàng",
                "Khu vực",
                "Địa Chỉ",
                "Trạng thái"
        };

        // Tạo model cho bảng (không cho edit trực tiếp)
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Load dữ liệu cho các filter (ComboBox)
     */
    private void loadFilterData() {
        // Load loại khách hàng
        cbCustomerType.removeAllItems();
        cbCustomerType.addItem(null); // Thêm option "Tất cả"
        List<loaiCustomer> loaiKHList = customerDao.getLoaiKhachHang();
        for (loaiCustomer lkh : loaiKHList) {
            cbCustomerType.addItem(lkh);
        }

        // Tùy chỉnh hiển thị cho ComboBox
        cbCustomerType.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("-- Tất cả loại KH --");
                }
                return this;
            }
        });

        // Load khu vực
        new KhuVucLoader().loadKhuVuc(cbKhuVuc);
    }

    /**
     * Gắn sự kiện cho các nút và controls
     */
    private void attachEventHandlers() {
        // Nút Refresh/Lọc
        btnRefresh.addActionListener(e -> filterHoSuDung());

        // Nút Thêm
        btnAdd.addActionListener(e -> {
            // Yêu cầu nhập ID khách hàng
            String input = JOptionPane.showInputDialog(
                    this,
                    "Nhập ID khách hàng:",
                    "Thêm hộ sử dụng",
                    JOptionPane.QUESTION_MESSAGE);

            if (input != null && !input.trim().isEmpty()) {
                try {
                    int customerId = Integer.parseInt(input.trim());

                    // Kiểm tra khách hàng có tồn tại không
                    Customer customer = customerDao.getCustomerById(customerId);
                    if (customer != null) {
                        new AddHoSuDungForm(customerId);
                    } else {
                        showError("Không tìm thấy khách hàng với ID: " + customerId);
                    }
                } catch (NumberFormatException ex) {
                    showError("ID khách hàng không hợp lệ!");
                }
            }
        });

        // Nút Sửa
        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                showWarning("Vui lòng chọn hộ sử dụng cần sửa!");
                return;
            }

            hoSuDung selected = hsdArr.get(selectedRow);
            new AddHoSuDungForm(selected);
        });

        // Nút Xóa
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                showWarning("Vui lòng chọn hộ sử dụng cần xóa!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xóa hộ sử dụng này?\nLưu ý: Sẽ xóa cả các dữ liệu liên quan!",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                hoSuDung selected = hsdArr.get(selectedRow);

                if (hsdDao.delete_HoSuDung(selected.getID_HoSuDung())) {
                    showSuccess("Xóa hộ sử dụng thành công!");
                    showAllHoSuDung(); // Refresh lại bảng
                } else {
                    showError("Xóa hộ sử dụng thất bại!");
                }
            }
        });

        // Enter ở textfield tìm kiếm
        tfSearchCustomerId.addActionListener(e -> filterHoSuDung());
    }

    /**
     * Hiển thị toàn bộ hộ sử dụng (không lọc)
     */
    public void showAllHoSuDung() {
        tableModel.setRowCount(0);
        hsdArr.clear();

        try {
            // Lấy tất cả khách hàng
            ArrayList<Customer> allCustomers = customerDao.getCustomers();

            // Với mỗi khách hàng, lấy danh sách hộ sử dụng
            for (Customer customer : allCustomers) {
                ArrayList<hoSuDung> hsdList = hsdDao.getHoSuDungByCustomerId(customer.getIdCustomer());

                for (hoSuDung hsd : hsdList) {
                    hsd.setID_Customer(customer.getIdCustomer()); // Đảm bảo có ID khách hàng

                    String trangThai = hsd.getTrangThai() == 1 ? "Đang sử dụng" : "Ngừng sử dụng";

                    Object[] row = {
                            hsd.getID_HoSuDung(),
                            customer.getIdCustomer(),
                            customer.getNameCustomer(),
                            hsd.getKhuVuc(),
                            hsd.getDiaChi(),
                            trangThai
                    };

                    tableModel.addRow(row);
                    hsdArr.add(hsd);
                }
            }
            System.out.println("Đã load " + hsdArr.size() + " hộ sử dụng");

        } catch (Exception e) {
            showError("Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }

    /**
     * Lọc hộ sử dụng theo các tiêu chí đã chọn
     */
    private void filterHoSuDung() {
        tableModel.setRowCount(0);
        hsdArr.clear();

        try {
            // Lấy giá trị filter
            loaiCustomer selectedType = (loaiCustomer) cbCustomerType.getSelectedItem();
            String selectedKhuVuc = (String) cbKhuVuc.getSelectedItem();
            String searchId = tfSearchCustomerId.getText().trim();

            // Lấy tất cả khách hàng
            ArrayList<Customer> allCustomers = customerDao.getCustomers();

            for (Customer customer : allCustomers) {
                // Lọc theo loại khách hàng
                if (selectedType != null && customer.getLoaiCustomer() != selectedType.getIdLoaiCustomer()) {
                    continue;
                }

                // Lọc theo ID khách hàng (nếu có nhập)
                if (!searchId.isEmpty()) {
                    try {
                        int searchIdInt = Integer.parseInt(searchId);
                        if (customer.getIdCustomer() != searchIdInt) {
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        // Bỏ qua nếu không phải số
                    }
                }

                // Lấy danh sách hộ sử dụng của khách hàng
                ArrayList<hoSuDung> hsdList = hsdDao.getHoSuDungByCustomerId(customer.getIdCustomer());

                for (hoSuDung hsd : hsdList) {
                    // Lọc theo khu vực
                    if (selectedKhuVuc != null && !selectedKhuVuc.equals("-- Chọn tỉnh ---")) {
                        if (!hsd.getKhuVuc().equals(selectedKhuVuc)) {
                            continue;
                        }
                    }

                    hsd.setID_Customer(customer.getIdCustomer());

                    String trangThai = hsd.getTrangThai() == 1 ? "Đang sử dụng" : "Ngừng sử dụng";

                    Object[] row = {
                            hsd.getID_HoSuDung(),
                            customer.getIdCustomer(),
                            customer.getNameCustomer(),
                            hsd.getKhuVuc(),
                            hsd.getDiaChi(),
                            trangThai
                    };

                    tableModel.addRow(row);
                    hsdArr.add(hsd);
                }
            }

            System.out.println("Đã lọc được " + hsdArr.size() + " hộ sử dụng");

        } catch (Exception e) {
            System.out.println("Lỗi khi lọc hộ sử dụng: " + e.getMessage());
            e.printStackTrace();
            showError("Lỗi khi lọc dữ liệu: " + e.getMessage());
        }
    }

    // ========================================
    // HELPER METHODS
    // ========================================

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}