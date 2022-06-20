//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 3 "exemploSem.y"
  import java.io.*;
  import java.util.ArrayList;

//#line 21 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IDENT=257;
public final static short INT=258;
public final static short DOUBLE=259;
public final static short BOOL=260;
public final static short NUM=261;
public final static short STRING=262;
public final static short LITERAL=263;
public final static short AND=264;
public final static short VOID=265;
public final static short IF=266;
public final static short STRUCT=267;
public final static short FUNCT=268;
public final static short RETURN=269;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    6,    0,    7,    7,    9,    8,   10,   10,    4,    4,
   12,   12,   13,    5,    5,   16,   14,   15,   15,   17,
    1,    1,    1,   11,   18,   18,   19,   19,   19,    2,
    2,    2,    2,    2,    2,    2,   20,    2,    3,    3,
    3,   21,   21,   22,   22,
};
final static short yylen[] = {                            2,
    0,    3,    2,    1,    0,    9,    1,    0,    1,    1,
    3,    1,    2,    2,    0,    0,    4,    3,    1,    1,
    1,    1,    1,    3,    2,    0,    2,    5,    3,    3,
    3,    3,    1,    3,    1,    3,    0,    5,    1,    4,
    3,    1,    0,    3,    1,
};
final static short yydefred[] = {                         1,
    0,   15,    0,   21,   22,   23,    0,   16,    0,    4,
   14,    9,   10,    0,    0,    3,    5,   20,    0,   19,
    0,    0,   17,    0,   18,    0,    0,    0,   12,   13,
   15,    0,    0,   11,   26,    6,    0,    0,   33,    0,
    0,    0,   24,    0,    0,   25,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   27,    0,    0,   41,    0,
    0,   29,   34,   32,    0,    0,    0,   40,    0,    0,
    0,    0,   38,    0,   28,    0,
};
final static short yydgoto[] = {                          1,
    8,   44,   45,   14,    3,    2,    9,   10,   21,   27,
   36,   28,   29,   11,   19,   15,   20,   37,   46,   49,
   70,   71,
};
final static short yysindex[] = {                         0,
    0,    0, -218,    0,    0,    0, -168,    0, -244,    0,
    0,    0,    0, -226, -225,    0,    0,    0,  -22,    0,
   -1, -225,    0, -165,    0, -214,    4,   10,    0,    0,
    0, -165, -117,    0,    0,    0,  -40,  -18,    0,   18,
  -37,  -37,    0,  -41,   -2,    0,  -37,  -37,   23,  -37,
  -26,  -28,  -37,  -37,  -37,    0,  -37,  -33,    0,  -37,
  -27,    0,    0,    0,  -23, -190,  -24,    0,  -24,   40,
   43,  -39,    0,  -37,    0,  -24,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,   88,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   57,    0,    0,    0,   58,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -36,    0,    0,
    0,    0,    0,    0,  -32,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   61,
    0,    0,    0,    0,    7,    3,    8,    0,   12,    0,
   63,    0,    0,    0,    0,   34,
};
final static short yygindex[] = {                         0,
   48,   29,    0,    0,   74,    0,    0,   97,    0,    0,
    0,    0,   75,    0,    0,    0,   86,    0,   37,    0,
    0,    0,
};
final static int YYTABLESIZE=241;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         42,
   42,   55,   42,   37,   39,   35,   39,   39,   35,   55,
   35,   35,   63,   72,   55,   55,   55,   56,   55,   55,
   54,   22,   39,    7,   39,   39,   35,   48,   54,   35,
   17,   18,   62,   54,   54,   54,   23,   54,   24,    4,
    5,    6,   30,   30,   31,   30,   30,   31,   36,    7,
   31,   36,   45,   32,   13,   45,   39,   50,   57,   68,
   35,   30,   60,   30,   30,   31,   36,   31,   36,   51,
   52,   26,   47,   53,   44,   58,   59,   44,   61,   26,
   73,   64,   65,   66,   43,   67,   74,    2,   69,    4,
    5,    6,    4,    5,    6,   30,   12,    8,    7,   31,
   36,   43,   76,   42,   33,   16,   34,   25,   75,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    4,    5,    6,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   38,   38,    0,   38,
   39,   39,   53,   39,    0,   40,   40,   39,   41,   41,
   53,   35,    0,    0,    0,   53,   53,   53,    0,   53,
   53,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   40,   43,   40,   40,   41,  123,   43,   44,   41,   43,
   43,   44,   41,   41,   43,   43,   43,   59,   43,   43,
   62,   44,   59,  268,   61,   62,   59,   46,   62,   62,
  257,  257,   59,   62,   62,   62,   59,   62,   40,  258,
  259,  260,  257,   41,   41,   43,   44,   41,   41,  268,
   44,   44,   41,   44,    7,   44,   93,   40,   61,   93,
   93,   59,   40,   61,   62,   59,   59,   61,   61,   41,
   42,   24,   91,  264,   41,   47,   48,   44,   50,   32,
   41,   53,   54,   55,  125,   57,   44,    0,   60,  258,
  259,  260,  258,  259,  260,   93,  265,   41,   41,   93,
   93,   41,   74,   41,   31,    9,   32,   22,   72,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  258,  259,  260,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,  257,   -1,  257,
  261,  261,  264,  261,   -1,  266,  266,  264,  269,  269,
  264,  264,   -1,   -1,   -1,  264,  264,  264,   -1,  264,
  264,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=269;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'",null,"'+'","','",
null,"'.'",null,null,null,null,null,null,null,null,null,null,null,null,"';'",
null,"'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"IDENT","INT","DOUBLE","BOOL","NUM",
"STRING","LITERAL","AND","VOID","IF","STRUCT","FUNCT","RETURN",
};
final static String yyrule[] = {
"$accept : prog",
"$$1 :",
"prog : $$1 dList ListaFuncoes",
"ListaFuncoes : ListaFuncoes umaFuncao",
"ListaFuncoes : umaFuncao",
"$$2 :",
"umaFuncao : FUNCT tipoOuVoid IDENT $$2 '(' ParamFormais ')' dList bloco",
"ParamFormais : ListaParametros",
"ParamFormais :",
"tipoOuVoid : VOID",
"tipoOuVoid : type",
"ListaParametros : ListaParametros ',' newIdentListaParametros",
"ListaParametros : newIdentListaParametros",
"newIdentListaParametros : type IDENT",
"dList : dList decl",
"dList :",
"$$3 :",
"decl : type $$3 LId ';'",
"LId : LId ',' nomeId",
"LId : nomeId",
"nomeId : IDENT",
"type : INT",
"type : DOUBLE",
"type : BOOL",
"bloco : '{' listacmd '}'",
"listacmd : listacmd cmd",
"listacmd :",
"cmd : exp ';'",
"cmd : IF '(' exp ')' cmd",
"cmd : RETURN exp ';'",
"exp : exp '+' exp",
"exp : exp '>' exp",
"exp : exp AND exp",
"exp : NUM",
"exp : '(' exp ')'",
"exp : lvalue",
"exp : lvalue '=' exp",
"$$4 :",
"exp : IDENT $$4 '(' ParamReais ')'",
"lvalue : IDENT",
"lvalue : IDENT '[' exp ']'",
"lvalue : IDENT '.' exp",
"ParamReais : ListaExpr",
"ParamReais :",
"ListaExpr : ListaExpr ',' exp",
"ListaExpr : exp",
};

//#line 203 "exemploSem.y"

  private Yylex lexer;

  private TabSimb ts;
  private TS_entry currType;
  private TS_entry currFunc;

  public static TS_entry Tp_INT =  new TS_entry("int", null, ClasseID.TipoBase);
  public static TS_entry Tp_DOUBLE = new TS_entry("double", null,  ClasseID.TipoBase);
  public static TS_entry Tp_BOOL = new TS_entry("bool", null,  ClasseID.TipoBase);
  
  public static TS_entry Tp_VOID = new TS_entry("void", null,  ClasseID.TipoBase);
  
  public static TS_entry Tp_ERRO = new TS_entry("_erro_", null,  ClasseID.TipoBase);

  public static final int ARRAY = 1500;
  public static final int ATRIB = 1600;

  private String currEscopo;
  private ClasseID currClass;

  private int count_param;
  private int listSize;

  private int yylex () {
    int yyl_return = -1;
    try {
      yylval = new ParserVal(0);
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :"+e);
    }
    return yyl_return;
  }


  public void yyerror (String error) {
    //System.err.println("Erro (linha: "+ lexer.getLine() + ")\tMensagem: "+error);
    System.err.printf("Erro (linha: %2d \tMensagem: %s)\n", lexer.getLine(), error);
  }


  public Parser(Reader r) {
    lexer = new Yylex(r, this);

    ts = new TabSimb();

    //
    // não me parece que necessitem estar na TS
    // já que criei todas como public static...
    //
    ts.insert(Tp_ERRO);
    ts.insert(Tp_INT);
    ts.insert(Tp_DOUBLE);
    ts.insert(Tp_BOOL);
    ts.insert(Tp_VOID);
    

  }  

  public void setDebug(boolean debug) {
    yydebug = debug;
  }

  public void listarTS() { ts.listar();}

  public static void main(String args[]) throws IOException {
    System.out.println("\n\nVerificador semantico simples\n");
    

    Parser yyparser;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
    }
    else {
      // interactive mode
      System.out.println("[Quit with CTRL-D]");
      System.out.print("Programa de entrada:\n");
        yyparser = new Parser(new InputStreamReader(System.in));
    }

    yyparser.yyparse();

      yyparser.listarTS();

      System.out.print("\n\nFeito!\n");
    
  }


   TS_entry validaTipo(int operador, TS_entry A, TS_entry B) {
       
         switch ( operador ) {
              case ATRIB:
                    if ( (A == Tp_INT && B == Tp_INT)                        ||
                         ((A == Tp_DOUBLE && (B == Tp_INT || B == Tp_DOUBLE))) ||
                         (A == B) )
                         return A;
                     else
                         yyerror("(sem) tipos incomp. para atribuicao: "+ A.getTipoStr() + " = "+B.getTipoStr());
                    break;

              case '+' :
                    if ( A == Tp_INT && B == Tp_INT)
                          return Tp_INT;
                    else if ( (A == Tp_DOUBLE && (B == Tp_INT || B == Tp_DOUBLE)) ||
                                            (B == Tp_DOUBLE && (A == Tp_INT || A == Tp_DOUBLE)) ) 
                         return Tp_DOUBLE;     
                    else
                        yyerror("(sem) tipos incomp. para soma: "+ A.getTipoStr() + " + "+B.getTipoStr());
                    break;

             case '>' :
                     if ((A == Tp_INT || A == Tp_DOUBLE) && (B == Tp_INT || B == Tp_DOUBLE))
                         return Tp_BOOL;
                      else
                        yyerror("(sem) tipos incomp. para op relacional: "+ A.getTipoStr() + " > "+B.getTipoStr());
                      break;

             case AND:
                     if (A == Tp_BOOL && B == Tp_BOOL)
                         return Tp_BOOL;
                      else
                        yyerror("(sem) tipos incomp. para op lógica: "+ A.getTipoStr() + " && "+B.getTipoStr());
                 break;
            }

            return Tp_ERRO;
           
     }

boolean compareTypes(TS_entry A, TS_entry B){
    if(A == Tp_INT && B == Tp_INT)
      return true;
    else if(A == Tp_BOOL && B == Tp_BOOL)
      return true;
    else if(A == Tp_DOUBLE && B == Tp_DOUBLE)
      return true;
    else
      return false;
}

//#line 437 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 29 "exemploSem.y"
{ currClass = ClasseID.VarGlobal; }
break;
case 5:
//#line 36 "exemploSem.y"
{ currEscopo = val_peek(0).sval;
                                      TS_entry nodo = ts.pesquisa(val_peek(0).sval);
                                      if (nodo != null) 
                                        yyerror("(sem) nome >" + val_peek(0).sval + "< jah declarada");
                                      else{
                                        ts.insert(new TS_entry(val_peek(0).sval, (TS_entry)val_peek(1).obj, ClasseID.NomeFuncao)); 
                                        currClass = ClasseID.VarLocal;
                                        }
                                      }
break;
case 9:
//#line 52 "exemploSem.y"
{ yyval.obj = Tp_VOID; }
break;
case 10:
//#line 53 "exemploSem.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 13:
//#line 60 "exemploSem.y"
{TS_entry nodo = ts.pesquisa(currEscopo);
                                    if(nodo == null)
                                      yyerror("É impossivle dar esse erro com a "+currEscopo);
                                    else{
                                      TS_entry new_var = new TS_entry(val_peek(0).sval, Tp_INT, ClasseID.NomeParam);
                                      nodo.insert_param(new_var);
                                      ts.insert(new_var);
                                      }
                                    }
break;
case 16:
//#line 74 "exemploSem.y"
{ currType = (TS_entry)val_peek(0).obj; }
break;
case 20:
//#line 81 "exemploSem.y"
{  TS_entry nodo = ts.pesquisa(val_peek(0).sval);
                          TS_entry scopo = ts.pesquisa(currEscopo);
                          if (currClass == ClasseID.VarLocal){
                              nodo = scopo.getLocais().pesquisa(val_peek(0).sval);
                          }
                          if (nodo != null) 
                              yyerror("(sem) variavel >" + val_peek(0).sval + "< jah declarada");
                          else {
                            ts.insert(new TS_entry(val_peek(0).sval, currType, currClass)); 
                            if (currClass == ClasseID.VarLocal)
                              scopo.insert_local(new TS_entry(val_peek(0).sval, currType, currClass)); 
                          }
                        }
break;
case 21:
//#line 96 "exemploSem.y"
{ yyval.obj = Tp_INT; }
break;
case 22:
//#line 97 "exemploSem.y"
{ yyval.obj = Tp_DOUBLE; }
break;
case 23:
//#line 98 "exemploSem.y"
{ yyval.obj = Tp_BOOL; }
break;
case 24:
//#line 113 "exemploSem.y"
{ ArrayList<TS_entry> l = ts.pesquisa(currEscopo).getParams().getLista();
                          for(TS_entry nodo: l)
                            ts.remove(nodo);
                        }
break;
case 28:
//#line 124 "exemploSem.y"
{  if ( ((TS_entry)val_peek(2).obj) != Tp_BOOL) 
                                     yyerror("(sem) expressão (if) deve ser lógica "+((TS_entry)val_peek(2).obj).getTipo());
                             }
break;
case 30:
//#line 131 "exemploSem.y"
{ yyval.obj = validaTipo('+', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 31:
//#line 132 "exemploSem.y"
{ yyval.obj = validaTipo('>', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 32:
//#line 133 "exemploSem.y"
{ yyval.obj = validaTipo(AND, (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 33:
//#line 134 "exemploSem.y"
{ yyval.obj = Tp_INT; }
break;
case 34:
//#line 135 "exemploSem.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 35:
//#line 136 "exemploSem.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 36:
//#line 137 "exemploSem.y"
{  yyval.obj = validaTipo(ATRIB, (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj);  }
break;
case 37:
//#line 138 "exemploSem.y"
{currFunc = ts.pesquisa(val_peek(0).sval); 
            if (currFunc == null)
              yyerror("Função <"+val_peek(0).sval+"> não declarada");
            else
             listSize = currFunc.getParams().getLista().size();
             count_param = 0;
             }
break;
case 38:
//#line 145 "exemploSem.y"
{ if(currFunc == null)
                            yyval.obj = Tp_ERRO;
                          else if(count_param < listSize){
                            yyerror("Parametros faltando na função "+currFunc.getId()+"()");
                            yyval.obj = Tp_ERRO;
                          }else if(count_param > listSize){
                            yyerror("Parametros a mais na função "+currFunc.getId()+"()");
                            yyval.obj = Tp_ERRO;
                          }else
                            yyval.obj = currFunc.getTipo();
                        }
break;
case 39:
//#line 160 "exemploSem.y"
{ TS_entry nodo = ts.pesquisa(val_peek(0).sval);
                    if (nodo == null) {
                       yyerror("(sem) var <" + val_peek(0).sval + "> nao declarada"); 
                       yyval.obj = Tp_ERRO;    
                       }           
                    else{
                        nodo = ts.getNomeParam(nodo);
                        yyval.obj = nodo.getTipo();
                    }
                  }
break;
case 40:
//#line 170 "exemploSem.y"
{ yyval.obj = Tp_ERRO; }
break;
case 41:
//#line 171 "exemploSem.y"
{ yyval.obj = Tp_ERRO; }
break;
case 44:
//#line 178 "exemploSem.y"
{ if(count_param >= listSize || currFunc == null)
                                  count_param++;
                                else{
                                  TS_entry nodo = currFunc.getParams().getLista().get(count_param);
                                  if(!compareTypes((TS_entry)val_peek(0).obj, nodo.getTipo())){
                                      yyerror("Funcao " + currFunc.getId() + "() tipo dos parametros errado, recebeu:"+((TS_entry)val_peek(0).obj).getTipoStr()+" esperava:"+nodo.getTipo().getTipoStr());
                                  }
                                }
                                count_param++;
                              }
break;
case 45:
//#line 188 "exemploSem.y"
{ if(count_param >= listSize || currFunc == null)
                    count_param++;
                  else{
                    TS_entry nodo = currFunc.getParams().getLista().get(count_param);
                    if(!compareTypes((TS_entry)val_peek(0).obj, nodo.getTipo())){
                      yyerror("Funcao " + currFunc.getId() + "() tipos errados, recebeu:"+((TS_entry)val_peek(0).obj).getTipoStr()+" esperava:"+nodo.getTipo().getTipoStr());
                    }else
                    count_param++;
                  }
                }
break;
//#line 766 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
