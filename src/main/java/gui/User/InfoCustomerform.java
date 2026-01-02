package gui.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.UserDao;
import model.Customer;

public class InfoCustomerform extends JFrame {
    UserDao userDao = new UserDao();

    JButton btnAdd = new JButton("Thêm hộ sử dụng");
    JButton btnEdit = new JButton("Sửa hộ sử dụng");
    JButton btnChangeStatus = new JButton("Thay đổi trạng thái");

    public InfoCustomerform(Customer customer) {
        setTitle("Thông tin khách hàng %s".formatted(customer.getNameCustomer()));
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel thông tin khách hàng (trên)
        add(createInfoPanel(customer), BorderLayout.NORTH);

        // Panel button thao tác (giữa)
        add(createButtonPanel(), BorderLayout.CENTER);

        // Bảng hộ sử dụng (dưới)
        add(createTablePanel(customer.getIdCustomer()), BorderLayout.SOUTH);
    }

    private JPanel createInfoPanel(Customer customer) {
        JPanel panel = new JPanel(new GridLayout(2, 4));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

        addInfo(panel, "ID:", String.valueOf(customer.getIdCustomer()));
        addInfo(panel, "Tên:", customer.getNameCustomer());
        addInfo(panel, "Loại KH:", String.valueOf(customer.getLoaiCustomer()));
        addInfo(panel, "CCCD:", customer.getCCCD());
        addInfo(panel, "SĐT:", customer.getPhoneCustomer());
        addInfo(panel, "Email:", customer.getEmail());

        return panel;
    }

    private void addInfo(JPanel panel, String label, String value) {
        JLabel lbl = new JLabel(label + " " + value);
        panel.add(lbl);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3));

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnChangeStatus);

        return panel;
    }

    private JScrollPane createTablePanel(int customerId) {
        String[] columns = { "Mã số hộ sử dụng", "Địa chỉ(số nhà)", "MaQuanHuyen", "Trạng thái" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setBackground(new Color(130, 204, 130));
        table.getTableHeader().setForeground(Color.WHITE);

        // Load dữ liệu từ database
        loadHoSuDung(model, customerId);

        return new JScrollPane(table);
    }

    private void loadHoSuDung(DefaultTableModel model, int customerId) {
        // TODO: Thay bằng query thực tế từ database
        // Ví dụ:
        // List<HoSuDung> list = userDao.getHoSuDungByCustomerId(customerId);
        // for (HoSuDung h : list) {
        // model.addRow(new Object[]{h.getMaHo(), h.getDiaChi(), h.getDienTich(),
        // h.getSoNhanKhau(), h.getTrangThai()});
        // }

        // Data mẫu
        model.addRow(new Object[] { "H001", "123 Nguyễn Văn Linh", "80m²", 4, "Đang sử dụng" });
        model.addRow(new Object[] { "H002", "456 Lê Lợi", "100m²", 5, "Đang sử dụng" });
    }

    public static void main(String[] args) {
        Customer test = new Customer(1, "Nguyễn Văn A", 1, "123456789", "0901234567", "test@email.com");
        new InfoCustomerform(test).setVisible(true);
    }
}