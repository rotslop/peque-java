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

import java.util.ArrayList;

/**
 *
 * @author qmarqeva
 */
public class Sintactico {

    //Tabla de Coincidencias Analisis LL(1)
    private ArrayList tabla = new ArrayList();

    /**
     * Cargará la estructura de la tabla desde un archivo.
     */
    public void cargarTabla(){

    }

    /**
     * Reemplaza los tokens que necesitan ser caracteres
     * para ser reconocidos como { } ( ) y otros.
     *
     * @param token
     * @return símbolo que representa al token
     */
    public String reemplazarToken(String token){
        return "";
    }
    /**
     * Buscará en la cabecera de la tabla el elemento
     * terminal
     * @param terminal
     * @return posición (columna) o -1 sino existe
     */

    public int buscarElementoTerminal(String terminal){
        return -1;
    }

   /**
     * Buscará en la columna izquierda de la tabla el elemento
     * no terminal solicitado
     * @param noterminal
     * @return posición (columna) o -1 sino existe
     */

    public int buscarElementoNoTerminal(String noterminal){
        return -1;
    }

    /**
     * Devuelve el contenido de la coincidencia en la tabla
     * LL(1), esta deberá ser luego separada por caracteres
     * @param fil
     * @param col
     * @return coincidencia de la tabla
     */
    public String extraerConcidencia(int fil, int col){
        return "";
    }

    /**
     * Elimina el elemento del inicio del arreglo cuando existe una coincidencia
     * en la Entrada
     */
    public void elimCndEntrada(){

    }

    /**
     * Elimina el elemento del final del arreglo de Analisis
     * al Encontrar una coincidencia
     */
    public void elimCndAnalisis(){

    }

    /**
     * Invierte la coincidencia de la tabla
     * y la inserta al final del arreglo de Analisis
     * previa la eliminación del anterior.
     * @param entrada
     */
    public void invertir(String entrada){

    }

    public static void main(String[] args) {
        
        System.out.println("LISTO ES LA SALIDA POR CONSOLA PARA LOS TEST");
        
    }

}
