#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "mpi.h"

int ta_ord(int *vet, int size){
	for(int i = 0; i< size-1; i++)
		if(vet[i] > vet[i+1]) return 0;
	return 1;
}

void bubbleSort(int n, int * vetor){
    int c=0, d, troca, trocou =1;

    while (c < (n-1) & trocou ){
        trocou = 0;
        for (d = 0 ; d < n - c - 1; d++)
            if (vetor[d] > vetor[d+1]) {
                troca      = vetor[d];
                vetor[d]   = vetor[d+1];
                vetor[d+1] = troca;
                trocou = 1;
            }
        c++;
    }
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

void bubbleSort_parallel_MPI(int *vetor, int size, int *vetor_temp, int level, int max_rank, int my_rank, MPI_Status status){
	int helper_rank = my_rank + pow(2,level);
	if(helper_rank >= max_rank){
		bubbleSort(size, vetor);
	} else{
		// send second half of array
		MPI_Send(&vetor[size/2], size - size/2, MPI_INT, helper_rank, 0, MPI_COMM_WORLD); // mando metade final 
		// sort first half
		bubbleSort_parallel_MPI(vetor,size/2,vetor_temp,level+1,max_rank,my_rank,status);
		// receive a second half sorted 
		MPI_Recv(&vetor[size/2], size - size/2, MPI_INT, helper_rank, MPI_ANY_TAG, MPI_COMM_WORLD, &status);    
        	// merge two sorted sub-arrays
		
        	interleaving(vetor, size, vetor_temp);
		for(int i = 0; i<size;i++)
			vetor[i] = vetor_temp[i];
		
	}
}

int main(int argc, char **argv){

    	int my_rank;
    	int max_rank; //proc_n
	int VETOR_SIZE = 1000000;
	int size;
    	MPI_Init(&argc, &argv);

	MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);
	MPI_Comm_size(MPI_COMM_WORLD, &max_rank);
	MPI_Status status;

	int vetor[VETOR_SIZE];
	int vetor_temp[VETOR_SIZE];
	if(my_rank == 0) {
		size = VETOR_SIZE;
		for(int i = 0;i<VETOR_SIZE;i++)
			vetor[i] = size-i;
		int level = 0;
		bubbleSort_parallel_MPI(vetor,size, vetor_temp, level, max_rank, my_rank ,status);
	}else {
		//run_helper_MPI(my_rank,...)
		// probe MPI for a message from parent rank ... todo
		MPI_Recv(vetor, VETOR_SIZE, MPI_INT, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status); // não sou a raiz, tenho pai 
		MPI_Get_count(&status, MPI_INT, &size); // descubro tamanho da mensagem recebida
    		int parent_rank = status.MPI_SOURCE;
		int level = 0;
		while(pow(2,level) <= my_rank) level++;
		//send sorted array to parent process
		bubbleSort_parallel_MPI(vetor, size, vetor_temp, level, max_rank, my_rank, status);
		MPI_Send(vetor, size, MPI_INT, parent_rank, 0, MPI_COMM_WORLD);
	}
	//array is sorted
	if(my_rank == 0){
		if(ta_ord)
			printf("ok!\n");
			
	}

	MPI_Finalize();
    return 0;
}
