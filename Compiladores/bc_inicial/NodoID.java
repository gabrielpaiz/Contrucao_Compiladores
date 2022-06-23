
/**
 * Write a description of class Nodo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NodoID implements INodo
{
    private int tipo;
    private String sval;
    

    public NodoID(String valor) {
        sval = valor;
    }

    public ResultValue avalia() {

         if (Parser.peekStack().containsKey(sval))
            return new ResultValue(Parser.peekStack().get(sval).getDouble());
         else 
            return new ResultValue(0);              
    }
    
    public String toString() {
            return sval;
        }       
 }
    
