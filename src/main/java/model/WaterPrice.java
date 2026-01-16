package model;

public class WaterPrice {
    private int idWaterPrice;
    private loaiCustomer loaiCustomer;
    private String khuVuc;
    private double thue;

    public WaterPrice() {
    }
    
    public WaterPrice(int idWaterPrice, loaiCustomer loaiCustomer, String khuVuc, double thue) {
        this.idWaterPrice = idWaterPrice;
        this.loaiCustomer = loaiCustomer;
        this.khuVuc = khuVuc;
        this.thue = thue;
    }

    public int getIdWaterPrice() {
        return idWaterPrice;
    }

    public void setIdWaterPrice(int idWaterPrice) {
        this.idWaterPrice = idWaterPrice;
    }

    public loaiCustomer getLoaiCustomer() {
        return loaiCustomer;
    }

    public void setLoaiCustomer(loaiCustomer loaiCustomer) {
        this.loaiCustomer = loaiCustomer;
    }

    public String getKhuVuc() {
        return khuVuc;
    }

    public void setKhuVuc(String khuVuc) {
        this.khuVuc = khuVuc;
    }

    public double getThue() {
        return thue;
    }

    public void setThue(double thue) {
        this.thue = thue;
    }

    
    
}
