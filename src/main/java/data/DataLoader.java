package data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    private static final String DATA_PATH = "src/main/java/data/";

    // Đọc thông tin login đã lưu
    public String[] loginRead() {
        try {
            Path path = getPath("login.txt");
            if (!Files.exists(path)) {
                return new String[] { null, null };
            }

            List<String> lines = Files.readAllLines(path);
            if (lines.size() >= 2) {
                return new String[] { lines.get(0), lines.get(1) };
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[] { null, null };
    }

    // Lưu thông tin login (khi check "Remember me")
    public boolean loginWrite(String username, String password) {
        try {
            String content = username + "\n" + password;
            Files.writeString(getPath("login.txt"), content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra xem có thông tin login đã lưu không
    public boolean hasLoginSaved() {
        String[] credentials = loginRead();
        return credentials[0] != null && credentials[1] != null;
    }

    private Path getPath(String filename) {
        return Paths.get(DATA_PATH + filename);
    }

    // Các method khác...
    public List<String> khuVuc() {
        try {
            return Files.readAllLines(getPath("tinh.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        DataLoader loader = new DataLoader();
        loader.loginWrite("admin", "password");
        System.out.println(loader.loginRead());
    }
}