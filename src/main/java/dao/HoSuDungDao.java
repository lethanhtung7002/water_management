package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.HoSuDung;

public class HoSuDungDao {
    private MySQLConnect mySQLConnect = new MySQLConnect();

    // lấy danh sách hộ sử dụng
    public ArrayList<HoSuDung> getHoSuDungByCustomerId(int customerId) {
        ArrayList<HoSuDung> userList = new ArrayList<>();
        try {
            String query = "SELECT * FROM %s WHERE %s = %d"
                    .formatted(DB_HoSuDungCol.TableName,
                            DB_HoSuDungCol.ID_KhachHang, customerId);
            ResultSet rs = mySQLConnect.executeQuery(query);
            while (rs.next()) {
                HoSuDung user = new HoSuDung();
                user.setID_HoSuDung(rs.getInt(DB_HoSuDungCol.ID));
                user.setMaQuanHuyen(rs.getString(DB_HoSuDungCol.KhuVuc));
                user.setDiaChi(rs.getString(DB_HoSuDungCol.DiaChi));
                user.setTrangThai(rs.getInt(DB_HoSuDungCol.TrangThai));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy hộ sử dụng: " + e.getMessage());
        }
        return userList;
    }

    // thêm hộ sử dụng
    public boolean add_HoSuDung(HoSuDung user) {
        int result = 0;
        String query = """
                INSERT INTO %s (%s, %s, %s, %s)
                VALUES ('%d', '%s', '%s', '%d')
                """.formatted(
                DB_HoSuDungCol.TableName, DB_HoSuDungCol.ID_KhachHang, DB_HoSuDungCol.DiaChi,
                DB_HoSuDungCol.KhuVuc, DB_HoSuDungCol.TrangThai,
                user.getID_Customer(), user.getDiaChi(), user.getMaQuanHuyen(), user.getTrangThai());

        try {
            result = mySQLConnect.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi thêm hộ sử dụng: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    // cập nhật hộ sử dụng
    public boolean update_HoSuDung(HoSuDung user) {
        int result = 0;
        String query = """
                UPDATE %s
                SET %s = '%d', %s = '%s', %s = '%s', %s = '%d'
                WHERE %s = '%d'
                """.formatted(
                DB_HoSuDungCol.TableName,
                DB_HoSuDungCol.ID_KhachHang, user.getID_Customer(),
                DB_HoSuDungCol.DiaChi, user.getDiaChi(),
                DB_HoSuDungCol.KhuVuc, user.getMaQuanHuyen(),
                DB_HoSuDungCol.TrangThai, user.getTrangThai(),
                DB_HoSuDungCol.ID, user.getID_HoSuDung());

        try {
            result = mySQLConnect.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật hộ sử dụng: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    // xóa hộ sử dụng
    public boolean delete_HoSuDung(int id_HoSuDung) {
        int result = 0;
        String query = "DELETE FROM %s WHERE %s = %d".formatted(
                DB_HoSuDungCol.TableName,
                DB_HoSuDungCol.ID,
                id_HoSuDung);
        try {
            result = mySQLConnect.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi xóa hộ sử dụng: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    // tìm hộ sử dụng theo ID
    public HoSuDung getHoSuDungById(int idHoSuDung) {
        HoSuDung hoSuDung = null;
        try {
            String query = "SELECT * FROM %s WHERE %s = %d"
                    .formatted(DB_HoSuDungCol.TableName, DB_HoSuDungCol.ID, idHoSuDung);
            ResultSet rs = mySQLConnect.executeQuery(query);
            if (rs.next()) {
                hoSuDung = new HoSuDung();
                hoSuDung.setID_HoSuDung(rs.getInt(DB_HoSuDungCol.ID));
                hoSuDung.setID_Customer(rs.getInt(DB_HoSuDungCol.ID_KhachHang));
                hoSuDung.setMaQuanHuyen(rs.getString(DB_HoSuDungCol.KhuVuc));
                hoSuDung.setDiaChi(rs.getString(DB_HoSuDungCol.DiaChi));
                hoSuDung.setTrangThai(rs.getInt(DB_HoSuDungCol.TrangThai));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy hộ sử dụng: " + e.getMessage());
        }
        return hoSuDung;
    }
}
