package gui.GiaNuoc;

import java.util.List;

import javax.swing.*;
import java.awt.*;

import dao.*;
import data.KhuVucLoader;
import gui.Customer.GUIConstants;
import model.Customer;
import model.GiaNuoc;
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
    private JButton btnCancel = new JButton("Cancel");

    private GiaNuocDao gnDao = new GiaNuocDao();
    private CustomerDao cDao = new CustomerDao();

    public AddWaterPriceForm(){
        setTitle("Thêm chính sách giá nước");
        init();

        // btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
        pack();
    }

    public AddWaterPriceForm(GiaNuoc gn){
        setTitle("Sửa chính sách giá nước");
        // init();

        btnCancel.addActionListener(e -> dispose());
        pack();
    }

    public void init(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load danh sách loại khách hàng từ database
        List<loaiCustomer> loaiNguoiDung = cDao.getLoaiKhachHang();
        for (loaiCustomer lnd : loaiNguoiDung) {
            cbCustomerType.addItem(lnd);
        }
        new KhuVucLoader().loadKhuVuc(cbKhuVuc);

        add(createFormPanel(), BorderLayout.CENTER);

        // Thêm button panel vào south
        // add(createButtonPanel(), BorderLayout.SOUTH);

    }

    private JPanel createFormPanel(){
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

}

class run_test{
    public static void main(String[] args) {
        new AddWaterPriceForm().setVisible(true);
    }
}
