#include <stdio.h>
#include "mpi.h"

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

int executar(int ** tab, int N, int col)
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
			sol += executar(tab, N, col + 1);

			// remove a rainha (backtracking)
			tab[i][col] = 0;
		}
	}

	return sol;
}


int main(int argc, char *argv[])
{

	int N = atoi(argv[1]);
	int my_rank;       // Identificador deste processo
	int proc_n;        // Numero de processos disparados pelo usuário na linha de comando (np)
	int message[N/(proc_n-1)];       // Buffer para as mensagens 
	int taskbag[N];     // Saco de Tarefas
	int sol = 0;
	int sol_message;


	MPI_Init(&argc , &argv);

	MPI_Comm_rank( MPI_COMM_WORLD, &my_rank );  // pega pega o numero do processo atual (rank)
	MPI_Comm_size( MPI_COMM_WORLD, &proc_n );  // pega informação do numero de processos (quantidade total)
	MPI_Status status;

	if (my_rank == 0){
		for(int i = 0; i< N; i++){
			taskbag[i] = i;
		}

		int aux = 0;
		for(int i = 1; i< proc_n; i++){
			for(int j = 0; i< sizeof(message)/4; i++){
				message[j+aux] = taskbag[j+aux];
			}
			MPI_Send(message, N/(proc_n-1), MPI_INT, i, 5, MPI_COMM_WORLD);
			aux += N/(proc_n-1);
		}

		for(int i = 0; i< N; i++){
			MPI_Recv(&sol_message, 1, MPI_INT, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
			sol += sol_message;
		}
		printf("\nEncontradas  %d solucoes!\n", sol);
	}else{
		// Papel do Escravo
		// MPI_Recv (message, 100, MPI_CHAR , source, tag, MPI_COMM_WORLD, &status);
		MPI_Recv(message, N/(proc_n-1), MPI_INT, 0, MPI_ANY_TAG, MPI_COMM_WORLD,&status);
		for(int i = 0;i < sizeof(message)/4;i++){
			int **tab = (int**) malloc(N  * sizeof(int *));

			for(int i = 0; i < N; ++i)
				tab[i] = (int*) malloc(N  * sizeof(int));
			
			for(int i = 0; i < N; i++)
				for(int j = 0; j < N; j++)
					tab[i][j] = 0;

			tab[message[i]][0] = 1;

			sol = executar(tab, N, 1);

			MPI_Send(&sol, 1, MPI_INT, 0, 1, MPI_COMM_WORLD); // envio trabalho saco[i] para escravo com id = i+1;
		}


	}
	MPI_Finalize();


}