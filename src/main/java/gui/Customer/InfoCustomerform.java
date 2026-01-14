package gui.Customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.HoSuDungDao;
import dao.CustomerDao;
import model.Customer;
import model.hoSuDung;

public class InfoCustomerform extends JFrame {
    CustomerDao userDao = new CustomerDao();
    HoSuDungDao hoSuDungDao = new HoSuDungDao();

    JButton btnRefresh = new JButton("Refresh");
    JButton btnAdd = new JButton("Thêm hộ sử dụng");
    JButton btnEdit = new JButton("Sửa hộ sử dụng");

    ArrayList<hoSuDung> hoSuDungArr = new ArrayList<hoSuDung>();

    public InfoCustomerform(Customer customer) {
        setTitle("Thông tin khách hàng %s".formatted(customer.getNameCustomer()));
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel thông tin khách hàng (trên)
        add(createInfoPanel(customer), BorderLayout.CENTER);

        // Panel button thao tác (giữa)
        add(createButtonPanel(customer.getIdCustomer()), BorderLayout.NORTH);

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

    private JPanel createButtonPanel(int customerId) {
        JPanel panel = new JPanel(new FlowLayout());

        panel.add(btnRefresh);
        panel.add(btnAdd);
        panel.add(btnEdit);

        btnRefresh.addActionListener(e -> {
            // Cập nhật lại bảng hộ sử dụng
            JScrollPane tableScrollPane = createTablePanel(customerId);
            getContentPane().remove(2); // Xóa bảng cũ
            add(tableScrollPane, BorderLayout.SOUTH);
            revalidate();
            repaint();
        });

        btnAdd.addActionListener(e -> {
            new AddHoSuDungForm(customerId).setVisible(true);
        });

        btnEdit.addActionListener(e -> {
            int selectedRow = ((JTable) ((JScrollPane) getContentPane().getComponent(2)).getViewport().getView())
                    .getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hộ sử dụng để sửa.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idHoSuDung = (int) ((DefaultTableModel) ((JTable) ((JScrollPane) getContentPane().getComponent(2))
                    .getViewport().getView()).getModel()).getValueAt(selectedRow, 0);

            hoSuDung hoSuDung = hoSuDungDao.getHoSuDungById(idHoSuDung);
            if (hoSuDung != null) {
                AddHoSuDungForm editForm = new AddHoSuDungForm(hoSuDung);
                editForm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hộ sử dụng.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JScrollPane createTablePanel(int customerId) {
        String[] columns = { "Mã số hộ sử dụng", "Địa chỉ(số nhà)", "MaQuanHuyen", "Trạng thái" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setBackground(new Color(130, 204, 130));

        // Load dữ liệu từ database
        showHoSuDung(model, customerId);

        return new JScrollPane(table);
    }

    private void showHoSuDung(DefaultTableModel model, int customerId) {
        model.setRowCount(0); // Xóa dữ liệu cũ

        this.hoSuDungArr = hoSuDungDao.getHoSuDungByCustomerId(customerId);
        for (hoSuDung hoSuDung : hoSuDungArr) {
            String trangThai = hoSuDung.getTrangThai() == 1 ? "Đang sử dụng" : "Ngừng sử dụng";
            Object[] row = {
                    hoSuDung.getID_HoSuDung(),
                    hoSuDung.getDiaChi(),
                    hoSuDung.getMaQuanHuyen(),
                    trangThai
            };
            model.addRow(row);
        }

    }

    public static void main(String[] args) {
        Customer test = new Customer(1, "Nguyễn Văn A", 1, "123456789", "0901234567", "test@email.com");
        new InfoCustomerform(test).setVisible(true);
    }
}