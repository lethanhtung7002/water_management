/* This is page in MenuForm
 */

package gui.Customer;

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

import dao.CustomerDao;
import model.Customer;

public class CustomerPage extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private JButton btnAdd = new JButton("Thêm Khách Hàng");
    private JButton btnEdit = new JButton("Sửa Thông Tin Khách Hàng");
    private JButton btnDelete = new JButton("Delete User");
    private JButton btnRefresh = new JButton("Refresh");
    private JButton btnInfo = new JButton("Info");

    private ArrayList<Customer> UserArr = new ArrayList<Customer>();
    private CustomerDao userDao = new CustomerDao();

    public CustomerPage() {

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

        String[] columnNames = { "ID", "Tên Khách hàng", "Loai Khách hàng", "CCCD", "số điện thoại", "Email" };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(table);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // showUserList();

        // === SỰ KIỆN BUTTONS ===
        btnRefresh.addActionListener(e -> showUserList());

        btnAdd.addActionListener(e -> {
            new AddCustomerForm().setVisible(true);
        });

        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a user to edit.",
                        "No User Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Customer selectedUser = userDao.getCustomerById((Integer) tableModel.getValueAt(selectedRow, 0));

            if (selectedUser != null) {
                AddCustomerForm editForm = new AddCustomerForm(selectedUser);
                editForm.setVisible(true);
            }
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
                if (userDao.deleteUserById(Integer.parseInt(idDelete))) {
                    JOptionPane.showMessageDialog(this,
                            "User Deleted Successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    showUserList();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "User Deleted Failed",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnInfo.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a user to view info.",
                        "No User Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Customer selectedUser = userDao.getCustomerById((Integer) tableModel.getValueAt(selectedRow, 0));

            if (selectedUser != null) {
                InfoCustomerform infoForm = new InfoCustomerform(selectedUser);
                infoForm.setVisible(true);
            }
        });
    }

    public void showUserList() {
        // Lấy dữ liệu từ DAO
        tableModel.setRowCount(0);
        this.UserArr = userDao.getCustomers();

        for (Customer user : UserArr) {
            tableModel.addRow(new Object[] {
                    user.getIdCustomer(),
                    user.getNameCustomer(),
                    user.getLoaiCustomer(),
                    user.getCCCD(),
                    user.getPhoneCustomer(),
                    user.getEmail()
            });
        }
    }
}