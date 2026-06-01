package view.menu.util;

import javafx.scene.text.Font;

import java.util.Objects;

public class FonteUtil {

    private static final String CAMINHO_FONTE_PIXEL =
            "/fonts/pixel_operator/PixelOperator-Bold.ttf";

    private FonteUtil() {
    }

    public static Font pixel(int tamanho) {
        return Font.loadFont(
                Objects.requireNonNull(
                        FonteUtil.class.getResourceAsStream(CAMINHO_FONTE_PIXEL)
                ),
                tamanho
        );
    }
}