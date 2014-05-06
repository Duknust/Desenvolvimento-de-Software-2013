/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.domains;

/**
 *
 * @author duarteduarte
 */
public class Estatisticas {

    private int idEquipa;
    private String nomeEquipa;
    private int golosMarcados;
    private int golosSofridos;
    private int cartoesAmarelos;
    private int cartoesVermelhos;
    private int totalGolos;
    private int totalCartoesAmarelos;
    private int totalCartoesVermelhos;

    public Estatisticas(int idEquipa) {
        this.idEquipa = idEquipa;
        this.nomeEquipa = null;
        this.golosMarcados = 0;
        this.golosSofridos = 0;
        this.cartoesAmarelos = 0;
        this.cartoesVermelhos = 0;
        this.totalGolos = 0;
        this.totalCartoesAmarelos = 0;
        this.totalCartoesVermelhos = 0;
    }

    public Estatisticas(int idEquipa, int golosMarcados, int golosSofridos, int cartoesAmarelos, int cartoesVermelhos) {
        this.idEquipa = -1;
        this.golosMarcados = golosMarcados;
        this.golosSofridos = golosSofridos;
        this.cartoesAmarelos = cartoesAmarelos;
        this.cartoesVermelhos = cartoesVermelhos;
    }

    public void actualizaEstatisticas(int golosMarcados, int golosSofridos, int cartoesAmarelos, int cartoesVermelhos, int totalGolos, int totalCartoesAmarelos, int totalCartoesVermelhos) {
        this.golosMarcados += golosMarcados;
        this.golosSofridos += golosSofridos;
        this.cartoesAmarelos += cartoesAmarelos;
        this.cartoesVermelhos += cartoesVermelhos;
        this.totalGolos = totalGolos;
        this.totalCartoesAmarelos = totalCartoesAmarelos;
        this.totalCartoesVermelhos = totalCartoesVermelhos;
    }

    public int getGolosMarcados() {
        return golosMarcados;
    }

    public void setGolosMarcados(int golosMarcados) {
        this.golosMarcados = golosMarcados;
    }

    public int getGolosSofridos() {
        return golosSofridos;
    }

    public void setGolosSofridos(int golosSofridos) {
        this.golosSofridos = golosSofridos;
    }

    public int getCartoesAmarelos() {
        return cartoesAmarelos;
    }

    public void setCartoesAmarelos(int cartoesAmarelos) {
        this.cartoesAmarelos = cartoesAmarelos;
    }

    public int getCartoesVermelhos() {
        return cartoesVermelhos;
    }

    public void setCartoesVermelhos(int cartoesVermelhos) {
        this.cartoesVermelhos = cartoesVermelhos;
    }

    public int getIdEquipa() {
        return idEquipa;
    }

    public void setIdEquipa(int idEquipa) {
        this.idEquipa = idEquipa;
    }

    public String getNomeEquipa() {
        return nomeEquipa;
    }

    public void setNomeEquipa(String nomeEquipa) {
        this.nomeEquipa = nomeEquipa;
    }

    public int getTotalGolos() {
        return totalGolos;
    }

    public void setTotalGolos(int totalGolos) {
        this.totalGolos = totalGolos;
    }

    public int getTotalCartoesAmarelos() {
        return totalCartoesAmarelos;
    }

    public void setTotalCartoesAmarelos(int totalCartoesAmarelos) {
        this.totalCartoesAmarelos = totalCartoesAmarelos;
    }

    public int getTotalCartoesVermelhos() {
        return totalCartoesVermelhos;
    }

    public void setTotalCartoesVermelhos(int totalCartoesVermelhos) {
        this.totalCartoesVermelhos = totalCartoesVermelhos;
    }
}
