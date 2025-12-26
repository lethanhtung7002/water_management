import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowsSettingsSwing {

    private JFrame frame;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public WindowsSettingsSwing() {
        frame = new JFrame("Windows Settings Clone");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 650);
        frame.setLayout(new BorderLayout());

        // --- 1. SIDEBAR (THANH BÊN) ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(32, 32, 32)); // Màu xám tối
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // Tiêu đề người dùng
        JLabel userLabel = new JLabel("  Lê Thanh Tùng");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        userLabel.setPreferredSize(new Dimension(230, 50));
        sidebar.add(userLabel);

        // Các nút menu
        sidebar.add(createMenuButton("Trang chủ", "🏠"));
        sidebar.add(createMenuButton("Hệ thống", "💻"));
        sidebar.add(createMenuButton("Mạng & internet", "🌐"));
        sidebar.add(createMenuButton("Cá nhân hóa", "🎨"));

        // --- 2. VÙNG NỘI DUNG (Dùng CardLayout để chuyển trang) ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(26, 26, 26));

        // Thêm các trang nội dung mẫu
        contentPanel.add(createPage("Chào mừng đến với Trang chủ"), "Trang chủ");
        contentPanel.add(createPage("Cài đặt Hệ thống"), "Hệ thống");

        // --- GHÉP NỐI ---
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Hàm tạo nút Menu với hiệu ứng Hover
    private JButton createMenuButton(String text, String icon) {
        JButton btn = new JButton(icon + "   " + text);
        btn.setPreferredSize(new Dimension(230, 40));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hiệu ứng rê chuột
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

        // Sự kiện khi bấm nút
        btn.addActionListener(e -> cardLayout.show(contentPanel, text));

        return btn;
    }

    // Hàm tạo một trang nội dung đơn giản
    private JPanel createPage(String title) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(26, 26, 26));
        JLabel label = new JLabel(title);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        p.add(label);
        return p;
    }

    public static void main(String[] args) {
        // Chỉnh giao diện hệ thống cho đẹp hơn
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(WindowsSettingsSwing::new);
    }
}