package com.cursoandroid.campominado;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CampoMedio extends Activity {

    private ImageView[][] botoes;
    private GridLayout gridLayout;
    private Button jogar;
    private int[][] matrizPopulada;
    private boolean[][] revelado;
    private ImageView duvida;
    private ImageView bandeira;
    private boolean[][] bandeiras;
    private Button desmarcar;
    private boolean[][] duvidas;
    private MediaPlayer mediaPlayer;
    private DateTimeFormatter sdf1;
    private LocalDateTime localDate;
    private TextView qtdBandeira;
    private TextView data;
    private Handler handler;
    private Runnable runnable;
    private TextView qtdDuvida;
    private TextView jogadas;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campo_medio);


        gridLayout = (GridLayout) findViewById(R.id.matrizGridLayout3);
        bandeira = (ImageView) findViewById(R.id.botaoBandeira3Id);
        duvida = (ImageView) findViewById(R.id.botaoDuvida3Id);
        desmarcar = (Button) findViewById(R.id.botaoDesmarcar2Id);
        mediaPlayer = MediaPlayer.create(this, R.raw.explosao);
        data = (TextView) findViewById(R.id.data2Id);
        jogadas = (TextView) findViewById(R.id.quantidadeJogadas2Id);
        qtdBandeira = (TextView) findViewById(R.id.qtdBandeira2Id);
        qtdDuvida = (TextView) findViewById(R.id.qtdDuvida2Id);

        int tam = 14;
        int cont = 0;


        Campo campo = new Campo(tam, tam);
        campo.sorteiaNumeros(tam, tam);

        botoes = new ImageView[tam][tam];
        revelado = new boolean[tam][tam];
        bandeiras = new boolean[tam][tam];
        duvidas = new boolean[tam][tam];
        sdf1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                localDate = LocalDateTime.now();
                String s = localDate.format(sdf1);
                data.setText(s);
                handler.postDelayed(runnable, 1000);

            }
        };
        runnable.run();



        campo.colocarBombas();
        campo.colocarVisinhos();
        matrizPopulada = campo.retornarMatriz();


        gridLayout.removeAllViews();
        gridLayout.setRowCount(tam);
        gridLayout.setColumnCount(tam);


        bandeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < tam; i++) {
                    for (int j = 0; j < tam; j++) {
                        int finalI = i;
                        int finalJ = j;


                        botoes[finalI][finalJ].setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                if (!revelado[finalI][finalJ]) {
                                    if(!bandeiras[finalJ][finalJ]) {
                                        colocarBandeira(finalI, finalJ);
                                    }else{
                                        desmarcar(finalI, finalJ);
                                    }

                                }
                            }
                        });
                    }
                }
            }







        });

        desmarcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<tam; i++){
                    for(int j=0; j<tam; j++){
                        botoes[i][j].setEnabled(true);
                        int finalI = i;
                        int finalJ = j;
                        botoes[i][j].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                desmarcar(finalI, finalJ);
                            }
                        });
                    }
                }
            }
        });

        duvida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<tam; i++){
                    for(int j=0; j<tam; j++){
                        int finalI = i;
                        int finalJ = j;
                        botoes[i][j].setEnabled(true);
                        botoes[i][j].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                colocarDuvida(finalI, finalJ);
                            }
                        });
                    }
                }
            }
        });

        final int[] contClick = {0};
        final String[] clicks = {(String) jogadas.getText()};
        contClick[0] = Integer.parseInt(clicks[0]);


        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                ImageView temp = new ImageView(CampoMedio.this);

                temp.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.semclicar));



                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.setGravity(0);
                params.setMargins(1,1,1,1);

                params.rowSpec = GridLayout.spec(i, 1f);
                params.columnSpec = GridLayout.spec(j, 1f);


                temp.setLayoutParams(params);
                gridLayout.addView(temp);


                final int linha = i;
                final int col = j;
                botoes[i][j] = temp;

                temp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (matrizPopulada[linha][col] == 0) {
                            revelarVazios(linha, col);
                            contClick[0]++;
                        }
                        if (matrizPopulada[linha][col] == -1) {
                            revelarBombas(linha, col);
                        } else {
                            mostrarBotoes(linha, col);
                            contClick[0] ++;
                        }
                        clicks[0] = String.valueOf(contClick[0]);
                        jogadas.setText(clicks[0]);
                    }
                });
            }
        }
    }

    public void revelarBombas(int linha, int col) {
        for (int i = 0; i < matrizPopulada.length; i++) {
            for (int j = 0; j < matrizPopulada[0].length; j++) {
                if (matrizPopulada[i][j] == -1) {
                    botoes[i][j].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.mina));
                }
            }
        }
        for (int i = 0; i < matrizPopulada.length; i++) {
            for (int j = 0; j < matrizPopulada[0].length; j++) {

                botoes[i][j].setEnabled(false);
                bandeira.setEnabled(false);
                duvida.setEnabled(false);
                desmarcar.setEnabled(false);

            }
        }
        mediaPlayer.start();
        Toast.makeText(getApplicationContext(), "Você perdeu", Toast.LENGTH_SHORT).show();
    }

    public void mostrarBotoes(int linha, int col) {
        if (matrizPopulada[linha][col] == -1) {
            revelarBombas(linha, col);
        } else if (matrizPopulada[linha][col] == 0) {
            revelarVazios(linha, col);
        }
        String x = String.valueOf(matrizPopulada[linha][col]);
        int op = Integer.parseInt(x);
        switch (op){
            case -1:
                revelarBombas(linha, col);
                qtdDuvida.setText("0");
                qtdBandeira.setText("0");
                break;
            case 0:
                revelarVazios(linha, col);
                break;
            case 1:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num1));
                revelado[linha][col] = true;
                break;
            case 2:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num2));
                revelado[linha][col] = true;
                break;
            case 3:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num3));
                revelado[linha][col] = true;
                break;
            case 4:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num4));
                revelado[linha][col] = true;
                break;
            case 5:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num5));
                revelado[linha][col] = true;
                break;
            case 6:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num6));
                revelado[linha][col] = true;
                break;
            case 7:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num7));
                revelado[linha][col] = true;
                break;
            case 8:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num8));
                revelado[linha][col] = true;
                break;



        }

    }

    public void revelarVazios(int linha, int col) {
        if (linha >= 0 && linha < matrizPopulada.length && col >= 0 && col < matrizPopulada[0].length) {
            if(!revelado[linha][col]) {
                revelado[linha][col] = true;
                if (matrizPopulada[linha][col] == 0) {
                    botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vazio));
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            revelarVazios(i + linha, col + j);
                        }
                    }
                }
                else {
                    mostrarBotoes(linha, col);
                }
            }

        }
    }

    public void desmarcar(int linha, int col){
        botoes[linha][col].setEnabled(true);
        String b = (String) qtdBandeira.getText();
        int qtdB = Integer.parseInt(b);
        String j = (String) jogadas.getText();
        int qtdJ = Integer.parseInt(j);

        if(!revelado[linha][col]) {
            if (bandeiras[linha][col]) {
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.semclicar));
                bandeiras[linha][col] = false;
                qtdB--;
                b = String.valueOf(qtdB);

            } else {
                mostrarBotoes(linha, col);
            }
            qtdBandeira.setText(b);
            qtdJ++;
            j = String.valueOf(qtdJ);
            jogadas.setText(j);
        }
    }
    public void colocarBandeira(int linha, int col){
        botoes[linha][col].setEnabled(false);
        String b = (String) qtdBandeira.getText();
        int qtdB = Integer.parseInt(b);
        String j = (String) jogadas.getText();
        int qtdJ = Integer.parseInt(j);

        if (!revelado[linha][col]) {
            if(!bandeiras[linha][col]) {
                botoes[linha][col].setEnabled(false);
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bandeira));
                bandeiras[linha][col] = true;
                qtdB++;
                b = String.valueOf(qtdB);
                qtdBandeira.setText(b);

            }else{
                desmarcar(linha, col);
            }
            qtdJ++;
            j = String.valueOf(qtdJ);
            jogadas.setText(j);
        }

    }
    public void colocarDuvida(int linha, int col){
        botoes[linha][col].setEnabled(true);
        String d = (String) qtdDuvida.getText();
        int qtdD = Integer.parseInt(d);
        String j = (String) jogadas.getText();
        int qtdJ = Integer.parseInt(j);
        if(!revelado[linha][col]) {
            if (!duvidas[linha][col]){
                botoes[linha][col].setEnabled(true);
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.duvida));
                duvidas[linha][col] = true;
                qtdD++;
                d = String.valueOf(qtdD);
            }else{
                desmarcar(linha, col);
                qtdD--;
                d = String.valueOf(qtdD);

            }
        }
        qtdDuvida.setText(d);
        qtdJ++;
        j  = String.valueOf(qtdJ);
        jogadas.setText(j);
    }

}




