package dao;

import model.User;

import java.util.ArrayList;
import java.sql.*;

public class UserDao {
    MySQLConnect mySQLConnect = new MySQLConnect();

    // lấy người dùng
    public ArrayList<User> getUsers() {
        ArrayList<User> userList = new ArrayList<>();
        try {
            String query = "SELECT * FROM user";
            ResultSet rs = mySQLConnect.getDB(query);
            while (rs.next()) {
                User user = new User();
                user.setIdUser(rs.getInt("idUser"));
                user.setLoaiUser(rs.getString("loaiUser"));
                user.setNameUser(rs.getString("Name"));
                user.setCCCD(rs.getString("CCCD"));
                user.setPhoneUser(rs.getString("phoneNumber"));
                user.setEmailUser(rs.getString("email"));

                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy người dùng: " + e.getMessage());
        }
        return userList;
    }

    public boolean addUser(User user) {
        int result = 0;
        String query = "INSERT INTO user (loaiUser, Name, CCCD, phoneNumber, email) VALUES ('"
                + user.getIdUser() + "', '"
                + user.getLoaiUser() + "', '"
                + user.getNameUser() + "', '"
                + user.getCCCD() + "', '"
                + user.getPhoneUser() + "', '"
                + user.getEmailUser() + "')";

        try {
            result = mySQLConnect.ExcuteDB(query);

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Lỗi thêm người dùng: " + e.getMessage());
        }
        return result > 0;
    }

    public boolean updateUser(User user) {
        int result = 0;
        String query = "UPDATE user SET "
                + "loaiUser = '" + user.getLoaiUser() + "', "
                + "Name = '" + user.getNameUser() + "', "
                + "CCCD = '" + user.getCCCD() + "', "
                + "phoneNumber = '" + user.getPhoneUser() + "', "
                + "email = '" + user.getEmailUser() + "' "
                + "WHERE idUser = " + user.getIdUser();

        try {
            result = mySQLConnect.ExcuteDB(query);

        } catch (Exception e) {
            System.out.println("Lỗi cập nhật người dùng: " + e.getMessage());
        }
        return result > 0;
    }

    public User getUserById(int idUser) {
        User user = null;
        try {
            String query = "SELECT * FROM user WHERE idUser = " + idUser;
            ResultSet rs = mySQLConnect.getDB(query);
            if (rs.next()) {
                user = new User();
                user.setIdUser(rs.getInt("idUser"));
                user.setLoaiUser(rs.getString("loaiUser"));
                user.setNameUser(rs.getString("Name"));
                user.setCCCD(rs.getString("CCCD"));
                user.setPhoneUser(rs.getString("phoneNumber"));
                user.setEmailUser(rs.getString("email"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy người dùng theo ID: " + e.getMessage());
        }
        return null;
    }

    public boolean deleteUserById(int idUser) {
        int result = 0;
        String query = "DELETE FROM user WHERE idUser = " + idUser;

        try {
            result = mySQLConnect.ExcuteDB(query);

        } catch (Exception e) {
            System.out.println("Lỗi xóa người dùng: " + e.getMessage());
        }
        return result > 0;
    }
}