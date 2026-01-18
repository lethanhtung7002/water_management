package gui.Customer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import gui.GUIConstants;

import dao.*;
import model.*;

/**
 * Trang quản lý Hộ sửu dụng.
 * 
 * Chức năng:
 * - Hiển thị danh sách toàn bộ của Hộ sử dụng dựa trên thông tin cần lọc (loại
 * khách hàng, Khu vực)
 * - Thêm Hộ sử dụng dựa trên id khách hàng nhập vào
 * - Sửa, xóa Hộ sử dụng
 * - Xem thông tin chi tiết, số nước sử dụng, tính hóa đơn
 * 
 * @author Lê Thanh Tùng
 * @version 0.0
 */

public class HoSuDungPage extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private JPanel topPanel = new JPanel();

    private JButton btnAdd = new JButton("Thêm");
    private JButton btnEdit = new JButton("Sửa Thông Tin");
    private JButton btnDelete = new JButton("Xóa");
    private JButton btnRefresh = new JButton("Làm mới và lọc");

    // Thông tin cần lọc
    private JComboBox<String> cbKhuVuc = new JComboBox<>();
    private JComboBox<String> cbCustomerType = new JComboBox<>();

    private ArrayList<hoSuDung> hsdArr = new ArrayList<>();
    private HoSuDungDao hsdDao = new HoSuDungDao();

    public HoSuDungPage() {
        setLayout(new BorderLayout(5, 5));
        setBackground(GUIConstants.Colors.BACKGROUND);

        // Panel chứa các nút
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(GUIConstants.Colors.BACKGROUND);

        JPanel panelBtn = new JPanel();
        panelBtn.add(btnAdd);
        panelBtn.add(btnEdit);
        panelBtn.add(btnDelete);
        JPanel panelLoc = new JPanel();
        panelLoc.add(cbCustomerType);
        panelLoc.add(cbKhuVuc);
        panelLoc.add(btnRefresh);

        topPanel.add(panelBtn);
        topPanel.add(panelLoc);

        // Tạo bảng
        String[] columnNames = { "Mã hộ sử dụng", "Tên Khách hàng", "Địa Chỉ", "Trạng thái" };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(table);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
