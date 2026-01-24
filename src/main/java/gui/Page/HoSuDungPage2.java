package gui.Page;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import gui.Customer.AddHoSuDungForm;
import model.Customer;
import model.HoSuDung;
import model.LoaiCustomer;
import gui.GUIConstants;

import static gui.utils.DialogHelper.*;
import static dao.CustomerDao.customerDao;
import static dao.HoSuDungDao.hsdDao;

import javax.swing.*;

import data.KhuVucLoader;

public class HoSuDungPage2 extends AbstractPage {

    static String[] columnNames = {
            "Mã HSD",
            "ID Khách hàng",
            "Tên Khách hàng",
            "Khu vực",
            "Địa Chỉ",
            "Trạng thái"
    };

    // ===== Data =====
    private ArrayList<HoSuDung> hoSuDungList = new ArrayList<>();

    // ===== FILTERS =====
    private JComboBox<String> cbKhuVuc = new JComboBox<>();
    private JComboBox<LoaiCustomer> cbCustomerType = new JComboBox<>();

    public HoSuDungPage2() {
        super(columnNames, "Thêm Hộ Sử Dụng");
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
                Customer customer = customerDao.getCustomerById(customerId);
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

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa hộ sử dụng này?\nLưu ý: Sẽ xóa cả các dữ liệu liên quan!",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
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
    protected void addCustomFilters(){
        // Label và combobox loại khách hàng
        JLabel lblLoaiKH = new JLabel("Loại KH:");
        lblLoaiKH.setForeground(Color.WHITE); // Đổi màu chữ thành trắng
        filterPanel.add(lblLoaiKH);
        cbCustomerType.setPreferredSize(new Dimension(150, 25));
        filterPanel.add(cbCustomerType);

        // Label và combobox khu vực
        JLabel lblKhuVuc = new JLabel("Khu vực:");
        lblKhuVuc.setForeground(Color.WHITE); // Đổi màu chữ thành trắng
        filterPanel.add(lblKhuVuc);
        cbKhuVuc.setPreferredSize(new Dimension(150, 25));
        filterPanel.add(cbKhuVuc);
        loadFilterData();
    }

        /**
     * Load dữ liệu cho các filter (ComboBox)
     */
    private void loadFilterData() {
        // Load loại khách hàng
        cbCustomerType.removeAllItems();
        cbCustomerType.addItem(new AllCustomerTypes()); // Thêm option "Tất cả"
        List<LoaiCustomer> loaiKHList = customerDao.getLoaiKhachHang();
        for (LoaiCustomer lkh : loaiKHList) {
            cbCustomerType.addItem(lkh);
        }

        // Load khu vực
        new KhuVucLoader().loadKhuVuc(cbKhuVuc);
    }

    private static class AllCustomerTypes extends LoaiCustomer {
        public AllCustomerTypes() {
            super(0, "Tất cả loại KH");
        }
    }


    /**
     * Hiển thị danh sách hộ sử dụng với các bộ lọc tùy chọn.
     * 
     * @param applyFilter true = áp dụng bộ lọc, false = hiển thị tất cả
     */
    public void showTableData(boolean applyFilter) {
        tableModel.setRowCount(0);
        hoSuDungList.clear();

        try {
            // Lấy giá trị filter (chỉ dùng khi applyFilter = true)
            LoaiCustomer selectedType = applyFilter ? (LoaiCustomer) cbCustomerType.getSelectedItem() : null;
            String selectedKhuVuc = applyFilter ? (String) cbKhuVuc.getSelectedItem() : null;
            String searchId = applyFilter ? tfSearchId.getText().trim() : "";

            // Lấy tất cả khách hàng
            ArrayList<Customer> allCustomers = customerDao.getCustomers();

            for (Customer customer : allCustomers) {
                // Lọc theo loại khách hàng (chỉ khi applyFilter = true)
                if (applyFilter && selectedType.getIdLoaiCustomer() != 0
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
                ArrayList<HoSuDung> hsdList = hsdDao.getHoSuDungByCustomerId(customer.getIdCustomer());

                for (HoSuDung hsd : hsdList) {
                    // Lọc theo khu vực (chỉ khi applyFilter = true)
                    if (applyFilter && selectedKhuVuc != null &&
                            !selectedKhuVuc.equals("-- Chọn tỉnh ---")) {
                        if (!hsd.getKhuVuc().equals(selectedKhuVuc)) {
                            continue;
                        }
                    }

                    hsd.setID_Customer(customer.getIdCustomer());

                    String trangThai = hsd.getTrangThai() == 1 ? "Đang sử dụng" : "Ngừng sử dụng";

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
            showError("Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }

}
