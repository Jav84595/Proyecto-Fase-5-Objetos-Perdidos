import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        try {
            ConexionBD.inicializarBaseDatos();
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo iniciar la base de datos.\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            RegistroUsuarioView registroUsuarioView = new RegistroUsuarioView();
            LoginView loginView = new LoginView();
            UsuarioController usuarioController = new UsuarioController(
                    new UsuarioDAO(), registroUsuarioView, loginView);

            RegistroObjetoView registroObjetoView = new RegistroObjetoView();
            ConsultaObjetoView consultaObjetoView = new ConsultaObjetoView();
            ObjetoController objetoController = new ObjetoController(
                    registroObjetoView, consultaObjetoView, new ObjetoDAO(),
                    usuarioController::autorizarOperacion);

            DevolucionView devolucionView = new DevolucionView();
            DevolucionController devolucionController = new DevolucionController(
                    objetoController, new DevolucionDAO(), devolucionView,
                    usuarioController::getUsuarioActivo);
            ConsultaDevolucionesView historialView = new ConsultaDevolucionesView();
            historialView.agregarActualizarListener(e ->
                    historialView.mostrarDevoluciones(devolucionController.consultarDevoluciones()));

            NotificacionController notificacionController = new NotificacionController(
                    new NotificacionDAO(), usuarioController::getUsuarioActivo);
            NotificacionView notificacionView = new NotificacionView();
            NotificacionWindows avisosWindows = new NotificacionWindows();
            notificacionView.agregarActualizarListener(e ->
                    notificacionView.mostrarNotificaciones(
                            notificacionController.consultarNotificaciones()));

            devolucionController.agregarObservadorDevolucion(objeto -> {
                boolean guardada = notificacionController.notificarObjetoDevuelto(
                        objeto.getId(), objeto.getNombre());
                if (guardada) {
                    notificacionView.mostrarNotificaciones(
                            notificacionController.consultarNotificaciones());
                    avisosWindows.mostrarNotificacion(
                            "Objeto devuelto", "Se devolvio el objeto: " + objeto.getNombre());
                } else {
                    System.err.println("La devolucion se registro, pero no se pudo guardar su notificacion.");
                }
            });

            MenuPrincipalView menu = new MenuPrincipalView();
            menu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    avisosWindows.cerrar();
                    usuarioController.cerrarSesion();
                    loginView.mostrarSesionCerrada();
                    loginView.mostrarInicioSesion();
                }
            });
            menu.agregarPestana("Registrar objeto", extraerPanel(registroObjetoView));
            menu.agregarPestana("Consultar objetos", extraerPanel(consultaObjetoView));
            menu.agregarPestana("Devoluciones", devolucionView);
            menu.agregarPestana("Historial", historialView);
            menu.agregarPestana("Notificaciones", notificacionView);

            devolucionView.agregarHistorialListener(e -> {
                historialView.mostrarDevoluciones(devolucionController.consultarDevoluciones());
                menu.seleccionarPestana(3);
            });
            menu.agregarCerrarSesionListener(e -> {
                usuarioController.cerrarSesion();
                loginView.mostrarSesionCerrada();
                loginView.mostrarInicioSesion();
            });

            loginView.integrarRegistro(registroUsuarioView);
            registroUsuarioView.setVisible(false);
            loginView.setVisible(true);
            menu.setVisible(false);

            usuarioController.agregarObservadorSesion(usuario -> {
                boolean activa = usuario != null;
                if (activa) {
                    menu.mostrarUsuario(usuario.getNombreUsuario());
                    consultaObjetoView.mostrarObjetos(objetoController.consultarObjetos());
                    historialView.mostrarDevoluciones(devolucionController.consultarDevoluciones());
                    notificacionView.mostrarNotificaciones(
                            notificacionController.consultarNotificaciones());
                    menu.seleccionarPestana(0);
                } else {
                    menu.seleccionarPestana(0);
                    menu.mostrarUsuario("Sin sesión activa");
                    notificacionView.limpiarNotificaciones();
                }
                menu.setVisible(activa);
                loginView.setVisible(!activa);
            });
        });
    }

    // Adaptador temporal para vistas JFrame existentes hasta que Rivera entregue JPanel.
    private static JPanel extraerPanel(javax.swing.JFrame vista) {
        JPanel panel = new JPanel(new BorderLayout());
        java.awt.Container contenido = vista.getContentPane();
        vista.setContentPane(new JPanel());
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }
}
