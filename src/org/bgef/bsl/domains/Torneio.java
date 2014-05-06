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
public class Torneio extends DomainPojo {

    private int id;
    private String nome;
    private int epoca;
    private List<Equipa> equipas;
    private int estado;
    private int mostrar;
    private int idEscalao;
    private Escalao escalao;

    public Torneio(String nome, int epoca, List<Equipa> equipas, int estado, int mostrar, int idEscalao, Escalao escalao) {
        this.nome = nome;
        this.epoca = epoca;
        this.equipas = equipas;
        this.estado = estado;
        this.mostrar = mostrar;
        this.idEscalao = idEscalao;
        this.escalao = escalao;
    }

    public Torneio() {
        this.id = -1;
        this.nome = null;
        this.epoca = -1;
        this.equipas = null;
        this.estado = -1;
        this.mostrar = -1;
        this.idEscalao = -1;
        this.escalao = null;
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

    public List<Equipa> getEquipas() {
        return equipas;
    }

    public void setEquipas(List<Equipa> equipas) {
        this.equipas = equipas;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getMostrar() {
        return mostrar;
    }

    public void setMostrar(int mostrar) {
        this.mostrar = mostrar;
    }

    public int getEpoca() {
        return epoca;
    }

    public void setEpoca(int epoca) {
        this.epoca = epoca;
    }

    public int getIdEscalao() {
        return idEscalao;
    }

    public void setIdEscalao(int idEscalao) {
        this.idEscalao = idEscalao;
    }

    public Escalao getEscalao() {
        return escalao;
    }

    public void setEscalao(Escalao escalao) {
        this.escalao = escalao;
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
        final Torneio other = (Torneio) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
