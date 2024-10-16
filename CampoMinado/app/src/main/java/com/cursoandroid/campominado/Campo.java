package com.cursoandroid.campominado;

import java.util.Random;

public class Campo {
    private int linhas;
    private int colunas;
    private int quantidadeBombas;
    private int[][] campo;
    private int[] bombasSorteadas;

    Random gerador = new Random();

    public Campo() {
    }

    public Campo(int n, int m) {
        linhas = n;
        colunas = m;
        quantidadeBombas = (int) (n * m * 0.15);
        campo = new int[n][m];
        bombasSorteadas = new int[quantidadeBombas];
    }

    public Campo(int n, int m, int quantidadeBombas) {
        linhas = n;
        colunas = m;
        this.quantidadeBombas = quantidadeBombas;
        campo = new int[linhas][colunas];
        bombasSorteadas = new int[quantidadeBombas];
    }

    public int getQuantidadeBombas() {
        return quantidadeBombas;
    }

    public int getLinhas() {
        return linhas;
    }

    public void setLinhas(int linhas) {
        this.linhas = linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public void setColunas(int colunas) {
        this.colunas = colunas;
    }

    public void sorteiaNumeros(int x, int y) {
        // Inicializa o contador de bombas sorteadas
        int count = 0;

        // Limpa a lista de bombas sorteadas
        for (int i = 0; i < bombasSorteadas.length; i++) {
            bombasSorteadas[i] = -1;
        }

        // Sorteia as bombas
        while (count < quantidadeBombas) {
            int num = gerador.nextInt(linhas * colunas);
            if (num != x * colunas + y && !verificaNumeros(num)) { // Verifica se o número não está na posição clicada
                bombasSorteadas[count] = num;
                count++;
            }
        }
    }

    public boolean verificaNumeros(int n) {
        for (int i = 0; i < bombasSorteadas.length; i++) {
            if (n == bombasSorteadas[i]) {
                return true;
            }
        }
        return false;
    }

    public void colocarBombas() {
        for (int k = 0; k < bombasSorteadas.length; k++) {
            int num = bombasSorteadas[k];
            int i = num / colunas;  // Calcula a linha
            int j = num % colunas;  // Calcula a coluna
            campo[i][j] = -1; // Marca a bomba
        }
    }

    public void colocarVisinhos() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (campo[i][j] != -1) { // Se não é uma bomba
                    int cont = 0;
                    for (int l = -1; l <= 1; l++) {
                        for (int p = -1; p <= 1; p++) {
                            int ni = i + l;
                            int nj = j + p;
                            if (ni >= 0 && ni < linhas && nj >= 0 && nj < colunas && campo[ni][nj] == -1) {
                                cont++;
                            }
                        }
                    }
                    campo[i][j] = cont; // Marca o número de bombas vizinhas
                }
            }
        }
    }

    public int[][] retornarMatriz() {
        return campo;
    }
}
