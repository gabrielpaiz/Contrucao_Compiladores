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






//#line 2 "trab.y"
  import java.io.*;
//#line 19 "Parser.java"




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
public final static short INT=257;
public final static short DOUBLE=258;
public final static short BOOLEAN=259;
public final static short VOID=260;
public final static short IDENT=261;
public final static short NUM=262;
public final static short FUNCT=263;
public final static short IF=264;
public final static short ELSE=265;
public final static short WHILE=266;
public final static short AND=267;
public final static short OR=268;
public final static short AND=269;
public final static short OR=270;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    3,    3,    4,    4,    2,    6,
    6,    5,    8,    8,    7,    7,   10,   11,   11,    9,
   12,   12,   13,   13,   13,   13,   15,   15,   14,   14,
   14,   14,   14,   14,   14,   14,   14,   14,   14,   14,
   16,   16,   16,   17,   18,   18,
};
final static short yylen[] = {                            2,
    2,    4,    0,    1,    1,    1,    3,    1,    2,    1,
    0,    4,    3,    4,    1,    1,    3,    1,    3,    3,
    2,    0,    1,    5,    5,    2,    2,    0,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    1,    2,    3,
    3,    2,    0,    2,    2,    0,
};
final static short yydefred[] = {                         0,
    4,    5,    6,    0,    0,    0,    0,    1,    0,    8,
    0,   15,   16,    0,   10,    9,    0,    0,    0,    2,
    7,    0,   12,    0,    0,    0,    0,   13,    0,    0,
    0,   38,    0,    0,    0,   23,    0,    0,    0,    0,
   17,   14,    0,   39,    0,    0,    0,   20,   21,    0,
    0,    0,    0,    0,    0,    0,   26,    0,    0,    0,
   42,    0,    0,    0,    0,   40,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   19,    0,   44,   41,    0,
    0,   45,    0,   24,   25,   27,
};
final static short yydgoto[] = {                          4,
    5,    8,    6,   11,    9,   16,   14,   23,   36,   26,
   41,   37,   38,   39,   84,   44,   63,   78,
};
final static short yysindex[] = {                      -150,
    0,    0,    0,    0, -248, -244, -206,    0, -248,    0,
  -20,    0,    0, -239,    0,    0, -150, -236,    1,    0,
    0,   -7,    0,  -63, -193,   29,  -17,    0, -189,  -63,
   33,    0,   39,   53,   -8,    0,  -45,  -17,   16,   51,
    0,    0,    5,    0,   -8,   -8,  -34,    0,    0,   -8,
   -8,   -8,   -8,   -8,   -8,   -8,    0,   -8,   -8, -150,
    0,   22,   56,  -12,   -5,    0,   43,   49,   49,  -37,
  -37, -226, -226,   43,   43,    0,   -8,    0,    0, -167,
  -17,    0,  -17,    0,    0,    0,
};
final static short yyrindex[] = {                      -152,
    0,    0,    0,    0,    0,    0,    0,    0,  114,    0,
    0,    0,    0,    0,    0,    0, -152,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -10,    0,    0,    0,
  -41,    0,    0,    0,    0,    0,    0,  -10,    0,   75,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   83,    0,    0,    0,    0,   30,  125,  129,   98,
  120,   86,   93,  133,  134,    0,    0,    0,    0,  -24,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  115,  140,   40,    0,    0,    0,    0,    0,   57,   90,
    0,  113,  -69,   67,    0,    0,   79,    0,
};
final static int YYTABLESIZE=319;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         43,
   43,   43,   43,   43,   55,   43,   66,   55,   53,   56,
   54,   85,   56,   86,    7,   28,   10,   43,   43,   43,
   43,   19,   35,   18,   21,   51,   50,   52,   80,   55,
   53,   35,   54,   24,   56,   81,   55,   53,   17,   54,
   22,   56,   58,   59,   35,   61,   13,   51,   50,   52,
    1,    2,    3,   12,   51,   50,   52,   55,   53,   27,
   54,   25,   56,   55,   53,   77,   54,   29,   56,   30,
   29,   40,   43,   29,   57,   51,   50,   52,   45,   48,
   28,   51,   50,   52,   55,   53,   42,   54,   29,   56,
   55,   53,   46,   54,   60,   56,   79,   83,   28,   25,
   28,   47,   51,   50,   52,   27,    1,    2,    3,   62,
    3,   64,   65,   11,   22,   18,   67,   68,   69,   70,
   71,   72,   73,   46,   74,   75,   32,   32,   32,   32,
   32,   20,   32,   33,   33,   33,   33,   33,   30,   33,
   30,   30,   30,   62,   32,   32,   32,   32,   15,   76,
   49,   33,   33,   33,   33,   82,   30,   30,   30,   30,
   31,    0,   31,   31,   31,   35,    0,    0,   35,   34,
    0,    0,   34,   36,   37,    0,   36,   37,   31,   31,
   31,   31,    0,   35,   35,   35,   35,   34,   34,   34,
   34,   36,   37,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   43,   43,    0,
    0,   58,   59,    0,   58,   59,   28,   28,    0,   28,
    0,   28,    0,   31,   32,    0,   33,    0,   34,    1,
    2,    3,   31,   32,    0,    0,   58,   59,    0,    0,
    0,    0,    0,   58,   59,   31,   32,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   58,   59,    0,    0,    0,    0,
   58,   59,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   58,   59,    0,    0,    0,    0,   58,   59,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   42,   47,   41,   42,   43,   47,
   45,   81,   47,   83,  263,   40,  261,   59,   60,   61,
   62,  261,   40,   44,  261,   60,   61,   62,   41,   42,
   43,   40,   45,   41,   47,   41,   42,   43,   59,   45,
   40,   47,  269,  270,   40,   41,    7,   60,   61,   62,
  257,  258,  259,  260,   60,   61,   62,   42,   43,  123,
   45,   22,   47,   42,   43,   44,   45,  261,   47,   41,
   41,  261,   40,   44,   59,   60,   61,   62,   40,  125,
   24,   60,   61,   62,   42,   43,   30,   45,   59,   47,
   42,   43,   40,   45,   44,   47,   41,  265,  123,   60,
  125,   35,   60,   61,   62,  123,  257,  258,  259,   43,
  263,   45,   46,    0,  125,   41,   50,   51,   52,   53,
   54,   55,   56,   41,   58,   59,   41,   42,   43,   44,
   45,   17,   47,   41,   42,   43,   44,   45,   41,   47,
   43,   44,   45,   77,   59,   60,   61,   62,    9,   60,
   38,   59,   60,   61,   62,   77,   59,   60,   61,   62,
   41,   -1,   43,   44,   45,   41,   -1,   -1,   44,   41,
   -1,   -1,   44,   41,   41,   -1,   44,   44,   59,   60,
   61,   62,   -1,   59,   60,   61,   62,   59,   60,   61,
   62,   59,   59,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  269,  270,   -1,
   -1,  269,  270,   -1,  269,  270,  261,  262,   -1,  264,
   -1,  266,   -1,  261,  262,   -1,  264,   -1,  266,  257,
  258,  259,  261,  262,   -1,   -1,  269,  270,   -1,   -1,
   -1,   -1,   -1,  269,  270,  261,  262,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  269,  270,   -1,   -1,   -1,   -1,
  269,  270,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  269,  270,   -1,   -1,   -1,   -1,  269,  270,
};
}
final static short YYFINAL=4;
final static short YYMAXTOKEN=270;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"INT","DOUBLE","BOOLEAN","VOID","IDENT",
"NUM","FUNCT","IF","ELSE","WHILE","AND","OR","\"AND\"","\"OR\"",
};
final static String yyrule[] = {
"$accept : Prog",
"Prog : Decl ListaFuncoes",
"Decl : Tipo LId ';' Decl",
"Decl :",
"Tipo : INT",
"Tipo : DOUBLE",
"Tipo : BOOLEAN",
"LId : LId ',' IDENT",
"LId : IDENT",
"ListaFuncoes : umaFuncao restoListFuncoes",
"restoListFuncoes : ListaFuncoes",
"restoListFuncoes :",
"umaFuncao : FUNCT tipoOuVoid IDENT restoUmaFuncao",
"restoUmaFuncao : '(' ')' Bloco",
"restoUmaFuncao : '(' ListaParametros ')' Bloco",
"tipoOuVoid : VOID",
"tipoOuVoid : Tipo",
"ListaParametros : Tipo IDENT restoListaParametros",
"restoListaParametros : IDENT",
"restoListaParametros : IDENT ',' ListaParametros",
"Bloco : '{' LCmd '}'",
"LCmd : Cmd LCmd",
"LCmd :",
"Cmd : Bloco",
"Cmd : IF '(' E ')' restoIF",
"Cmd : WHILE '(' E ')' Cmd",
"Cmd : E ';'",
"restoIF : ELSE Cmd",
"restoIF :",
"E : E '=' E",
"E : E '+' E",
"E : E '-' E",
"E : E '*' E",
"E : E '/' E",
"E : E '>' E",
"E : E '<' E",
"E : E \"AND\" E",
"E : E \"OR\" E",
"E : NUM",
"E : IDENT restoIDENT",
"E : '(' E ')'",
"restoIDENT : '(' listaExp ')'",
"restoIDENT : '(' ')'",
"restoIDENT :",
"listaExp : E restoListaEXP",
"restoListaEXP : ',' listaExp",
"restoListaEXP :",
};

//#line 100 "trab.y"

  private Yylex lexer;


  private int yylex () {
    int yyl_return = -1;
    try {
      yylval = new ParserVal(0);
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :"+e.getMessage());
    }
    return yyl_return;
  }


  public void yyerror (String error) {
    System.err.println ("Error: " + error);
  }


  public Parser(Reader r) {
    lexer = new Yylex(r, this);
  }


  static boolean interactive;

  public void setDebug(boolean debug) {
    yydebug = debug;
  }


  public static void main(String args[]) throws IOException {
    System.out.println("");

    Parser yyparser;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
    }
    else {
      // interactive mode
      System.out.println("[Quit with CTRL-D]");
      System.out.print("> ");
      interactive = true;
	    yyparser = new Parser(new InputStreamReader(System.in));
    }

    yyparser.yyparse();
    
  //  if (interactive) {
      System.out.println();
      System.out.println("done!");
  //  }
  }

//#line 366 "Parser.java"
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
