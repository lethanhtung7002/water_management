package gui.GiaNuoc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.GiaNuocDao;
import model.GiaNuoc;

public class GiaNuocForm extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private JButton btnAdd = new JButton("Thêm giá nước");
    private JButton btnEdit = new JButton("Sửa giá nước");
    private JButton btnDelete = new JButton("Xóa giá nước");
    private JButton btnRefresh = new JButton("Làm mới");
    private JButton btnInfo = new JButton("Thông tin");

    private ArrayList<GiaNuoc> gnArr = new ArrayList<>();
    private GiaNuocDao giaNuocDao = new GiaNuocDao();

    public GiaNuocForm() {
        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(26, 26, 26)); // Thêm màu nền cho phù hợp với MenuForm

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBackground(new Color(26, 26, 26)); // Màu nền

        topPanel.add(btnRefresh);
        topPanel.add(btnAdd);
        topPanel.add(btnEdit);
        topPanel.add(btnDelete);
        topPanel.add(btnInfo);

        String[] columnNames = {"Id Đơn giá", "Đơn giá", "Loại khách hàng"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(table);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        
}

// class test{
//     public static void main(String[] args) {
//         new MySQLConnect();
//     }
// }
}
