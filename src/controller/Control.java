package controller;

import logic.Karnaugh;
import logic.Operacion;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.util.*;

public class Control {
    private String function;
    private JTextField jTextField;
    private JTable jTable;

    private JTable jTable2;
    private JLabel jLabel;
    public Control(String function, JTextField jTextField, JTable jTable, JTable jTable2, JLabel jLabel) {
        this.function = function;
        this.jTextField = jTextField;
        this.jTable = jTable;
        this.jTable2 = jTable2;
        this.jLabel = jLabel;
        int cont = 0;
        // validar espacios vacios
        if (validar_no_campos_vacios()==true)
            cont++;
         else
            JOptionPane.showMessageDialog(null, "Debe ingresar una función.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        //validar caracteres validos
        if (validar_caracteres() == true)
            cont++;
        else
            JOptionPane.showMessageDialog(null, "Debe ingresar caracteres validos", "Advertencia", JOptionPane.WARNING_MESSAGE);

        // *Validar operaciones
        /*
        if(validar_operaciones() == true)
            i++;
        else
            JOptionPane.showMessageDialog(null, "Operacion no valida", "Advertencia", JOptionPane.WARNING_MESSAGE);
        */

        // Validar parentesis
        if(valida_parentesis() == true)
            cont++;
        else
            JOptionPane.showMessageDialog(null, "Parentesis no validos", "Advertencia", JOptionPane.WARNING_MESSAGE);
        if (cont == 3) {
            // Contar variables unicas
            int num_vars = contarVariablesUnicas();
            int numCombinaciones = encontrarNumeroCombinaciones(num_vars);
            boolean[][] tablaDeVerdad = crearTablaDeVerdad(num_vars, numCombinaciones);
            char[] operaciones = encontrarOperaciones();
            Operacion op = new Operacion(num_vars, function, tablaDeVerdad, operaciones);
            int[] resultadoEntero = op.calcularResultadoComoEnteros();
            // tabla de verdad
            llenarTablaDinamica(jTable, tablaDeVerdad, resultadoEntero, num_vars);
            //Mapa de Karnough
            Karnaugh kaugh = new Karnaugh(num_vars, resultadoEntero);
            int[][] karnaughMap = kaugh.crearMapaKarnaugh();
            //mostrar mapa
            llenarMapaKarnaugh(jTable2, karnaughMap, num_vars);
            //Obtener resultado mapa karnaugh
            String resultado = kaugh.simplificarFuncion(karnaughMap, num_vars);
            String cadena = jLabel.getText()+resultado;
            jLabel.setText(cadena);
        }
    }
    public boolean validar_no_campos_vacios() {
        if(jTextField.getText().isEmpty())
            return false;
        else
            return true;
    }
    public boolean validar_caracteres() {
        for (char caracter : function.toCharArray()) {
            if (caracter != 'X' && caracter != 'Y' && caracter != 'Z' && caracter != 'W' &&
                    caracter != '^' && caracter != 'v' && caracter != '\'' && caracter != '(' &&
                    caracter != ')') {
                return false;
            }
        }
        return true;
    }
    public boolean validar_operaciones() {
        LinkedList<Character> lista = new LinkedList<>();

        for (char caracter : function.toCharArray()) {
            if (Character.isLetter(caracter)) {
                lista.add(caracter);
            } else if (caracter == '^' || caracter == 'v') {
                if (lista.isEmpty()) {
                    return false; // Operador antes de una variable
                }
                char variable = lista.removeLast(); // Removemos la última variable
                if (caracter == '\'' && lista.isEmpty()) {
                    return false; // Símbolo de negación al final de la cadena
                }
                if (caracter != '\'' && lista.isEmpty()) {
                    return false; // Operador antes de una variable
                }
                lista.add('V'); // Agregamos una variable ficticia para indicar que se realizó una operación.
            } else {
                return false; // Carácter no válido en la función.
            }
        }

        // Al final de la función, debe haber exactamente una variable en la lista.
        return lista.size() == 1 && Character.isLetter(lista.getLast());
    }
    public boolean valida_parentesis() {
        Stack<Character> pila = new Stack<>();
        for (char caracter : function.toCharArray()) {
            if (caracter == '(') {
                pila.push(caracter); // Si encontramos un paréntesis abierto, lo agregamos a la pila.
            } else if (caracter == ')') {
                if (pila.isEmpty() || pila.pop() != '(') {
                    // Si encontramos un paréntesis cerrado y la pila está vacía o el elemento superior de la pila no es un paréntesis abierto, la función no está equilibrada.
                    return false;
                }
            }
        }
        // Si la pila está vacía al final y no hay paréntesis vacíos, significa que todos los paréntesis se cerraron correctamente.
        return pila.isEmpty() && !function.contains("()");
    }
    public int contarVariablesUnicas() {
        Set<Character> variablesUnicas = new HashSet<>();

        for (char caracter : function.toCharArray()) {
            if (Character.isLetter(caracter) && Character.isUpperCase(caracter)) {
                variablesUnicas.add(caracter);
            }
        }

        return variablesUnicas.size();
    }
    public int encontrarNumeroCombinaciones(int num_vars) {
        int numCombinaciones = (int) Math.pow(2, num_vars);
        return numCombinaciones;
    }

    public boolean[][] crearTablaDeVerdad(int num_vars, int numCombinaciones) {

        // Crear la matriz para la tabla de verdad
        boolean[][] tablaDeVerdad = new boolean[numCombinaciones][num_vars];

        // Llenar la tabla de verdad
        for (int i = 0; i < numCombinaciones; i++) {
            int combinacion = i;
            for (int j = num_vars - 1; j >= 0; j--) {
                tablaDeVerdad[i][j] = (combinacion % 2 == 1);
                combinacion /= 2;
            }
        }
        return tablaDeVerdad;
    }
    public char[] encontrarOperaciones() {
        List<Character> operacionesEncontradas = new ArrayList<>();

        for (char caracter : function.toCharArray()) {
            if (caracter == ',' || caracter == '^' || caracter == 'v' || caracter == '\'') {
                operacionesEncontradas.add(caracter);
            }
        }

        char[] arregloOperaciones = new char[operacionesEncontradas.size()];
        for (int i = 0; i < operacionesEncontradas.size(); i++) {
            arregloOperaciones[i] = operacionesEncontradas.get(i);
        }

        return arregloOperaciones;
    }

    // Método para llenar la tabla dinámica con la tabla de verdad y el resultado
    private void llenarTablaDinamica(JTable tabla, boolean[][] tablaDeVerdad, int[] resultado, int num_vars) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setColumnIdentifiers(obtenerEncabezados(num_vars));

        for (int i = 0; i < tablaDeVerdad.length; i++) {
            Vector<Object> row = new Vector<>();

            for (int j = 0; j < num_vars; j++) {
                row.add(tablaDeVerdad[i][j] ? 1 : 0); // Convertir a 1 o 0
            }

            // Agregar el resultado al final de la fila
            row.add(resultado[i]);

            model.addRow(row);
        }
    }

    // Método para obtener los encabezados de las columnas
    private Vector<Object> obtenerEncabezados(int num_vars) {
        Vector<Object> encabezados = new Vector<>();
        // Agregar los nombres de las variables (X, Y, Z, etc.) como encabezados
        if (num_vars == 2) {
            encabezados.add('X');
            encabezados.add('Y');
        } else if (num_vars == 3){
            encabezados.add('X');
            encabezados.add('Y');
            encabezados.add('Z');
        } else {
            encabezados.add('X');
            encabezados.add('Y');
            encabezados.add('Z');
            encabezados.add('W');
        }
        // Agregar una columna adicional sin encabezado para el resultado
        encabezados.add("");
        return encabezados;
    }

    private void llenarMapaKarnaugh(JTable jTable, int[][] karnaughMap, int num_vars) {
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();

        // Limpiar la tabla antes de llenarla
        model.setRowCount(0);
        model.setColumnCount(0);

        // Encabezados de las columnas
        String[] columnHeaders = new String[(int) Math.pow(2, num_vars) + 1];

        if (num_vars == 2) {
            columnHeaders[0] = "XY";
            columnHeaders[1] = "0";
            columnHeaders[2] = "1";
        } else if (num_vars == 3) {
            columnHeaders[0] = "XYZ";
            columnHeaders[1] = "00";
            columnHeaders[2] = "01";
            columnHeaders[3] = "11";
            columnHeaders[4] = "10";
        } else if (num_vars == 4) {
            columnHeaders[0] = "XYZW";
            columnHeaders[1] = "00";
            columnHeaders[2] = "01";
            columnHeaders[3] = "11";
            columnHeaders[4] = "10";
        }

        // Agregar la primera fila con los encabezados de columna
        model.setColumnIdentifiers(columnHeaders);

        // Agregar filas y datos al mapa de Karnaugh
        for (int i = 0; i < karnaughMap.length; i++) {
            Vector<Object> row = new Vector<>();
            String binaryString = Integer.toBinaryString(i);
            while (binaryString.length() < num_vars) {
                binaryString = "0" + binaryString; // Rellenar con ceros a la izquierda
            }
            row.add(binaryString);

            for (int j = 0; j < karnaughMap[i].length; j++) {
                row.add(karnaughMap[i][j]);
            }

            model.addRow(row);
        }
    }


}
