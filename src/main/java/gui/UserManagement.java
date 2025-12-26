package gui;

import dao.*;
import model.User;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class UserManagement {

    JLabel lbTitle = new JLabel("User Management");
    JTable table = new JTable();
    DefaultTableModel tableModel;
    JScrollPane scrollPane;

    JButton btnAdd = new JButton("Add User");
    JButton btnEdit = new JButton("Edit User");
    JButton btnDelete = new JButton("Delete User");
    JButton btnRefresh = new JButton("Refresh");

    ArrayList<User> UserArr = new ArrayList<User>();
    
}