#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void inicializa(int * vetor, int tam){
    vetor = (int*) malloc(tam  * sizeof(int *));

    for(int i = 0;i<tam;i++){
        vetor[i] = rand() % tam;
    }
}

int main(int argc, char *argv[])
{
    int tam = 10;
    int *vetor1;

    inicializa(vetor1, tam);

    for(int i = 0;i<tam;i++){
        printf("%d,",vetor1[i]);
    }
    printf("\n");

    return 0;
}