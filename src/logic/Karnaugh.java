package logic;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
public class Karnaugh {
    private int num_vars;
    private int[] resultadoEntero;
    public Karnaugh(int num_vars, int[] resultadoEntero) {
        this.num_vars = num_vars;
        this.resultadoEntero = resultadoEntero;
    }
    public int[][] crearMapaKarnaugh() {
        //cambiar orden resultados
        if(num_vars == 3) {
            int value1 = resultadoEntero[4];
            int value2 = resultadoEntero[5];
            resultadoEntero[4] = resultadoEntero[6];
            resultadoEntero[5] = resultadoEntero[7];
            resultadoEntero[6] = value1;
            resultadoEntero[7] = value2;
        }
        if (num_vars == 4) {
            int value1 = resultadoEntero[2];
            int value2 = resultadoEntero[6];
            int value3 = resultadoEntero[8];
            int value4 = resultadoEntero[9];
            int value5 = resultadoEntero[10];
            int value6 = resultadoEntero[11];
            resultadoEntero[2] = resultadoEntero[3];
            resultadoEntero[6] = resultadoEntero[7];
            resultadoEntero[8] = resultadoEntero[12];
            resultadoEntero[9] = resultadoEntero[13];
            resultadoEntero[10] = resultadoEntero[15];
            resultadoEntero[11] = resultadoEntero[14];
            resultadoEntero[3] = value1;
            resultadoEntero[7] = value2;
            resultadoEntero[12] = value3;
            resultadoEntero[13] = value4;
            resultadoEntero[15] = value5;
            resultadoEntero[14] = value6;
        }

        int dimension1;
        int dimension2;
        int[][] karnaughMap;
        //tener en cuenta si el numero de vars es par o impar
        if (num_vars == 2) {
            dimension1 = num_vars;
            dimension2 = num_vars;
            karnaughMap = new int[dimension1][dimension2];
        } else {
            if (num_vars == 4) { //es par
                dimension1 = 4;
                dimension2 = num_vars;
                karnaughMap = new int[dimension1][dimension2];
            } else { // impar
                dimension1 = 4;
                dimension2 = num_vars-1;
                karnaughMap = new int[dimension1][dimension2];
            }
        }
        // Llenar mapa
        int cont = 0;
        for (int i = 0; i < dimension1; i++) {
            for (int j = 0; j < dimension2; j++) {
                if (resultadoEntero[cont] == 1) {
                    karnaughMap[i][j] = 1;
                } else {
                    karnaughMap[i][j] = 0;
                }
                cont++;
            }
        }
        return karnaughMap;
    }

    public String simplificarFuncion(int[][] karnaughMap, int numVariables) {
        int numRows = karnaughMap.length;
        int numCols = karnaughMap[0].length;

        StringBuilder simplifiedExpression = new StringBuilder();
        boolean hasTerms = false; // Variable para rastrear si se encontraron términos en el mapa.

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (karnaughMap[row][col] == 1) {
                    hasTerms = true;
                    // Verificar si existen otros 1's en las direcciones especificadas
                    boolean right = karnaughMap[row][(col + 1) % numCols] == 1;
                    boolean left = karnaughMap[row][(col + numCols - 1) % numCols] == 1;
                    boolean up = karnaughMap[(row + numRows - 1) % numRows][col] == 1;
                    boolean down = karnaughMap[(row + 1) % numRows][col] == 1;

                    StringBuilder variable = new StringBuilder();

                    // Crear una matriz con las combinaciones correspondientes
                    if (right || left) {
                        variable.append("X");
                    }

                    if (up || down) {
                        variable.append("Y");
                    }

                    for (int i = 2; i < numVariables; i++) {
                        if ((row + col) % (1 << i) == 0) {
                            variable.append((char) ('Z' - i + 1));
                        } else {
                            variable.append((char) ('Z' - i + 1) + "'");
                        }
                    }

                    simplifiedExpression.append(variable);

                    // Agregar el operador + para OR o no agregar nada para XOR
                    if (right && left || up && down) {
                        simplifiedExpression.append(" + ");
                    }

                    // Marcar el elemento como visitado para no contar nuevamente.
                    karnaughMap[row][col] = 0;
                }
            }
        }

        if (hasTerms) {
            // Eliminar el último operador + si se encontraron términos en el mapa.
            simplifiedExpression.setLength(simplifiedExpression.length() - 3);
        } else {
            // Si no se encontraron términos, la función es 0.
            simplifiedExpression.append("0");
        }

        return simplifiedExpression.toString();
    }
}
