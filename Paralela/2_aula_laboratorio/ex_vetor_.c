#include <stdio.h>
#include <omp.h>

#define N 16

int main(int argc, char **argv) {

	double starttime, stoptime;

	int i, j, aux;
    int tam = 100000;
    int vet[tam];
	
	starttime = omp_get_wtime(); 
	omp_set_num_threads(4);
//	#pragma omp parallel for schedule(static)
//	#pragma omp parallel for schedule(static, 2)
//	#pragma omp parallel for schedule(dynamic)
//	#pragma omp parallel for schedule(dynamic, 2)
	#pragma omp parallel for schedule(guided)
	for(i=0; i<tam; i++) {
		aux = 0;
		for(j=0; j<i+1; j++){
			aux += j;
		}
		vet[i] = aux;
	}

	stoptime = omp_get_wtime();
	// for(i = 0; i<tam;i++){
	// 	printf("%d ", vet[i]);
	// }
	printf("Tempo de execucao: %3.2f segundos\n", stoptime-starttime);
}