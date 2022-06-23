import java.util.ArrayList;
import java.util.HashMap;

public class NodoFunc implements INodo{

    private ArrayList<String> params;
    private String ident;
    private NodoNT cmds;
    
    public NodoFunc(ArrayList<String> params, String ident, INodo cmds) {
        this.params = params;
        this.ident = ident;
        this.cmds = (NodoNT) cmds;
    }
    @Override
    public ResultValue avalia() {
        Parser.funcs.put(ident, this);
        return new ResultValue(1);
    }
    public ArrayList<String> getParams() {
        return params;
    }
    
    public String getIdent() {
        return ident;
    }
    
    public NodoNT getCmds() {
        return cmds;
    }

    public String toString(){
        String s = ident + "(" + params.toString()+")";
        return s;
    }

    public ResultValue executa(ArrayList<ResultValue> val){
        HashMap<String, ResultValue> func_scopo = new HashMap<>();
        ResultValue result;
        for(int i=0; i< val.size(); i++){
            func_scopo.put(params.get(i), val.get(i));
        }
        Parser.pushStack(func_scopo);

        result = cmds.avalia();
        Parser.popStack();
        return result;
    }

}
