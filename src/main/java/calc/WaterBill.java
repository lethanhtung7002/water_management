package calc;

import java.util.Arrays;

/**
 * Class tính tiền nước theo bậc thang (logic chuẩn, không lệch m³)
 *
 * Ví dụ: 25 m³
 * - Bậc 1 (1–10): 10 × 5,000 = 50,000
 * - Bậc 2 (11–20): 10 × 6,000 = 60,000
 * - Bậc 3 (21–∞): 5 × 7,000 = 35,000
 * Tổng: 145,000
 * Thuế 10%: 14,500
 * Thành tiền: 159,500
 */
public class WaterBill {

    /**
     * Tính tổng tiền nước theo bậc thang
     *
     * @param mucNuocSD  Tổng số m³ nước sử dụng
     * @param tuMucNuoc  Mốc bắt đầu mỗi bậc (bao gồm)
     * @param denMucNuoc Mốc kết thúc mỗi bậc (bao gồm)
     * @param gia        Giá mỗi bậc (VNĐ/m³)
     * @param thue       Thuế (1.1 = 10%, 1.05 = 5%)
     * @return Tổng tiền đã bao gồm thuế
     */
    public static double calculateTotalMoney(
            int mucNuocSD,
            int[] tuMucNuoc,
            int[] denMucNuoc,
            double[] gia,
            double thueHeSo) {

        double totalMoney = 0;
        int nuocDaTinh = 0; // Theo dõi số nước đã được phân bổ vào các bậc trước

        for (int i = 0; i < gia.length; i++) {
            if (mucNuocSD <= nuocDaTinh)
                break;

            // Xác định số khối nằm trong bậc này
            int gioiHanBac = denMucNuoc[i] - tuMucNuoc[i];
            // Lưu ý: Nếu database lưu tuMuc là 0, denMuc là 10 -> gioiHanBac = 10.

            int soKhoiTrongBac = Math.min(mucNuocSD - nuocDaTinh, gioiHanBac);

            if (soKhoiTrongBac > 0) {
                totalMoney += soKhoiTrongBac * gia[i];
                nuocDaTinh += soKhoiTrongBac;
            }
        }

        return totalMoney * thueHeSo;
    }

    /**
     * Main test
     */
    public static void main(String[] args) {

        // Khởi tạo dữ liệu bậc giá
        int[] tuMuc = { 1, 11, 31 }; // Bắt đầu: Bậc 1 từ 1, Bậc 2 từ 11, Bậc 3 từ 21
        int[] denMuc = { 10, 30, 999999 }; // Kết thúc: Bậc 1 đến 10, Bậc 2 đến 20, Bậc 3 đến ∞
        double[] gia = { 4333.33, 5200, 6486.71 }; // Đơn giá: Bậc 1: 5k, Bậc 2: 6k, Bậc 3: 7k
        double thue = 1.05; // Thuế 5% (= 1 + 0.05)

        System.out.println(calculateTotalMoney(1, tuMuc, denMuc, gia, thue));
    }
}