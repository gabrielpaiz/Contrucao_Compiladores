
import java.util.HashMap;

public class NodoNT implements INodo
{
    private TipoOperacao op;
    private INodo subE, subD;
    private INodo expr;
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
   
    public ResultValue avalia() {

        ResultValue result = null;
        ResultValue  left, right, expressao;
        
        if (op == TipoOperacao.NULL)
           return null; 

        if (op == TipoOperacao.UMINUS) 
             result = new ResultValue(-1.0 * subE.avalia().getDouble()) ;
        
        else if (op == TipoOperacao.NEG) 
            result = new ResultValue( ! subE.avalia().getBool()) ;
        
        else if (op == TipoOperacao.ATRIB) {
             result = subE.avalia();
             Parser.memory.put(ident, subE.avalia()); 
             result = null;   
             //System.out.printf("sube: %s, %s <- %f\n", subE, ident, result.getDouble());         
        }
        else if (op == TipoOperacao.SOMA_ATRIB) {
            result = new ResultValue(Parser.memory.get(ident).getDouble() + subE.avalia().getDouble());
            Parser.memory.put(ident, result);
            result = null;    
            //System.out.printf("sube: %s, %s <- %f\n", subE, ident, result.getDouble());         
       }
       else if (op == TipoOperacao.MULT_ATRIB) { 
        result = new ResultValue(Parser.memory.get(ident).getDouble() * subE.avalia().getDouble());
        Parser.memory.put(ident, result);    
        result = null;
        //System.out.printf("sube: %s, %s <- %f\n", subE, ident, result.getDouble());         
        }

       else if (op == TipoOperacao.IF) {
            expressao = expr.avalia();
            if(expressao.getBool()){
                result = subE.avalia();
            }
      }
      else if (op == TipoOperacao.PRINT) {
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
       else if (op == TipoOperacao.SEQ) {
           subE.avalia();
           result = subD.avalia();
        }
        else if (op == TipoOperacao.RETURN) {
            result = new ResultValue(-1.0);
        }
        else if (op == TipoOperacao.DEFINE) {
            result = new ResultValue(-1.0);
        }
        else if (op == TipoOperacao.FOR) {
            result = new ResultValue(-1.0);
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
