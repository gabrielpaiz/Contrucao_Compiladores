/*
 * Integrantes:
 *  - Gabriel Bonatto Justo 17104118-9 gabriel.justo@acad.pucrs.br
 *  - Niccolas Guntzel Maganeli 16201269-4 niccolas.maganeli@edu.pucrs.br
 */

%%

%byaccj

%{
  private Parser yyparser;

  public Yylex(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
  }
%}

NUM = [:digit:]+ ("." [:digit:]+)?
IDENT = [:letter:]+ ([:letter:]|[:digit:])*
NL  = \n | \r | \r\n

%%

"==" { return Parser.EQUALS; }
"!=" { return Parser.DIFF; }
">=" { return Parser.GE; }
"<=" { return Parser.LE; }
"&&" { return Parser.AND; }
"||" { return Parser.OR; }
"+=" { return Parser.PLUS_EQ; }
"*=" { return Parser.TIMES_EQ; }
"++" { return Parser.PLUS_PLUS; }
"--" { return Parser.MINUS_MINUS; }

/* operators */
";" | 
"{" | 
"}" | 
"+" | 
"-" | 
"*" | 
"/" | 
"^" | 
"(" | 
")" |
"<" |
">" |
"#" |
"!" |
"," |
"="   { return (int) yycharat(0); }

define { return Parser.DEFINE; }
for { return Parser.FOR; }
if  { return Parser.IF; }
else  { return Parser.ELSE; }
while  { return Parser.WHILE; }
print  { return Parser.PRINT; }
help { return Parser.HELP; }
show_all { return Parser.SHOW_ALL; }
show { return Parser.SHOW; }
return { return Parser.RETURN; }

{NL}   { return Parser.NL; }

{NUM}  { yyparser.yylval = new ParserVal(Double.parseDouble(yytext()));
         return Parser.NUM; }

{IDENT} { yyparser.yylval = new ParserVal(yytext());
         return Parser.IDENT; }

[ \t]+ { }

[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
