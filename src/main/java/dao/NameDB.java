/**
 * Database Constants - Quản lý nước
 * 
 * Chứa tên database, bảng và cột để tránh hard-code trong SQL queries.
 * CẬP NHẬT: Thêm column names cho ChiSoNuoc và HoaDon
 * 
 * @author Lê Thanh Tùng
 * @version 2.0 - Updated with ChiSoNuoc and HoaDon columns
 */

package dao;

public class NameDB {
    private NameDB() {
    }

    public static final String DB_quanlynuoc = "quanlynuoc";
}

class qlnTableName {
    public static final String Customer = "khachhang";
    public static final String CustomerType = "loai_khach_hang";
    public static final String HoSuDung = "hosudung";
    public static final String ChiSoNuoc = "chisonuoc";
    public static final String HoaDon = "hoadon";
    public static final String GiaNuoc = "gianuoc";
    public static final String BacGia = "bacgia";
    public static final String ThanhToan = "thanhtoan";
}

class qlnIDName {
    public static final String CustomerID = "ID_KhachHang";
    public static final String CustomerTypeID = "Id_LoaiKH";
    public static final String HoSuDungID = "ID_HoSuDung";
    public static final String ChiSoNuocID = "ID_ChiSo";
    public static final String HoaDonID = "ID_HoaDon";
    public static final String GiaNuocID = "ID_DonGia";
    public static final String BacGiaID = "ID_Bac";
    public static final String ThanhToanID = "ID_ThanhToan";
}

// Tên cột trong DB_quanlynuoc bảng khachhang
class qlnCustomerCol {
    private qlnCustomerCol() {
    }

    public static final String Name = "Name";
    public static final String CCCD = "CCCD";
    public static final String PhoneNumber = "Phone_number";
    public static final String Email = "Email";
}

// Tên cột trong DB_quanlynuoc bảng loai_khach_hang
class qlnCustomerTypeCol {
    private qlnCustomerTypeCol() {
    }

    public static final String Name = "ten_loai";
}

// Tên cột trong DB_quanlynuoc bảng ho su dung
class qlnHoSuDungCol {
    private qlnHoSuDungCol() {
    }

    public static final String DiaChi = "DiaChi";
    public static final String KhuVuc = "KhuVuc";
    public static final String TrangThai = "TrangThai";
}

// Tên cột trong DB_quanlynuoc bảng chi_so_nuoc
class qlnChiSoNuocCol {
    private qlnChiSoNuocCol() {
    }

    public static final String NamGhi = "NamGhi";
    public static final String ThangGhi = "ThangGhi";
    public static final String NgayGhi = "NgayGhi";
    public static final String ChiSoMoi = "ChiSoMoi";
    public static final String ChiSoCu = "ChiSoCu";
}

// Tên cột trong DB_quanlynuoc bảng HoaDon
class qlnHoaDonCol {
    private qlnHoaDonCol() {
    }

    public static final String SoNuocTieuThu = "SanLuongTieuThu";
    public static final String TongTien = "TongTienThanhToan";
    public static final String NgayLap = "NgayLapHoaDon";
    public static final String TrangThai = "TrangThaiHoaDon";
}

// Tên cột trong DB_quanlynuoc bảng ThanhToan
class qlnThanhToanCol {
    private qlnThanhToanCol() {
    }

    public static final String NgayThanhToan = "NgayThanhToan";
    public static final String HinhThuc = "HinhThuc";
}

class qlnGiaNuocCol {
    public static final String KhuVuc = "KhuVuc";
    public static final String Thue = "Thue";
}

class qlnBacGiaCol {
    public static final String BacGia = "BacGia";
    public static final String TuMucNuoc = "TuMucNuoc";
    public static final String DenMucNuoc = "DenMucNuoc";
    public static final String Gia = "Gia";
}