#include <stdio.h>
#include <omp.h>

#define N 20

int main() {
	int i, soma = 0;
	omp_set_num_threads(2);	
	#pragma omp parallel for default(shared) private(i)
	for(i=0; i<N; i++) {
		soma += i;
		// if(i == ((omp_get_thread_num()+1)*(N/omp_get_num_threads())-1))
		// 	printf("%d: %d\n", omp_get_thread_num(), soma);
        if(i%2==0)
		 	printf("%d: %d -> %d\n", omp_get_thread_num(), i, soma);
	}
	printf("%d\n", soma);
	return 0;
}