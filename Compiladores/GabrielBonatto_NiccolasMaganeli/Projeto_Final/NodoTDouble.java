/*
 * Integrantes:
 *  - Gabriel Bonatto Justo 17104118-9 gabriel.justo@acad.pucrs.br
 *  - Niccolas Guntzel Maganeli 16201269-4 niccolas.maganeli@edu.pucrs.br
 */

public class NodoTDouble implements INodo {
    private double dval;

    public NodoTDouble(double valor) {
        dval = valor;
    }

    public ResultValue avalia() {
        return new ResultValue(dval);
    }

    public String toString() {
        return Double.toString(dval);
    }
}
