package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.HoaDon;
import static dao.MySQLConnect.ConnectQLN;

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
                    .formatted(qlnTableName.HoaDon, qlnIDName.ChiSoNuocID, idChiSo);

            ResultSet rs = ConnectQLN.executeQuery(query);

            if (rs.next()) {
                hoaDon = new HoaDon();
                hoaDon.setIdHoaDon(rs.getInt(qlnIDName.HoaDonID));
                hoaDon.setIdChiSo(rs.getInt(qlnIDName.ChiSoNuocID));
                hoaDon.setSanLuongTieuThu(rs.getInt(qlnHoaDonCol.SoNuocTieuThu));
                hoaDon.setTongTienThanhToan(rs.getDouble(qlnHoaDonCol.TongTien));
                hoaDon.setNgayLapHoaDon(rs.getString(qlnHoaDonCol.NgayLap));
                hoaDon.setTrangThaiHoaDon(rs.getInt(qlnHoaDonCol.TrangThai));
                hoaDon.setIdDonGia(rs.getInt(qlnIDName.GiaNuocID));
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
                qlnTableName.HoaDon,
                qlnIDName.ChiSoNuocID,
                qlnHoaDonCol.SoNuocTieuThu,
                qlnHoaDonCol.TongTien,
                qlnHoaDonCol.NgayLap,
                qlnHoaDonCol.TrangThai,
                qlnIDName.GiaNuocID,

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
                        qlnTableName.HoaDon,
                        qlnHoaDonCol.TrangThai, trangThai,
                        qlnIDName.HoaDonID, idHoaDon);

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
        int result = 0;

        String query = "DELETE FROM %s WHERE %s = %d"
                .formatted(qlnTableName.HoaDon, qlnIDName.HoaDonID, idHoaDon);

        try {
            result = ConnectQLN.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi xóa hóa đơn: " + e.getMessage());
        }

        return result > 0;
    }
}