import math

from numpy import append


class KNN:
    n = 1
    conj_treino = []

    def __init__(self, n, list_data) -> None:
        self.n = n
        self.conj_treino = list_data

    def dist_euclidiana(self, conj_x, conj_y) -> float:
        ret = 0.0
        for i in range(len(conj_x)):
            ret+=abs(conj_x[i] - conj_y[i])**2
        
        return math.sqrt(ret)

    def n_proximos(self, y) -> list:
        ls_vizinho = []
        # ls_vizinho -> lista de n vizinhos
        # ls_vizinhos adiciona uma lista de tuplas, sendo as tuplas -> (posição no conjunto treino, distancia euclidiana)
        ls_vizinho.append((0,self.dist_euclidiana(self.conj_treino[0], y)))

        # Percorre de 1 até o tamanho do treino
        for i in range(1,len(self.conj_treino)):
            #Calcula distancia entre o y e o conj_treino[i]
            dist = self.dist_euclidiana(self.conj_treino[i], y)
            #percorre de 0 até o tamnho da lista de n vizinhos mais proximos
            for j in range(len(ls_vizinho)):
                #Se a distancia encontrada for menor que alguma na lista atual. Insere na posição que achou a tupla com (i, distancia) I
                if dist < ls_vizinho[j][1]:
                    ls_vizinho.insert(j, (i, dist))
                # Se o tamanho da lista ficar maior que o n, tira o ultimo
                if len(ls_vizinho) > self.n:
                    ls_vizinho.pop()
                    break
            
        
        result = []
        # Apenas para retornar o set com todo o conteudo do treino
        for set in ls_vizinho:
            result.append(self.conj_treino[set[0]])

        return result
