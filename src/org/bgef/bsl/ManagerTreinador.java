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
import org.bgef.bsl.domains.Treinador;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.BslInsertionFailedException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.dao.EquipaDAO;
import org.bgef.dao.TreinadorDAO;
import org.bgef.dao.db.ConnectionBrokerFactory;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

public class ManagerTreinador extends GenericManager<Treinador> {

    private IConnectionBroker cb = null;
    private TreinadorDAO treinadorDAO = null;

    public ManagerTreinador(Properties props) throws BslConnectionBrokerUnavailableException {
        try {
            Properties connectionBrokerProperties = new Properties();
            connectionBrokerProperties.load(new FileInputStream(props.getProperty("bgef.connectionbroker.props")));
            this.cb = (props != null) ? ConnectionBrokerFactory.giveMeConnectionBrokerByName(props.getProperty("bgef.connectionbroker.impl"), connectionBrokerProperties) : ConnectionBrokerFactory.giveMeConnectionBrokerFromProperties();
            treinadorDAO = new TreinadorDAO(cb);
        } catch (GenericDAOException ex) {
            throw new BslConnectionBrokerUnavailableException("Unable to load a connection broker", ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean insereNovo(Treinador object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.treinadorDAO.insert(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed insertion of Treinador", daoe);
        }
        return result;
    }

    @Override
    public boolean remove(Treinador object) throws GenericBslException {
        boolean result = false;
        try {
            result = this.treinadorDAO.delete(object);
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed remove of Treinador", daoe);
        }
        return result;
    }

    @Override
    public boolean update(Treinador object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.treinadorDAO.update(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed update of Treinador", daoe);
        }

        return result;
    }

    @Override
    protected boolean valida(Treinador object) {
        if (object.getNome().length() > 30) {
            return false;
        }
        if (object.getDataNascimento().length() > 10) {
            return false;
        }
        if (object.getNacionalidade().length() > 20) {
            return false;
        }
        if (object.getSexo().length() > 10) {
            return false;
        }
        return true;
    }

    @Override
    public List<Treinador> procuraPorCaracteristicas(Treinador object) {
        List<Treinador> treinadors = new ArrayList<>();
        this.treinadorDAO = new TreinadorDAO(cb);
        try {
            treinadors = treinadorDAO.getByCriteria(object);
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerTreinador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return treinadors;
    }

    @Override
    public List<Treinador> getAll() {
        List<Treinador> treinadors = null;
        treinadorDAO = new TreinadorDAO(cb);
        try {
            treinadors = treinadorDAO.getAll();
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerTreinador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return treinadors;
    }

    public void getEquipa(Treinador c) {

        int idC = c.getIdEquipa();
        EquipaDAO cad = new EquipaDAO(this.cb);
        try {
            c.setEquipa(cad.getById(idC));
        } catch (DatabaseConnectionDAOException | StatementExecuteDAOException ex) {
            Logger.getLogger(ManagerJogador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Treinador procuraPorId(int id) throws GenericBslException {
        Treinador treinador = null;
        try {
            treinador = this.treinadorDAO.getById(id);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura por Id of Treinador", daoe);
        }
        return treinador;
    }
}
