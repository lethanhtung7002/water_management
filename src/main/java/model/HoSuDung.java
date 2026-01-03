package model;

public class HoSuDung {
    private int ID_HoSuDung;
    private int ID_Customer;
    private String DiaChi;
    private String MaQuanHuyen;
    private int TrangThai;

    public HoSuDung() {
    }

    public HoSuDung(int iD_HoSuDung, int iD_Customer, String diaChi, String maQuanHuyen, int trangThai) {
        ID_HoSuDung = iD_HoSuDung;
        ID_Customer = iD_Customer;
        DiaChi = diaChi;
        MaQuanHuyen = maQuanHuyen;
        TrangThai = trangThai;
    }

    public int getID_HoSuDung() {
        return ID_HoSuDung;
    }

    public void setID_HoSuDung(int iD_HoSuDung) {
        ID_HoSuDung = iD_HoSuDung;
    }

    public int getID_Customer() {
        return ID_Customer;
    }

    public void setID_Customer(int iD_Customer) {
        ID_Customer = iD_Customer;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getMaQuanHuyen() {
        return MaQuanHuyen;
    }

    public void setMaQuanHuyen(String maQuanHuyen) {
        MaQuanHuyen = maQuanHuyen;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        TrangThai = trangThai;
    }

    
}
