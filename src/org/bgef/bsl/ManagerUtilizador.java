/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bgef.bsl.domains.Utilizador;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.BslInsertionFailedException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.dao.UtilizadorDAO;
import org.bgef.dao.db.ConnectionBrokerFactory;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.GenericDAOException;

public class ManagerUtilizador extends GenericManager<Utilizador> {

    private IConnectionBroker cb = null;
    private UtilizadorDAO utilizadorDAO = null;

    public ManagerUtilizador(Properties props) throws BslConnectionBrokerUnavailableException {
        try {
            Properties connectionBrokerProperties = new Properties();
            connectionBrokerProperties.load(new FileInputStream(props.getProperty("bgef.connectionbroker.props")));
            this.cb = (props != null) ? ConnectionBrokerFactory.giveMeConnectionBrokerByName(props.getProperty("bgef.connectionbroker.impl"), connectionBrokerProperties) : ConnectionBrokerFactory.giveMeConnectionBrokerFromProperties();
            utilizadorDAO = new UtilizadorDAO(cb);
        } catch (GenericDAOException ex) {
            throw new BslConnectionBrokerUnavailableException("Unable to load a connection broker", ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean insereNovo(Utilizador object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.utilizadorDAO.insert(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed insertion of Utilizador", daoe);
        }
        return result;
    }

    @Override
    public boolean remove(Utilizador object) throws GenericBslException {
        boolean result = false;
        try {
            result = this.utilizadorDAO.delete(object);
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed remove of Utilizador", daoe);
        }
        return result;
    }

    @Override
    public boolean update(Utilizador object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.utilizadorDAO.update(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed update of Utilizador", daoe);
        }

        return result;
    }

    @Override
    protected boolean valida(Utilizador object) {
        if (object.getUsername().length() > 30) {
            return false;
        }
        if (object.getPassword().length() > 10) {
            return false;
        }
        return true;
    }

    @Override
    public List<Utilizador> procuraPorCaracteristicas(Utilizador object) {
        List<Utilizador> treinadors = new ArrayList<>();
        this.utilizadorDAO = new UtilizadorDAO(cb);
        try {
            treinadors = utilizadorDAO.getByCriteria(object);
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerUtilizador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return treinadors;
    }

    @Override
    public List<Utilizador> getAll() {
        List<Utilizador> users = null;
        utilizadorDAO = new UtilizadorDAO(cb);
        try {
            users = utilizadorDAO.getAll();
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerUtilizador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

    @Override
    public Utilizador procuraPorId(int id) throws GenericBslException {
        Utilizador utilizador = null;
        try {
            utilizador = this.utilizadorDAO.getById(id);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura por Id of Utilizador", daoe);
        }
        return utilizador;
    }
}
