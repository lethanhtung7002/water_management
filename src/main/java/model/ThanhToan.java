package model;

/**
 * Model cho thanh toán - theo database schema thực tế
 * 
 * Bảng: ThanhToan
 * Lưu trữ thông tin thanh toán hóa đơn
 */
public class ThanhToan {
    private int idThanhToan;
    private int idHoaDon;
    private String ngayThanhToan; // Ngày thanh toán
    private String hinhThuc; // Hình thức thanh toán (Tiền mặt, Chuyển khoản...)

    public ThanhToan() {
    }

    public ThanhToan(int idThanhToan, int idHoaDon, String ngayThanhToan, String hinhThuc) {
        this.idThanhToan = idThanhToan;
        this.idHoaDon = idHoaDon;
        this.ngayThanhToan = ngayThanhToan;
        this.hinhThuc = hinhThuc;
    }

    public int getIdThanhToan() {
        return idThanhToan;
    }

    public void setIdThanhToan(int idThanhToan) {
        this.idThanhToan = idThanhToan;
    }

    public int getIdHoaDon() {
        return idHoaDon;
    }

    public void setIdHoaDon(int idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public String getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(String ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public String getHinhThuc() {
        return hinhThuc;
    }

    public void setHinhThuc(String hinhThuc) {
        this.hinhThuc = hinhThuc;
    }
}