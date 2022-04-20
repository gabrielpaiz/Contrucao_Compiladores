import sys
from Parte1.run_dataset import dataset


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
ds.run_teste(perc, k=k)