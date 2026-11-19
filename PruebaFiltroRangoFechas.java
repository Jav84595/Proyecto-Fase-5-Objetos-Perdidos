import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PruebaFiltroRangoFechas {

    private static int correctas = 0;

    public static void main(String[] args) {
        Objeto libro = objeto(1, "Libro", "2026-11-01");
        Objeto mochila = objeto(2, "Mochila", "2026-11-10");
        Objeto telefono = objeto(3, "Telefono", "2026-11-15");
        Objeto llaves = objeto(4, "Llaves", "2026-11-20");

        List<Objeto> objetos = new ArrayList<>(
                Arrays.asList(libro, mochila, telefono, llaves));

        verificar("Rango con varios resultados",
                new FiltroRangoFechas("2026-11-01", "2026-11-15")
                        .filtrar(objetos).size() == 3);

        verificar("Rango sin resultados",
                new FiltroRangoFechas("2026-10-01", "2026-10-31")
                        .filtrar(objetos).isEmpty());

        verificar("Fechas limite incluidas",
                new FiltroRangoFechas("2026-11-01", "2026-11-15")
                        .filtrar(objetos).containsAll(
                                Arrays.asList(libro, telefono)));

        verificar("Fecha inicial igual a final",
                new FiltroRangoFechas("2026-11-10", "2026-11-10")
                        .filtrar(objetos).size() == 1);

        verificarError("Fecha inexistente",
                () -> new FiltroRangoFechas(
                        "2026-02-30", "2026-11-15"));

        verificarError("Formato incorrecto",
                () -> new FiltroRangoFechas(
                        "01/11/2026", "2026-11-15"));

        verificarError("Rango invertido",
                () -> new FiltroRangoFechas(
                        "2026-11-20", "2026-11-01"));

        verificarError("Fecha nula",
                () -> new FiltroRangoFechas(
                        null, "2026-11-15"));

        verificar("Lista vacia",
                new FiltroRangoFechas("2026-11-01", "2026-11-15")
                        .filtrar(new ArrayList<>()).isEmpty());

        verificar("Lista nula",
                new FiltroRangoFechas("2026-11-01", "2026-11-15")
                        .filtrar(null).isEmpty());

        List<Objeto> conNulos = Arrays.asList(
                libro, null,
                new Objeto(5, "Sin fecha", "Prueba",
                        "UVG", null));

        verificar("Objetos y fechas nulas",
                new FiltroRangoFechas("2026-11-01", "2026-11-15")
                        .filtrar(conNulos).size() == 1);

        List<Objeto> resultado =
                new FiltroRangoFechas("2026-11-01", "2026-11-15")
                        .filtrar(objetos);

        resultado.clear();

        verificar("Lista original sin modificaciones",
                objetos.size() == 4);

        System.out.println(
                "\nPruebas correctas: " + correctas + "/12");

        if (correctas != 12) {
            System.exit(1);
        }
    }

    private static Objeto objeto(
            int id, String nombre, String fecha) {
        return new Objeto(id, nombre, "Prueba",
                "UVG", LocalDate.parse(fecha));
    }

    private static void verificar(
            String nombre, boolean condicion) {
        if (condicion) {
            correctas++;
            System.out.println("CORRECTA: " + nombre);
        } else {
            System.out.println("FALLO: " + nombre);
        }
    }

    private static void verificarError(
            String nombre, Runnable accion) {
        try {
            accion.run();
            System.out.println("FALLO: " + nombre);
        } catch (IllegalArgumentException e) {
            correctas++;
            System.out.println("CORRECTA: " + nombre);
        }
    }
}
