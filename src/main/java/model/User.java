package model;

public class User {
    private int idUser;
    private String nameUser;
    private String LoaiUser;
    private String CCCD;
    private String phoneUser;
    private String emailUser;

    public User() {
    }

    public User(int idUser, String nameUser, String loaiUser, String cCCD, String phoneUser, String emailUser) {
        this.idUser = idUser;
        this.nameUser = nameUser;
        LoaiUser = loaiUser;
        CCCD = cCCD;
        this.phoneUser = phoneUser;
        this.emailUser = emailUser;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getLoaiUser() {
        return LoaiUser;
    }

    public void setLoaiUser(String loaiUser) {
        LoaiUser = loaiUser;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String cCCD) {
        CCCD = cCCD;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

}
