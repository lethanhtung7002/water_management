package gui.User;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dao.UserDao;
import model.LoaiCustomer;
import model.Customer;

public class AddHoSuDungForm extends JFrame {
    JLabel lbAdd = new JLabel("Thêm thông tin hộ sử dụng");

    JLabel lbKhuVuc = new JLabel("Tỉnh");
    JLabel lbAddress = new JLabel("Địa chỉ(số nhà, tên đường)");
    JLabel lbTrangThai = new JLabel("Trạng Thái");

    JComboBox<String> cbKhuVuc = new JComboBox<>();
    JTextField tfAddress = new JTextField();
    JComboBox<String> cbTrangThai = new JComboBox<>();

    JButton btnSave = new JButton("Save");
    JButton btnCancel = new JButton("Cancel");

    UserDao dao = new UserDao();

    public AddHoSuDungForm(Customer user) {
        setTitle(getTitle());
        init();

        // btnSave.addActionListener(e -> saveUser());
        btnCancel.addActionListener(e -> dispose());

    }

    public void init() {
        setSize(500, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int height = 30;
        int x_lb = 50;
        int x_tf_cb =240;

        lbAdd.setBounds(160, 10, 200, 30);
        lbKhuVuc.setBounds(x_lb, 60, 200, height);
        cbKhuVuc.setBounds(x_tf_cb, 60, 200, height);
        lbAddress.setBounds(x_lb, 100, 200, height);
        tfAddress.setBounds(x_tf_cb, 100, 200, height);
        lbTrangThai.setBounds(x_lb,140, 200,height);
        cbTrangThai.setBounds(x_tf_cb,140, 200,height);
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
}

class run {
    public static void main(String[] args) {
        new AddHoSuDungForm(null).setVisible(true);
    }
}
