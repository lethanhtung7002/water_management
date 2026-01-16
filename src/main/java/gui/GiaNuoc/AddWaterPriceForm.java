package gui.GiaNuoc;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dao.*;
import data.KhuVucLoader;
import model.Customer;
import model.GiaNuoc;
import model.loaiCustomer;

public class AddWaterPriceForm extends JFrame {

    // === Labels ===
    private JLabel lbNameForm = new JLabel("Thêm chính sách giá nước");
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
    }

    public AddWaterPriceForm(GiaNuoc gn){
        setTitle("Sửa chính sách giá nước");
        // init();

        btnCancel.addActionListener(e -> dispose());
    }

    public void init(){
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Load danh sách loại khách hàng từ database
        List<loaiCustomer> loaiNguoiDung = cDao.getLoaiKhachHang();
        for (loaiCustomer lnd : loaiNguoiDung) {
            cbCustomerType.addItem(lnd);
        }
        new KhuVucLoader().loadKhuVuc(cbKhuVuc);

        int height = 40;

        // x, y, wight, h
        lbNameForm.setBounds(159, 10, 200, height);


        add(lbNameForm);
    }

}

class run_test{
    public static void main(String[] args) {
        new AddWaterPriceForm().setVisible(true);
    }
}
