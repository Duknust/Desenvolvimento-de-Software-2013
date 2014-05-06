/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.domains;

/**
 *
 * @author duarteduarte
 */
public class Escalao extends DomainPojo {

    private int id;
    private String nome;
    private int idadeMaxima;

    public Escalao(String nome, int idadeMaxima) {
        this.nome = nome;
        this.idadeMaxima = idadeMaxima;
    }

    public Escalao() {
        this.id = -1;
        this.nome = null;
        this.idadeMaxima = -1;
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

    public int getIdadeMaxima() {
        return idadeMaxima;
    }

    public void setIdadeMaxima(int idadeMaxima) {
        this.idadeMaxima = idadeMaxima;
    }

    @Override
    public String toString() {
        return this.nome + " - " + this.idadeMaxima;
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
        final Escalao other = (Escalao) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
