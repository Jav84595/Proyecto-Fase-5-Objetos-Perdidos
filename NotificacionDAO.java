import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDAO {

    public NotificacionDAO() {
        crearTabla();
    }

    private void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS notificaciones ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "titulo TEXT NOT NULL, "
                + "mensaje TEXT NOT NULL, "
                + "tipo TEXT NOT NULL, "
                + "objeto_id INTEGER NOT NULL, "
                + "fecha_creacion TEXT NOT NULL, "
                + "FOREIGN KEY (objeto_id) REFERENCES objetos(id)"
                + ");";

        try (Connection conexion = ConexionBD.obtenerConexion();
             Statement statement = conexion.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No se pudo crear la tabla de notificaciones.", e);
        }
    }

    public boolean guardar(Notificacion notificacion) {
        if (notificacion == null
                || notificacion.getTitulo() == null
                || notificacion.getTitulo().trim().isEmpty()
                || notificacion.getMensaje() == null
                || notificacion.getMensaje().trim().isEmpty()
                || notificacion.getObjetoId() <= 0
                || notificacion.getFechaCreacion() == null
                || !tipoValido(notificacion.getTipo())) {
            return false;
        }

        String sql = "INSERT INTO notificaciones "
                + "(titulo, mensaje, tipo, objeto_id, fecha_creacion) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setString(1, notificacion.getTitulo().trim());
            statement.setString(2, notificacion.getMensaje().trim());
            statement.setString(3, notificacion.getTipo());
            statement.setInt(4, notificacion.getObjetoId());
            statement.setString(5,
                    notificacion.getFechaCreacion().toString());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println(
                    "Error al guardar notificacion: " + e.getMessage());
            return false;
        }
    }

    public List<Notificacion> consultarTodas() {
        List<Notificacion> notificaciones = new ArrayList<>();

        String sql = "SELECT id, titulo, mensaje, tipo, "
                + "objeto_id, fecha_creacion "
                + "FROM notificaciones ORDER BY id DESC";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement =
                     conexion.prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {
                notificaciones.add(crearNotificacion(resultado));
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al consultar notificaciones: " + e.getMessage());
        }

        return notificaciones;
    }

    public Notificacion buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }

        String sql = "SELECT id, titulo, mensaje, tipo, "
                + "objeto_id, fecha_creacion "
                + "FROM notificaciones WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return crearNotificacion(resultado);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al buscar notificacion: " + e.getMessage());
        }

        return null;
    }

    private boolean tipoValido(String tipo) {
        return Notificacion.OBJETO_REGISTRADO.equals(tipo)
                || Notificacion.OBJETO_MODIFICADO.equals(tipo)
                || Notificacion.OBJETO_DEVUELTO.equals(tipo);
    }

    private Notificacion crearNotificacion(ResultSet resultado)
            throws SQLException {

        return new Notificacion(
                resultado.getInt("id"),
                resultado.getString("titulo"),
                resultado.getString("mensaje"),
                resultado.getString("tipo"),
                resultado.getInt("objeto_id"),
                LocalDateTime.parse(
                        resultado.getString("fecha_creacion"))
        );
    }
}