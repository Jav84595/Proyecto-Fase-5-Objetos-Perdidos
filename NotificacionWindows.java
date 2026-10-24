import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;

public class NotificacionWindows {

    private TrayIcon icono;
    private boolean disponible;

    public NotificacionWindows() {
        inicializar();
    }

    private void inicializar() {
        if (!SystemTray.isSupported()) {
            disponible = false;
            return;
        }

        try {
            SystemTray bandeja = SystemTray.getSystemTray();

            Image imagen = crearIcono();

            icono = new TrayIcon(
                    imagen,
                    "Sistema de Objetos Perdidos"
            );

            icono.setImageAutoSize(true);

            bandeja.add(icono);
            disponible = true;

        } catch (AWTException | SecurityException e) {
            disponible = false;
            System.err.println(
                    "No se pudo iniciar la bandeja: "
                    + e.getMessage()
            );
        }
    }

    private Image crearIcono() {
        BufferedImage imagen = new BufferedImage(
                32, 32, BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = imagen.createGraphics();

        try {
            g.setColor(new Color(35, 110, 180));
            g.fillRoundRect(0, 0, 32, 32, 8, 8);

            g.setColor(Color.WHITE);
            g.fillOval(10, 5, 12, 12);
            g.fillRoundRect(7, 18, 18, 9, 5, 5);
        } finally {
            g.dispose();
        }

        return imagen;
    }

    public boolean mostrarNotificacion(
            String titulo, String mensaje) {

        if (!disponible || icono == null
                || titulo == null || titulo.trim().isEmpty()
                || mensaje == null || mensaje.trim().isEmpty()) {
            return false;
        }

        icono.displayMessage(
                titulo,
                mensaje,
                TrayIcon.MessageType.INFO
        );

        return true;
    }

    public boolean estaDisponible() {
        return disponible;
    }

    public void cerrar() {
        if (icono != null) {
            SystemTray.getSystemTray().remove(icono);
            icono = null;
        }

        disponible = false;
    }
}