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
public interface IGenericManager<T> {

    public boolean insereNovo(T object) throws GenericBslException;

    public boolean remove(T object) throws GenericBslException;

    public boolean update(T object) throws GenericBslException;

    public List<T> procuraPorCaracteristicas(T object);

    public List<T> getAll();

    public T procuraPorId(int id) throws GenericBslException;
}
