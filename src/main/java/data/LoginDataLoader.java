package data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Class quản lý thông tin đăng nhập (username và password).
 * 
 * Chức năng:
 * - Lưu thông tin đăng nhập vào file khi user tick "Remember me"
 * - Đọc thông tin đăng nhập từ file để tự động login
 * 
 * File lưu trữ: src/main/java/data/login.txt
 * 
 * @author Lê Thanh Tùng
 * @version 1.0
 */
public class LoginDataLoader {

    // Đường dẫn đến file lưu thông tin login
    private static final File file = new File("src/main/java/data/login.txt");

    /**
     * Đọc thông tin đăng nhập từ file.
     * File có 2 dòng: dòng 1 là username, dòng 2 là password.
     * 
     * @return Mảng có 2 phần tử: [username, password]
     *         Trả về [null, null] nếu file không tồn tại hoặc thiếu dữ liệu
     */
    public String[] loginRead() {
        // Tạo mảng để lưu kết quả
        String[] thongTin = new String[2];
        thongTin[0] = null; // username
        thongTin[1] = null; // password

        // Kiểm tra file có tồn tại không
        if (!file.exists()) {
            System.out.println("File login.txt chưa tồn tại");
            return thongTin;
        }

        // Đọc file
        try {
            Scanner sc = new Scanner(file);

            // Đọc dòng 1: username (nếu có)
            if (sc.hasNextLine()) {
                thongTin[0] = sc.nextLine().trim();
            }

            // Đọc dòng 2: password (nếu có)
            if (sc.hasNextLine()) {
                thongTin[1] = sc.nextLine().trim();
            }

            // Đóng file
            sc.close();

            System.out.println("Đọc file thành công!");

        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }

        return thongTin;
    }

    /**
     * Lưu thông tin đăng nhập vào file.
     * 
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return true nếu lưu thành công, false nếu thất bại
     */
    public boolean loginWrite(String username, String password) {
        try {
            // Tạo thư mục nếu chưa tồn tại
            File thuMuc = file.getParentFile();
            if (thuMuc != null && !thuMuc.exists()) {
                thuMuc.mkdirs();
            }

            // Ghi file
            FileWriter fw = new FileWriter(file);

            // Ghi username (dòng 1)
            fw.write(username);
            fw.write("\n"); // Xuống dòng

            // Ghi password (dòng 2)
            fw.write(password);

            // Đóng file (quan trọng!)
            fw.close();

            System.out.println("Lưu thông tin thành công!");
            return true;

        } catch (IOException e) {
            System.out.println("Lỗi khi lưu file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Kiểm tra xem có thông tin đăng nhập đã lưu hay chưa.
     * 
     * @return true nếu có thông tin hợp lệ, false nếu không
     */
    public boolean hasLoginSaved() {
        // Kiểm tra file có tồn tại không
        if (!file.exists()) {
            return false;
        }

        // Đọc thông tin từ file
        String[] thongTin = loginRead();

        // Kiểm tra cả username VÀ password có hợp lệ không
        if (thongTin[0] != null && !thongTin[0].isEmpty()
                && thongTin[1] != null && !thongTin[1].isEmpty()) {
            return true;
        }

        return false;
    }

}