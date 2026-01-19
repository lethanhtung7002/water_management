package gui.Customer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import gui.GUIConstants;

import dao.CustomerDao;
import model.Customer;

/**
 * Trang quản lý khách hàng.
 * 
 * Chức năng:
 * - Hiển thị danh sách khách hàng
 * - Thêm, sửa, xóa khách hàng
 * - Xem thông tin chi tiết và quản lý hộ sử dụng với khách hàng tương ứng
 * 
 */
public class CustomerPage extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private JButton btnAdd = new JButton("Thêm Khách Hàng");
    private JButton btnEdit = new JButton("Sửa Thông Tin");
    private JButton btnDelete = new JButton("Xóa Khách Hàng");
    private JButton btnRefresh = new JButton("Làm mới");

    private ArrayList<Customer> customerList = new ArrayList<>();
    private CustomerDao customerDao = new CustomerDao();

    public CustomerPage() {
        setLayout(new BorderLayout(5, 5));
        setBackground(GUIConstants.Colors.BACKGROUND);

        // Panel chứa các nút
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(GUIConstants.Colors.BACKGROUND);

        topPanel.add(btnRefresh);
        topPanel.add(btnAdd);
        topPanel.add(btnEdit);
        topPanel.add(btnDelete);

        // Tạo bảng
        String[] columnNames = { "ID", "Tên Khách hàng", "Loại KH", "CCCD", "Số điện thoại", "Email" };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(table);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load dữ liệu
        showCustomerList();

        // ===== SỰ KIỆN BUTTONS =====

        // Refresh - Làm mới danh sách
        btnRefresh.addActionListener(e -> showCustomerList());

        // Add - Thêm khách hàng mới
        btnAdd.addActionListener(e -> {
            new AddCustomerForm(); // Form thêm mới
        });

        // Edit - Sửa thông tin khách hàng
        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn khách hàng cần sửa!",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Lấy ID từ bảng
            int customerId = (Integer) tableModel.getValueAt(selectedRow, 0);

            // Lấy thông tin chi tiết từ database
            Customer selectedCustomer = customerDao.getCustomerById(customerId);

            if (selectedCustomer != null) {
                new AddCustomerForm(selectedCustomer); // Form sửa + quản lý hộ sử dụng
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin khách hàng!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Delete - Xóa khách hàng
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn khách hàng cần xóa!",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Xác nhận xóa
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa khách hàng này?\nLưu ý: Sẽ xóa cả các hộ sử dụng liên quan!",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                int customerId = (Integer) tableModel.getValueAt(selectedRow, 0);

                if (customerDao.deleteUserById(customerId)) {
                    JOptionPane.showMessageDialog(this,
                            "Xóa khách hàng thành công!",
                            "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);
                    showCustomerList(); // Refresh lại bảng
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Xóa khách hàng thất bại!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    /**
     * Hiển thị danh sách khách hàng lên bảng.
     */
    public void showCustomerList() {
        // Xóa dữ liệu cũ
        tableModel.setRowCount(0);

        // Lấy dữ liệu từ DAO
        this.customerList = customerDao.getCustomers();

        // Thêm từng dòng vào bảng
        for (Customer customer : customerList) {
            tableModel.addRow(new Object[] {
                    customer.getIdCustomer(),
                    customer.getNameCustomer(),
                    customer.getLoaiCustomer(),
                    customer.getCCCD(),
                    customer.getPhoneCustomer(),
                    customer.getEmail()
            });
        }
    }
}