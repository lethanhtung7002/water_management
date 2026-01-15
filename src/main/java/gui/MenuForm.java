package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;

import gui.Customer.CustomerForm;
import gui.GiaNuoc.GiaNuocForm;
import data.DataLoader;

/**
 * Form menu chính của hệ thống quản lý nước.
 * 
 * Bao gồm:
 * 
 * Sidebar bên trái với các menu điều hướng
 * Panel tìm kiếm ở phía trên
 * Content area chính sử dụng CardLayout để chuyển đổi giữa các trang
 * 
 * @author Lê Thanh Tùng :)
 * @version 1.0
 */

public class MenuForm extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private JPanel sidebar;
    private JPanel searchPanel;
    private JButton logoutButton = new JButton("🚪 Đăng xuất");

    // Key cho CardLayout (tránh phụ thuộc text hiển thị)
    private static final String PAGE_CUSTOMER = "PAGE_CUSTOMER";
    private static final String PAGE_WATER_PRICE = "PAGE_WATER_PRICE";

    public MenuForm() {
        setTitle("Water Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 800);
        setMinimumSize(new Dimension(900, 700));
        setLayout(new BorderLayout());

        initSearch();
        initSideBar();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initSearch() {
        // ==== SEARCH FIELD ====
        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(new Color(26, 26, 26));
        JTextField searchField = new JTextField(30);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(searchField);

        add(searchPanel, BorderLayout.NORTH);
    }

    private void initSideBar() {
        /*
         * ==== SIDEBAR ====
         * tạo ra thanh bên trái để chọn các thao tác cần thực hiện
         */
        sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(32, 32, 32));
        sidebar.setPreferredSize(new Dimension(250, 0));

        /*
         * ==== MENU CENTER ====
         * tạo ra thanh menu chứa các nút cần nội dung để thực hiện hành động
         */
        JPanel centerBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        centerBar.setBackground(new Color(32, 32, 32));

        centerBar.add(createMenuButton("Khách hàng", "👤", PAGE_CUSTOMER));
        centerBar.add(createMenuButton("Giá nước", "💧", PAGE_WATER_PRICE));
        sidebar.add(centerBar, BorderLayout.CENTER);
        // ==== MENU CENTER ==== \\

        /*
         * ==== ACCOUNT BAR ====
         * tạo ra thanh tài khoản ở trên để đăng xuất
         */
        JPanel accountBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        accountBar.setBackground(new Color(32, 32, 32));
        logoutButton.setPreferredSize(new Dimension(230, 40));
        accountBar.add(logoutButton);
        sidebar.add(accountBar, BorderLayout.NORTH);

        logoutButton.addActionListener(e -> {
            // ghi kí tự trống vào tệp để lần sau không tự động đang nhập
            logoutAction();
        });

        // ==== ACCOUNT BAR ==== \\

        /*
         * ==== CONTENT ====
         * Phần nội dung cho Menu Center sau khi chọn nút tương ứng sẽ hiễn thi trang
         * nội dung để thực hiện
         */
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(26, 26, 26));

        contentPanel.add(new CustomerForm(), PAGE_CUSTOMER);
        contentPanel.add(new GiaNuocForm(), PAGE_WATER_PRICE);

        // === CONTENT === \\

        // ==== GHÉP VÀO FRAME ====
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * ==== MENU BUTTON ====
     * Tạo ra các nút cho thanh bên trái
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
        btn.setContentAreaFilled(false);// tắt nền nút
        btn.setForeground(Color.white); // màu chữ
        btn.setHorizontalAlignment(SwingConstants.LEFT); // căn lề trái
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
                btn.setBackground(new Color(60, 60, 60));
                btn.setContentAreaFilled(true);// bật nền nút khi di chuột đến
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setContentAreaFilled(false);// tắt nền chuột khi di chuột ra
            }
        });

        btn.addActionListener(e -> cardLayout.show(contentPanel, pageKey));

        return btn;
    }

    private void logoutAction() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn đăng xuất?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            new DataLoader().loginWrite("", "");
            dispose();
            new LoginForm();
        }
    }

    // ==== PAGE MẪU ====
    private JPanel createPage(String title) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(26, 26, 26));
        JLabel label = new JLabel(title);
        label.setForeground(Color.WHITE);
        p.add(label);
        return p;
    }
}
