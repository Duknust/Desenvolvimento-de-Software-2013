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
public class Escola extends DomainPojo {

    private int id;
    private String nome;
    private List<Equipa> equipas; //deixou de existir aqui
    private String localidade;
    private int idInstalacao;
    private Instalacao instalacao;

    public Escola(String nome, List<Equipa> equipas, String localidade, int idInstalacao, Instalacao instalacao) {
        this.nome = nome;
        this.equipas = equipas;
        this.localidade = localidade;
        this.idInstalacao = idInstalacao;
        this.instalacao = instalacao;
    }

    public Escola() {
        this.id = -1;
        this.nome = null;
        this.equipas = null;
        this.localidade = null;
        this.idInstalacao = -1;
        this.instalacao = null;
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

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public Instalacao getInstalacao() {
        return instalacao;
    }

    public void setInstalacao(Instalacao instalacao) {
        this.instalacao = instalacao;
    }

    public int getIdInstalacao() {
        return idInstalacao;
    }

    public void setIdInstalacao(int idInstalacao) {
        this.idInstalacao = idInstalacao;
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
        final Escola other = (Escola) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
