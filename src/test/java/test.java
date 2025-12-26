import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.BorderLayout;

public class test extends JFrame {

    public test() {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());

        frame.add(new JButton("Top"), BorderLayout.NORTH);
        frame.add(new JButton("Bottom"), BorderLayout.SOUTH);
        frame.add(new JButton("Left"), BorderLayout.WEST);
        frame.add(new JButton("Right"), BorderLayout.EAST);
        frame.add(new JButton("Center"), BorderLayout.CENTER);

        frame.setSize(400, 300);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new test();
    }
}
