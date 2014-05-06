/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.domains;

/**
 *
 * @author duarteduarte
 */
public class CampeonatoTorneio extends DomainPojo {

    int id;
    private String nome;
    private int epoca;
    private int idCampeonato1;
    private Campeonato campeonato1;
    private int idCampeonato2;
    private Campeonato campeonato2;
    private int idCampeonato3;
    private Campeonato campeonato3;
    private int idCampeonato4;
    private Campeonato campeonato4;
    private int idTorneio;
    private Torneio faseEliminatoria;
    private int estado;

    public CampeonatoTorneio(String nome, int epoca, int idCampeonato1, int idCampeonato2, int idCampeonato3, int idCampeonato4, Campeonato campeonato1, Campeonato campeonato2, Campeonato campeonato3, Campeonato campeonato4, int idTorneio, Torneio faseEliminatoria, int estado) {
        this.nome = nome;
        this.epoca = epoca;
        this.idCampeonato1 = idCampeonato1;
        this.idCampeonato2 = idCampeonato2;
        this.idCampeonato3 = idCampeonato3;
        this.idCampeonato4 = idCampeonato4;
        this.campeonato1 = campeonato1;
        this.campeonato2 = campeonato2;
        this.campeonato3 = campeonato3;
        this.campeonato4 = campeonato4;
        this.idTorneio = idTorneio;
        this.faseEliminatoria = faseEliminatoria;
        this.estado = estado;
    }

    public CampeonatoTorneio() {
        this.id = -1;
        this.epoca = -1;
        this.nome = null;
        this.idCampeonato1 = -1;
        this.idCampeonato2 = -1;
        this.idCampeonato3 = -1;
        this.idCampeonato4 = -1;
        this.campeonato1 = null;
        this.campeonato2 = null;
        this.campeonato3 = null;
        this.campeonato4 = null;
        this.idTorneio = -1;
        this.faseEliminatoria = null;
        this.estado = -1;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Torneio getFaseEliminatoria() {
        return faseEliminatoria;
    }

    public void setFaseEliminatoria(Torneio faseEliminatoria) {
        this.faseEliminatoria = faseEliminatoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdTorneio() {
        return idTorneio;
    }

    public void setIdTorneio(int idTorneio) {
        this.idTorneio = idTorneio;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getEpoca() {
        return epoca;
    }

    public void setEpoca(int epoca) {
        this.epoca = epoca;
    }

    public void setFaseEliminatoria(int faseEliminatoria) {
        this.idTorneio = faseEliminatoria;
    }

    public int getIdCampeonato1() {
        return idCampeonato1;
    }

    public void setIdCampeonato1(int idCampeonato1) {
        this.idCampeonato1 = idCampeonato1;
    }

    public Campeonato getCampeonato1() {
        return campeonato1;
    }

    public void setCampeonato1(Campeonato campeonato1) {
        this.campeonato1 = campeonato1;
    }

    public int getIdCampeonato2() {
        return idCampeonato2;
    }

    public void setIdCampeonato2(int idCampeonato2) {
        this.idCampeonato2 = idCampeonato2;
    }

    public Campeonato getCampeonato2() {
        return campeonato2;
    }

    public void setCampeonato2(Campeonato campeonato2) {
        this.campeonato2 = campeonato2;
    }

    public int getIdCampeonato3() {
        return idCampeonato3;
    }

    public void setIdCampeonato3(int idCampeonato3) {
        this.idCampeonato3 = idCampeonato3;
    }

    public Campeonato getCampeonato3() {
        return campeonato3;
    }

    public void setCampeonato3(Campeonato campeonato3) {
        this.campeonato3 = campeonato3;
    }

    public int getIdCampeonato4() {
        return idCampeonato4;
    }

    public void setIdCampeonato4(int idCampeonato4) {
        this.idCampeonato4 = idCampeonato4;
    }

    public Campeonato getCampeonato4() {
        return campeonato4;
    }

    public void setCampeonato4(Campeonato campeonato4) {
        this.campeonato4 = campeonato4;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final CampeonatoTorneio other = (CampeonatoTorneio) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
