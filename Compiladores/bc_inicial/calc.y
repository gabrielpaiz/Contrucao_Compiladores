
%{
  import java.io.*;
  import java.util.HashMap;
%}
      
%token NL          /* newline  */
%token <dval> NUM  /* a number */
%token IF, WHILE, ELSE, PRINT, FOR, DEFINE, RETURN, PRINT
%token AND, OR
%token MAIOR_IGUAL, MENOR_IGUAL, DIFF, IGUAL
%token SOMA_ATR, MULT_ATR
%token HELP, SHOW, SHOW_ALL
%token <bval> BOOL
%token <sval> IDENT

%type <obj> exp, cmd, line, input, lcmd, restFunc, lident 

%left AND OR
%nonassoc '<' '>' MAIOR_IGUAL MENOR_IGUAL DIFF IGUAL
%right '!'
%left '-' '+'
%left '*', '/'
%left NEG         /* negation--unary minus */
%right '^'         /* exponentiation        */
      
%%

input:   /* empty string */ {$$=null;}
       | input line  { if (((INodo) $2).avalia() != null) {
                           System.out.print("Avaliacao: " + ((INodo) $2).avalia() +"\n> "); 
							             $$=$2;
						}
					    else {
                          System.out.print("\n> "); 
						  $$=null;
						}
					}
       | error NL { System.out.println("entrada ignorada"); }
       ;
      
line:    NL      { if (interactive) System.out.print("\n> "); $$ = null; }
       | exp NL  { $$ = $1;
		   System.out.println("\n= " + $1); 
                   if (interactive) System.out.print("\n>: "); }
       | cmd NL
       ;


restFunc: '('')' '{' lcmd '}' 
        | '(' lident ')' '{' lcmd '}' 
        ;

lident : IDENT
       | IDENT ',' lident
       ;

cmd :  exp ';'
    |  RETURN exp ';'
    |  FOR '(' IDENT '=' exp ';' exp ';' exp ')' cmd
    |  DEFINE IDENT restFunc        
    |  IF '(' exp ')' cmd           { $$ = new NodoNT(TipoOperacao.IF,(INodo)$3, (INodo)$5, null); }
    |  IF '(' exp ')' cmd ELSE cmd  { $$ = new NodoNT(TipoOperacao.IFELSE,(INodo)$3, (INodo)$5, (INodo)$7); }
    |  WHILE '(' exp ')' cmd        { $$ = new NodoNT(TipoOperacao.WHILE,(INodo)$3, (INodo)$5, null); }
    |  '{' lcmd '}'    { $$ = $2; }
    ;
      
lcmd : lcmd cmd                 { $$ = new NodoNT(TipoOperacao.SEQ,(INodo)$1,(INodo)$2); }
     |                          { $$ = new NodoNT(TipoOperacao.NULL, null, null, null); }               
     ;


exp:     NUM                { $$ = new NodoTDouble($1); }
       | IDENT              { $$ = new NodoID($1); }
       | IDENT '=' exp      { $$ = new NodoNT(TipoOperacao.ATRIB, $1, (INodo)$3); }
       | IDENT SOMA_ATR exp { $$ = new NodoNT(TipoOperacao.SOMA_ATRIB, $1, (INodo)$3); }
       | IDENT MULT_ATR exp { $$ = new NodoNT(TipoOperacao.MULT_ATRIB, $1, (INodo)$3); }            
       | exp '+' exp        { $$ = new NodoNT(TipoOperacao.ADD,(INodo)$1,(INodo)$3); }
       | exp '-' exp        { $$ = new NodoNT(TipoOperacao.SUB,(INodo)$1,(INodo)$3); }
       | exp '*' exp        { $$ = new NodoNT(TipoOperacao.MULL,(INodo)$1,(INodo)$3); }
       | exp '/' exp        { $$ = new NodoNT(TipoOperacao.DIV,(INodo)$1,(INodo)$3); }
       | exp '<' exp        { $$ = new NodoNT(TipoOperacao.LESS,(INodo)$1,(INodo)$3); }
       | '-' exp  %prec NEG { $$ = new NodoNT(TipoOperacao.UMINUS,(INodo)$2,null); }
       | exp '^' exp        { $$ = new NodoNT(TipoOperacao.POW,(INodo)$1,(INodo)$3); }
       | '(' exp ')'        { $$ = $2; }
       | exp '>' exp        { $$ = new NodoNT(TipoOperacao.GREAT,(INodo)$1,(INodo)$3); }
       | exp MAIOR_IGUAL exp{ $$ = new NodoNT(TipoOperacao.GREAT_EQ,(INodo)$1,(INodo)$3); }
       | exp MENOR_IGUAL exp{ $$ = new NodoNT(TipoOperacao.LESS_EQ,(INodo)$1,(INodo)$3); }
       | exp DIFF exp       { $$ = new NodoNT(TipoOperacao.DIFF,(INodo)$1,(INodo)$3); }
       | exp IGUAL exp      { $$ = new NodoNT(TipoOperacao.EQUAL,(INodo)$1,(INodo)$3); }
       | exp AND exp        { $$ = new NodoNT(TipoOperacao.AND,(INodo)$1,(INodo)$3); }
       | exp OR exp         { $$ = new NodoNT(TipoOperacao.OR,(INodo)$1,(INodo)$3); }
       | '!' exp            { $$ = new NodoNT(TipoOperacao.NEG,(INodo)$2,null); }
       | PRINT '(' exp ')'  { $$ = new NodoNT(TipoOperacao.PRINT,(INodo)$3, null);}
       ;

%%

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
    System.err.println ("Error: "+ error+" in text '"+lexer.GetText()+"' in line "+lexer.GetLine());
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
