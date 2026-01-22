package dao;

public class QLNdbConstants {
    private QLNdbConstants() {
    }

    public static final String DATABASE_NAME = "quanlynuoc";

    public static final class Tables {
        public static final String Customer = "khachhang";
        public static final String CustomerType = "loai_khach_hang";
        public static final String HoSuDung = "hosudung";
        public static final String ChiSoNuoc = "chisonuoc";
        public static final String HoaDon = "hoadon";
        public static final String GiaNuoc = "gianuoc";
        public static final String BacGia = "bacgia";
        public static final String ThanhToan = "thanhtoan";
    }

    public static final class Id {
        public static final String CustomerID = "ID_KhachHang";
        public static final String CustomerTypeID = "Id_LoaiKH";
        public static final String HoSuDungID = "ID_HoSuDung";
        public static final String ChiSoNuocID = "ID_ChiSo";
        public static final String HoaDonID = "ID_HoaDon";
        public static final String GiaNuocID = "ID_DonGia";
        public static final String BacGiaID = "ID_Bac";
        public static final String ThanhToanID = "ID_ThanhToan";
    }

    public static final class CustomerCol {
        public static final String Name = "Name";
        public static final String CCCD = "CCCD";
        public static final String PhoneNumber = "Phone_number";
        public static final String Email = "Email";
    }

    public static final class qlnCustomerTypeCol {
        public static final String Name = "ten_loai";
    }

    public static final class qlnHoSuDungCol {
        public static final String DiaChi = "DiaChi";
        public static final String KhuVuc = "KhuVuc";
        public static final String TrangThai = "TrangThai";
    }

    public static final class qlnChiSoNuocCol {
        public static final String NamGhi = "NamGhi";
        public static final String ThangGhi = "ThangGhi";
        public static final String NgayGhi = "NgayGhi";
        public static final String ChiSoMoi = "ChiSoMoi";
        public static final String ChiSoCu = "ChiSoCu";
    }

    public static final class qlnHoaDonCol {
        public static final String SoNuocTieuThu = "SanLuongTieuThu";
        public static final String TongTien = "TongTienThanhToan";
        public static final String NgayLap = "NgayLap";
        public static final String TrangThai = "TrangThai";
    }

    public static final class qlnThanhToanCol {
        public static final String NgayThanhToan = "NgayThanhToan";
        public static final String HinhThuc = "HinhThuc";
    }

    public static final class qlnGiaNuocCol {
        public static final String KhuVuc = "KhuVuc";
        public static final String Thue = "Thue";
    }

    public static final class qlnBacGiaCol {
        public static final String BacGia = "BacGia";
        public static final String TuMucNuoc = "TuMucNuoc";
        public static final String DenMucNuoc = "DenMucNuoc";
        public static final String Gia = "Gia";
    }
}

