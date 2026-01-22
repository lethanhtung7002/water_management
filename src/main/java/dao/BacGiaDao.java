package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.WaterPriceTier;
import static dao.MySQLConnect.ConnectQLN;
import static dao.QLNdbConstants.*;
import static dao.SharesDao.sharesDao;

/**
 * Data Access Object cho bảng Bậc Giá Nước (bacgia).
 * 
 * Cung cấp các phương thức CRUD để thao tác với bậc giá nước.
 * 
 * Cấu trúc bảng:
 * - ID_Bac: ID bậc giá (auto increment)
 * - ID_DonGia: ID chính sách giá nước (foreign key)
 * - BacGia: Số thứ tự bậc (1, 2, 3...)
 * - TuMucNuoc: Từ mức nước (m³)
 * - DenMucNuoc: Đến mức nước (m³)
 * - Gia: Giá tiền (VNĐ/m³)
 * 
 */
public class BacGiaDao {

    /**
     * Lấy danh sách bậc giá nước theo ID chính sách giá.
     * 
     * @param idDonGia ID của chính sách giá nước
     * @return ArrayList chứa các bậc giá nước
     */
    public ArrayList<WaterPriceTier> getBacGiaNuocByIdDonGia(int idDonGia) {
        ArrayList<WaterPriceTier> listBac = new ArrayList<>();

        try {
            String query = "SELECT * FROM %s WHERE %s = %d ORDER BY %s"
                    .formatted(
                            Tables.BacGia,
                            Id.GiaNuocID,
                            idDonGia,
                            BacGiaCol.BacGia);

            ResultSet rs = ConnectQLN.executeQuery(query);

            while (rs.next()) {
                WaterPriceTier tier = new WaterPriceTier();
                tier.setIdWaterPriceTier(rs.getInt(Id.BacGiaID));
                tier.setIdWaterPrice(rs.getInt(Id.GiaNuocID));
                tier.setTier(rs.getInt(BacGiaCol.BacGia));
                tier.setMinConsumption(rs.getInt(BacGiaCol.TuMucNuoc));
                tier.setMaxConsumption(rs.getInt(BacGiaCol.DenMucNuoc));
                tier.setPrice(rs.getDouble(BacGiaCol.Gia));
                listBac.add(tier);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách bậc giá: " + e.getMessage());
        }

        return listBac;
    }

    /**
     * Lấy bậc giá nước theo ID.
     * 
     * @param idBac ID của bậc giá
     * @return WaterPriceTier hoặc null nếu không tìm thấy
     */
    public WaterPriceTier getBacGiaById(int idBac) {
        WaterPriceTier tier = null;

        try {
            String query = "SELECT * FROM %s WHERE %s = %d".formatted(
                    Tables.BacGia, Id.BacGiaID, idBac);

            ResultSet rs = ConnectQLN.executeQuery(query);

            if (rs.next()) {
                tier = new WaterPriceTier();
                tier.setIdWaterPriceTier(rs.getInt(Id.BacGiaID));
                tier.setIdWaterPrice(rs.getInt(Id.GiaNuocID));
                tier.setTier(rs.getInt(BacGiaCol.BacGia));
                tier.setMinConsumption(rs.getInt(BacGiaCol.TuMucNuoc));
                tier.setMaxConsumption(rs.getInt(BacGiaCol.DenMucNuoc));
                tier.setPrice(rs.getDouble(BacGiaCol.Gia));
            }

        } catch (SQLException e) {
            System.out.println("Lỗi lấy bậc giá theo ID: " + e.getMessage());
        }

        return tier;
    }

    /**
     * Lấy bậc giá cuối cùng của một chính sách giá.
     * Dùng để gợi ý mức nước cho bậc tiếp theo.
     * 
     * @param idDonGia ID của chính sách giá nước
     * @return WaterPriceTier cuối cùng hoặc null nếu chưa có bậc nào
     */
    public WaterPriceTier getLastTier(int idDonGia) {
        WaterPriceTier tier = null;

        try {
            String query = "SELECT * FROM %s WHERE %s = %d ORDER BY %s DESC LIMIT 1"
                    .formatted(
                            Tables.BacGia,
                            Id.GiaNuocID,
                            idDonGia,
                            BacGiaCol.BacGia);

            ResultSet rs = ConnectQLN.executeQuery(query);

            if (rs.next()) {
                tier = new WaterPriceTier();
                tier.setIdWaterPriceTier(rs.getInt(Id.BacGiaID));
                tier.setIdWaterPrice(rs.getInt(Id.GiaNuocID));
                tier.setTier(rs.getInt(BacGiaCol.BacGia));
                tier.setMinConsumption(rs.getInt(BacGiaCol.TuMucNuoc));
                tier.setMaxConsumption(rs.getInt(BacGiaCol.DenMucNuoc));
                tier.setPrice(rs.getDouble(BacGiaCol.Gia));
            }

        } catch (SQLException e) {
            System.out.println("Lỗi lấy bậc giá cuối: " + e.getMessage());
        }

        return tier;
    }

    /**
     * Lấy số bậc tiếp theo (tự động tăng).
     * Ví dụ: Đã có bậc 1, 2, 3 → trả về 4
     * 
     * @param idDonGia ID của chính sách giá nước
     * @return Số bậc tiếp theo
     */
    public int getNextTierNumber(int idDonGia) {
        int nextTier = 1; // Mặc định bắt đầu từ 1

        try {
            String query = "SELECT MAX(%s) as maxTier FROM %s WHERE %s = %d"
                    .formatted(
                            BacGiaCol.BacGia,
                            Tables.BacGia,
                            Id.GiaNuocID,
                            idDonGia);

            ResultSet rs = ConnectQLN.executeQuery(query);

            if (rs.next()) {
                int maxTier = rs.getInt("maxTier");
                if (maxTier > 0) {
                    nextTier = maxTier + 1;
                }
            }

        } catch (SQLException e) {
            System.out.println("Lỗi lấy số bậc tiếp theo: " + e.getMessage());
        }

        return nextTier;
    }

    /**
     * Kiểm tra khoảng mức nước có bị trùng với bậc giá khác không.
     * 
     * @param idDonGia   ID chính sách giá nước
     * @param tuMucNuoc  Từ mức nước
     * @param denMucNuoc Đến mức nước
     * @param excludeId  ID cần loại trừ (dùng khi update, -1 nếu thêm mới)
     * @return true nếu bị trùng, false nếu không trùng
     */
    public boolean isRangeOverlapping(int idDonGia, int tuMucNuoc, int denMucNuoc, int excludeId) {
        try {
            // Kiểm tra overlap: (TuMucNuoc < denMucNuoc AND DenMucNuoc > tuMucNuoc)
            String query;
            if (excludeId == -1) {
                // Thêm mới: kiểm tra tất cả
                query = """
                        SELECT COUNT(*) as count FROM %s
                        WHERE %s = %d
                        AND (
                            (%s < %d AND %s > %d) OR
                            (%s <= %d AND %s >= %d) OR
                            (%s >= %d AND %s <= %d)
                        )
                        """.formatted(
                        Tables.BacGia,
                        Id.GiaNuocID, idDonGia,
                        BacGiaCol.TuMucNuoc, denMucNuoc, BacGiaCol.DenMucNuoc, tuMucNuoc,
                        BacGiaCol.TuMucNuoc, tuMucNuoc, BacGiaCol.DenMucNuoc, tuMucNuoc,
                        BacGiaCol.TuMucNuoc, tuMucNuoc, BacGiaCol.DenMucNuoc, denMucNuoc);
            } else {
                // Update: loại trừ chính nó
                query = """
                        SELECT COUNT(*) as count FROM %s
                        WHERE %s = %d
                        AND %s != %d
                        AND (
                            (%s < %d AND %s > %d) OR
                            (%s <= %d AND %s >= %d) OR
                            (%s >= %d AND %s <= %d)
                        )
                        """.formatted(
                        Tables.BacGia,
                        Id.GiaNuocID, idDonGia,
                        Id.BacGiaID, excludeId,
                        BacGiaCol.TuMucNuoc, denMucNuoc, BacGiaCol.DenMucNuoc, tuMucNuoc,
                        BacGiaCol.TuMucNuoc, tuMucNuoc, BacGiaCol.DenMucNuoc, tuMucNuoc,
                        BacGiaCol.TuMucNuoc, tuMucNuoc, BacGiaCol.DenMucNuoc, denMucNuoc);
            }

            ResultSet rs = ConnectQLN.executeQuery(query);

            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0; // Có trùng nếu count > 0
            }

        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra trùng mức nước: " + e.getMessage());
        }

        return false;
    }

    /**
     * Thêm bậc giá nước mới.
     * 
     * @param tier Đối tượng WaterPriceTier cần thêm
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean addBacGia(WaterPriceTier tier) {
        int result = 0;

        String query = String.format(java.util.Locale.US,
                "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (%d, %d, %d, %d, %.2f)",
                Tables.BacGia,
                Id.GiaNuocID, BacGiaCol.BacGia, BacGiaCol.TuMucNuoc,
                BacGiaCol.DenMucNuoc, BacGiaCol.Gia,
                tier.getIdWaterPrice(), tier.getTier(), tier.getMinConsumption(),
                tier.getMaxConsumption(), tier.getPrice());

        try {
            result = ConnectQLN.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi thêm bậc giá: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return result > 0;
    }

    /**
     * Cập nhật bậc giá nước.
     * 
     * @param tier Đối tượng WaterPriceTier cần cập nhật
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean updateBacGia(WaterPriceTier tier) {
        int result = 0;

        String query = String.format(java.util.Locale.US,
                "UPDATE %s SET %s = %d, %s = %d, %s = %d, %s = %d, %s = %.2f WHERE %s = %d",
                Tables.BacGia,
                Id.GiaNuocID, tier.getIdWaterPrice(),
                BacGiaCol.BacGia, tier.getTier(),
                BacGiaCol.TuMucNuoc, tier.getMinConsumption(),
                BacGiaCol.DenMucNuoc, tier.getMaxConsumption(),
                BacGiaCol.Gia, tier.getPrice(),
                Id.BacGiaID, tier.getIdWaterPriceTier());

        try {
            result = ConnectQLN.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật bậc giá: " + e.getMessage());
        }

        return result > 0;
    }

    /**
     * Xóa bậc giá nước theo ID.
     * 
     * @param idBac ID của bậc giá cần xóa
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean deleteBacGiaById(int idBac) {
        return sharesDao.deleteByCol(idBac, Tables.BacGia, Id.BacGiaID);
    }
}