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
import org.bgef.bsl.domains.Relatorio;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.BslInsertionFailedException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.dao.RelatorioDAO;
import org.bgef.dao.db.ConnectionBrokerFactory;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.GenericDAOException;

/**
 *
 * @author duarteduarte
 */
public class ManagerRelatorio extends GenericManager<Relatorio> {

    private IConnectionBroker cb = null;
    private RelatorioDAO relatorioDAO = null;

    public ManagerRelatorio(Properties props) throws BslConnectionBrokerUnavailableException {
        try {
            Properties connectionBrokerProperties = new Properties();
            connectionBrokerProperties.load(new FileInputStream(props.getProperty("bgef.connectionbroker.props")));
            this.cb = (props != null) ? ConnectionBrokerFactory.giveMeConnectionBrokerByName(props.getProperty("bgef.connectionbroker.impl"), connectionBrokerProperties) : ConnectionBrokerFactory.giveMeConnectionBrokerFromProperties();
            relatorioDAO = new RelatorioDAO(cb);
        } catch (GenericDAOException ex) {
            throw new BslConnectionBrokerUnavailableException("Unable to load a connection broker", ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean insereNovo(Relatorio object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.relatorioDAO.insert(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed insertion of Relatorio", daoe);
        }
        return result;
    }

    @Override
    public boolean remove(Relatorio object) throws GenericBslException {
        boolean result = false;
        try {
            result = this.relatorioDAO.delete(object);
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed remove of Relatorio", daoe);
        }
        return result;
    }

    @Override
    public boolean update(Relatorio object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.relatorioDAO.update(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed update of Relatorio", daoe);
        }

        return result;
    }

    @Override
    protected boolean valida(Relatorio object) {
        if (object.getCartoesAmarelos() < 0) {
            return false;
        }
        if (object.getCartoesVermelhos() < 0) {
            return false;
        }
        if (object.getnFaltas() < 0) {
            return false;
        }
        if (object.getGolosCasa() < 0) {
            return false;
        }
        if (object.getGolosFora() < 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<Relatorio> procuraPorCaracteristicas(Relatorio object) {
        List<Relatorio> relatorios = new ArrayList<>();
        this.relatorioDAO = new RelatorioDAO(cb);
        try {
            relatorios = relatorioDAO.getByCriteria(object);
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }

        return relatorios;
    }

    @Override
    public List<Relatorio> getAll() {
        List<Relatorio> relatorios = null;
        relatorioDAO = new RelatorioDAO(cb);
        try {
            relatorios = relatorioDAO.getAll();
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return relatorios;
    }

    @Override
    public Relatorio procuraPorId(int id) {
        Relatorio relatorio = null;
        try {
            relatorio = this.relatorioDAO.getById(id);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura por Id of Relatorio", daoe);
        }
        return relatorio;
    }
}
