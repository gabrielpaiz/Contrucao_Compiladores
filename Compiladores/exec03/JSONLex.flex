import java.io.InputStreamReader;
%%


%public
%class JSONLex
%integer
%unicode
%line
%ignorecase


%{

public static int IDENT		= 257;
public static int NUM			= 258;


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
    JSONLex scanner;
    if (argv.length == 0) {
      try {        
          // scanner = new JSONLex( System.in );
          scanner = new JSONLex( new InputStreamReader(System.in) );
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
          scanner = new JSONLex( new java.io.FileReader(argv[i]) );
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
  }


%}

WHITESPACE=	[ \t]
LineTerminator = \r|\n|\r\n    


%%

\"[^\"]*\"  {return IDENT;}
\'[^\']*\'  {return IDENT;}
[:digit:]+(\.[:digit:]+)?     {return NUM;}


":" |
"{" |
"}" |
"," |
"[" |
"]"                         {return yytext().charAt(0);}

{WHITESPACE}+               { }
{LineTerminator}		{}
.          {System.out.println(yyline+1 + ": caracter invalido: "+yytext());}
