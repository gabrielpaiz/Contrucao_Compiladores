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






//#line 8 "calc.y"

  import java.io.*;
  import java.util.HashMap;
  import java.util.ArrayList;
  import java.util.List;
  import java.util.Stack;
//#line 24 "Parser.java"




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
public final static short EQUALS=259;
public final static short DIFF=260;
public final static short GE=261;
public final static short LE=262;
public final static short AND=263;
public final static short OR=264;
public final static short PLUS_EQ=265;
public final static short TIMES_EQ=266;
public final static short PLUS_PLUS=267;
public final static short MINUS_MINUS=268;
public final static short DEFINE=269;
public final static short FOR=270;
public final static short IF=271;
public final static short ELSE=272;
public final static short WHILE=273;
public final static short PRINT=274;
public final static short HELP=275;
public final static short SHOW_ALL=276;
public final static short SHOW=277;
public final static short RETURN=278;
public final static short IDENT=279;
public final static short NEG=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    3,    3,    3,    6,    6,    7,    7,
    2,    2,    2,    2,    2,    2,    2,    8,    2,    2,
    2,    2,    5,    5,    5,    4,    4,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,   10,    1,
    1,    9,    9,   11,   11,
};
final static short yylen[] = {                            2,
    0,    2,    2,    1,    2,    2,    1,    0,    3,    1,
    4,    4,    4,    5,    7,    5,    9,    0,    9,    3,
    3,    1,    1,    3,    0,    2,    0,    1,    1,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    2,    2,    3,    2,    2,    2,    2,    0,    5,
    3,    1,    0,    3,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    3,    4,   28,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   27,    0,    0,    2,
   47,   48,    0,    0,    0,    0,    0,    0,    0,    0,
   45,   46,    0,   49,    0,    0,    0,    0,    5,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    6,   18,    0,    0,    0,    0,    0,   20,
    0,    0,    0,    0,   51,   21,    0,   26,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   12,   13,   11,    0,
    0,    0,   10,    0,    0,    0,    0,    0,   16,   50,
    0,    0,    0,    0,    0,    0,   27,    9,    0,   15,
    0,    0,   19,   17,
};
final static short yydgoto[] = {                          2,
   67,   68,   20,   38,   57,   94,   95,   82,   91,   64,
   92,
};
final static short yysindex[] = {                      -252,
 -251,  654,    0,    0,    0, -247, -244, -243,   -2,    2,
    4,   40,   -1,   40,   40,   40,    0, 1117, -217,    0,
    0,    0,    5,   72,   40,   40,  -35, 1124,   40,   40,
    0,    0,   40,    0,  545, -241, 1094,  827,    0,   40,
   40,   40,   40,   40,   40,   40,   40,   40,   40,   40,
   40,   40,    0,    0,  -24, 1184,  -12, 1101, 1108,    0,
 1131, 1154, 1161,   40,    0,    0, 1184,    0, 1184, 1184,
   46,   46,  545, 1190,   46,   46,   16,   16,  -79,  -79,
  -79, -231,   40,   72,  855,  855,    0,    0,    0, 1184,
    8,    6,    0,   10,    9, 1184,   -7, -218,    0,    0,
   40,  -68, -223,   72,  855, 1184,    0,    0,   18,    0,
  841,  855,    0,    0,
};
final static short yyrindex[] = {                         1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  411,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    3,    0,    0,   24,    0,    0,    0,
    0,    0,    0,    0,  722,  -33,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  113,  -38,    0,    0,    0,    0,
    0,    0,    0,   23,    0,    0,  295,    0,  768,  795,
  434,  458,  739,  -16,  481,  553,  365,  388,   54,   86,
  124,   29,    0,    3,    0,    0,    0,    0,    0,  -21,
    0,   31,    0,    0,   33,  -28,    0,  813,    0,    0,
    0,    0,    0,   34,    0,  -11,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
 1247,   77,    0,  -31,  -82,    0,    0,    0,    0,    0,
    0,
};
final static int YYTABLESIZE=1453;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         43,
    1,   97,   23,    1,   34,    3,   43,   43,   43,   43,
   43,   43,   24,   43,   52,   34,   41,   40,   41,   55,
   23,  109,   55,   41,   41,   43,   43,   41,   43,   54,
   24,   21,   54,    1,   22,   23,   83,   24,   34,   53,
    1,   25,   41,   26,   54,    1,   84,   93,  100,  101,
  102,  104,  103,  105,  107,  108,   29,   50,  112,   33,
   43,   25,   51,   53,   29,   29,   29,   29,   29,    8,
   29,   52,   14,    7,   25,  111,    0,    0,   19,   16,
    0,    0,   29,   29,   15,   29,   32,   50,   49,   43,
   48,   43,   51,   32,   32,   32,   32,   32,   32,    0,
   32,    0,    0,    0,   14,    0,   41,    0,   41,   52,
    0,   16,   32,   32,    0,   32,   15,   29,   33,    0,
    0,    0,    0,    1,    0,   33,   33,   33,   33,   33,
   33,    0,   33,    0,    0,    0,    0,    0,    0,   52,
    0,    0,    0,    0,   33,   33,   29,   33,   29,    0,
    0,    0,    0,   29,   29,   29,   44,   29,    0,   29,
    0,   98,   99,   44,   44,   44,   44,   44,   44,    0,
   44,   29,   29,    0,   29,    0,   32,    0,   32,   40,
   41,  110,   44,   44,    0,   44,    0,    0,  114,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   29,    0,   33,    0,
   33,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   43,   43,    0,    0,   43,   43,   43,
   43,   31,   32,   43,   43,   43,   43,   43,   43,   43,
   41,   41,   31,   32,   43,   43,   44,   41,   44,    0,
   41,   41,   41,   41,   41,   41,   41,    1,    1,    0,
    0,   41,   41,   29,   30,   31,   32,    1,    1,    1,
    1,    1,    0,    1,   40,   41,    0,    0,    1,    1,
   29,   29,   29,   29,   29,   29,   29,   29,    0,    0,
    0,    0,   29,   29,   29,   29,   29,    5,    0,    0,
    0,   29,   29,    0,   40,   41,    6,    7,    0,    0,
   32,   32,    0,    0,   32,   32,   32,   32,   27,    0,
   32,   32,   32,   32,   32,   32,   32,   22,    0,    5,
    0,   32,   32,    0,   22,    0,    0,    0,    6,    7,
    0,    0,   33,   33,    0,    0,   33,   33,   33,   33,
   55,    0,   33,   33,   33,   33,   33,   33,   33,    0,
    0,    0,    0,   33,   33,    0,    0,    0,    0,    0,
    0,   29,   29,   29,   29,   29,   29,    0,    0,    0,
   44,   44,    0,    0,   44,   44,   44,   44,    0,    0,
   44,   44,   44,   44,   44,   44,   44,   31,    0,    0,
    0,   44,   44,    0,   31,   31,    0,   31,   31,   31,
    0,    0,    0,    0,    0,    0,    0,   22,    0,   22,
   30,    0,    0,   31,   31,    0,   31,   30,   30,    0,
   30,   30,   30,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   29,    0,    0,   30,   30,    0,   30,
    0,    0,   29,   29,    0,   29,    0,   29,    0,    0,
    0,    0,    0,    0,    0,    0,   36,    0,    0,    0,
   29,    0,   29,   36,   36,    0,    0,   36,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   31,    0,   31,
   37,    0,   36,   36,    0,   36,    0,   37,   37,    0,
    0,   37,    0,    0,   29,    0,    0,    0,    0,    0,
   30,    0,   30,   34,    0,    0,   37,   37,    0,   37,
   34,   34,    0,    0,   34,    0,    0,    0,    0,    0,
    0,    0,    0,   29,    0,   29,    0,    0,    0,   34,
   34,    0,   34,    0,    0,    0,    0,    0,    0,    0,
    0,   22,   22,    0,    0,    0,   36,    0,   36,    0,
    0,   22,   22,   22,   22,   22,   22,   22,    0,    0,
    0,    0,   22,   22,    0,    0,    0,    0,    0,    0,
   37,    0,   37,    0,    0,   35,   50,   49,    0,   48,
    0,   51,   35,   35,    0,    0,   35,    0,    0,    0,
    0,    0,    0,   34,   46,   34,   47,    0,    0,    0,
    0,   35,   35,    0,   35,    0,    0,    0,    0,    0,
    0,   31,   31,    0,    0,   31,   31,   31,   31,    0,
    0,   31,   31,   31,   31,   31,   31,   31,   52,    0,
    0,    0,   31,   31,   30,   30,    0,    0,   30,   30,
   30,   30,    0,    0,   30,   30,   30,   30,   30,   30,
   30,    0,    0,    0,    0,   30,   30,   29,   29,   29,
   29,   29,   29,   29,   29,   35,    0,   35,    0,   29,
   29,   29,   29,   29,    0,    0,   14,    0,   29,   29,
   36,   36,    0,   16,   36,   36,   36,   36,   15,    0,
   36,   36,   36,   36,   36,   36,   36,    0,    0,    0,
    0,   36,   36,    0,   37,   37,    0,    0,   37,   37,
   37,   37,    0,    0,   37,   37,   37,   37,   37,   37,
   37,    0,    0,    0,    0,   37,   37,   34,   34,    0,
    0,   34,   34,   34,   34,    0,    0,   34,   34,   34,
   34,   34,   34,   34,   42,    0,    0,    0,   34,   34,
    0,   42,   42,    0,    0,   42,    0,    0,    0,    0,
    0,   40,    0,    0,    0,    0,   17,    0,   40,   40,
   42,    0,   40,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   40,    0,    0,
   38,    0,    0,   40,   41,   42,   43,   38,   38,   35,
   35,   38,    0,   35,   35,   35,   35,    0,    0,   35,
   35,   35,   35,   35,   35,   35,   38,   39,    0,    0,
   35,   35,    0,    0,   39,   39,    0,    0,   39,    0,
    0,    0,    0,    0,   42,   14,   42,    0,    0,    0,
    0,    0,   14,   39,    0,    0,    0,   14,    0,   14,
    0,   40,    0,   40,    0,    0,   16,    0,    0,    0,
    0,   15,    0,   14,    0,    0,    0,    0,    0,    0,
   16,    0,    0,    0,    0,   15,    0,   14,    0,    0,
   38,    0,   38,    0,   16,    0,    0,    0,    0,   15,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    4,    5,    0,    0,    0,    0,    0,   39,    0,   39,
    6,    7,    8,    9,   10,    0,   11,    0,    0,    0,
    0,   12,   13,    0,    0,   14,    0,   14,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   17,
    0,   66,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,  113,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   17,   42,   42,
    0,    0,    0,    0,   42,   42,    0,    0,   42,   42,
   42,   42,   42,   42,   42,   40,   40,    0,    0,   42,
   42,   40,   40,    0,    0,   40,   40,   40,   40,   40,
   40,   40,    0,    0,    0,    0,   40,   40,    0,    0,
    0,    0,    0,    0,   38,   38,    0,    0,    0,    0,
    0,    0,    0,    0,   38,   38,   38,   38,   38,   38,
   38,    0,    0,    0,    0,   38,   38,    0,    0,    0,
    0,   39,   39,    0,    0,    0,    0,    0,    0,    0,
    0,   39,   39,   39,   39,   39,   39,   39,    0,   14,
   14,    0,   39,   39,    0,    0,    0,    0,    0,   14,
   14,   14,   14,   14,    5,   14,    0,    0,    0,    0,
   14,   14,    0,    6,    7,    8,    9,   10,    5,   11,
    0,    0,    0,    0,   12,   13,    0,    6,    7,    8,
    9,   10,    5,   11,    0,    0,    0,    0,   12,   13,
    0,    6,    7,    8,    9,   10,    0,   11,    0,    0,
    0,    0,   12,   13,   65,   50,   49,    0,   48,    0,
   51,   85,   50,   49,    0,   48,    0,   51,   86,   50,
   49,    0,   48,   46,   51,   47,    0,    0,   50,   49,
   46,   48,   47,   51,    0,   50,   49,   46,   48,   47,
   51,    0,   50,   49,    0,   48,   46,   51,   47,    0,
    0,    0,   60,   46,    0,   47,    0,   52,    0,   87,
   46,    0,   47,    0,   52,   50,   49,    0,   48,    0,
   51,   52,   50,   49,    0,   48,    0,   51,    0,    0,
   52,    0,   88,   46,    0,   47,    0,   52,    0,   89,
   46,    0,   47,    0,   52,   50,   49,    0,   48,    0,
   51,   50,   49,    0,   48,    0,   51,    0,    0,    0,
    0,    0,    0,   46,    0,   47,    0,   52,   18,   46,
    0,   47,    0,    0,   52,    0,    0,    0,   28,    0,
   35,   36,   37,    0,    0,    0,    0,    0,    0,    0,
   56,   58,   59,    0,    0,   61,   62,   52,    0,   63,
    0,    0,    0,   52,    0,    0,   69,   70,   71,   72,
   73,   74,   75,   76,   77,   78,   79,   80,   81,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   90,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   96,
   56,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  106,    0,    0,
   56,    0,   40,   41,   42,   43,   44,   45,    0,   40,
   41,   42,   43,   44,   45,    0,   40,   41,   42,   43,
   44,   45,    0,   39,    0,   40,   41,   42,   43,   44,
   45,    0,   40,   41,   42,   43,   44,   45,    0,   40,
   41,   42,   43,   44,   45,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   40,   41,   42,   43,   44,   45,    0,   40,
   41,   42,   43,   44,   45,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   40,   41,   42,   43,   44,   45,   40,   41,
   42,   43,   44,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
    0,   84,   41,  256,   40,  257,   40,   41,   42,   43,
   44,   45,   41,   47,   94,   40,   33,  259,  260,   41,
   59,  104,   44,   40,   41,   59,   60,   44,   62,   41,
   59,  279,   44,   33,  279,  279,   61,   40,   40,  257,
   40,   40,   59,   40,   40,   45,   59,  279,   41,   44,
   41,   59,   44,  272,  123,  279,   33,   42,   41,   61,
   94,   59,   47,   41,   41,   42,   43,   44,   45,   41,
   47,   41,   33,   41,   41,  107,   -1,   -1,    2,   40,
   -1,   -1,   59,   60,   45,   62,   33,   42,   43,  123,
   45,  125,   47,   40,   41,   42,   43,   44,   45,   -1,
   47,   -1,   -1,   -1,   33,   -1,  123,   -1,  125,   94,
   -1,   40,   59,   60,   -1,   62,   45,   94,   33,   -1,
   -1,   -1,   -1,  123,   -1,   40,   41,   42,   43,   44,
   45,   -1,   47,   -1,   -1,   -1,   -1,   -1,   -1,   94,
   -1,   -1,   -1,   -1,   59,   60,  123,   62,  125,   -1,
   -1,   -1,   -1,   41,   42,   43,   33,   45,   -1,   47,
   -1,   85,   86,   40,   41,   42,   43,   44,   45,   -1,
   47,   59,   60,   -1,   62,   -1,  123,   -1,  125,  259,
  260,  105,   59,   60,   -1,   62,   -1,   -1,  112,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   94,   -1,  123,   -1,
  125,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  258,   -1,   -1,  261,  262,  263,
  264,  267,  268,  267,  268,  269,  270,  271,  272,  273,
  257,  258,  267,  268,  278,  279,  123,  264,  125,   -1,
  267,  268,  269,  270,  271,  272,  273,  257,  258,   -1,
   -1,  278,  279,  265,  266,  267,  268,  267,  268,  269,
  270,  271,   -1,  273,  259,  260,   -1,   -1,  278,  279,
  257,  258,  259,  260,  261,  262,  263,  264,   -1,   -1,
   -1,   -1,  269,  270,  271,  272,  273,  258,   -1,   -1,
   -1,  278,  279,   -1,  259,  260,  267,  268,   -1,   -1,
  257,  258,   -1,   -1,  261,  262,  263,  264,  279,   -1,
  267,  268,  269,  270,  271,  272,  273,   33,   -1,  258,
   -1,  278,  279,   -1,   40,   -1,   -1,   -1,  267,  268,
   -1,   -1,  257,  258,   -1,   -1,  261,  262,  263,  264,
  279,   -1,  267,  268,  269,  270,  271,  272,  273,   -1,
   -1,   -1,   -1,  278,  279,   -1,   -1,   -1,   -1,   -1,
   -1,  259,  260,  261,  262,  263,  264,   -1,   -1,   -1,
  257,  258,   -1,   -1,  261,  262,  263,  264,   -1,   -1,
  267,  268,  269,  270,  271,  272,  273,   33,   -1,   -1,
   -1,  278,  279,   -1,   40,   41,   -1,   43,   44,   45,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  123,   -1,  125,
   33,   -1,   -1,   59,   60,   -1,   62,   40,   41,   -1,
   43,   44,   45,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   33,   -1,   -1,   59,   60,   -1,   62,
   -1,   -1,   42,   43,   -1,   45,   -1,   47,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   33,   -1,   -1,   -1,
   60,   -1,   62,   40,   41,   -1,   -1,   44,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  123,   -1,  125,
   33,   -1,   59,   60,   -1,   62,   -1,   40,   41,   -1,
   -1,   44,   -1,   -1,   94,   -1,   -1,   -1,   -1,   -1,
  123,   -1,  125,   33,   -1,   -1,   59,   60,   -1,   62,
   40,   41,   -1,   -1,   44,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  123,   -1,  125,   -1,   -1,   -1,   59,
   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,  258,   -1,   -1,   -1,  123,   -1,  125,   -1,
   -1,  267,  268,  269,  270,  271,  272,  273,   -1,   -1,
   -1,   -1,  278,  279,   -1,   -1,   -1,   -1,   -1,   -1,
  123,   -1,  125,   -1,   -1,   33,   42,   43,   -1,   45,
   -1,   47,   40,   41,   -1,   -1,   44,   -1,   -1,   -1,
   -1,   -1,   -1,  123,   60,  125,   62,   -1,   -1,   -1,
   -1,   59,   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,
   -1,  257,  258,   -1,   -1,  261,  262,  263,  264,   -1,
   -1,  267,  268,  269,  270,  271,  272,  273,   94,   -1,
   -1,   -1,  278,  279,  257,  258,   -1,   -1,  261,  262,
  263,  264,   -1,   -1,  267,  268,  269,  270,  271,  272,
  273,   -1,   -1,   -1,   -1,  278,  279,  257,  258,  259,
  260,  261,  262,  263,  264,  123,   -1,  125,   -1,  269,
  270,  271,  272,  273,   -1,   -1,   33,   -1,  278,  279,
  257,  258,   -1,   40,  261,  262,  263,  264,   45,   -1,
  267,  268,  269,  270,  271,  272,  273,   -1,   -1,   -1,
   -1,  278,  279,   -1,  257,  258,   -1,   -1,  261,  262,
  263,  264,   -1,   -1,  267,  268,  269,  270,  271,  272,
  273,   -1,   -1,   -1,   -1,  278,  279,  257,  258,   -1,
   -1,  261,  262,  263,  264,   -1,   -1,  267,  268,  269,
  270,  271,  272,  273,   33,   -1,   -1,   -1,  278,  279,
   -1,   40,   41,   -1,   -1,   44,   -1,   -1,   -1,   -1,
   -1,   33,   -1,   -1,   -1,   -1,  123,   -1,   40,   41,
   59,   -1,   44,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   59,   -1,   -1,
   33,   -1,   -1,  259,  260,  261,  262,   40,   41,  257,
  258,   44,   -1,  261,  262,  263,  264,   -1,   -1,  267,
  268,  269,  270,  271,  272,  273,   59,   33,   -1,   -1,
  278,  279,   -1,   -1,   40,   41,   -1,   -1,   44,   -1,
   -1,   -1,   -1,   -1,  123,   33,  125,   -1,   -1,   -1,
   -1,   -1,   40,   59,   -1,   -1,   -1,   45,   -1,   33,
   -1,  123,   -1,  125,   -1,   -1,   40,   -1,   -1,   -1,
   -1,   45,   -1,   33,   -1,   -1,   -1,   -1,   -1,   -1,
   40,   -1,   -1,   -1,   -1,   45,   -1,   33,   -1,   -1,
  123,   -1,  125,   -1,   40,   -1,   -1,   -1,   -1,   45,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  257,  258,   -1,   -1,   -1,   -1,   -1,  123,   -1,  125,
  267,  268,  269,  270,  271,   -1,  273,   -1,   -1,   -1,
   -1,  278,  279,   -1,   -1,  123,   -1,  125,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  123,
   -1,  125,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  123,   -1,  125,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  123,  257,  258,
   -1,   -1,   -1,   -1,  263,  264,   -1,   -1,  267,  268,
  269,  270,  271,  272,  273,  257,  258,   -1,   -1,  278,
  279,  263,  264,   -1,   -1,  267,  268,  269,  270,  271,
  272,  273,   -1,   -1,   -1,   -1,  278,  279,   -1,   -1,
   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  267,  268,  269,  270,  271,  272,
  273,   -1,   -1,   -1,   -1,  278,  279,   -1,   -1,   -1,
   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  267,  268,  269,  270,  271,  272,  273,   -1,  257,
  258,   -1,  278,  279,   -1,   -1,   -1,   -1,   -1,  267,
  268,  269,  270,  271,  258,  273,   -1,   -1,   -1,   -1,
  278,  279,   -1,  267,  268,  269,  270,  271,  258,  273,
   -1,   -1,   -1,   -1,  278,  279,   -1,  267,  268,  269,
  270,  271,  258,  273,   -1,   -1,   -1,   -1,  278,  279,
   -1,  267,  268,  269,  270,  271,   -1,  273,   -1,   -1,
   -1,   -1,  278,  279,   41,   42,   43,   -1,   45,   -1,
   47,   41,   42,   43,   -1,   45,   -1,   47,   41,   42,
   43,   -1,   45,   60,   47,   62,   -1,   -1,   42,   43,
   60,   45,   62,   47,   -1,   42,   43,   60,   45,   62,
   47,   -1,   42,   43,   -1,   45,   60,   47,   62,   -1,
   -1,   -1,   59,   60,   -1,   62,   -1,   94,   -1,   59,
   60,   -1,   62,   -1,   94,   42,   43,   -1,   45,   -1,
   47,   94,   42,   43,   -1,   45,   -1,   47,   -1,   -1,
   94,   -1,   59,   60,   -1,   62,   -1,   94,   -1,   59,
   60,   -1,   62,   -1,   94,   42,   43,   -1,   45,   -1,
   47,   42,   43,   -1,   45,   -1,   47,   -1,   -1,   -1,
   -1,   -1,   -1,   60,   -1,   62,   -1,   94,    2,   60,
   -1,   62,   -1,   -1,   94,   -1,   -1,   -1,   12,   -1,
   14,   15,   16,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   24,   25,   26,   -1,   -1,   29,   30,   94,   -1,   33,
   -1,   -1,   -1,   94,   -1,   -1,   40,   41,   42,   43,
   44,   45,   46,   47,   48,   49,   50,   51,   52,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   64,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   83,
   84,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  101,   -1,   -1,
  104,   -1,  259,  260,  261,  262,  263,  264,   -1,  259,
  260,  261,  262,  263,  264,   -1,  259,  260,  261,  262,
  263,  264,   -1,  257,   -1,  259,  260,  261,  262,  263,
  264,   -1,  259,  260,  261,  262,  263,  264,   -1,  259,
  260,  261,  262,  263,  264,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  259,  260,  261,  262,  263,  264,   -1,  259,
  260,  261,  262,  263,  264,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  259,  260,  261,  262,  263,  264,  259,  260,
  261,  262,  263,
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
null,null,null,null,null,null,null,null,null,"NL","NUM","EQUALS","DIFF","GE",
"LE","AND","OR","PLUS_EQ","TIMES_EQ","PLUS_PLUS","MINUS_MINUS","DEFINE","FOR",
"IF","ELSE","WHILE","PRINT","HELP","SHOW_ALL","SHOW","RETURN","IDENT","NEG",
};
final static String yyrule[] = {
"$accept : input",
"input :",
"input : input line",
"input : error NL",
"line : NL",
"line : exp NL",
"line : cmd NL",
"ParamFormais : ListaParams",
"ParamFormais :",
"ListaParams : ListaParams ',' IDENT",
"ListaParams : IDENT",
"cmd : IDENT '=' exp ';'",
"cmd : IDENT PLUS_EQ exp ';'",
"cmd : IDENT TIMES_EQ exp ';'",
"cmd : IF '(' exp ')' cmd",
"cmd : IF '(' exp ')' cmd ELSE cmd",
"cmd : WHILE '(' exp ')' cmd",
"cmd : FOR '(' Exp ';' Exp ';' Exp ')' cmd",
"$$1 :",
"cmd : DEFINE IDENT '(' $$1 ParamFormais ')' '{' lcmd '}'",
"cmd : RETURN exp ';'",
"cmd : '{' lcmd '}'",
"cmd : exp",
"Exp : exp",
"Exp : IDENT '=' exp",
"Exp :",
"lcmd : lcmd cmd",
"lcmd :",
"exp : NUM",
"exp : IDENT",
"exp : exp '+' exp",
"exp : exp '-' exp",
"exp : exp '*' exp",
"exp : exp '/' exp",
"exp : exp '<' exp",
"exp : exp '>' exp",
"exp : exp GE exp",
"exp : exp LE exp",
"exp : exp EQUALS exp",
"exp : exp DIFF exp",
"exp : exp AND exp",
"exp : exp OR exp",
"exp : '!' exp",
"exp : '-' exp",
"exp : exp '^' exp",
"exp : IDENT PLUS_PLUS",
"exp : IDENT MINUS_MINUS",
"exp : PLUS_PLUS IDENT",
"exp : MINUS_MINUS IDENT",
"$$2 :",
"exp : IDENT '(' $$2 ParamsReais ')'",
"exp : '(' exp ')'",
"ParamsReais : ListaExpr",
"ParamsReais :",
"ListaExpr : ListaExpr ',' exp",
"ListaExpr : exp",
};

//#line 143 "calc.y"


  public static HashMap<String, ResultValue> memory = new HashMap<>();
  public static HashMap<String, NodoFuncDecl> funcs = new HashMap<>();
  private Yylex lexer;
  private List<String> currParams;
  private List<INodo> realParams;
  private Stack<List<INodo>> recurFunc;


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
//#line 630 "Parser.java"
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
//#line 36 "calc.y"
{yyval.obj=null;}
break;
case 2:
//#line 37 "calc.y"
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
//#line 46 "calc.y"
{ System.out.println("entrada ignorada"); }
break;
case 4:
//#line 49 "calc.y"
{ if (interactive) System.out.print("\n> "); yyval.obj = null; }
break;
case 5:
//#line 50 "calc.y"
{ yyval.obj = val_peek(1).obj;
		   System.out.println("\n= " + val_peek(1).obj); 
                   if (interactive) System.out.print("\n>: "); }
break;
case 9:
//#line 60 "calc.y"
{ currParams.add(val_peek(0).sval); }
break;
case 10:
//#line 61 "calc.y"
{ currParams.add(val_peek(0).sval); }
break;
case 11:
//#line 64 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.ATRIB, val_peek(3).sval, (INodo)val_peek(1).obj); }
break;
case 12:
//#line 65 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.PLUS_EQ, val_peek(3).sval, (INodo)val_peek(1).obj); }
break;
case 13:
//#line 66 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.TIMES_EQ, val_peek(3).sval, (INodo)val_peek(1).obj); }
break;
case 14:
//#line 67 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.IF,(INodo)val_peek(2).obj, (INodo)val_peek(0).obj, null); }
break;
case 15:
//#line 68 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.IFELSE,(INodo)val_peek(4).obj, (INodo)val_peek(2).obj, (INodo)val_peek(0).obj); }
break;
case 16:
//#line 69 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.WHILE,(INodo)val_peek(2).obj, (INodo)val_peek(0).obj, null); }
break;
case 17:
//#line 70 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.FOR, (INodo)val_peek(6).obj, (INodo)val_peek(4).obj, (INodo)val_peek(2).obj, (INodo)val_peek(0).obj); }
break;
case 18:
//#line 71 "calc.y"
{ currParams = new ArrayList<>(); }
break;
case 19:
//#line 71 "calc.y"
{ yyval.obj = new NodoFuncDecl(val_peek(7).sval, currParams, (INodo)val_peek(1).obj); currParams = null;}
break;
case 20:
//#line 72 "calc.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 21:
//#line 73 "calc.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 22:
//#line 74 "calc.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 23:
//#line 77 "calc.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 24:
//#line 78 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.ATRIB, val_peek(2).sval, (INodo)val_peek(0).obj); }
break;
case 25:
//#line 79 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.NULL, null, null, null); }
break;
case 26:
//#line 82 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.SEQ,(INodo)val_peek(1).obj,(INodo)val_peek(0).obj); }
break;
case 27:
//#line 83 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.NULL, null, null, null); }
break;
case 28:
//#line 86 "calc.y"
{ yyval.obj = new NodoTDouble(val_peek(0).dval); }
break;
case 29:
//#line 87 "calc.y"
{ yyval.obj = new NodoID(val_peek(0).sval); }
break;
case 30:
//#line 88 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.ADD,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 31:
//#line 89 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.SUB,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 32:
//#line 90 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.MULL,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 33:
//#line 91 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.DIV,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 34:
//#line 92 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.LESS,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 35:
//#line 93 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.GREATER,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 36:
//#line 94 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.GE,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 37:
//#line 95 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.LE,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 38:
//#line 96 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.EQUALS,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 39:
//#line 97 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.DIFF,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 40:
//#line 98 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.AND,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 41:
//#line 99 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.OR,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 42:
//#line 100 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.NOT,(INodo)val_peek(0).obj,null); }
break;
case 43:
//#line 101 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.UMINUS,(INodo)val_peek(0).obj,null); }
break;
case 44:
//#line 102 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.POW,(INodo)val_peek(2).obj,(INodo)val_peek(0).obj); }
break;
case 45:
//#line 103 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.PP_A,val_peek(1).sval,null); }
break;
case 46:
//#line 104 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.MM_A,val_peek(1).sval,null); }
break;
case 47:
//#line 105 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.PP_B,val_peek(0).sval,null); }
break;
case 48:
//#line 106 "calc.y"
{ yyval.obj = new NodoNT(TipoOperacao.MM_B,val_peek(0).sval,null); }
break;
case 49:
//#line 107 "calc.y"
{ if (recurFunc == null) 
                    recurFunc = new Stack<>();
                  if (realParams != null) 
                    recurFunc.push(realParams);
                  realParams = new ArrayList<>();
                }
break;
case 50:
//#line 112 "calc.y"
{ 
                                    if (!funcs.containsKey(val_peek(4).sval)) {
                                      yyerror("Função " + val_peek(4).sval + " não definida.");
                                      yyval.obj = null;
                                    } else {
                                      NodoFuncDecl func = funcs.get(val_peek(4).sval);
                                      if (func.getParams().size() != realParams.size()) {
                                        yyerror("Número de parâmetros diferente");
                                        yyval.obj = null;
                                      } else {
                                        if (realParams.contains(null)) {
                                          yyval.obj = null;
                                        } else {
                                          yyval.obj = new NodoFuncRun(val_peek(4).sval, realParams);
                                        }
                                      }
                                    }
                                    if (recurFunc.size() > 0) 
                                        realParams = recurFunc.pop();
                                  }
break;
case 51:
//#line 132 "calc.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 54:
//#line 139 "calc.y"
{ realParams.add((INodo) val_peek(0).obj); }
break;
case 55:
//#line 140 "calc.y"
{ realParams.add((INodo) val_peek(0).obj); }
break;
//#line 1013 "Parser.java"
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
