/*
 * Integrantes:
 *  - Gabriel Bonatto Justo 17104118-9 gabriel.justo@acad.pucrs.br
 *  - Niccolas Guntzel Maganeli 16201269-4 niccolas.maganeli@edu.pucrs.br
 */

public class NodoID implements INodo {
    private int tipo;
    private String sval;

    public NodoID(String valor) {
        sval = valor;
    }

    public ResultValue avalia() {
        return Parser.memory.getOrDefault(sval, new ResultValue(0));
    }

    public String toString() {
        return sval;
    }
}
