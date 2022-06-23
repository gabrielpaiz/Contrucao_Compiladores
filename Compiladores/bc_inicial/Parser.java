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
  import java.util.ArrayList;
  import java.util.Stack;
//#line 23 "Parser.java"




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
public final static short restFunc=279;
public final static short NEG=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    3,    3,    3,    6,    6,    5,    5,
    2,    2,    7,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,    4,    4,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    8,    1,    9,    9,
   10,   10,
};
final static short yylen[] = {                            2,
    0,    2,    2,    1,    2,    2,    1,    0,    1,    3,
    2,    3,    0,    9,    9,    5,    7,    5,    5,    3,
    1,    2,    1,    2,    0,    1,    1,    3,    3,    3,
    3,    3,    3,    3,    3,    2,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    2,    0,    5,    1,    0,
    1,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    3,    4,   26,    0,    0,    0,    0,    0,
    0,   21,    0,   23,    0,    0,    0,    0,   25,    0,
    0,    2,    0,    0,    0,    0,   13,    0,   22,    0,
    0,    0,    0,    0,    0,    0,    0,    5,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   11,    6,    0,    0,    0,    0,    0,   12,    0,
    0,    0,    0,   38,   20,    0,   24,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   18,
   19,    0,    9,    0,    0,   48,    0,    0,    0,    0,
    0,    0,   17,    0,   10,   25,    0,    0,   15,   14,
};
final static short yydgoto[] = {                          2,
   66,   67,   22,   37,   94,   95,   58,   33,   87,   88,
};
final static short yysindex[] = {                      -252,
 -248,   23,    0,    0,    0,  -26,  -21,  -12,   -4, -254,
  -22,    0, -241,    0,  -59,  -22,  -22,  -22,    0,  444,
 -219,    0,  -22,  -22,  -22,  -22,    0,  450,    0,  -22,
  -22,  -22,   -1,  -16,  -54,  150,  -25,    0,  -22,  -22,
  -22,  -22,  -22,  -22,  -22,  -22,  -22,  -22,  -22,  -22,
  -22,    0,    0,  158,  341,  389,  471,    2,    0,  504,
  504,  504,  -22,    0,    0,  477,    0,  527,  527,  -16,
  -16,  -16,  -16,  -16,  -16,  -37,  -37,  -54,  -54,  -54,
   54,   54,  -15,  -22, -235,  504,    4,    3, -212,    0,
    0,  498,    0,    7,   11,    0,  -22,   54,  -22, -225,
  -69,  504,    0,  396,    0,    0,   54,   31,    0,    0,
};
final static short yyrindex[] = {                         1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   67,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  422,   74,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -38,
  -11,   -9,   14,    0,    0,    0,    0,  145,  213,  467,
  534,  536,  542,  547,  579,  348,  416,   98,  106,  128,
    0,    0,    0,    0,   17,  -28,    0,   18,  -33,    0,
    0,    0,    0,   19,    0,    0,    0,    0,    0,    0,
    0,  -19,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  808,   15,    0,  -45,    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=907;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         16,
    1,   32,   29,    1,   49,   29,   16,   16,    3,   50,
   16,   16,   51,   23,   18,   51,   21,   18,   24,   17,
   29,   52,   17,   27,   52,   49,   48,   25,   47,   30,
   50,   28,   30,    1,   28,   26,   29,   53,   63,   51,
    1,   85,   93,   91,   96,    1,   97,   30,   98,   28,
  100,  101,  105,  106,   50,   16,   51,    8,   49,    7,
  108,    0,   18,   16,    0,    0,    0,   17,    0,    0,
   18,    0,    0,    0,    0,   17,    0,   51,    0,    0,
    0,    0,    0,    0,    0,    0,   16,    0,    0,   16,
    0,   16,    0,   18,    0,   89,   90,   19,   17,   65,
    0,    0,    0,    0,    0,    0,   47,   27,   27,   27,
   27,   27,  103,   27,   36,   36,   36,   36,   36,    0,
   36,  109,    0,    1,    0,   27,   27,    0,   27,    0,
    0,    0,   36,   36,    0,   36,    0,    0,   33,   33,
   33,   33,   33,    0,   33,   19,   34,   34,   34,   34,
   34,    0,   34,   19,    0,  110,   33,   33,    0,   33,
   27,    0,    0,    0,   34,   34,    0,   34,   37,   37,
   37,   37,   37,    0,   37,    0,   19,    0,    0,    0,
    0,    0,    0,    0,    0,   44,   37,   37,   44,   37,
   64,   49,   48,    0,   47,    0,   50,    0,   81,   49,
   48,    0,   47,   44,   50,    0,    0,    0,    0,   45,
    0,   46,   30,   31,    0,    0,    0,   45,   29,   46,
    0,    0,    0,   16,   16,   16,   16,    0,   16,   16,
   16,   16,    5,    6,    7,    5,    8,    9,   10,   11,
   16,   16,   16,   51,   16,   30,    0,   28,   12,   13,
   14,   51,   15,   45,    0,   15,   45,    1,    1,    1,
    1,    0,    1,    1,    1,    1,    0,    0,    0,    0,
    0,   45,    0,    0,    1,    1,    1,    0,    1,    4,
    5,    6,    7,    0,    8,    9,   10,   11,    5,    6,
    7,    0,    8,    9,   10,   11,   12,   13,   14,    0,
   15,    0,    0,    0,   12,   13,   14,    0,   15,    0,
    0,    5,    6,    7,    0,    8,    9,   10,   11,    0,
    0,    0,    0,   27,    0,    0,    0,   12,   13,   14,
   36,   15,   27,   27,   27,   27,   27,   27,    0,   36,
   36,   36,   36,   36,   36,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   33,    0,    0,    0,    0,    0,
    0,    0,   34,   33,   33,   33,   33,   33,   33,    0,
    0,   34,   34,   34,   34,   34,   34,    0,    0,    0,
    0,   82,   49,   48,   37,   47,    0,   50,   32,    0,
   32,   32,   32,   37,   37,   37,   37,   37,   37,    0,
   45,   44,   46,    0,    0,    0,   32,   32,    0,   32,
   44,   44,    0,    0,    0,   39,   40,   41,   42,   43,
   44,    0,    0,   39,   40,   41,   42,   43,   44,   83,
   49,   48,    0,   47,   51,   50,  107,   49,   48,    0,
   47,    0,   50,    0,    0,    0,    0,    0,   45,    0,
   46,    0,    0,    0,    0,   45,   31,   46,   31,   31,
   31,    0,   46,    0,    0,   46,    0,    0,    0,   45,
    0,    0,    0,    0,   31,   31,    0,   31,   45,   45,
   46,   46,   51,   46,    0,   49,   48,    0,   47,   51,
   50,   49,   48,    0,   47,    0,   50,    0,    0,    0,
    0,    0,   52,   45,    0,   46,    0,   40,   59,   45,
   40,   46,   49,   48,    0,   47,    0,   50,   49,   48,
    0,   47,    0,   50,    0,   40,    0,    0,    0,   84,
   45,    0,   46,    0,    0,   52,   45,   51,   46,   49,
   48,    0,   47,   51,   50,   49,   48,    0,   47,    0,
   50,    0,    0,    0,    0,    0,   99,   45,    0,   46,
    0,    0,    0,   45,   51,   46,    0,    0,   49,   48,
   51,   47,    0,   50,   41,    0,   42,   41,    0,   42,
    0,    0,   43,    0,    0,   43,   45,   35,   46,    0,
   35,   51,   41,    0,   42,    0,    0,   51,    0,    0,
   43,    0,    0,    0,   32,   35,   39,   40,   41,   42,
   43,   44,    0,   32,   32,   32,   32,   32,   32,   39,
   51,    0,   39,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   39,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   39,   40,   41,   42,   43,   44,
    0,   39,   40,   41,   42,   43,   44,    0,    0,    0,
    0,    0,   31,    0,    0,    0,    0,    0,   46,    0,
    0,   31,   31,   31,   31,   31,   31,   46,   46,   46,
   46,   46,   46,    0,    0,    0,    0,    0,    0,    0,
   38,    0,    0,    0,    0,    0,    0,    0,    0,   39,
   40,   41,   42,   43,   44,   39,   40,   41,   42,   43,
   44,    0,    0,   40,    0,    0,    0,    0,    0,    0,
    0,    0,   40,   40,    0,    0,   39,   40,   41,   42,
   43,   44,   39,   40,   41,   42,   43,   44,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   39,   40,   41,   42,   43,   44,   39,
   40,   41,   42,   43,   44,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   41,    0,   42,    0,   41,   42,   43,   44,   43,   41,
   41,   42,   42,   35,    0,    0,    0,   43,   43,   20,
    0,    0,   35,   35,    0,    0,    0,    0,   28,    0,
    0,    0,    0,   34,   35,   36,    0,    0,    0,    0,
   54,   55,   56,   57,    0,   39,    0,   60,   61,   62,
    0,    0,    0,    0,   39,   39,   68,   69,   70,   71,
   72,   73,   74,   75,   76,   77,   78,   79,   80,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   86,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   92,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  102,    0,  104,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
    0,   61,   41,  256,   42,   44,   40,   33,  257,   47,
   33,   45,   41,   40,   40,   44,    2,   40,   40,   45,
   59,   41,   45,  278,   44,   42,   43,   40,   45,   41,
   47,   41,   44,   33,   44,   40,  278,  257,   40,   94,
   40,   40,  278,   59,   41,   45,   44,   59,  261,   59,
   44,   41,  278,  123,   41,   33,   94,   41,   41,   41,
  106,   -1,   40,   33,   -1,   -1,   -1,   45,   -1,   -1,
   40,   -1,   -1,   -1,   -1,   45,   -1,   94,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   33,   -1,   -1,  123,
   -1,  125,   -1,   40,   -1,   81,   82,  123,   45,  125,
   -1,   -1,   -1,   -1,   -1,   -1,   40,   41,   42,   43,
   44,   45,   98,   47,   41,   42,   43,   44,   45,   -1,
   47,  107,   -1,  123,   -1,   59,   60,   -1,   62,   -1,
   -1,   -1,   59,   60,   -1,   62,   -1,   -1,   41,   42,
   43,   44,   45,   -1,   47,  123,   41,   42,   43,   44,
   45,   -1,   47,  123,   -1,  125,   59,   60,   -1,   62,
   94,   -1,   -1,   -1,   59,   60,   -1,   62,   41,   42,
   43,   44,   45,   -1,   47,   -1,  123,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   41,   59,   60,   44,   62,
   41,   42,   43,   -1,   45,   -1,   47,   -1,   41,   42,
   43,   -1,   45,   59,   47,   -1,   -1,   -1,   -1,   60,
   -1,   62,  272,  273,   -1,   -1,   -1,   60,  257,   62,
   -1,   -1,   -1,  257,  258,  259,  260,   -1,  262,  263,
  264,  265,  258,  259,  260,  258,  262,  263,  264,  265,
  274,  275,  276,   94,  278,  257,   -1,  257,  274,  275,
  276,   94,  278,   41,   -1,  278,   44,  257,  258,  259,
  260,   -1,  262,  263,  264,  265,   -1,   -1,   -1,   -1,
   -1,   59,   -1,   -1,  274,  275,  276,   -1,  278,  257,
  258,  259,  260,   -1,  262,  263,  264,  265,  258,  259,
  260,   -1,  262,  263,  264,  265,  274,  275,  276,   -1,
  278,   -1,   -1,   -1,  274,  275,  276,   -1,  278,   -1,
   -1,  258,  259,  260,   -1,  262,  263,  264,  265,   -1,
   -1,   -1,   -1,  257,   -1,   -1,   -1,  274,  275,  276,
  257,  278,  266,  267,  268,  269,  270,  271,   -1,  266,
  267,  268,  269,  270,  271,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  257,  266,  267,  268,  269,  270,  271,   -1,
   -1,  266,  267,  268,  269,  270,  271,   -1,   -1,   -1,
   -1,   41,   42,   43,  257,   45,   -1,   47,   41,   -1,
   43,   44,   45,  266,  267,  268,  269,  270,  271,   -1,
   60,  257,   62,   -1,   -1,   -1,   59,   60,   -1,   62,
  266,  267,   -1,   -1,   -1,  266,  267,  268,  269,  270,
  271,   -1,   -1,  266,  267,  268,  269,  270,  271,   41,
   42,   43,   -1,   45,   94,   47,   41,   42,   43,   -1,
   45,   -1,   47,   -1,   -1,   -1,   -1,   -1,   60,   -1,
   62,   -1,   -1,   -1,   -1,   60,   41,   62,   43,   44,
   45,   -1,   41,   -1,   -1,   44,   -1,   -1,   -1,  257,
   -1,   -1,   -1,   -1,   59,   60,   -1,   62,  266,  267,
   59,   60,   94,   62,   -1,   42,   43,   -1,   45,   94,
   47,   42,   43,   -1,   45,   -1,   47,   -1,   -1,   -1,
   -1,   -1,   59,   60,   -1,   62,   -1,   41,   59,   60,
   44,   62,   42,   43,   -1,   45,   -1,   47,   42,   43,
   -1,   45,   -1,   47,   -1,   59,   -1,   -1,   -1,   59,
   60,   -1,   62,   -1,   -1,   59,   60,   94,   62,   42,
   43,   -1,   45,   94,   47,   42,   43,   -1,   45,   -1,
   47,   -1,   -1,   -1,   -1,   -1,   59,   60,   -1,   62,
   -1,   -1,   -1,   60,   94,   62,   -1,   -1,   42,   43,
   94,   45,   -1,   47,   41,   -1,   41,   44,   -1,   44,
   -1,   -1,   41,   -1,   -1,   44,   60,   41,   62,   -1,
   44,   94,   59,   -1,   59,   -1,   -1,   94,   -1,   -1,
   59,   -1,   -1,   -1,  257,   59,  266,  267,  268,  269,
  270,  271,   -1,  266,  267,  268,  269,  270,  271,   41,
   94,   -1,   44,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   59,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  266,  267,  268,  269,  270,  271,
   -1,  266,  267,  268,  269,  270,  271,   -1,   -1,   -1,
   -1,   -1,  257,   -1,   -1,   -1,   -1,   -1,  257,   -1,
   -1,  266,  267,  268,  269,  270,  271,  266,  267,  268,
  269,  270,  271,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  257,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  266,
  267,  268,  269,  270,  271,  266,  267,  268,  269,  270,
  271,   -1,   -1,  257,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  266,  267,   -1,   -1,  266,  267,  268,  269,
  270,  271,  266,  267,  268,  269,  270,  271,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  266,  267,  268,  269,  270,  271,  266,
  267,  268,  269,  270,  271,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  257,   -1,  257,   -1,  268,  269,  270,  271,  257,  266,
  267,  266,  267,  257,   -1,   -1,   -1,  266,  267,    2,
   -1,   -1,  266,  267,   -1,   -1,   -1,   -1,   11,   -1,
   -1,   -1,   -1,   16,   17,   18,   -1,   -1,   -1,   -1,
   23,   24,   25,   26,   -1,  257,   -1,   30,   31,   32,
   -1,   -1,   -1,   -1,  266,  267,   39,   40,   41,   42,
   43,   44,   45,   46,   47,   48,   49,   50,   51,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   63,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   84,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   97,   -1,   99,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=280;
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
"IGUAL","SOMA_ATR","MULT_ATR","HELP","SHOW","SHOW_ALL","BOOL","IDENT",
"restFunc","NEG",
};
final static String yyrule[] = {
"$accept : input",
"input :",
"input : input line",
"input : error NL",
"line : NL",
"line : exp NL",
"line : cmd NL",
"params : lident",
"params :",
"lident : IDENT",
"lident : lident ',' IDENT",
"cmd : exp ';'",
"cmd : RETURN exp ';'",
"$$1 :",
"cmd : DEFINE IDENT $$1 '(' params ')' '{' lcmd '}'",
"cmd : FOR '(' exp ';' exp ';' exp ')' cmd",
"cmd : IF '(' exp ')' cmd",
"cmd : IF '(' exp ')' cmd ELSE cmd",
"cmd : WHILE '(' exp ')' cmd",
"cmd : PRINT '(' exp ')' ';'",
"cmd : '{' lcmd '}'",
"cmd : HELP",
"cmd : SHOW IDENT",
"cmd : SHOW_ALL",
"lcmd : lcmd cmd",
"lcmd :",
"exp : NUM",
"exp : IDENT",
"exp : IDENT '=' exp",
"exp : IDENT SOMA_ATR exp",
"exp : IDENT MULT_ATR exp",
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
"$$2 :",
"exp : IDENT $$2 '(' valParams ')'",
"valParams : lparam",
"valParams :",
"lparam : exp",
"lparam : lparam ',' exp",
};

//#line 137 "calc.y"


  public static HashMap<String, ResultValue> memory = new HashMap<>();
  public static HashMap<String, NodoFunc> funcs = new HashMap<>();

  public static Stack<HashMap<String, ResultValue>> scopo = new Stack<>();

  public ArrayList<ResultValue> actualParam;
  public ArrayList<String> currParam;
  public ResultValue avalia;
  public boolean erro = true;
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
    if(erro)
      System.err.println ("Error: "+ error+" in text '"+lexer.GetText()+"' in line "+lexer.GetLine());
    else
      System.err.println ("Error: "+ error+" in line "+lexer.GetLine());
  } 


  public Parser(Reader r) {
    lexer = new Yylex(r, this);
  }


  static boolean interactive = false;

  public static HashMap<String, ResultValue> popStack(){
    return scopo.pop();  
  }

  public static void pushStack(HashMap<String, ResultValue> mem){
    scopo.push(mem);  
  }

  public static void printStack(){
    System.out.println(scopo);
  }

  public static HashMap<String, ResultValue> peekStack(){
    return scopo.peek();
  }

  public static void help(){
    System.out.println("Informações:");
    System.out.println("\nPara usar funções é necessario cria-lás primeiros: Ex.: define nomefuncao(param1, param2 ...){lista de comandos};");
    System.out.println("Não foi implementado o '++', por isso ultilize +=1 no lugar: Ex.: for(i=0;i<10;i+=1;){...};");
    System.out.println("Não é possivel quebrar a linha no meio das funções e comandos, escreva tudo em uma linha só, separado por ';';");
    System.out.println("Todas as operações aritméticas básicas estão disponiveis;");
    System.out.println("\n");
    System.out.println("Comandos:");
    System.out.println("\n#help - Para aparecer a ajuda;");
    System.out.println("\n#show [ident] - Apresenta uma tabela com os valores associados a esse identificador;");
    System.out.println("\n#show_all - Apresenta uma tabela com todos os valores associados a todos os identificador;");
    System.out.println("\nUse a calculadora com sabedoria!");
  }

  public static void show(String ident){
    if(!scopo.peek().containsKey(ident) && !funcs.containsKey(ident))
      System.out.println(ident+" não é uma varaivel nem uma função");
    else {
      if(scopo.peek().containsKey(ident)){
        System.out.println("Variaveis:");
        System.out.println(ident +" -> "+scopo.peek().get(ident).getDouble());
        }
      }
      if(funcs.containsKey(ident)){
        System.out.println("Funções:");
        String parameters = "";
        for(int i = 0; i< funcs.get(ident).getParams().size(); i++){
          parameters += funcs.get(ident).getParams().get(i) + ',';
        }
        System.out.println(ident +" -> "+funcs.get(ident).getIdent()+"("+parameters+")");
      }
    }

    public static void show_all(){
    if(scopo.peek().size() == 0 && funcs.size()== 0)
      System.out.println("Nenhuma variavel e função foram instaciadas!");
    else {
      if(scopo.peek().size() > 0){
        System.out.println("Variaveis:");
        scopo.peek().forEach((key, value) -> {
          System.out.println(key +" -> "+scopo.peek().get(key).getDouble());
        });
      }
      if(funcs.size() > 0){
        System.out.println("Funções:");
        funcs.forEach((key, value) -> {
          String parameters = "";
          for(int i = 0; i< funcs.get(key).getParams().size(); i++){
            parameters += funcs.get(key).getParams().get(i) + ',';
          }
          System.out.println(key +" -> "+funcs.get(key).getIdent()+"("+parameters+")");
        });
      }
    }
      
  }

  public static void main(String args[]) throws IOException {
    System.out.println("BYACC/Java with JFlex Calculator Demo");

    Parser yyparser;
    scopo.push(memory);
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
//#line 594 "Parser.java"
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
//#line 31 "calc.y"
{yyval.obj=null;}
break;
case 2:
//#line 32 "calc.y"
{     avalia = ((INodo) val_peek(0).obj).avalia();
                           if (avalia != null) {
                           if(interactive)
                              System.out.print("Avaliacao: " + avalia +"\n> "); 
							             yyval.obj=val_peek(0).obj;
						            }
					              else {
                          if(interactive)
                            System.out.print("\n> "); 
						              yyval.obj=null;
						            }
					}
break;
case 3:
//#line 44 "calc.y"
{ System.out.println("entrada ignorada"); }
break;
case 4:
//#line 47 "calc.y"
{ if (interactive) System.out.print("\n> "); yyval.obj = null; }
break;
case 5:
//#line 48 "calc.y"
{ yyval.obj = val_peek(1).obj;
		   System.out.println("\n= " + val_peek(1).obj); 
                   if (interactive) System.out.print("\n>: "); }
break;
case 9:
//#line 59 "calc.y"
{currParam.add(val_peek(0).sval);}
break;
case 10:
//#line 60 "calc.y"
{currParam.add(val_peek(0).sval);}
break;
case 11:
//#line 63 "calc.y"
{yyval.obj = val_peek(1).obj;}
break;
case 12:
//#line 64 "calc.y"
{yyval.obj = val_peek(1).obj;}
break;
case 13:
//#line 65 "calc.y"
{currParam = new ArrayList<>();}
break;
case 14:
//#line 65 "calc.y"
{yyval.obj = new NodoFunc(currParam, val_peek(7).sval, (INodo)val_peek(1).obj);}
break;
case 15:
//#line 66 "calc.y"
{yyval.obj = new NodoNT(TipoOperacao.FOR, (INodo)val_peek(6).obj, (INodo)val_peek(4).obj,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj);}
break;
case 16:
//#line 67 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.IF,(INodo)val_peek(2).obj, (INodo)val_peek(0).obj, null); }
break;
case 17:
//#line 68 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.IFELSE,(INodo)val_peek(4).obj, (INodo)val_peek(2).obj, (INodo)val_peek(0).obj); }
break;
case 18:
//#line 69 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.WHILE,(INodo)val_peek(2).obj, (INodo)val_peek(0).obj, null); }
break;
case 19:
//#line 70 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.PRINT,(INodo)val_peek(2).obj, null);}
break;
case 20:
//#line 71 "calc.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 21:
//#line 72 "calc.y"
{help(); yyval.obj = new NodoNT(TipoOperacao.NULL, null, null, null);}
break;
case 22:
//#line 73 "calc.y"
{show(val_peek(0).sval); yyval.obj = new NodoNT(TipoOperacao.NULL, null, null, null);}
break;
case 23:
//#line 74 "calc.y"
{show_all(); yyval.obj = new NodoNT(TipoOperacao.NULL, null, null, null);}
break;
case 24:
//#line 77 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.SEQ,(INodo)val_peek(1).obj,(INodo)val_peek(0).obj); }
break;
case 25:
//#line 78 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.NULL, null, null, null); }
break;
case 26:
//#line 82 "calc.y"
{ yyval.obj = new NodoTDouble(val_peek(0).dval); }
break;
case 27:
//#line 83 "calc.y"
{ yyval.obj = new NodoID(val_peek(0).sval); }
break;
case 28:
//#line 84 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.ATRIB, val_peek(2).sval, (INodo)val_peek(0).obj); }
break;
case 29:
//#line 85 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.SOMA_ATRIB, val_peek(2).sval, (INodo)val_peek(0).obj); }
break;
case 30:
//#line 86 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.MULT_ATRIB, val_peek(2).sval, (INodo)val_peek(0).obj); }
break;
case 31:
//#line 87 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.ADD,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 32:
//#line 88 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.SUB,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 33:
//#line 89 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.MULL,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 34:
//#line 90 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.DIV,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 35:
//#line 91 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.LESS,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 36:
//#line 92 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.UMINUS,(INodo)val_peek(0).obj,null); }
break;
case 37:
//#line 93 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.POW,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 38:
//#line 94 "calc.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 39:
//#line 95 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.GREAT,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 40:
//#line 96 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.GREAT_EQ,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 41:
//#line 97 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.LESS_EQ,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 42:
//#line 98 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.DIFF,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 43:
//#line 99 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.EQUAL,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 44:
//#line 100 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.AND,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 45:
//#line 101 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.OR,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 46:
//#line 102 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.NEG,(INodo)val_peek(0).obj,null); }
break;
case 47:
//#line 103 "calc.y"
{actualParam = new ArrayList<>(); erro = false;}
break;
case 48:
//#line 104 "calc.y"
{if(!funcs.containsKey(val_peek(4).sval))
                                  yyerror("função "+val_peek(4).sval+"() não existe");
                                else{
                                  currParam = funcs.get(val_peek(4).sval).getParams(); 
                                  if(actualParam.size() < currParam.size())
                                      yyerror("Parametros faltando na função "+val_peek(4).sval+"()");
                                  else if(actualParam.size() > currParam.size())
                                      yyerror("Parametros a mais na função "+val_peek(4).sval+"()");
                                  else{
                                      ResultValue result = funcs.get(val_peek(4).sval).executa(actualParam);
                                      if(result.getType() == TypeEnum.DOUBLE)
                                        yyval.obj = new NodoTDouble(result.getDouble());
                                      else if(result.getType() == TypeEnum.BOOLEAN)
                                        yyval.obj = new NodoTBool(result.getBool());
                                      else{
                                        yyerror("Resultado invalido da função "+val_peek(4).sval+"()");
                                        yyval.obj = new NodoNT(TipoOperacao.NULL, null, null, null);;
                                        }
                                  }
                                }
                                erro = true;
                              }
break;
case 51:
//#line 132 "calc.y"
{actualParam.add( ((INodo)val_peek(0).obj).avalia() );}
break;
case 52:
//#line 133 "calc.y"
{actualParam.add( ((INodo)val_peek(0).obj).avalia() );}
break;
//#line 965 "Parser.java"
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
