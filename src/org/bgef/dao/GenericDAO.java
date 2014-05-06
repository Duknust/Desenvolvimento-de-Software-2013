/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import javax.sql.DataSource;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.bsl.domains.DomainPojo;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public abstract class GenericDAO<T extends DomainPojo> {

    protected IConnectionBroker cb;

    /*public GenericDAO(IConnectionBroker cb) {
     this.cb = cb;
     this.cb.init();
     }*/
    protected DataSource executeStatement(Connection conn, String sqlStatement) throws DatabaseConnectionDAOException, StatementExecuteDAOException {

        // Prepare a statement to cleanup the employees table
        try {
            Statement stmt = conn.createStatement();
            try {
                stmt.execute(sqlStatement);
            } finally {
                // Close the statement
                stmt.close();
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Unable to execute statement", sqle);
        }

        return null;
    }

    public abstract List<T> getAll() throws GenericDAOException;

    public abstract T getById(int id) throws GenericDAOException;

    public abstract boolean insert(T object) throws GenericDAOException;

    public abstract boolean delete(T object) throws GenericDAOException;

    public abstract boolean update(T object) throws GenericDAOException;

    public abstract boolean exists(T object) throws GenericDAOException;

    public abstract List<T> getByCriteria(T object) throws GenericDAOException;

    public abstract Collection<T> values();
}
