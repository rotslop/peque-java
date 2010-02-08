/*
 * Funciones utilizadas por el Analizador
 *
 * En la presentación ignora las palabras reservadas:
 * VARIABLES
 * INICIO
 * FIN 
 */
package clases;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author qmarqeva
 */
public class Analizador {

    //Tablas Compactadas
    static int tabla1[][] = null;
    static int tabla2[][] = null;
    //Llaves de los Tokens
    static HashMap token = null;
    static HashMap codigo = null;
    //Salida de Tokens
    static ArrayList<String> salida = new ArrayList<String>();
    private static String terminalLexico = "";

    /**
     * Carga todos los archivos necesarios del Analizador
     *
     * @author qmarqeva
     */
    public static void cargarArchivos() {
        //Carga todo los archivos necesarios.
        cargarCompactas();
        cargarTokens();
        cargarCodigoCaracteres();

    }

    /**
     * Inicia todo el proceso de lectura
     *
     * @param texto un vector conteniendo todas las líneas
     * de codigo que se analizarán
     *
     * @author qmarqeva
     */
    public static int analizar(String[] texto) {
        System.out.println("********ANALISIS********");
        terminalLexico += "********ANÁLISIS LÉXICO********\n";
        int error = 0;
        for (int i = 0; i < texto.length; i++) {
            System.out.print("line number[" + (i + 1) + "]:");
            terminalLexico += "Número de Linea[" + (i + 1) + "]: ";
            error += lecturaBuffer(texto[i]);
            System.out.println("");
            terminalLexico += "\n";
        }
        System.err.println("****ATENCIÓN: SE DETECTARON [" + error + "] ERRORES****");
        terminalLexico += "\n****ATENCIÓN: SE DETECTARON [" + error + "] ERRORES****\n";

        //Añadir elemento del Final $
        salida.add("$");

        System.out.println("-------------------------------");
        for (String s : salida) {
            System.out.print("[" + s + "] ");
        }
        System.out.println("");
        System.out.println("-------------------------------");
        //Analisis Sintáctico
        if(Sintactico.analisisSintactico(salida)){
            error = 1;
        }else{
            error = 0;
        }
        return error;
    }

    /**
     * Retorna el arreglo de la salida para mandarlo a analisar en el sisntactico
     *
     * @return ArrayList<String> salida lista de las palabras a pasarse a analisar
     */
    public static ArrayList<String> getSalida() {
        return salida;
    }

    /**
     * Retorna el arreglo de la salida para mandarlo a analisar en el sisntactico
     *
     * @return ArrayList<String> salida lista de las palabras a pasarse a analisar
     */
    public static void setResetSalida() {
        salida.clear();
    }

    /**
     * Realiza el reconocimiento de los elementos contenidos en el buffer.
     * @param buffer Contiene el texto a ser analizado
     *
     * @author qmarqeva
     */
    private static int lecturaBuffer(String buffer) {
        int error = 0;

        int num = 0;
        for (int i = 0; i < buffer.length(); i++) {
            int numcaracter = buscarCodigo(String.valueOf(buffer.charAt(i)));
            num = encontrar(tabla1, tabla2, numcaracter, tabla1[num][1], tabla1[num][0]);
            if (num == 0) {
                System.out.print("ERROR  ");
                terminalLexico += "ERROR  ";
                error++;
                //System.out.print("["+buffer.charAt(i)+"]  "+ i);
                if (buffer.indexOf(" ", i) > i) {
                    i = buffer.indexOf(" ", i);
                } else {
                    i = buffer.length() + 120;
                }
            } else if (num < 0) {
                String aux = buscarToken(String.valueOf(num));
                System.out.print(aux + "  ");
                terminalLexico += aux + "  ";
                num = 0;
                //Control de espacio al inicio FOR REVIEW
                if ((i + 2) < (buffer.length() - 1)) {
                    if (String.valueOf(buffer.charAt(i + 1)).equals(" ")) {
                        i = i + 1;
                    }
                }
            }
        }
        return error;
    }

    /**
     * Carga las Tablas Compactas
     * desde un archivo separarado por comas.
     *
     * @author qmarqeva
     */
    private static void cargarCompactas() {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            //TODO:
            //La ruta debería ser relativa.
            archivo = new File("archivos\\compactas.csv");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Línea del Fichero
            String linea;

            //Elementos de la línea
            String[] numeros;

            //Filas Procesadas
            int filas = 0;

            numeros = br.readLine().split(";");
            numeros = br.readLine().split(";");

            //Filas de la Primer Tabla
            int limite1 = Integer.parseInt(numeros[0]);
            tabla1 = new int[limite1][2];

            //Filas de la Segunda Tabla
            int limite2 = Integer.parseInt(numeros[3]);
            tabla2 = new int[limite2][2];
            while ((linea = br.readLine()) != null) {
                numeros = linea.split(";");

                if (filas < limite1) {
                    tabla1[filas][0] = Integer.parseInt(numeros[1]);
                    tabla1[filas][1] = Integer.parseInt(numeros[2]);
                }
                tabla2[filas][0] = Integer.parseInt(numeros[4]);
                tabla2[filas][1] = Integer.parseInt(numeros[5]);

                filas++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                    System.out.println("Compactas OK");
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }


    }

    /**
     * Presenta la información de una matriz
     * Para verificar
     *
     * @author qmarqeva
     */
    private static void presentar(int[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                System.out.print(mat[i][j] + "   ");
            }
            System.out.println();
        }
    }

    /**
     *
     * @param mat1   Tabla 1
     * @param mat2   Tabla 2
     * @param numCaracter   Codigo del Caracter a buscar
     * @param posibError      Posibilidad de Error
     * @param filaInicio    Fila dentro de la Tabla 2 a donde Iniciar
     * @return      El valor de la fila en la Tabla 1 o Token o 0 si hay error.
     *
     * @author qmarqeva
     */
    private static int encontrar(int[][] mat1, int[][] mat2, int numCaracter,
            int posibError, int filaInicio) {

        //Restado debido a los indices del archivo de Excel.
        //CUIDADO =;-)
        filaInicio--;
        for (int i = filaInicio; i < (filaInicio + posibError); i++) {
            if (mat2[i][1] == numCaracter) {
                return mat2[i][0];
            }
        }
        return 0;
    }

    /**
     * Carga los elementos de cada token
     *
     * @author qmarqeva
     */
    private static void cargarTokens() {

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        token = new HashMap();

        try {
            //La ruta debería ser relativa.
            archivo = new File("archivos\\tokens.csv");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Línea del Fichero
            String linea;

            //Elementos de la línea
            String[] data;

            while ((linea = br.readLine()) != null) {
                data = linea.split(";");
                token.put(data[0], data[1]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                    System.out.println("Token OK");
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     *  Devuelve el valor del token de acuerdo a una clave enviada.
     * @param key el valor devuelto de las tablas compactas.
     * @return El valor del token encontrado o "" vacio sino existe.
     *
     * @author qmarqeva
     */
    private static String buscarToken(String key) {
        String valor = (String) token.get(key);
        if (valor != null) {

            //Guardando la Salida
            String aux2 = Sintactico.reemplazarToken(valor);

            if (aux2.equals("")) {
                salida.add(valor);
                if (valor.equals("CADENA") || valor.equals("BOOL") || valor.equals("ENTERO")) {
                    salida.add(";");
                }
            } else {
                //Para símbolos compuestos
                //En base a la respuesta de
                //reemplazarToken(valor)

                String[] alfa = aux2.split(" ");
                for (int i = 0; i < alfa.length; i++) {
                    salida.add(alfa[i]);
                }
            }
            return valor + "[" + key + "]";
        }
        return "";
    }

    /**
     * Carga la correspondencia de caracteres con su código 
     * para trabajar dentro del analisis léxico.
     *
     * @author qmarqeva
     */
    private static void cargarCodigoCaracteres() {

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        codigo = new HashMap();

        try {
            //La ruta debería ser relativa.
            archivo = new File("archivos\\codigos.csv");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Línea del Fichero
            String linea;

            //Elementos de la línea
            String[] data;

            while ((linea = br.readLine()) != null) {
                data = linea.split(";");
                //Hay un error en la lectura de este símbolo '
                //el error viene desde el archivo de excel   OJO
                //por eso hay que ponerlo manualmente.

                if (Integer.parseInt(data[0]) == 47) {
                    codigo.put("'", data[0]);
                } else {
                    if (Integer.parseInt(data[0]) == 44) {
                        codigo.put(";", data[0]);
                    } else {
                        codigo.put(data[1], data[0]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                    System.out.println("Codigos OK");
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * Devuelve el codigo de un caracter.
     * @param caracter
     * @return código numérico del caracter enviado o 0 si no lo encuentra.
     *
     * @author qmarqeva
     *
     * Modificado christmo
     */
    private static int buscarCodigo(String caracter) {
        String valor = (String) codigo.get(caracter.toUpperCase());
        if (valor == null) {
            return 0;
        }
        return Integer.valueOf(valor);
    }

    /**
     * A travez de expresiones regulares se quita todos los espacios que sean >1
     * y los tabs o espacios que se encuentren al iniciar el codigo para que no
     * cause problemas en la validacion de los caracteres.
     * @param strCodigo Codigo de Java_ol
     * @return  String[]    Lineas del codigo sin epacios al inicio o espacios repetidos
     *
     * @autor christmo
     */
    public static String[] quitarEspaciosLineas(String texto) {
        String[] lineas = texto.split("\n");
        String[] Salida = new String[lineas.length];
        boolean inicio = false;
        for (int i = 0; i < lineas.length; i++) {
            //Patron para reconocer todos los estapacios, tabs y enters del codigo que sean >1
            Pattern patron = Pattern.compile("\\s+");
            Matcher encaja = patron.matcher(lineas[i]);
            String resultado = encaja.replaceAll(" ");
            //Patron para quitar todos los espacios que esten al inicio de la linea
            Pattern patron2 = Pattern.compile("^\\s+");
            Matcher encaja2 = patron2.matcher(resultado);
            String resultado2 = encaja2.replaceAll("");
            String strCodigoListo = separadoresCodigo(resultado2);

            if (strCodigoListo.equals("VARIABLES")) {
                Salida[i] = strCodigoListo + " "; //error aqui aun hay que poner espacio al final de variables revisar christmo
                //System.out.println("--"+Salida[i]);
                inicio = false;
            }
            if (strCodigoListo.equals("INICIO")) {
                Salida[i] = strCodigoListo + " ";
                inicio = true;
            }
            if (inicio) {
                Salida[i] = strCodigoListo + " ";
            } else {
                Salida[i] = strCodigoListo;
            }
        }
        return Salida;
    }

    /**
     * Permite presentar los datos de los arreglos
     * @param texto
     *
     * @autor christmo
     */
    private static void presentarDatos(String[] texto) {
        for (int i = 0; i < texto.length; i++) {
            System.out.println(texto[i]);
        }
    }

    /**
     * Permite formatear el codigo para evitar errores en los separadores de
     * "()" y "{}" le da espacios a los lados de cada parentesis y llave que no
     * lo tenga.
     * @param strCodigo algoritmo a analizar
     * @return  String  algortmo formateado
     *
     * @autor christmo
     */
    private static String separadoresCodigo(String strCodigo) {
        String strLetra, strDespues, strNuevoCod = "";
        try {
            int limite = strCodigo.length();
            for (int i = 0; i < limite; i++) {
                if (i <= strCodigo.length() - 2) {
                    strLetra = strCodigo.substring(i, i + 1);
                    strDespues = strCodigo.substring(i + 1, i + 2);
                    //espacio al lado izquierdo del parentesis
                    if (!strLetra.equals(" ") && strDespues.equals("(")) {
                        strNuevoCod += strLetra + " ";
                        limite++;
                    } else if (!strLetra.equals(" ") && strDespues.equals(")")) {
                        strNuevoCod += strLetra + " ";
                        limite++;
                    } else {
                        strNuevoCod += strLetra;
                    }
                    //espacio al lado derecho del parentesis
                    if (strLetra.equals("(") && !strDespues.equals(" ")) {
                        strNuevoCod += " ";
                        limite++;
                    } else if (strLetra.equals(")") && !strDespues.equals(" ")) {
                        strNuevoCod += " ";
                        limite++;
                    }
                    //espacio al lado izquierdo de la llave
                    if (!strLetra.equals(" ") && strDespues.equals("}")) {
                        strNuevoCod += strLetra + " ";
                        limite++;
                    }
                    //espacio al lado derecho de la llave
                    if (strLetra.equals("{") && !strDespues.equals(" ")) {
                        strNuevoCod += " ";
                        limite++;
                    } else if (strLetra.equals("}") && !strDespues.equals(" ")) {
                        strNuevoCod += " ";
                        limite++;
                    }
                }
            }
            strNuevoCod += strCodigo.substring(strCodigo.length() - 1, strCodigo.length());
        } catch (StringIndexOutOfBoundsException e) {
            //System.out.println("Linea Vacia!!!");
            return "";
        }
        return strNuevoCod;
    }

    /**
     * @return the terminalLexico
     */
    public static String getTerminalLexico() {
        return terminalLexico;
    }

    /**
     * @param aTerminalLexico the terminalLexico to set
     */
    public static void setTerminalLexico(String aTerminalLexico) {
        terminalLexico = aTerminalLexico;
    }
}
