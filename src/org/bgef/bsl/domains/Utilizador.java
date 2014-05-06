/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.domains;

import java.util.Objects;

/**
 *
 * @author duarteduarte
 */
public class Utilizador extends DomainPojo {

    private int id;
    private String username;
    private String password;
    private int idArbitro;

    public Utilizador(String username, String password, int idArbitro) {
        this.username = username;
        this.password = password;
        this.idArbitro = idArbitro;
    }

    public Utilizador(String username, String password) {
        this.username = username;
        this.password = password;
        this.idArbitro = -1;
    }

    public Utilizador() {
        this.id = -1;
        this.username = null;
        this.password = null;
        this.idArbitro = -1;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdArbitro() {
        return idArbitro;
    }

    public void setIdArbitro(int idArbitro) {
        this.idArbitro = idArbitro;
    }

    @Override
    public String toString() {
        return this.username;
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
        final Utilizador other = (Utilizador) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        return true;
    }
}
