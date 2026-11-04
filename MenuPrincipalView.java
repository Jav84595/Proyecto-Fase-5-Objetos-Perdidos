import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class MenuPrincipalView extends JFrame {

    private final JTabbedPane pestanas = new JTabbedPane();
    private final JLabel lblUsuario = new JLabel("Sin sesión activa");
    private final JButton btnCerrarSesion = new JButton("Cerrar sesión");

    public MenuPrincipalView() {
        setTitle("Sistema de Objetos Perdidos");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setMinimumSize(new Dimension(850, 550));
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel encabezado = new JPanel(new BorderLayout(10, 10));
        JLabel titulo = new JLabel("Sistema de Objetos Perdidos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        JPanel marca = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        marca.add(EstiloUVG.etiquetaLogo(55, 55));
        marca.add(titulo);
        encabezado.add(marca, BorderLayout.WEST);

        JPanel sesion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sesion.add(lblUsuario);
        sesion.add(btnCerrarSesion);
        encabezado.add(sesion, BorderLayout.EAST);

        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        contenido.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );
        contenido.add(encabezado, BorderLayout.NORTH);
        contenido.add(pestanas, BorderLayout.CENTER);

        setContentPane(contenido);
        agregarPestana("Inicio", crearPanelInicio());
        EstiloUVG.aplicarVentana(this);
        encabezado.setBackground(EstiloUVG.VERDE);
        marca.setBackground(EstiloUVG.VERDE);
        titulo.setForeground(java.awt.Color.WHITE);
        sesion.setBackground(EstiloUVG.VERDE);
        lblUsuario.setForeground(java.awt.Color.WHITE);
    }

    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel bienvenida = new JLabel(
                "Bienvenido al Sistema de Objetos Perdidos",
                SwingConstants.CENTER
        );
        bienvenida.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(bienvenida, BorderLayout.CENTER);
        return panel;
    }

    public void agregarPestana(String titulo, JPanel panel) {
        if (titulo == null || titulo.trim().isEmpty() || panel == null) {
            throw new IllegalArgumentException(
                    "La pestaña necesita un título y un panel."
            );
        }
        EstiloUVG.aplicar(panel);
        pestanas.addTab(titulo, panel);
    }

    public void mostrarUsuario(String nombreUsuario) {
        lblUsuario.setText("Sesión: " + nombreUsuario);
    }

    public void agregarCerrarSesionListener(ActionListener listener) {
        btnCerrarSesion.addActionListener(listener);
    }

    public void seleccionarPestana(int indice) {
        if (indice >= 0 && indice < pestanas.getTabCount()) {
            pestanas.setSelectedIndex(indice);
        }
    }
}
