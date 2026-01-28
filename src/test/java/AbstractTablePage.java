
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import gui.GUIConstants;

/**
 * Abstract class chung cho các trang CRUD trong giao diện người dùng.
 * Các class con chỉ cần implement các phương thức abstract và override
 * 
 * Các chức năng mặc định:
 * - Hiển thị bảng với dữ liệu
 * - Nút Thêm, Sửa, Xóa, Làm mới, Lọc
 * - Bộ lọc tìm kiếm theo ID
 * - Pagination với input trực tiếp và chọn số hàng/trang
 * 
 * Các Override methods bắt buộc
 * 
 * @see #loadTableData() Load dữ liệu từ database với pagination và filter
 * @see #handleAdd() Xử lý sự kiện nút Thêm
 * @see #handleEdit() Xử lý sự kiện nút Sửa
 * @see #handleDelete() Xử lý sự kiện nút Xóa
 * 
 *      Các Override methods tùy chọn
 * @see #addCustomButtons() Thêm các nút tùy chỉnh vào buttonPanel
 * @see #addCustomFilters() Thêm các filters tùy chỉnh vào filterPanel
 * @see #attachCustomEvents() Gắn sự kiện tùy chỉnh
 * @see #hasActiveFilters() Kiểm tra có filter nào đang được áp dụng
 * 
 * @author Lê Thanh Tùng
 * @version 2.0
 */
public abstract class AbstractTablePage extends JPanel {

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
    protected JButton btnFilter;
    protected JButton btnRefresh;

    // ===== Filters =====
    protected JTextField tfSearch;

    // ===== Pagination bar =====
    protected JPanel paginationPanel;
    protected JTextField tfCurrentPage;
    protected JLabel lblTotalPages;
    protected JLabel lblTotalRecords;
    protected JComboBox<Integer> cbItemsPerPage;
    protected JButton btnPrevPage;
    protected JButton btnNextPage;
    protected JButton btnFirstPage;
    protected JButton btnLastPage;

    // ===== Pagination State =====
    protected int currentPage = 1;
    protected int totalPages = 1;
    protected int totalRecords = 0;
    protected int itemsPerPage = 100;

    // ===== UI Settings =====
    protected int rowHeightTable = 30;
    protected int idColumnIndex = 0; // Cột ID mặc định là cột 0

    // ===== Debounce Timer cho auto-filter =====
    protected Timer autoFilterTimer;
    protected boolean enableAutoFilter = false; // Tắt mặc định

    /**
     * Constructor - Khởi tạo giao diện
     * 
     * @param columnTableNames Tên các cột của bảng
     * @since 2.0
     */
    public AbstractTablePage(String[] columnTableNames) {
        initBase();
        initUI(columnTableNames);
        initEvents();
    }

    protected void initBase() {
        setLayout(new BorderLayout(5, 5));
        setBackground(GUIConstants.Colors.BACKGROUND);
    }

    protected void initUI(String[] columnTableNames) {
        initButtons();
        initTopPanel();
        initTable(columnTableNames);
        initPaginationBar();

        table.setRowHeight(rowHeightTable);
    }

    protected void initEvents() {
        attachDefaultEvents();
        attachPaginationEvents();
        attachCustomEvents();
    }

    /**
     * Khởi tạo các buttons
     */
    private void initButtons() {
        btnRefresh = new JButton("🔄 Làm mới");
        btnAdd = new JButton("➕ Thêm");
        btnEdit = new JButton("✏️ Sửa");
        btnDelete = new JButton("🗑️ Xóa");
        btnFilter = new JButton("🔍 Lọc");

        btnFirstPage = new JButton("|<<");
        btnPrevPage = new JButton("<<");
        btnNextPage = new JButton(">>");
        btnLastPage = new JButton(">>|");

        // Tooltips
        btnRefresh.setToolTipText("Làm mới và xóa bộ lọc");
        btnFilter.setToolTipText("Áp dụng bộ lọc");
        btnAdd.setToolTipText("Thêm bản ghi mới");
        btnEdit.setToolTipText("Sửa bản ghi đã chọn");
        btnDelete.setToolTipText("Xóa bản ghi đã chọn");
    }

    /**
     * Khởi tạo panel chứa buttons và filters
     * 
     * @since 2.0
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
        buttonPanel.add(btnRefresh);

        // Cho phép class con thêm nút tùy chỉnh vào buttonPanel
        addCustomButtons();

        // ===== HÀNG 2: Panel chứa bộ lọc =====
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(GUIConstants.Colors.BACKGROUND);

        // Khởi tạo components của filter
        tfSearch = new JTextField(15);
        tfSearch.setToolTipText("Nhập ID hoặc từ khóa tìm kiếm");

        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchLabel.setForeground(Color.WHITE);

        filterPanel.add(btnFilter);
        filterPanel.add(searchLabel);
        filterPanel.add(tfSearch);

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
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Khởi tạo pagination bar với đầy đủ tính năng
     */
    private void initPaginationBar() {
        paginationPanel = new JPanel();
        paginationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        paginationPanel.setBackground(GUIConstants.Colors.BACKGROUND);
        paginationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // Navigation buttons
        btnFirstPage.setToolTipText("Trang đầu");
        btnPrevPage.setToolTipText("Trang trước");
        btnNextPage.setToolTipText("Trang sau");
        btnLastPage.setToolTipText("Trang cuối");

        // Page input
        tfCurrentPage = new JTextField(4);
        tfCurrentPage.setHorizontalAlignment(JTextField.CENTER);
        tfCurrentPage.setText("1");
        tfCurrentPage.setToolTipText("Nhập số trang và nhấn Enter");

        // Total pages label (read-only)
        lblTotalPages = new JLabel("1");
        lblTotalPages.setForeground(Color.WHITE);

        // Total records label
        lblTotalRecords = new JLabel("Tổng: 0 bản ghi");
        lblTotalRecords.setForeground(Color.WHITE);

        // Items per page combo
        cbItemsPerPage = new JComboBox<>(new Integer[] {
                20, 50, 100, 200, 500, 1000
        });
        cbItemsPerPage.setSelectedItem(100);
        cbItemsPerPage.setToolTipText("Chọn số dòng hiển thị mỗi trang");

        // Layout
        paginationPanel.add(btnFirstPage);
        paginationPanel.add(btnPrevPage);

        JLabel lblPage = new JLabel("Trang:");
        lblPage.setForeground(Color.WHITE);
        paginationPanel.add(lblPage);
        paginationPanel.add(tfCurrentPage);

        JLabel lblOf = new JLabel("/");
        lblOf.setForeground(Color.WHITE);
        paginationPanel.add(lblOf);
        paginationPanel.add(lblTotalPages);

        paginationPanel.add(btnNextPage);
        paginationPanel.add(btnLastPage);

        // Separator
        JLabel separator1 = new JLabel("  |  ");
        separator1.setForeground(Color.WHITE);
        paginationPanel.add(separator1);

        JLabel lblItemsPerPage = new JLabel("Hiển thị:");
        lblItemsPerPage.setForeground(Color.WHITE);
        paginationPanel.add(lblItemsPerPage);
        paginationPanel.add(cbItemsPerPage);

        JLabel lblRows = new JLabel("dòng/trang");
        lblRows.setForeground(Color.WHITE);
        paginationPanel.add(lblRows);

        // Separator
        JLabel separator2 = new JLabel("  |  ");
        separator2.setForeground(Color.WHITE);
        paginationPanel.add(separator2);

        paginationPanel.add(lblTotalRecords);

        add(paginationPanel, BorderLayout.SOUTH);
    }

    /**
     * Gắn sự kiện cho các nút mặc định
     */
    private void attachDefaultEvents() {
        // Refresh button - Xóa filter và load lại trang 1
        btnRefresh.addActionListener(e -> handleRefresh());

        // Filter button - Áp dụng filter và reset về trang 1
        btnFilter.addActionListener(e -> handleFilter());

        // CRUD buttons với validation
        btnAdd.addActionListener(e -> handleAdd());

        btnEdit.addActionListener(e -> {
            if (!hasSelectedRow()) {
                showWarning("Vui lòng chọn dòng cần sửa!");
                return;
            }
            handleEdit();
        });

        btnDelete.addActionListener(e -> {
            if (!hasSelectedRow()) {
                showWarning("Vui lòng chọn dòng cần xóa!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn xóa bản ghi này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                handleDelete();
            }
        });

        // Enter ở search box = filter
        tfSearch.addActionListener(e -> handleFilter());

        // Auto-filter khi gõ (nếu enable)
        if (enableAutoFilter) {
            tfSearch.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    scheduleAutoFilter();
                }

                public void removeUpdate(DocumentEvent e) {
                    scheduleAutoFilter();
                }

                public void insertUpdate(DocumentEvent e) {
                    scheduleAutoFilter();
                }
            });
        }
    }

    /**
     * Gắn sự kiện cho pagination
     */
    private void attachPaginationEvents() {
        // Navigation buttons
        btnFirstPage.addActionListener(e -> goToPage(1));
        btnPrevPage.addActionListener(e -> goToPage(currentPage - 1));
        btnNextPage.addActionListener(e -> goToPage(currentPage + 1));
        btnLastPage.addActionListener(e -> goToPage(totalPages));

        // Direct page input - Enter để chuyển trang
        tfCurrentPage.addActionListener(e -> {
            try {
                int page = Integer.parseInt(tfCurrentPage.getText().trim());
                goToPage(page);
            } catch (NumberFormatException ex) {
                tfCurrentPage.setText(String.valueOf(currentPage));
                showWarning("Vui lòng nhập số trang hợp lệ!");
            }
        });

        // Focus lost = validate và restore nếu invalid
        tfCurrentPage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    int page = Integer.parseInt(tfCurrentPage.getText().trim());
                    if (page < 1 || page > totalPages) {
                        tfCurrentPage.setText(String.valueOf(currentPage));
                    }
                } catch (NumberFormatException ex) {
                    tfCurrentPage.setText(String.valueOf(currentPage));
                }
            }
        });

        // Items per page changed - Reset về trang 1
        cbItemsPerPage.addActionListener(e -> {
            Integer selected = (Integer) cbItemsPerPage.getSelectedItem();
            if (selected != null && selected != itemsPerPage) {
                itemsPerPage = selected;
                currentPage = 1; // Reset về trang 1
                loadTableData();
            }
        });
    }

    /***********************************************
     * ABSTRACT METHODS - Class con phải implement *
     ***********************************************/

    /**
     * Load dữ liệu từ database với pagination và filter hiện tại.
     * Class con phải implement để query database và gọi updatePaginationUI().
     * 
     * @implNote Sử dụng currentPage, itemsPerPage, và hasActiveFilters()
     * @see #updatePaginationUI(int, int, int)
     */
    protected abstract void loadTableData();

    protected abstract void handleAdd();

    protected abstract void handleEdit();

    protected abstract void handleDelete();

    /***********************************************
     * OPTIONAL METHODS - Class con có thể override *
     ***********************************************/

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

    /**
     * Kiểm tra có filter nào đang được áp dụng không.
     * Override để check thêm các custom filters.
     * 
     * @return true nếu có filter đang active
     */
    protected boolean hasActiveFilters() {
        return !getSearchValue().isEmpty();
        // Class con override để check thêm filters khác
    }

    /***********************************************
     * PAGINATION METHODS *
     ***********************************************/

    /**
     * Navigate đến trang cụ thể với validation
     * 
     * @param page Số trang cần chuyển đến
     */
    protected void goToPage(int page) {
        if (page < 1 || page > totalPages || page == currentPage) {
            return; // Invalid hoặc đang ở trang đó rồi
        }

        currentPage = page;
        loadTableData();
    }

    /**
     * Xử lý sự kiện nút Refresh - Xóa filter và load lại
     */
    protected void handleRefresh() {
        clearFilters();
        currentPage = 1;
        loadTableData();
    }

    /**
     * Xử lý sự kiện nút Filter - Áp dụng filter và reset về trang 1
     */
    protected void handleFilter() {
        currentPage = 1; // Reset về trang 1 khi filter
        loadTableData();
    }

    /**
     * Schedule auto-filter với debounce (500ms)
     */
    private void scheduleAutoFilter() {
        if (autoFilterTimer != null) {
            autoFilterTimer.stop();
        }
        autoFilterTimer = new Timer(500, e -> handleFilter());
        autoFilterTimer.setRepeats(false);
        autoFilterTimer.start();
    }

    /**
     * Update pagination UI sau khi load data.
     * Gọi method này trong loadTableData() sau khi query xong.
     * 
     * @param current      Trang hiện tại
     * @param total        Tổng số trang
     * @param totalRecords Tổng số bản ghi
     */
    protected void updatePaginationUI(int current, int total, int totalRecords) {
        this.currentPage = current;
        this.totalPages = Math.max(1, total);
        this.totalRecords = totalRecords;

        // Update text fields
        tfCurrentPage.setText(String.valueOf(current));
        lblTotalPages.setText(String.valueOf(this.totalPages));

        // Update total records label
        int from = totalRecords > 0 ? (current - 1) * itemsPerPage + 1 : 0;
        int to = Math.min(current * itemsPerPage, totalRecords);
        lblTotalRecords.setText(String.format("Tổng: %d bản ghi (hiển thị %d-%d)",
                totalRecords, from, to));

        // Enable/disable navigation buttons
        btnFirstPage.setEnabled(current > 1);
        btnPrevPage.setEnabled(current > 1);
        btnNextPage.setEnabled(current < this.totalPages);
        btnLastPage.setEnabled(current < this.totalPages);

        // Disable tất cả pagination nếu không có data
        boolean hasData = totalRecords > 0;
        btnFirstPage.setEnabled(hasData && current > 1);
        btnPrevPage.setEnabled(hasData && current > 1);
        btnNextPage.setEnabled(hasData && current < this.totalPages);
        btnLastPage.setEnabled(hasData && current < this.totalPages);
        tfCurrentPage.setEnabled(hasData);
        cbItemsPerPage.setEnabled(hasData);
    }

    /**
     * @deprecated Sử dụng loadTableData() thay thế
     */
    @Deprecated
    public void showTableData(boolean applyFilter) {
        loadTableData();
    }

    /***********************************************
     * UTILITY METHODS *
     ***********************************************/

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
     * Lấy ID từ dòng được chọn
     * 
     * @return ID hoặc -1 nếu không có dòng nào được chọn
     */
    protected int getSelectedId() {
        int selectedRow = getSelectedRow();
        if (selectedRow == -1) {
            return -1;
        }
        try {
            Object value = tableModel.getValueAt(selectedRow, idColumnIndex);
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            if (value instanceof String) {
                return Integer.parseInt((String) value);
            }
        } catch (Exception e) {
            // Ignore parsing errors
        }
        return -1;
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
     * Xóa toàn bộ dữ liệu trong bảng
     */
    protected void clearTable() {
        tableModel.setRowCount(0);
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
     * Lấy giá trị tìm kiếm
     * 
     * @return Giá trị trong ô tìm kiếm
     */
    protected String getSearchValue() {
        return tfSearch.getText().trim();
    }

    /**
     * Xóa các filter về giá trị mặc định
     */
    protected void clearFilters() {
        tfSearch.setText("");
        // Class con override để clear thêm custom filters
    }

    /**
     * Hiển thị thông báo warning
     */
    protected void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Hiển thị thông báo error
     */
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Hiển thị thông báo success
     */
    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /***********************************************
     * GETTERS & SETTERS *
     ***********************************************/

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
        cbItemsPerPage.setSelectedItem(itemsPerPage);
    }

    public void setRowHeightTable(int rowHeightTable) {
        this.rowHeightTable = rowHeightTable;
        if (table != null) {
            table.setRowHeight(rowHeightTable);
        }
    }

    public void setIdColumnIndex(int idColumnIndex) {
        this.idColumnIndex = idColumnIndex;
    }

    public void setEnableAutoFilter(boolean enable) {
        this.enableAutoFilter = enable;
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JTextField getTfSearch() {
        return tfSearch;
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JButton getBtnRefresh() {
        return btnRefresh;
    }

    public JButton getBtnFilter() {
        return btnFilter;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public JPanel getFilterPanel() {
        return filterPanel;
    }
}