package gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;

public class LoginForm extends JFrame {

    JLabel userLabel = new JLabel("Username:");
    JTextField userTextField = new JTextField(20);

    JLabel passLabel = new JLabel("Password:");
    JPasswordField passField = new JPasswordField(20);

    JButton loginButton = new JButton("Login");

    String username = "password", password = "admin";

    public LoginForm() {
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        /* ===== LOGO ===== */
        ImageIcon icon = new ImageIcon("src\\main\\resources\\assets\\login_logo.jpg");
        Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(img));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        add(logoLabel, BorderLayout.NORTH);

        /* ===== FORM ===== */
        JPanel formPanel = new JPanel(new java.awt.GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5); // Khoảng cách giữa các ô
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dòng 1: Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Cho phép ô nhập chiếm thêm không gian thừa
        formPanel.add(userTextField, gbc);

        // Dòng 2: Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        formPanel.add(passField, gbc);

        add(formPanel, BorderLayout.CENTER);

        /* ===== BUTTON ===== */
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener((ActionEvent e) -> {
            String username = userTextField.getText();
            String password = new String(passField.getPassword());
            if (checkLogin(username, password)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Login successful!");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password.");
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public boolean checkLogin(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(LoginForm::new);

    }
}
