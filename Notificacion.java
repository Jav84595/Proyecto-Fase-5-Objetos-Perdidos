import java.time.LocalDateTime;

public class Notificacion {

    public static final String OBJETO_REGISTRADO = "OBJETO_REGISTRADO";
    public static final String OBJETO_MODIFICADO = "OBJETO_MODIFICADO";
    public static final String OBJETO_DEVUELTO = "OBJETO_DEVUELTO";

    private final int id;
    private final String titulo;
    private final String mensaje;
    private final String tipo;
    private final int objetoId;
    private final LocalDateTime fechaCreacion;

    public Notificacion(int id, String titulo, String mensaje,
                        String tipo, int objetoId,
                        LocalDateTime fechaCreacion) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.objetoId = objetoId;
        this.fechaCreacion = fechaCreacion;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public int getObjetoId() {
        return objetoId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    @Override
    public String toString() {
        return "[" + fechaCreacion + "] "
                + titulo + " - " + mensaje;
    }
}