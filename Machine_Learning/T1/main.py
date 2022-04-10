import csv
#from knn import knn

with open("penguins.csv", "r") as csvfile:
    reader = csv.DictReader(csvfile)
    for row in reader:
        print(row)
