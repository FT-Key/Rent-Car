package utils;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class UIStyles {

    // 🎨 Colores base
    public static final Color COLOR_FONDO_PRINCIPAL = new Color(245, 247, 250);
    public static final Color COLOR_FONDO_PANEL = Color.WHITE;
    public static final Color COLOR_TEXTO_PRINCIPAL = new Color(33, 37, 41);
    public static final Color COLOR_TEXTO_SECUNDARIO = new Color(80, 80, 80);

    // 🎨 Botones
    public static final Color COLOR_BOTON = new Color(52, 152, 219);
    public static final Color COLOR_BOTON_HOVER = new Color(41, 128, 185);
    public static final Color COLOR_BOTON_TEXTO = Color.WHITE;

    // 🎨 Bordes y acentos
    public static final Color COLOR_BORDE_INPUT = new Color(200, 200, 200);
    public static final Color COLOR_SELECCION = new Color(41, 128, 185);
    public static final Color COLOR_FILA_ALTERNADA = new Color(235, 240, 245);
    public static final Color COLOR_CABECERA_TABLA = new Color(52, 152, 219);
    public static final Color COLOR_CABECERA_TEXTO = Color.WHITE;

    // 🖋 Fuentes
    public static final Font FUENTE_TEXTO = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FUENTE_LABEL = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 22);

    // ⬜ Bordes
    public static final Border BORDE_INPUT = BorderFactory.createLineBorder(COLOR_BORDE_INPUT, 1);
    public static final Border BORDE_PANEL = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE_INPUT, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
    );

    // 🌈 Sombras o efectos (si luego quieres aplicar)
    public static final Color COLOR_SOMBRA_SUAVE = new Color(0, 0, 0, 30);
}
