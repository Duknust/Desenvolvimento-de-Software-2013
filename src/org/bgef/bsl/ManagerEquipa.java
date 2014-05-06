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
import org.bgef.bsl.domains.Equipa;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.BslInsertionFailedException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.dao.EquipaDAO;
import org.bgef.dao.EscalaoDAO;
import org.bgef.dao.EscolaDAO;
import org.bgef.dao.JogadorDAO;
import org.bgef.dao.db.ConnectionBrokerFactory;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class ManagerEquipa extends GenericManager<Equipa> {

    private IConnectionBroker cb = null;
    private EquipaDAO equipaDAO = null;

    public ManagerEquipa(Properties props) throws BslConnectionBrokerUnavailableException {
        try {
            Properties connectionBrokerProperties = new Properties();
            connectionBrokerProperties.load(new FileInputStream(props.getProperty("bgef.connectionbroker.props")));
            this.cb = (props != null) ? ConnectionBrokerFactory.giveMeConnectionBrokerByName(props.getProperty("bgef.connectionbroker.impl"), connectionBrokerProperties) : ConnectionBrokerFactory.giveMeConnectionBrokerFromProperties();
            equipaDAO = new EquipaDAO(cb);
        } catch (GenericDAOException ex) {
            throw new BslConnectionBrokerUnavailableException("Unable to load a connection broker", ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean insereNovo(Equipa object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.equipaDAO.insert(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed insertion of Equipa", daoe);
        }
        return result;
    }

    @Override
    public boolean remove(Equipa object) throws GenericBslException {
        boolean result = false;
        try {
            result = this.equipaDAO.delete(object);
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed remove of Equipa", daoe);
        }
        return result;
    }

    @Override
    public boolean update(Equipa object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.equipaDAO.update(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed update of Equipa", daoe);
        }

        return result;
    }

    @Override
    protected boolean valida(Equipa object) {
        if (object.getNome().length() > 30) {
            return false;
        }
        if (object.getIdEscalao() < 0) {
            return false;
        }

        return true;
    }

    @Override
    public List<Equipa> procuraPorCaracteristicas(Equipa object) {
        List<Equipa> equipas = new ArrayList<>();
        this.equipaDAO = new EquipaDAO(cb);
        try {
            equipas = equipaDAO.getByCriteria(object);
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerEquipa.class.getName()).log(Level.SEVERE, null, ex);
        }

        return equipas;
    }

    @Override
    public List<Equipa> getAll() {
        List<Equipa> equipas = null;
        equipaDAO = new EquipaDAO(cb);
        try {
            equipas = equipaDAO.getAll();
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerEquipa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return equipas;
    }

    public void getEscola(Equipa c) {

        EscolaDAO cad = new EscolaDAO(this.cb);
        try {
            c.setEscola(cad.getById(c.getIdEscola()));
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(ManagerEquipa.class.getName()).log(Level.SEVERE, null, ex);
        } catch (StatementExecuteDAOException ex) {
            Logger.getLogger(ManagerEquipa.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getEscalao(Equipa c) {

        int idE = c.getIdEscalao();
        EscalaoDAO cad = new EscalaoDAO(this.cb);
        try {
            c.setEscalao(cad.getById(idE));
        } catch (DatabaseConnectionDAOException | StatementExecuteDAOException ex) {
            Logger.getLogger(ManagerJogador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getJogadores(Equipa c) {

        int idE = c.getId();
        JogadorDAO cad = new JogadorDAO(this.cb);
        try {
            //c.setJogadores(cad.getAllJogadoresporEquipa(idE));
            c.setJogadores(cad.getAllDaEquipa(idE));
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerEquipa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Equipa procuraPorId(int id) {
        Equipa equipa = null;
        try {
            equipa = this.equipaDAO.getById(id);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura por Id of Equipa", daoe);
        }
        return equipa;
    }
}
