import java.io.InputStreamReader;
%%


%public
%class Contrato
%integer
%unicode
%line
%ignorecase


%{

public static double VALOR	    = 257.0;
public static int NUM_PARCELAS  = 258;
public static String NOME       = "Agustini";
public static float JUROS       = 0.03f;

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
    Contrato scanner;
    if (argv.length == 0) {
      try {        
          // scanner = new Contrato( System.in );
          scanner = new Contrato( new InputStreamReader(System.in) );
          while ( !scanner.zzAtEOF ) {
	              scanner.yylex();
                scanner.yytext();
          }
        }
        catch (Exception e) {
          // System.out.println("Unexpected exception:");
          // e.printStackTrace();
        }
        
    }
    else {
      for (int i = 0; i < argv.length; i++) {
        scanner = null;
        try {
          scanner = new Contrato( new java.io.FileReader(argv[i]) );
          while ( !scanner.zzAtEOF ) {
                scanner.yylex();
                scanner.yytext();
          }
        }
        catch (java.io.FileNotFoundException e) {
          //  System.out.println("File not found : \""+argv[i]+"\"");
        }
        catch (java.io.IOException e) {
          // System.out.println("IO error scanning file \""+argv[i]+"\"");
          // System.out.println(e);
        }
        catch (Exception e) {
          // System.out.println("Unexpected exception:");
          // e.printStackTrace();
        }
      }
    }
  }


%}

WHITESPACE=	[ \t]
LineTerminator = \r|\n|\r\n    


%%

"#data#" {System.out.print(yytext());}
"#nome#" {System.out.print(NOME);}
"#valor#" {System.out.print(VALOR);}
"#numero#" {System.out.print(NUM_PARCELAS);}
"#juros#" {System.out.print(JUROS);}
"#parcelas#" {System.out.print(NUM_PARCELAS);}


{WHITESPACE}+               {System.out.print(yytext());}
{LineTerminator}		{System.out.print(yytext());}
.         {System.out.print(yytext());}
