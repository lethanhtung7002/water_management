package gui.HoSuDung;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;

import dao.*;
import gui.GUIConstants;
import model.*;
import calc.WaterBill;

/**
 * Form thêm chỉ số nước và tự động lập hóa đơn
 * 
 * Chức năng:
 * - Nhập chỉ số nước mới
 * - Tự động lấy chỉ số cũ từ lần ghi trước
 * - Tự động tính tiêu thụ
 * - Lưu chỉ số vào database
 * - Tự động tính tiền theo bậc thang
 * - Tự động tạo hóa đơn
 * 
 */
public class AddChiSoNuocForm extends JFrame {

    // ===== DATA =====
    private HoSuDung HoSuDung;
    private Customer customer;
    private int chiSoCu = 0;

    // ===== DAO =====
    private ChiSoNuocDao chiSoDao = new ChiSoNuocDao();
    private HoaDonDao hoaDonDao = new HoaDonDao();
    private CustomerDao customerDao = new CustomerDao();
    private GiaNuocDao giaNuocDao = new GiaNuocDao();
    private BacGiaDao bacGiaDao = new BacGiaDao();

    // ===== LABELS =====
    private JLabel lbHoSuDung = new JLabel("Hộ sử dụng:");
    private JLabel lbDiaChi = new JLabel("Địa chỉ:");
    private JLabel lbNgayGhi = new JLabel("Ngày ghi:");
    private JLabel lbChiSoCu = new JLabel("Chỉ số cũ (m³):");
    private JLabel lbChiSoMoi = new JLabel("Chỉ số mới (m³):");
    private JLabel lbTieuThu = new JLabel("Tiêu thụ (m³):");

    // ===== INPUT FIELDS =====
    private JTextField tfHoSuDung = new JTextField();
    private JTextField tfDiaChi = new JTextField();
    private JComboBox<Integer> cbNgay = new JComboBox<>();
    private JComboBox<Integer> cbThang = new JComboBox<>();
    private JComboBox<Integer> cbNam = new JComboBox<>();
    private JTextField tfChiSoCu = new JTextField();
    private JTextField tfChiSoMoi = new JTextField();
    private JTextField tfTieuThu = new JTextField();

    // ===== BUTTONS =====
    private JButton btnSaveAndBill = new JButton("Lưu và lập hóa đơn");
    private JButton btnCancel = new JButton("Hủy");

    /**
     * Constructor
     * 
     * @param HoSuDung Hộ sử dụng cần ghi chỉ số
     */
    public AddChiSoNuocForm(HoSuDung HoSuDung) {
        this.HoSuDung = HoSuDung;
        this.customer = customerDao.getCustomerById(HoSuDung.getID_Customer());

        setTitle("Ghi chỉ số nước - Hộ #" + HoSuDung.getID_HoSuDung());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load chỉ số cũ
        loadChiSoCu();

        // Tạo giao diện
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        // Điền thông tin
        fillData();

        // Gắn sự kiện
        attachEventHandlers();

        pack();
        // setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Load chỉ số cũ từ lần ghi trước
     */
    private void loadChiSoCu() {
        ChiSoNuoc latestChiSo = chiSoDao.getLatestChiSo(HoSuDung.getID_HoSuDung());
        if (latestChiSo != null) {
            this.chiSoCu = latestChiSo.getChiSoMoi();
        } else {
            this.chiSoCu = 0; // Lần đầu ghi
        }
    }

    /**
     * Tạo form panel
     */
    private JPanel createFormPanel() {
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

        gbc.gridwidth = 1;

        // ===== HỘ SỬ DỤNG =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbHoSuDung.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbHoSuDung, gbc);

        gbc.gridx = 1;
        tfHoSuDung.setPreferredSize(GUIConstants.Sizes.tf);
        tfHoSuDung.setFont(GUIConstants.Fonts.TieuDePhu);
        tfHoSuDung.setEditable(false);
        panel.add(tfHoSuDung, gbc);
        row++;

        // ===== ĐỊA CHỈ =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbDiaChi.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbDiaChi, gbc);

        gbc.gridx = 1;
        tfDiaChi.setPreferredSize(GUIConstants.Sizes.tf);
        tfDiaChi.setFont(GUIConstants.Fonts.TieuDePhu);
        tfDiaChi.setEditable(false);
        panel.add(tfDiaChi, gbc);
        row++;

        // ===== NGÀY GHI =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbNgayGhi.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbNgayGhi, gbc);

        gbc.gridx = 1;
        JPanel datePanel = createDatePanel();
        panel.add(datePanel, gbc);
        row++;

        // ===== CHỈ SỐ CŨ =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbChiSoCu.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbChiSoCu, gbc);

        gbc.gridx = 1;
        tfChiSoCu.setPreferredSize(GUIConstants.Sizes.tf);
        tfChiSoCu.setFont(GUIConstants.Fonts.TieuDePhu);
        tfChiSoCu.setEditable(false);
        panel.add(tfChiSoCu, gbc);
        row++;

        // ===== CHỈ SỐ MỚI =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbChiSoMoi.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbChiSoMoi, gbc);

        gbc.gridx = 1;
        tfChiSoMoi.setPreferredSize(GUIConstants.Sizes.tf);
        tfChiSoMoi.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(tfChiSoMoi, gbc);
        row++;

        // ===== TIÊU THỤ =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbTieuThu.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbTieuThu, gbc);

        gbc.gridx = 1;
        tfTieuThu.setPreferredSize(GUIConstants.Sizes.tf);
        tfTieuThu.setFont(GUIConstants.Fonts.TieuDePhu);
        tfTieuThu.setEditable(false);
        tfTieuThu.setBackground(Color.WHITE);
        panel.add(tfTieuThu, gbc);

        return panel;
    }

    /**
     * Tạo panel chọn ngày tháng năm
     */
    private JPanel createDatePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        LocalDate today = LocalDate.now();

        // Load ngày
        for (int i = 1; i <= 31; i++) {
            cbNgay.addItem(i);
        }

        // Load tháng
        for (int i = 1; i <= 12; i++) {
            cbThang.addItem(i);
        }

        // Load năm
        int currentYear = today.getYear();
        for (int i = currentYear - 4; i <= currentYear + 4; i++) {
            cbNam.addItem(i);
        }

        // Set ngày hiện tại
        cbNgay.setSelectedItem(today.getDayOfMonth());
        cbThang.setSelectedItem(today.getMonthValue());
        cbNam.setSelectedItem(today.getYear());

        panel.add(new JLabel("Ngày:"));
        panel.add(cbNgay);
        panel.add(new JLabel("Tháng:"));
        panel.add(cbThang);
        panel.add(new JLabel("Năm:"));
        panel.add(cbNam);

        return panel;
    }

    /**
     * Tạo button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        btnSaveAndBill.setPreferredSize(new Dimension(180, 35));
        btnCancel.setPreferredSize(GUIConstants.Sizes.btn);

        btnSaveAndBill.setFont(GUIConstants.Fonts.TieuDePhu);
        btnCancel.setFont(GUIConstants.Fonts.TieuDePhu);

        panel.add(btnSaveAndBill);
        panel.add(btnCancel);

        return panel;
    }

    /**
     * Điền dữ liệu vào form
     */
    private void fillData() {
        tfHoSuDung.setText(customer != null ? customer.getNameCustomer() : "N/A");
        tfDiaChi.setText(HoSuDung.getDiaChi() + " - " + HoSuDung.getKhuVuc());
        tfChiSoCu.setText(String.valueOf(chiSoCu));
        tfTieuThu.setText("0");
    }

    /**
     * Gắn sự kiện
     */
    private void attachEventHandlers() {
        // Tự động tính tiêu thụ khi nhập chỉ số mới
        tfChiSoMoi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateTieuThu();
            }
        });

        // Lưu và lập hóa đơn
        btnSaveAndBill.addActionListener(e -> saveAndCreateBill());

        // Hủy
        btnCancel.addActionListener(e -> dispose());
    }

    /**
     * Tính tiêu thụ tự động
     */
    private void calculateTieuThu() {
        try {
            String chiSoMoiText = tfChiSoMoi.getText().trim();
            if (!chiSoMoiText.isEmpty()) {
                int chiSoMoi = Integer.parseInt(chiSoMoiText);
                int tieuThu = chiSoCu - chiSoMoi;

                if (tieuThu < 0) {
                    tfTieuThu.setText("Lỗi: Chỉ số mới < chỉ số cũ");
                    tfTieuThu.setForeground(Color.RED);
                } else {
                    tfTieuThu.setText(String.valueOf(tieuThu));
                    tfTieuThu.setForeground(Color.BLACK);
                }
            }
        } catch (NumberFormatException e) {
            tfTieuThu.setText("0");
        }
    }

    /**
     * Lưu chỉ số và tự động tạo hóa đơn
     */
    private void saveAndCreateBill() {
        try {
            // ===== VALIDATE =====
            String chiSoMoiText = tfChiSoMoi.getText().trim();
            if (chiSoMoiText.isEmpty()) {
                showError("Vui lòng nhập chỉ số mới!");
                return;
            }

            int chiSoMoi = Integer.parseInt(chiSoMoiText);
            if (chiSoMoi < chiSoCu) {
                showError("Chỉ số mới không được nhỏ hơn chỉ số cũ!");
                return;
            }

            int tieuThu = chiSoMoi - chiSoCu;
            int ngay = (Integer) cbNgay.getSelectedItem();
            int thang = (Integer) cbThang.getSelectedItem();
            int nam = (Integer) cbNam.getSelectedItem();

            // debug
            System.out.println("=== LƯU CHỈ SỐ VÀ LẬP HÓA ĐƠN ===");
            System.out.println("Hộ sử dụng: " + HoSuDung.getID_HoSuDung());
            System.out.println("Ngày ghi: " + ngay + "/" + thang + "/" + nam);
            System.out.println("Chỉ số cũ: " + chiSoCu);
            System.out.println("Chỉ số mới: " + chiSoMoi);
            System.out.println("Tiêu thụ: " + tieuThu);

            // ===== BƯỚC 1: LƯU CHỈ SỐ =====
            ChiSoNuoc chiSo = new ChiSoNuoc(
                    0,
                    HoSuDung.getID_HoSuDung(),
                    nam,
                    thang,
                    ngay,
                    chiSoCu,
                    chiSoMoi);

            if (!chiSoDao.addChiSoNuoc(chiSo)) {
                showError("Lưu chỉ số thất bại!");
                return;
            }

            System.out.println("Lưu chỉ số thành công!");

            // ===== BƯỚC 2: LẤY ID CHỈ SỐ VỪA TẠO =====
            ChiSoNuoc latestChiSo = chiSoDao.getLatestChiSo(HoSuDung.getID_HoSuDung());
            if (latestChiSo == null) {
                showError("Không lấy được ID chỉ số vừa tạo!");
                return;
            }

            int idChiSo = latestChiSo.getIdChiSo();
            System.out.println("ID chỉ số: " + idChiSo);

            // ===== BƯỚC 3: TÌM BẢNG GIÁ =====
            GiaNuoc giaNuoc = findGiaNuoc();
            if (giaNuoc == null) {
                showWarning("Không tìm thấy bảng giá phù hợp!\n" +
                        "Chỉ số đã được lưu nhưng chưa lập hóa đơn.\n" +
                        "Vui lòng kiểm tra bảng giá nước.");
                dispose();
                return;
            }

            System.out.println("✓ Tìm thấy bảng giá: " + giaNuoc.getIdDonGia());

            // ===== BƯỚC 4: LẤY CÁC BẬC GIÁ =====
            ArrayList<WaterPriceTier> tiers = bacGiaDao.getBacGiaNuocByIdDonGia(giaNuoc.getIdDonGia());
            if (tiers.isEmpty()) {
                showWarning("Bảng giá chưa có bậc giá!\n" +
                        "Chỉ số đã được lưu nhưng chưa lập hóa đơn.\n" +
                        "Vui lòng thêm bậc giá trong bảng giá.");
                dispose();
                return;
            }

            System.out.println("✓ Số bậc giá: " + tiers.size());

            // ===== BƯỚC 5: TÍNH TIỀN THEO BẬC THANG =====
            double totalMoney = calculateBill(tieuThu, tiers, giaNuoc.getThue());

            System.out.println("✓ Tổng tiền: " + String.format("%,.0f VNĐ", totalMoney));

            // ===== BƯỚC 6: TẠO HÓA ĐƠN =====
            HoaDon hoaDon = new HoaDon();
            hoaDon.setIdChiSo(idChiSo);
            hoaDon.setSanLuongTieuThu(tieuThu);
            hoaDon.setTongTienThanhToan(totalMoney);
            hoaDon.setNgayLapHoaDon(LocalDate.now().toString());
            hoaDon.setTrangThaiHoaDon(0); // Chưa thanh toán
            hoaDon.setIdDonGia(giaNuoc.getIdDonGia());

            if (!hoaDonDao.addHoaDon(hoaDon)) {
                showWarning("Lưu chỉ số thành công nhưng tạo hóa đơn thất bại!\n" +
                        "Vui lòng kiểm tra lại.");
                dispose();
                return;
            }

            System.out.println("✓ Tạo hóa đơn thành công!");
            System.out.println("===================================");

            // ===== THÀNH CÔNG =====
            showSuccessDialog(tieuThu, totalMoney);
            dispose();

        } catch (NumberFormatException e) {
            showError("Vui lòng nhập chỉ số hợp lệ!");
            e.printStackTrace();
        } catch (Exception e) {
            showError("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tìm bảng giá phù hợp với hộ sử dụng
     */
    private GiaNuoc findGiaNuoc() {
        ArrayList<GiaNuoc> allGiaNuoc = giaNuocDao.getGiaNuoc();

        for (GiaNuoc gn : allGiaNuoc) {
            // Kiểm tra khu vực và loại khách hàng
            if (gn.getKhuVuc().equals(HoSuDung.getKhuVuc()) &&
                    gn.getIdLoaiCustomer() == customer.getLoaiCustomer()) {
                return gn;
            }
        }

        return null;
    }

    /**
     * Tính tiền theo bậc thang sử dụng WaterBill
     */
    private double calculateBill(int tieuThu, ArrayList<WaterPriceTier> tiers, double thuePercent) {
        // Chuẩn bị dữ liệu cho WaterBill
        int[] tuMucNuoc = new int[tiers.size()];
        int[] denMucNuoc = new int[tiers.size()];
        double[] gia = new double[tiers.size()];

        for (int i = 0; i < tiers.size(); i++) {
            WaterPriceTier tier = tiers.get(i);
            tuMucNuoc[i] = tier.getMinConsumption();
            denMucNuoc[i] = tier.getMaxConsumption();
            gia[i] = tier.getPrice();
        }

        // Chuyển thuế từ % sang hệ số (ví dụ: 10% → 1.1)
        double thueHeSo = 1 + (thuePercent / 100);

        // Tính tiền
        double totalMoney = WaterBill.calculateTotalMoney(
                tieuThu,
                tuMucNuoc,
                denMucNuoc,
                gia,
                thueHeSo);

        return totalMoney;
    }

    /**
     * Hiển thị dialog thành công với thông tin hóa đơn
     */
    private void showSuccessDialog(int tieuThu, double totalMoney) {
        String message = String.format("""
                ✅ LƯU CHỈ SỐ VÀ LẬP HÓA ĐƠN THÀNH CÔNG!

                == Thông tin ==
                - Tiêu thụ: %d m³
                - Tổng tiền: %,.0f VNĐ
                - Trạng thái: Chưa thanh toán

                Hóa đơn đã được tạo tự động.
                """,
                tieuThu,
                totalMoney);

        JOptionPane.showMessageDialog(this,
                message,
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }
}