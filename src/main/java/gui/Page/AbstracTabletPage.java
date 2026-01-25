package gui.Page;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import gui.GUIConstants;

/**
 * Abstract class chung cho các trang CRUD trong giao diện người dùng.
 * Các class con chỉ cần implement các phương thức abstract và override
 * 
 * Các chức năng mặc định:
 * - Hiển thị bảng với dữ liệu
 * - Nút Thêm, Sửa, Xóa
 * - Bộ lọc tìm kiếm theo ID
 * 
 * Các Override methods bắt buộc
 * 
 * @see #showTableData(boolean) Hiển thị dữ liệu bảng với/không áp dụng bộ lọc
 * @see #handleAdd() Xử lý sự kiện nút Thêm
 * @see #handleEdit() Xử lý sự kiện nút Sửa
 * @see #handleDelete() Xử lý sự kiện nút Xóa
 * 
 *      Các Override methods tùy chọn
 * @see #addCustomButtons() Thêm các nút tùy chỉnh vào buttonPanel
 * @see #addCustomFilters() Thêm các filters tùy chỉnh vào filterPanel
 * @see #attachCustomEvents() Gắn sự kiện tùy chỉnh
 * 
 * @author Lê Thanh Tùng
 * @version 1.0
 */

public abstract class AbstracTabletPage extends JPanel {

    // ===== TABLE =====
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected JScrollPane scrollPane;

    // ===== PANEL =====
    protected JPanel topPanel;
    protected JPanel buttonPanel;
    protected JPanel filterPanel;

    // ===== Default Buttons =====
    protected JButton btnAdd;
    protected JButton btnEdit;
    protected JButton btnDelete;
    protected JButton btnRefreshAndFilter;

    // ===== Filters =====
    protected JTextField tfSearchId;

    private int rowHeightTable = 30;

    /**
     * Constructor - Khởi tạo giao diện
     * 
     * @param columnNames   Tên các cột của bảng
     * @param addButtonText Text cho nút Thêm (vd: "Thêm Khách Hàng")
     * @since 1.0
     */
    public AbstracTabletPage(String[] columnNames, String addButtonText) {
        setLayout(new BorderLayout(5, 5));
        setBackground(GUIConstants.Colors.BACKGROUND);

        // Khởi tạo buttons
        initButtons(addButtonText);

        // Tạo giao diện
        initTopPanel();
        initTable(columnNames);

        // Gắn sự kiện
        attachDefaultEvents();
        attachCustomEvents();
    }

    /**
     * Constructor đơn giản - dùng text mặc định
     * 
     * @since 1.0
     */
    public AbstracTabletPage(String[] columnNames) {
        this(columnNames, "Thêm");
    }

    /**
     * Khởi tạo các buttons
     */
    private void initButtons(String addButtonText) {
        btnAdd = new JButton(addButtonText);
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnRefreshAndFilter = new JButton("Làm mới / Lọc");
    }

    /**
     * Khởi tạo panel chứa buttons và filters
     * 
     * @since 1.0
     */
    private void initTopPanel() {
        topPanel = new JPanel();
        topPanel.setBackground(GUIConstants.Colors.BACKGROUND);
        topPanel.setLayout(new BorderLayout(5, 5));

        // ===== HÀNG 1: Panel chứa các nút thao tác =====
        buttonPanel = new JPanel();
        buttonPanel.setBackground(GUIConstants.Colors.BACKGROUND);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Cho phép class con thêm nút tùy chỉnh vào buttonPanel
        addCustomButtons();

        // ===== HÀNG 2: Panel chứa bộ lọc =====
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(GUIConstants.Colors.BACKGROUND);

        // Khởi tạo components của filter
        tfSearchId = new JTextField(15);

        JLabel searchLabel = new JLabel("Tìm theo ID:");
        
        searchLabel.setForeground(Color.WHITE);
        filterPanel.add(btnRefreshAndFilter);
        filterPanel.add(searchLabel);
        filterPanel.add(tfSearchId);
        // Cho phép class con thêm filters tùy chỉnh
        addCustomFilters();
        
        // Thêm cả 2 panel vào topPanel
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout());
        containerPanel.setBackground(GUIConstants.Colors.BACKGROUND);
        containerPanel.add(buttonPanel, BorderLayout.NORTH);
        containerPanel.add(filterPanel, BorderLayout.CENTER);

        topPanel.add(containerPanel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Khởi tạo bảng
     */
    private void initTable(String[] columnNames) {
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(rowHeightTable);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Hiển thị danh sách Table với các bộ lọc tùy chọn.
     * 
     * @param applyFilter true = áp dụng bộ lọc, false = hiển thị tất cả
     */
    public abstract void showTableData(boolean applyFilter);

    /**
     * Gắn sự kiện cho các nút mặc định
     */
    private void attachDefaultEvents() {
        btnRefreshAndFilter.addActionListener(e -> handleRefreshAndFilter());
        btnAdd.addActionListener(e -> handleAdd());
        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());
        tfSearchId.addActionListener(e -> showTableData(true));
    }

    /***********************************************
     * ABSTRACT METHODS - Class con phải implement *
     ***********************************************/

    protected abstract void handleAdd();

    protected abstract void handleEdit();

    protected abstract void handleDelete();

    /**
     * Xử lý sự kiện nút Làm mới / Lọc
     */
    protected abstract void handleRefreshAndFilter();

    // ========================================
    // OPTIONAL METHODS - Class con có thể override
    // ========================================

    /**
     * Thêm các nút tùy chỉnh vào buttonPanel.
     * Override phương thức này để thêm nút của riêng bạn.
     */
    protected void addCustomButtons() {
        // Mặc định không làm gì
        // Class con override để thêm nút
        // dùng buttonPanel.add(...) để thêm nút
    }

    /**
     * Thêm các filters tùy chỉnh vào filterPanel.
     * Override phương thức này để thêm filter của riêng bạn.
     */
    protected void addCustomFilters() {
        // Mặc định không làm gì
        // Class con override để thêm filter
        // dùng filterPanel.add(...) để thêm filter
    }

    /**
     * Gắn sự kiện tùy chỉnh.
     * Override phương thức này để thêm sự kiện của riêng bạn.
     */
    protected void attachCustomEvents() {
        // Mặc định không làm gì
        // Class con override để thêm sự kiện
    }

    // ========================================
    // UTILITY METHODS
    // ========================================

    /**
     * Kiểm tra xem có dòng nào được chọn không
     * 
     * @return true nếu có dòng được chọn
     */
    protected boolean hasSelectedRow() {
        return table.getSelectedRow() != -1;
    }

    /**
     * Lấy index của dòng được chọn
     * 
     * @return index hoặc -1 nếu không có dòng nào được chọn
     */
    protected int getSelectedRow() {
        return table.getSelectedRow();
    }

    /**
     * Lấy ID từ dòng được chọn (giả sử cột 0 là ID)
     * 
     * @return ID hoặc -1 nếu không có dòng nào được chọn
     */
    protected int getSelectedId() {
        int selectedRow = getSelectedRow();
        if (selectedRow == -1) {
            return -1;
        }
        return (Integer) tableModel.getValueAt(selectedRow, 0);
    }

    /**
     * Lấy giá trị tại cột chỉ định của dòng được chọn
     * 
     * @param column index của cột
     * @return giá trị hoặc null nếu không có dòng nào được chọn
     */
    protected Object getSelectedValue(int column) {
        int selectedRow = getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }
        return tableModel.getValueAt(selectedRow, column);
    }

    /**
     * Thêm một dòng vào bảng
     * 
     * @param rowData dữ liệu dòng
     */
    protected void addRow(Object[] rowData) {
        tableModel.addRow(rowData);
    }

    /**
     * Cập nhật dòng tại vị trí chỉ định
     * 
     * @param row     index của dòng
     * @param rowData dữ liệu mới
     */
    protected void updateRow(int row, Object[] rowData) {
        for (int i = 0; i < rowData.length && i < tableModel.getColumnCount(); i++) {
            tableModel.setValueAt(rowData[i], row, i);
        }
    }

    /**
     * Xóa dòng tại vị trí chỉ định
     * 
     * @param row index của dòng
     */
    protected void removeRow(int row) {
        tableModel.removeRow(row);
    }

    /**
     * Lấy giá trị tìm kiếm theo ID
     * 
     * @return Giá trị trong ô tìm kiếm ID
     */
    protected String getSearchIdValue() {
        return tfSearchId.getText().trim();
    }

    /**
     * Xóa các filter về giá trị mặc định
     */
    protected void clearFilters() {
        tfSearchId.setText("");
    }

    // ========================================
    // GETTERS & SETTERS
    // ========================================

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public JPanel getFilterPanel() {
        return filterPanel;
    }

    public int getRowHeightTable() {
        return rowHeightTable;
    }

    public void setRowHeightTable(int rowHeightTable) {
        this.rowHeightTable = rowHeightTable;
        if (table != null) {
            table.setRowHeight(rowHeightTable);
        }
    }
}