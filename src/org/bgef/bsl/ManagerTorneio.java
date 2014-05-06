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
import org.bgef.bsl.domains.Torneio;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.BslInsertionFailedException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.dao.ArbitroDAO;
import org.bgef.dao.EquipaDAO;
import org.bgef.dao.TorneioDAO;
import org.bgef.dao.db.ConnectionBrokerFactory;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class ManagerTorneio extends GenericManager<Torneio> {

    private IConnectionBroker cb = null;
    private TorneioDAO torneioDAO = null;

    public ManagerTorneio(Properties props) throws BslConnectionBrokerUnavailableException {
        try {
            Properties connectionBrokerProperties = new Properties();
            connectionBrokerProperties.load(new FileInputStream(props.getProperty("bgef.connectionbroker.props")));
            this.cb = (props != null) ? ConnectionBrokerFactory.giveMeConnectionBrokerByName(props.getProperty("bgef.connectionbroker.impl"), connectionBrokerProperties) : ConnectionBrokerFactory.giveMeConnectionBrokerFromProperties();
            torneioDAO = new TorneioDAO(cb);
        } catch (GenericDAOException ex) {
            throw new BslConnectionBrokerUnavailableException("Unable to load a connection broker", ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean insereNovo(Torneio object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.torneioDAO.insert(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed insertion of Torneio", daoe);
        }
        return result;
    }

    @Override
    public boolean remove(Torneio object) throws GenericBslException {
        boolean result = false;
        try {
            result = this.torneioDAO.delete(object);
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed remove of Torneio", daoe);
        }
        return result;
    }

    @Override
    public boolean update(Torneio object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.torneioDAO.update(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed update of Torneio", daoe);
        }

        return result;
    }

    @Override
    protected boolean valida(Torneio object) {
        if (object.getNome().length() > 30) {
            return false;
        }

        return true;
    }

    @Override
    public List<Torneio> procuraPorCaracteristicas(Torneio object) {
        List<Torneio> torneios = new ArrayList<>();
        this.torneioDAO = new TorneioDAO(cb);
        try {
            torneios = torneioDAO.getByCriteria(object);
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerTorneio.class.getName()).log(Level.SEVERE, null, ex);
        }

        return torneios;
    }

    @Override
    public List<Torneio> getAll() {
        List<Torneio> torneios = null;
        torneioDAO = new TorneioDAO(cb);
        try {
            torneios = torneioDAO.getAll();
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerTorneio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return torneios;
    }

    public void getequipas(Torneio c) {

        int idC = c.getId();
        EquipaDAO cad = new EquipaDAO(this.cb);

        c.setEquipas(cad.getAllEquipasdoTorneio(idC));

    }

    @Override
    public Torneio procuraPorId(int id) {
        Torneio torneio = null;
        try {
            torneio = this.torneioDAO.getById(id);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura por Id of Torneio", daoe);
        }
        return torneio;
    }

    public void getEquipas(Torneio torn) {

        int idT = torn.getId();
        EquipaDAO cad = new EquipaDAO(this.cb);
        torn.setEquipas(cad.getAllEquipasdoTorneio(idT));
    }

}
