import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.ArrayList;
%%


%public
%class Tabela_MeuLexico
%integer
%unicode
%line


%{

// public static int IDENT		= 257;
// public static int NUM			= 258;

// public static int IF 			= 259; 
// public static int ELSE 		= 260;
// public static int PUBLIC 	= 261;
// public static int PRIVATE = 262;
// public static int CLASS		= 263;
// public static int EQUALS	= 264;

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
    Tabela_MeuLexico scanner;
    if (argv.length == 0) {
      try {        
          // scanner = new Tabela_MeuLexico( System.in );
          scanner = new Tabela_MeuLexico( new InputStreamReader(System.in) );
          while ( !scanner.zzAtEOF ){
            scanner.yylex();
            scanner.yytext();
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
          scanner = new Tabela_MeuLexico( new java.io.FileReader(argv[i]) );
          while ( !scanner.zzAtEOF ) 	{
            scanner.yylex();
            scanner.yytext();
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
LETTER=		[a-zA-Z]
WHITESPACE=	[ \t]
LineTerminator = \r|\n|\r\n    


%%

if				{addMap("if", yyline);}
else			{addMap("else", yyline);} 
public		{addMap("public", yyline);}
private		{addMap("private", yyline);}
class			{addMap("class", yyline);}
this      {addMap("this", yyline);}

{LETTER}({LETTER}|{DIGIT})* {addMap(yytext(), yyline);}
{DIGIT}+                    {addMap(yytext(), yyline);}

"=" |
"+" |
"*" |
";" |
"{" |
"}" |
"." |
"," |
"(" |
")"                         {addMap(yytext(), yyline);}
"=="                        {addMap("==", yyline);}
{WHITESPACE}+               { }
{LineTerminator}		{}
.          {}
