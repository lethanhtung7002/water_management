package dao;

import static dao.MySQLConnect.ConnectQLN;
import static dao.SharesDao.sharesDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.QLNdbConstants.HoSuDungCol;
import dao.QLNdbConstants.Id;
import dao.QLNdbConstants.Tables;
import model.HoSuDung;

public class HoSuDungDao {

    // lấy danh sách hộ sử dụng
    public ArrayList<HoSuDung> getHoSuDungByCustomerId(int customerId) {
        ArrayList<HoSuDung> userList = new ArrayList<>();
        try {
            String query = "SELECT * FROM %s WHERE %s = %d"
                    .formatted(Tables.HoSuDung,
                            Id.CustomerID, customerId);
            ResultSet rs = ConnectQLN.executeQuery(query);
            while (rs.next()) {
                HoSuDung user = new HoSuDung();
                user.setID_HoSuDung(rs.getInt(Id.HoSuDungID));
                user.setKhuVuc(rs.getString(HoSuDungCol.KhuVuc));
                user.setDiaChi(rs.getString(HoSuDungCol.DiaChi));
                user.setTrangThai(rs.getInt(HoSuDungCol.TrangThai));
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
                Tables.HoSuDung, Id.CustomerID, HoSuDungCol.DiaChi,
                HoSuDungCol.KhuVuc, HoSuDungCol.TrangThai,
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
                Tables.HoSuDung,
                Id.CustomerID, user.getID_Customer(),
                HoSuDungCol.DiaChi, user.getDiaChi(),
                HoSuDungCol.KhuVuc, user.getKhuVuc(),
                HoSuDungCol.TrangThai, user.getTrangThai(),
                Id.HoSuDungID, user.getID_HoSuDung());

        try {
            result = ConnectQLN.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật hộ sử dụng: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    // tìm hộ sử dụng theo ID
    public HoSuDung getHoSuDungById(int idHoSuDung) {
        HoSuDung hoSuDung = null;
        try {
            String query = "SELECT * FROM %s WHERE %s = %d"
                    .formatted(Tables.HoSuDung, Id.HoSuDungID, idHoSuDung);
            ResultSet rs = ConnectQLN.executeQuery(query);
            if (rs.next()) {
                hoSuDung = new HoSuDung();
                hoSuDung.setID_HoSuDung(rs.getInt(Id.HoSuDungID));
                hoSuDung.setID_Customer(rs.getInt(Id.CustomerID));
                hoSuDung.setKhuVuc(rs.getString(HoSuDungCol.KhuVuc));
                hoSuDung.setDiaChi(rs.getString(HoSuDungCol.DiaChi));
                hoSuDung.setTrangThai(rs.getInt(HoSuDungCol.TrangThai));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy hộ sử dụng: " + e.getMessage());
        }
        return hoSuDung;
    }

    public boolean delete_HoSuDung(int idHoSuDung) {
        return sharesDao.deleteByCol(idHoSuDung, Tables.HoSuDung, Id.HoSuDungID);
    }
}
