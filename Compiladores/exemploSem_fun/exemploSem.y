    
%{
  import java.io.*;
  import java.util.ArrayList;

%}


%token IDENT, INT, DOUBLE, BOOL, NUM, STRING
%token LITERAL, AND, VOID, IF
%token STRUCT, FUNCT, VOID, RETURN

%right '='
%nonassoc '>'
%left '+'
%left AND
%left '[' 
%left '.'  

%type <sval> IDENT
%type <ival> NUM
%type <obj> type
%type <obj> exp
%type <obj> lvalue
%type <obj> tipoOuVoid

%%

prog : { currClass = ClasseID.VarGlobal; } dList ListaFuncoes ;


ListaFuncoes :  ListaFuncoes umaFuncao
             |  umaFuncao
             ;                      

umaFuncao : FUNCT tipoOuVoid IDENT   { currEscopo = $3;
                                      TS_entry nodo = ts.pesquisa($3);
                                      if (nodo != null) 
                                        yyerror("(sem) nome >" + $3 + "< jah declarada");
                                      else{
                                        ts.insert(new TS_entry($3, (TS_entry)$2, ClasseID.NomeFuncao)); 
                                        currClass = ClasseID.VarLocal;
                                        }
                                      }
                '(' ParamFormais  ')'   dList bloco


ParamFormais :  ListaParametros 
            |
            ;

tipoOuVoid : VOID  { $$ = Tp_VOID; }
           | type  { $$ = $1; }
           ;
  
ListaParametros : ListaParametros ',' newIdentListaParametros
                | newIdentListaParametros 
                ;

newIdentListaParametros: type IDENT {TS_entry nodo = ts.pesquisa(currEscopo);
                                    if(nodo == null)
                                      yyerror("É impossivle dar esse erro com a "+currEscopo);
                                    else{
                                      TS_entry new_var = new TS_entry($2, Tp_INT, ClasseID.NomeParam);
                                      nodo.insert_param(new_var);
                                      ts.insert(new_var);
                                      }
                                    }
                       ;

dList :  dList decl 
      | ;

decl : type { currType = (TS_entry)$1; } LId ';' 
      ;

LId :  LId ',' nomeId 
       | nomeId
       ;
       
nomeId : IDENT         {  TS_entry nodo = ts.pesquisa($1);
                          TS_entry scopo = ts.pesquisa(currEscopo);
                          if (currClass == ClasseID.VarLocal){
                              nodo = scopo.getLocais().pesquisa($1);
                          }
                          if (nodo != null) 
                              yyerror("(sem) variavel >" + $1 + "< jah declarada");
                          else {
                            ts.insert(new TS_entry($1, currType, currClass)); 
                            if (currClass == ClasseID.VarLocal)
                              scopo.insert_local(new TS_entry($1, currType, currClass)); 
                          }
                        }
        ;

type : INT    { $$ = Tp_INT; }
     | DOUBLE { $$ = Tp_DOUBLE; }
     | BOOL   { $$ = Tp_BOOL; }
     ;

//
//    não faremos tipo struct...
//
//     | IDENT  { TS_entry nodo = ts.pesquisa($1);
//                if (nodo == null ) 
//                   yyerror("(sem) Nome de tipo <" + $1 + "> nao declarado ");
//                else 
//                    $$ = nodo;
//               } 
//     ;


bloco : '{' listacmd '}'{ ArrayList<TS_entry> l = ts.pesquisa(currEscopo).getParams().getLista();
                          for(TS_entry nodo: l)
                            ts.remove(nodo);
                        }
      ;

listacmd : listacmd cmd
        |
         ;

cmd :  exp ';' 
      | IF '(' exp ')' cmd   {  if ( ((TS_entry)$3) != Tp_BOOL) 
                                     yyerror("(sem) expressão (if) deve ser lógica "+((TS_entry)$3).getTipo());
                             }  
      | RETURN exp ';'                          
       ;


exp : exp '+' exp { $$ = validaTipo('+', (TS_entry)$1, (TS_entry)$3); }
    | exp '>' exp { $$ = validaTipo('>', (TS_entry)$1, (TS_entry)$3); }
    | exp AND exp { $$ = validaTipo(AND, (TS_entry)$1, (TS_entry)$3); } 
    | NUM         { $$ = Tp_INT; }      
    | '(' exp ')' { $$ = $2; }
    | lvalue   { $$ = $1; }                   
    | lvalue '=' exp  {  $$ = validaTipo(ATRIB, (TS_entry)$1, (TS_entry)$3);  }
    | IDENT {currFunc = ts.pesquisa($1); 
            if (currFunc == null)
              yyerror("Função <"+$1+"> não declarada");
            else
             listSize = currFunc.getParams().getLista().size();
             count_param = 0;
             }
     '(' ParamReais ')' { if(currFunc == null)
                            $$ = Tp_ERRO;
                          else if(count_param < listSize){
                            yyerror("Parametros faltando na função "+currFunc.getId()+"()");
                            $$ = Tp_ERRO;
                          }else if(count_param > listSize){
                            yyerror("Parametros a mais na função "+currFunc.getId()+"()");
                            $$ = Tp_ERRO;
                          }else
                            $$ = currFunc.getTipo();
                        }
    ;



lvalue :  IDENT   { TS_entry nodo = ts.pesquisa($1);
                    if (nodo == null) {
                       yyerror("(sem) var <" + $1 + "> nao declarada"); 
                       $$ = Tp_ERRO;    
                       }           
                    else{
                        nodo = ts.getNomeParam(nodo);
                        $$ = nodo.getTipo();
                    }
                  } 
       | IDENT '[' exp ']'  { $$ = Tp_ERRO; }
       | IDENT '.' exp      { $$ = Tp_ERRO; }


ParamReais : ListaExpr
           | 
           ;

ListaExpr : ListaExpr ',' exp { if(count_param >= listSize || currFunc == null)
                                  count_param++;
                                else{
                                  TS_entry nodo = currFunc.getParams().getLista().get(count_param);
                                  if(!compareTypes((TS_entry)$3, nodo.getTipo())){
                                      yyerror("Funcao " + currFunc.getId() + "() tipo dos parametros errado, recebeu:"+((TS_entry)$3).getTipoStr()+" esperava:"+nodo.getTipo().getTipoStr());
                                  }
                                }
                                count_param++;
                              }  
          | exp { if(count_param >= listSize || currFunc == null)
                    count_param++;
                  else{
                    TS_entry nodo = currFunc.getParams().getLista().get(count_param);
                    if(!compareTypes((TS_entry)$1, nodo.getTipo())){
                      yyerror("Funcao " + currFunc.getId() + "() tipos errados, recebeu:"+((TS_entry)$1).getTipoStr()+" esperava:"+nodo.getTipo().getTipoStr());
                    }else
                    count_param++;
                  }
                }
  
          ;


%%

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

