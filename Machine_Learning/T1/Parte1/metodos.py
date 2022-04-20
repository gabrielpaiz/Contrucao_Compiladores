from itertools import count
import math
from random import randint

from numpy import Infinity, append

def insertionSort(array, index):
    
    for step in range(1, len(array)):
        key = array[step]
        j = step - 1
        
        # Compare key with each element on the left of it until an element smaller than it is found
        # For descending order, change key<array[j] to key>array[j].        
        while j >= 0 and key[index] < array[j][index]:
            array[j + 1] = array[j]
            j = j - 1
        
        # Place key at after the element just smaller than it.
        array[j + 1] = key

    return array

class KNN:
    head = None
    n = 1
    conj_treino = []

    def __init__(self, n, list_data):
        self.n = n
        self.conj_treino = list_data


    def dist_euclidiana(self, conj_x, conj_y):
        ret = 0.0
        for i in range(len(conj_x)):
            ret+=abs(conj_x[i] - conj_y[i])**2
        
        return math.sqrt(ret)

    def k_proximos(self, y):
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

        
        
class KDTree:
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
    conj_treino = []

    def __init__(self, list_data):
        self.conj_treino = list_data

        #list_order = ord_list_of_tuple_by_index_from_tuple(list_data.copy(), 0)
        list_order = insertionSort(list_data.copy(), 0)

        #print(list_order)

        self.head = self.Node(list_order[int(len(list_order)/2)], 0)

        self.head.insere_left(self.make_tree(self.head.left, list_order[int(len(list_order)/2)+1:].copy(), 1))
        self.head.insere_right(self.make_tree(self.head.right, list_order[:int(len(list_order)/2)].copy(), 1))


    
    def printTree(self, node, level=0):
        if node != None:
            self.printTree(node.left, level + 1)
            print(' ' * 4 * level + '-> (' + str(node.nivel) + ") - " + str(node.conj[0]))
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
        list_order = insertionSort(list_data, nivel)


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

    def k_proximos(self, y):
        #self.printTree(self.head)
        ret = self.__rec_proximos_KDTree(self.head, y, self.head).conj
        return [ret]

    def __rec_proximos_KDTree(self, node, y, vizinho_prox):
        if node is None:
            return vizinho_prox
        dist_node = self.dist_euclidiana(node.conj, y)
        dist_vizinho = self.dist_euclidiana(vizinho_prox.conj, y)

        if dist_node < dist_vizinho:
            vizinho_prox = node

        if node.conj[node.nivel] < y[node.nivel]:
            good_side = node.left
            bad_side = node.right
        else:
            bad_side = node.left
            good_side = node.right
        
        vizinho_prox = self.__rec_proximos_KDTree(good_side, y, vizinho_prox)

        if bad_side is not None:
            if self.dist_euclidiana(vizinho_prox.conj, y) >= abs(bad_side.conj[bad_side.nivel] - y[bad_side.nivel]):
                vizinho_prox = self.__rec_proximos_KDTree(bad_side, y, vizinho_prox)
        
        return vizinho_prox

class Naive_Bayes:
    table = []
    list_targets = []
    targets = []
    count_features = 0



    def __init__(self, table, colum_target):
        self.table = table
        self.list_targets = colum_target
        self.targets = list(set(colum_target))
        self.count_features = len(self.table[0])

    #altera

    def __priori(self, const):
        aux = 0
        for value in self.list_targets:
            if const == value:
                aux += 1
        #print(f'Priori de {const}\n Total: {aux}')
        return aux/len(self.list_targets)

    def __average(self, colum, target):
        ret = 0
        count_target = 0
        for i in range(len(self.list_targets)):
            if target == self.list_targets[i]:
                ret += self.table[i][colum]
                count_target += 1

        #print(f'AVG: {ret}/{count_target} = {ret/(count_target)}')

        return ret/count_target

    #alterar
    def __variance(self, colum, target, avg):
        ret = 0
        count_target = 0
        for i in range(len(self.list_targets)):
            if target == self.list_targets[i]:
                ret += (self.table[i][colum] - avg)**2
                count_target += 1
        #print(f'Variance: {ret}/{count_target-1} = {ret/(count_target-1)}')
        return ret/(count_target-1)

    #alterar
    def _gaussian_distribuition(self, x, colum, target):
        avg = self.__average(colum, target)
        var = self.__variance(colum, target, avg)
        return ( 1/ math.sqrt(2*math.pi*var) ) * math.exp(-((x - avg)**2 / (2*var)))


    def bayes_prediction(self, object):
        best_predicion_value = 0
        prediction_name = ""
        
        for target in self.targets:
            posteriori = self.__priori(target)
            for i in range(self.count_features):
                posteriori *= self._gaussian_distribuition(object[i], i, target)

            #print(f'{posteriori} de ser {target}!')
            if posteriori > best_predicion_value:
                #print("Mudou")
                best_predicion_value = posteriori
                prediction_name = target

        return prediction_name



