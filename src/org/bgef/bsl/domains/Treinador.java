/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.domains;

/**
 *
 * @author duarteduarte
 */
public class Treinador extends DomainPojo {

    private int id;
    private String nome;
    private String dataNascimento;
    private String nacionalidade;
    private String sexo;
    private int idEquipa;
    private Equipa equipa;

    public Treinador(String nome, String dataNascimento, String nacionalidade, String sexo, int idEquipa, Equipa equipa) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.nacionalidade = nacionalidade;
        this.sexo = sexo;
        this.idEquipa = idEquipa;
        this.equipa = equipa;
    }

    public Treinador() {
        this.id = -1;
        this.nome = null;
        this.dataNascimento = null;
        this.nacionalidade = null;
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

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getIdEquipa() {
        return idEquipa;
    }

    public void setIdEquipa(int idEquipa) {
        this.idEquipa = idEquipa;
    }

    public Equipa getEquipa() {
        return equipa;
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
        final Treinador other = (Treinador) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
