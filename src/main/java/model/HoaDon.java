package model;

public class HoaDon {
    private int idHoaDon;
    private int idChiSo;
    private int sanLuongTieuThu;
    private double tongTienThanhToan; // Tổng tiền phải trả
    private String ngayLapHoaDon; 
    private int trangThaiHoaDon; // 0: Chưa thanh toán, 1: Đã thanh toán
    private int idDonGia; // ID bảng giá áp dụng

    public HoaDon() {
    }

    public HoaDon(int idHoaDon, int idChiSo, int sanLuongTieuThu,
            double tongTienThanhToan, String ngayLapHoaDon,
            int trangThaiHoaDon, int idDonGia) {
        this.idHoaDon = idHoaDon;
        this.idChiSo = idChiSo;
        this.sanLuongTieuThu = sanLuongTieuThu;
        this.tongTienThanhToan = tongTienThanhToan;
        this.ngayLapHoaDon = ngayLapHoaDon;
        this.trangThaiHoaDon = trangThaiHoaDon;
    }

    // Getters and Setters
    public int getIdHoaDon() {
        return idHoaDon;
    }

    public void setIdHoaDon(int idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public int getIdChiSo() {
        return idChiSo;
    }

    public void setIdChiSo(int idChiSo) {
        this.idChiSo = idChiSo;
    }

    public int getSanLuongTieuThu() {
        return sanLuongTieuThu;
    }

    public void setSanLuongTieuThu(int sanLuongTieuThu) {
        this.sanLuongTieuThu = sanLuongTieuThu;
    }

    public double getTongTienThanhToan() {
        return tongTienThanhToan;
    }

    public void setTongTienThanhToan(double tongTienThanhToan) {
        this.tongTienThanhToan = tongTienThanhToan;
    }

    public String getNgayLapHoaDon() {
        return ngayLapHoaDon;
    }

    public void setNgayLapHoaDon(String ngayLapHoaDon) {
        this.ngayLapHoaDon = ngayLapHoaDon;
    }

    public int getTrangThaiHoaDon() {
        return trangThaiHoaDon;
    }

    public void setTrangThaiHoaDon(int trangThaiHoaDon) {
        this.trangThaiHoaDon = trangThaiHoaDon;
    }

    public int getIdDonGia() {
        return idDonGia;
    }

    public void setIdDonGia(int idDonGia) {
        this.idDonGia = idDonGia;
    }

    /**
     * Lấy trạng thái hóa đơn dưới dạng text
     */
    public String getTrangThaiText() {
        return trangThaiHoaDon == 1 ? "Đã thanh toán" : "Chưa thanh toán";
    }

    /**
     * Format tổng tiền có dấu phẩy
     */
    public String getTongTienFormatted() {
        return String.format("%,.0f VNĐ", tongTienThanhToan);
    }
}