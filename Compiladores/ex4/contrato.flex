import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
%%


%public
%class Contrato
%integer
%unicode
%line


%{
public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

public static String NOME;
public static String DATA		= LocalDate.now().format(formatter);
public static Double VALOR;
public static Double JUROS;
public static Integer	PARCELAS;


// public static HashMap<String, ArrayList<Integer>> TABELA = new HashMap<>();


// public static void addMap(String ident, Integer linha){
//   if (TABELA.containsKey(ident)){
//     TABELA.get(ident).add(linha+1);
//   }else{
//     ArrayList<Integer> l = new ArrayList<>();
//     l.add(linha+1);
//     TABELA.put(ident, l);
//   }
// }

// public static void printTable(){
//   for(String key:TABELA.keySet()){
//     System.out.print(key+" -> ");
//     for(Integer value : TABELA.get(key)){
//       System.out.print(value+" | ");
//     }
//     System.out.println("");
//   }
// }
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
        NOME = argv[0];
        VALOR = Double.parseDouble(argv[1]);
        JUROS = Double.parseDouble(argv[2]);
        PARCELAS = Integer.parseInt(argv[3]);
        scanner = null;
        try {
          scanner = new Contrato( new java.io.FileReader(argv[4]) );
          while ( !scanner.zzAtEOF ) 	{
            scanner.yylex();
            scanner.yytext();
          }
        }
        catch (java.io.FileNotFoundException e) {
          System.out.println("File not found : \""+argv[4]+"\"");
        }
        catch (java.io.IOException e) {
          System.out.println("IO error scanning file \""+argv[4]+"\"");
          System.out.println(e);
        }
        catch (Exception e) {
          System.out.println("Unexpected exception:");
          e.printStackTrace();
        }
    }
  }


%}

WHITESPACE=	[ \t]
LineTerminator = \r|\n|\r\n 

%%

#data#    {System.out.print(DATA);}
#nome#    {System.out.print(NOME);}
#valor#   {System.out.printf("R$ %.2f", VALOR);}
#numero#  {System.out.print(PARCELAS);}
#juros#   {System.out.print(JUROS*100+"%");}
#parcelas# {double value = VALOR/PARCELAS;
            for(int i = 0;i<PARCELAS;i++){
              System.out.printf("%d - R$ %.2f", i, value);
              System.out.println("\\par");
              value *= (1+JUROS);
              }
           }

{WHITESPACE}+               {System.out.print(yytext());}
{LineTerminator}		{System.out.print(yytext());}
.          {System.out.print(yytext());}
