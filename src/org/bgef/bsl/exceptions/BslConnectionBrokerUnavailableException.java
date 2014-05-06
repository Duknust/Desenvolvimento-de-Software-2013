/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.exceptions;

import org.bgef.dao.exceptions.GenericDAOException;

/**
 *
 * @author duarteduarte
 */
public class BslConnectionBrokerUnavailableException extends GenericBslException {

    public BslConnectionBrokerUnavailableException(String unable_to_load_a_connection_broker, GenericDAOException ex) {
    }
}
