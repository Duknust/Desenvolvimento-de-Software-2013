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
import org.bgef.bsl.domains.Escola;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.BslInsertionFailedException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.dao.EquipaDAO;
import org.bgef.dao.EscolaDAO;
import org.bgef.dao.InstalacaoDAO;
import org.bgef.dao.db.ConnectionBrokerFactory;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class ManagerEscola extends GenericManager<Escola> {

    private IConnectionBroker cb = null;
    private EscolaDAO escolaDAO = null;

    public ManagerEscola(Properties props) throws BslConnectionBrokerUnavailableException {
        try {
            Properties connectionBrokerProperties = new Properties();
            connectionBrokerProperties.load(new FileInputStream(props.getProperty("bgef.connectionbroker.props")));
            this.cb = (props != null) ? ConnectionBrokerFactory.giveMeConnectionBrokerByName(props.getProperty("bgef.connectionbroker.impl"), connectionBrokerProperties) : ConnectionBrokerFactory.giveMeConnectionBrokerFromProperties();
            escolaDAO = new EscolaDAO(cb);
        } catch (GenericDAOException ex) {
            throw new BslConnectionBrokerUnavailableException("Unable to load a connection broker", ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean insereNovo(Escola object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.escolaDAO.insert(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed insertion of Escola", daoe);
        }
        return result;
    }

    @Override
    public boolean remove(Escola object) throws GenericBslException {
        boolean result = false;
        try {
            result = this.escolaDAO.delete(object);
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed remove of Escola", daoe);
        }
        return result;
    }

    @Override
    public boolean update(Escola object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.escolaDAO.update(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed update of Escola", daoe);
        }

        return result;
    }

    @Override
    protected boolean valida(Escola object) {
        if (object.getNome().length() > 30) {
            return false;
        }
        if (object.getLocalidade().length() > 20) {
            return false;
        }
        if (object.getIdInstalacao() < 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<Escola> procuraPorCaracteristicas(Escola object) {
        List<Escola> escolas = new ArrayList<>();
        this.escolaDAO = new EscolaDAO(cb);
        try {
            escolas = escolaDAO.getByCriteria(object);
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerEscola.class.getName()).log(Level.SEVERE, null, ex);
        }

        return escolas;
    }

    @Override
    public List<Escola> getAll() {
        List<Escola> escolas = null;
        escolaDAO = new EscolaDAO(cb);
        try {
            escolas = escolaDAO.getAll();
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerEscola.class.getName()).log(Level.SEVERE, null, ex);
        }
        return escolas;
    }

    public void getInstalacao(Escola c) {

        int idC = c.getIdInstalacao();
        InstalacaoDAO cad = new InstalacaoDAO(this.cb);
        try {
            c.setInstalacao(cad.getById(idC));
        } catch (DatabaseConnectionDAOException | StatementExecuteDAOException ex) {
            Logger.getLogger(ManagerJogador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getEquipas(Escola c) {

        int idC = c.getId();
        EquipaDAO cad = new EquipaDAO(this.cb);
        Equipa equipa = new Equipa();
        equipa.setIdEscola(idC);
        try {
            c.setEquipas(cad.getByCriteria(equipa));
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerEscola.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Escola procuraPorId(int id) {
        Escola escola = null;
        try {
            escola = this.escolaDAO.getById(id);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura por Id of Escola", daoe);
        }
        return escola;
    }
}
