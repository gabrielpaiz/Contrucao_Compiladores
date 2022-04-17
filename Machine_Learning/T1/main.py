from random import randint
from unittest import result
from numpy import NaN
import pandas as pd
from knn import KNN

# with open("penguins.csv", "r") as csvfile:
#     reader = csv.DictReader(csvfile)
#     for row in reader:
#         print(row)

def print_list(l) -> None:
    for d in l:
        print(d)

class dataset:
    data_frame = None
    df_list = []
    comparative_colum = []

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

    # busca no dataframe grandão, as respostas obtidas
    def __get_set_from_subset(self, list_subset):
        set_list = []
        for subset in list_subset:
            for tup in self.df_list:
                if set(subset).issubset(tup):
                    set_list.append(tup)
                    break
        
        return set_list

    # serve para definir qual é o melhor
    def def_result(self, list_tuple_values):
        subset = self.__get_set_from_subset(list_tuple_values)
        #print_list(subset)
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
    def run_knn(self, n, object):
        float_data_object = self.__get_tuple_of_type(float, object)

        treino_conj = self.__get_list_of_type(float)

        knn = KNN(n, treino_conj)
        n_proximos = knn.n_proximos(float_data_object)
        result_knn = self.def_result(n_proximos)

        i = 0
        r = True

        for col in self.comparative_colum:
            index = self.data_frame.columns.get_loc(col)
            #print(f"--Coluna {col}--\nResultado:{result_knn[i]}\nEsperado: {object[index]}\n{result_knn[i] == object[index]}\n")
            r = r and (result_knn[i] == object[index])
            i += 1

        return r


            


count_correct = 0
tested = 100

#["Species", "Region", "Island", "Stage", "Clutch Completion", "Sex"]
for i in range(tested):
    ds = dataset("penguins.csv", ["Species", "Region", "Island"])
    object = ds.get_one_test()
    if ds.run_knn(3, object):
        count_correct += 1
    
    ds.clenup()

print(f"Acertou {count_correct} de {tested}, {(100*count_correct)/tested}%")

