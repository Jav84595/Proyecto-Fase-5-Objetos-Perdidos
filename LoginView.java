import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class LoginView extends JFrame {
    private final JTextField txtUsuario = new JTextField(28);
    private final JPasswordField txtContrasena = new JPasswordField(28);
    private final JButton btnCrearCuenta = new JButton("Crear cuenta");
    private final JButton btnVolver = new JButton("Volver al inicio de sesión");
    private final CardLayout tarjetas = new CardLayout();
    private final JPanel contenedorTarjetas = new JPanel(tarjetas);
    private final JButton btnIngresar = new JButton("Ingresar");
    private final JButton btnCerrarSesion = new JButton("Cerrar sesión");
    private final JLabel lblSesion = new JLabel("Sin sesión activa");

    public LoginView() {
        setTitle("Inicio de sesión");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(680, 560);
        setMinimumSize(new Dimension(570, 470));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        btnCerrarSesion.setEnabled(false);
        getRootPane().setDefaultButton(btnIngresar);
        EstiloUVG.aplicarVentana(this);
    }

    private JPanel centrarFormulario(JPanel formulario) {
        JPanel exterior = new JPanel(new GridBagLayout());
        exterior.setBackground(EstiloUVG.CLARO);
        exterior.add(formulario);
        return exterior;
    }

    private JPanel construirInicio() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(9, 10, 9, 10);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        JLabel titulo = new JLabel("Iniciar sesión", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        formulario.add(titulo, c);
        c.gridwidth = 1;
        Font fuenteCampos = new Font("SansSerif", Font.PLAIN, 16);
        txtUsuario.setFont(fuenteCampos);
        txtContrasena.setFont(fuenteCampos);
        txtUsuario.setPreferredSize(new Dimension(325, 38));
        txtContrasena.setPreferredSize(new Dimension(325, 38));
        Dimension botonTamano = new Dimension(440, 46);
        for (JButton boton : new JButton[]{btnIngresar, btnCerrarSesion, btnCrearCuenta}) {
            boton.setPreferredSize(botonTamano);
            boton.setFont(new Font("SansSerif", Font.BOLD, 15));
        }
        c.gridx = 0; c.gridy = 1; formulario.add(new JLabel("Usuario:"), c);
        c.gridx = 1; formulario.add(txtUsuario, c);
        c.gridx = 0; c.gridy = 2; formulario.add(new JLabel("Contraseña:"), c);
        c.gridx = 1; formulario.add(txtContrasena, c);
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2; formulario.add(btnIngresar, c);
        c.gridy = 4; formulario.add(lblSesion, c);
        c.gridy = 5; formulario.add(btnCerrarSesion, c);
        c.gridy = 6; formulario.add(btnCrearCuenta, c);
        return centrarFormulario(formulario);
    }

    public void integrarRegistro(RegistroUsuarioView registroView) {
        Container contenidoRegistro = registroView.getContentPane();
        registroView.setContentPane(new JPanel());
        JPanel formularioRegistro = new JPanel(new BorderLayout(0, 12));
        formularioRegistro.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formularioRegistro.add(contenidoRegistro, BorderLayout.CENTER);
        formularioRegistro.add(btnVolver, BorderLayout.SOUTH);
        JPanel registroCentrado = centrarFormulario(formularioRegistro);
        contenedorTarjetas.add(construirInicio(), "login");
        contenedorTarjetas.add(registroCentrado, "registro");
        setContentPane(contenedorTarjetas);
        btnCrearCuenta.addActionListener(e -> mostrarRegistro());
        btnVolver.addActionListener(e -> mostrarInicioSesion());
        EstiloUVG.aplicarVentana(this);
        mostrarInicioSesion();
        setLocationRelativeTo(null);
    }

    public void mostrarRegistro() { tarjetas.show(contenedorTarjetas, "registro"); }
    public void mostrarInicioSesion() { tarjetas.show(contenedorTarjetas, "login"); }
    public String getNombreUsuario() { return txtUsuario.getText().trim(); }
    public String getContrasena() { return new String(txtContrasena.getPassword()); }
    public void agregarLoginListener(ActionListener listener) { btnIngresar.addActionListener(listener); }
    public void agregarCerrarSesionListener(ActionListener listener) { btnCerrarSesion.addActionListener(listener); }
    public void mostrarMensaje(String mensaje) { JOptionPane.showMessageDialog(this, mensaje); }
    public void mostrarSesionActiva(String nombreUsuario) {
        lblSesion.setText("Sesión: " + nombreUsuario);
        btnCerrarSesion.setEnabled(true);
        btnIngresar.setEnabled(false);
        txtUsuario.setEnabled(false);
        txtContrasena.setEnabled(false);
    }
    public void mostrarSesionCerrada() {
        lblSesion.setText("Sin sesión activa");
        btnCerrarSesion.setEnabled(false);
        btnIngresar.setEnabled(true);
        txtUsuario.setEnabled(true);
        txtContrasena.setEnabled(true);
        limpiarCampos();
    }
    public void limpiarCampos() { txtUsuario.setText(""); txtContrasena.setText(""); }
}
