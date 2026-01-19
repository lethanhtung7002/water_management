package model;

public class ChiSoNuoc {
    private int idChiSo;
    private int idHoSuDung;
    private int namGhi; 
    private int thangGhi; 
    private int ngayGhi; 
    private int chiSoMoi; 
    private int chiSoCu; 

    public ChiSoNuoc() {
    }

    public ChiSoNuoc(int idChiSo, int idHoSuDung, int namGhi, int thangGhi,
            int ngayGhi, int chiSoMoi, int chiSoCu) {
        this.idChiSo = idChiSo;
        this.idHoSuDung = idHoSuDung;
        this.namGhi = namGhi;
        this.thangGhi = thangGhi;
        this.ngayGhi = ngayGhi;
        this.chiSoMoi = chiSoMoi;
        this.chiSoCu = chiSoCu;
    }

    public int getIdChiSo() {
        return idChiSo;
    }

    public void setIdChiSo(int idChiSo) {
        this.idChiSo = idChiSo;
    }

    public int getIdHoSuDung() {
        return idHoSuDung;
    }

    public void setIdHoSuDung(int idHoSuDung) {
        this.idHoSuDung = idHoSuDung;
    }

    public int getNamGhi() {
        return namGhi;
    }

    public void setNamGhi(int namGhi) {
        this.namGhi = namGhi;
    }

    public int getThangGhi() {
        return thangGhi;
    }

    public void setThangGhi(int thangGhi) {
        this.thangGhi = thangGhi;
    }

    public int getNgayGhi() {
        return ngayGhi;
    }

    public void setNgayGhi(int ngayGhi) {
        this.ngayGhi = ngayGhi;
    }

    public int getChiSoMoi() {
        return chiSoMoi;
    }

    public void setChiSoMoi(int chiSoMoi) {
        this.chiSoMoi = chiSoMoi;
    }

    public int getChiSoCu() {
        return chiSoCu;
    }

    public void setChiSoCu(int chiSoCu) {
        this.chiSoCu = chiSoCu;
    }

    /**
     * Tính số nước tiêu thụ
     * 
     * @return Chỉ số mới - Chỉ số cũ
     */
    public int getTieuThu() {
        return chiSoCu - chiSoMoi;
    }

    /**
     * Lấy ngày ghi dưới dạng chuỗi
     * 
     * @return "dd/MM/yyyy"
     */
    public String getNgayGhiFormatted() {
        return String.format("%02d/%02d/%04d", ngayGhi, thangGhi, namGhi);
    }

    /**
     * Lấy tháng/năm dưới dạng chuỗi
     * 
     * @return "MM/yyyy"
     */
    public String getThangNamFormatted() {
        return String.format("%02d/%04d", thangGhi, namGhi);
    }
}