#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "mpi.h"

#define DEBUG 1            // comentar esta linha quando for medir tempo
     // trabalho final com o valores 10.000, 100.000, 1.000.000

void bs(int n, int * vetor)
{
    int c=0, d, troca, trocou =1;

    while (c < (n-1) & trocou )
        {
        trocou = 0;
        for (d = 0 ; d < n - c - 1; d++)
            if (vetor[d] > vetor[d+1])
                {
                troca      = vetor[d];
                vetor[d]   = vetor[d+1];
                vetor[d+1] = troca;
                trocou = 1;
                }
        c++;
        }
}


int* slice_vetor(int * vet, int size, int max_rank, int my_rank){
    int new_size = size/max_rank;
    int new_vet[new_size];
    for(int i = 0; i<new_size;i++)
        new_vet[i] = vet[new_size*my_rank+i];
    return new_vet;
}

int main(int argc, char **argv)
{
    int my_rank;
    int max_rank; //proc_n
	int VETOR_SIZE = 1000000;
    int vetor[VETOR_SIZE];
    
	int size;
    bool ready;
    int state;
    int higher;
    int lower;
    int left_lower;

    MPI_Init(&argc, &argv);

	MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);
	MPI_Comm_size(MPI_COMM_WORLD, &max_rank);
	MPI_Status status;

    for (i=0 ; i<ARRAY_SIZE; i++)              /* init array with worst case for sorting */
        vetor[i] = ARRAY_SIZE-i;

    int new_SIZE = VETOR_SIZE/max_rank
    int new_vet[new_SIZE];

    new_vet = slice_vetor(vetor, VETOR_SIZE, max_rank, my_rank);
    // gero parte local do vetor (1/np avos do vetor global)


    ready = FALSE;

    while ( !ready )
    {

    // ordeno vetor local

        bs(new_SIZE, new_vet);
        higher = new_vetor[new_SIZE-1];
        lower = new_vetor[0];

        // verifico condição de parada

        // se não for np-1, mando o meu maior elemento para a direita
        if(my_rank != max_rank-1)
            MPI_Send(&higher, 1, MPI_INT, my_rank+1, 0, MPI_COMM_WORLD);

        // se não for 0, recebo o maior elemento da esquerda
        if(my_rank != 0)
            MPI_Recv(&left_lower, 1, MPI_INT, my_rank-1, MPI_ANY_TAG, MPI_COMM_WORLD, &status);

        // comparo se o meu menor elemento é maior do que o maior elemento recebido (se sim, estou ordenado em relação ao meu vizinho)
        if(lower > left_lower)
            state = 0;
        else
            state = 1;
        // compartilho o meu estado com todos os processos, fazendo um BCAST com cada processo como origem (NP vezes)
        for(int i = 0;i<max_rank;i++) 
            MPI_Bcast(&state, 1, MPI_INT, i, MPI_COMM_WORLD);
        // se todos estiverem ordenados com seus vizinhos, a ordenação do vetor global está pronta ( pronto = TRUE, break)

        // senão continuo

    // troco valores para convergir

        // se não for o 0, mando os menores valores do meu vetor para a esquerda

        // se não for np-1, recebo os menores valores da direita

            // ordeno estes valores com a parte mais alta do meu vetor local

            // devolvo os valores que recebi para a direita

        // se não for o 0, recebo de volta os maiores valores da esquerda

    }

    MPI_Finalize();
    

    #ifdef DEBUG
    printf("\nVetor: ");
    for (i=0 ; i<ARRAY_SIZE; i++)                              /* print sorted array */
        printf("[%03d] ", vetor[i]);
    #endif

    return 0;
}
