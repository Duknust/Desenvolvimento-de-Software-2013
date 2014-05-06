/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.domains;

import java.util.List;

/**
 *
 * @author duarteduarte
 */
public class Equipa extends DomainPojo {

    private int id;
    private String nome;
    private List<Jogador> jogadores;
    private int idEscalao;
    private Escalao escalao;
    private int idEscola;
    private Escola escola;

    public Equipa(String nome, List<Jogador> jogadores, int idEscalao, Escalao escalao, int idEscola, Escola escola) {
        this.nome = nome;
        this.jogadores = jogadores;
        this.idEscalao = idEscalao;
        this.escalao = escalao;
        this.idEscola = idEscola;
        this.escola = escola;
    }

    public Equipa() {
        this.id = -1;
        this.nome = null;
        this.idEscalao = -1;
        this.escalao = null;
        this.idEscola = -1;
        this.escola = null;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    public Escalao getEscalao() {
        return escalao;
    }

    public void setEscalao(Escalao escalao) {
        this.escalao = escalao;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public int getIdEscalao() {
        return idEscalao;
    }

    public void setIdEscalao(int idEscalao) {
        this.idEscalao = idEscalao;
    }

    public int getIdEscola() {
        return idEscola;
    }

    public void setIdEscola(int idEscola) {
        this.idEscola = idEscola;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Equipa other = (Equipa) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
