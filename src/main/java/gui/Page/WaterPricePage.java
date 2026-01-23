/**
 * WaterPricePage.java
 * 
 * Trang quản lý giá nước trong ứng dụng quản lý hệ thống nước.
 * Trang này cung cấp giao diện để xem, thêm, sửa và xóa thông tin giá nước.
 * 
 * Chức năng chính:
 * - Hiển thị danh sách giá nước trong bảng (JTable)
 * - Thêm giá nước, bậc giá nước mới
 * - Sửa thông tin giá nước đã có
 * - Xóa giá nước (có xác nhận trước khi xóa)
 * - Làm mới danh sách
 * 
 * Các thành phần giao diện:
 * - Bảng hiển thị: Id Đơn giá, Loại khách hàng, Khu vực, Thuế
 * - Các nút chức năng: Thêm, Sửa, Xóa, Làm mới
 *
 */

package gui.Page;

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
import gui.GiaNuoc.AddWaterPriceForm;
import dao.GiaNuocDao;
import model.GiaNuoc;

public class WaterPricePage extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private JButton btnAdd = new JButton("Thêm giá nước");
    private JButton btnEdit = new JButton("Sửa giá nước");
    private JButton btnDelete = new JButton("Xóa giá nước");
    private JButton btnRefresh = new JButton("Làm mới");

    private ArrayList<GiaNuoc> gnArr = new ArrayList<>();
    private GiaNuocDao giaNuocDao = new GiaNuocDao();

    public WaterPricePage() {
        setLayout(new BorderLayout(5, 5));
        setBackground(GUIConstants.Colors.BACKGROUND); // Thêm màu nền cho phù hợp với MenuForm

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(GUIConstants.Colors.BACKGROUND); // Màu nền

        topPanel.add(btnRefresh);
        topPanel.add(btnAdd);
        topPanel.add(btnEdit);
        topPanel.add(btnDelete);

        String[] columnNames = { "Id Đơn giá", "Loại khách hàng", "Khu vực", "Thue" };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(table);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // showGiaNuocList();

        btnRefresh.addActionListener(e -> showWaterPriceList());
        btnAdd.addActionListener(e -> {
            new AddWaterPriceForm().setVisible(true);

        });

        btnEdit.addActionListener(e -> {
            new AddWaterPriceForm(gnArr.get(table.getSelectedRow())).setVisible(true);

        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn hàng để xóa",
                        "No User Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa giá nước này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                String idDelete = tableModel.getValueAt(selectedRow, 0).toString();
                if (giaNuocDao.deleteGiaNuocById(Integer.parseInt(idDelete))) {
                    JOptionPane.showMessageDialog(this,
                            "Xóa giá nước thành công!",
                            "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);
                    showWaterPriceList();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Xóa giá nước thất bại!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        });

    }

    public void showWaterPriceList() {
        tableModel.setRowCount(0);

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
