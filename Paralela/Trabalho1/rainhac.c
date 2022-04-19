/*
	Solução do problema das n-rainhas (N-Queens Problem)
	Autor: Marcos Castro
	www.GeeksBR.com
*/

#include <omp.h>
#include <stdio.h>
#include <stdlib.h>

// conta a quantidade de soluções
int sol = 0;
int line = 50;
int colun = 50;

// função para mostrar o tabuleiro
void mostrarTabuleiro(int tab[line][colun], int N)
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
int seguro(int tab[line][colun], int N, int lin, int col)
{
	int i, j;

	// verifica se ocorre ataque na linha
	for(i = 0; i < N; i++)
	{
		if(tab[lin][i] == 1)
			return 0;
	}

	//verifica se ocorre ataque na coluna
	for(i = 0; i < N; i++)
	{
		if(tab[i][col] == 1)
			return 0;
	}

	// verifica se ocorre ataque na diagonal principal
	// acima e abaixo
	for(i = lin, j = col; i >= 0 && j >= 0; i--, j--)
	{
		if(tab[i][j] == 1)
			return 0;
	}
	for(i = lin, j = col; i < N && j < N; i++, j++)
	{
		if(tab[i][j] == 1)
			return 0;
	}

	// verifica se ocorre ataque na diagonal secundária
	// acima e abaixo
	for(i = lin, j = col; i >= 0 && j < N; i--, j++)
	{
		if(tab[i][j] == 1)
			return 0;
	}
	for(i = lin, j = col; i < N && j >= 0; i++, j--)
	{
		if(tab[i][j] == 1)
			return 0;
	}

	// se chegou aqui, então está seguro (retorna true)
	return 1;
}

/*
	função que resolve o problema
	retorna true se conseguiu resolver e false caso contrário
*/
void executar(int tab[line][colun], int N, int col)
{
	if(col == N)
	{
		printf("Solucao %d:\n\n", sol + 1);
		mostrarTabuleiro(tab, N);
		sol++;
		return;
	}

	for(int i = 0; i < N; i++)
	{
		// verifica se é seguro colocar a rainha naquela coluna
		if(seguro(tab, N, i, col))
		{
			// insere a rainha (marca com 1)
			tab[i][col] = 1;

			// chamada recursiva
			executar(tab, N, col + 1);

			// remove a rainha (backtracking)
			tab[i][col] = 0;
		}
	}
}

int main(int argc, char *argv[])
{
	// número de rainhas
	int N = 8;

	// tabuleiro (matriz)
	int tab[line][colun];

	// inserindo todas as linhas
	for(int i = 0; i < N; i++)
	{
        for(int j = 0; j < N; j++)
		    tab[i][j] = 0;
	}

	// imprime todas as soluções
	executar(tab, N, 0);

	// imprime a quantidade de soluções
	printf("\nEncontradas  %d solucoes!\n", sol);


	return 0;
}
