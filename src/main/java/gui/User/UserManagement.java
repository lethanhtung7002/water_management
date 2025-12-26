package gui.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.UserDao;
import model.User;

public class UserManagement extends JPanel { // ĐỔI TỪ JFrame → JPanel

    JLabel lbTitle = new JLabel("User Management");
    JTable table = new JTable();
    DefaultTableModel tableModel;
    JScrollPane scrollPane;

    JButton btnAdd = new JButton("Add User");
    JButton btnEdit = new JButton("Edit User");
    JButton btnDelete = new JButton("Delete User");
    JButton btnRefresh = new JButton("Refresh");

    ArrayList<User> UserArr = new ArrayList<User>();
    UserDao userDao = new UserDao();

    public UserManagement() {
        // BỎ setTitle, setDefaultCloseOperation, setLocationRelativeTo, setSize
        // CHỈ GIỮ LẠI setLayout và các thành phần UI

        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(26, 26, 26)); // Thêm màu nền cho phù hợp với MenuForm

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBackground(new Color(26, 26, 26)); // Màu nền

        // Style cho label
        lbTitle.setForeground(Color.WHITE);

        topPanel.add(lbTitle);
        topPanel.add(btnRefresh);
        topPanel.add(btnAdd);
        topPanel.add(btnEdit);
        topPanel.add(btnDelete);

        String[] columnNames = { "ID", "Name", "Loai User", "CCCD", "Phone Number", "Email" };
        tableModel = new DefaultTableModel(columnNames, 0);
        table.setModel(tableModel);
        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(table);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        showUserList();

        // === SỰ KIỆN BUTTONS ===
        btnRefresh.addActionListener(e -> showUserList());

        btnAdd.addActionListener(e -> {
            // Tìm parent JFrame để truyền vào AddUserForm
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window instanceof javax.swing.JFrame) {
                AddUserForm addUserForm = new AddUserForm();
                addUserForm.setVisible(true);
            } else {
                AddUserForm addUserForm = new AddUserForm(null);
                addUserForm.setVisible(true);
            }
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

            User selectedUser = userDao.getUserById((Integer) tableModel.getValueAt(selectedRow, 0));

            if (selectedUser != null) {
                // Tìm parent JFrame
                java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
                if (window instanceof javax.swing.JFrame) {
                    AddUserForm editForm = new AddUserForm(selectedUser);
                    editForm.setVisible(true);
                } else {
                    AddUserForm editForm = new AddUserForm(selectedUser);
                    editForm.setVisible(true);
                }
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
    }

    private void showUserList() {
        // Lấy dữ liệu từ DAO
        tableModel.setRowCount(0);
        this.UserArr = userDao.getUsers();

        for (User user : UserArr) {
            tableModel.addRow(new Object[] {
                    user.getIdUser(),
                    user.getNameUser(),
                    user.getLoaiUser(),
                    user.getCCCD(),
                    user.getPhoneUser(),
                    user.getEmailUser()
            });
        }
    }

    // Public method để refresh từ bên ngoài (từ AddUserForm)
    public void refreshTable() {
        showUserList();
    }

    // Main method để test standalone (tùy chọn)
    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame("Test UserManagement");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new UserManagement());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}