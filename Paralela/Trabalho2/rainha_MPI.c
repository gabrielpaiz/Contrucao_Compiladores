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
int message[200];       // Buffer para as mensagens 
int taskbag[N];     // Saco de Tarefas

MPI_Init();

MPI_Comm_rank( &my_rank );  // pega pega o numero do processo atual (rank)
MPI_Comm_size( &proc_n );  // pega informação do numero de processos (quantidade total)

if (my_rank == 0){
    // Papel do Mestre
}else{
    // Papel do Escravo
    MPI_Recv(&message, 0);
    for(int i = 0;i < N/proc_n;i++){
        int sol = 0;
        int **tab = (int**) malloc(N  * sizeof(int *));

        for(int i = 0; i < N; ++i)
            tab[i] = (int*) malloc(N  * sizeof(int));
        
        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++)
                tab[i][j] = 0;


        tab[message[i]][0] = 1;

        sol = executar(tab, N, 1)

        message = sol;

        MPI_Send(&message, 0); // envio trabalho saco[i] para escravo com id = i+1;
    }
}



}