import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FiltroRangoFechas implements FiltroAvanzado {

    private final LocalDate fechaInicial;
    private final LocalDate fechaFinal;

    public FiltroRangoFechas(String fechaInicial, String fechaFinal) {
        this.fechaInicial = LocalDate.parse(fechaInicial);
        this.fechaFinal = LocalDate.parse(fechaFinal);
    }

    @Override
    public List<Objeto> filtrar(List<Objeto> objetos) {
        List<Objeto> resultados = new ArrayList<>();

        for (Objeto objeto : objetos) {
            LocalDate fecha = objeto.getFechaEncontrado();

            if (!fecha.isBefore(fechaInicial)
                    && !fecha.isAfter(fechaFinal)) {
                resultados.add(objeto);
            }
        }

        return resultados;
    }
}
