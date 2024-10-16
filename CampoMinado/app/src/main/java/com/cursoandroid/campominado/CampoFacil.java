package com.cursoandroid.campominado;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CampoFacil extends AppCompatActivity {

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
    private TextView jogadas;
    private DateTimeFormatter sdf1;
    private LocalDateTime localDate;
    private TextView qtdBandeira;
    private TextView data;
    private Handler handler;
    private Runnable runnable;
    private TextView qtdDuvida;
    private Instant instantI;
    private Instant instantF;
    private Duration duration;
    private TextView tempo;
    private TextView teste;
    private int revelados;
    private int contarRevelado =0;
    private boolean perdeu;


    //agendador de tarefas
    //gregorian
    //data hora simpledateformat
    //contador de tempo

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campofacil);


        gridLayout = findViewById(R.id.matrizGridLayout3);
        bandeira = findViewById(R.id.botaoBandeira2Id);
        duvida = findViewById(R.id.botaoDuvida2Id);
        desmarcar = findViewById(R.id.desmarcarBotaoId);
        mediaPlayer = MediaPlayer.create(this, R.raw.explosao);
        jogadas = findViewById(R.id.quantidadeJogadasId);
        qtdBandeira = findViewById(R.id.qtdBandeiraId);
        qtdDuvida = findViewById(R.id.qtdDuvidaId);
        data = findViewById(R.id.dataId);
        tempo = findViewById(R.id.tempoId);






        int tam = 9;


        Campo campo = new Campo(tam, tam);
        campo.sorteiaNumeros(tam, tam);
        botoes = new ImageView[tam][tam];
        revelado = new boolean[tam][tam];
        bandeiras = new boolean[tam][tam];
        duvidas = new boolean[tam][tam];
        instantI = Instant.now();
        revelados = (revelado.length * revelado[0].length) - campo.getQuantidadeBombas();
        contarRevelado = 0;
        sdf1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        perdeu = false;
        handler = new Handler();

        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void run() {
                localDate = LocalDateTime.now();
                instantF = Instant.now();
                duration = Duration.between(instantI, instantF);

                String s = localDate.format(sdf1);
                data.setText(s);

                if(!ganhou() && !perdeu) {
                    tempo.setText(duration.toMinutes() + ":" + duration.toSeconds() % 60);
                    handler.postDelayed(runnable, 1000);
                }

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
                                    if(!bandeiras[finalI][finalJ]) {
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
                ImageView temp = new ImageView(CampoFacil.this);

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

                botoes[i][j].setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.S)
                    @Override
                    public void onClick(View view) {
                        if (!ganhou()) {
                            if (!revelado[linha][col]) {
                                if (matrizPopulada[linha][col] == 0) {
                                    revelarVazios(linha, col);
                                    contClick[0]++;
                                } else if (matrizPopulada[linha][col] == -1) {
                                    revelarBombas();
                                } else {
                                    mostrarBotoes(linha, col);
                                    contClick[0]++;
                                    contarRevelado++;
                                }
                                clicks[0] = String.valueOf(contClick[0]);
                                jogadas.setText(clicks[0]);

                            }
                        }

                    }

                });
            }
        }
    }



    public void revelarBombas() {
        for (int i = 0; i < matrizPopulada.length; i++) {
            for (int j = 0; j < matrizPopulada[0].length; j++) {
                if (matrizPopulada[i][j] == -1) {
                    botoes[i][j].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.mina));
                }
            }
        }
        desativarBotoes();
        mediaPlayer.start();
        Toast.makeText(getApplicationContext(), "Você perdeu", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"faltam "+ String.valueOf(revelados - contarRevelado), Toast.LENGTH_LONG).show();
    }

    public void mostrarBotoes(int linha, int col) {
        if (matrizPopulada[linha][col] == -1) {
            revelarBombas();
        } else if (matrizPopulada[linha][col] == 0) {
            revelarVazios(linha, col);
        }
        String x = String.valueOf(matrizPopulada[linha][col]);
        int op = Integer.parseInt(x);
        revelado[linha][col] = true;


        switch (op){
            case -1:
                revelarBombas();
                qtdDuvida.setText("0");
                qtdBandeira.setText("0");
                break;
            case 0:
                revelarVazios(linha, col);
                break;
            case 1:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num1));
                botoes[linha][col].setEnabled(false);
                break;
            case 2:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num2));
                botoes[linha][col].setEnabled(false);
                break;
            case 3:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num3));
                botoes[linha][col].setEnabled(false);
                break;
            case 4:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num4));
                botoes[linha][col].setEnabled(false);
                break;
            case 5:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num5));
                botoes[linha][col].setEnabled(false);
                break;
            case 6:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num6));
                botoes[linha][col].setEnabled(false);
                break;
            case 7:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num7));
                botoes[linha][col].setEnabled(false);
                break;
            case 8:
                botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.num8));
                botoes[linha][col].setEnabled(false);
                break;
        }


    }

    public void revelarVazios(int linha, int col) {
        if (linha >= 0 && linha < matrizPopulada.length && col >= 0 && col < matrizPopulada[0].length) {
            if (!revelado[linha][col]) {
                revelado[linha][col] = true;
                contarRevelado++;
                if (matrizPopulada[linha][col] == 0) {
                    botoes[linha][col].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vazio));
                    botoes[linha][col].setEnabled(false);
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            revelarVazios(i + linha, col + j);
                        }
                    }
                } else {
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
        qtdJ++;
        j  = String.valueOf(qtdJ);
        qtdDuvida.setText(d);
        jogadas.setText(j);
    }
    @RequiresApi(api = Build.VERSION_CODES.S)
    public boolean ganhou() {
        int faltam =0;
        faltam =(revelados - contarRevelado);
        if (faltam == 0) {
                inserirBD(data.getText().toString(), qtdBandeira.getText().toString(), jogadas.getText().toString(), qtdDuvida.getText().toString(), String.valueOf(duration.toMinutes()+":"+duration.toSeconds()));
                desativarBotoes();
                return true;
            }
        Log.v("nao ganhou", ""+ faltam);
        return false;
        }


    public void desativarBotoes(){
        for(int i=0; i<botoes.length; i++){
            for(int j=0; j< botoes[0].length; j++){
                botoes[i][j].setEnabled(false);
            }
        }
        bandeira.setEnabled(false);
        desmarcar.setEnabled(false);
        duvida.setEnabled(false);
    }


    public List<RankingClasse> lerBD() {
        List<RankingClasse> lista = new ArrayList<>();
        SQLiteDatabase dbRanking = null;
        Cursor cursor = null;

        try {
            dbRanking = openOrCreateDatabase("ranking", MODE_PRIVATE, null);
            String sql = "SELECT * FROM posicoes";
            cursor = dbRanking.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                do {
                    // Uso de @SuppressLint removido
                    String data = cursor.getString(cursor.getColumnIndex("data"));
                    String bandeiras = cursor.getString(cursor.getColumnIndex("bandeiras"));
                    String clicadas = cursor.getString(cursor.getColumnIndex("clicadas"));
                    String duvidas = cursor.getString(cursor.getColumnIndex("duvidas"));
                    String tempo = cursor.getString(cursor.getColumnIndex("tempo"));

                    // Validação dos dados
                    if (data != null && bandeiras != null && clicadas != null && duvidas != null && tempo != null) {
                        RankingClasse temp = new RankingClasse(data, bandeiras, clicadas, duvidas, tempo);
                        lista.add(temp);
                    } else {
                        Log.w("DBWarning", "Dados inválidos encontrados: data=" + data + ", bandeiras=" + bandeiras +
                                ", clicadas=" + clicadas + ", duvidas=" + duvidas + ", tempo=" + tempo);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DBError", "Erro ao ler o banco de dados", e);
        } finally {
            // Fechar o cursor e o banco de dados no bloco finally
            if (cursor != null) {
                cursor.close();
            }
            if (dbRanking != null) {
                dbRanking.close();
            }
        }
        return lista;
    }


    public void inserirBD(String data, String bandeiras, String clicadas, String duvidas, String tempo) {
        SQLiteDatabase dbRanking = null;
        try {
            dbRanking = openOrCreateDatabase("ranking", MODE_PRIVATE, null);
            dbRanking.execSQL("CREATE TABLE IF NOT EXISTS posicoes(posicoesId INTEGER PRIMARY KEY AUTOINCREMENT, data VARCHAR(50),bandeiras CHAR(2), clicadas CHAR(2), duvidas CHAR(2), tempo CHAR(20))");

            String sql = "INSERT INTO posicoes(data, bandeiras, clicadas, duvidas, tempo) VALUES (?, ?, ?, ?, ?)";
            SQLiteStatement stmt = dbRanking.compileStatement(sql);
            stmt.bindString(1, data);
            stmt.bindString(2, bandeiras);
            stmt.bindString(3, clicadas);
            stmt.bindString(4, duvidas);
            stmt.bindString(5, tempo);

            stmt.executeInsert();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbRanking != null) {
                dbRanking.close();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        perdeu = true;
    }
}






