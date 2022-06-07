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
BOOL = "true"|"false"
NL  = \n | \r | \r\n

%%

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
"!" | 
"," |
"="   { return (int) yycharat(0); }
 
if  { return Parser.IF; }
else  { return Parser.ELSE; }
while  { return Parser.WHILE; }
print  { return Parser.PRINT; }
for  { return Parser.FOR; }
define  { return Parser.DEFINE; }
return  { return Parser.RETURN; }

">=" {return Parser.MAIOR_IGUAL;}
"<=" {return Parser.MENOR_IGUAL;}
"==" {return Parser.IGUAL;}
"!=" {return Parser.DIFF;}

"+=" {return Parser.SOMA_ATR;}
"*=" {return Parser.MULT_ATR;}

#help {return Parser.HELP;}
#show {return Parser.SHOW;}
#show_all {return Parser.SHOW_ALL;}

"&&" {return Parser.AND;}
"||" {return Parser.OR;} 



{NL}   { return Parser.NL; }

{NUM}  { yyparser.yylval = new ParserVal(Double.parseDouble(yytext()));
         return Parser.NUM; }
{BOOL} { yyparser.yylval = new ParserVal(Boolean.parseBoolean(yytext()));
         return Parser.BOOL;}

{IDENT} { yyparser.yylval = new ParserVal(yytext());
         return Parser.IDENT; }


[ \t]+ { }

[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
