import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

/** Apariencia visual compartida; no modifica la logica de negocio. */
public final class EstiloUVG {
    public static final Color VERDE = new Color(0, 138, 50);
    public static final Color OSCURO = new Color(20, 83, 45);
    public static final Color CLARO = new Color(241, 247, 243);
    public static final Color TEXTO = new Color(35, 55, 42);
    public static final Color BORDE = new Color(199, 220, 204);
    private static final Font FUENTE = new Font("SansSerif", Font.PLAIN, 13);
    private EstiloUVG() {}

    public static void aplicar(Component componente) {
        if (componente instanceof JPanel) componente.setBackground(CLARO);
        if (componente instanceof JLabel) {
            componente.setForeground(TEXTO);
            componente.setFont(FUENTE);
        }
        if (componente instanceof JButton) {
            JButton b = (JButton) componente;
            b.setBackground(VERDE);
            b.setForeground(Color.WHITE);
            b.setFont(FUENTE.deriveFont(Font.BOLD));
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(OSCURO),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)));
        }
        if (componente instanceof JTextField || componente instanceof JPasswordField
                || componente instanceof JTextArea || componente instanceof JList
                || componente instanceof JComboBox) {
            componente.setFont(FUENTE);
            componente.setForeground(TEXTO);
            componente.setBackground(Color.WHITE);
        }
        if (componente instanceof JTabbedPane) {
            JTabbedPane t = (JTabbedPane) componente;
            t.setBackground(CLARO);
            t.setForeground(OSCURO);
            t.setFont(FUENTE.deriveFont(Font.BOLD));
        }
        if (componente instanceof Container) {
            for (Component hijo : ((Container) componente).getComponents()) aplicar(hijo);
        }
    }

    public static Image cargarLogo() {
        try {
            java.net.URL url = EstiloUVG.class.getResource("/recursos/logo_uvg.png");
            if (url != null) return ImageIO.read(url);
            File archivo = new File("recursos/logo_uvg.png");
            if (archivo.isFile()) return ImageIO.read(archivo);
        } catch (IOException e) {
            System.err.println("No se pudo cargar el logo UVG: " + e.getMessage());
        }
        return null;
    }

    public static JLabel etiquetaLogo(int ancho, int alto) {
        JLabel etiqueta = new JLabel();
        Image imagen = cargarLogo();
        if (imagen != null) etiqueta.setIcon(new ImageIcon(
            imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH)));
        return etiqueta;
    }

    public static void aplicarVentana(JFrame ventana) {
        aplicar(ventana.getContentPane());
        Image logo = cargarLogo();
        if (logo != null) ventana.setIconImage(logo);
    }
}
