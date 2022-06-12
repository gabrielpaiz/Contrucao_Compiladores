/*
 * Integrantes:
 *  - Gabriel Bonatto Justo 17104118-9 gabriel.justo@acad.pucrs.br
 *  - Niccolas Guntzel Maganeli 16201269-4 niccolas.maganeli@edu.pucrs.br
 */

public class NodoNT implements INodo {
    private TipoOperacao op;
    private INodo subE, subD;
    private INodo expr, init, step;
    private String ident;

    public NodoNT(TipoOperacao op, INodo se, INodo sd) {
        this.op = op;
        subE = se;
        subD = sd;
    }

    public NodoNT(TipoOperacao op, String id, INodo se) {
        this.op = op;
        ident = id;
        subE = se;
    }

    public NodoNT(TipoOperacao op, INodo exp, INodo caseT, INodo caseF) {
        this.op = op;
        expr = exp;
        subE = caseT;
        subD = caseF;
    }

    public NodoNT(TipoOperacao op, INodo init, INodo expr, INodo step, INodo block) {
        this.op = op;
        this.init = init;
        this.expr = expr;
        this.step = step;
        subE = block;
    }

    public ResultValue avalia() {

        ResultValue result = null;
        ResultValue left, right, expressao;

        // System.out
        // .println("Op: " + op + "\nExpr: " + expr + "\nSubE: " + subE + "\nSubD: " +
        // subD + "\nIdent: " + ident);

        if (op == TipoOperacao.NULL)
            return null;
        if (op == TipoOperacao.UMINUS)
            result = new ResultValue(-1.0 * subE.avalia().getDouble());
        else if (op == TipoOperacao.NOT)
            result = new ResultValue(!(subE.avalia().getBool()));
        else if (op == TipoOperacao.PP_A) {
            result = Parser.memory.getOrDefault(ident, new ResultValue(0));
            Parser.memory.put(ident, new ResultValue(result.getDouble() + 1));
        } else if (op == TipoOperacao.PP_B) {
            result = new ResultValue(Parser.memory.getOrDefault(ident, new ResultValue(0)).getDouble() + 1);
            Parser.memory.put(ident, result);
        } else if (op == TipoOperacao.MM_A) {
            result = Parser.memory.getOrDefault(ident, new ResultValue(0));
            Parser.memory.put(ident, new ResultValue(result.getDouble() - 1));
        } else if (op == TipoOperacao.MM_B) {
            result = new ResultValue(Parser.memory.getOrDefault(ident, new ResultValue(0)).getDouble() - 1);
            Parser.memory.put(ident, result);
        } else if (op == TipoOperacao.ATRIB) {
            result = subE.avalia();
            Parser.memory.put(ident, result);
        } else if (op == TipoOperacao.PLUS_EQ) {
            result = subE.avalia();
            ResultValue currentVal = Parser.memory.get(ident);
            System.out.println(currentVal.getDouble());
            Parser.memory.put(ident, new ResultValue(result.getDouble() + currentVal.getDouble()));
        } else if (op == TipoOperacao.TIMES_EQ) {
            result = subE.avalia();
            ResultValue currentVal = Parser.memory.get(ident);
            Parser.memory.put(ident, new ResultValue(result.getDouble() * currentVal.getDouble()));
        } else if (op == TipoOperacao.IF) {
            expressao = expr.avalia();
            if (expressao.getBool())
                result = subE.avalia();
        } else if (op == TipoOperacao.IFELSE) {
            expressao = expr.avalia();
            if (expressao.getBool())
                result = subE.avalia();
            else
                result = subD.avalia();
        } else if (op == TipoOperacao.WHILE) {
            while (expr.avalia().getBool())
                result = subE.avalia();

        } else if (op == TipoOperacao.FOR) {
            init.avalia();
            while (true) {
                if (expr != null) {
                    ResultValue aux = expr.avalia();
                    if (aux != null && !aux.getBool())
                        break;
                }
                result = subE.avalia();
                if (step != null)
                    step.avalia();
            }
        } else if (op == TipoOperacao.SEQ) {
            subE.avalia();
            result = subD.avalia();
        } else {
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
                    result = new ResultValue(Math.pow(left.getDouble(), right.getDouble()));
                    break;
                case LESS:
                    result = new ResultValue(left.getDouble() < right.getDouble());
                    break;
                case GREATER:
                    result = new ResultValue(left.getDouble() > right.getDouble());
                    break;
                case GE:
                    result = new ResultValue(left.getDouble() >= right.getDouble());
                    break;
                case LE:
                    result = new ResultValue(left.getDouble() <= right.getDouble());
                    break;
                case EQUALS:
                    result = new ResultValue(left.getDouble() == right.getDouble());
                    break;
                case DIFF:
                    result = new ResultValue(left.getDouble() != right.getDouble());
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
            result = ident + "=" + subE;
        else if (op == TipoOperacao.PLUS_EQ)
            result = ident + "+=" + subE;
        else if (op == TipoOperacao.TIMES_EQ)
            result = ident + "*=" + subE;
        else if (op == TipoOperacao.IF)
            result = "if (" + expr + ")" + subE;
        else if (op == TipoOperacao.IFELSE)
            result = "if (" + expr + ")" + subE + "else" + subD;
        else if (op == TipoOperacao.WHILE)
            result = "while (" + subE + ")" + subD;
        else if (op == TipoOperacao.FOR)
            result = "for (" + init + ";" + expr + ";" + step + ")" + subE;
        else if (op == TipoOperacao.UMINUS)
            result = "-" + subE;
        else if (op == TipoOperacao.NOT)
            result = "!" + subE;
        else if (op == TipoOperacao.PP_A)
            result = ident + "++";
        else if (op == TipoOperacao.PP_B)
            result = "++" + ident;
        else if (op == TipoOperacao.MM_A)
            result = ident + "--";
        else if (op == TipoOperacao.MM_B)
            result = "--" + ident;
        else if (op == TipoOperacao.SEQ)
            result = subE + ";" + subD;
        else {
            switch (op) {

                case ADD:
                    opBin = " + ";
                    break;
                case SUB:
                    opBin = " - ";
                    break;
                case MULL:
                    opBin = " * ";
                    break;
                case DIV:
                    opBin = " / ";
                    break;
                case POW:
                    opBin = " ^ ";
                    break;
                case LESS:
                    opBin = " < ";
                    break;
                case GREATER:
                    opBin = " > ";
                    break;
                case GE:
                    opBin = " >= ";
                    break;
                case LE:
                    opBin = " <= ";
                    break;
                case EQUALS:
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

                default:
                    opBin = " ? ";
            }
            result = "(" + subE + opBin + subD + ")";
        }
        return result;
    }

}
