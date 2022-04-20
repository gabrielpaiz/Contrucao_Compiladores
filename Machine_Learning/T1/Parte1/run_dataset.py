from random import randint
from numpy import NaN
import pandas as pd
from Parte1.metodos import KNN, KDTree, Naive_Bayes

def print_list(l):
    for d in l:
        print(d)

class dataset:
    data_frame = None
    df_list = []
    comparative_colum = ""
    index_target = 0

    def __init__(self, csv_name, comp_colum):
        self.data_frame = pd.read_csv(csv_name)
        self.data_frame = self.data_frame.dropna(subset=self.data_frame.columns[:-1])

        self.comparative_colum = comp_colum
        self.index_target = self.data_frame.columns.get_loc(self.comparative_colum)

        for row_index, row in self.data_frame.iterrows():
            tup = ()
            for data in row:
                tup = tup + (data,)
            self.df_list.append(tup)
        

    def clenup(self):
        self.df_list.clear()
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
    def get_prediction_from_list(self, list_tuple_values):
        subset = self.__get_set_from_subset(list_tuple_values)
        if len(list_tuple_values) == 1:
            return subset[0][self.index_target]
        else:
            predictions = {}
            high = 0
            name_pred = ""
            for tup in subset:
                if tup[self.index_target] not in predictions.keys():
                    predictions[tup[self.index_target]] = 1
                else:
                    predictions[tup[self.index_target]] += 1


            for key in predictions:
                if predictions[key] > high:
                    high = predictions[key]
                    name_pred = key


           
            return name_pred

    #Roda o knn
    def run_knn_brutal(self, k, object):
        #Ele recebe um objeto que é uma linha do penguins, na linha debaixo, ele está só retirando dessa tupla os floats par ao calculo
        float_data_object = self.__get_tuple_of_type(float, object)

        #Mesma coisa, porém agora com o dataframe inteiro
        treino_conj = self.__get_list_of_type(float)

        #cria o objeto KNN
        knn = KNN(k, treino_conj)
        n_proximos = knn.k_proximos(float_data_object)
        result_knn = self.get_best(n_proximos)

        return self.compare_result(result_knn)

        
    def run_knn_KDTree(self, object):
        #mesmo codigo que encima, porem para rodar com o KD Tree
        float_data_object = self.__get_tuple_of_type(float, object)
        treino_conj = self.__get_list_of_type(float)

        knn = KDTree(treino_conj)
        n_proximos = knn.k_proximos(float_data_object)
        result = self.get_best([n_proximos])

        return self.compare_result(result)        

    def compare_result(self, result):
        i = 0
        r = True

        #compara o resultado obtido com as colunas que vão ser comparads, pra ver se chegou no resultado
        for col in self.comparative_colum:
            index = self.data_frame.columns.get_loc(col)
            r = r and (result[i] == object[index])
            i += 1

        return r

    def run_naive_bayes_prediction(self, object):
        float_data_object = self.__get_tuple_of_type(float, object)
        conj_treino = self.__get_list_of_type(float)

        target_colum = []

        target_index = self.data_frame.columns.get_loc(self.comparative_colum)

        for values in self.df_list:
            target_colum.append(values[target_index])

        #print(target_colum)
        nb = Naive_Bayes(conj_treino, target_colum)

        prediction = nb.bayes_prediction(float_data_object)

        #print(f'É {object[target_index]} e eu acho que é {prediction}')
        return prediction == object[target_index]
        
    def run_teste(self, treino_porcent, k=3, show = True):
        conj_test = []
        qtd_test = int(len(self.df_list)*(treino_porcent/100))
        for _ in range(qtd_test):
            conj_test.append(self.get_one_test())

        conj_treino = self.__get_list_of_type(float)

        target_colum = []

        for values in self.df_list:
            target_colum.append(values[self.index_target])

        knn = KNN(k, conj_treino)
        kd_tree = KDTree(conj_treino)
        nBayes = Naive_Bayes(conj_treino, target_colum) 

        count_kd = 0
        count_knn = 0
        count_nb = 0

        knn_pred  = []
        kd_pred = []
        nb_pred = []
        true_pred  = []

        for test in conj_test:
            float_data_object = self.__get_tuple_of_type(float, test)

            pred_knn = self.get_prediction_from_list(knn.k_proximos(float_data_object))
            knn_pred.append(pred_knn[:5])
            if pred_knn == test[self.index_target]:
                count_knn += 1

            pred_kd = self.get_prediction_from_list(kd_tree.k_proximos(float_data_object))
            kd_pred.append(pred_kd[:5])
            if pred_kd == test[self.index_target]:
                count_kd += 1

            pred_nb = nBayes.bayes_prediction(float_data_object)
            nb_pred.append(pred_nb[:5])            
            if pred_nb == test[self.index_target]:
                count_nb += 1

            true_pred.append(test[self.index_target][:5])

        if show:
            print("------------- Resultados -------------\n")
            print("## KNN Forca Bruta acertou {:10.1f}% ##\n".format((count_knn/qtd_test*100)))
            print("## KNN KD Tree acertou {:10.1f}% ##\n".format((count_kd/qtd_test*100)))
            print("## Naive Bayes acertou {:10.1f}% ##\n".format((count_nb/qtd_test*100)))

        return knn_pred, kd_pred, nb_pred, true_pred

        