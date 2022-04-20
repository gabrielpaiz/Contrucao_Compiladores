A parte 1 do trabalho eu desenvolvi em dois arquivos, o metodos.py e run_dataset.py. Um é a logica dos metodos de predição e o outro é para trabalgar com os dados do csv.

Para executar a primeira parte, eu fiz um main_parte1.py. Onde deve ser executado da pasta sua pasta por causa do arquivo .csv

Esse main_parte1 aceita 3 argumentos. A classe, o k, e a porcentagem de distribuição dos testes(Se vc quer um distribuição 30% - 70%, coloque 30). 
Porém, apenas a classe é obrigatório. Por padrão, K = 3 e Porcnetagem = 30. Se rodar sem a classe vai dar meg de erro.

Ex:
python3 main_parte1.py Species 3 30
python3 main_parte1.py Sex 5
python3 main_parte1.py Island


Já a parte 2, eu apenas fiz um código para executar os comandos do sklearn, que é o metricas.py. ele tbm tem que ser executado da pasta onde está o arquivo.
Eu fiz um reaproveitamento de código, então ele roda exatamente da mesma maneira que o main_parte1.py, com os mesmo argumentos

Ex:
python3 metricas.py Species 3 30
python3 metricas.py Sex 5
python3 metricas.py Island

No metricas eu estou plotando a tabela de confusão. A figure 1 = Knn, figure 2 = KDTree e figure 3 = Naive Bayes