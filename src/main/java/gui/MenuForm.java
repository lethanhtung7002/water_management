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

import gui.Customer.CustomerForm;
import data.DataLoader;

public class MenuForm extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private JPanel sidebar;
    private JPanel searchPanel;
    private JButton logoutButton;

    // Key cho CardLayout (tránh phụ thuộc text hiển thị)
    private static final String PAGE_CUSTOMER = "PAGE_CUSTOMER";
    private static final String PAGE_WATER_PRICE = "PAGE_WATER_PRICE";

    public MenuForm() {
        setTitle("Water Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
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
        
        add(searchPanel,BorderLayout.NORTH);
    }

    private void initSideBar() {

        // ==== LOGOUT BUTTON (PHẢI TẠO TRƯỚC) ====
        logoutButton = new JButton("🚪 Đăng xuất");
        logoutButton.setFocusPainted(false);

        logoutButton.addActionListener(e -> {
            DataLoader loader = new DataLoader();
            loader.loginWrite("", "");
            dispose();
            new LoginForm();
        });

        // ==== SIDEBAR ====
        sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(32, 32, 32));
        sidebar.setPreferredSize(new Dimension(250, 0));

        // ==== MENU CENTER ====
        JPanel centerBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        centerBar.setBackground(new Color(32, 32, 32));

        centerBar.add(createMenuButton("Khách hàng", "👤", PAGE_CUSTOMER));
        centerBar.add(createMenuButton("Giá nước", "💧", PAGE_WATER_PRICE));

        sidebar.add(centerBar, BorderLayout.CENTER);

        // ==== ACCOUNT BAR ====
        JPanel accountBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        accountBar.setBackground(new Color(32, 32, 32));
        logoutButton.setPreferredSize(new Dimension(230, 40));
        accountBar.add(logoutButton);
        sidebar.add(accountBar, BorderLayout.NORTH);

        // ==== CONTENT ====
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(26, 26, 26));

        contentPanel.add(new CustomerForm(), PAGE_CUSTOMER);
        contentPanel.add(createPage("GiaNuoc Page"), PAGE_WATER_PRICE);

        // ==== GHÉP VÀO FRAME ====
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    // ==== MENU BUTTON ====
    private JButton createMenuButton(String text, String icon, String pageKey) {
        JButton btn = new JButton(icon + "   " + text);
        btn.setPreferredSize(new Dimension(230, 40));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(60, 60, 60));
                btn.setContentAreaFilled(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setContentAreaFilled(false);
            }
        });

        btn.addActionListener(e -> cardLayout.show(contentPanel, pageKey));

        return btn;
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
