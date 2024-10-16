package com.cursoandroid.campominado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MeuAdaptador extends RecyclerView.Adapter<MeuAdaptador.MinhaHolder>{
    Context context;
    List<RankingClasse> rankings;
    public MeuAdaptador(List<RankingClasse> rankings, Context context) {
        this.rankings = rankings;
        this.context = context;
    }

    @NonNull
    @Override
    public MinhaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MinhaHolder(LayoutInflater.from(context).inflate(R.layout.ranking_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MinhaHolder holder, int position) {
        holder.bandeiras.setText(String.valueOf(rankings.get(position).getBandeiras()));
        holder.clicadas.setText(String.valueOf(rankings.get(position).getClicadas()));
        holder.tempo.setText(rankings.get(position).getTempo());
        holder.data.setText(rankings.get(position).getData());
        holder.duvidas.setText(String.valueOf(rankings.get(position).getDuvidas()));
    }

    @Override
    public int getItemCount() {
        return rankings.size();
    }

    public static class MinhaHolder extends RecyclerView.ViewHolder{
        public TextView data;
        public TextView tempo, clicadas,bandeiras,duvidas;

        public MinhaHolder(@NonNull View itemView) {
            super(itemView);
            data = itemView.findViewById(R.id.DataRankingId);
            tempo = itemView.findViewById(R.id.tempoRankingId);
            clicadas = itemView.findViewById(R.id.clickadasRankingId);
            bandeiras = itemView.findViewById(R.id.bandeirasRankingId);
            duvidas = itemView.findViewById(R.id.duvidasRankingId);

        }
    }
}
