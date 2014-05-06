/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.domains;

/**
 *
 * @author duarteduarte
 */
public class Jogador extends DomainPojo {

    private int id;
    private String nome;
    private String dataNascimento;
    private String sexo;
    private int idEquipa;
    private Equipa equipa;

    public Jogador(String nome, String dataNascimento, String sexo, int idEquipa, Equipa equipa) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.idEquipa = idEquipa;
        this.equipa = equipa;
    }

    public Jogador() {
        this.id = -1;
        this.nome = null;
        this.dataNascimento = null;
        this.sexo = null;
        this.idEquipa = -1;
        this.equipa = null;
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

    public Equipa getEquipa() {
        return equipa;
    }

    public void setImagem(Equipa equipa) {
        this.equipa = equipa;
    }

    public int getIdEquipa() {
        return idEquipa;
    }

    public void setIdEquipa(int idEquipa) {
        this.idEquipa = idEquipa;
    }

    public void setEquipa(Equipa equipa) {
        this.equipa = equipa;
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
        final Jogador other = (Jogador) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
