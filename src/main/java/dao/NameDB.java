package dao;

public class NameDB {
    private NameDB() {
    }

    public static final String DB_quanlynuoc = "quanlynuoc";
}

// Tên cột trong DB_quanlynuoc bảng khachhang 
class DB_CustomerCol {
    private DB_CustomerCol() {
    }

    public static final String TableName = "khachhang";
    public static final String ID = "ID_KhachHang";
    public static final String Loai = "LoaiKhachHang";
    public static final String Name = "Name";
    public static final String CCCD = "CCCD";
    public static final String PhoneNumber = "Phone_number";
    public static final String Email = "Email";
}

// Tên cột trong DB_quanlynuoc bảng loai_khach_hang
class DB_CustomerTypeCol {
    private DB_CustomerTypeCol() {
    }

    public static final String TableName = "loai_khach_hang";
    public static final String ID = "id_loai";
    public static final String Name = "ten_loai";
}

// Tên cột trong DB_quanlynuoc bảng ho su dung
class DB_HoSuDungCol {
    private DB_HoSuDungCol() {
    }

    public static final String TableName = "hosudung";
    public static final String ID = "ID_HoSuDung";
    public static final String ID_KhachHang = "ID_KhachHang";
    public static final String DiaChi = "DiaChi";
    public static final String MaQuanHuyen = "MaQuanHuyen";
    public static final String TrangThai = "TrangThai";
}

// Tên cột trong DB_quanlynuoc bảng dong_ho
class DB_DongHoCol {
    private DB_DongHoCol() {
    }

    public static final String TableName = "donghonuoc";
    public static final String ID = "ID_DongHo";
    public static final String ID_HoSuDung = "ID_HoSuDung";
    public static final String SoDongHo = "SoHieuDongHo";
    public static final String TrangThai = "TrangThai";
}

// Tên cột trong DB_quanlynuoc bảng chi_so_nuoc
class DB_ChiSoNuocCol {
    private DB_ChiSoNuocCol() {
    }

    public static final String TableName = "chisonuoc";
    public static final String ID = "ID_ChiSo";
    public static final String ID_DongHo = "ID_DongHo";
    public static final String NgayGhi = "NgayGhi";
    public static final String ChiSo = "ChiSo";
}

// Tên cột trong DB_quanlynuoc bảng HoaDon
class DB_HoaDonCol {
    private DB_HoaDonCol() {
    }

    public static final String TableName = "hoadon";
    public static final String ID = "ID_HoaDon";
    public static final String ID_KhachHang = "ID_KhachHang";
    public static final String ID_ChiSo = "ID_ChiSo";
    public static final String SoNuocTieuThu = "SanLuongTieuThu";
    public static final String NgayLap = "NgayLap";
    public static final String TrangThai = "TrangThai";
    public static final String Thue = "Thue";
}

