package gui;

import data.LoginDataLoader;

import java.awt.*;
import java.util.Arrays;
import javax.swing.*;

/**
 * Form đăng nhập của hệ thống quản lý nước.
 * 
 * Chức năng chính:
 * - Đăng nhập với username và password
 * - Lưu thông tin đăng nhập 
 * - Tự động đăng nhập nếu đã lưu thông tin
 * - Hỗ trợ phím Enter để submit form
 * 
 * Quy trình hoạt động:
 * 1. Kiểm tra file login.txt có tồn tại không
 * 2. Nếu có -> Tự động đăng nhập -> Mở MenuForm
 * 3. Nếu không -> Hiển thị form đăng nhập
 * 
 * @author Lê Thanh Tùng
 * @version 1.0
 */
public class LoginForm extends JFrame {

    // ===== UI COMPONENTS =====
    private final JTextField userTextField = new JTextField(20);
    private final JPasswordField passField = new JPasswordField(20);
    private final JCheckBox rememberMeCheckbox = new JCheckBox("Nhớ Thông tin đăng nhập");
    private final JButton loginButton = new JButton("Login");

    // ===== CREDENTIALS =====
    private String username = "admin";
    private String password = "password";
    private final LoginDataLoader loader = new LoginDataLoader();

    /**
     * Khởi tạo LoginForm.
     * Kiểm tra file login.txt, nếu có thì tự động đăng nhập,
     * nếu không thì hiển thị form đăng nhập.
     */
    public LoginForm() {
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        String[] credentials = loader.loginRead();
        if (checkLogin(credentials[0], credentials[1])) {
            System.out.println("Đăng nhập tự động thành công\n" +
                    "Username: " + credentials[0] + "\n");

            new MenuForm();
            dispose();
        } else {
            showLoginForm();
        }
    }

    /**
     * Hiển thị form đăng nhập khi chưa có thông tin lưu.
     */
    private void showLoginForm() {
        init();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Kiểm tra username và password có khớp với thông tin đã lưu không.
     */
    private boolean checkLogin(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Khởi tạo các component của form đăng nhập.
     * Bao gồm: Logo, Form input, Button panel.
     */
    private void init() {
        add(createLogoPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    /**
     * Tạo panel chứa logo ở phía trên form.
     * Logo được scale về kích thước 120x120 pixels.
     */
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        ImageIcon icon = new ImageIcon("src/main/resources/assets/login_logo.jpg");
        Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(img));
        logoPanel.add(logoLabel);
        return logoPanel;
    }

    /**
     * Tạo form nhập username, password và checkbox "Remember me".
     * Sử dụng GridBagLayout để căn chỉnh các component.
     * Hỗ trợ phím Enter: Enter ở Username -> Focus vào Password, Enter ở Password ->
     * Submit form.
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(userTextField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(passField, gbc);

        // Remember me checkbox
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(rememberMeCheckbox, gbc);

        // Enter key support
        userTextField.addActionListener(e -> passField.requestFocus());
        passField.addActionListener(e -> handleLogin());

        return formPanel;
    }

    /**
     * Tạo panel chứa nút Login ở phía dưới form.
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        loginButton.addActionListener(e -> handleLogin());
        buttonPanel.add(loginButton);
        return buttonPanel;
    }

    /**
     * Xử lý sự kiện đăng nhập khi user click nút Login hoặc nhấn Enter.
     * Quy trình:
     * 1. Validate input (không để trống)
     * 2. Kiểm tra username/password
     * 3. Nếu đúng: Lưu thông tin (nếu tick Remember me) -> Mở MenuForm -> Đóng
     * LoginForm
     * 4. Nếu sai: Hiển thị lỗi và clear password field
     * 5. Luôn clear password từ memory sau khi xử lý (bảo mật)
     */
    private void handleLogin() {
        String inputUsername = userTextField.getText().trim();
        char[] passwordChars = passField.getPassword();
        String inputPassword = new String(passwordChars);

        try {
            // Validation: Kiểm tra không để trống
            if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                showError("Username and password cannot be empty.");
                return;
            }

            // Kiểm tra credentials
            if (checkLogin(inputUsername, inputPassword)) {
                // Lưu thông tin login nếu checkbox được tick
                if (rememberMeCheckbox.isSelected()) {
                    if (!loader.loginWrite(inputUsername, inputPassword)) {
                        showWarning("Failed to save login credentials.");
                    }
                }

                // Mở MenuForm và đóng LoginForm
                SwingUtilities.invokeLater(MenuForm::new);
                dispose();
            } else {
                // Sai thông tin đăng nhập
                showError("Invalid username or password.");
                passField.setText("");
                passField.requestFocus();
            }
        } finally {
            // Clear password từ memory (bảo mật)
            Arrays.fill(passwordChars, '0');
        }
    }

    /**
     * Hiển thị dialog thông báo lỗi.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Hiển thị dialog cảnh báo.
     */
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}