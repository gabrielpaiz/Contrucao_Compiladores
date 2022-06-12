/*
 * Integrantes:
 *  - Gabriel Bonatto Justo 17104118-9 gabriel.justo@acad.pucrs.br
 *  - Niccolas Guntzel Maganeli 16201269-4 niccolas.maganeli@edu.pucrs.br
 */

import java.util.List;

public class NodoFuncDecl implements INodo {

    private NodoNT block;
    private String ident;
    private List<String> params;

    public NodoFuncDecl(String ident, List<String> params, INodo block) {
        this.ident = ident;
        this.params = params;
        this.block = (NodoNT) block;
    }

    @Override
    public ResultValue avalia() {
        Parser.funcs.put(ident, this);

        return new ResultValue(1);
    }

    @Override
    public String toString() {
        return ident + "(" + params.toString() + ") {" + block + "}";
    }

    public List<String> getParams() {
        return params;
    }

    public NodoNT getBlock() {
        return block;
    }

}
