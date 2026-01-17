package calc;

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
     * @param thue       Thuế (1.1 = 10%)
     * @return Tổng tiền đã bao gồm thuế
     */
    public static double calculateTotalMoney(
            int mucNuocSD,
            int[] tuMucNuoc,
            int[] denMucNuoc,
            double[] gia,
            double thue) {

        double totalMoney = 0;

        for (int i = 0; i < gia.length; i++) {

            // Nếu chưa tới bậc này thì dừng
            if (mucNuocSD < tuMucNuoc[i]) {
                break;
            }

            int mucTinh = Math.min(mucNuocSD, denMucNuoc[i]);
            int soKhoiNuoc = mucTinh - tuMucNuoc[i] + 1;

            if (soKhoiNuoc <= 0) {
                continue;
            }

            double tienBac = soKhoiNuoc * gia[i];
            totalMoney += tienBac;

            System.out.println(
                    "Bậc " + (i + 1) + ": " +
                            soKhoiNuoc + " m³ × " +
                            String.format("%,.0f", gia[i]) +
                            " = " +
                            String.format("%,.0f", tienBac) + " VNĐ");
        }

        System.out.println("Tổng chưa thuế: " +
                String.format("%,.0f", totalMoney) + " VNĐ");

        double tienThue = totalMoney * (thue - 1);
        double totalWithTax = totalMoney * thue;

        System.out.println("Thuế (" +
                String.format("%.0f", (thue - 1) * 100) +
                "%): " +
                String.format("%,.0f", tienThue) + " VNĐ");

        System.out.println("Tổng tiền: " +
                String.format("%,.0f", totalWithTax) + " VNĐ");

        return totalWithTax;
    }

    /**
     * Main test
     */
    public static void main(String[] args) {

        int[] tuMuc = { 1, 11, 21 };
        int[] denMuc = { 10, 20, 999999 };
        double[] gia = { 5000, 6000, 7000 };
        double thue = 1.1;

        System.out.println("=== TEST 1: 25 m³ ===");
        calculateTotalMoney(25, tuMuc, denMuc, gia, thue);

        System.out.println("\n=== TEST 2: 8 m³ ===");
        calculateTotalMoney(8, tuMuc, denMuc, gia, thue);

        System.out.println("\n=== TEST 3: 50 m³ ===");
        calculateTotalMoney(50, tuMuc, denMuc, gia, thue);
    }
}
