
%{
  import java.io.*;
  import java.util.HashMap;
  import java.util.ArrayList;
  import java.util.Stack;
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
       | input line  {     avalia = ((INodo) $2).avalia();
                           if (avalia != null) {
                           if(interactive)
                              System.out.print("Avaliacao: " + avalia +"\n> "); 
							             $$=$2;
						            }
					              else {
                          if(interactive)
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


params: lident
      |
      ;

lident : IDENT  {currParam.add($1);}
       | lident ',' IDENT {currParam.add($3);}
       ;

cmd :  exp ';' {$$ = $1;}
    |  RETURN exp ';' {$$ = $2;}
    |  DEFINE IDENT {currParam = new ArrayList<>();} '('params')' '{' lcmd '}' {$$ = new NodoFunc(currParam, $2, (INodo)$8);}         
    |  FOR '(' exp ';' exp ';' exp ')' cmd {$$ = new NodoNT(TipoOperacao.FOR, (INodo)$3, (INodo)$5,(INodo)$7,(INodo)$9);}
    |  IF '(' exp ')' cmd           { $$ = new NodoNT(TipoOperacao.IF,(INodo)$3, (INodo)$5, null); }
    |  IF '(' exp ')' cmd ELSE cmd  { $$ = new NodoNT(TipoOperacao.IFELSE,(INodo)$3, (INodo)$5, (INodo)$7); }
    |  WHILE '(' exp ')' cmd        { $$ = new NodoNT(TipoOperacao.WHILE,(INodo)$3, (INodo)$5, null); }
    |  PRINT '(' exp ')' ';'  { $$ = new NodoNT(TipoOperacao.PRINT,(INodo)$3, null);}
    |  '{' lcmd '}'    { $$ = $2; }
    | HELP {help(); $$ = new NodoNT(TipoOperacao.NULL, null, null, null);}
    | SHOW IDENT {show($2); $$ = new NodoNT(TipoOperacao.NULL, null, null, null);}
    | SHOW_ALL {show_all(); $$ = new NodoNT(TipoOperacao.NULL, null, null, null);}
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
       | IDENT {actualParam = new ArrayList<>(); erro = false;} 
              '(' valParams ')'{if(!funcs.containsKey($1))
                                  yyerror("função "+$1+"() não existe");
                                else{
                                  currParam = funcs.get($1).getParams(); 
                                  if(actualParam.size() < currParam.size())
                                      yyerror("Parametros faltando na função "+$1+"()");
                                  else if(actualParam.size() > currParam.size())
                                      yyerror("Parametros a mais na função "+$1+"()");
                                  else{
                                      ResultValue result = funcs.get($1).executa(actualParam);
                                      if(result.getType() == TypeEnum.DOUBLE)
                                        $$ = new NodoTDouble(result.getDouble());
                                      else if(result.getType() == TypeEnum.BOOLEAN)
                                        $$ = new NodoTBool(result.getBool());
                                      else{
                                        yyerror("Resultado invalido da função "+$1+"()");
                                        $$ = new NodoNT(TipoOperacao.NULL, null, null, null);;
                                        }
                                  }
                                }
                                erro = true;
                              }
       ;

valParams: lparam
      |
      ;

lparam : exp  {actualParam.add( ((INodo)$1).avalia() );}
      | lparam ',' exp {actualParam.add( ((INodo)$3).avalia() );}
      ;


%%

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
        scopo.peek().forEach((key, value) -> {
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
