package model;

public class Customer {
    private int idCustomer;
    private String nameCustomer;
    private int LoaiCustomer;
    private String CCCD;
    private String phoneCustomer;
    private String email;

    public Customer() {
    }

    public Customer(int idCustomer, String nameCustomer, int loaiCustomer, String CCCD, String phoneCustomer, String email) {
        this.idCustomer = idCustomer;
        this.nameCustomer = nameCustomer;
        this.LoaiCustomer = loaiCustomer;
        this.CCCD = CCCD;
        this.phoneCustomer = phoneCustomer;
        this.email = email;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idUser) {
        this.idCustomer = idUser;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public int getLoaiCustomer() {
        return LoaiCustomer;
    }

    public void setLoaiCustomer(int loaiCustomer) {
        this.LoaiCustomer = loaiCustomer;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String cCCD) {
        this.CCCD = cCCD;
    }

    public String getPhoneCustomer() {
        return phoneCustomer;
    }

    public void setPhoneCustomer(String phoneCustomer) {
        this.phoneCustomer = phoneCustomer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
