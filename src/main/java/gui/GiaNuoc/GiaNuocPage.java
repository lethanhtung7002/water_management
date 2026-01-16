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

public class GiaNuocPage extends JPanel {

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

    public GiaNuocPage() {
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

        String[] columnNames = { "Id Đơn giá", "Loại khách hàng", "Khu vực", "Thue"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(table);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // showGiaNuocList();

        btnRefresh.addActionListener(e -> showGiaNuocList());
        btnAdd.addActionListener(e -> {
            // new AddGiaNuocForm().setVisible(true);
            
        });

        btnEdit.addActionListener(e -> {
            // new EditGiaNuocForm().setVisible(true);
            
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a user to delete.",
                        "No User Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this user?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                String idDelete = tableModel.getValueAt(selectedRow, 0).toString();
                if (giaNuocDao.deleteGiaNuocById(Integer.parseInt(idDelete))) {
                    JOptionPane.showMessageDialog(this,
                            "User Deleted Successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    showGiaNuocList();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "User Deleted Failed",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            
        });

        btnInfo.addActionListener(e -> {
            // new InfoGiaNuocForm().setVisible(true);
            
        });

    }

    public void showGiaNuocList() {
        gnArr = giaNuocDao.getGiaNuoc();
        tableModel.setRowCount(0);
        for (GiaNuoc gn : gnArr) {
            tableModel.addRow(new Object[] { 
                gn.getIdDonGia(), 
                gn.getIdLoaiCustomer(),  
                gn.getKhuVuc(), 
                gn.getThue()
            });
        }
    }
}
