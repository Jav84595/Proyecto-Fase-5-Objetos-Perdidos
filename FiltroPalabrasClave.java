import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FiltroPalabrasClave implements FiltroAvanzado {

    private final String textoBusqueda;

    public FiltroPalabrasClave(String textoBusqueda) {
        this.textoBusqueda = normalizar(textoBusqueda);
    }

    private static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }

        String resultado = Normalizer.normalize(
                texto, Normalizer.Form.NFD);

        resultado = resultado.replaceAll("\\p{M}+", "");

        return resultado.toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("\\s+", " ");
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

        String[] palabras = textoBusqueda.split(" ");

        for (Objeto objeto : objetos) {
            if (objeto == null) {
                continue;
            }

            String nombre = objeto.getNombre() == null
                    ? "" : objeto.getNombre();

            String descripcion = objeto.getDescripcion() == null
                    ? "" : objeto.getDescripcion();

            String contenido = normalizar(
                    nombre + " " + descripcion);

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