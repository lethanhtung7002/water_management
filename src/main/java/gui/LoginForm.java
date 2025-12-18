package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginForm extends JFrame {

    JLabel userLabel = new JLabel("Username:");
    JTextField userTextField = new JTextField(15);

    JLabel passLabel = new JLabel("Password:");
    JPasswordField passField = new JPasswordField(15);

    JButton loginButton = new JButton("Login");

    String username = "password", password = "admin";

    public LoginForm() {
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        /* ===== LOGO ===== */
        ImageIcon icon = new ImageIcon("src\\main\\java\\assets\\login_logo.jpg");
        Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(img));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);

        add(logoLabel, BorderLayout.NORTH);

        /* ===== FORM ===== */
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(userLabel);
        formPanel.add(userTextField);
        formPanel.add(passLabel);
        formPanel.add(passField);

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
        new LoginForm();

    }
}
