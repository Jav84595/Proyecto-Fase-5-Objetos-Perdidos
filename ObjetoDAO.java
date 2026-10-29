import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ObjetoDAO {
    public ObjetoDAO() {
        crearTabla();
    }

    private void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS objetos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "descripcion TEXT, "
                + "lugar_encontrado TEXT NOT NULL, "
                + "fecha_encontrado TEXT NOT NULL, "
                + "estado TEXT NOT NULL DEFAULT 'DISPONIBLE' "
                + "CHECK (estado IN ('DISPONIBLE', 'DEVUELTO'))"
                + ");";
        try (Connection conexion = ConexionBD.obtenerConexion();
             Statement statement = conexion.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo crear la tabla de objetos.", e);
        }
    }

    public boolean guardar(Objeto objeto) {
        if (objeto == null || objeto.getDescripcion() == null
                || objeto.getDescripcion().trim().isEmpty()) return false;
        String sql = "INSERT INTO objetos "
                + "(nombre, descripcion, lugar_encontrado, fecha_encontrado, estado) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, objeto.getNombre());
            statement.setString(2, objeto.getDescripcion());
            statement.setString(3, objeto.getLugarEncontrado());
            statement.setString(4, objeto.getFechaEncontrado().toString());
            statement.setString(5, objeto.getEstado());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar objeto: " + e.getMessage());
            return false;
        }
    }

    // Inserta y devuelve el registro con el ID asignado por SQLite.
    // null indica que la insercion no se completo.
    public Objeto guardarYObtener(Objeto objeto) {
        if (objeto == null || objeto.getDescripcion() == null
                || objeto.getDescripcion().trim().isEmpty()
                || objeto.getNombre() == null
                || objeto.getNombre().trim().isEmpty()
                || objeto.getLugarEncontrado() == null
                || objeto.getLugarEncontrado().trim().isEmpty()
                || objeto.getFechaEncontrado() == null
                || objeto.getFechaEncontrado().isAfter(LocalDate.now())) return null;
        String sql = "INSERT INTO objetos (nombre, descripcion, lugar_encontrado, fecha_encontrado, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = ConexionBD.obtenerConexion()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, objeto.getNombre());
                    ps.setString(2, objeto.getDescripcion());
                    ps.setString(3, objeto.getLugarEncontrado());
                    ps.setString(4, objeto.getFechaEncontrado().toString());
                    ps.setString(5, objeto.getEstado());
                    if (ps.executeUpdate() != 1) { c.rollback(); return null; }
                }
                int id;
                try (Statement st = c.createStatement();
                     ResultSet rs = st.executeQuery("SELECT last_insert_rowid()")) {
                    if (!rs.next()) { c.rollback(); return null; }
                    id = rs.getInt(1);
                }
                if (id <= 0) { c.rollback(); return null; }
                c.commit();
                return new Objeto(id, objeto.getNombre(), objeto.getDescripcion(),
                        objeto.getLugarEncontrado(), objeto.getFechaEncontrado(), objeto.getEstado());
            } catch (SQLException ex) {
                c.rollback();
                System.err.println("Error al guardar objeto y obtener ID: " + ex.getMessage());
                return null;
            }
        } catch (SQLException ex) {
            System.err.println("Error de conexion al guardar objeto: " + ex.getMessage());
            return null;
        }
    }

    public List<Objeto> consultarTodos() {
        List<Objeto> objetos = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, lugar_encontrado, fecha_encontrado, estado "
                + "FROM objetos ORDER BY id";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {
            while (resultado.next()) objetos.add(crearObjeto(resultado));
        } catch (SQLException e) {
            System.err.println("Error al consultar objetos: " + e.getMessage());
        }
        return objetos;
    }

    public List<Objeto> buscar(String criterio) {
        List<Objeto> objetos = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, lugar_encontrado, fecha_encontrado, estado "
                + "FROM objetos WHERE LOWER(nombre) LIKE LOWER(?) "
                + "OR LOWER(lugar_encontrado) LIKE LOWER(?) ORDER BY id";
        String busqueda = "%" + criterio + "%";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, busqueda);
            statement.setString(2, busqueda);
            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) objetos.add(crearObjeto(resultado));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar objetos: " + e.getMessage());
        }
        return objetos;
    }

    public Objeto buscarPorId(int id) {
        String sql = "SELECT id, nombre, descripcion, lugar_encontrado, fecha_encontrado, estado "
                + "FROM objetos WHERE id = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) return crearObjeto(resultado);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar objeto por ID: " + e.getMessage());
        }
        return null;
    }


    public List<Objeto> consultarPorEstado(String estado) {
        List<Objeto> objetos = new ArrayList<>();

        if (!Objeto.ESTADO_DISPONIBLE.equals(estado)
                && !Objeto.ESTADO_DEVUELTO.equals(estado)) {
            return objetos;
        }

        String sql = "SELECT id, nombre, descripcion, lugar_encontrado, "
                + "fecha_encontrado, estado "
                + "FROM objetos WHERE estado = ? ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, estado);

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    objetos.add(crearObjeto(resultado));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar objetos por estado: "
                    + e.getMessage());
        }

        return objetos;
    }





    public boolean modificarDatos(int id, String nombre, String descripcion, String lugar, LocalDate fecha) {
        if (id <= 0 || nombre == null || nombre.trim().isEmpty()
                || descripcion == null || descripcion.trim().isEmpty()
                || lugar == null || lugar.trim().isEmpty() || fecha == null
                || fecha.isAfter(LocalDate.now())) return false;
        String sql = "UPDATE objetos SET nombre=?, descripcion=?, lugar_encontrado=?, fecha_encontrado=? WHERE id=?";
        try (Connection c = ConexionBD.obtenerConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombre.trim()); ps.setString(2, descripcion == null ? "" : descripcion.trim());
            ps.setString(3, lugar.trim()); ps.setString(4, fecha.toString()); ps.setInt(5, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { System.err.println("Error al editar objeto: " + e.getMessage()); return false; }
    }

    // El estado solo puede cambiar mediante la transaccion de DevolucionDAO.
    // Se conserva la firma para compatibilidad con codigo existente.
    // Ni DEVUELTO ni DISPONIBLE pueden asignarse directamente desde este DAO.
    public boolean actualizarEstado(int id, String nuevoEstado) {
        return false;
    }

    private Objeto crearObjeto(ResultSet resultado) throws SQLException {
        return new Objeto(
                resultado.getInt("id"),
                resultado.getString("nombre"),
                resultado.getString("descripcion"),
                resultado.getString("lugar_encontrado"),
                LocalDate.parse(resultado.getString("fecha_encontrado")),
                resultado.getString("estado"));
    }
}
