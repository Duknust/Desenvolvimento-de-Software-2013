/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.domains;

/**
 *
 * @author duarteduarte
 */
public class Arbitro extends DomainPojo {

    private int id = -1;
    private String nome = null;
    private String dataNascimento = null;
    private String sexo = null;

    public Arbitro(String nome, String dataNascimento, String sexo) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
    }

    public Arbitro() {
        this.id = -1;
        this.nome = null;
        this.dataNascimento = null;
        this.sexo = null;
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

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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
        final Arbitro other = (Arbitro) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
