import java.io.*;

public class AsdrSample {

   private static final int BASE_TOKEN_NUM = 301;

   public static final int IDENT = 301;
   public static final int NUM = 302;
   public static final int WHILE = 303;
   public static final int IF = 304;
   public static final int FI = 305;
   public static final int ELSE = 306;
   public static final int INT = 307;
   public static final int DOUBLE = 308;
   public static final int AND = 309;
   public static final int OR = 310;
   public static final int EQUALS = 311;




   public static final String tokenList[] = { "IDENT",
         "NUM",
         "WHILE",
         "IF",
         "FI",
         "ELSE",
         "INT",
         "DOUBLE",
         "AND",
         "OR", 
         "EQUALS" };

   /* referencia ao objeto Scanner gerado pelo JFLEX */
   private Yylex lexer;

   public ParserVal yylval;

   private static int laToken;
   private boolean debug;

   /* construtor da classe */
   public AsdrSample(Reader r) {
      lexer = new Yylex(r, this);
   }

   /*****
    * Gramática original
    * Prog --> LDecl Bloco
    * 
    * LDecl --> Decl LDecl
    * | // vazia
    * 
    * Decl --> Tipo LIdent ;
    *          

    * LIdent --> ident LidentAux
    * 
    * LIdentAux --> , ident LIdentAux
    * //vazia
    * 
    * Tipo --> int | double

    * Bloco --> { Lcmd }
    * 
    
    LCmd --> Cmd LCmd
          |    //vazio   





    * Cmd --> Bloco
    * | while ( E ) Cmd
    * | ident = E ;
    * | if ( E ) Cmd fi
    * | if ( E ) Cmd else Cmd fi
    * 
    * E --> IDENT
    * | NUM
    * | ( E )
    ***/

   /*****
    * Gramática 'fatorada'
    * Prog --> Bloco
    * 
    * Bloco --> { Cmd }
    * 
    * Cmd --> Bloco
    * | while ( E ) Cmd
    * | ident = E ;
    * | if ( E ) Cmd RestoIf // 'fatorada à esquerda'
    * 
    * RestoIf --> else Cmd | vazio 
    * 
   
    
    E --> E > E | E < E | E == E    //   |   não associativo
      | E+E | E-E | E OR E          //   |   assoc a esquerda
      | E*E | E/E | E AND E         //   \/  maior precedencia, assoc a esqu
      | ident | num | (E)           // casos base 

    E -> indetX | numX| (E)X
    X -> <E| >E | ==E| -E | +E
         |OR E | *E | /E |AND E 
         | vazio
    
    ***/


   private void lcmd()
   {
      if (laToken == '{'
      || laToken == WHILE
      || laToken == IDENT 
      || laToken == IF) {
         Cmd();
         lcmd();
      }

   }

   private void ldecl()
   {
      if (laToken == INT || laToken == DOUBLE) {
         decl();
         ldecl();
      }
   }

   private void decl()
   {
      if (laToken == INT || laToken == DOUBLE) {
         tipo();
         lident();
         verifica(';');
      }else {
         yyerror("esperado 'um tipo de variável'");
      }
   } 
   private void lidentaux() {
      if (laToken == ',') {
         if (debug)
            System.out.println("LIdent --> ");

         verifica(',');
         verifica(IDENT);
         lidentaux();

      }

   }

   private void lident() {
      if (laToken == IDENT) {
         verifica(laToken);
         lidentaux();
      } else
         yyerror("esperado 'um tipo de variável'");

   }

   private void tipo() {
      if (laToken == INT) {
         if (debug)
            System.out.println("Tipo --> int");
         verifica(laToken);
      } else if (laToken == DOUBLE) {
         if (debug)
            System.out.println("Tipo --> double");
         verifica(laToken);
      } else {
         yyerror("esperado 'um tipo de variável'");
      }
   }

   private void Prog() {
      if (laToken == INT || laToken == DOUBLE ) {
         if (debug)
            System.out.println("Prog --> Bloco");
         ldecl();
         Bloco();
      } else
         yyerror("esperado Uma lista de declarações");
   }

   private void Bloco() {
      if (debug)
         System.out.println("Bloco --> { Cmd }");

      verifica('{');
      lcmd();
      verifica('}');
   }

   private void Cmd() {
      if (laToken == '{') {
         if (debug)
            System.out.println("Cmd --> Bloco");
         Bloco();
      } else if (laToken == WHILE) {
         if (debug)
            System.out.println("Cmd --> WHILE ( E ) Cmd");
         verifica(WHILE); // laToken = this.yylex();
         verifica('(');
         E();
         verifica(')');
         Cmd();
      } else if (laToken == IDENT) {
         if (debug)
            System.out.println("Cmd --> IDENT = E ;");
         verifica(IDENT);
         verifica('=');
         E();
         verifica(';');
      } else if (laToken == IF) {
         if (debug)
            System.out.println("Cmd --> if (E) Cmd RestoIF");
         verifica(IF);
         verifica('(');
         E();
         verifica(')');
         Cmd();
         RestoIF();
      } else
         yyerror("Esperado {, if, while ou identificador");
   }

   private void RestoIF() {
      if (laToken == ELSE) {
         if (debug)
            System.out.println("RestoIF --> else Cmd FI ");
         verifica(ELSE);
         Cmd();
      }
   }
   
//    E --> E > E | E < E | E == E    //   |   não associativo
//    | E+E | E-E | E OR E          //   |   assoc a esquerda
//    | E*E | E/E | E AND E         //   \/  maior precedencia, assoc a esqu
//    | ident | num | (E)           // casos base 

//  E -> indetX | numX| (E)X
//  X -> <E| >E | ==E| -E | +E
//       |OR E | *E | /E |AND E 
//       | vazio
   private void E() {
      if (laToken == IDENT) {
         if (debug)
            System.out.println("E --> IDENT");
         verifica(IDENT);
         X();
      } else if (laToken == NUM) {
         if (debug)
            System.out.println("E --> NUM");
         verifica(NUM);
         X();
      } else if (laToken == '(') {
         if (debug)
            System.out.println("E --> ( E )");
         verifica('(');
         E();
         verifica(')');
         X();
      } else
         yyerror("Esperado operando (, identificador ou numero");
   }

   private void X() {
      if (laToken == '<'){
         verifica('<');
         E();
      }else if (laToken == '>'){
         verifica('>');
         E();
      }else if (laToken == EQUALS){
         verifica(EQUALS);
         E();
      }else if (laToken == '-'){
         verifica('-');
         E();
      }else if (laToken == '+'){
         verifica('+');
         E();
      }else if (laToken == '*'){
         verifica('*');
         E();
      }else if (laToken == '/'){
         verifica('/');
         E();
      }else if (laToken == AND){
         verifica(AND);
         E();
      }else if (laToken == OR){
         verifica(OR);
         E();
      }
   }



   private void verifica(int expected) {
      if (laToken == expected)
         laToken = this.yylex();
      else {
         String expStr, laStr;

         expStr = ((expected < BASE_TOKEN_NUM)
               ? "" + (char) expected
               : tokenList[expected - BASE_TOKEN_NUM]);

         laStr = ((laToken < BASE_TOKEN_NUM)
               ? new Character((char) laToken).toString()
               : tokenList[laToken - BASE_TOKEN_NUM]);

         yyerror("esperado token : " + expStr +
               " na entrada: " + laStr);
      }
   }

   /* metodo de acesso ao Scanner gerado pelo JFLEX */
   private int yylex() {
      int retVal = -1;
      try {
         yylval = new ParserVal(0); // zera o valor do token
         retVal = lexer.yylex(); // le a entrada do arquivo e retorna um token
      } catch (IOException e) {
         System.err.println("IO Error:" + e);
      }
      return retVal; // retorna o token para o Parser
   }

   /* metodo de manipulacao de erros de sintaxe */
   public void yyerror(String error) {
      System.err.println("Erro: " + error);
      System.err.println("Entrada rejeitada");
      System.out.println("\n\nFalhou!!!");
      System.exit(1);

   }

   public void setDebug(boolean trace) {
      debug = true;
   }

   /**
    * Runs the scanner on input files.
    *
    * This main method is the debugging routine for the scanner.
    * It prints debugging information about each returned token to
    * System.out until the end of file is reached, or an error occured.
    *
    * @param args the command line, contains the filenames to run
    *             the scanner on.
    */
   public static void main(String[] args) {
      AsdrSample parser = null;
      try {
         if (args.length == 0)
            parser = new AsdrSample(new InputStreamReader(System.in));
         else
            parser = new AsdrSample(new java.io.FileReader(args[0]));

         parser.setDebug(false);
         laToken = parser.yylex();

         parser.Prog();

         if (laToken == Yylex.YYEOF)
            System.out.println("\n\nSucesso!");
         else
            System.out.println("\n\nFalhou - esperado EOF.");

      } catch (java.io.FileNotFoundException e) {
         System.out.println("File not found : \"" + args[0] + "\"");
      }
      // catch (java.io.IOException e) {
      // System.out.println("IO error scanning file \""+args[0]+"\"");
      // System.out.println(e);
      // }
      // catch (Exception e) {
      // System.out.println("Unexpected exception:");
      // e.printStackTrace();
      // }

   }

}
