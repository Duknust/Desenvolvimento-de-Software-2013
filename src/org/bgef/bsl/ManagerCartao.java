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
import org.bgef.bsl.domains.Cartao;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.BslInsertionFailedException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.dao.CartaoDAO;
import org.bgef.dao.db.ConnectionBrokerFactory;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.GenericDAOException;

/**
 *
 * @author duarteduarte
 */
public class ManagerCartao extends GenericManager<Cartao> {

    private IConnectionBroker cb = null;
    private CartaoDAO cartaoDAO = null;

    public ManagerCartao(Properties props) throws BslConnectionBrokerUnavailableException {
        try {
            Properties connectionBrokerProperties = new Properties();
            connectionBrokerProperties.load(new FileInputStream(props.getProperty("bgef.connectionbroker.props")));
            this.cb = (props != null) ? ConnectionBrokerFactory.giveMeConnectionBrokerByName(props.getProperty("bgef.connectionbroker.impl"), connectionBrokerProperties) : ConnectionBrokerFactory.giveMeConnectionBrokerFromProperties();
            cartaoDAO = new CartaoDAO(cb);
        } catch (GenericDAOException ex) {
            throw new BslConnectionBrokerUnavailableException("Unable to load a connection broker", ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean insereNovo(Cartao object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.cartaoDAO.insert(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed insertion of Cartao", daoe);
        }
        return result;
    }

    @Override
    public boolean remove(Cartao object) throws GenericBslException {
        boolean result = false;
        try {
            result = this.cartaoDAO.delete(object);
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed remove of Cartao", daoe);
        }
        return result;
    }

    @Override
    public boolean update(Cartao object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.cartaoDAO.update(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed update of Cartao", daoe);
        }

        return result;
    }

    @Override
    protected boolean valida(Cartao object) {
        if (object.getIdJogador() < 0) {
            return false;
        }
        if (object.getIdJogo() < 0) {
            return false;
        }
        if (object.getTipo().length() > 10) {
            return false;
        }
        return true;
    }

    @Override
    public List<Cartao> procuraPorCaracteristicas(Cartao object) {
        List<Cartao> cartoes = new ArrayList<>();
        this.cartaoDAO = new CartaoDAO(cb);
        try {
            cartoes = cartaoDAO.getByCriteria(object);
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerCartao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cartoes;
    }

    @Override
    public List<Cartao> getAll() {
        List<Cartao> cartoes = null;
        cartaoDAO = new CartaoDAO(cb);
        try {
            cartoes = cartaoDAO.getAll();
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerCartao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cartoes;
    }

    @Override
    public Cartao procuraPorId(int id) {
        Cartao cartao = null;
        try {
            cartao = this.cartaoDAO.getById(id);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura por Id of Cartao", daoe);
        }
        return cartao;
    }
}
