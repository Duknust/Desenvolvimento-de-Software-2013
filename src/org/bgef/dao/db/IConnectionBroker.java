/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.dao.db;

import java.sql.Connection;
import java.util.Properties;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;

/**
 *
 * @author duarteduarte
 */
public interface IConnectionBroker {

    Connection getConnection() throws DatabaseConnectionDAOException;

    boolean destroy();

    boolean init();

    boolean setProperties(Properties ps);
}
