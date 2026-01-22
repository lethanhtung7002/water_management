package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.ChiSoNuoc;
import static dao.MySQLConnect.ConnectQLN;
import static dao.QLNdbConstants.*;
import static dao.SharesDao.sharesDao;

/**
 * Data Access Object cho bảng chỉ số nước - theo schema thực tế
 * 
 * Cung cấp các phương thức CRUD để thao tác với chỉ số nước
 */
public class ChiSoNuocDao {

    /**
     * Lấy danh sách chỉ số nước theo ID hộ sử dụng
     * 
     * @param idHoSuDung ID của hộ sử dụng
     * @return ArrayList chứa các chỉ số nước
     */
    public ArrayList<ChiSoNuoc> getChiSoNuocByHoSuDungId(int idHoSuDung) {
        ArrayList<ChiSoNuoc> listChiSo = new ArrayList<>();

        try {
            // Sắp xếp theo năm, tháng, ngày giảm dần (mới nhất lên trước)
            String query = """
                    SELECT * FROM %s
                    WHERE %s = %d
                    ORDER BY %s DESC, %s DESC, %s DESC
                    """.formatted(
                    Tables.ChiSoNuoc,
                    Id.HoSuDungID,
                    idHoSuDung,
                    ChiSoNuocCol.NamGhi,
                    ChiSoNuocCol.ThangGhi,
                    ChiSoNuocCol.NgayGhi);

            ResultSet rs = ConnectQLN.executeQuery(query);

            while (rs.next()) {
                ChiSoNuoc chiSo = new ChiSoNuoc();
                chiSo.setIdChiSo(rs.getInt(Id.ChiSoNuocID));
                chiSo.setIdHoSuDung(rs.getInt(Id.HoSuDungID));
                chiSo.setNamGhi(rs.getInt(ChiSoNuocCol.NamGhi));
                chiSo.setThangGhi(rs.getInt(ChiSoNuocCol.ThangGhi));
                chiSo.setNgayGhi(rs.getInt(ChiSoNuocCol.NgayGhi));
                chiSo.setChiSoMoi(rs.getInt(ChiSoNuocCol.ChiSoMoi));
                chiSo.setChiSoCu(rs.getInt(ChiSoNuocCol.ChiSoCu));

                listChiSo.add(chiSo);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách chỉ số nước: " + e.getMessage());
            e.printStackTrace();
        }

        return listChiSo;
    }

    /**
     * Lấy chỉ số nước mới nhất của một hộ sử dụng
     * 
     * @param idHoSuDung ID của hộ sử dụng
     * @return ChiSoNuoc mới nhất hoặc null nếu chưa có
     */
    public ChiSoNuoc getLatestChiSo(int idHoSuDung) {
        ChiSoNuoc chiSo = null;

        try {
            String query = """
                    SELECT * FROM %s
                    WHERE %s = %d
                    ORDER BY %s DESC, %s DESC, %s DESC
                    LIMIT 1
                    """.formatted(
                    Tables.ChiSoNuoc,
                    Id.HoSuDungID,
                    idHoSuDung,
                    ChiSoNuocCol.NamGhi,
                    ChiSoNuocCol.ThangGhi,
                    ChiSoNuocCol.NgayGhi);

            ResultSet rs = ConnectQLN.executeQuery(query);

            if (rs.next()) {
                chiSo = new ChiSoNuoc();
                chiSo.setIdChiSo(rs.getInt(Id.ChiSoNuocID));
                chiSo.setIdHoSuDung(rs.getInt(Id.HoSuDungID));
                chiSo.setNamGhi(rs.getInt(ChiSoNuocCol.NamGhi));
                chiSo.setThangGhi(rs.getInt(ChiSoNuocCol.ThangGhi));
                chiSo.setNgayGhi(rs.getInt(ChiSoNuocCol.NgayGhi));
                chiSo.setChiSoMoi(rs.getInt(ChiSoNuocCol.ChiSoMoi));
                chiSo.setChiSoCu(rs.getInt(ChiSoNuocCol.ChiSoCu));
            }

        } catch (SQLException e) {
            System.out.println("Lỗi lấy chỉ số nước mới nhất: " + e.getMessage());
            e.printStackTrace();
        }

        return chiSo;
    }

    /**
     * Thêm chỉ số nước mới
     * 
     * @param chiSo Đối tượng ChiSoNuoc cần thêm
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean addChiSoNuoc(ChiSoNuoc chiSo) {
        int result = 0;

        String query = """
                INSERT INTO %s (%s, %s, %s, %s, %s, %s)
                VALUES (%d, %d, %d, %d, %d, %d)
                """.formatted(
                Tables.ChiSoNuoc,
                Id.HoSuDungID,
                ChiSoNuocCol.NamGhi,
                ChiSoNuocCol.ThangGhi,
                ChiSoNuocCol.NgayGhi,
                ChiSoNuocCol.ChiSoMoi,
                ChiSoNuocCol.ChiSoCu,
                chiSo.getIdHoSuDung(),
                chiSo.getNamGhi(),
                chiSo.getThangGhi(),
                chiSo.getNgayGhi(),
                chiSo.getChiSoMoi(),
                chiSo.getChiSoCu());

        System.out.println("=== SQL INSERT CHI SO NUOC ===");
        System.out.println(query);
        System.out.println("==============================");

        try {
            result = ConnectQLN.executeUpdate(query);
            System.out.println("Số dòng thêm: " + result);
        } catch (Exception e) {
            System.out.println("Lỗi thêm chỉ số nước: " + e.getMessage());
            e.printStackTrace();
        }

        return result > 0;
    }

    /**
     * Cập nhật chỉ số nước
     * 
     * @param chiSo Đối tượng ChiSoNuoc cần cập nhật
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean updateChiSoNuoc(ChiSoNuoc chiSo) {
        int result = 0;

        String query = """
                UPDATE %s
                SET %s = %d, %s = %d, %s = %d, %s = %d, %s = %d, %s = %d
                WHERE %s = %d
                """.formatted(
                Tables.ChiSoNuoc,
                Id.HoSuDungID, chiSo.getIdHoSuDung(),
                ChiSoNuocCol.NamGhi, chiSo.getNamGhi(),
                ChiSoNuocCol.ThangGhi, chiSo.getThangGhi(),
                ChiSoNuocCol.NgayGhi, chiSo.getNgayGhi(),
                ChiSoNuocCol.ChiSoMoi, chiSo.getChiSoMoi(),
                ChiSoNuocCol.ChiSoCu, chiSo.getChiSoCu(),
                Id.ChiSoNuocID, chiSo.getIdChiSo());

        try {
            result = ConnectQLN.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật chỉ số nước: " + e.getMessage());
            e.printStackTrace();
        }

        return result > 0;
    }

    /**
     * Xóa chỉ số nước
     * 
     * @param idChiSo ID của chỉ số nước cần xóa
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean deleteChiSoNuoc(int idChiSo) {
        return sharesDao.deleteByCol(idChiSo, Tables.ChiSoNuoc, Id.ChiSoNuocID);
    }

    /**
     * Lấy chỉ số nước theo ID
     * 
     * @param idChiSo ID của chỉ số nước
     * @return ChiSoNuoc hoặc null nếu không tìm thấy
     */
    public ChiSoNuoc getChiSoById(int idChiSo) {
        ChiSoNuoc chiSo = null;

        try {
            String query = "SELECT * FROM %s WHERE %s = %d"
                    .formatted(Tables.ChiSoNuoc, Id.ChiSoNuocID, idChiSo);

            ResultSet rs = ConnectQLN.executeQuery(query);

            if (rs.next()) {
                chiSo = new ChiSoNuoc();
                chiSo.setIdChiSo(rs.getInt(Id.ChiSoNuocID));
                chiSo.setIdHoSuDung(rs.getInt(Id.HoSuDungID));
                chiSo.setNamGhi(rs.getInt(ChiSoNuocCol.NamGhi));
                chiSo.setThangGhi(rs.getInt(ChiSoNuocCol.ThangGhi));
                chiSo.setNgayGhi(rs.getInt(ChiSoNuocCol.NgayGhi));
                chiSo.setChiSoMoi(rs.getInt(ChiSoNuocCol.ChiSoMoi));
                chiSo.setChiSoCu(rs.getInt(ChiSoNuocCol.ChiSoCu));
            }

        } catch (SQLException e) {
            System.out.println("Lỗi lấy chỉ số nước theo ID: " + e.getMessage());
            e.printStackTrace();
        }

        return chiSo;
    }
}