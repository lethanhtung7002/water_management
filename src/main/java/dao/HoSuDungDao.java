package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.HoSuDung;
import static dao.MySQLConnect.ConnectQLN;

public class HoSuDungDao {

    // lấy danh sách hộ sử dụng
    public ArrayList<HoSuDung> getHoSuDungByCustomerId(int customerId) {
        ArrayList<HoSuDung> userList = new ArrayList<>();
        try {
            String query = "SELECT * FROM %s WHERE %s = %d"
                    .formatted(qlnTableName.HoSuDung,
                            qlnIDName.CustomerID, customerId);
            ResultSet rs = ConnectQLN.executeQuery(query);
            while (rs.next()) {
                HoSuDung user = new HoSuDung();
                user.setID_HoSuDung(rs.getInt(qlnIDName.HoSuDungID));
                user.setKhuVuc(rs.getString(qlnHoSuDungCol.KhuVuc));
                user.setDiaChi(rs.getString(qlnHoSuDungCol.DiaChi));
                user.setTrangThai(rs.getInt(qlnHoSuDungCol.TrangThai));
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
                qlnTableName.HoSuDung, qlnIDName.CustomerID, qlnHoSuDungCol.DiaChi,
                qlnHoSuDungCol.KhuVuc, qlnHoSuDungCol.TrangThai,
                user.getID_Customer(), user.getDiaChi(), user.getKhuVuc(), user.getTrangThai());

        try {
            result = ConnectQLN.executeUpdate(query);
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
                qlnTableName.HoSuDung,
                qlnIDName.CustomerID, user.getID_Customer(),
                qlnHoSuDungCol.DiaChi, user.getDiaChi(),
                qlnHoSuDungCol.KhuVuc, user.getKhuVuc(),
                qlnHoSuDungCol.TrangThai, user.getTrangThai(),
                qlnIDName.HoSuDungID, user.getID_HoSuDung());

        try {
            result = ConnectQLN.executeUpdate(query);
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
                qlnTableName.HoSuDung,
                qlnIDName.HoSuDungID,
                id_HoSuDung);
        try {
            result = ConnectQLN.executeUpdate(query);
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
                    .formatted(qlnTableName.HoSuDung, qlnIDName.HoSuDungID, idHoSuDung);
            ResultSet rs = ConnectQLN.executeQuery(query);
            if (rs.next()) {
                hoSuDung = new HoSuDung();
                hoSuDung.setID_HoSuDung(rs.getInt(qlnIDName.HoSuDungID));
                hoSuDung.setID_Customer(rs.getInt(qlnIDName.CustomerID));
                hoSuDung.setKhuVuc(rs.getString(qlnHoSuDungCol.KhuVuc));
                hoSuDung.setDiaChi(rs.getString(qlnHoSuDungCol.DiaChi));
                hoSuDung.setTrangThai(rs.getInt(qlnHoSuDungCol.TrangThai));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy hộ sử dụng: " + e.getMessage());
        }
        return hoSuDung;
    }
}
