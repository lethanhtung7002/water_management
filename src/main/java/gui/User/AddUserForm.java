package gui.User;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dao.UserDao;
import model.User;

public class AddUserForm extends JFrame {
    JLabel lbAdd = new JLabel("Add New User");

    JLabel lbName = new JLabel("Name:");
    JLabel lbLoaiUser = new JLabel("Loai User:");
    JLabel lbCCCD = new JLabel("CCCD:");
    JLabel lbPhone = new JLabel("Phone Number:");
    JLabel lbEmail = new JLabel("Email:");

    JTextField tfName = new JTextField();
    JComboBox<String> tfLoaiUser;
    JTextField tfCCCD = new JTextField();
    JTextField tfPhone = new JTextField();
    JTextField tfEmail = new JTextField();

    JButton btnSave = new JButton("Save");
    JButton btnCancel = new JButton("Cancel");

    UserDao userDao = new UserDao();

    public AddUserForm() {
        setTitle("Add User");
        
        init();

        btnSave.addActionListener(e -> saveUser());
        btnCancel.addActionListener(e -> dispose());
    }

    public AddUserForm(User user) {
        // Constructor for editing an existing user (not implemented yet)
        setTitle("Edit User");
        init();

        tfName.setText(user.getNameUser());
        if (user.getLoaiUser() != null) {
            tfLoaiUser.setSelectedItem(user.getLoaiUser());
        }
        tfCCCD.setText(user.getCCCD());
        tfPhone.setText(user.getPhoneUser());
        tfEmail.setText(user.getEmailUser());

        btnSave.addActionListener(e -> saveUser());
        btnCancel.addActionListener(e -> dispose());
    }

    private void saveUser() {
        User user = new User(
                0,
                tfName.getText(),
                (String) tfLoaiUser.getSelectedItem(),
                tfCCCD.getText(),
                tfPhone.getText(),
                tfEmail.getText());

        if (userDao.addUser(user)) {
            JOptionPane.showMessageDialog(this, "User added successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void init() {
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        String[] loaiUsers = { "Ho Gia Dinh", "Doanh Nghiep" };
        tfLoaiUser = new JComboBox<>(loaiUsers);

        lbAdd.setBounds(150, 10, 200, 30);
        lbName.setBounds(50, 60, 100, 25);
        tfName.setBounds(150, 60, 200, 25);
        lbLoaiUser.setBounds(50, 100, 100, 25);
        tfLoaiUser.setBounds(150, 100, 200, 25);
        lbCCCD.setBounds(50, 140, 100, 25);
        tfCCCD.setBounds(150, 140, 200, 25);
        lbPhone.setBounds(50, 180, 100, 25);
        tfPhone.setBounds(150, 180, 200, 25);
        lbEmail.setBounds(50, 220, 100, 25);
        tfEmail.setBounds(150, 220, 200, 25);
        btnSave.setBounds(80, 280, 100, 30);
        btnCancel.setBounds(220, 280, 100, 30);

        add(lbAdd);
        add(lbName);
        add(tfName);
        add(lbLoaiUser);
        add(tfLoaiUser);
        add(lbCCCD);
        add(tfCCCD);
        add(lbPhone);
        add(tfPhone);
        add(lbEmail);
        add(tfEmail);
        add(btnSave);
        add(btnCancel);

    }

    public static void main(String[] args) {
        AddUserForm addUserForm = new AddUserForm();
        addUserForm.setVisible(true);
    }
}
