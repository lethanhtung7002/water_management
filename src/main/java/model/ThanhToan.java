package model;

public class ThanhToan {
    private int idThanhToan;
    private int idHoaDon;
    private String ngayThanhToan; 
    private String hinhThuc;

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