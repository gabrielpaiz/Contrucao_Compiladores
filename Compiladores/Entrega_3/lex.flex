%%

%byaccj


%{
  private Parser yyparser;

  public Yylex(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
  }
%}

NL  = \n | \r | \r\n

%%

"$TRACE_ON"  { yyparser.setDebug(true);  }
"$TRACE_OFF" { yyparser.setDebug(false); }

if { return Parser.IF;}
else { return Parser.ELSE;}
while { return Parser.WHILE;}
int { return Parser.INT;}
double { return Parser.DOUBLE;}
boolean { return Parser.BOOLEAN;}
void { return Parser.VOID;}
&& {return Parser.AND;}
"||" {return Parser.OR;}
private|public {return Parser.FUNCT;}


[0-9]+ { return Parser.NUM;}
[a-zA-Z][a-zA-Z0-9]* { return Parser.IDENT;}
">" |
"<" |
"," |
"{" |
"}" |
"=" |
"(" |
")" |
";" |
"*" |
"/" |
"+" |
"-"     { return (int) yycharat(0); }

[ \t]+ { }
{NL}+  { } 

.    { System.err.println("Error: unexpected character '"+yytext()+"' na linha "+yyline); return YYEOF; }






