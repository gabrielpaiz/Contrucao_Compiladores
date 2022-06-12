/*
 * Integrantes:
 *  - Gabriel Bonatto Justo 17104118-9 gabriel.justo@acad.pucrs.br
 *  - Niccolas Guntzel Maganeli 16201269-4 niccolas.maganeli@edu.pucrs.br
 */

public class NodoTBool implements INodo {
    private boolean bval;

    public NodoTBool(boolean valor) {
        bval = valor;
    }

    public ResultValue avalia() {
        return new ResultValue(bval);
    }

    public String toString() {
        return Boolean.toString(bval);
    }
}
