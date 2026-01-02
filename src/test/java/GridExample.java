import javax.swing.*;
import java.awt.*;

public class GridExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GridLayout 1x1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel(new GridLayout(2, 1));

        JButton button = new JButton("Click Me");
        panel.add(button); // Nút này sẽ chiếm toàn bộ panel

        frame.add(panel);
        frame.setVisible(true);
    }
    
}
