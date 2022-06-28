
import java.util.HashMap;

public class NodoNT implements INodo
{
    private TipoOperacao op;
    private INodo subE, subD;
    private INodo expr, cmd;
    private String ident;

    public NodoNT(TipoOperacao op, INodo se, INodo sd) {
        this.op = op;
        subE = se;
        subD = sd;
    }

    public NodoNT(TipoOperacao op, String id, INodo se) {
        this.op = op;
        subE = se;
        ident = id;
    }

    public NodoNT(TipoOperacao op, INodo exp, INodo caseT, INodo caseF ) {
        this.op = op;
        subE = caseT;
        subD = caseF;
        expr = exp;
    }

    public NodoNT(TipoOperacao op, INodo var, INodo cond, INodo exp, INodo cmd){
        this.op = op;
        subE = var;
        subD = cond;
        expr = exp;
        this.cmd = cmd;
    }

    public NodoNT(TipoOperacao op, String ident){
        this.op = op;
        this.ident = ident;
    }
   
    public ResultValue avalia() {
        HashMap<String, ResultValue> memory = Parser.peekStack();
        ResultValue result = null;
        ResultValue  left, right, expressao;
        
        if (op == TipoOperacao.NULL){
            return null; 
        }
        if (op == TipoOperacao.UMINUS) 
             result = new ResultValue(-1.0 * subE.avalia().getDouble()) ;
        
        else if (op == TipoOperacao.NEG) 
            result = new ResultValue( ! subE.avalia().getBool()) ;
        
        else if (op == TipoOperacao.ATRIB) {
             result = subE.avalia();
             memory.put(ident, result);  
             Parser.popStack();
             Parser.pushStack(memory);
             //System.out.printf("sube: %s, %s <- %f\n", subE, ident, result.getDouble());         
        }
        else if (op == TipoOperacao.SOMA_ATRIB) {
            result = new ResultValue(memory.get(ident).getDouble() + subE.avalia().getDouble());
            memory.put(ident, result);
            Parser.popStack();
            Parser.pushStack(memory);    
            //System.out.printf("sube: %s, %s <- %f\n", subE, ident, result.getDouble());         
       }
       else if (op == TipoOperacao.MULT_ATRIB) { 
        
        result = new ResultValue(memory.get(ident).getDouble() * subE.avalia().getDouble());
        memory.put(ident, result);    
        Parser.popStack();
        Parser.pushStack(memory);
        //System.out.printf("sube: %s, %s <- %f\n", subE, ident, result.getDouble());         
        }

       else if (op == TipoOperacao.IF) {
            expressao = expr.avalia();
            if(expressao.getBool()){
                result = subE.avalia();
            }
      }
      else if (op == TipoOperacao.PRINT) {
        if(subE != null)
            System.out.println("Print -> "+subE.avalia());  
      }

        else if (op == TipoOperacao.IFELSE) {
            expressao = expr.avalia();
            if(expressao.getBool()){
                result = subE.avalia();
            }else{
                result = subD.avalia();
            }
        }

        else if (op == TipoOperacao.WHILE) {
            expressao = expr.avalia();
            while(expressao.getBool()){
                result = subE.avalia();
                expressao = expr.avalia();
            }
        }
        else if (op == TipoOperacao.FUNC) {
            result = Parser.funcs.get(ident).avalia();
        }
       else if (op == TipoOperacao.SEQ) {
           subE.avalia();
           result = subD.avalia();
        }
        else if (op == TipoOperacao.FOR) {
            // this.op = op;
            // subE = var;
            // subD = cond;
            // expr = exp;
            // this.cmd = cmd;
            result = subE.avalia();
            expressao = subD.avalia();
            while(expressao.getBool()){
                result = cmd.avalia();
                expr.avalia();
                expressao = subD.avalia();
            }
        }

        else {        
            left = subE.avalia();
            right = subD.avalia();
          switch (op) {
            case ADD:
               result = new ResultValue((left.getDouble() + right.getDouble()));
               break;
            case SUB:
               result = new ResultValue(left.getDouble() - right.getDouble());
               break;
            case MULL:
               result = new ResultValue(left.getDouble() * right.getDouble());
               break;
            case DIV:
              result = new ResultValue(left.getDouble() / right.getDouble());
              break;
            case POW:
              result = new ResultValue(Math.pow(left.getDouble(),right.getDouble()));
              break;
            case LESS:
              result = new ResultValue(left.getDouble() < right.getDouble());
              break;
            case GREAT:
              result = new ResultValue(left.getDouble() > right.getDouble());
              break;
            case GREAT_EQ:
              result = new ResultValue(left.getDouble() >= right.getDouble());
              break;
            case LESS_EQ:
              result = new ResultValue(left.getDouble() <= right.getDouble());
              break;
            case DIFF:
              result = new ResultValue(left.getDouble() != right.getDouble());
              break;
            case EQUAL:
              result = new ResultValue(left.getDouble() == right.getDouble());
              break;
            case AND:
              result = new ResultValue(left.getBool() && right.getBool());
              break;
            case OR:
              result = new ResultValue(left.getBool() || right.getBool());
              break;                   
            default:
              result = new ResultValue(0);
            }
        }
        return result;               
    }
    
    public String toString() {
        String opBin, result;
        if (op == TipoOperacao.ATRIB) 
            result =  ident + "=" + subE  ;
        else if (op == TipoOperacao.SOMA_ATRIB) 
            result = ident + "+=" + subE  ;
        else if (op == TipoOperacao.MULT_ATRIB) 
            result = ident + "*=" + subE  ;
        else if (op == TipoOperacao.IF) 
            result = "if (" + expr + ")" + subE + " else " + subD  ;
        else if (op == TipoOperacao.WHILE) 
            result = "while (" + subE + ")" + subD   ;
        else if (op == TipoOperacao.FOR) 
            result = "Vai aparecer um for aqui";
        else if (op == TipoOperacao.UMINUS) 
            result = "-" + subE  ;
        else if (op == TipoOperacao.NEG) 
            result = "!" + subE  ;
        else {
            switch (op) {
           
                case ADD:
                    opBin = " + ";
                    break;
                 case SUB:
                    opBin = " - ";
                    break;
                 case MULL:
                    opBin  = " * ";
                    break;
                 case DIV:
                    opBin  = " / ";
                    break;
                 case POW:
                    opBin  = " ^ ";
                    break;

                 case LESS:
                    opBin = " < ";
                    break;
                 case GREAT:
                    opBin = " > ";
                    break;
                 case GREAT_EQ:
                    opBin = " >= ";
                    break;
                 case LESS_EQ:
                    opBin = " <= ";
                    break;
                 case EQUAL:
                    opBin = " == ";
                    break;
                 case DIFF:
                    opBin = " != ";
                    break;
                 case AND:
                    opBin = " && ";
                    break;
                 case OR:
                    opBin = " || ";
                    break;
                 case PRINT:
                    opBin = "";
                    break;
                 default:
                    opBin = " ? ";
                }
                result = "(" + subE + opBin + subD+")";
            }
                 return result;
        }       
  
}
