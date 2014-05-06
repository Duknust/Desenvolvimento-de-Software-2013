/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.domains;

/**
 *
 * @author duarteduarte
 */
public class Jogo extends DomainPojo implements Comparable<Jogo> {

    private int id;
    private int idEquipaCasa;
    private Equipa equipaCasa;
    private int idEquipaFora;
    private Equipa equipaFora;
    private int idRelatorio;
    private Relatorio relatorio;
    private int semana;
    private int ano;
    private int idCampeonato;
    private Campeonato campeonato;
    private int idTorneio;
    private Torneio torneio;
    private int fase;

    public Jogo(int idEquipaCasa, Equipa equipaCasa, int idEquipaFora, Equipa equipaFora, int idRelatorio, Relatorio relatorio, int semana, int ano, int idCampeonato, Campeonato campeonato, int idTorneio, Torneio torneio, int fase) {
        this.idEquipaCasa = idEquipaCasa;
        this.equipaCasa = equipaCasa;
        this.idEquipaFora = idEquipaFora;
        this.equipaFora = equipaFora;
        this.idRelatorio = idRelatorio;
        this.relatorio = relatorio;
        this.semana = semana;
        this.ano = ano;
        this.idCampeonato = idCampeonato;
        this.campeonato = campeonato;
        this.idTorneio = idTorneio;
        this.torneio = torneio;
        this.fase = fase;
    }

    public Jogo() {
        this.id = -1;
        this.idEquipaCasa = -1;
        this.equipaCasa = null;
        this.idEquipaFora = -1;
        this.equipaFora = null;
        this.idRelatorio = -1;
        this.relatorio = null;
        this.semana = -1;
        this.ano = -1;
        this.idCampeonato = -1;
        this.campeonato = null;
        this.idTorneio = -1;
        this.torneio = null;
        this.fase = -1;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Equipa getEquipaCasa() {
        return equipaCasa;
    }

    public void setEquipaCasa(Equipa equipaCasa) {
        this.equipaCasa = equipaCasa;
    }

    public Equipa getEquipaFora() {
        return equipaFora;
    }

    public void setEquipaFora(Equipa equipaFora) {
        this.equipaFora = equipaFora;
    }

    public Relatorio getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(Relatorio relatorio) {
        this.relatorio = relatorio;
    }

    public int getIdEquipaCasa() {
        return idEquipaCasa;
    }

    public void setIdEquipaCasa(int idEquipaCasa) {
        this.idEquipaCasa = idEquipaCasa;
    }

    public int getIdEquipaFora() {
        return idEquipaFora;
    }

    public void setIdEquipaFora(int idEquipaFora) {
        this.idEquipaFora = idEquipaFora;
    }

    public int getIdRelatorio() {
        return idRelatorio;
    }

    public void setIdRelatorio(int idRelatorio) {
        this.idRelatorio = idRelatorio;
    }

    public int getSemana() {
        return semana;
    }

    public void setSemana(int semana) {
        this.semana = semana;
    }

    public int getIdCampeonato() {
        return idCampeonato;
    }

    public void setIdCampeonato(int idCampeonato) {
        this.idCampeonato = idCampeonato;
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public int getIdTorneio() {
        return idTorneio;
    }

    public void setIdTorneio(int idTorneio) {
        this.idTorneio = idTorneio;
    }

    public Torneio getTorneio() {
        return torneio;
    }

    public void setTorneio(Torneio torneio) {
        this.torneio = torneio;
    }

    public int getFase() {
        return fase;
    }

    public void setFase(int fase) {
        this.fase = fase;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    @Override
    public String toString() {
        if (equipaCasa != null && equipaFora != null) {
            return this.equipaCasa.toString() + " x " + this.equipaFora.toString() + "(" + this.semana + "/" + this.ano + ")";
        } else {
            return " ---- x ---- " + "(" + this.semana + "/" + this.ano + ")";
        }
    }

    @Override
    public int compareTo(Jogo t) {
        if (t.getFase() > this.getFase()) {
            return -1;

        } else if (t.getFase() < this.getFase()) {
            return 1;
        } else {
            return 0;
        }

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
        final Jogo other = (Jogo) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
