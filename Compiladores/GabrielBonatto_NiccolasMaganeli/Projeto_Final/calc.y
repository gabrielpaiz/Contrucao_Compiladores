
/*
 * Integrantes:
 *  - Gabriel Bonatto Justo 17104118-9 gabriel.justo@acad.pucrs.br
 *  - Niccolas Guntzel Maganeli 16201269-4 niccolas.maganeli@edu.pucrs.br
 */

%{
  import java.io.*;
  import java.util.HashMap;
  import java.util.ArrayList;
  import java.util.List;
  import java.util.Stack;
%}
      
%token NL          /* newline  */
%token <dval> NUM  /* a number */
%token EQUALS, DIFF, GE, LE, AND, OR, PLUS_EQ, TIMES_EQ, PLUS_PLUS, MINUS_MINUS, DEFINE, FOR, IF, ELSE, WHILE, PRINT, HELP, SHOW_ALL, SHOW, RETURN
%token <sval> IDENT

%type <obj> exp, cmd, line, input, lcmd, Exp

%left OR
%left AND
%nonassoc '!'
%left '<' '>' GE LE
%right '=' PLUS_EQ TIMES_EQ
%left '-' '+'
%left '*' '/'
%right '^'
%nonassoc NEG          
%nonassoc PLUS_PLUS MINUS_MINUS
      
%%

input:   /* empty string */ {$$=null;}
       | input line  { if ($2 != null) {
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

ParamFormais: ListaParams
            |
            ;

ListaParams: ListaParams ',' IDENT { currParams.add($3); }
            | IDENT                { currParams.add($1); }
            ;

cmd : IDENT '=' exp ';'                              { $$ = new NodoNT(TipoOperacao.ATRIB, $1, (INodo)$3); }
    | IDENT PLUS_EQ exp ';'                          { $$ = new NodoNT(TipoOperacao.PLUS_EQ, $1, (INodo)$3); }
    | IDENT TIMES_EQ exp ';'                         { $$ = new NodoNT(TipoOperacao.TIMES_EQ, $1, (INodo)$3); }
    | IF '(' exp ')' cmd                             { $$ = new NodoNT(TipoOperacao.IF,(INodo)$3, (INodo)$5, null); }
    | IF '(' exp ')' cmd ELSE cmd                    { $$ = new NodoNT(TipoOperacao.IFELSE,(INodo)$3, (INodo)$5, (INodo)$7); }
    | WHILE '(' exp ')' cmd                          { $$ = new NodoNT(TipoOperacao.WHILE,(INodo)$3, (INodo)$5, null); }
    | FOR '(' Exp ';' Exp ';' Exp ')' cmd            { $$ = new NodoNT(TipoOperacao.FOR, (INodo)$3, (INodo)$5, (INodo)$7, (INodo)$9); }
    | DEFINE IDENT '(' { currParams = new ArrayList<>(); } ParamFormais ')' '{' lcmd '}' { $$ = new NodoFuncDecl($2, currParams, (INodo)$8); currParams = null;}
    | RETURN exp ';'                                 { $$ = $2; }
    | '{' lcmd '}'                                   { $$ = $2; }
    | exp                                            { $$ = $1; }
    ;

Exp : exp           { $$ = $1; }
    | IDENT '=' exp { $$ = new NodoNT(TipoOperacao.ATRIB, $1, (INodo)$3); }
    |               { $$ = new NodoNT(TipoOperacao.NULL, null, null, null); }
    ;

lcmd : lcmd cmd { $$ = new NodoNT(TipoOperacao.SEQ,(INodo)$1,(INodo)$2); }
     |          { $$ = new NodoNT(TipoOperacao.NULL, null, null, null); }               
     ;

exp : NUM                { $$ = new NodoTDouble($1); }
    | IDENT              { $$ = new NodoID($1); }
    | exp '+' exp        { $$ = new NodoNT(TipoOperacao.ADD,(INodo)$1,(INodo)$3); }
    | exp '-' exp        { $$ = new NodoNT(TipoOperacao.SUB,(INodo)$1,(INodo)$3); }
    | exp '*' exp        { $$ = new NodoNT(TipoOperacao.MULL,(INodo)$1,(INodo)$3); }
    | exp '/' exp        { $$ = new NodoNT(TipoOperacao.DIV,(INodo)$1,(INodo)$3); }
    | exp '<' exp        { $$ = new NodoNT(TipoOperacao.LESS,(INodo)$1,(INodo)$3); }
    | exp '>' exp               { $$ = new NodoNT(TipoOperacao.GREATER,(INodo)$1,(INodo)$3); }
    | exp GE exp                { $$ = new NodoNT(TipoOperacao.GE,(INodo)$1,(INodo)$3); }
    | exp LE exp                { $$ = new NodoNT(TipoOperacao.LE,(INodo)$1,(INodo)$3); }
    | exp EQUALS exp            { $$ = new NodoNT(TipoOperacao.EQUALS,(INodo)$1,(INodo)$3); }
    | exp DIFF exp              { $$ = new NodoNT(TipoOperacao.DIFF,(INodo)$1,(INodo)$3); }
    | exp AND exp               { $$ = new NodoNT(TipoOperacao.AND,(INodo)$1,(INodo)$3); }
    | exp OR exp                { $$ = new NodoNT(TipoOperacao.OR,(INodo)$1,(INodo)$3); }
    | '!' exp                   { $$ = new NodoNT(TipoOperacao.NOT,(INodo)$2,null); }
    | '-' exp  %prec NEG        { $$ = new NodoNT(TipoOperacao.UMINUS,(INodo)$2,null); }
    | exp '^' exp               { $$ = new NodoNT(TipoOperacao.POW,(INodo)$1,(INodo)$3); }
    | IDENT PLUS_PLUS           { $$ = new NodoNT(TipoOperacao.PP_A,$1,null); }
    | IDENT MINUS_MINUS         { $$ = new NodoNT(TipoOperacao.MM_A,$1,null); }
    | PLUS_PLUS IDENT           { $$ = new NodoNT(TipoOperacao.PP_B,$2,null); }
    | MINUS_MINUS IDENT         { $$ = new NodoNT(TipoOperacao.MM_B,$2,null); }
    | IDENT '(' { if (recurFunc == null) 
                    recurFunc = new Stack<>();
                  if (realParams != null) 
                    recurFunc.push(realParams);
                  realParams = new ArrayList<>();
                } ParamsReais ')' { 
                                    if (!funcs.containsKey($1)) {
                                      yyerror("Função " + $1 + " não definida.");
                                      $$ = null;
                                    } else {
                                      NodoFuncDecl func = funcs.get($1);
                                      if (func.getParams().size() != realParams.size()) {
                                        yyerror("Número de parâmetros diferente");
                                        $$ = null;
                                      } else {
                                        if (realParams.contains(null)) {
                                          $$ = null;
                                        } else {
                                          $$ = new NodoFuncRun($1, realParams);
                                        }
                                      }
                                    }
                                    if (recurFunc.size() > 0) 
                                        realParams = recurFunc.pop();
                                  }
    | '(' exp ')'               { $$ = $2; }
    ;

ParamsReais : ListaExpr
            |
            ;

ListaExpr : ListaExpr ',' exp { realParams.add((INodo) $3); }
          | exp               { realParams.add((INodo) $1); }
          ;

%%

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
