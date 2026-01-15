package gui.Customer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dao.HoSuDungDao;
import model.hoSuDung;
import data.DataLoader;

public class AddHoSuDungForm extends JFrame {
    private JLabel lbAdd = new JLabel("Thêm thông tin hộ sử dụng");

    private JLabel lbKhuVuc = new JLabel("Tỉnh");
    private JLabel lbAddress = new JLabel("Địa chỉ(số nhà, tên đường)");
    private JLabel lbTrangThai = new JLabel("Trạng Thái");

    private JComboBox<String> cbKhuVuc = new JComboBox<>();
    private JTextField tfAddress = new JTextField();
    private JComboBox<String> cbTrangThai = new JComboBox<>();

    private JButton btnSave = new JButton("Save");
    private JButton btnCancel = new JButton("Cancel");

    private HoSuDungDao dao = new HoSuDungDao();
    private int customerId;

    /* Truyền thông số id của Khách hàng vào bảng thêm hộ sử dụng tương ứng */
    public AddHoSuDungForm(int customerId) {
        setTitle(getTitle());
        init();

        btnSave.addActionListener(e -> saveHoSuDung());
        btnCancel.addActionListener(e -> dispose());
        this.customerId = customerId;
    }

    /* Chỉnh sửa sử dụng lại thông số đã chọn */
    public AddHoSuDungForm(hoSuDung hoSuDung) {
        setTitle("Sửa thông tin hộ sử dụng");
        init();

        // Điền thông tin hộ sử dụng vào form
        cbKhuVuc.setSelectedItem(hoSuDung.getMaQuanHuyen());
        tfAddress.setText(hoSuDung.getDiaChi());
        cbTrangThai.setSelectedItem(hoSuDung.getTrangThai() == 1 ? "Đang sử dụng" : "Ngừng sử dụng");

        btnSave.addActionListener(e -> saveHoSuDung(hoSuDung));
        btnCancel.addActionListener(e -> dispose());
        this.customerId = hoSuDung.getID_Customer();
    }

    void init() {
        setSize(500, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        loadKhuVuc();
        loadtrangThai();

        int height = 30;
        int x_lb = 50;
        int x_tf_cb = 240;

        lbAdd.setBounds(160, 10, 200, 30);
        lbKhuVuc.setBounds(x_lb, 60, 200, height);
        cbKhuVuc.setBounds(x_tf_cb, 60, 200, height);
        lbAddress.setBounds(x_lb, 100, 200, height);
        tfAddress.setBounds(x_tf_cb, 100, 200, height);
        lbTrangThai.setBounds(x_lb, 140, 200, height);
        cbTrangThai.setBounds(x_tf_cb, 140, 200, height);
        btnSave.setBounds(80, 280, 100, 30);
        btnCancel.setBounds(220, 280, 100, 30);

        add(lbAdd);
        add(lbKhuVuc);
        add(cbKhuVuc);
        add(lbAddress);
        add(tfAddress);
        add(lbTrangThai);
        add(cbTrangThai);

        add(btnCancel);
        add(btnSave);
    }

    void loadKhuVuc() {
        cbKhuVuc.addItem("-- Chọn tỉnh ---");
        new DataLoader().khuVuc().forEach(cbKhuVuc::addItem);
    }

    void loadtrangThai() {
        cbTrangThai.addItem("-- Chọn trạng thái ---");
        cbTrangThai.addItem("Đang sử dụng");
        cbTrangThai.addItem("Ngừng sử dụng");
    }

    private void saveHoSuDung(hoSuDung hoSuDung) {
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

        hoSuDung.setMaQuanHuyen(khuVuc);
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

        hoSuDung hoSuDung = new hoSuDung();
        hoSuDung.setID_Customer(customerId);
        hoSuDung.setDiaChi(diaChi);
        hoSuDung.setMaQuanHuyen(khuVuc);
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
