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
     * @param thue       Thuế (1.1 = 10%, 1.05 = 5%)
     * @return Tổng tiền đã bao gồm thuế
     */
    public static double calculateTotalMoney(
            int mucNuocSD,
            int[] tuMucNuoc,
            int[] denMucNuoc,
            double[] gia,
            double thue) {

        double totalMoney = 0;

        // Duyệt qua từng bậc giá (Bậc 1, Bậc 2, Bậc 3...)
        for (int i = 0; i < gia.length; i++) {

            // --- Checkpoint 1: Kiểm tra đã đến bậc này chưa ---
            // Nếu tổng mức nước < mức bắt đầu của bậc này
            // → Chưa đến bậc này → Dừng vòng lặp
            // Ví dụ: Dùng 8 m³, bậc 2 bắt đầu từ 11 m³
            // → 8 < 11 → Dừng lại, không tính bậc 2 và 3
            if (mucNuocSD < tuMucNuoc[i]) {
                break;
            }

            // --- Bước 1: Xác định mức nước tối đa để tính cho bậc này ---
            // Lấy giá trị nhỏ hơn giữa:
            // - Tổng nước đã dùng (mucNuocSD)
            // - Mức tối đa của bậc (denMucNuoc[i])
            //
            // Ví dụ 1: Dùng 25 m³, Bậc 1 (đến 10)
            // mucTinh = min(25, 10) = 10
            // → Chỉ tính tối đa 10 m³ cho bậc 1
            //
            // Ví dụ 2: Dùng 25 m³, Bậc 3 (đến 999999)
            // mucTinh = min(25, 999999) = 25
            // → Tính hết 25 m³
            int mucTinh = Math.min(mucNuocSD, denMucNuoc[i]);

            // --- Bước 2: Tính số khối nước nằm trong khoảng của bậc này ---
            // Công thức: (Mức cuối - Mức đầu + 1)
            //
            // Ví dụ 1: Bậc 1 (từ 1 đến 10), mucTinh = 10
            // soKhoiNuoc = 10 - 1 + 1 = 10 m³
            // → Bậc 1 tính 10 m³
            //
            // Ví dụ 2: Bậc 2 (từ 11 đến 20), mucTinh = 20, dùng 25 m³
            // soKhoiNuoc = 20 - 11 + 1 = 10 m³
            // → Bậc 2 tính 10 m³ (từ m³ thứ 11 đến 20)
            //
            // Ví dụ 3: Bậc 3 (từ 21), mucTinh = 25, dùng 25 m³
            // soKhoiNuoc = 25 - 21 + 1 = 5 m³
            // → Bậc 3 tính 5 m³ (từ m³ thứ 21 đến 25)
            int soKhoiNuoc = mucTinh - tuMucNuoc[i] + 1;

            // --- Checkpoint 2: Kiểm tra số khối nước có hợp lệ không ---
            // Nếu số khối nước <= 0 → Bỏ qua bậc này
            // (Trường hợp hiếm khi xảy ra, nhưng cần kiểm tra để tránh lỗi logic)
            if (soKhoiNuoc <= 0) {
                continue;
            }

            // --- Bước 3: Tính tiền cho bậc này ---
            // Công thức: Số khối nước × Đơn giá
            //
            // Ví dụ: Bậc 2: 10 m³ × 6,000 = 60,000 VNĐ
            double tienBac = soKhoiNuoc * gia[i];
            System.out.println(tienBac);
            // --- Bước 4: Cộng dồn vào tổng tiền ---
            totalMoney += tienBac;
        }

        return totalMoney * thue;
    }

    /**
     * Main test
     */
    public static void main(String[] args) {

        // Khởi tạo dữ liệu bậc giá
        int[] tuMuc = { 1, 11, 21 }; // Bắt đầu: Bậc 1 từ 1, Bậc 2 từ 11, Bậc 3 từ 21
        int[] denMuc = { 10, 20, 999999 }; // Kết thúc: Bậc 1 đến 10, Bậc 2 đến 20, Bậc 3 đến ∞
        double[] gia = { 5000, 6000, 7000 }; // Đơn giá: Bậc 1: 5k, Bậc 2: 6k, Bậc 3: 7k
        double thue = 1.1; // Thuế 10% (= 1 + 0.1)

        // Test case 1: Sử dụng 25 m³
        System.out.println("=== TEST 1: 25 m³ ===");
        calculateTotalMoney(25, tuMuc, denMuc, gia, thue);

        // Test case 2: Sử dụng 8 m³ (chỉ có Bậc 1)
        System.out.println("\n=== TEST 2: 8 m³ ===");
        calculateTotalMoney(8, tuMuc, denMuc, gia, thue);

        // Test case 3: Sử dụng 50 m³ (đầy đủ 3 bậc)
        System.out.println("\n=== TEST 3: 50 m³ ===");
        calculateTotalMoney(50, tuMuc, denMuc, gia, thue);
    }
}