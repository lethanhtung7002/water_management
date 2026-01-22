package dao;

import static dao.MySQLConnect.ConnectQLN;

public class SharesDao {

    public static SharesDao sharesDao = new SharesDao();

    public SharesDao() {
    }

    public boolean deleteByCol(int Value, String TableName, String ColName) {
        int result = 0;
        String query = "DELETE FROM %s WHERE %s = '%d'"
                .formatted(TableName, ColName, Value);
        System.out.println(query);
        try {
            result = ConnectQLN.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Lỗi xóa dữ liệu: " + e.getMessage());
        }
        return result > 0;
    }
}
