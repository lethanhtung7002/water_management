package gui.Page;

import dao.CustomerDao;
import static dao.HoSuDungDao.hsdDao;
import static gui.utils.DialogHelper.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import data.KhuVucLoader;
import gui.Customer.AddHoSuDungForm;
import gui.HoSuDung.ChiSoVaThanhToan;
import gui.utils.DialogHelper;
import model.Customer;
import model.HoSuDung;
import model.LoaiCustomer;

/**
 * Trang quản lý Hộ sử dụng.
 * 
 * Chức năng:
 * - Hiển thị danh sách toàn bộ hộ sử dụng
 * - Lọc theo loại khách hàng và khu vực
 * - Thêm hộ sử dụng dựa trên ID khách hàng
 * - Sửa, xóa hộ sử dụng
 * - Xem thông tin chi tiết hộ sử dụng
 * 
 * @author Lê Thanh Tùng
 * @version 2.1 - Fixed NullPointerException
 */
public class HoSuDungPage extends AbstractTablePage {

    static String[] columNameHSD = {
            "Mã HSD",
            "ID Khách hàng",
            "Tên Khách hàng",
            "Khu vực",
            "Địa Chỉ",
            "Trạng thái"
    };

    // ===== Data =====
    private ArrayList<HoSuDung> hoSuDungList = new ArrayList<>();

    // ===== Custom Filters - PHẢI PRIVATE =====
    private JComboBox<LoaiCustomer> cbCustomerType;
    private JComboBox<String> cbKhuVuc;

    // ==== Customs buttons =====
    private JButton btnChiSo_HoaDon;

    public HoSuDungPage() {
        super(columNameHSD);
        showTableData(false);
    }

    @Override
    protected void handleRefreshAndFilter() {
        showTableData(true);
    }

    @Override
    protected void handleAdd() {
        String input = JOptionPane.showInputDialog(
                this,
                "Nhập ID Khách hàng:",
                "Thêm Hộ Sử Dụng",
                JOptionPane.PLAIN_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            try {
                int customerId = Integer.parseInt(input.trim());

                // Kiểm tra khách hàng có tồn tại không
                Customer customer = CustomerDao.getCustomerById(customerId);
                if (customer != null) {
                    new AddHoSuDungForm(customerId);
                } else {
                    showError("Không tìm thấy khách hàng với ID: " + customerId);
                }
            } catch (NumberFormatException ex) {
                showError("ID khách hàng không hợp lệ!");
            }
        }
    }

    @Override
    protected void handleEdit() {
        int selectedRow = getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Vui lòng chọn một hộ sử dụng để sửa.");
            return;
        }

        HoSuDung hsdSelected = hoSuDungList.get(selectedRow);
        new AddHoSuDungForm(hsdSelected);
    }

    @Override
    protected void handleDelete() {
        int selectedRow = getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Vui lòng chọn một hộ sử dụng để xóa.");
            return;
        }

        boolean confirm = DialogHelper.showDeleteConfirm(
                this,
                "Bạn có chắc muốn xóa hộ sử dụng này?",
                "Lưu ý: Hành động này không thể hoàn tác!");

        if (confirm == true) {
            HoSuDung selected = hoSuDungList.get(selectedRow);

            if (hsdDao.delete_HoSuDung(selected.getID_HoSuDung())) {
                showSuccess("Xóa hộ sử dụng thành công!");
                showTableData(false); // Refresh lại bảng
            } else {
                showError("Xóa hộ sử dụng thất bại!");
            }
        }
    }

    @Override
    protected void addCustomFilters() {
        // Khởi tạo ComboBox
        cbCustomerType = new JComboBox<>();
        cbKhuVuc = new JComboBox<>();

        // Load loại khách hàng
        loadCustomerTypes();

        // Load khu vực
        new KhuVucLoader().loadKhuVuc(cbKhuVuc);

        // Thêm vào filterPanel
        JLabel lblLoaiKH = new JLabel("Loại KH:");
        lblLoaiKH.setForeground(Color.WHITE);
        filterPanel.add(lblLoaiKH);
        filterPanel.add(cbCustomerType);

        JLabel lblKhuVuc = new JLabel("Khu vực:");
        lblKhuVuc.setForeground(Color.WHITE);
        filterPanel.add(lblKhuVuc);
        filterPanel.add(cbKhuVuc);
    }

    /**
     * Load danh sách loại khách hàng vào ComboBox
     */
    private void loadCustomerTypes() {
        cbCustomerType.removeAllItems();
        cbCustomerType.addItem(new AllCustomerTypes());

        List<LoaiCustomer> loaiCustomers = CustomerDao.getLoaiKhachHang();
        for (LoaiCustomer lc : loaiCustomers) {
            cbCustomerType.addItem(lc);
        }
    }

    /**
     * Class đại diện cho option "Tất cả loại KH"
     */
    private static class AllCustomerTypes extends LoaiCustomer {
        public AllCustomerTypes() {
            super(0, "Tất cả loại KH");
        }
    }

    @Override
    protected void addCustomButtons() {
        btnChiSo_HoaDon = new JButton("Chỉ số nước và Hóa đơn");
        buttonPanel.add(btnChiSo_HoaDon);
    }

    @Override
    protected void attachCustomEvents() {

        btnChiSo_HoaDon.addActionListener(e -> {
            int selectedRow = getSelectedRow();

            if (selectedRow == -1) {
                showWarning("Vui lòng chọn hộ sử dụng!");
                return;
            }

            HoSuDung selected = hoSuDungList.get(selectedRow);
            new ChiSoVaThanhToan(selected);
        });
    }

    @Override
    public void showTableData(boolean applyFilter) {
        tableModel.setRowCount(0);
        hoSuDungList.clear();

        try {
            LoaiCustomer selectedType = null;
            String selectedKhuVuc = null;
            String searchId = "";

            if (applyFilter) {
                selectedType = (LoaiCustomer) cbCustomerType.getSelectedItem();
                selectedKhuVuc = (String) cbKhuVuc.getSelectedItem();
                searchId = tfSearchId.getText().trim();

                if (selectedType == null) {
                    selectedType = new AllCustomerTypes();
                }
            }

            // Lấy tất cả khách hàng
            ArrayList<Customer> allCustomers = CustomerDao.getCustomers();

            for (Customer customer : allCustomers) {
                // Lọc theo loại khách hàng (chỉ khi applyFilter = true)
                if (applyFilter && selectedType != null
                        && selectedType.getIdLoaiCustomer() != 0
                        && customer.getLoaiCustomer() != selectedType.getIdLoaiCustomer()) {
                    continue;
                }

                // Lọc theo ID khách hàng (chỉ khi applyFilter = true)
                if (applyFilter && !searchId.isEmpty()) {
                    try {
                        int searchIdInt = Integer.parseInt(searchId);
                        if (customer.getIdCustomer() != searchIdInt) {
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        // Bỏ qua nếu không phải số
                    }
                }

                // Lấy danh sách hộ sử dụng của khách hàng
                ArrayList<HoSuDung> hsdList = hsdDao.getHoSuDungByCustomerId(
                        customer.getIdCustomer());

                for (HoSuDung hsd : hsdList) {
                    // Lọc theo khu vực (chỉ khi applyFilter = true)
                    if (applyFilter && selectedKhuVuc != null
                            && !selectedKhuVuc.equals("-- Chọn tỉnh ---")
                            && !hsd.getKhuVuc().equals(selectedKhuVuc)) {
                        continue;
                    }

                    hsd.setID_Customer(customer.getIdCustomer());

                    String trangThai = hsd.getTrangThai() == 1
                            ? "Đang sử dụng"
                            : "Ngừng sử dụng";

                    Object[] row = {
                            hsd.getID_HoSuDung(),
                            customer.getIdCustomer(),
                            customer.getNameCustomer(),
                            hsd.getKhuVuc(),
                            hsd.getDiaChi(),
                            trangThai
                    };

                    tableModel.addRow(row);
                    hoSuDungList.add(hsd);
                }
            }

        } catch (Exception e) {
            System.out.println("Lỗi khi tải/lọc hộ sử dụng: " + e.getMessage());
            e.printStackTrace(); // ✅ Thêm stack trace để debug
            showError("Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }
}