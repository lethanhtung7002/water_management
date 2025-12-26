package com;

import javax.swing.UIManager;
import gui.LoginForm;

public class Main {
    public static void main(String[] args) {

        try {
            // Thiết lập giao diện Nimbus
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Chạy giao diện
        new LoginForm();
    }
}