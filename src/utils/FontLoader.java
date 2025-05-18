package utils;

import java.awt.Font;
import java.io.InputStream;

public class FontLoader {

    public static Font loadCustomFont(float size) {
        try {
        	InputStream is = FontLoader.class.getResourceAsStream("/fonts/Nunito-Regular.ttf");
            if (is == null) {
                throw new RuntimeException("Fonte não encontrada");
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, (int) size); // fallback
        }
    }
}
