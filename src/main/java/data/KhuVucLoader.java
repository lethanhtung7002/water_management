package data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import javax.swing.JComboBox;

public class KhuVucLoader {

    private static final File file = new File("src/main/java/data/khu_vuc.txt");

    public void loadKhuVuc(JComboBox<String> cbKhuVuc) {
        cbKhuVuc.addItem("-- Chọn tỉnh ---");
        try {
            Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
            while (scanner.hasNextLine()) {
                cbKhuVuc.addItem(scanner.nextLine().trim());
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }
}