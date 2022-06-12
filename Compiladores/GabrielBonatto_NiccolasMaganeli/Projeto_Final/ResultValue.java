/*
 * Integrantes:
 *  - Gabriel Bonatto Justo 17104118-9 gabriel.justo@acad.pucrs.br
 *  - Niccolas Guntzel Maganeli 16201269-4 niccolas.maganeli@edu.pucrs.br
 */

public class ResultValue {
    private TypeEnum type;
    private double dval;
    private boolean bval;

    public ResultValue(double val) {
        type = TypeEnum.DOUBLE;
        dval = val;
    }

    public ResultValue(boolean val) {
        type = TypeEnum.BOOLEAN;
        bval = val;
    }

    public double getDouble() {
        return dval;
    }

    public boolean getBool() {
        return bval;
    }

    public String toString() {
        switch (type) {
            case DOUBLE:
                return Double.toString(dval);
            case BOOLEAN:
                return Boolean.toString(bval);
        }

        return "erro! tipo nao tratado em ResultValue";
    }
}
