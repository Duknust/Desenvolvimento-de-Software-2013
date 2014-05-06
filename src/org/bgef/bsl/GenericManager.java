/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl;

import java.util.List;
import org.bgef.bsl.exceptions.GenericBslException;

/**
 *
 * @author duarteduarte
 */
public abstract class GenericManager<T> {

    public GenericManager() {
    }

    public abstract boolean insereNovo(T object) throws GenericBslException;

    public abstract boolean remove(T object) throws GenericBslException;

    public abstract boolean update(T object) throws GenericBslException;

    protected abstract boolean valida(T object);

    public abstract List<T> procuraPorCaracteristicas(T object);

    public abstract List<T> getAll();

    public abstract T procuraPorId(int id) throws GenericBslException;
}
