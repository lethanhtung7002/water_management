package dao;

import java.sql.ResultSet;
import java.util.ArrayList;

import model.GiaNuoc;
import model.WaterPriceTier;

import static dao.MySQLConnect.ConnectQLN;

public class GiaNuocDao {

    // Danh Sach Gia Nuoc
    public ArrayList<GiaNuoc> getGiaNuoc() {
        ArrayList<GiaNuoc> listGia = new ArrayList<GiaNuoc>();

        try {
            String query = "SELECT * FROM %s"
                    .formatted(qlnTableName.GiaNuoc);
            ResultSet rs = ConnectQLN.executeQuery(query);
            while (rs.next()) {
                GiaNuoc giaNuoc = new GiaNuoc();
                giaNuoc.setIdDonGia(rs.getInt(qlnIDName.GiaNuocID));
                giaNuoc.setIdLoaiCustomer(rs.getInt(qlnIDName.CustomerTypeID));
                giaNuoc.setKhuVuc(rs.getString(qlnGiaNuocCol.KhuVuc));
                giaNuoc.setThue(rs.getDouble(qlnGiaNuocCol.Thue));
                listGia.add(giaNuoc);
            }

        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách giá nước: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return listGia;
    }

    // Lấy bậc giá nước theo id giá nước
    public ArrayList<WaterPriceTier> getBacGiaNuocByIdGiaNuoc(int idGiaNuoc) {
        ArrayList<WaterPriceTier> listBac = new ArrayList<WaterPriceTier>();
        try {
            String query = "SELECT * FROM %s WHERE %s = %d"
                    .formatted(qlnTableName.BacGia, qlnIDName.GiaNuocID, idGiaNuoc);
            ResultSet rs = ConnectQLN.executeQuery(query);
            while (rs.next()) {
                WaterPriceTier bacGiaNuoc = new WaterPriceTier();
                bacGiaNuoc.setTier(rs.getInt(qlnIDName.BacGiaID));
                bacGiaNuoc.setMinConsumption(rs.getInt(qlnBacGiaCol.TuMucNuoc));
                bacGiaNuoc.setMaxConsumption(rs.getInt(qlnBacGiaCol.DenMucNuoc));
                bacGiaNuoc.setPrice(rs.getDouble(qlnBacGiaCol.Gia));
                listBac.add(bacGiaNuoc);
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách bậc giá nước: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return listBac;
    }

    // Thêm giá nước
    public boolean addGiaNuoc(GiaNuoc giaNuoc) {
        int result = 0;
        String query = """
                INSERT INTO %s (%s, %s, %s)
                VALUES (%d, '%s', %f)     // ✅ BỎ dấu nháy ở %d và %f
                """.formatted(
                qlnTableName.GiaNuoc,
                qlnIDName.CustomerTypeID, qlnGiaNuocCol.KhuVuc, qlnGiaNuocCol.Thue,
                giaNuoc.getIdLoaiCustomer(), giaNuoc.getKhuVuc(), giaNuoc.getThue());
        try {
            result = ConnectQLN.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi thêm giá nước: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    // cập nhật người dùng
    public boolean updateGiaNuoc(GiaNuoc giaNuoc) {
        int result = 0;
        String query = """
                UPDATE %s SET
                        %s = %d,
                        %s = '%s',
                        %s = %f
                WHERE %s = %d""".formatted(
                qlnTableName.GiaNuoc,
                qlnIDName.CustomerTypeID, giaNuoc.getIdLoaiCustomer(),
                qlnGiaNuocCol.KhuVuc, giaNuoc.getKhuVuc(),
                qlnGiaNuocCol.Thue, giaNuoc.getThue(),
                qlnIDName.GiaNuocID, giaNuoc.getIdDonGia());

        System.out.println("SQL Query: " + query); // Debug

        try {
            result = ConnectQLN.executeUpdate(query);
            System.out.println("Rows affected: " + result); // Debug
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật giá nước: " + e.getMessage());
            e.printStackTrace(); // In chi tiết lỗi
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    // xóa giá nước
    public boolean deleteGiaNuocById(int id) {
        int result = 0;
        String query = """
                DELETE FROM %s WHERE %s = %d""".formatted(
                qlnTableName.GiaNuoc,
                qlnIDName.GiaNuocID, id);
        try {
            result = ConnectQLN.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi xóa giá nước: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result > 0;
    }
}
