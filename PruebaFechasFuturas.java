import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class PruebaFechasFuturas {
    private static int correctas = 0;

    private static void rechaza(String inicio, String fin) {
        try {
            new FiltroRangoFechas(inicio, fin);
            throw new AssertionError("Se acepto una fecha futura: " + inicio + " / " + fin);
        } catch (IllegalArgumentException e) {
            if (!e.getMessage().contains("No se permiten fechas futuras")) {
                throw new AssertionError("Mensaje inesperado: " + e.getMessage());
            }
            correctas++;
        }
    }

    public static void main(String[] args) {
        LocalDate hoy = LocalDate.now();
        LocalDate ayer = hoy.minusDays(1);
        LocalDate manana = hoy.plusDays(1);

        rechaza(manana.toString(), manana.toString());
        rechaza(manana.toString(), hoy.toString());
        rechaza(ayer.toString(), manana.toString());

        FiltroRangoFechas filtro = new FiltroRangoFechas(ayer.toString(), hoy.toString());
        List<Objeto> objetos = Arrays.asList(
                new Objeto(1, "Objeto ayer", "Prueba", "UVG", ayer),
                new Objeto(2, "Objeto hoy", "Prueba", "UVG", hoy));
        if (filtro.filtrar(objetos).size() != 2) {
            throw new AssertionError("Las fechas permitidas no se filtraron correctamente.");
        }
        correctas++;
        System.out.println("Pruebas correctas: " + correctas + "/4");
    }
}
