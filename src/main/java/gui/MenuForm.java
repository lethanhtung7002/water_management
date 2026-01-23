package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import data.LoginDataLoader;
import gui.Page.CustomerPage;
import gui.Page.HoSuDungPage;
import gui.Page.WaterPricePage;

/**
 * Form menu chính của hệ thống quản lý nước.
 * 
 * Bao gồm:
 * - Sidebar bên trái với các menu điều hướng
 * - Panel tìm kiếm ở phía trên
 * - Content area chính sử dụng CardLayout để chuyển đổi giữa các trang
 * - Hiển thị trang đang được chọn
 * 
 */
public class MenuForm extends JFrame {

    // Key cho CardLayout (để tránh phụ thuộc text hiển thị)
    private class Page{
        static final String HOME = "0";
        static final String CUSTOMER = "1";
        static final String HO_SU_DUNG = "2";
        static final String WATER_PRICRE = "3";
    }

    private JPanel contentPanel;
    private CardLayout cardLayout; 
    // CardLayout là cơ chế đổi màn hình trong cùng một cửa sổ.

    private JPanel sidebar;
    private JPanel searchPanel;
    private JButton logoutButton = new JButton("🚪 Đăng xuất");

    // ===== THÊM: Lưu các nút menu để highlight =====
    private JButton btnHome;
    private JButton btnCustomer;
    private JButton btnHoSuDung;
    private JButton btnWaterPrice;
    private JButton currentSelectedButton = null; // Nút đang được chọn

    public MenuForm() {
        setTitle("Water Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(900, 700));
        setLayout(new BorderLayout());

        // initSearch();
        initSideBar();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Khởi tạo thanh tìm kiếm ở phía trên.
     */
    private void initSearch() {
        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(GUIConstants.Colors.BACKGROUND);
        JTextField searchField = new JTextField(30);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(searchField);

        add(searchPanel, BorderLayout.NORTH);
    }

    /**
     * Khởi tạo sidebar bên trái bao gồm menu và nút logout.
     */
    private void initSideBar() {
        /*
         * ==== SIDEBAR ====
         * tạo ra thanh bên trái để chọn các thao tác cần thực hiện
         */
        sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(GUIConstants.Colors.BACKGROUND);
        sidebar.setPreferredSize(new Dimension(250, 0));

        /*
         * ==== MENU CENTER ====
         * tạo ra thanh menu chứa các nút cần nội dung để thực hiện hành động
         */
        JPanel centerBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        centerBar.setBackground(GUIConstants.Colors.BACKGROUND);

        // Tạo các nút menu và lưu
        // btnHome = createMenuButton("Trang chủ", "🏠", Page.HOME);
        btnCustomer = createMenuButton("Khách hàng", "👤", Page.CUSTOMER);
        btnHoSuDung = createMenuButton("Hộ Sủ dụng", "🏘", Page.HO_SU_DUNG);
        btnWaterPrice = createMenuButton("Giá nước", "💧", Page.WATER_PRICRE);

        // centerBar.add(btnHome);
        centerBar.add(btnCustomer);
        centerBar.add(btnHoSuDung);
        centerBar.add(btnWaterPrice);

        sidebar.add(centerBar, BorderLayout.CENTER);
        // ==== MENU CENTER ==== \\

        /*
         * ==== ACCOUNT BAR ====
         * tạo ra thanh tài khoản ở trên để đăng xuất
         */
        JPanel accountBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        accountBar.setBackground(GUIConstants.Colors.BACKGROUND);
        logoutButton.setPreferredSize(new Dimension(230, 40));
        accountBar.add(logoutButton);
        sidebar.add(accountBar, BorderLayout.NORTH);

        logoutButton.addActionListener(e -> logoutAction());

        /*
         * ==== CONTENT ====
         * Phần nội dung cho Menu Center sau khi chọn nút tương ứng sẽ hiễn thi trang
         * nội dung để thực hiện
         */
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(GUIConstants.Colors.BACKGROUND);

        // contentPanel.add(createPage("Trang chủ để thực hiện các tính năng nhanh nhưng chưa thêm gì cả :))"), Page.HOME);
        contentPanel.add(new CustomerPage(), Page.CUSTOMER);
        contentPanel.add(new HoSuDungPage(), Page.HO_SU_DUNG);
        contentPanel.add(new WaterPricePage(), Page.WATER_PRICRE);

        // ==== GHÉP VÀO FRAME ====
        add(sidebar, BorderLayout.WEST);
        add(contentPanel);

        // ===== THÊM: Mặc định =====
        setSelectedButton(btnCustomer);
    }

    /**
     * Tạo nút menu với hiệu ứng hover và highlight khi được chọn.
     * 
     * @param text    Text hiển thị trên nút
     * @param icon    Icon emoji hiển thị trước text
     * @param pageKey Key để chuyển trang trong CardLayout
     * @return JButton đã được cấu hình
     */
    private JButton createMenuButton(String text, String icon, String pageKey) {
        /*
         * +setContentAreaFilled(true) Swing tự vẽ nền nút (background) theo Look & Feel
         * hiện tại
         * +Cursor(Cursor.HAND_CURSOR) đổi hình con trõ chuột thành hình bàn tay
         */
        JButton btn = new JButton(icon + "   " + text);
        btn.setPreferredSize(new Dimension(230, 40));
        btn.setContentAreaFilled(false);// tắt nền nút`
        btn.setForeground(Color.WHITE);
        btn.setHorizontalAlignment(SwingConstants.LEFT); // căn trái
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        /*
         * MouseAdapter là abstract class đã implement sẵn MouseListener
         * và để trống tất cả method.
         * override cái mình cần để thực hiện thay đổi kiểu
         * mouseEntered khi di chuột vào nút
         * mouseExited khi di chuột ra khỏi nút
         */
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Chỉ đổi màu nếu KHÔNG phải nút đang được chọn
                if (btn != currentSelectedButton) {
                    btn.setBackground(GUIConstants.Colors.HOVER);
                    btn.setContentAreaFilled(true);// bật nền nút khi di chuột đến
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Chỉ tắt nền nếu KHÔNG phải nút đang được chọn
                if (btn != currentSelectedButton) {
                    btn.setContentAreaFilled(false);// tắt nền nút khi di chuột ra
                }
            }
        });

        // Action khi click
        btn.addActionListener(e -> {
            cardLayout.show(contentPanel, pageKey);
            setSelectedButton(btn); // Highlight nút được chọn
        });

        return btn;
    }

    /**
     * Đặt nút được chọn và highlight nó.
     * 
     * @param selectedButton Nút được chọn
     */
    private void setSelectedButton(JButton selectedButton) {
        // Bỏ highlight nút cũ
        if (currentSelectedButton != null) {
            currentSelectedButton.setContentAreaFilled(false);
            currentSelectedButton.setBackground(GUIConstants.Colors.BACKGROUND);
        }

        // Highlight nút mới
        currentSelectedButton = selectedButton;
        currentSelectedButton.setContentAreaFilled(true);
        currentSelectedButton.setBackground(GUIConstants.Colors.SELECTED);
    }

    /**
     * Xử lý sự kiện logout.
     */
    private void logoutAction() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn đăng xuất?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            new LoginDataLoader().loginWrite("", "");
            dispose();
            new LoginForm();
        }
    }

    /**
     * Tạo trang mẫu với label ở giữa.
     * 
     * @param title Text hiển thị
     * @return JPanel chứa trang mẫu
     */
    private JPanel createPage(String title) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(GUIConstants.Colors.BACKGROUND);
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.WHITE);
        p.add(label);
        return p;
    }
}