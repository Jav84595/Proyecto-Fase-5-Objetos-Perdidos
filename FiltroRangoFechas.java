import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

public class FiltroRangoFechas implements FiltroAvanzado {

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ISO_LOCAL_DATE
                    .withResolverStyle(ResolverStyle.STRICT);

    private final LocalDate fechaInicial;
    private final LocalDate fechaFinal;

    public FiltroRangoFechas(String fechaInicial, String fechaFinal) {
        this.fechaInicial = convertirFecha(fechaInicial);
        this.fechaFinal = convertirFecha(fechaFinal);

        if (this.fechaInicial.isAfter(this.fechaFinal)) {
            throw new IllegalArgumentException(
                    "La fecha inicial no puede ser posterior a la fecha final.");
        }
    }

    private static LocalDate convertirFecha(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Las fechas son obligatorias.");
        }

        String fecha = texto.trim();

        if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException(
                    "Formato de fecha invalido. Use AAAA-MM-DD.");
        }

        try {
            return LocalDate.parse(fecha, FORMATO);
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "La fecha ingresada no existe o es invalida.", e);
        }
    }

    @Override
    public List<Objeto> filtrar(List<Objeto> objetos) {
        List<Objeto> resultados = new ArrayList<>();

        if (objetos == null || objetos.isEmpty()) {
            return resultados;
        }

        for (Objeto objeto : objetos) {
            if (objeto == null || objeto.getFechaEncontrado() == null) {
                continue;
            }

            LocalDate fecha = objeto.getFechaEncontrado();

            if (!fecha.isBefore(fechaInicial)
                    && !fecha.isAfter(fechaFinal)) {
                resultados.add(objeto);
            }
        }

        return resultados;
    }
}
