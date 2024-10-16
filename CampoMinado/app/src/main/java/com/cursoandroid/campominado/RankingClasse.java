package com.cursoandroid.campominado;

public class RankingClasse  implements Comparable<RankingClasse>{
    private String data;
    private String bandeiras;
    private String clicadas;
    private String duvidas;
    private String tempo;




    public RankingClasse(){

    }
    public RankingClasse(String data, String bandeiras,String clicadas, String duvidas, String tempo){
        this.data = data;
        this.bandeiras = bandeiras;
        this.clicadas = clicadas;
        this.duvidas = duvidas;
        this.tempo = tempo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBandeiras() {
        return bandeiras;
    }

    public void setBandeiras(String bandeiras) {
        this.bandeiras = bandeiras;
    }

    public String getClicadas() {
        return clicadas;
    }

    public void setClicadas(String clicadas) {
        this.clicadas = clicadas;
    }

    public String getDuvidas() {
        return duvidas;
    }

    public void setDuvidas(String duvidas) {
        this.duvidas = duvidas;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    @Override
    public int compareTo(RankingClasse rankingClasse) {
        return Integer.compare(parseTempo(this.tempo), parseTempo(rankingClasse.getTempo()));
    }
    private int parseTempo(String tempo) {
        String[] parts = tempo.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Formato de tempo inválido: " + tempo);
        }

        int minutos;
        int segundos;
        try {
            minutos = Integer.parseInt(parts[0]);
            segundos = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Tempo contém valores não numéricos: " + tempo, e);
        }

        return minutos * 60 + segundos;
    }
}
