import java.util.ArrayList;
import java.util.HashMap;

public class NodoFunc implements INodo{

    private ArrayList<String> params;
    private String ident;
    private NodoNT cmds;
    private ArrayList<ResultValue> execParam;
    
    public NodoFunc(ArrayList<String> params, String ident, INodo cmds) {
        this.params = params;
        this.ident = ident;
        this.cmds = (NodoNT) cmds;
        execParam = new ArrayList<>();
        Parser.funcs.put(ident, this);
    }
    @Override
    public ResultValue avalia() {
        ResultValue result;
        if(Parser.actualParam == null)
            return null;
        for(int i = 0;i<Parser.actualParam.size();i++){
            execParam.add(Parser.actualParam.get(i).avalia());
        }
        result = this.executa(execParam);
        execParam.clear();

        return result;
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
