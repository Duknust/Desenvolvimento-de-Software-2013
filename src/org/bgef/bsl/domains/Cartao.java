/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.domains;

/**
 *
 * @author duarteduarte
 */
public class Cartao extends DomainPojo {

    private int id;
    private int idJogador;
    private Jogador jogador;
    private int idJogo;
    private String tipo;

    public Cartao(int idJogador, Jogador jogador, int idJogo, String tipo) {
        this.idJogador = idJogador;
        this.jogador = jogador;
        this.idJogo = idJogo;
        this.tipo = tipo;
    }

    public Cartao() {
        this.id = -1;
        this.idJogador = -1;
        this.jogador = null;
        this.idJogo = -1;
        this.tipo = null;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdJogador() {
        return idJogador;
    }

    public void setIdJogador(int idJogador) {
        this.idJogador = idJogador;
    }

    public int getIdJogo() {
        return idJogo;
    }

    public void setIdJogo(int idJogo) {
        this.idJogo = idJogo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    @Override
    public String toString() {
        return this.getTipo() + " - " + this.idJogador;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cartao other = (Cartao) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
