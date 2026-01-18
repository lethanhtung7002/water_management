package gui.Customer;

import javax.swing.*;
import java.awt.*;

import dao.HoSuDungDao;
import data.KhuVucLoader;
import gui.GUIConstants;
import model.HoSuDung;

public class AddHoSuDungForm extends JFrame {

    private JLabel lbKhuVuc = new JLabel("Tỉnh");
    private JLabel lbAddress = new JLabel("Địa chỉ(số nhà, tên đường)");
    private JLabel lbTrangThai = new JLabel("Trạng Thái");

    // ===== INPUT FIELDS =====
    private JComboBox<String> cbKhuVuc = new JComboBox<>();
    private JTextField tfAddress = new JTextField();
    private JComboBox<String> cbTrangThai = new JComboBox<>();

    // ===== BUTTONS =====
    private JButton btnSave = new JButton("Save");
    private JButton btnCancel = new JButton("Cancel");

    private HoSuDungDao dao = new HoSuDungDao();
    private int customerId;

    /* Truyền thông số id của Khách hàng vào bảng thêm hộ sử dụng tương ứng */
    public AddHoSuDungForm(int customerId) {
        setTitle("Thêm hộ sử dụng");
        init();
        pack();

        btnSave.addActionListener(e -> saveHoSuDung());
        btnCancel.addActionListener(e -> dispose());
        this.customerId = customerId;

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /* Chỉnh sửa sử dụng lại thông số đã chọn */
    public AddHoSuDungForm(HoSuDung hoSuDung) {
        setTitle("Sửa thông tin hộ sử dụng");
        init();

        // Điền thông tin hộ sử dụng vào form
        cbKhuVuc.setSelectedItem(hoSuDung.getKhuVuc());
        tfAddress.setText(hoSuDung.getDiaChi());
        cbTrangThai.setSelectedItem(hoSuDung.getTrangThai() == 1 ? "Đang sử dụng" : "Ngừng sử dụng");

        btnSave.addActionListener(e -> saveHoSuDung(hoSuDung));
        btnCancel.addActionListener(e -> dispose());
        this.customerId = hoSuDung.getID_Customer();

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        new KhuVucLoader().loadKhuVuc(cbKhuVuc);
        loadtrangThai();

        add(createFormPanel(), BorderLayout.CENTER);

        add(createButtonPanel(), BorderLayout.SOUTH);
    }

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

        // === Khu Vuc ===
        gbc.gridx = 0;
        gbc.gridy = row;
        lbKhuVuc.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbKhuVuc, gbc);

        gbc.gridx = 1;
        cbKhuVuc.setPreferredSize(GUIConstants.Sizes.tf);
        cbKhuVuc.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(cbKhuVuc, gbc);
        row++;

        // ===== Dia Chi =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbAddress.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbAddress, gbc);

        gbc.gridx = 1;
        tfAddress.setPreferredSize(GUIConstants.Sizes.tf);
        tfAddress.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(tfAddress, gbc);
        row++;

        // ===== CCCD =====
        gbc.gridx = 0;
        gbc.gridy = row;
        lbTrangThai.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbTrangThai, gbc);

        gbc.gridx = 1;
        cbTrangThai.setPreferredSize(GUIConstants.Sizes.tf);
        cbTrangThai.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(cbTrangThai, gbc);
        row++;

        return panel;
    }

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

    void loadtrangThai() {
        cbTrangThai.addItem("-- Chọn trạng thái ---");
        cbTrangThai.addItem("Đang sử dụng");
        cbTrangThai.addItem("Ngừng sử dụng");
    }

    private void saveHoSuDung(HoSuDung hoSuDung) {
        // Lấy thông tin từ form
        String khuVuc = (String) cbKhuVuc.getSelectedItem();
        String diaChi = tfAddress.getText().trim();
        String trangThai = (String) cbTrangThai.getSelectedItem();

        if (khuVuc == null || khuVuc.equals("-- Chọn tỉnh ---")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực hợp lệ.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (diaChi == null || diaChi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (trangThai == null || trangThai.equals("-- Chọn trạng thái ---")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái hợp lệ.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        hoSuDung.setKhuVuc(khuVuc);
        hoSuDung.setDiaChi(diaChi);
        hoSuDung.setTrangThai(trangThai.equals("Đang sử dụng") ? 1 : 0);

        if (dao.update_HoSuDung(hoSuDung)) {
            JOptionPane.showMessageDialog(this, "Cập nhật hộ sử dụng thành công!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật hộ sử dụng.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveHoSuDung() {
        // Lấy thông tin từ form
        String khuVuc = (String) cbKhuVuc.getSelectedItem();
        String diaChi = tfAddress.getText();
        String trangThai = (String) cbTrangThai.getSelectedItem();

        if (khuVuc == null || khuVuc.equals("-- Chọn tỉnh ---")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực hợp lệ.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (diaChi == null || diaChi.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (trangThai == null || trangThai.equals("-- Chọn trạng thái ---")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái hợp lệ.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        HoSuDung hoSuDung = new HoSuDung();
        hoSuDung.setID_Customer(customerId);
        hoSuDung.setDiaChi(diaChi);
        hoSuDung.setKhuVuc(khuVuc);
        hoSuDung.setTrangThai(trangThai.equals("Đang sử dụng") ? 1 : 0);

        if (dao.add_HoSuDung(hoSuDung)) {
            JOptionPane.showMessageDialog(this, "Thêm hộ sử dụng thành công!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm hộ sử dụng.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

// class run {
// public static void main(String[] args) {
// new AddHoSuDungForm(0).setVisible(true);
// }
// }
