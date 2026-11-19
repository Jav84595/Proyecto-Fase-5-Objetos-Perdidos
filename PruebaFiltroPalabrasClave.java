import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PruebaFiltroPalabrasClave {

    private static int correctas = 0;
    private static int fallidas = 0;

    private static void verificar(String nombre, boolean condicion) {
        if (condicion) {
            correctas++;
            System.out.println("CORRECTA: " + nombre);
        } else {
            fallidas++;
            System.out.println("FALLO: " + nombre);
        }
    }

    public static void main(String[] args) {

        Objeto mochila = new Objeto(
                1, "Mochila deportiva", "Color negro",
                "Biblioteca", LocalDate.of(2026, 10, 20));

        Objeto telefono = new Objeto(
                2, "Teléfono Samsung", "Pantalla rota",
                "Cafetería", LocalDate.of(2026, 10, 21));

        Objeto libro = new Objeto(
                3, "Libro de cálculo", "Portada azul",
                "Salón 101", LocalDate.of(2026, 10, 22));

        Objeto sinDescripcion = new Objeto(
                4, "Llaves", null,
                "Laboratorio", LocalDate.of(2026, 10, 23));

        Objeto sinNombre = new Objeto(
                5, null, "Objeto metálico",
                "Recepción", LocalDate.of(2026, 10, 24));

        List<Objeto> objetos = new ArrayList<>(Arrays.asList(
                mochila, telefono, libro,
                sinDescripcion, sinNombre));

        verificar("Una palabra",
                new FiltroPalabrasClave("mochila")
                        .filtrar(objetos).contains(mochila));

        verificar("Varias palabras",
        new FiltroPalabrasClave("mochila negro")
                        .filtrar(objetos).equals(
                                Collections.singletonList(mochila)));


	verificar("Palabras en distinto orden",
        new FiltroPalabrasClave("negro mochila")
                .filtrar(objetos).equals(
                        Collections.singletonList(mochila)));

        verificar("Palabras repartidas entre campos",
                new FiltroPalabrasClave("deportiva negro")
                        .filtrar(objetos).equals(
                                Collections.singletonList(mochila)));

        verificar("Mayusculas y minusculas",
                new FiltroPalabrasClave("MOCHILA NEGRO")
                        .filtrar(objetos).equals(
                                Collections.singletonList(mochila)));

        verificar("Acentos",
                new FiltroPalabrasClave("telefono")
                        .filtrar(objetos).equals(
                                Collections.singletonList(telefono)));

        verificar("Espacios repetidos",
                new FiltroPalabrasClave("  mochila   negro  ")
                        .filtrar(objetos).equals(
                                Collections.singletonList(mochila)));

        verificar("Sin coincidencias",
                new FiltroPalabrasClave("computadora")
                        .filtrar(objetos).isEmpty());

        verificar("Busqueda vacia",
                new FiltroPalabrasClave("")
                        .filtrar(objetos).equals(objetos));

        verificar("Lista vacia",
                new FiltroPalabrasClave("mochila")
                        .filtrar(Collections.emptyList()).isEmpty());

        verificar("Lista nula",
                new FiltroPalabrasClave("mochila")
                        .filtrar(null).isEmpty());

        verificar("Descripcion nula",
                new FiltroPalabrasClave("llaves")
                        .filtrar(objetos).contains(sinDescripcion));

        verificar("Nombre nulo",
                new FiltroPalabrasClave("metalico")
                        .filtrar(objetos).contains(sinNombre));

        verificar("Objetos originales conservados",
                objetos.size() == 5
                        && mochila.getNombre().equals("Mochila deportiva")
                        && telefono.getNombre().equals("Teléfono Samsung"));

        verificar("Lista nueva",
                new FiltroPalabrasClave("")
                        .filtrar(objetos) != objetos);

        System.out.println();
        System.out.println("Pruebas correctas: " + correctas);
        System.out.println("Pruebas fallidas: " + fallidas);

        if (fallidas > 0) {
            throw new AssertionError(
                    "El filtro no supero todas las pruebas.");
        }

        System.out.println("TODAS LAS PRUEBAS FUERON SATISFACTORIAS");
    }
}