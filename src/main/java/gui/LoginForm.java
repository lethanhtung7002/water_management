package gui;

import data.DataLoader;   
import java.awt.*;
import java.util.Arrays;
import javax.swing.*;

public class LoginForm extends JFrame {
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";

    private final JTextField userTextField = new JTextField(20);
    private final JPasswordField passField = new JPasswordField(20);
    private final JCheckBox rememberMeCheckbox = new JCheckBox("Remember me");
    private final JButton loginButton = new JButton("Login");

    private String username = DEFAULT_USERNAME;
    private String password = DEFAULT_PASSWORD;

    public LoginForm() {
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Kiểm tra xem có thông tin login đã lưu không
        if (checkSavedLogin()) {
            // Tự động đăng nhập và mở MenuForm
            SwingUtilities.invokeLater(MenuForm::new);
            dispose();
        } else {
            // Hiển thị form login
            initComponents();
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

    private boolean checkSavedLogin() {
        DataLoader loader = new DataLoader();
        if (loader.hasLoginSaved()) {
            String[] credentials = loader.loginRead();
            if (credentials[0] != null && credentials[1] != null) {
                this.username = credentials[0];
                this.password = credentials[1];
                return true;
            }
        }
        return false;
    }

    private boolean checkLogin(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    private void initComponents() {
        add(createLogoPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        ImageIcon icon = new ImageIcon("src/main/resources/assets/login_logo.jpg");
        Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(img));
        logoPanel.add(logoLabel);
        return logoPanel;
    }

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

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        loginButton.addActionListener(e -> handleLogin());
        buttonPanel.add(loginButton);
        return buttonPanel;
    }

    private void handleLogin() {
        String inputUsername = userTextField.getText().trim();
        char[] passwordChars = passField.getPassword();
        String inputPassword = new String(passwordChars);

        try {
            // Validation
            if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                showError("Username and password cannot be empty.");
                return;
            }

            // Check credentials
            if (checkLogin(inputUsername, inputPassword)) {
                DataLoader loader = new DataLoader();

                // Lưu hoặc xóa thông tin login tùy theo checkbox
                if (rememberMeCheckbox.isSelected()) {
                    if (!loader.loginWrite(inputUsername, inputPassword)) {
                        showWarning("Failed to save login credentials.");
                    }
                } 
                // Open main form
                SwingUtilities.invokeLater(MenuForm::new);
                dispose();
            } else {
                showError("Invalid username or password.");
                passField.setText("");
                passField.requestFocus();
            }
        } finally {
            // Clear password from memory
            Arrays.fill(passwordChars, '0');
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }    
}