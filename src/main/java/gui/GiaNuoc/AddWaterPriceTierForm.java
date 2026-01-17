package gui.GiaNuoc;

import java.awt.*;
import javax.swing.*;

import dao.BacGiaDao;
import gui.GUIConstants;
import model.WaterPriceTier;

/**
 * Form thêm mới hoặc chỉnh sửa bậc giá nước.
 * 
 * Chức năng:
 * - Thêm bậc giá nước mới cho một chính sách giá nước
 * - Sửa bậc giá nước đã có
 * - Validate dữ liệu đầu vào
 * 
 * Cấu trúc bảng bacgia:
 * - ID_Bac (auto increment)
 * - ID_DonGia (foreign key)
 * - BacGia (tier number: 1, 2, 3...)
 * - TuMucNuoc (từ mức nước - m³)
 * - DenMucNuoc (đến mức nước - m³)
 * - Gia (giá tiền - VNĐ/m³)
 * 
 * @author Lê Thanh Tùng
 * @version 1.0
 */
public class AddWaterPriceTierForm extends JFrame {

    // ===== LABELS =====
    private JLabel lbBacGia = new JLabel("Bậc giá");
    private JLabel lbTuMucNuoc = new JLabel("Từ mức nước (m³)");
    private JLabel lbDenMucNuoc = new JLabel("Đến mức nước (m³)");
    private JLabel lbGia = new JLabel("Giá (VNĐ/m³)");

    // ===== INPUT FIELDS =====
    private JTextField tfBacGia = new JTextField();
    private JTextField tfTuMucNuoc = new JTextField();
    private JTextField tfDenMucNuoc = new JTextField();
    private JTextField tfGia = new JTextField();

    // ===== BUTTONS =====
    private JButton btnSave = new JButton("Lưu");
    private JButton btnCancel = new JButton("Hủy");

    // ===== DAO =====
    private BacGiaDao bacGiaDao = new BacGiaDao();

    // ===== DATA =====
    private int idDonGia; // ID của chính sách giá nước

    /**
     * Constructor cho chế độ THÊM MỚI bậc giá nước.
     * 
     * @param idDonGia ID của chính sách giá nước
     */
    public AddWaterPriceTierForm(int idDonGia) {
        this.idDonGia = idDonGia;

        setTitle("Thêm bậc giá nước");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Tạo giao diện
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        // Lấy số bậc tiếp theo
        int nextTier = bacGiaDao.getNextTierNumber(idDonGia);
        tfBacGia.setText(String.valueOf(nextTier));
        tfBacGia.setEditable(false); // Không cho sửa, tự động tăng

        // Gợi ý mức nước dựa trên bậc trước
        suggestWaterRange(idDonGia);

        // Gắn sự kiện
        btnSave.addActionListener(e -> saveTier());
        btnCancel.addActionListener(e -> dispose());

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Constructor cho chế độ SỬA bậc giá nước.
     * 
     * @param tier Đối tượng WaterPriceTier cần sửa
     */
    public AddWaterPriceTierForm(WaterPriceTier tier) {
        this.idDonGia = tier.getIdWaterPrice();

        setTitle("Sửa bậc giá nước");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Tạo giao diện
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        // Điền dữ liệu vào form
        fillFormData(tier);

        // Không cho sửa bậc giá
        tfBacGia.setEditable(false);

        // Gắn sự kiện - Update thay vì Save
        btnSave.setText("Cập nhật");
        btnSave.addActionListener(e -> updateTier(tier.getIdWaterPriceTier()));
        btnCancel.addActionListener(e -> dispose());

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Tạo panel chứa form nhập liệu.
     * 
     * @return JPanel chứa form
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

        // ===== BẬC GIÁ =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbBacGia.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbBacGia, gbc);

        gbc.gridx = 1;
        tfBacGia.setPreferredSize(GUIConstants.Sizes.tf);
        tfBacGia.setFont(GUIConstants.Fonts.TieuDePhu);
        tfBacGia.setBackground(new Color(240, 240, 240)); // Màu xám nhạt vì không edit được
        panel.add(tfBacGia, gbc);
        row++;

        // ===== TỪ MỨC NƯỚC =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbTuMucNuoc.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbTuMucNuoc, gbc);

        gbc.gridx = 1;
        tfTuMucNuoc.setPreferredSize(GUIConstants.Sizes.tf);
        tfTuMucNuoc.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(tfTuMucNuoc, gbc);
        row++;

        // ===== ĐẾN MỨC NƯỚC =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbDenMucNuoc.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbDenMucNuoc, gbc);

        gbc.gridx = 1;
        tfDenMucNuoc.setPreferredSize(GUIConstants.Sizes.tf);
        tfDenMucNuoc.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(tfDenMucNuoc, gbc);
        row++;

        // ===== GIÁ =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbGia.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbGia, gbc);

        gbc.gridx = 1;
        tfGia.setPreferredSize(GUIConstants.Sizes.tf);
        tfGia.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(tfGia, gbc);

        return panel;
    }

    /**
     * Tạo panel chứa các nút Lưu và Hủy.
     * 
     * @return JPanel chứa buttons
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        btnSave.setPreferredSize(GUIConstants.Sizes.btn);
        btnCancel.setPreferredSize(GUIConstants.Sizes.btn);

        btnSave.setFont(GUIConstants.Fonts.TieuDePhu);
        btnCancel.setFont(GUIConstants.Fonts.TieuDePhu);

        panel.add(btnSave);
        panel.add(btnCancel);

        return panel;
    }

    /**
     * Gợi ý khoảng mức nước dựa trên bậc trước đó.
     * Ví dụ: Bậc 1 (0-10), Bậc 2 sẽ gợi ý từ 11.
     * 
     * @param idDonGia ID của chính sách giá nước
     */
    private void suggestWaterRange(int idDonGia) {
        try {
            WaterPriceTier lastTier = bacGiaDao.getLastTier(idDonGia);

            if (lastTier != null) {
                // Bậc tiếp theo bắt đầu từ (mức cuối bậc trước + 1)
                int fromLevel = lastTier.getMaxConsumption() + 1;
                tfTuMucNuoc.setText(String.valueOf(fromLevel));

                // Gợi ý mức cuối = mức đầu + 9
                int toLevel = fromLevel + 9;
                tfDenMucNuoc.setText(String.valueOf(toLevel));
            } else {
                // Bậc đầu tiên: từ 0
                tfTuMucNuoc.setText("0");
                tfDenMucNuoc.setText("10");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi gợi ý mức nước: " + e.getMessage());
        }
    }

    /**
     * Điền dữ liệu vào form (dùng cho chế độ sửa).
     * 
     * @param tier Đối tượng WaterPriceTier
     */
    private void fillFormData(WaterPriceTier tier) {
        tfBacGia.setText(String.valueOf(tier.getTier()));
        tfTuMucNuoc.setText(String.valueOf(tier.getMinConsumption()));
        tfDenMucNuoc.setText(String.valueOf(tier.getMaxConsumption()));
        tfGia.setText(String.valueOf(tier.getPrice()));
    }

    /**
     * Lưu bậc giá nước mới vào database.
     */
    private void saveTier() {
        try {
            System.out.println("=== SAVE TIER ===");

            // Validate và lấy dữ liệu
            int bacGia = Integer.parseInt(tfBacGia.getText().trim());

            String tuMucText = tfTuMucNuoc.getText().trim();
            if (tuMucText.isEmpty()) {
                showError("Vui lòng nhập từ mức nước!");
                return;
            }
            int tuMucNuoc = Integer.parseInt(tuMucText);

            String denMucText = tfDenMucNuoc.getText().trim();
            if (denMucText.isEmpty()) {
                showError("Vui lòng nhập đến mức nước!");
                return;
            }
            int denMucNuoc = Integer.parseInt(denMucText);

            String giaText = tfGia.getText().trim();
            if (giaText.isEmpty()) {
                showError("Vui lòng nhập giá!");
                return;
            }
            double gia = Double.parseDouble(giaText);

            // Validate logic
            if (tuMucNuoc < 0) {
                showError("Từ mức nước phải >= 0!");
                return;
            }

            if (denMucNuoc <= tuMucNuoc) {
                showError("Đến mức nước phải lớn hơn từ mức nước!");
                return;
            }

            if (gia <= 0) {
                showError("Giá phải lớn hơn 0!");
                return;
            }

            // Kiểm tra trùng lặp khoảng mức nước
            if (bacGiaDao.isRangeOverlapping(idDonGia, tuMucNuoc, denMucNuoc, -1)) {
                showError("Khoảng mức nước bị trùng với bậc giá khác!");
                return;
            }

            // Debug
            System.out.println("ID Đơn giá: " + idDonGia);
            System.out.println("Bậc giá: " + bacGia);
            System.out.println("Từ mức: " + tuMucNuoc);
            System.out.println("Đến mức: " + denMucNuoc);
            System.out.println("Giá: " + gia);

            // Tạo object WaterPriceTier
            WaterPriceTier tier = new WaterPriceTier(
                    0, // ID tự động tăng
                    idDonGia,
                    bacGia,
                    tuMucNuoc,
                    denMucNuoc,
                    gia);

            // Lưu vào database
            boolean success = bacGiaDao.addBacGia(tier);
            System.out.println("Kết quả: " + success);

            if (success) {
                showSuccess("Thêm bậc giá nước thành công!");
                dispose();
            } else {
                showError("Thêm bậc giá nước thất bại!");
            }

        } catch (NumberFormatException e) {
            showError("Vui lòng nhập số hợp lệ!");
            e.printStackTrace();
        } catch (Exception e) {
            showError("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật bậc giá nước đã có.
     * 
     * @param tierId ID của bậc giá cần cập nhật
     */
    private void updateTier(int tierId) {
        try {
            System.out.println("=== UPDATE TIER ===");
            System.out.println("Tier ID: " + tierId);

            // Validate và lấy dữ liệu
            int bacGia = Integer.parseInt(tfBacGia.getText().trim());

            String tuMucText = tfTuMucNuoc.getText().trim();
            if (tuMucText.isEmpty()) {
                showError("Vui lòng nhập từ mức nước!");
                return;
            }
            int tuMucNuoc = Integer.parseInt(tuMucText);

            String denMucText = tfDenMucNuoc.getText().trim();
            if (denMucText.isEmpty()) {
                showError("Vui lòng nhập đến mức nước!");
                return;
            }
            int denMucNuoc = Integer.parseInt(denMucText);

            String giaText = tfGia.getText().trim();
            if (giaText.isEmpty()) {
                showError("Vui lòng nhập giá!");
                return;
            }
            double gia = Double.parseDouble(giaText);

            // Validate logic
            if (tuMucNuoc < 0) {
                showError("Từ mức nước phải >= 0!");
                return;
            }

            if (denMucNuoc <= tuMucNuoc) {
                showError("Đến mức nước phải lớn hơn từ mức nước!");
                return;
            }

            if (gia <= 0) {
                showError("Giá phải lớn hơn 0!");
                return;
            }

            // Kiểm tra trùng lặp (trừ bản thân record đang sửa)
            if (bacGiaDao.isRangeOverlapping(idDonGia, tuMucNuoc, denMucNuoc, tierId)) {
                showError("Khoảng mức nước bị trùng với bậc giá khác!");
                return;
            }

            // Debug
            System.out.println("ID Đơn giá: " + idDonGia);
            System.out.println("Bậc giá: " + bacGia);
            System.out.println("Từ mức: " + tuMucNuoc);
            System.out.println("Đến mức: " + denMucNuoc);
            System.out.println("Giá: " + gia);

            // Tạo object WaterPriceTier
            WaterPriceTier tier = new WaterPriceTier(
                    tierId,
                    idDonGia,
                    bacGia,
                    tuMucNuoc,
                    denMucNuoc,
                    gia);

            // Cập nhật database
            boolean success = bacGiaDao.updateBacGia(tier);
            System.out.println("Kết quả: " + success);

            if (success) {
                showSuccess("Cập nhật bậc giá nước thành công!");
                dispose();
            } else {
                showError("Cập nhật bậc giá nước thất bại!");
            }

        } catch (NumberFormatException e) {
            showError("Vui lòng nhập số hợp lệ!");
            e.printStackTrace();
        } catch (Exception e) {
            showError("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
            new AddWaterPriceTierForm(1); // Test với ID_DonGia = 1
        });
    }
}