import java.time.LocalDate;

import dao.HoaDonDao;
import model.HoaDon;


public class test {

    public static void main(String[] args) {
        HoaDonDao hoaDonDao = new HoaDonDao();
        LocalDate today = LocalDate.now();

        HoaDon kq = hoaDonDao.getGiaCaoNhatTrongThang(today.getMonthValue(), today.getYear());
        if (kq != null) {
            System.out.println("ID Hóa Đơn: " + kq.getIdHoaDon());
            System.out.println("Tổng Tiền Thanh Toán: " + kq.getTongTienThanhToan());
        } else {
            System.out.println("Không tìm thấy hóa đơn nào trong tháng này.");
        }
    }
}