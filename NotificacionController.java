import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class NotificacionController {

    private final NotificacionDAO notificacionDAO;
    private final Supplier<Usuario> usuarioActual;

    public NotificacionController(
            NotificacionDAO notificacionDAO,
            Supplier<Usuario> usuarioActual) {

        if (notificacionDAO == null || usuarioActual == null) {
            throw new IllegalArgumentException(
                    "El DAO y el proveedor de sesion son obligatorios.");
        }

        this.notificacionDAO = notificacionDAO;
        this.usuarioActual = usuarioActual;
    }

    private boolean autorizado() {
        return usuarioActual.get() != null;
    }

    public boolean registrarNotificacion(
            String titulo, String mensaje,
            String tipo, int objetoId) {

        if (!autorizado()
                || titulo == null || titulo.trim().isEmpty()
                || mensaje == null || mensaje.trim().isEmpty()
                || objetoId <= 0
                || !tipoValido(tipo)) {
            return false;
        }

        Notificacion notificacion = new Notificacion(
                0,
                titulo.trim(),
                mensaje.trim(),
                tipo,
                objetoId,
                LocalDateTime.now()
        );

        return notificacionDAO.guardar(notificacion);
    }

    public boolean notificarObjetoRegistrado(
            int objetoId, String nombreObjeto) {

        if (nombreObjeto == null || nombreObjeto.trim().isEmpty()) {
            return false;
        }

        return registrarNotificacion(
                "Nuevo objeto encontrado",
                "Se registro un nuevo objeto: " + nombreObjeto.trim(),
                Notificacion.OBJETO_REGISTRADO,
                objetoId
        );
    }

    public boolean notificarObjetoModificado(
            int objetoId, String nombreObjeto) {

        if (nombreObjeto == null || nombreObjeto.trim().isEmpty()) {
            return false;
        }

        return registrarNotificacion(
                "Objeto actualizado",
                "Se actualizo la informacion del objeto: "
                        + nombreObjeto.trim(),
                Notificacion.OBJETO_MODIFICADO,
                objetoId
        );
    }

    public boolean notificarObjetoDevuelto(
            int objetoId, String nombreObjeto) {

        if (nombreObjeto == null || nombreObjeto.trim().isEmpty()) {
            return false;
        }

        return registrarNotificacion(
                "Objeto devuelto",
                "El objeto fue devuelto a su propietario: "
                        + nombreObjeto.trim(),
                Notificacion.OBJETO_DEVUELTO,
                objetoId
        );
    }

    public List<Notificacion> consultarNotificaciones() {
        if (!autorizado()) {
            return Collections.emptyList();
        }

        return notificacionDAO.consultarTodas();
    }

    public Notificacion buscarNotificacionPorId(int id) {
        if (!autorizado() || id <= 0) {
            return null;
        }

        return notificacionDAO.buscarPorId(id);
    }

    private boolean tipoValido(String tipo) {
        return Notificacion.OBJETO_REGISTRADO.equals(tipo)
                || Notificacion.OBJETO_MODIFICADO.equals(tipo)
                || Notificacion.OBJETO_DEVUELTO.equals(tipo);
    }
}