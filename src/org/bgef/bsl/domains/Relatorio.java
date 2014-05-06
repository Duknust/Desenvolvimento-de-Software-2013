/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.domains;

/**
 *
 * @author duarteduarte
 */
public class Relatorio extends DomainPojo {

    private int id;
    private int cartoesAmarelos;
    private int cartoesVermelhos;
    private int nFaltas;
    private int golosCasa;
    private int golosFora;
    private int idArbitro;

    public Relatorio(int cartoesAmarelos, int cartoesVermelhos, int nFaltas, int golosCasa, int golosFora, int idArbitro) {
        this.cartoesAmarelos = cartoesAmarelos;
        this.cartoesVermelhos = cartoesVermelhos;
        this.nFaltas = nFaltas;
        this.golosCasa = golosCasa;
        this.golosFora = golosFora;
        this.idArbitro = idArbitro;
    }

    public Relatorio() {
        this.id = -1;
        this.cartoesAmarelos = -1;
        this.cartoesVermelhos = -1;
        this.nFaltas = -1;
        this.golosCasa = -1;
        this.golosFora = -1;
        this.idArbitro = -1;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCartoesAmarelos() {
        return cartoesAmarelos;
    }

    public void setCartoesAmarelos(int cartoesAmarelos) {
        this.cartoesAmarelos = cartoesAmarelos;
    }

    public int getCartoesVermelhos() {
        return cartoesVermelhos;
    }

    public void setCartoesVermelhos(int cartoesVermelhos) {
        this.cartoesVermelhos = cartoesVermelhos;
    }

    public int getnFaltas() {
        return nFaltas;
    }

    public void setnFaltas(int nFaltas) {
        this.nFaltas = nFaltas;
    }

    public int getGolosCasa() {
        return golosCasa;
    }

    public void setGolosCasa(int golosCasa) {
        this.golosCasa = golosCasa;
    }

    public int getGolosFora() {
        return golosFora;
    }

    public void setGolosFora(int golosFora) {
        this.golosFora = golosFora;
    }

    public int getIdArbitro() {
        return idArbitro;
    }

    public void setIdArbitro(int idArbitro) {
        this.idArbitro = idArbitro;
    }

    @Override
    public String toString() {
        return this.idArbitro + " - " + this.id;
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
        final Relatorio other = (Relatorio) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
