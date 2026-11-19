import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FiltroPalabrasClave implements FiltroAvanzado {

    private final String textoBusqueda;

    public FiltroPalabrasClave(String textoBusqueda) {
        this.textoBusqueda = textoBusqueda == null
                ? "" : textoBusqueda.trim().toLowerCase(Locale.ROOT);
    }

    @Override
    public List<Objeto> filtrar(List<Objeto> objetos) {
        List<Objeto> resultados = new ArrayList<>();

        if (objetos == null) {
            return resultados;
        }

        if (textoBusqueda.isEmpty()) {
            resultados.addAll(objetos);
            return resultados;
        }

        String[] palabras = textoBusqueda.split("\\s+");

        for (Objeto objeto : objetos) {
            if (objeto == null) {
                continue;
            }

            String nombre = objeto.getNombre() == null
                    ? "" : objeto.getNombre();

            String descripcion = objeto.getDescripcion() == null
                    ? "" : objeto.getDescripcion();

            String contenido = (nombre + " " + descripcion)
                    .toLowerCase(Locale.ROOT);

            boolean coincide = true;

            for (String palabra : palabras) {
                if (!contenido.contains(palabra)) {
                    coincide = false;
                    break;
                }
            }

            if (coincide) {
                resultados.add(objeto);
            }
        }

        return resultados;
    }
}