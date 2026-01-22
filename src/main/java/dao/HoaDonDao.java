package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.HoaDon;
import static dao.MySQLConnect.ConnectQLN;
import static dao.QLNdbConstants.*;
import static dao.SharesDao.sharesDao;

/**
 * Data Access Object cho bảng hóa đơn
 */
public class HoaDonDao {

    /**
     * Lấy hóa đơn theo ID chỉ số
     * 
     * @param idChiSo ID của chỉ số nước
     * @return HoaDon hoặc null nếu chưa có
     */
    public HoaDon getHoaDonByChiSoId(int idChiSo) {
        HoaDon hoaDon = null;

        try {
            String query = "SELECT * FROM %s WHERE %s = %d"
                    .formatted(Tables.HoaDon, Id.ChiSoNuocID, idChiSo);

            ResultSet rs = ConnectQLN.executeQuery(query);

            if (rs.next()) {
                hoaDon = new HoaDon();
                hoaDon.setIdHoaDon(rs.getInt(Id.HoaDonID));
                hoaDon.setIdChiSo(rs.getInt(Id.ChiSoNuocID));
                hoaDon.setSanLuongTieuThu(rs.getInt(HoaDonCol.SoNuocTieuThu));
                hoaDon.setTongTienThanhToan(rs.getDouble(HoaDonCol.TongTien));
                hoaDon.setNgayLapHoaDon(rs.getString(HoaDonCol.NgayLap));
                hoaDon.setTrangThaiHoaDon(rs.getInt(HoaDonCol.TrangThai));
                hoaDon.setIdDonGia(rs.getInt(Id.GiaNuocID));
            }

        } catch (SQLException e) {
            System.out.println("Lỗi lấy hóa đơn theo chỉ số: " + e.getMessage());
        }

        return hoaDon;
    }

    /**
     * Thêm hóa đơn mới
     * 
     * @param hoaDon Đối tượng HoaDon cần thêm
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean addHoaDon(HoaDon hoaDon) {
        int result = 0;

        String query = String.format(java.util.Locale.US,
                """
                        INSERT INTO %s (%s, %s, %s, %s, %s, %s)
                        VALUES (%d, %d, %.2f, '%s', %d, %d)
                        """,
                Tables.HoaDon,
                Id.ChiSoNuocID,
                HoaDonCol.SoNuocTieuThu,
                HoaDonCol.TongTien,
                HoaDonCol.NgayLap,
                HoaDonCol.TrangThai,
                Id.GiaNuocID,

                hoaDon.getIdChiSo(),
                hoaDon.getSanLuongTieuThu(),
                hoaDon.getTongTienThanhToan(),
                hoaDon.getNgayLapHoaDon(),
                hoaDon.getTrangThaiHoaDon(),
                hoaDon.getIdDonGia());

        try {
            result = ConnectQLN.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi thêm hóa đơn: " + e.getMessage());
        }

        return result > 0;
    }

    /**
     * Cập nhật trạng thái hóa đơn
     * 
     * @param idHoaDon  ID hóa đơn
     * @param trangThai Trạng thái mới (0: Chưa TT, 1: Đã TT)
     * @return true nếu thành công
     */
    public boolean updateTrangThai(int idHoaDon, int trangThai) {
        int result = 0;

        String query = "UPDATE %s SET %s = %d WHERE %s = %d"
                .formatted(
                        Tables.HoaDon,
                        HoaDonCol.TrangThai, trangThai,
                        Id.HoaDonID, idHoaDon);

        try {
            result = ConnectQLN.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật trạng thái hóa đơn: " + e.getMessage());
        }

        return result > 0;
    }

    /**
     * Xóa hóa đơn
     * 
     * @param idHoaDon ID hóa đơn cần xóa
     * @return true nếu thành công
     */
    public boolean deleteHoaDon(int idHoaDon) {
        return sharesDao.deleteByCol(idHoaDon, Tables.HoaDon, Id.HoaDonID);
    }
}