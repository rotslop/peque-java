/*
 * Analisis Sintáctico
 *
 * Proceso:
 *
 * - Carga de la Tabla de Analisis Lexico LL(1)
 *      - Sera una matriz identica a la hoja de calculo
 *      - Por cada casilla se eliminará el elemento xxx -->
 *      - Serán almacenados como cadena
 * - Los elementos Detectados por el Analizador Lexico
 *      - Serán almacenados en un Array o ArrayList
 *      - Se reemplzarán los tokens de los símbolos { } ( ) ;
 *        por su valor caracter. (xq la tabla los tiene asi )
 * - El analisis empezará leyendo como entrada el elemento en
 *   la posición 0 del la salida del Analizador Lexico
 * - Idear un proceso de búsqueda de coincidencias en la tabla
 * - Idear un proceso para invertir la correspondencia de la tabla
 * - Manejador de Coincidencias y Detector de Errores.
 *
 */
package clases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author qmarqeva
 */
public class Sintactico {

    //Tabla de Coincidencias Analisis LL(1)
    private static String[][] tabla = new String[31][37];

    /**
     * Cargará la estructura de la tabla desde un archivo.
     * Estado : COMPLETADO
     */
    public static void cargarTabla() {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        String linea = null;

        int fil = 0;


        try {
            archivo = new File("archivos\\Tabla.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            String[] datos = null;
            System.out.println(" SALIDA ");

            while ((linea = br.readLine()) != null) {
                datos = linea.split("\t");
                int col = 0;
                for (int i = 0; i < datos.length; i++) {

                    if (!datos[i].isEmpty()) {

                        //Eliminar la parte de xxx-->
                        int indice = datos[i].indexOf("-->");
                        if (indice > 0) {
                            int k = indice + 3;
                            datos[i] = datos[i].substring(k).trim();
                        }
                        //almacenar en la matriz
                        tabla[fil][col] = datos[i];
                    }
                    col++;
                }
                fil++;
            }

            //Capturamos las posibles excepciones
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * Se puede Borrar
     * Es solo para prueba
     * ESTADO : COMPLETADO
     */
    public static void presentarTabla() {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 36; j++) {
                System.out.print("[" + tabla[i][j] + "]");
            }
            System.out.println("");
        }
    }

    /**
     * Reemplaza los tokens que necesitan ser caracteres
     * para ser reconocidos como { } ( ) y otros.
     *
     * @param token
     * @return símbolo que representa al token
     */
    public String reemplazarToken(String token) {
        return "";
    }

    /**
     * Buscará en la cabecera de la tabla el elemento
     * terminal
     * @param terminal
     * @return posición (columna) o -1 sino existe
     */
    public int buscarElementoTerminal(String terminal) {
        return -1;
    }

    /**
     * Buscará en la columna izquierda de la tabla el elemento
     * no terminal solicitado
     * @param noterminal
     * @return posición (columna) o -1 sino existe
     */
    public int buscarElementoNoTerminal(String noterminal) {
        return -1;
    }

    /**
     * Devuelve el contenido de la coincidencia en la tabla
     * LL(1), esta deberá ser luego separada por caracteres
     * @param fil
     * @param col
     * @return coincidencia de la tabla
     */
    public String extraerConcidencia(int fil, int col) {
        return "";
    }

    /**
     * Elimina el elemento del inicio del arreglo cuando existe una coincidencia
     * en la Entrada
     */
    public void elimCndEntrada() {
    }

    /**
     * Elimina el elemento del final del arreglo de Analisis
     * al Encontrar una coincidencia
     */
    public void elimCndAnalisis() {
    }

    /**
     * Invierte la coincidencia de la tabla
     * y la inserta al final del arreglo de Analisis
     * previa la eliminación del anterior.
     * @param entrada
     */
    public void invertir(String entrada) {
    }

    //AREA DE TESTING
    public static void main(String[] args) {

        cargarTabla();
        presentarTabla();

    }
}
