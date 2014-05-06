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
import org.bgef.bsl.domains.Escalao;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.BslInsertionFailedException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.dao.EscalaoDAO;
import org.bgef.dao.db.ConnectionBrokerFactory;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.GenericDAOException;

/**
 *
 * @author duarteduarte
 */
public class ManagerEscalao extends GenericManager<Escalao> {

    private IConnectionBroker cb = null;
    private EscalaoDAO escalaoDAO = null;

    public ManagerEscalao(Properties props) throws BslConnectionBrokerUnavailableException {
        try {
            Properties connectionBrokerProperties = new Properties();
            connectionBrokerProperties.load(new FileInputStream(props.getProperty("bgef.connectionbroker.props")));
            this.cb = (props != null) ? ConnectionBrokerFactory.giveMeConnectionBrokerByName(props.getProperty("bgef.connectionbroker.impl"), connectionBrokerProperties) : ConnectionBrokerFactory.giveMeConnectionBrokerFromProperties();
            escalaoDAO = new EscalaoDAO(cb);
        } catch (GenericDAOException ex) {
            throw new BslConnectionBrokerUnavailableException("Unable to load a connection broker", ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean insereNovo(Escalao object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.escalaoDAO.insert(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed insertion of Escalao", daoe);
        }
        return result;
    }

    @Override
    public boolean remove(Escalao object) throws GenericBslException {
        boolean result = false;
        try {
            result = this.escalaoDAO.delete(object);
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed remove of Escalao", daoe);
        }
        return result;
    }

    @Override
    public boolean update(Escalao object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.escalaoDAO.update(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed update of Escalao", daoe);
        }

        return result;
    }

    @Override
    protected boolean valida(Escalao object) {
        if (object.getNome().length() > 30) {
            return false;
        }
        if (object.getIdadeMaxima() < 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<Escalao> procuraPorCaracteristicas(Escalao object) {
        List<Escalao> escalaos = new ArrayList<>();
        this.escalaoDAO = new EscalaoDAO(cb);
        try {
            escalaos = escalaoDAO.getByCriteria(object);
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerEscalao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return escalaos;
    }

    @Override
    public List<Escalao> getAll() {
        List<Escalao> escalaos = null;
        escalaoDAO = new EscalaoDAO(cb);
        try {
            escalaos = escalaoDAO.getAll();
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerEscalao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return escalaos;
    }

    @Override
    public Escalao procuraPorId(int id) {
        Escalao escalao = null;
        try {
            escalao = this.escalaoDAO.getById(id);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura por Id of Escalao", daoe);
        }
        return escalao;
    }
}
