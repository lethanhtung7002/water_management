package dao;

import java.sql.ResultSet;
import java.util.ArrayList;

import model.Customer;
import model.GiaNuoc;

public class GiaNuocDao {
    

    // Danh Sach Gia Nuoc
    public ArrayList<GiaNuoc> getGiaNuoc(){
        ArrayList<GiaNuoc> listGia = new ArrayList<GiaNuoc>();

        try {
            String query = "SELECT * FORM %s"
                .formatted(qlnTableName.GiaNuoc);
            ResultSet rs = MySQLConnect.Connect.executeQuery(query);
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

    // Thêm giá nước
    public boolean addGiaNuoc(GiaNuoc giaNuoc){
        int result = 0;
        String query = """
                INSERT INTO %s (%s, %s, %s, %s)
                VALUES ('%d', '%s', '%s', '%s')
                """.formatted(
                qlnTableName.GiaNuoc, qlnIDName.CustomerTypeID, qlnGiaNuocCol.KhuVuc, qlnGiaNuocCol.Thue,
                giaNuoc.getIdLoaiCustomer(), giaNuoc.getKhuVuc(), giaNuoc.getThue());
        try {
            result = MySQLConnect.Connect.executeUpdate(query);
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
                        %s = '%s',
                        %s = '%s',
                        %s = '%s',
                        %s = '%s'
                WHERE %s = %d""".formatted(
                qlnTableName.GiaNuoc,
                qlnIDName.CustomerTypeID, giaNuoc.getIdLoaiCustomer(),
                qlnGiaNuocCol.KhuVuc, giaNuoc.getKhuVuc(),
                qlnGiaNuocCol.Thue, giaNuoc.getThue(),
                qlnIDName.GiaNuocID, giaNuoc.getIdDonGia());
        System.out.println(query);
        try {
            result = MySQLConnect.Connect.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật giá nước: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result > 0;
    }
}
