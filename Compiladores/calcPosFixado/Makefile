# atenção, copie para a pasta de tabalho
# os arquivos JFlex.jar e yacc.linux
#

JFLEX  = jflex
#JFLEX  = java -jar JFlex.jar 
BYACCJ = byaccj -tv -J
#BYACCJ = ./yacc.linux -tv -J
JAVAC  = javac
PROG = calc

# targets:

all: Parser.class

run: Parser.class
	java Parser

build: clean Parser.class

clean:
	rm -f *~ *.class Yylex.java Parser.java y.output

Parser.class: Yylex.java Parser.java
	$(JAVAC) Parser.java

Yylex.java: $(PROG).flex
	$(JFLEX) $(PROG).flex

Parser.java: $(PROG).y
	$(BYACCJ) $(PROG).y
