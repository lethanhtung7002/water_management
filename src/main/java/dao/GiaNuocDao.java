package dao;

import java.sql.*;
import java.util.ArrayList;

import model.*;

public class GiaNuocDao {
    private MySQLConnect connect = new MySQLConnect();

    // Danh Sach Gia Nuoc
    public ArrayList<GiaNuoc> getGiaNuoc(){
        ArrayList<GiaNuoc> listGia = new ArrayList<GiaNuoc>();

        try {
            String query = "SECLECT * FORM %s WHERE $s "
                .formatted();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return listGia;
    }
}
