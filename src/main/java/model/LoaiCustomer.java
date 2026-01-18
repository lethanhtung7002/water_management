package model;

public class LoaiCustomer {
    protected int idLoaiCustomer;
    private String tenLoaiCustomer;

    public LoaiCustomer(int idLoaiCustomer, String tenLoaiCustomer) {
        this.idLoaiCustomer = idLoaiCustomer;
        this.tenLoaiCustomer = tenLoaiCustomer;
    }

    public int getIdLoaiCustomer() {
        return idLoaiCustomer;
    }

    public void setIdLoaiCustomer(int idLoaiNguoiDung) {
        this.idLoaiCustomer = idLoaiNguoiDung;
    }

    public String getTenLoaiCustomer() {
        return tenLoaiCustomer;
    }

    public void setTenLoaiCustomer(String tenLoaiCustomer) {
        this.tenLoaiCustomer = tenLoaiCustomer;
    }

    @Override
    public String toString() {
        return tenLoaiCustomer; // JComboBox sẽ hiển thị tên
    }
}
