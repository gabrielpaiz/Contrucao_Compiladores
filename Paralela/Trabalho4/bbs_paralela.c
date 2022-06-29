
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "mpi.h"

#define DEBUG 1            // comentar esta linha quando for medir tempo

void bs(int n, int * vetor){
    int c=0, d, troca, trocou =1;
    while ((c < (n-1)) & trocou ){
        trocou = 0;
        for (d = 0 ; d < n - c - 1; d++)
            if (vetor[d] > vetor[d+1]){
                troca      = vetor[d];
                vetor[d]   = vetor[d+1];
                vetor[d+1] = troca;
                trocou = 1;
                }
        c++;
        }
}

void printvet(int my_rank, int* vet, int size){
    printf("Vetor do Rank %d:", my_rank);
    for(int i = 0;i<size;i++)
	    printf("%d - ",vet[i]);
    printf("\n");
}

void interleaving(int vetor[], int tam, int *vetor_auxiliar){
        int i1, i2, i_aux;
        i1 = 0;
        i2 = tam / 2;

        for (i_aux = 0; i_aux < tam; i_aux++) {
                if (((vetor[i1] <= vetor[i2]) && (i1 < (tam / 2)))
                    || (i2 == tam))
                        vetor_auxiliar[i_aux] = vetor[i1++];
                else
                        vetor_auxiliar[i_aux] = vetor[i2++];
        }
}

int main(int argc, char **argv){
    int my_rank, max_rank;
    int VETOR_SIZE = atoi(argv[1]);
    int vetor[VETOR_SIZE];
    int brk, left_higher = 0;

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &max_rank);
    MPI_Status status;

    for (int i=0 ; i<VETOR_SIZE; i++)      
        vetor[i] = VETOR_SIZE-i;
    int new_SIZE = ceil(VETOR_SIZE/((float)max_rank));
    int troca = ceil(new_SIZE* (atoi(argv[2])/100.0)); 
    int new_vet[new_SIZE+troca];
    int vetor_temp[troca*2];
    for(int i =0;i<new_SIZE;i++)
	    new_vet[i] = vetor[new_SIZE*my_rank+i];

    while(1){
        bs(new_SIZE, new_vet);
 
        if(my_rank != max_rank-1)
            MPI_Send(&new_vet[new_SIZE-1], 1, MPI_INT, my_rank + 1, 0, MPI_COMM_WORLD);

        if(my_rank != 0)
            MPI_Recv(&left_higher, 1, MPI_INT, my_rank - 1, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
        int state[max_rank];
	    state[my_rank] = left_higher < new_vet[0] || my_rank == 0;

	    for(int i = 0 ;i<max_rank;i++)
            MPI_Bcast(&state[i], 1, MPI_INT, i, MPI_COMM_WORLD);
        for(int i = 0;i<max_rank;i++){
            if(!state[i]){
                brk = 0;
                break;
            }
            brk = 1;
        }
        if(brk)
            break;
       
        if(my_rank != 0)
            MPI_Send(new_vet, troca, MPI_INT, my_rank-1, 0, MPI_COMM_WORLD);
    
        if(my_rank != max_rank-1){
            MPI_Recv(&new_vet[new_SIZE], troca, MPI_INT, my_rank+1, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
            // printf("newvet | ");printvet(my_rank,&new_vet[new_SIZE-troca],troca*2);
            // interleaving( &new_vet[new_SIZE-troca], troca*2, vetor_temp);
            // printf("temp | ");printvet(my_rank,vetor_temp,troca*2);printf("\n ");
		    // for(int i=0; i<new_SIZE;i++)
			//     new_vet[i+troca] = vetor_temp[i];
            bs(new_SIZE, &new_vet[troca]);
            MPI_Send(&new_vet[new_SIZE], troca, MPI_INT, my_rank+1, 0, MPI_COMM_WORLD);
	    }

        if(my_rank != 0)
     	    MPI_Recv(&new_vet, troca, MPI_INT, my_rank -1, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
    }
    MPI_Finalize();
    

    #ifdef DEBUG
    printf("\nVetor: ");
    for (int i=0 ; i<new_SIZE; i++)                              /* print sorted array */
        printf("[%03d] ", new_vet[i]);
    #endif
    printf("\n");
    return 0;
}
