import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {
    private static final String URL = construirURL();
    private static String construirURL() {
    String localAppData = System.getenv("LOCALAPPDATA");

    java.nio.file.Path carpeta;

    if (localAppData != null && !localAppData.isBlank()) {
        carpeta = java.nio.file.Paths.get(
                localAppData, "SistemaObjetosPerdidos");
    } else {
        carpeta = java.nio.file.Paths.get(
                System.getProperty("user.home"),
                "SistemaObjetosPerdidos");
    }

    try {
        java.nio.file.Files.createDirectories(carpeta);
    } catch (java.io.IOException e) {
        throw new IllegalStateException(
                "No se pudo crear la carpeta de datos.", e);
    }

    return "jdbc:sqlite:"
            + carpeta.resolve("objetos_perdidos.db").toAbsolutePath();
}

    private ConexionBD() {
    }

    public static Connection obtenerConexion() throws SQLException {
        Connection conexion = DriverManager.getConnection(URL);
        try (Statement statement = conexion.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return conexion;
    }

    public static void inicializarBaseDatos() {
        String sqlUsuarios =
                "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "nombre_usuario TEXT NOT NULL COLLATE NOCASE UNIQUE, "
                + "contrasena_hash TEXT NOT NULL"
                + ");";

        String sqlObjetos =
                "CREATE TABLE IF NOT EXISTS objetos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "descripcion TEXT, "
                + "lugar_encontrado TEXT NOT NULL, "
                + "fecha_encontrado TEXT NOT NULL, "
                + "estado TEXT NOT NULL DEFAULT 'DISPONIBLE' "
                + "CHECK (estado IN ('DISPONIBLE', 'DEVUELTO'))"
                + ");";

        String sqlDevoluciones =
                "CREATE TABLE IF NOT EXISTS devoluciones ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "objeto_id INTEGER NOT NULL UNIQUE, "
                + "nombre_propietario TEXT NOT NULL, "
                + "informacion_verificacion TEXT NOT NULL, "
                + "fecha_devolucion TEXT NOT NULL, "
                + "FOREIGN KEY (objeto_id) REFERENCES objetos(id)"
                + ");";

        try (Connection conexion = obtenerConexion();
             Statement statement = conexion.createStatement()) {
            statement.execute(sqlUsuarios);
            statement.execute(sqlObjetos);
            statement.execute(sqlDevoluciones);
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo inicializar la base de datos.", e);
        }
    }
}
