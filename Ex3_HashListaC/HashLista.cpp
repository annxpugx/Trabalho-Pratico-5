#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include <err.h>

#define MAXTAM 5
#define bool short
#define true 1
#define false 0

#define TAM_MAX_LINHA 250

bool isFim(char *s)
{
    return (strlen(s) >= 3 && s[0] == 'F' &&
            s[1] == 'I' && s[2] == 'M');
}

char *replaceChar(char *string, char toSearch, char toReplace)
{
    char *charPtr = strchr(string, toSearch);
    if (charPtr != NULL)
        *charPtr = toReplace;
    return charPtr;
}

void lerLinha(char *string, int tamanhoMaximo, FILE *arquivo)
{
    fgets(string, tamanhoMaximo, arquivo);
    replaceChar(string, '\r', '\0');
    replaceChar(string, '\n', '\0');
}

typedef struct Jogador {
    int id;
    int altura;
    int peso;
    int anoNascimento;
    char nome[100];
    char universidade[100];
    char cidadeNascimento[100];
    char estadoNascimento[100];
} Jogador;

typedef struct CelulaDupla {
	Jogador elemento;        // Elemento inserido na celula.
	struct CelulaDupla* prox; // Aponta a celula prox.
    struct CelulaDupla* ant;  // Aponta a celula anterior.
} CelulaDupla;

void inserirNaoInformado(char *linha, char *novaLinha)
{
    int tam = strlen(linha);
    for (int i = 0; i <= tam; i++, linha++)
    {
        *novaLinha++ = *linha;
        if (*linha == ',' && (*(linha + 1) == ',' || *(linha + 1) == '\0'))
        {
            strcpy(novaLinha, "nao informado");
            novaLinha += strlen("nao informado");
        }
    }
}

void tirarQuebraDeLinha(char linha[])
{
    int tam = strlen(linha);

    if (linha[tam - 2] == '\r' && linha[tam - 1] == '\n') // Linha do Windows
        linha[tam - 2] = '\0';                            // Apaga a linha

    else if (linha[tam - 1] == '\r' || linha[tam - 1] == '\n') // Mac ou Linux
        linha[tam - 1] = '\0';                                 // Apaga a linha
}

void setJogador(Jogador *jogador, char linha[])
{
    char novaLinha[TAM_MAX_LINHA];
    tirarQuebraDeLinha(linha);
    inserirNaoInformado(linha, novaLinha);

    jogador->id = atoi(strtok(novaLinha, ","));
    strcpy(jogador->nome, strtok(NULL, ","));
    jogador->altura = atoi(strtok(NULL, ","));
    jogador->peso = atoi(strtok(NULL, ","));
    strcpy(jogador->universidade, strtok(NULL, ","));
    jogador->anoNascimento = atoi(strtok(NULL, ","));
    strcpy(jogador->cidadeNascimento, strtok(NULL, ","));
    strcpy(jogador->estadoNascimento, strtok(NULL, ","));
}

void ler(char linhas_corrigidas[][TAM_MAX_LINHA])
{
    FILE *players;
    //abrindo o arquivo
    players = fopen("/tmp/players.csv", "r");

    char linhas[4000][TAM_MAX_LINHA];

    int i = 0;
    lerLinha(linhas[0], TAM_MAX_LINHA, players);
    do
    {
        lerLinha(linhas[i++], TAM_MAX_LINHA, players);
    } while (!feof(players));
    i--;

    for (int i = 0; i < 4000; i++)
    {
        inserirNaoInformado(linhas[i], linhas_corrigidas[i]);
    }
}

// CELULA =======================================================================

typedef struct Celula {
	char elemento[100];        // Elemento inserido na celula.
	struct Celula* prox; // Aponta a celula prox.
} Celula;

Celula* novaCelula(char elemento[100]) {
   Celula* nova = (Celula*) malloc(sizeof(Celula));
   strcpy(nova->elemento, elemento);
   nova->prox = NULL;
   return nova;
}

// HASH =========================================================================

Celula *tabela [25];

int hash(int altura){
    return (int) altura % 25;
}

void inserir(Jogador *j){

    int x = hash(j->altura);
    if(tabela[x] == NULL){
        tabela[x] = novaCelula(j->nome);
    } else {
        Celula *i = new Celula();
        for(i = tabela[x]; i->prox != NULL; i = i->prox);
        i->prox = novaCelula(j->nome);
    }
}

bool pesquisar(char nome[100]){
    bool resp = false;
    for(int i = 0; i < 25; i++){
        for(Celula *j = tabela[i]; j != NULL; j = j->prox){
            if(strcmp(j->elemento, nome) == 0){
                resp = true;
                i = 25;
            }
        }
    }
    return resp;
}


int main(int argc, char **argv)
{

    char entrada[1000][10];
    int numEntrada = 0;
    do
    {
        lerLinha(entrada[numEntrada], 10, stdin);
    } while (isFim(entrada[numEntrada++]) == false); // pegar primeiros ids
    numEntrada--;

    int entrada_inteiro[1000];

    for (int i = 0; i < 1000; i++)
    {
        sscanf(entrada[i], "%d", &entrada_inteiro[i]); // transformação para inteiros
    }

    char saida[4000][TAM_MAX_LINHA];

    ler(saida); // leitura do arquivo completo

    for (int i = 0; i < numEntrada; i++)
    {
        Jogador *j = (Jogador*) malloc (sizeof(Jogador));
        setJogador(j, saida[entrada_inteiro[i]]); // criação dos jogadores e inserção na lista
        inserir(j);
    }

    char *linha = (char*) malloc (100 * sizeof(char));
    lerLinha(linha, 100, stdin);

    for (int i = 0; isFim(linha) == false; i++)
    {
        if(pesquisar(linha)){
            printf("%s SIM\n", linha);
        }else{
            printf("%s NAO\n", linha);
        }
        lerLinha(linha, 100, stdin);
    }

    // caminharPre();

}