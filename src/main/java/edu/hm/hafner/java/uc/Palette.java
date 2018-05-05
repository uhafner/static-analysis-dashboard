package edu.hm.hafner.java.uc;

import java.awt.*;

/**
 * Color palette for the static analysis tools.
 *
 * @author Ullrich Hafner
 */
enum Palette {
    BLUE(new Color(0xb8, 0xda, 0xff)),
    YELLOW(new Color(0xff, 0xee, 0xba)),
    RED(new Color(0xf5, 0xc6, 0xcb)),
    GREY(new Color(0xd9, 0xd8, 0xd6));

    private final Color color;

    Palette(final Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static String toWebColor(final Color color) {
        return String.format("#%6x", color.getRGB() & 0xffffff);
    }

    public static String toWebColor(final Palette palette) {
        return toWebColor(palette.color);
    }

    public Color brighter() {
        return new Color(brighter(color.getRed()), brighter(color.getBlue()), brighter(color.getGreen()));
    }

    private int brighter(final int component) {
        return Math.min(255, (int) (component * 1.10));
    }
}
