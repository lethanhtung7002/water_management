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
import javax.swing.SwingConstants;

public class MenuForm extends JFrame {

    private JFrame frame;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MenuForm() {
        frame = new JFrame("Water Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 650);
        frame.setLayout(new BorderLayout());

        // --- 1. SIDEBAR (THANH BÊN) ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(32, 32, 32)); // Màu xám tối
        sidebar.setPreferredSize(new Dimension(250, 0)); // Chiều rộng cố định
        sidebar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Canh trái với khoảng cách

        // Tiêu đề người dùng
        JLabel userLabel = new JLabel("Water Management");
        userLabel.setForeground(Color.WHITE);
        userLabel.setPreferredSize(new Dimension(230, 50));

        sidebar.add(userLabel);

        // menu buttons
        sidebar.add(createMenuButton("User Management", "👤"));
        sidebar.add(createMenuButton("GiaNuoc", "💧"));

        // --- 2. VÙNG NỘI DUNG (Dùng CardLayout để chuyển trang) ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(26, 26, 26)); // Màu nền tối

        // Thêm các trang nội dung mẫu
        contentPanel.add(createPage("User Management Page"), "User Management");
        contentPanel.add(createPage("GiaNuoc Page"), "GiaNuoc");

        // --- GHÉP NỐI ---
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null); // Canh giữa
        frame.setVisible(true);
    }

    // Hàm tạo nút Menu với hiệu ứng Hover
    private JButton createMenuButton(String text, String icon) {
        JButton btn = new JButton(icon + "   " + text);
        btn.setPreferredSize(new Dimension(230, 40));
        btn.setFocusPainted(false); // Bỏ viền khi bấm
        btn.setContentAreaFilled(false); // Bỏ nền mặc định
        btn.setBorderPainted(false); // Bỏ viền nút
        btn.setForeground(Color.WHITE);  // Màu chữ trắng
        btn.setHorizontalAlignment(SwingConstants.LEFT); // Canh trái
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Con trỏ tay

        // Hiệu ứng rê chuột
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(60, 60, 60));
                btn.setContentAreaFilled(true); // Hiện nền khi hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setContentAreaFilled(false);
            }
        });

        // Sự kiện khi bấm nút
        btn.addActionListener(e -> cardLayout.show(contentPanel, text));

        return btn;
    }

    private JPanel createPage(String title) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(26, 26, 26));
        JLabel label = new JLabel(title);
        label.setForeground(Color.WHITE);
        p.add(label);
        return p;
    }

    public static void main(String[] args) {
        new MenuForm();
    }
}
