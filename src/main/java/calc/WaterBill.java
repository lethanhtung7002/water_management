package calc;

abstract class WaterBill {
    protected double thueVAT;
    protected double giaMAX;
    protected double khoi_nuoc; // m³

    public WaterBill(double khoi_nuoc, double thueVAT, double giaMAX) {
        this.khoi_nuoc = khoi_nuoc;
        this.thueVAT = thueVAT;
        this.giaMAX = giaMAX;
    }

    double calc_giaMAX() {
        return khoi_nuoc * giaMAX * thueVAT;
    }

    abstract double calc_total();
}

class WaterBillFamily extends WaterBill {
    private double giaBac1; // 0 - 10
    private double giaBac2; // 11 - 30

    public WaterBillFamily(double khoi_nuoc, double thueVAT,
            double giaBac1, double giaBac2, double giaMAX) {
        super(khoi_nuoc, thueVAT, giaMAX);
        this.giaBac1 = giaBac1;
        this.giaBac2 = giaBac2;
    }

    double calc_giaBac1() {
        double soKhoi = Math.min(khoi_nuoc, 10);
        return soKhoi * giaBac1 * thueVAT;
    }

    double calc_giaBac2() {
        if (khoi_nuoc <= 10)
            return 0;

        double soKhoi = Math.min(khoi_nuoc - 10, 20);
        return soKhoi * giaBac2 * thueVAT;
    }

    @Override
    double calc_giaMAX() {
        if (khoi_nuoc <= 30)
            return 0;

        return (khoi_nuoc - 30) * giaMAX * thueVAT;
    }

    @Override
    double calc_total() {
        return calc_giaBac1() + calc_giaBac2() + calc_giaMAX();
    }
}
