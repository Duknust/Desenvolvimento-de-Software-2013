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
import org.bgef.bsl.domains.Campeonato;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.BslInsertionFailedException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.dao.CampeonatoDAO;
import org.bgef.dao.EquipaDAO;
import org.bgef.dao.db.ConnectionBrokerFactory;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.GenericDAOException;

/**
 *
 * @author duarteduarte
 */
public class ManagerCampeonato extends GenericManager<Campeonato> implements IGenericManager<Campeonato> {

    private IConnectionBroker cb = null;
    private CampeonatoDAO campeonatoDAO = null;

    public ManagerCampeonato(Properties props) throws BslConnectionBrokerUnavailableException {
        try {
            Properties connectionBrokerProperties = new Properties();
            connectionBrokerProperties.load(new FileInputStream(props.getProperty("bgef.connectionbroker.props")));
            this.cb = (props != null) ? ConnectionBrokerFactory.giveMeConnectionBrokerByName(props.getProperty("bgef.connectionbroker.impl"), connectionBrokerProperties) : ConnectionBrokerFactory.giveMeConnectionBrokerFromProperties();
            campeonatoDAO = new CampeonatoDAO(cb);
        } catch (GenericDAOException ex) {
            throw new BslConnectionBrokerUnavailableException("Unable to load a connection broker", ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean insereNovo(Campeonato object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.campeonatoDAO.insert(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed insertion of Campeonato", daoe);
        }
        return result;
    }

    @Override
    public boolean remove(Campeonato object) throws GenericBslException {
        boolean result = false;
        try {
            result = this.campeonatoDAO.delete(object);
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed remove of Campeonato", daoe);
        }
        return result;
    }

    @Override
    public boolean update(Campeonato object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.campeonatoDAO.update(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed update of Campeonato", daoe);
        }

        return result;
    }

    @Override
    public Campeonato procuraPorId(int id) {
        Campeonato campeonato = null;
        try {
            campeonato = this.campeonatoDAO.getById(id);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura por Id of Campeonato", daoe);
        }
        return campeonato;
    }

    @Override
    protected boolean valida(Campeonato object) {
        if (object.getNome().length() > 30) {
            return false;
        }
        if (object.getEpoca() < 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<Campeonato> procuraPorCaracteristicas(Campeonato object) {
        List<Campeonato> campeonatos = new ArrayList<>();
        this.campeonatoDAO = new CampeonatoDAO(cb);
        try {
            campeonatos = campeonatoDAO.getByCriteria(object);
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerCampeonato.class.getName()).log(Level.SEVERE, null, ex);
        }

        return campeonatos;
    }

    @Override
    public List<Campeonato> getAll() {
        List<Campeonato> campeonatos = null;
        campeonatoDAO = new CampeonatoDAO(cb);
        try {
            campeonatos = campeonatoDAO.getAll();
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerCampeonato.class.getName()).log(Level.SEVERE, null, ex);
        }
        return campeonatos;
    }

    public void getEquipas(Campeonato c) {

        int idC = c.getId();
        EquipaDAO cad = new EquipaDAO(this.cb);
        c.setEquipas(cad.getAllEquipasdoCampeonato(idC));
    }
}
