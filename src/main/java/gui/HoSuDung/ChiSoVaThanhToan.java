package gui.HoSuDung;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.*;
import gui.GUIConstants;
import model.*;

/**
 * Form quản lý chỉ số nước và thanh toán cho một hộ sử dụng
 * 
 * Chức năng:
 * - Hiển thị thông tin hộ sử dụng
 * - Hiển thị danh sách các lần ghi chỉ số
 * - Thêm/Sửa/Xóa chỉ số nước
 * - Xem hóa đơn
 * - Thanh toán hóa đơn
 * 
 * @author Lê Thanh Tùng
 * @version 1.0
 */
public class ChiSoVaThanhToan extends JFrame {

    // ===== DATA =====
    private HoSuDung HoSuDung;
    private Customer customer;

    // ===== DAO =====
    private ChiSoNuocDao chiSoDao = new ChiSoNuocDao();
    private HoaDonDao hoaDonDao = new HoaDonDao();
    private CustomerDao customerDao = new CustomerDao();

    // ===== UI COMPONENTS - Info Panel =====
    private JLabel lbTenKH = new JLabel();
    private JLabel lbDiaChi = new JLabel();
    private JLabel lbKhuVuc = new JLabel();
    private JLabel lbTrangThai = new JLabel();

    // ===== UI COMPONENTS - Table =====
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private ArrayList<ChiSoNuoc> chiSoList = new ArrayList<>();

    // ===== UI COMPONENTS - Buttons =====
    private JButton btnRefresh = new JButton("Làm mới");
    private JButton btnAdd = new JButton("Thêm chỉ số");
    private JButton btnDelete = new JButton("Xóa chỉ số");
    private JButton btnViewBill = new JButton("Xem hóa đơn");
    private JButton btnPayment = new JButton("Thanh toán");

    /**
     * Constructor
     * 
     * @param HoSuDung Hộ sử dụng cần quản lý chỉ số
     */
    public ChiSoVaThanhToan(HoSuDung HoSuDung) {
        this.HoSuDung = HoSuDung;

        // Lấy thông tin khách hàng
        this.customer = customerDao.getCustomerById(HoSuDung.getID_Customer());

        setTitle("Quản lý chỉ số nước - Hộ #" + HoSuDung.getID_HoSuDung());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout(10, 10));

        // Tạo các panel
        add(createInfoPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        // Load dữ liệu
        loadChiSoData();

        // Gắn sự kiện
        attachEventHandlers();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Tạo panel hiển thị thông tin hộ sử dụng
     */
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin hộ sử dụng"));
        panel.setBackground(GUIConstants.Colors.BACKGROUND);

        // Row 1
        panel.add(createInfoLabel("Tên khách hàng:"));
        lbTenKH.setText(customer != null ? customer.getNameCustomer() : "N/A");
        lbTenKH.setFont(GUIConstants.Fonts.TieuDePhu);
        lbTenKH.setForeground(Color.WHITE);
        panel.add(lbTenKH);

        panel.add(createInfoLabel("Địa chỉ:"));
        lbDiaChi.setText(HoSuDung.getDiaChi());
        lbDiaChi.setFont(GUIConstants.Fonts.TieuDePhu);
        lbDiaChi.setForeground(Color.WHITE);
        panel.add(lbDiaChi);

        // Row 2
        panel.add(createInfoLabel("Khu vực:"));
        lbKhuVuc.setText(HoSuDung.getKhuVuc());
        lbKhuVuc.setFont(GUIConstants.Fonts.TieuDePhu);
        lbKhuVuc.setForeground(Color.WHITE);
        panel.add(lbKhuVuc);

        panel.add(createInfoLabel("Trạng thái:"));
        String trangThai = HoSuDung.getTrangThai() == 1 ? "Đang sử dụng" : "Ngừng sử dụng";
        lbTrangThai.setText(trangThai);
        lbTrangThai.setFont(GUIConstants.Fonts.TieuDePhu);
        lbTrangThai.setForeground(HoSuDung.getTrangThai() == 1 ? Color.GREEN : Color.RED);
        panel.add(lbTrangThai);

        return panel;
    }

    /**
     * Helper method tạo label cho info panel
     */
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.LIGHT_GRAY);
        return label;
    }

    /**
     * Tạo panel chứa bảng chỉ số nước
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(GUIConstants.Colors.BACKGROUND);

        // Định nghĩa cột
        String[] columns = {
                "ID",
                "Ngày Ghi",
                "Chỉ Số Cũ (m³)",
                "Chỉ Số Mới (m³)",
                "Tiêu Thụ (m³)",
                "Trạng Thái TT"
        };

        // Tạo model
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho edit trực tiếp
            }
        };

        // Tạo table
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Căn giữa các cột số
        var centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 6; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Tạo panel chứa các nút chức năng
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(GUIConstants.Colors.BACKGROUND);

        panel.add(btnRefresh);
        panel.add(btnAdd);
        panel.add(btnDelete);
        panel.add(btnViewBill);
        panel.add(btnPayment);

        return panel;
    }

    /**
     * Gắn sự kiện cho các nút
     */
    private void attachEventHandlers() {
        // Làm mới dữ liệu
        btnRefresh.addActionListener(e -> loadChiSoData());

        // Thêm chỉ số
        btnAdd.addActionListener(e -> {
            new AddChiSoNuocForm(HoSuDung);
            // Refresh sau khi đóng form
            loadChiSoData();
        });

        // Xóa chỉ số
        btnDelete.addActionListener(e -> deleteChiSo());

        // Xem hóa đơn
        btnViewBill.addActionListener(e -> viewBill());

        // Thanh toán
        btnPayment.addActionListener(e -> processPayment());
    }

    /**
     * Load dữ liệu chỉ số nước từ database
     */
    private void loadChiSoData() {
        tableModel.setRowCount(0);
        chiSoList.clear();

        try {
            chiSoList = chiSoDao.getChiSoNuocByHoSuDungId(HoSuDung.getID_HoSuDung());

            for (ChiSoNuoc cs : chiSoList) {
                // Lấy hóa đơn để kiểm tra trạng thái thanh toán
                HoaDon hoaDon = hoaDonDao.getHoaDonByChiSoId(cs.getIdChiSo());
                String trangThaiTT = hoaDon != null ? hoaDon.getTrangThaiText() : "Chưa lập HĐ";

                Object[] row = {
                        cs.getIdChiSo(),
                        cs.getNgayGhiFormatted(),
                        cs.getChiSoCu(),
                        cs.getChiSoMoi(),
                        cs.getTieuThu(),
                        trangThaiTT
                };
                tableModel.addRow(row);
            }

            System.out.println("Đã load " + chiSoList.size() + " chỉ số nước");

        } catch (Exception e) {
            System.out.println("Lỗi load chỉ số: " + e.getMessage());
        }
    }

    /**
     * Xóa chỉ số nước
     */
    private void deleteChiSo() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Vui lòng chọn chỉ số cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa chỉ số này?\n" +
                        "Lưu ý: Sẽ xóa cả hóa đơn liên quan (nếu có)!",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idChiSo = (Integer) tableModel.getValueAt(selectedRow, 0);

                // Xóa hóa đơn trước (nếu có)
                HoaDon hoaDon = hoaDonDao.getHoaDonByChiSoId(idChiSo);
                if (hoaDon != null) {
                    hoaDonDao.deleteHoaDon(hoaDon.getIdHoaDon());
                }

                // Xóa chỉ số
                if (chiSoDao.deleteChiSoNuoc(idChiSo)) {
                    showSuccess("Xóa chỉ số thành công!");
                    loadChiSoData();
                } else {
                    showError("Xóa chỉ số thất bại!");
                }

            } catch (Exception e) {
                System.out.println("Lỗi xóa chỉ số: " + e.getMessage());
                e.printStackTrace();
                showError("Lỗi: " + e.getMessage());
            }
        }
    }

    /**
     * Xem chi tiết hóa đơn
     */
    private void viewBill() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Vui lòng chọn chỉ số để xem hóa đơn!");
            return;
        }

        try {
            int idChiSo = (Integer) tableModel.getValueAt(selectedRow, 0);
            HoaDon hoaDon = hoaDonDao.getHoaDonByChiSoId(idChiSo);

            if (hoaDon == null) {
                showWarning("Chưa có hóa đơn cho chỉ số này!");
                return;
            }

            // Lấy thông tin chỉ số
            ChiSoNuoc chiSo = chiSoList.get(selectedRow);

            // Hiển thị dialog chi tiết hóa đơn
            String message = """
                    ========== HÓA ĐƠN TIỀN NƯỚC ==========

                    Hộ sử dụng: %s
                    Địa chỉ: %s
                    Khu vực: %s

                    Kỳ ghi: %s
                    Chỉ số cũ: %d m³
                    Chỉ số mới: %d m³
                    Tiêu thụ: %d m³

                    Ngày lập hóa đơn: %s
                    Tổng tiền: %s
                    Trạng thái: %s

                    =====================================
                    """.formatted(
                    customer.getNameCustomer(),
                    HoSuDung.getDiaChi(),
                    HoSuDung.getKhuVuc(),
                    chiSo.getNgayGhiFormatted(),
                    chiSo.getChiSoCu(),
                    chiSo.getChiSoMoi(),
                    chiSo.getTieuThu(),
                    hoaDon.getNgayLapHoaDon(),
                    hoaDon.getTongTienFormatted(),
                    hoaDon.getTrangThaiText());

            JTextArea textArea = new JTextArea(message);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

            JOptionPane.showMessageDialog(this,
                    new JScrollPane(textArea),
                    "Chi tiết hóa đơn",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            System.out.println("Lỗi xem hóa đơn: " + e.getMessage());
            e.printStackTrace();
            showError("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Xử lý thanh toán hóa đơn
     */
    private void processPayment() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Vui lòng chọn hóa đơn cần thanh toán!");
            return;
        }

        try {
            int idChiSo = (Integer) tableModel.getValueAt(selectedRow, 0);
            HoaDon hoaDon = hoaDonDao.getHoaDonByChiSoId(idChiSo);

            if (hoaDon == null) {
                showWarning("Chưa có hóa đơn cho chỉ số này!");
                return;
            }

            if (hoaDon.getTrangThaiHoaDon() == 1) {
                showInfo("Hóa đơn này đã được thanh toán!");
                return;
            }

            // Xác nhận thanh toán
            int confirm = JOptionPane.showConfirmDialog(this,
                    String.format("Xác nhận thanh toán hóa đơn?\nSố tiền: %s",
                            hoaDon.getTongTienFormatted()),
                    "Xác nhận thanh toán",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Cập nhật trạng thái hóa đơn
                if (hoaDonDao.updateTrangThai(hoaDon.getIdHoaDon(), 1)) {
                    showSuccess("Thanh toán thành công!");
                    loadChiSoData(); // Refresh
                } else {
                    showError("Thanh toán thất bại!");
                }
            }

        } catch (Exception e) {
            System.out.println("Lỗi thanh toán: " + e.getMessage());
        }
    }

    // ===== HELPER METHODS =====

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    // ===== MAIN METHOD FOR TESTING =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test data
            HoSuDung testHoSuDung = new HoSuDung();
            testHoSuDung.setID_HoSuDung(1);
            testHoSuDung.setID_Customer(1);
            testHoSuDung.setDiaChi("123 Nguyễn Văn Linh");
            testHoSuDung.setKhuVuc("Đà Nẵng");
            testHoSuDung.setTrangThai(1);

            new ChiSoVaThanhToan(testHoSuDung);
        });
    }
}