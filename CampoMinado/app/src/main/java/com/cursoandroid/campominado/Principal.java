package com.cursoandroid.campominado;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Principal extends AppCompatActivity {
    private ImageView facil;
    private ImageView medio;
    private ImageView dificil;
    private ImageView personalizado;
    private Button ranking;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        facil  = findViewById(R.id.facilBotaoId);
        medio = findViewById(R.id.medioBotaoId);
        dificil = findViewById(R.id.dificilBotaoId);
        personalizado = findViewById(R.id.personalizadoBotaoId);
        ranking = findViewById(R.id.rankingId);


        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Principal.this, Ranking.class);
                startActivity(intent);
            }
        });


        facil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Principal.this, CampoFacil.class);
                startActivity(intent);
            }
        });
        medio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Principal.this, CampoMedio.class);
                startActivity(intent);
            }
        });
        dificil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Principal.this, CampoDificil.class);
                startActivity(intent);
            }
        });
        personalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Principal.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}