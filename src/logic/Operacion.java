package logic;

import javax.swing.*;
import java.util.Stack;

public class Operacion {
    private int num_vars;
    private String function;
    private boolean[][] tablaDeVerdad;
    private char[] operaciones;

    public Operacion(int num_vars, String function, boolean[][] tablaDeVerdad, char[] operaciones) {
        this.num_vars = num_vars;
        this.function = function;
        this.tablaDeVerdad = tablaDeVerdad;
        this.operaciones = operaciones;
    }

    public boolean[] calcularResultado() {
        Stack<boolean[]> valores = new Stack<>();
        Stack<Character> operadores = new Stack<>();

        for (char caracter : function.toCharArray()) {
            int variableIndex = obtenerIndiceVariable(caracter);
            if (variableIndex != -1) {
                boolean[] valor = new boolean[tablaDeVerdad.length];
                for (int i = 0; i < tablaDeVerdad.length; i++) {
                    valor[i] = tablaDeVerdad[i][variableIndex];
                }
                valores.push(valor);
            } else if (caracter == '\'' || caracter == '^' || caracter == 'v') {
                if (caracter == '\'') {
                    // Negación: Pop y negar el último valor en la pila.
                    boolean[] valor = valores.pop();
                    for (int i = 0; i < valor.length; i++) {
                        valor[i] = !valor[i];
                    }
                    valores.push(valor);
                } else {
                    while (!operadores.isEmpty() && precedencia(operadores.peek()) >= precedencia(caracter)) {
                        char operador = operadores.pop();
                        boolean[] valor2 = valores.pop();
                        boolean[] valor1 = valores.pop();
                        boolean[] resultado = realizarOperacion(valor1, valor2, operador);
                        valores.push(resultado);
                    }
                    operadores.push(caracter);
                }
            } else if (caracter == '(') {
                operadores.push(caracter);
            } else if (caracter == ')') {
                while (!operadores.isEmpty() && operadores.peek() != '(') {
                    char operador = operadores.pop();
                    boolean[] valor2 = valores.pop();
                    boolean[] valor1 = valores.pop();
                    boolean[] resultado = realizarOperacion(valor1, valor2, operador);
                    valores.push(resultado);
                }
                operadores.pop(); // Eliminar el '(' correspondiente
            }
        }

        while (!operadores.isEmpty()) {
            char operador = operadores.pop();
            boolean[] valor2 = valores.pop();
            boolean[] valor1 = valores.pop();
            boolean[] resultado = realizarOperacion(valor1, valor2, operador);
            valores.push(resultado);
        }

        return valores.pop();
    }

    private int precedencia(char operador) {
        if (operador == '^') {
            return 2;
        } else if (operador == 'v') {
            return 1;
        } else if (operador == '\'') {
            return 3; // Mayor prioridad para la negación
        } else {
            return 0;
        }
    }

    private boolean[] realizarOperacion(boolean[] valor1, boolean[] valor2, char operador) {
        int n = valor1.length;
        boolean[] resultado = new boolean[n];

        if (operador == '^') {
            for (int i = 0; i < n; i++) {
                resultado[i] = valor1[i] && valor2[i];
            }
        } else if (operador == 'v') {
            for (int i = 0; i < n; i++) {
                resultado[i] = valor1[i] || valor2[i];
            }
        }
        return resultado;
    }

    public int[] calcularResultadoComoEnteros() {
        boolean[] resultadoBoolean = calcularResultado();
        int[] resultadoEntero = new int[resultadoBoolean.length];

        for (int i = 0; i < resultadoBoolean.length; i++) {
            resultadoEntero[i] = resultadoBoolean[i] ? 1 : 0;
        }

        return resultadoEntero;
    }
    private int obtenerIndiceVariable(char caracter) {
        switch (caracter) {
            case 'X':
                return 0;
            case 'Y':
                return 1;
            case 'Z':
                return 2;
            case 'W':
                return 3;
            default:
                return -1; // Valor no válido
        }
    }
}
