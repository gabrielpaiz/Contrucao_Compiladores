import math
from random import randint

from numpy import Infinity, append

#ordena uma lista de tuplas através de um idex da tupla
def ord_list_of_tuple_by_index_from_tuple(list_data, index):
    for i in range(len(list_data)-1):
        for j in range(i):
            if (list_data[i][index] > list_data[j+1][index]):
                temp = list_data[i]
                list_data[i] = list_data[j]
                list_data[j] = temp
    
    return list_data


class KNN:
    class Node:
        conj = ()
        left = None
        right = None
        nivel = None

        def __init__(self, conj, nivel):
            self.conj = conj
            self.nivel = nivel
        
        def insere_left(self, node):
            self.left = node
        
        def insere_right(self, node):
            self.right = node


    
    head = None
    n = 1
    conj_treino = []


    #Admito que estou com pregiça de escrever
    #Basicamente: Esse é o knn, eu decidi fazer a KDTree e o brutal froce no mesmo objeto. Tá aqui como eleé montado.
    # Eu segui bem como o professor mostrou em aula. eu espero. Eu tentei pelo menos
    def __init__(self, n, list_data):
        self.n = n
        self.conj_treino = list_data

        list_order = ord_list_of_tuple_by_index_from_tuple(list_data.copy(), 0)


        self.head = self.Node(list_order[int(len(list_order)/2)], 0)

        self.head.insere_left(self.make_tree(self.head.left, list_order[:int(len(list_order)/2)].copy(), 1))
        self.head.insere_right(self.make_tree(self.head.right, list_order[int(len(list_order)/2)+1:].copy(), 1))

    
    def printTree(self, node, level=0):
        if node != None:
            self.printTree(node.left, level + 1)
            print(' ' * 4 * level + '-> ' + str(node.nivel))
            self.printTree(node.right, level + 1)
    
    #Cria a arvore
    #O Nivel é o index na tupla, ou seja, qual x1, x2, x3 etc que está sendo comparado naquela nodo
    def make_tree(self, nodo, list_data, nivel):
        # 1 condiçãod e parada: Se está vazia a lista de tuplas, retorna nada
        if len(list_data) == 0:
            return None
        # reseta o nivle se ele passar no tamanho da tupla
        if nivel == len(list_data[0]):
            nivel = 0
        #2 se só tem um elemento, retorna ele 
        if len(list_data) == 1:
            return self.Node(list_data[0], nivel)
        list_order = ord_list_of_tuple_by_index_from_tuple(list_data, nivel)

        #Cria do nodo com a tupla do meio da lista, e o nivel atual
        nodo = self.Node(list_order[int(len(list_order)/2)], nivel)
        #Insere os nodos da esqueda e direita com as outras metades da lista, e com nivel +1
        nodo.insere_left(self.make_tree(nodo.left, list_order[:int(len(list_order)/2)].copy(), nivel+1))
        nodo.insere_right(self.make_tree(nodo.right, list_order[int(len(list_order)/2)+1:].copy(), nivel+1))

        return nodo



    def dist_euclidiana(self, conj_x, conj_y):
        ret = 0.0
        for i in range(len(conj_x)):
            ret+=abs(conj_x[i] - conj_y[i])**2
        
        return math.sqrt(ret)

    def k_proximos_brutal(self, y):
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

    def k_proximos_KDTree(self, y):
        prox = [self.head.conj]
        self.__rec_proximos_KDTree(self.head, y, self.head, prox)
        return prox

    def __rec_proximos_KDTree(self, node, y, vizinho_prox, prox):
        if node is None:
            return vizinho_prox
        dist_node = self.dist_euclidiana(node.conj, y)
        dist_vizinho = self.dist_euclidiana(vizinho_prox.conj, y)

        if dist_node < dist_vizinho:
            vizinho_prox = node

        for j in range(len(prox)):
            #Se a distancia encontrada for menor que alguma na lista atual. Insere na posição que achou a tupla com (i, distancia) I
            dist = self.dist_euclidiana(prox[j], y)
            if dist_node < dist:
                prox.insert(j, node.conj)
            # Se o tamanho da lista ficar maior que o n, tira o ultimo
            if len(prox) > self.n:
                prox.pop()
                break

        if node.conj[node.nivel] > y[node.nivel]:
            good_side = node.left
            bad_side = node.right
        else:
            bad_side = node.left
            good_side = node.right
        
        vizinho_prox = self.__rec_proximos_KDTree(good_side, y, vizinho_prox, prox)

        aux = False

        for conj in prox:
            if bad_side is None:
                break
            if self.dist_euclidiana(conj, y) > abs(bad_side.conj[bad_side.nivel] - y[bad_side.nivel]):
                aux = True
                break

        if aux:
            vizinho_prox = self.__rec_proximos_KDTree(bad_side, y, vizinho_prox, prox)
        
        return vizinho_prox
        
        