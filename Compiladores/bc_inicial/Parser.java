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






//#line 2 "calc.y"

  import java.io.*;
  import java.util.HashMap;
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
public final static short NL=257;
public final static short NUM=258;
public final static short IF=259;
public final static short WHILE=260;
public final static short ELSE=261;
public final static short PRINT=262;
public final static short FOR=263;
public final static short DEFINE=264;
public final static short RETURN=265;
public final static short AND=266;
public final static short OR=267;
public final static short MAIOR_IGUAL=268;
public final static short MENOR_IGUAL=269;
public final static short DIFF=270;
public final static short IGUAL=271;
public final static short SOMA_ATR=272;
public final static short MULT_ATR=273;
public final static short HELP=274;
public final static short SHOW=275;
public final static short SHOW_ALL=276;
public final static short BOOL=277;
public final static short IDENT=278;
public final static short NEG=279;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    3,    3,    3,    5,    5,    6,    6,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    4,    4,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,
};
final static short yylen[] = {                            2,
    0,    2,    2,    1,    2,    2,    5,    6,    1,    3,
    4,    4,    4,    3,   11,    3,    5,    7,    5,    3,
    2,    0,    1,    1,    1,    3,    3,    3,    3,    3,
    2,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    2,
};
final static short yydefred[] = {                         0,
    0,    0,    3,    4,   23,    0,    0,    0,    0,    0,
   25,    0,    0,    0,    0,   22,    0,    0,    2,    0,
    0,    0,    0,   24,    0,    0,    0,    0,    0,    0,
    0,    0,    5,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    6,    0,    0,    0,
    0,   16,   14,    0,    0,    0,   33,    0,   20,   21,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   12,
   13,   11,    0,   19,    0,    0,   22,    0,    0,    0,
   10,    0,   22,   18,    0,    7,    0,    0,    8,    0,
    0,   15,
};
final static short yydgoto[] = {                          2,
   17,   60,   19,   32,   52,   79,
};
final static short yysindex[] = {                      -251,
 -248,  -33,    0,    0,    0,  -23,  -13,  -11, -262,  -30,
    0,  -57,  -30,  -30,  -30,    0,  147, -225,    0,  -30,
  -30, -245,   -5,    0,  161,  -30,  -30,  -30,   88,  -58,
   44,  283,    0,  -30,  -30,  -30,  -30,  -30,  -30,  -30,
  -30,  -30,  -30,  -30,  -30,  -30,    0,   58,   66,  -22,
  -39,    0,    0,  360,  391,  397,    0,  -57,    0,    0,
   88,   88,  -24,  -24,  -24,  -24,  -24,  -24,  -36,  -36,
  -58,  -58,  -58,  266,  266,  -30,   -7,  -73,    3,    0,
    0,    0, -209,    0,  418, -218,    0,  -61,  266,  -30,
    0,  300,    0,    0,  424,    0,  312,  -30,    0,   74,
  266,    0,
};
final static short yyrindex[] = {                         1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  445,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  110,  -17,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  117,  125,  -28,  -21,  -19,   13,   34,   43,   82,  102,
    6,   14,   36,    0,    0,    0,   22,    0,    0,    0,
    0,    0,  237,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  579,  220,    0,  -79,    0,  -15,
};
final static int YYTABLESIZE=716;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         13,
    1,   78,   13,   28,    1,   44,   15,   92,    3,   15,
   45,   14,   35,   97,   14,   23,   20,   44,   43,   36,
   42,   37,   45,   31,   31,   31,   21,   31,   22,   31,
   35,   47,   50,    1,   51,   46,   86,   36,   76,   37,
    1,   31,   31,   88,   31,    1,   28,   28,   28,   87,
   28,   89,   28,   38,   29,   29,   29,   46,   29,   77,
   29,   93,    9,    0,   28,   28,    0,   28,    0,   46,
   91,   38,   29,   29,   30,   29,   32,   32,   32,    0,
   32,    0,   32,   34,   57,   44,   43,    0,   42,   16,
   45,    0,   30,    0,   32,   32,    0,   32,   74,   44,
   43,   34,   42,   40,   45,   41,   75,   44,   43,    0,
   42,    0,   45,    0,  101,   44,   43,   40,   42,   41,
   45,    0,   27,    1,   27,   40,   27,   41,    0,   44,
   43,    0,   42,   40,   45,   41,    0,   46,    0,    0,
   27,   27,   26,   27,   26,    0,   26,    0,    0,    0,
   41,   46,    0,    0,    0,    0,    0,   39,    0,   46,
   26,   26,    0,   26,    0,   40,    0,   46,   41,   41,
    0,   41,    0,    0,    0,   39,   39,    0,   39,    0,
    0,   46,    0,   40,   40,    0,   40,    0,   44,   43,
    0,   42,    0,   45,    0,    0,    0,    0,    0,    0,
    0,    0,   44,   43,    0,   42,   40,   45,   41,    0,
    0,    0,    0,    0,   26,   27,    0,    0,    0,   53,
   40,   18,   41,    4,    5,    6,    7,    5,   35,    8,
    9,   10,    0,    0,    0,   36,    0,   37,   77,   31,
   46,   34,   35,   11,   12,    0,   11,   24,   31,   31,
   31,   31,   31,   31,   46,    0,    0,    1,    1,    1,
    1,    0,   28,    1,    1,    1,    0,    0,    0,   38,
   29,   28,   28,   28,   28,   28,   28,    1,    1,   29,
   29,   29,   29,   29,   29,    0,    0,    0,    0,    0,
   30,    0,   32,   83,   84,    0,    0,    0,    0,   34,
    0,   32,   32,   32,   32,   32,   32,    0,   94,   34,
   35,   36,   37,   38,   39,    0,    0,    0,    0,    0,
  102,    0,    0,   34,   35,   36,   37,   38,   39,    0,
    0,   34,   35,   36,   37,   38,   39,    0,   27,   34,
   35,   36,   37,   38,   39,    0,    0,   27,   27,   27,
   27,   27,   27,    0,    0,    0,    0,    0,   26,   17,
    0,   17,    0,    0,    0,    0,   41,   26,   26,   26,
   26,   26,   26,   39,    0,   41,   41,   41,   41,   41,
   41,   40,   39,   39,   39,   39,   39,   39,   16,    0,
   40,   40,   40,   40,   40,   40,    0,    0,    0,    0,
    0,   44,   43,   33,   42,   16,   45,   59,    0,    0,
    0,    0,   34,   35,   36,   37,   38,   39,   80,   40,
    0,   41,   16,    0,   96,    0,   34,   35,   36,   37,
   38,   39,   44,   43,   16,   42,   99,   45,   44,   43,
    0,   42,    0,   45,    0,    0,    0,    0,    0,   81,
   40,    0,   41,   46,    0,   82,   40,    0,   41,   44,
   43,    0,   42,    0,   45,   44,   43,    0,   42,    0,
   45,    0,    0,    0,    0,    0,   90,   40,    0,   41,
    0,    0,   98,   40,   46,   41,   24,   24,    0,   24,
   46,   24,    0,   17,    0,   17,   17,    0,    0,   17,
   17,   17,    0,    0,   24,    0,   24,    0,    0,    0,
    0,   46,    0,    0,   17,    0,    0,   46,    0,    0,
    0,    0,    0,    0,    6,    7,    0,    0,    8,    9,
   10,    0,    0,    0,    0,    0,    0,    0,   24,    0,
    0,    6,    7,   58,    0,    8,    9,   10,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    6,    7,
   58,    0,    8,    9,   10,    0,    0,    0,    0,    0,
    6,    7,    0,    0,    8,    9,   10,   58,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   25,   58,
    0,   29,   30,   31,    0,    0,    0,    0,   48,   49,
    0,    0,    0,    0,   54,   55,   56,    0,    0,    0,
    0,    0,   61,   62,   63,   64,   65,   66,   67,   68,
   69,   70,   71,   72,   73,   34,   35,   36,   37,   38,
   39,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   85,    0,   34,   35,   36,   37,
   38,   39,   34,   35,   36,   37,   38,   39,   95,    0,
    0,    0,    0,    0,    0,    0,  100,    0,    0,    0,
    0,    0,    0,   34,   35,   36,   37,   38,   39,   34,
   35,   36,   37,   38,   39,    0,    0,    0,    0,    0,
    0,   24,    0,    0,    0,    0,    0,    0,    0,    0,
   24,   24,   24,   24,   24,   24,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
    0,   41,   33,   61,  256,   42,   40,   87,  257,   40,
   47,   45,   41,   93,   45,  278,   40,   42,   43,   41,
   45,   41,   47,   41,   42,   43,   40,   45,   40,   47,
   59,  257,  278,   33,   40,   94,   44,   59,   61,   59,
   40,   59,   60,   41,   62,   45,   41,   42,   43,  123,
   45,  261,   47,   41,   41,   42,   43,   94,   45,  278,
   47,  123,   41,   -1,   59,   60,   -1,   62,   -1,   94,
   86,   59,   59,   60,   41,   62,   41,   42,   43,   -1,
   45,   -1,   47,   41,   41,   42,   43,   -1,   45,  123,
   47,   -1,   59,   -1,   59,   60,   -1,   62,   41,   42,
   43,   59,   45,   60,   47,   62,   41,   42,   43,   -1,
   45,   -1,   47,   -1,   41,   42,   43,   60,   45,   62,
   47,   -1,   41,  123,   43,   60,   45,   62,   -1,   42,
   43,   -1,   45,   60,   47,   62,   -1,   94,   -1,   -1,
   59,   60,   41,   62,   43,   -1,   45,   -1,   -1,   -1,
   41,   94,   -1,   -1,   -1,   -1,   -1,   41,   -1,   94,
   59,   60,   -1,   62,   -1,   41,   -1,   94,   59,   60,
   -1,   62,   -1,   -1,   -1,   59,   60,   -1,   62,   -1,
   -1,   94,   -1,   59,   60,   -1,   62,   -1,   42,   43,
   -1,   45,   -1,   47,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   42,   43,   -1,   45,   60,   47,   62,   -1,
   -1,   -1,   -1,   -1,  272,  273,   -1,   -1,   -1,   59,
   60,    2,   62,  257,  258,  259,  260,  258,  257,  263,
  264,  265,   -1,   -1,   -1,  257,   -1,  257,  278,  257,
   94,  266,  267,  277,  278,   -1,  277,  278,  266,  267,
  268,  269,  270,  271,   94,   -1,   -1,  257,  258,  259,
  260,   -1,  257,  263,  264,  265,   -1,   -1,   -1,  257,
  257,  266,  267,  268,  269,  270,  271,  277,  278,  266,
  267,  268,  269,  270,  271,   -1,   -1,   -1,   -1,   -1,
  257,   -1,  257,   74,   75,   -1,   -1,   -1,   -1,  257,
   -1,  266,  267,  268,  269,  270,  271,   -1,   89,  266,
  267,  268,  269,  270,  271,   -1,   -1,   -1,   -1,   -1,
  101,   -1,   -1,  266,  267,  268,  269,  270,  271,   -1,
   -1,  266,  267,  268,  269,  270,  271,   -1,  257,  266,
  267,  268,  269,  270,  271,   -1,   -1,  266,  267,  268,
  269,  270,  271,   -1,   -1,   -1,   -1,   -1,  257,  123,
   -1,  125,   -1,   -1,   -1,   -1,  257,  266,  267,  268,
  269,  270,  271,  257,   -1,  266,  267,  268,  269,  270,
  271,  257,  266,  267,  268,  269,  270,  271,  123,   -1,
  266,  267,  268,  269,  270,  271,   -1,   -1,   -1,   -1,
   -1,   42,   43,  257,   45,  123,   47,  125,   -1,   -1,
   -1,   -1,  266,  267,  268,  269,  270,  271,   59,   60,
   -1,   62,  123,   -1,  125,   -1,  266,  267,  268,  269,
  270,  271,   42,   43,  123,   45,  125,   47,   42,   43,
   -1,   45,   -1,   47,   -1,   -1,   -1,   -1,   -1,   59,
   60,   -1,   62,   94,   -1,   59,   60,   -1,   62,   42,
   43,   -1,   45,   -1,   47,   42,   43,   -1,   45,   -1,
   47,   -1,   -1,   -1,   -1,   -1,   59,   60,   -1,   62,
   -1,   -1,   59,   60,   94,   62,   42,   43,   -1,   45,
   94,   47,   -1,  257,   -1,  259,  260,   -1,   -1,  263,
  264,  265,   -1,   -1,   60,   -1,   62,   -1,   -1,   -1,
   -1,   94,   -1,   -1,  278,   -1,   -1,   94,   -1,   -1,
   -1,   -1,   -1,   -1,  259,  260,   -1,   -1,  263,  264,
  265,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   94,   -1,
   -1,  259,  260,  278,   -1,  263,  264,  265,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  259,  260,
  278,   -1,  263,  264,  265,   -1,   -1,   -1,   -1,   -1,
  259,  260,   -1,   -1,  263,  264,  265,  278,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   10,  278,
   -1,   13,   14,   15,   -1,   -1,   -1,   -1,   20,   21,
   -1,   -1,   -1,   -1,   26,   27,   28,   -1,   -1,   -1,
   -1,   -1,   34,   35,   36,   37,   38,   39,   40,   41,
   42,   43,   44,   45,   46,  266,  267,  268,  269,  270,
  271,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   76,   -1,  266,  267,  268,  269,
  270,  271,  266,  267,  268,  269,  270,  271,   90,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   98,   -1,   -1,   -1,
   -1,   -1,   -1,  266,  267,  268,  269,  270,  271,  266,
  267,  268,  269,  270,  271,   -1,   -1,   -1,   -1,   -1,
   -1,  257,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  266,  267,  268,  269,  270,  271,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=279;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,null,null,null,"'('","')'","'*'","'+'",
"','","'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,
"';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,"'^'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"NL","NUM","IF","WHILE","ELSE",
"PRINT","FOR","DEFINE","RETURN","AND","OR","MAIOR_IGUAL","MENOR_IGUAL","DIFF",
"IGUAL","SOMA_ATR","MULT_ATR","HELP","SHOW","SHOW_ALL","BOOL","IDENT","NEG",
};
final static String yyrule[] = {
"$accept : input",
"input :",
"input : input line",
"input : error NL",
"line : NL",
"line : exp NL",
"line : cmd NL",
"restFunc : '(' ')' '{' lcmd '}'",
"restFunc : '(' lident ')' '{' lcmd '}'",
"lident : IDENT",
"lident : IDENT ',' lident",
"cmd : IDENT '=' exp ';'",
"cmd : IDENT SOMA_ATR exp ';'",
"cmd : IDENT MULT_ATR exp ';'",
"cmd : RETURN exp ';'",
"cmd : FOR '(' IDENT '=' exp ';' exp ';' exp ')' cmd",
"cmd : DEFINE IDENT restFunc",
"cmd : IF '(' exp ')' cmd",
"cmd : IF '(' exp ')' cmd ELSE cmd",
"cmd : WHILE '(' exp ')' cmd",
"cmd : '{' lcmd '}'",
"lcmd : lcmd cmd",
"lcmd :",
"exp : NUM",
"exp : IDENT",
"exp : BOOL",
"exp : exp '+' exp",
"exp : exp '-' exp",
"exp : exp '*' exp",
"exp : exp '/' exp",
"exp : exp '<' exp",
"exp : '-' exp",
"exp : exp '^' exp",
"exp : '(' exp ')'",
"exp : exp '>' exp",
"exp : exp MAIOR_IGUAL exp",
"exp : exp MENOR_IGUAL exp",
"exp : exp DIFF exp",
"exp : exp IGUAL exp",
"exp : exp AND exp",
"exp : exp OR exp",
"exp : '!' exp",
};

//#line 96 "calc.y"


  public static HashMap<String, ResultValue> memory = new HashMap<>();
  private Yylex lexer;


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
    System.err.println ("Error: " + error);
  }


  public Parser(Reader r) {
    lexer = new Yylex(r, this);
  }


  static boolean interactive;

  public static void main(String args[]) throws IOException {
    System.out.println("BYACC/Java with JFlex Calculator Demo");

    Parser yyparser;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
    }
    else {
      // interactive mode
      System.out.println("[Quit with CTRL-D]");
      System.out.print("Expression: ");
      interactive = true;
	    yyparser = new Parser(new InputStreamReader(System.in));
    }

    yyparser.yyparse();
    
    if (interactive) {
      System.out.println();
      System.out.println("Have a nice day");
    }
  }
//#line 453 "Parser.java"
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
//#line 29 "calc.y"
{yyval.obj=null;}
break;
case 2:
//#line 30 "calc.y"
{ if (val_peek(0).obj != null) {
                           System.out.print("Avaliacao: " + ((INodo) val_peek(0).obj).avalia() +"\n> "); 
							yyval.obj=val_peek(0).obj;
						}
					    else {
                          System.out.print("\n> "); 
						  yyval.obj=null;
						}
					}
break;
case 3:
//#line 39 "calc.y"
{ System.out.println("entrada ignorada"); }
break;
case 4:
//#line 42 "calc.y"
{ if (interactive) System.out.print("\n> "); yyval.obj = null; }
break;
case 5:
//#line 43 "calc.y"
{ yyval.obj = val_peek(1).obj;
		   System.out.println("\n= " + val_peek(1).obj); 
                   if (interactive) System.out.print("\n>: "); }
break;
case 11:
//#line 58 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.ATRIB, val_peek(3).sval, (INodo)val_peek(1).obj); }
break;
case 17:
//#line 64 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.IF,(INodo)val_peek(2).obj, (INodo)val_peek(0).obj, null); }
break;
case 18:
//#line 65 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.IFELSE,(INodo)val_peek(4).obj, (INodo)val_peek(2).obj, (INodo)val_peek(0).obj); }
break;
case 19:
//#line 66 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.WHILE,(INodo)val_peek(2).obj, (INodo)val_peek(0).obj, null); }
break;
case 20:
//#line 67 "calc.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 21:
//#line 70 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.SEQ,(INodo)val_peek(1).obj,(INodo)val_peek(0).obj); }
break;
case 22:
//#line 71 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.NULL, null, null, null); }
break;
case 23:
//#line 75 "calc.y"
{ yyval.obj = new NodoTDouble(val_peek(0).dval); }
break;
case 24:
//#line 76 "calc.y"
{ yyval.obj = new NodoID(val_peek(0).sval); }
break;
case 26:
//#line 78 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.ADD,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 27:
//#line 79 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.SUB,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 28:
//#line 80 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.MULL,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 29:
//#line 81 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.DIV,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 30:
//#line 82 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.LESS,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 31:
//#line 83 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.UMINUS,(INodo)val_peek(0).obj,null); }
break;
case 32:
//#line 84 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.POW,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 33:
//#line 85 "calc.y"
{ yyval.obj = val_peek(1).obj; }
break;
//#line 700 "Parser.java"
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
