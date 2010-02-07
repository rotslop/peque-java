/*
 * Analisis Sintáctico
 *
 * Proceso:
 *
 * - Carga de la Tabla de Analisis Lexico LL(1)
 *      - Sera una matriz identica a la hoja de calculo (listo)
 *      - Por cada casilla se eliminará el elemento xxx --> (listo)
 *      - Serán almacenados como cadena (listo)
 * - Los elementos Detectados por el Analizador Lexico
 *      - Serán almacenados en un Array o ArrayList (listo)
 *      - Se reemplzarán los tokens de los símbolos { } ( ) ;
 *        por su valor caracter. (xq la tabla los tiene asi )(listo)
 * - El analisis empezará leyendo como entrada el elemento en
 *   la posición 0 del la salida del Analizador Lexico
 * - Idear un proceso de búsqueda de coincidencias en la tabla
 * - Idear un proceso para invertir la correspondencia de la tabla
 * - Manejador de Coincidencias y Detector de Errores.
 *
 * PROBLEMAS:
 * - El analizador léxico no reconce todos los ; en la salida
 * - Ignora la parte de VARIABLES INICIO FIN en la salida
 *      Esto hace que el léxico de errores luego.... ojo
 * - Se necesita que la salida del analizador separe los == <= >= <>
 *   como símbolos a parte.
 */
package clases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author qmarqeva
 */
public class Sintactico {

    //Tabla de Coincidencias Analisis LL(1)
    private static String[][] tabla = new String[31][37];
    //Pila de Analisis Sintáctico
    private static ArrayList<String> pila = new ArrayList<String>();
    //Entrada de Prueba
    //- Ejemplo 1
    private static String[] datos = {"VARIABLES", "VAR", "ID", "BOOL", ";",
        "INICIO", "LEER", "(", "ID", ")", ";", "FIN", "$", ""};
    //- Ejemplo 2
//       private static String[] datos = {"VARIABLES", "VAR", "ID", "ENTERO", ";", "VAR",
//        "ID", "CADENA", ";", "INICIO", "REPETIR", "(",
//        "ID", "=", "=", "NUM", ")", "{", "ESCRIBIR", "(",
//        "NUM", ")", ";", "}", "SI", "(", "TRUE", ")",
//        "{", "}", "SINO", "{", "ID", "=", "NUM", "+",
//        "NUM", ";", "}", "FIN", "$"};
    //- Ejemplo 3
//    private static String[] datos = {"VARIABLES","VAR","ID","BOOL",";","INICIO",
//                                "PARA","(","ID","=","NUM",";","NUM",";","NUM",
//                                ")","{","OPCION","(","ID","+","NUM",")","{",
//                                "CASO","NUM",":","ESCRIBIR","(","CADENA",")",
//                                ";","CASO","NUM",":","ESCRIBIR","(","CADENA",
//                                ")",";","SINO",":","LEER","(","ID",")",";",
//                                "}","}","FIN","$"};
    static ArrayList<String> intro = new ArrayList<String>();

    /**
     * Solo para pruebas
     * Crea un ArrayList similar al que envia el
     * Analizador Lexico
     */
    public static void llenado(String[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            intro.add(tokens[i]);
        }
        pila.add("$");
        pila.add("PROGRAMA");
    }

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
            archivo = new File("archivos\\Tabla2.txt");
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
        System.out.println("TABLA LL(1) ok");
        corregir();
    }

    /**
     * Corregir Tabla
     */
    public static void corregir() {
        for (int i = 0; i < tabla.length; i++) {
            for (int j = 0; j < tabla[0].length; j++) {
                if (tabla[i][j] == null) {
                    tabla[i][j] = "ERROR";
                }
            }
        }
        System.out.println("CORRECION ok");
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
     *
     * ESTADO : COMPLETADO
     */
    public static String reemplazarToken(String token) {
        HashMap tok = new HashMap();
        tok.put("SUM", "+");
        tok.put("RESTA", "-");
        tok.put("FIN LINEA", ";");
        tok.put("ASIGNACION", "=");
        tok.put("IGUALDAD", "==");
        tok.put("MAYOR QUE", ">");
        tok.put("MAYOR_IGUAL QUE", ">=");
        tok.put("MENOR QUE", "<");
        tok.put("MENOR IGUAL QUE", "<=");
        tok.put("INI_PAR", "(");
        tok.put("FIN_PAR", ")");
        tok.put("INI_LLA", "}");
        tok.put("FIN_LLA", "}");

        if (tok.get(token) == null) {
            return "";
        }
        return (String) tok.get(token);
    }

    /**
     * Buscará en la cabecera de la tabla el elemento
     * terminal
     * @param terminal
     * @return posición (columna) o -1 sino existe
     *
     * ESTADO : TERMINADO
     */
    public static int buscarElementoTerminal(String terminal) {
        for (int i = 0; i < tabla[0].length; i++) {
            if (tabla[0][i].equals(terminal)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Buscará en la columna izquierda de la tabla el elemento
     * no terminal solicitado
     * @param noterminal
     * @return posición (columna) o -1 sino existe
     *
     * ESTADO : TERMINADO
     */
    public static int buscarElementoNoTerminal(String noterminal) {
        for (int i = 0; i < tabla.length; i++) {
            if (tabla[i][0].equals(noterminal)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Devuelve el contenido de la coincidencia en la tabla
     * LL(1), esta deberá ser luego separada por caracteres
     * @param fil
     * @param col
     * @return coincidencia de la tabla
     */
    public static String extraerConcidencia(int fil, int col) {
        return tabla[fil][col];
    }

    /**
     * Elimina el elemento del fin y de inicio de
     * la pila y de la entrada respectivamente.
     * ESTADO : COMPLETO
     */
    public static void coincidencia() {
        System.out.print("COINCIDENCIA [" + pila.get(pila.size() - 1) + "]==[" + intro.get(0) + "]");
        //Elimina el último de la pila
        pila.remove(pila.size() - 1);
        //Elimina el primero de la entrada
        intro.remove(0);
    }

    /**
     * Invierte la coincidencia de la tabla
     * y la inserta al final del arreglo de Analisis
     * previa la eliminación del anterior.
     * @param entrada
     * ESTADO : COMPLETO
     */
    public static void invertir(String entrada) {
        String[] dat = entrada.split(" ");
        //Elimina el último
        pila.remove(pila.size() - 1);
        //Lo reemplaza con la coincidencia inversa
        for (int i = dat.length - 1; i > -1; i--) {
            pila.add(dat[i]);
        }
    }

    /**
     * Presentación Arreglos
     */
    public static void presentar() {
        System.out.println("");
        for (String a : pila) {
            System.out.print(a + "  ");
        }
        System.out.print("              ");
        for (String a : intro) {
            System.out.print(a + "  ");
        }
    }

    public static void analisisSintactico(String[] tokens) {
        cargarTabla();
        //   presentarTabla();

        llenado(tokens);
        System.out.println("--------------");
        boolean error = false;
        boolean continuar = true;

        presentar();

        while (continuar && !error) {
            int col = buscarElementoTerminal(intro.get(0));
            int fil = buscarElementoNoTerminal(pila.get(pila.size() - 1));

            //System.out.println("[" + pila.get(pila.size() - 1) + "][" + intro.get(0) + "]");

            //Localizo el elemento en la tabla
            String sal = extraerConcidencia(fil, col);
            if (sal.equals("ERROR")) {
                System.out.println("ERROR EN LA ENTRADA[" + pila.get(pila.size() - 1) + "][" + intro.get(0) + "]");
                error = true;
                continuar = false;
                break;
            }
            //Es vacío, elimino el último elemento de la pila.
            if (sal.toUpperCase().equals("E")) {
                pila.remove(pila.size() - 1);
            } else {
                //convierto la coincidencia a la inversa en la pila
                invertir(sal);
            }

            presentar();

            //Elimino todas las coincidencias
            while (pila.get(pila.size() - 1).equals(intro.get(0))) {
                //Verificar si ya terminó
                if ((pila.get(pila.size() - 1).equals("$")) && (intro.get(0).equals("$"))) {
                    System.out.println("GRAMATICA TERMINADA [EXITO]");
                    continuar = false;
                    break;
                } else {
                    coincidencia();
                }

            }
        }

        //Presento si hubo errores.
        if (error) {
            System.out.println("GRAMATICA CON ERRORES");
        }
    }
    //AREA DE TESTING
    public static void main(String[] args) {
        analisisSintactico(datos);
    }
}
