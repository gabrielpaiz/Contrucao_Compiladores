
#include <stdlib.h>
#include <stdio.h>
#include <omp.h>

int main (int argc, const char * argv[]) 
{
	const int intervalo = 5000;
	double starttime, stoptime;

	int i,j,k;
	int prime;
	int total;
	
	starttime = omp_get_wtime(); 

    omp_set_num_threads(4);
	
	#pragma omp parallel for schedule(dynamic) default(none) private(i, j, k, prime, total, intervalo)
	
	for (k = 1 ; k <= intervalo ; k++)
	{ 
		total = 0;
			
		for ( i = 2; i <= k ; i++ )
		{
		    prime = 1;
		        for ( j = 2; j < i; j++ )
		        {
			        if ( i % j == 0 )
			        {
				      prime = 0;
				      break;
			        }
		        }
		    total = total + prime;
	    }
	    if(k%500==0){
	    	printf("O numero de primos do intervalo [1-%d] e %d\n", k, total);
		}
	}
	
	stoptime = omp_get_wtime();
	printf("Tempo de execucao: %3.2f segundos\n", stoptime-starttime);

	return(0);
}