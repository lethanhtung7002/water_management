package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.WaterPriceTier;
import static dao.MySQLConnect.ConnectQLN;

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
 * @author Lê Thanh Tùng
 * @version 1.0
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
                            qlnTableName.BacGia,
                            qlnIDName.GiaNuocID,
                            idDonGia,
                            qlnBacGiaCol.BacGia);

            ResultSet rs = ConnectQLN.executeQuery(query);

            while (rs.next()) {
                WaterPriceTier tier = new WaterPriceTier();
                tier.setIdWaterPriceTier(rs.getInt(qlnIDName.BacGiaID));
                tier.setIdWaterPrice(rs.getInt(qlnIDName.GiaNuocID));
                tier.setTier(rs.getInt(qlnBacGiaCol.BacGia));
                tier.setMinConsumption(rs.getInt(qlnBacGiaCol.TuMucNuoc));
                tier.setMaxConsumption(rs.getInt(qlnBacGiaCol.DenMucNuoc));
                tier.setPrice(rs.getDouble(qlnBacGiaCol.Gia));
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
            String query = "SELECT * FROM %s WHERE %s = %d"
                    .formatted(qlnTableName.BacGia, qlnIDName.BacGiaID, idBac);

            ResultSet rs = ConnectQLN.executeQuery(query);

            if (rs.next()) {
                tier = new WaterPriceTier();
                tier.setIdWaterPriceTier(rs.getInt(qlnIDName.BacGiaID));
                tier.setIdWaterPrice(rs.getInt(qlnIDName.GiaNuocID));
                tier.setTier(rs.getInt(qlnBacGiaCol.BacGia));
                tier.setMinConsumption(rs.getInt(qlnBacGiaCol.TuMucNuoc));
                tier.setMaxConsumption(rs.getInt(qlnBacGiaCol.DenMucNuoc));
                tier.setPrice(rs.getDouble(qlnBacGiaCol.Gia));
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
                            qlnTableName.BacGia,
                            qlnIDName.GiaNuocID,
                            idDonGia,
                            qlnBacGiaCol.BacGia);

            ResultSet rs = ConnectQLN.executeQuery(query);

            if (rs.next()) {
                tier = new WaterPriceTier();
                tier.setIdWaterPriceTier(rs.getInt(qlnIDName.BacGiaID));
                tier.setIdWaterPrice(rs.getInt(qlnIDName.GiaNuocID));
                tier.setTier(rs.getInt(qlnBacGiaCol.BacGia));
                tier.setMinConsumption(rs.getInt(qlnBacGiaCol.TuMucNuoc));
                tier.setMaxConsumption(rs.getInt(qlnBacGiaCol.DenMucNuoc));
                tier.setPrice(rs.getDouble(qlnBacGiaCol.Gia));
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
                            qlnBacGiaCol.BacGia,
                            qlnTableName.BacGia,
                            qlnIDName.GiaNuocID,
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
                        qlnTableName.BacGia,
                        qlnIDName.GiaNuocID, idDonGia,
                        qlnBacGiaCol.TuMucNuoc, denMucNuoc, qlnBacGiaCol.DenMucNuoc, tuMucNuoc,
                        qlnBacGiaCol.TuMucNuoc, tuMucNuoc, qlnBacGiaCol.DenMucNuoc, tuMucNuoc,
                        qlnBacGiaCol.TuMucNuoc, tuMucNuoc, qlnBacGiaCol.DenMucNuoc, denMucNuoc);
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
                        qlnTableName.BacGia,
                        qlnIDName.GiaNuocID, idDonGia,
                        qlnIDName.BacGiaID, excludeId,
                        qlnBacGiaCol.TuMucNuoc, denMucNuoc, qlnBacGiaCol.DenMucNuoc, tuMucNuoc,
                        qlnBacGiaCol.TuMucNuoc, tuMucNuoc, qlnBacGiaCol.DenMucNuoc, tuMucNuoc,
                        qlnBacGiaCol.TuMucNuoc, tuMucNuoc, qlnBacGiaCol.DenMucNuoc, denMucNuoc);
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
                qlnTableName.BacGia,
                qlnIDName.GiaNuocID, qlnBacGiaCol.BacGia, qlnBacGiaCol.TuMucNuoc,
                qlnBacGiaCol.DenMucNuoc, qlnBacGiaCol.Gia,
                tier.getIdWaterPrice(), tier.getTier(), tier.getMinConsumption(),
                tier.getMaxConsumption(), tier.getPrice());

        System.out.println("=== SQL INSERT TIER ===");
        System.out.println(query);
        System.out.println("=======================");

        try {
            result = ConnectQLN.executeUpdate(query);
            System.out.println("Số dòng thêm: " + result);
        } catch (Exception e) {
            System.out.println("Lỗi thêm bậc giá: " + e.getMessage());
            e.printStackTrace();
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
                qlnTableName.BacGia,
                qlnIDName.GiaNuocID, tier.getIdWaterPrice(),
                qlnBacGiaCol.BacGia, tier.getTier(),
                qlnBacGiaCol.TuMucNuoc, tier.getMinConsumption(),
                qlnBacGiaCol.DenMucNuoc, tier.getMaxConsumption(),
                qlnBacGiaCol.Gia, tier.getPrice(),
                qlnIDName.BacGiaID, tier.getIdWaterPriceTier());

        System.out.println("=== SQL UPDATE TIER ===");
        System.out.println(query);
        System.out.println("=======================");

        try {
            result = ConnectQLN.executeUpdate(query);
            System.out.println("Số dòng cập nhật: " + result);
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
        int result = 0;

        String query = "DELETE FROM %s WHERE %s = %d"
                .formatted(qlnTableName.BacGia, qlnIDName.BacGiaID, idBac);

        System.out.println("=== SQL DELETE TIER ===");
        System.out.println(query);
        System.out.println("=======================");

        try {
            result = ConnectQLN.executeUpdate(query);
            System.out.println("Số dòng xóa: " + result);
        } catch (Exception e) {
            System.out.println("Lỗi xóa bậc giá: " + e.getMessage());
        }

        return result > 0;
    }

    /**
     * Xóa tất cả bậc giá của một chính sách giá nước.
     * 
     * @param idDonGia ID của chính sách giá nước
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean deleteAllBacGiaByDonGiaId(int idDonGia) {
        int result = 0;

        String query = "DELETE FROM %s WHERE %s = %d"
                .formatted(qlnTableName.BacGia, qlnIDName.GiaNuocID, idDonGia);

        try {
            result = ConnectQLN.executeUpdate(query);
            System.out.println("Đã xóa " + result + " bậc giá");
        } catch (Exception e) {
            System.out.println("Lỗi xóa tất cả bậc giá: " + e.getMessage());
        }

        return result >= 0; // >= 0 vì có thể không có bậc giá nào
    }
}