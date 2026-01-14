package model;

public class GiaNuoc {
    private int idDonGia;
    private int idLoaiCustomer;
    private String KhuVuc;
    private Double Thue;

    public GiaNuoc(int iD_DonGia, int idLoaiCustomer, String khuVuc, Double thue) {
        idDonGia = iD_DonGia;
        this.idLoaiCustomer = idLoaiCustomer;
        KhuVuc = khuVuc;
        Thue = thue;
    }

    public int getIdLoaiCustomer() {
        return idLoaiCustomer;
    }

    public void setIdLoaiCustomer(int idLoaiCustomer) {
        this.idLoaiCustomer = idLoaiCustomer;
    }

    public String getKhuVuc() {
        return KhuVuc;
    }

    public void setKhuVuc(String khuVuc) {
        KhuVuc = khuVuc;
    }

    public Double getThue() {
        return Thue;
    }

    public void setThue(Double thue) {
        Thue = thue;
    }

    public int getIdDonGia() {
        return idDonGia;
    }

    public void setIdDonGia(int idDonGia) {
        this.idDonGia = idDonGia;
    }

}

class BacGia {
    private int idBac;
    private int idDonGia;
    private int bacGia;
    private int TuMucNuoc;
    private int DenMucNuoc;
    private Double Gia;

    public BacGia(){

    }
    
    public BacGia(int idBac, int idDonGia, int bacGia, int tuMucNuoc, int denMucNuoc, double gia) {
        this.idBac = idBac;
        this.idDonGia = idDonGia;
        this.bacGia = bacGia;
        this.TuMucNuoc = tuMucNuoc;
        this.DenMucNuoc = denMucNuoc;
        this.Gia = gia;
    }

    public int getIdBac() {
        return idBac;
    }

    public void setIdBac(int idBac) {
        this.idBac = idBac;
    }

    public int getIdDonGia() {
        return idDonGia;
    }

    public void setIdDonGia(int idDonGia) {
        this.idDonGia = idDonGia;
    }

    public int getBacGia() {
        return bacGia;
    }

    public void setBacGia(int bacGia) {
        this.bacGia = bacGia;
    }

    public int getTuMucNuoc() {
        return TuMucNuoc;
    }

    public void setTuMucNuoc(int tuMucNuoc) {
        TuMucNuoc = tuMucNuoc;
    }

    public int getDenMucNuoc() {
        return DenMucNuoc;
    }

    public void setDenMucNuoc(int denMucNuoc) {
        DenMucNuoc = denMucNuoc;
    }

    public double getGia() {
        return Gia;
    }

    public void setGia(Double gia) {
        Gia = gia;
    }  
}