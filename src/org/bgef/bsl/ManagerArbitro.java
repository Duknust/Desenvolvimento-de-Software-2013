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
import org.bgef.bsl.domains.Arbitro;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.BslInsertionFailedException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.dao.ArbitroDAO;
import org.bgef.dao.db.ConnectionBrokerFactory;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.GenericDAOException;

/**
 *
 * @author duarteduarte
 */
public class ManagerArbitro extends GenericManager<Arbitro> {

    private IConnectionBroker cb = null;
    private ArbitroDAO arbitroDAO = null;

    public ManagerArbitro(Properties props) throws BslConnectionBrokerUnavailableException {
        try {
            Properties connectionBrokerProperties = new Properties();
            connectionBrokerProperties.load(new FileInputStream(props.getProperty("bgef.connectionbroker.props")));
            this.cb = (props != null) ? ConnectionBrokerFactory.giveMeConnectionBrokerByName(props.getProperty("bgef.connectionbroker.impl"), connectionBrokerProperties) : ConnectionBrokerFactory.giveMeConnectionBrokerFromProperties();
            arbitroDAO = new ArbitroDAO(cb);
        } catch (GenericDAOException ex) {
            throw new BslConnectionBrokerUnavailableException("Unable to load a connection broker", ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean insereNovo(Arbitro object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.arbitroDAO.insert(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed insertion of Arbitro", daoe);
        }
        return result;
    }

    @Override
    public boolean remove(Arbitro object) throws GenericBslException {
        boolean result = false;
        try {
            result = this.arbitroDAO.delete(object);
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed remove of Arbitro", daoe);
        }
        return result;
    }

    @Override
    public boolean update(Arbitro object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.arbitroDAO.update(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed update of Arbitro", daoe);
        }

        return result;
    }

    @Override
    protected boolean valida(Arbitro object) {
        if (object.getNome().length() > 30) {
            return false;
        }
        if (object.getDataNascimento().length() > 10) {
            return false;
        }
        if (object.getSexo().length() > 10) {
            return false;
        }
        return true;
    }

    @Override
    public List<Arbitro> procuraPorCaracteristicas(Arbitro object) {
        List<Arbitro> arbitros = new ArrayList<>();

        try {
            arbitros = arbitroDAO.getByCriteria(object);
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }

        return arbitros;
    }

    @Override
    public List<Arbitro> getAll() {
        List<Arbitro> arbitros = null;

        try {
            arbitros = arbitroDAO.getAll();
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arbitros;
    }

    @Override
    public Arbitro procuraPorId(int id) throws GenericBslException {
        Arbitro campeonatoTorneio = null;
        try {
            campeonatoTorneio = this.arbitroDAO.getById(id);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura por Id of Arbitro", daoe);
        }
        return campeonatoTorneio;
    }
}
