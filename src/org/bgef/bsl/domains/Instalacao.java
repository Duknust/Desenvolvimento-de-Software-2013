/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.domains;

/**
 *
 * @author duarteduarte
 */
public class Instalacao extends DomainPojo {

    private int id;
    private String nome;
    private String localidade;
    private int capacidade;

    public Instalacao(String nome, String localidade, int capacidade) {
        this.nome = nome;
        this.localidade = localidade;
        this.capacidade = capacidade;
    }

    public Instalacao() {
        this.id = -1;
        this.nome = null;
        this.localidade = null;
        this.capacidade = -1;
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

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    @Override
    public String toString() {
        return this.nome;
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
        final Instalacao other = (Instalacao) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
