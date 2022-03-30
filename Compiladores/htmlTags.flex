%%

%public
%class HtmlTags
%standalone
%ignorecase

%unicode

%line


%%
"<img"[^>]*> {  System.out.println( "(" + yyline + ") tag: " + yytext()); } 
"<A HREF"[^>]*> {  System.out.println( "(" + yyline + ") tag: " + yytext()); } 
[^]   {}
