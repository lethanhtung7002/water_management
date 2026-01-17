package gui.GiaNuoc;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

import dao.*;
import data.KhuVucLoader;
import gui.GUIConstants;
import model.Customer;
import model.*;
import model.loaiCustomer;

public class AddWaterPriceForm extends JFrame {

    // === Labels ===
    private JLabel lbCustomerTpye = new JLabel("Loại Khách Hàng");
    private JLabel lbKhuVuc = new JLabel("Khu Vực");
    private JLabel lbThue = new JLabel("Thue");

    // === Input ===
    private JComboBox<loaiCustomer> cbCustomerType = new JComboBox<>();
    private JComboBox<String> cbKhuVuc = new JComboBox<>();
    private JTextField tfThue = new JTextField();

    // === Buttons ===
    private JButton btnSave = new JButton("Save");
    private JButton btnUpdate = new JButton("Update");
    private JButton btnCancel = new JButton("Cancel");
    private JButton btnRefresh = new JButton("Làm mới");
    private JButton btnAdd = new JButton("Thêm bậc giá nước");
    private JButton btnEdit = new JButton("Sửa bậc giá nước");
    private JButton btnDelete = new JButton("Xóa bậc giá nước");

    private GiaNuocDao gnDao = new GiaNuocDao();
    private CustomerDao cDao = new CustomerDao();

    private JScrollPane wptTable;

    ArrayList<WaterPriceTier> WaterPriceTiersArr = new ArrayList<>();

    public AddWaterPriceForm() {
        setTitle("Thêm chính sách giá nước");
        init();

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
        setLocationRelativeTo(null);
        pack();
    }

    public AddWaterPriceForm(GiaNuoc gn) {
        setTitle("Sửa chính sách giá nước");
        init(gn.getIdDonGia());

        cbCustomerType.setSelectedItem(gn.getIdLoaiCustomer());
        cbKhuVuc.setSelectedItem(gn.getKhuVuc());
        tfThue.setText(String.valueOf(gn.getThue()));

        btnUpdate.addActionListener(e -> updateData(gn.getIdDonGia()));
        btnCancel.addActionListener(e -> dispose());
        setLocationRelativeTo(null);
        pack();
    }

    public void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load danh sách loại khách hàng từ database
        List<loaiCustomer> loaiNguoiDung = cDao.getLoaiKhachHang();
        for (loaiCustomer lnd : loaiNguoiDung) {
            cbCustomerType.addItem(lnd);
        }
        new KhuVucLoader().loadKhuVuc(cbKhuVuc);

        add(createFormPanel(), BorderLayout.WEST);
        add(createBtnWP(), BorderLayout.CENTER);

        // Thêm button panel vào south
        add(createBtnWP(), BorderLayout.SOUTH);

    }

    public void init(int idWPT) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load danh sách loại khách hàng từ database
        List<loaiCustomer> loaiNguoiDung = cDao.getLoaiKhachHang();
        for (loaiCustomer lnd : loaiNguoiDung) {
            cbCustomerType.addItem(lnd);
        }
        new KhuVucLoader().loadKhuVuc(cbKhuVuc);

        JPanel leftFrame = new JPanel(new BorderLayout());
        leftFrame.add(createFormPanel(), BorderLayout.WEST);
        leftFrame.add(createBtnWP(), BorderLayout.SOUTH);

        JPanel rightFrame = new JPanel(new BorderLayout());
        rightFrame.add(createBtnWPT(idWPT), BorderLayout.NORTH);
        rightFrame.add(createTableWPT(idWPT));

        add(leftFrame, BorderLayout.WEST);
        add(rightFrame, BorderLayout.CENTER);

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

        // ===== LOẠI KHÁCH HÀNG =====
        gbc.gridx = 0;
        gbc.gridy = row++;
        lbCustomerTpye.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbCustomerTpye, gbc);

        gbc.gridx = 1;
        cbCustomerType.setPreferredSize(GUIConstants.Sizes.tf);
        cbCustomerType.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(cbCustomerType, gbc);

        // ===== KHU VỰC =====
        gbc.gridx = 0;
        gbc.gridy = row++;
        lbKhuVuc.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbKhuVuc, gbc);

        gbc.gridx = 1;
        cbKhuVuc.setPreferredSize(GUIConstants.Sizes.tf);
        cbKhuVuc.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(cbKhuVuc, gbc);

        // ===== THUẾ =====
        gbc.gridx = 0;
        gbc.gridy = row++;
        lbThue.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(lbThue, gbc);

        gbc.gridx = 1;
        tfThue.setPreferredSize(GUIConstants.Sizes.tf);
        tfThue.setFont(GUIConstants.Fonts.TieuDePhu);
        panel.add(tfThue, gbc);

        return panel;
    }

    private JPanel createBtnWP() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnSave.setPreferredSize(GUIConstants.Sizes.btn); // kích thước button
        btnCancel.setPreferredSize(GUIConstants.Sizes.btn);

        btnSave.setFont(GUIConstants.Fonts.TieuDePhu); // font button
        btnCancel.setFont(GUIConstants.Fonts.TieuDePhu);

        panel.add(btnSave);
        panel.add(btnCancel);
        return panel;
    }

    private JPanel createBtnWPT(int wptId) {
        JPanel panel = new JPanel(new FlowLayout());

        panel.add(new JLabel("Bậc giá nước"));
        panel.add(btnRefresh);
        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);

        btnRefresh.addActionListener(e -> {
            this.wptTable = createTableWPT(wptId);
        });

        btnAdd.addActionListener(e -> {
            
        });

        btnEdit.addActionListener(e -> {
            
        });

        btnDelete.addActionListener(e -> {
            
        });

        return panel;
    }

    private JScrollPane createTableWPT(int wptId) {
        String[] col = { "Bậc Giá", "Từ mức nước", " Đến Mức Nước", "Giá" };
        DefaultTableModel model = new DefaultTableModel(col, 0);

        JTable table = new JTable(model);
        table.setRowHeight(25);

        showWaterPriceTiers(model, wptId);

        return new JScrollPane(table);
    }

    private void showWaterPriceTiers(DefaultTableModel model, int wptId) {
        model.setRowCount(0);

        this.WaterPriceTiersArr = gnDao.getBacGiaNuocByIdGiaNuoc(wptId);
        for (WaterPriceTier wpt : WaterPriceTiersArr) {
            model.addRow(new Object[] { wpt.getTier(),
                    wpt.getMinConsumption(),
                    wpt.getMaxConsumption(),
                    wpt.getPrice() });
        }

    }

    private void save() {
        // Lấy loại khách hàng đã chọn từ ComboBox
        loaiCustomer loaiKhachHang = (loaiCustomer) cbCustomerType.getSelectedItem();

        // Lấy khu vực đã chọn từ ComboBox
        String khuVuc = (String) cbKhuVuc.getSelectedItem();

        // Lấy thuế đã nhập từ TextField
        double thue = Double.parseDouble(tfThue.getText());

        // Tạo đối tượng GiaNuoc
        GiaNuoc giaNuoc = new GiaNuoc(
                0,
                loaiKhachHang.getIdLoaiCustomer(),
                khuVuc,
                thue);

        // Lưu vào database
        if (gnDao.addGiaNuoc(giaNuoc)) {
            // Hiển thị thông báo thành công
            JOptionPane.showMessageDialog(this, "Thêm chính sách giá nước thành công!", "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Thêm chính sách giá nước thất bại!", "Thất bại",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Đóng form hiện tại
        dispose();
    }

    private void updateData(int ID_giaNuoc) {
        // Lấy loại khách hàng đã chọn từ ComboBox
        loaiCustomer loaiKhachHang = (loaiCustomer) cbCustomerType.getSelectedItem();

        // Lấy khu vực đã chọn từ ComboBox
        String khuVuc = (String) cbKhuVuc.getSelectedItem();

        // Lấy thuế đã nhập từ TextField
        double thue = Double.parseDouble(tfThue.getText());

        GiaNuoc giaNuoc = new GiaNuoc(
                ID_giaNuoc,
                loaiKhachHang.getIdLoaiCustomer(),
                khuVuc,
                thue);

        // Lưu vào database
        if (gnDao.updateGiaNuoc(giaNuoc)) {
            // Hiển thị thông báo thành công
            JOptionPane.showMessageDialog(this, "Cập nhật chính sách giá nước thành công!", "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật chính sách giá nước thất bại!", "Thất bại",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Đóng form hiện tại
        dispose();
    }

}

class run_test {
    public static void main(String[] args) {
        new AddWaterPriceForm().setVisible(true);
    }
}
