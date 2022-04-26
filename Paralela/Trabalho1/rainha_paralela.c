/*
	Solução do problema das n-rainhas (N-Queens Problem)
	Autor: Marcos Castro
	www.GeeksBR.com
*/

#include <omp.h>
#include <stdio.h>
#include <stdlib.h>

// função para mostrar o tabuleiro
void mostrarTabuleiro(int ** tab, int N)
{
	for(int i = 0; i < N; i++)
	{
		for(int j = 0; j < N; j++)
		{
			if(tab[i][j] == 1)
				printf("R\t");
			else
				printf("-\t");
		}
		printf("\n\n");
	}
	printf("\n");
}

// verifica se é seguro colocar a rainha numa determinada coluna
// isso poderia ser feito com menos código, mas assim ficou mais didático
int seguro(int ** tab, int N, int lin, int col)
{
	int i, j;
	int ret = 1;
	for(i = 0; i < N; i++)
	{
		if(tab[lin][i] == 1)
			ret = 0;
	}

	//verifica se ocorre ataque na coluna
	for(i = 0; i < N; i++)
	{
		if(tab[i][col] == 1)
			ret = 0;
	}

	// verifica se ocorre ataque na diagonal principal
	// acima e abaixo
	for(i = lin, j = col; i >= 0 && j >= 0; i--, j--)
	{
		if(tab[i][j] == 1)
			ret = 0;
	}
	for(i = lin, j = col; i < N && j < N; i++, j++)
	{
		if(tab[i][j] == 1)
			ret = 0;
	}

	// verifica se ocorre ataque na diagonal secundária
	// acima e abaixo
	for(i = lin, j = col; i >= 0 && j < N; i--, j++)
	{
		if(tab[i][j] == 1)
			ret = 0;
	}
	for(i = lin, j = col; i < N && j >= 0; i++, j--)
	{
		if(tab[i][j] == 1)
			ret = 0;
	}

	// se chegou aqui, então está seguro (retorna true)
	return ret;
}

/*
	função que resolve o problema
	retorna true se conseguiu resolver e false caso contrário
*/
int executar(int N)
{
	int sol;
	omp_set_num_threads(4);
	#pragma omp parallel for schedule(dynamic) reduction(+:sol)
	for(int i = 0; i < N; i++)
	{
		int **tab = (int**) malloc(N  * sizeof(int *));
		for(int i = 0; i < N; ++i)
        	tab[i] = (int*) malloc(N  * sizeof(int));
		
		for(int i = 0; i < N; i++)
        	for(int j = 0; j < N; j++)
		    	tab[i][j] = 0;

		// verifica se é seguro colocar a rainha naquela coluna
			// insere a rainha (marca com 1)
		tab[i][0] = 1;

		// chamada recursiva
		sol += executar_rec(tab, N, 1);

		// remove a rainha (backtracking)
		tab[i][0] = 0;
		printf("Acabou a Folha %d do BackTraking!\n", i);
	}
	return sol;
}

int executar_rec(int ** tab, int N, int col)
{
	if(col == N)
	{
		//printf("Solucao %d:\n\n", sol + 1);
		//mostrarTabuleiro(tab, N);
		return 1;
	}
	int sol = 0;
	for(int i = 0; i < N; i++)
	{
		// verifica se é seguro colocar a rainha naquela coluna
		if(seguro(tab, N, i, col))
		{
			// insere a rainha (marca com 1)
			tab[i][col] = 1;

			// chamada recursiva
			sol += executar_rec(tab, N, col + 1);

			// remove a rainha (backtracking)
			tab[i][col] = 0;
		}
	}

	return sol;
}

int main(int argc, char *argv[])
{
	// número de rainhas
	int N = atoi(argv[1]);
	int sol;
	double starttime, stoptime;

	starttime = omp_get_wtime(); 
	// imprime todas as soluções
	sol = executar(N);
	stoptime = omp_get_wtime();

	// imprime a quantidade de soluções
	printf("\nEncontradas  %d solucoes!\n", sol);

	printf("Tempo de execucao: %3.2f segundos\n", stoptime-starttime);

	return 0;
}
