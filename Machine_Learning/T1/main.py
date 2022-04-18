from random import randint
from numpy import NaN
import time
import pandas as pd
from knn import KNN

# with open("penguins.csv", "r") as csvfile:
#     reader = csv.DictReader(csvfile)
#     for row in reader:
#         print(row)

def print_list(l):
    for d in l:
        print(d)

class dataset:
    data_frame = None
    df_list = []
    comparative_colum = []

    # Bom vou tentar explicar oq eu fiz, espero que vc entenda kkkkk
    # o data_frame é um objeto pandas, que lê o csv. Com ele que eu consigo as informações
    # o df_list é o mesmo data frame só que do jeito que eu achei melhor trabalhar, que é uma lista de tuplas. Como funciona
    # Bom é uma matriz né, só que em vez de lista, eu fiz lista x tuplas
    # [
    #  (data1, data2, data3, ..., data4)
    #  (data1, data2, data3, ..., data4)
    # ]
    # os metodos __get_list_of_type e __get_tuple_of_type criam uma lista assim, porém com apenas o tipo que eu definir, por exemplo float. A diferença 
    # entre eles é que um retorna só uma tupla, e o outro retorna uma lista. Oq retorna a lista pega os dados apenas do data_frama. Já o outro, tem que passar
    # por parametro
    # 
    # o comparative_colum, são as colunas que eu quro compara no final, ou que eu quero q o programa adivinhe. Achei mais facil passar por parametro
    # dá para fazer altos testes
    # 
    # O bom é, no data_frame eu consigo buscar a posição de um coluna com o "self.data_frame.columns.get_loc(col)", passando o nome da coluna por paramentro
    # ele retorna o index dela no csv/dataframe, e como os dados nas tupla(ou tbm posso dizer, nas colunas da nossa matriz) estão na mesma ordem, eu consigo 
    # pegar facilemnte o dado da matriz. Ex: "StudyName" é a coluna 0 do csv, se eu chamar self.data_frame.columns.get_loc("StudyName") vai retornar 0, na 
    # tupla, por ter pego so dados na ordem do csv, o tup[0] tem é o "StudyName". Acho que vc entendeu, essa parte né, espero eu
    def __init__(self, csv_name, comp_colum):
        self.data_frame = pd.read_csv(csv_name)
        self.data_frame = self.data_frame.dropna(subset=self.data_frame.columns[:-1])

        self.comparative_colum = comp_colum

        for row_index, row in self.data_frame.iterrows():
            tup = ()
            for data in row:
                tup = tup + (data,)
            self.df_list.append(tup)

    def clenup(self):
        self.df_list.clear()
        self.comparative_colum.clear()
        del self.data_frame

    #Pega um aleatório do data set para teste, retira do dataframe
    def get_one_test(self):
        #print(len(self.df_list))
        index = randint(0, len(self.df_list)-1)
        self.data_frame = self.data_frame.drop(self.data_frame.index[index])

        return self.df_list.pop(index)

    # Cria uma lista de tuplas dos valores de um tipo só
    def __get_list_of_type(self, tipo):
        list_type = []
        for row_index, row in self.data_frame.iterrows():
            list_type. append(self.__get_tuple_of_type(tipo, row))
        
        return list_type

    #Cria uma tupla de valores de um tipo só
    def __get_tuple_of_type(self, tipo, tup):
        aux = ()
        for data in tup:
            if type(data) is tipo and data is not NaN:
                aux = aux + (data,)
        
        return aux

    # busca no dataframe grandão, as respostas obtidas. Basicamente um search, eu passo um subset de um row e ele retorna o row inteira
    def __get_set_from_subset(self, list_subset):
        set_list = []
        for subset in list_subset:
            for tup in self.df_list:
                if set(subset).issubset(tup):
                    set_list.append(tup)
                    break
        
        return set_list

    # serve para definir quais são os atributos que aparecem mais na lista dos mais proximos 
    def get_best(self, list_tuple_values):
        subset = self.__get_set_from_subset(list_tuple_values)
        obj = []
        #  Anda através das colunas que serão comparadas
        for col in self.comparative_colum:
            index = self.data_frame.columns.get_loc(col)
            count_higher = {}
            higher = ""
            #andas atrávés dos dados no subconjuto encontrado
            for data in subset:
                #Se no dicionario tiver oq está sendo comparado, soma seu contador. Se não adciiona 1 naquele chave
                if higher == "":
                    higher = data[index]
                if data[index] not in count_higher.keys():
                    count_higher[data[index]] = 1
                else:
                    count_higher[data[index]] += 1
                #Se a quantidade de algum valor no dicionario for maior que o higher, então troca
                if count_higher[higher] < count_higher[data[index]]:
                    higher = data[index]
            
            obj.append(higher)

        return obj

    #Roda o knn
    def run_knn_brutal(self, k, object):
        #Ele recebe um objeto que é uma linha do penguins, na linha debaixo, ele está só retirando dessa tupla os floats par ao calculo
        float_data_object = self.__get_tuple_of_type(float, object)

        #Mesma coisa, porém agora com o dataframe inteiro
        treino_conj = self.__get_list_of_type(float)

        #cria o objeto KNN
        knn = KNN(k, treino_conj)
        n_proximos = knn.k_proximos_brutal(float_data_object)
        result_knn = self.get_best(n_proximos)

        return self.compare_result(result_knn)

        
    def run_knn_KDTree(self, k , object):
        #mesmo codigo que encima, porem para rodar com o KD Tree
        float_data_object = self.__get_tuple_of_type(float, object)
        treino_conj = self.__get_list_of_type(float)

        knn = KNN(k, treino_conj)
        n_proximos = knn.k_proximos_KDTree(float_data_object)
        result_knn = self.get_best(n_proximos)

        return self.compare_result(result_knn)        

    def compare_result(self, result):
        i = 0
        r = True

        #compara o resultado obtido com as colunas que vão ser comparads, pra ver se chegou no resultado
        for col in self.comparative_colum:
            index = self.data_frame.columns.get_loc(col)
            #print(f"--Coluna {col}--\nResultado:{result[i]}\nEsperado: {object[index]}\n{result[i] == object[index]}\n")
            r = r and (result[i] == object[index])
            i += 1

        return r




            


count_correct = 0
tested = 100

# ds = dataset("Machine_Learning/T1/penguins.csv", ["Species", "Region", "Island"])
# object = ds.get_one_test()

# ds.run_knn_KDTree(3,object)

#["Species", "Region", "Island", "Stage", "Clutch Completion", "Sex"]
t = time.time()
for i in range(tested):
    ds = dataset("penguins.csv", ["Species"])
    object = ds.get_one_test()
    if ds.run_knn_KDTree(3, object):
        count_correct += 1
    
    ds.clenup()


print(f"Acertou {count_correct} de {tested}, {(100*count_correct)/tested}%")
print(f"Terminou em {time.time() - t} segundos")
