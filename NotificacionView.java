import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NotificacionView extends JPanel {

    private final JTextArea areaNotificaciones = new JTextArea();
    private final JButton btnActualizar = new JButton("Actualizar");
    private final JLabel lblContador = new JLabel("0 notificaciones");

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public NotificacionView() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(
                new FlowLayout(FlowLayout.LEFT));

        panelSuperior.add(new JLabel("Historial de notificaciones"));
        panelSuperior.add(btnActualizar);

        add(panelSuperior, BorderLayout.NORTH);

        areaNotificaciones.setEditable(false);
        areaNotificaciones.setLineWrap(true);
        areaNotificaciones.setWrapStyleWord(true);

        add(new JScrollPane(areaNotificaciones),
                BorderLayout.CENTER);

        add(lblContador, BorderLayout.SOUTH);

        mostrarNotificaciones(null);
    
        EstiloUVG.aplicar(this);
    }

    public void agregarActualizarListener(ActionListener listener) {
        btnActualizar.addActionListener(listener);
    }

    public void mostrarNotificaciones(
            List<Notificacion> notificaciones) {

        areaNotificaciones.setText("");

        if (notificaciones == null || notificaciones.isEmpty()) {
            lblContador.setText("0 notificaciones");
            areaNotificaciones.setText(
                    "No hay notificaciones disponibles.");
            return;
        }

        lblContador.setText(
                notificaciones.size() + " notificacion(es)");

        for (Notificacion notificacion : notificaciones) {
            areaNotificaciones.append(
                    "Fecha: "
                    + notificacion.getFechaCreacion().format(FORMATO)
                    + "\n"
                    + "Tipo: " + notificacion.getTipo()
                    + "\n"
                    + "Objeto ID: " + notificacion.getObjetoId()
                    + "\n"
                    + "Titulo: " + notificacion.getTitulo()
                    + "\n"
                    + "Mensaje: " + notificacion.getMensaje()
                    + "\n"
                    + "----------------------------------------\n\n"
            );
        }

        areaNotificaciones.setCaretPosition(0);
    }

    public void limpiarNotificaciones() {
        mostrarNotificaciones(null);
    }
}