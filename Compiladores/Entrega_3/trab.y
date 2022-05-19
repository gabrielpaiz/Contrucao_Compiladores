%{
  import java.io.*;
%}
   

%token INT, DOUBLE, BOOLEAN, VOID, IDENT, NUM, FUNCT, IF, ELSE, WHILE, AND, OR

%right '='
%left '<' '>' AND OR
%left '+' '-'
%left '*' '/'


%%
 
Prog : Decl ListaFuncoes
    ;

Decl : Tipo LId ';' Decl
    |     //vazio
    ;

Tipo : INT
     | DOUBLE
     | BOOLEAN
     ;

LId : LId ',' IDENT
    | IDENT
    ;

ListaFuncoes: umaFuncao restoListFuncoes
            ;

restoListFuncoes: ListaFuncoes
                |        //vazio
                ;

umaFuncao : FUNCT tipoOuVoid IDENT restoUmaFuncao
          ;

restoUmaFuncao: '('')' Bloco
              | '(' ListaParametros ')' Bloco
              ;

tipoOuVoid: VOID
          | Tipo
          ;

ListaParametros: Tipo restoListaParametros
               ;
restoListaParametros: IDENT
                    | IDENT "," ListaParametros
                    ;

Bloco : '{' LCmd '}'
      ;

LCmd : Cmd LCmd
     |       // vazio
     ;

Cmd : Bloco
    | IF '(' E ')' Cmd restoIF
    | WHILE '(' E ')' Cmd
    | E ';'
    ;

restoIF: ELSE Cmd
       | //vazio
       ;

E : F EA
  ;

EA: OP E
  |  //vazio
  ;

F: NUM
  | IDENT restoIDENT
  | '(' E ')'
  ;

OP: '='
  | '+'
  | '-'
  | '*'
  | '/'
  | '<'
  | '>'
  | AND
  | OR
  ;

restoIDENT: '(' listaExp ')'
          | '(' ')'
          |   //vazio
          ;
        
listaExp: E restoListaEXP
        ;

restoListaEXP: ',' listaExp
             |      //vazio
             ;


%%

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

