from Parte1.run_dataset import dataset
from sklearn.metrics import recall_score, f1_score, accuracy_score, precision_score, confusion_matrix, ConfusionMatrixDisplay
import matplotlib.pyplot as plt
import sys


def str_list_format(l):
    s = "[{:.5f}".format(l[0])
    for i in range(1,len(l)):
        s += ' ' * 3 +"{:.5f}".format(l[i])

    return s +"]"

if len(sys.argv) == 1:
    print("Erro! Precisa, pelo menos, do parametros classe!")
    exit(0)
elif len(sys.argv) == 2:
    k = 3
    perc = 30
elif len(sys.argv) == 3:
    k = int(sys.argv[2])
    perc = 30
else:
    k = int(sys.argv[2])
    perc = float(sys.argv[3])

classe = sys.argv[1]

ds = dataset("penguins.csv", classe)
knn_pred, kd_pred, nb_pred, true_pred = ds.run_teste(perc, k=k, show=False)

print(f"### Knn Metrics ####\n\nAcuracia: {accuracy_score(true_pred, knn_pred)}" +
    f"\nPrecisão: {str_list_format(precision_score(true_pred, knn_pred, average = None))}" +
    f"\nRecall: {str_list_format(recall_score(true_pred, knn_pred, average = None))}" +
    f"\nF1: {str_list_format(f1_score(true_pred, knn_pred, average = None))}\n")
ConfusionMatrixDisplay.from_predictions(true_pred, knn_pred)

print(f"### KD Tree Metrics ####\n\nAcuracia: {accuracy_score(true_pred, kd_pred)}" +
        f"\nPrecisão: {str_list_format(precision_score(true_pred, kd_pred, average = None))}" +
        f"\nRecall: {str_list_format(recall_score(true_pred, kd_pred, average = None))}" +
        f"\nF1: {str_list_format(f1_score(true_pred, kd_pred, average = None))}\n")
ConfusionMatrixDisplay.from_predictions(true_pred, kd_pred)


print(f"### Naive Bayes Metrics ####\n\nAcuracia: {accuracy_score(true_pred, nb_pred)}"+
f"\nPrecisão: {str_list_format(precision_score(true_pred, nb_pred, average = None))}" +
f"\nRecall: {str_list_format(recall_score(true_pred, nb_pred, average = None))}" +
f"\nF1: {str_list_format(f1_score(true_pred, nb_pred, average = None))}\n")
ConfusionMatrixDisplay.from_predictions(true_pred, nb_pred)

plt.show()