import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.ArrayList;
%%


%public
%class Json
%integer
%unicode
%line


%{

public static int STRING		= 257;
public static int NUMBER			= 258;

public static int ARRAY 	= 259; 

public static HashMap<String, ArrayList<Integer>> TABELA = new HashMap<>();


public static void addMap(String ident, Integer linha){
  if (TABELA.containsKey(ident)){
    TABELA.get(ident).add(linha+1);
  }else{
    ArrayList<Integer> l = new ArrayList<>();
    l.add(linha+1);
    TABELA.put(ident, l);
  }
}

public static void printTable(){
  for(String key:TABELA.keySet()){
    System.out.print(key+" -> ");
    for(Integer value : TABELA.get(key)){
      System.out.print(value+" | ");
    }
    System.out.println("");
  }
}
/**
   * Runs the scanner on input files.
   *
   * This is a standalone scanner, it will print any unmatched
   * text to System.out unchanged.
   *
   * @param argv   the command line, contains the filenames to run
   *               the scanner on.
   */
  public static void main(String argv[]) {
    Json scanner;
    if (argv.length == 0) {
      try {        
          // scanner = new Json( System.in );
          scanner = new Json( new InputStreamReader(System.in) );
          while ( !scanner.zzAtEOF ){
            System.out.println("token: "+scanner.yylex()+"\t<"+scanner.yytext()+">");
          }
        }
        catch (Exception e) {
          System.out.println("Unexpected exception:");
          e.printStackTrace();
        }
        
    }
    else {
      for (int i = 0; i < argv.length; i++) {
        scanner = null;
        try {
          scanner = new Json( new java.io.FileReader(argv[i]) );
          while ( !scanner.zzAtEOF ) 	{
            System.out.println("token: "+scanner.yylex()+"\t<"+scanner.yytext()+">");
          }
        }
        catch (java.io.FileNotFoundException e) {
          System.out.println("File not found : \""+argv[i]+"\"");
        }
        catch (java.io.IOException e) {
          System.out.println("IO error scanning file \""+argv[i]+"\"");
          System.out.println(e);
        }
        catch (Exception e) {
          System.out.println("Unexpected exception:");
          e.printStackTrace();
        }
      }
    }
    printTable();
  }


%}

DIGIT=		[0-9]
WHITESPACE=	[ \t]
LineTerminator = \r|\n|\r\n    

%%

\"[^\"]*\"              {addMap(yytext(), yyline); return STRING;}
\'[^\']*\'              {addMap(yytext(), yyline); return STRING;}
{DIGIT}+("."{DIGIT}+)?  {addMap(yytext(), yyline); return NUMBER;}

"[" |
"]" |
":" |
"{" |
"}" |
"," |
"(" |
")"                         {addMap(yytext(), yyline); return yytext().charAt(0);}
{WHITESPACE}+               { }
{LineTerminator}		{}
.          {System.out.println(yyline+1 + ": caracter invalido: "+yytext());}
