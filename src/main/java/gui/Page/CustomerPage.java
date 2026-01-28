package gui.Page;

import java.util.ArrayList;

import dao.CustomerDao;
import gui.Customer.AddCustomerForm;
import gui.utils.DialogHelper;
import model.Customer;

/**
 * Trang quản lý khách hàng.
 * 
 * Chức năng:
 * - Hiển thị danh sách khách hàng
 * - Thêm, sửa, xóa khách hàng
 * - Xem thông tin chi tiết và quản lý hộ sử dụng với khách hàng tương ứng
 * 
 * @author Lê Thanh Tùng
 * @version 2.0
 */

public class CustomerPage extends AbstractTablePage {

    static String[] columnNames = { "ID", "Tên Khách hàng", "Loại KH", "CCCD", "Số điện thoại", "Email" };

    private ArrayList<Customer> customerList = new ArrayList<>();

    public CustomerPage() {
        super(columnNames);
        showTableData(false);
    }

    @Override
    protected void handleAdd() {
        new AddCustomerForm();
    }

    @Override
    protected void handleEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            DialogHelper.showWarning(this, "Vui lòng chọn khách hàng cần sửa!");
            return;
        }

        Customer selectedCustomer = customerList.get(selectedRow);
        new AddCustomerForm(selectedCustomer); // Form sửa + quản lý hộ sử dụng
    }

    @Override
    protected void handleDelete() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            DialogHelper.showWarning(this, "Vui lòng chọn khách hàng cần xóa!");
            return;
        }

        boolean confirm = DialogHelper.showDeleteConfirm(this,
                "Bạn có chắc muốn xóa khách hàng này?",
                "Lưu ý: Sẽ xóa cả các hộ sử dụng liên quan!");

        if (confirm == true) {
            int customerId = (Integer) tableModel.getValueAt(selectedRow, 0);

            if (CustomerDao.deleteUserById(customerId)) {
                DialogHelper.showInfo(this, "Xóa khách hàng thành công!");
                showTableData(false); // Refresh lại bảng
            } else {
                DialogHelper.showError(this, "Xóa khách hàng thất bại!");
            }
        }

    }

    @Override
    protected void handleFilter() {
        showTableData(true);
    }

    @Override
    public void showTableData(boolean applyFilter) {
        // Xóa dữ liệu cũ
        tableModel.setRowCount(0);

        String search = "";
        if (!applyFilter) {
            this.customerList = CustomerDao.getCustomers(); 
        }else {
            search = tfSearch.getText().trim();
            // this.customerList = CustomerDao.getCustomers(search);  thêm filter sau
        }



        // Thêm từng dòng vào bảng
        for (Customer customer : this.customerList) {
            Object[] rowData = {
                    customer.getIdCustomer(),
                    customer.getNameCustomer(),
                    customer.getLoaiCustomer(),
                    customer.getCCCD(),
                    customer.getPhoneCustomer(),
                    customer.getEmail()
            };
            tableModel.addRow(rowData);
        }

    }

}
