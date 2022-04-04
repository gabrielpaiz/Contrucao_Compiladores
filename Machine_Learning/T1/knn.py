import math

from numpy import append


class knn:
    n = 1
    conj_dados = []

    def __init__(self, n, list_data) -> None:
        self.n = n
        self.conj_dados = list_data

    def dist_euclidiana(self, conj_x, conj_y) -> float:
        ret = 0.0
        for i in range(len(conj_x)):
            ret+=abs(conj_x[i] - conj_y[i])**2
        
        return math.sqrt(ret)

    def n_proximos(self, conj_y) -> list:
        ls_proximos = []
        ls_proximos.append((0,self.dist_euclidiana(self.conj_dados[0], conj_y)))

        for i in range(1,len(self.conj_dados)):
            dist = self.dist_euclidiana(self.conj_dados[i], conj_y)
            for j in range(len(ls_proximos)):
                if dist < ls_proximos[j][1]:
                    ls_proximos.insert(j, (i, dist))
                if len(ls_proximos) > self.n:
                    ls_proximos.pop()
                    break
            
        result = []
        for set in ls_proximos:
            result.append(self.conj_dados[set[0]])

        return result


lista_treino = [(0,1), (2,2), (3,4), (4,1)]
knn = knn(2, lista_treino)

print(f'Vizinho mais próximo de {(3,3)}: {knn.n_proximos((3,3))}')
print(f'Vizinho mais próximo de {(3,2)}: {knn.n_proximos((3,2))}')
