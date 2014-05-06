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
import org.bgef.bsl.domains.Instalacao;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.BslInsertionFailedException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.dao.InstalacaoDAO;
import org.bgef.dao.db.ConnectionBrokerFactory;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.GenericDAOException;

/**
 *
 * @author duarteduarte
 */
public class ManagerInstalacao extends GenericManager<Instalacao> {

    private IConnectionBroker cb = null;
    private InstalacaoDAO instalacaoDAO = null;

    public ManagerInstalacao(Properties props) throws BslConnectionBrokerUnavailableException {
        try {
            Properties connectionBrokerProperties = new Properties();
            connectionBrokerProperties.load(new FileInputStream(props.getProperty("bgef.connectionbroker.props")));
            this.cb = (props != null) ? ConnectionBrokerFactory.giveMeConnectionBrokerByName(props.getProperty("bgef.connectionbroker.impl"), connectionBrokerProperties) : ConnectionBrokerFactory.giveMeConnectionBrokerFromProperties();
            instalacaoDAO = new InstalacaoDAO(cb);
        } catch (GenericDAOException ex) {
            throw new BslConnectionBrokerUnavailableException("Unable to load a connection broker", ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean insereNovo(Instalacao object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.instalacaoDAO.insert(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed insertion of Instalacao", daoe);
        }
        return result;
    }

    @Override
    public boolean remove(Instalacao object) throws GenericBslException {
        boolean result = false;
        try {
            result = this.instalacaoDAO.delete(object);
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed remove of Instalacao", daoe);
        }
        return result;
    }

    @Override
    public boolean update(Instalacao object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.instalacaoDAO.update(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed update of Instalacao", daoe);
        }

        return result;
    }

    @Override
    protected boolean valida(Instalacao object) {
        if (object.getNome().length() > 30) {
            return false;
        }
        if (object.getLocalidade().length() > 20) {
            return false;
        }
        if (object.getCapacidade() < 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<Instalacao> procuraPorCaracteristicas(Instalacao object) {
        List<Instalacao> instalacoes = new ArrayList<>();
        this.instalacaoDAO = new InstalacaoDAO(cb);
        try {
            instalacoes = instalacaoDAO.getByCriteria(object);
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerInstalacao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return instalacoes;
    }

    @Override
    public List<Instalacao> getAll() {
        List<Instalacao> instalacoes = null;
        instalacaoDAO = new InstalacaoDAO(cb);
        try {
            instalacoes = instalacaoDAO.getAll();
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerInstalacao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return instalacoes;
    }

    @Override
    public Instalacao procuraPorId(int id) {
        Instalacao instalacao = null;
        try {
            instalacao = this.instalacaoDAO.getById(id);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura por Id of Instalacao", daoe);
        }
        return instalacao;
    }
}
