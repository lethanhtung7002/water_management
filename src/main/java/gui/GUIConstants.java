package gui;

import java.awt.*;

public class GUIConstants {
    private GUIConstants() {
    }

    public static class Fonts {
        private static final String FONT_FAMILY = "Arial";

        private static final int SIZE_TieuDe = 22;
        private static final int SIZE_TieuDePhu = 14;

        public static final Font TieuDe = new Font(FONT_FAMILY, Font.BOLD, SIZE_TieuDe);
        public static final Font TieuDePhu = new Font(FONT_FAMILY, Font.BOLD, SIZE_TieuDePhu);
    }

    public static class Sizes {
        public static final Dimension tf = new Dimension(250, 30);
        public static final Dimension btn = new Dimension(120, 35);
    }

    public static class Colors {
        public static final Color BACKGROUND = rgb(32, 32, 32);
        public static final Color SELECTED = rgb(41, 35, 102);
        public static final Color HOVER = rgb(50, 50, 50);
    }

    public static Color rgb(int r, int g, int b) {
        return new Color(r, g, b);
    }
}
