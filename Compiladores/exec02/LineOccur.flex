import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
%%


%public
%class LineOccur
%integer
%unicode
%line


%{

public static int IDENT		= 257;
public static int NUM			= 258;

public static int IF 			= 259; 
public static int ELSE 		= 260;
public static int PUBLIC 	= 261;
public static int PRIVATE = 262;
public static int CLASS		= 263;
public static int EQUALS	= 264;

public static HashMap<String, Set<Integer>> occur = new HashMap<>();

public static void populate(String cmd, int line) {
  // System.out.println(cmd + " " + line + " bunda");
  if (!occur.containsKey(cmd)) 
    occur.put(cmd, new HashSet<>()); 
  
  Set<Integer> temp = occur.get(cmd);
  temp.add(line);
  occur.put(cmd, temp);
}

public static void printTable() {
  for (String cmd : occur.keySet()) {
    System.out.print(cmd + " -> ");
    for (int line : occur.get(cmd)) {
      System.out.print(line + " ");
    }
    System.out.println();
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
    LineOccur scanner;
    if (argv.length == 0) {
      try {        
          // scanner = new LineOccur( System.in );
          scanner = new LineOccur( new InputStreamReader(System.in) );
          while ( !scanner.zzAtEOF ) 
	        System.out.println("token: "+scanner.yylex()+"\t<"+scanner.yytext()+">");
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
          scanner = new LineOccur( new java.io.FileReader(argv[i]) );
          while ( !scanner.zzAtEOF ) 	
                System.out.println("token: "+scanner.yylex()+"\t<"+scanner.yytext()+">");
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

if				{ populate("if", yyline + 1); }
else			{ populate("else", yyline + 1); } 
public		{ populate("public", yyline + 1); }
private		{ populate("private", yyline + 1); }
class			{ populate("class", yyline + 1); }
int       { populate("int", yyline + 1); }
String    { populate("String", yyline + 1); }
this      { populate("this", yyline + 1); }

{LETTER}({LETTER}|{DIGIT})* |
{DIGIT}+                    { populate(yytext(), yyline + 1); }

"=" |
"+" |
"*" |
";" |
"{" |
"}" |
"." |
"," |
"(" |
")"                         { populate(yytext().charAt(0) + "", yyline + 1);}
"=="                        { populate("==", yyline + 1); }
{WHITESPACE}+               { }
{LineTerminator}		{}
.          { }
