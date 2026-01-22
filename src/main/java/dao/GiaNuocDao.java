package dao;

import java.sql.ResultSet;
import java.util.ArrayList;

import model.GiaNuoc;
import model.WaterPriceTier;

import static dao.MySQLConnect.ConnectQLN;
import static dao.QLNdbConstants.*;
import static dao.SharesDao.sharesDao;

public class GiaNuocDao {

    // Danh Sach Gia Nuoc
    public ArrayList<GiaNuoc> getGiaNuoc() {
        ArrayList<GiaNuoc> listGia = new ArrayList<GiaNuoc>();

        try {
            String query = "SELECT * FROM %s"
                    .formatted(Tables.GiaNuoc);
            ResultSet rs = ConnectQLN.executeQuery(query);
            while (rs.next()) {
                GiaNuoc giaNuoc = new GiaNuoc();
                giaNuoc.setIdDonGia(rs.getInt(Id.GiaNuocID));
                giaNuoc.setIdLoaiCustomer(rs.getInt(Id.CustomerTypeID));
                giaNuoc.setKhuVuc(rs.getString(GiaNuocCol.KhuVuc));
                giaNuoc.setThue(rs.getDouble(GiaNuocCol.Thue));
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
            String query = "SELECT * FROM %s WHERE %s = %d ORDER BY %s"
                    .formatted(
                            Tables.BacGia,
                            Id.GiaNuocID,
                            idGiaNuoc,
                            BacGiaCol.BacGia);

            ResultSet rs = ConnectQLN.executeQuery(query);

            while (rs.next()) {
                WaterPriceTier bacGiaNuoc = new WaterPriceTier();

                // Set đầy đủ tất cả các field
                bacGiaNuoc.setIdWaterPriceTier(rs.getInt(Id.BacGiaID));
                bacGiaNuoc.setIdWaterPrice(rs.getInt(Id.GiaNuocID));
                bacGiaNuoc.setTier(rs.getInt(BacGiaCol.BacGia));
                bacGiaNuoc.setMinConsumption(rs.getInt(BacGiaCol.TuMucNuoc));
                bacGiaNuoc.setMaxConsumption(rs.getInt(BacGiaCol.DenMucNuoc));
                bacGiaNuoc.setPrice(rs.getDouble(BacGiaCol.Gia));

                listBac.add(bacGiaNuoc);
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách bậc giá nước: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return listBac;
    }

    // Thêm giá nước
    public boolean addGiaNuoc(GiaNuoc giaNuoc) {
        int result = 0;
        String query = String.format(java.util.Locale.US,
                "INSERT INTO %s (%s, %s, %s) VALUES (%d, '%s', %.2f)",
                Tables.GiaNuoc,
                Id.CustomerTypeID, GiaNuocCol.KhuVuc, GiaNuocCol.Thue,
                giaNuoc.getIdLoaiCustomer(), giaNuoc.getKhuVuc(), giaNuoc.getThue());

        System.out.println("=== SQL INSERT ===");
        System.out.println(query);
        System.out.println("==================");

        try {
            result = ConnectQLN.executeUpdate(query);
            System.out.println("Số dòng thêm: " + result);
        } catch (Exception e) {
            System.out.println("Lỗi thêm giá nước: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    // cập nhật giá nước
    public boolean updateGiaNuoc(GiaNuoc giaNuoc) {
        int result = 0;
        String query = String.format(java.util.Locale.US,
                "UPDATE %s SET %s = %d, %s = '%s', %s = %.2f WHERE %s = %d",
                Tables.GiaNuoc,
                Id.CustomerTypeID, giaNuoc.getIdLoaiCustomer(),
                GiaNuocCol.KhuVuc, giaNuoc.getKhuVuc(),
                GiaNuocCol.Thue, giaNuoc.getThue(),
                Id.GiaNuocID, giaNuoc.getIdDonGia());

        System.out.println("=== SQL UPDATE ===");
        System.out.println(query);
        System.out.println("==================");

        try {
            result = ConnectQLN.executeUpdate(query);
            System.out.println("Số dòng cập nhật: " + result);
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật giá nước: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result > 0;
    }

    // xóa giá nước
    public boolean deleteGiaNuocById(int id) {
        return sharesDao.deleteByCol(id, Tables.GiaNuoc, Id.GiaNuocID);
    }
}