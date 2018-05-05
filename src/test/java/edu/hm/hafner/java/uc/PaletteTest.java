package edu.hm.hafner.java.uc;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the class {@link Palette}.
 *
 * @author Ullrich Hafner
 */
class PaletteTest {
    @Test
    void shouldConvertColor() {
        String color = Palette.toWebColor(Palette.RED.getColor());

        assertThat(color).isEqualTo("#f5c6cb");
    }
}