/*
 * Integrantes:
 *  - Gabriel Bonatto Justo 17104118-9 gabriel.justo@acad.pucrs.br
 *  - Niccolas Guntzel Maganeli 16201269-4 niccolas.maganeli@edu.pucrs.br
 */

import java.util.List;
import java.util.stream.Collectors;

public class NodoFuncRun implements INodo {

    private String ident;
    private List<INodo> params;

    public NodoFuncRun(String ident, List<INodo> params) {
        this.ident = ident;
        this.params = params;
    }

    @Override
    public ResultValue avalia() {
        NodoFuncDecl func = Parser.funcs.get(ident);

        for (int i = 0; i < params.size(); i++) {
            String tempId = func.getParams().get(i);
            Parser.memory.put(tempId, params.get(i).avalia());
        }

        return func.getBlock().avalia();
    }

    @Override
    public String toString() {
        return ident + "(" + params.stream().map(Object::toString).collect(Collectors.joining(", ")) + ")";
    }

}
