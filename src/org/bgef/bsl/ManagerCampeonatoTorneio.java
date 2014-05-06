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
import org.bgef.bsl.domains.CampeonatoTorneio;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.BslInsertionFailedException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.dao.CampeonatoTorneioDAO;
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
public class ManagerCampeonatoTorneio extends GenericManager<CampeonatoTorneio> {

    private IConnectionBroker cb = null;
    private CampeonatoTorneioDAO CampeonatoTorneioDAO = null;
    private Properties props = null;

    public ManagerCampeonatoTorneio(Properties props) throws BslConnectionBrokerUnavailableException {
        try {
            Properties connectionBrokerProperties = new Properties();
            connectionBrokerProperties.load(new FileInputStream(props.getProperty("bgef.connectionbroker.props")));
            this.cb = (props != null) ? ConnectionBrokerFactory.giveMeConnectionBrokerByName(props.getProperty("bgef.connectionbroker.impl"), connectionBrokerProperties) : ConnectionBrokerFactory.giveMeConnectionBrokerFromProperties();
            CampeonatoTorneioDAO = new CampeonatoTorneioDAO(cb);
        } catch (GenericDAOException ex) {
            throw new BslConnectionBrokerUnavailableException("Unable to load a connection broker", ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean insereNovo(CampeonatoTorneio object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.CampeonatoTorneioDAO.insert(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed insertion of CampeonatoTorneio", daoe);
        }
        return result;
    }

    @Override
    public boolean remove(CampeonatoTorneio object) throws GenericBslException {
        boolean result = false;
        try {
            result = this.CampeonatoTorneioDAO.delete(object);
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed remove of CampeonatoTorneio", daoe);
        }
        return result;
    }

    @Override
    public boolean update(CampeonatoTorneio object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.CampeonatoTorneioDAO.update(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed update of CampeonatoTorneio", daoe);
        }

        return result;
    }

    @Override
    protected boolean valida(CampeonatoTorneio object) {
        if (object.getNome().length() > 30) {
            return false;
        }
        return true;
    }

    @Override
    public List<CampeonatoTorneio> procuraPorCaracteristicas(CampeonatoTorneio object) {
        List<CampeonatoTorneio> campeonatotorneios = new ArrayList<>();
        this.CampeonatoTorneioDAO = new CampeonatoTorneioDAO(cb);
        try {
            campeonatotorneios = CampeonatoTorneioDAO.getByCriteria(object);
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerCampeonatoTorneio.class.getName()).log(Level.SEVERE, null, ex);
        }

        return campeonatotorneios;
    }

    @Override
    public List<CampeonatoTorneio> getAll() {
        List<CampeonatoTorneio> campeonatotorneios = null;
        CampeonatoTorneioDAO = new CampeonatoTorneioDAO(cb);
        try {
            campeonatotorneios = CampeonatoTorneioDAO.getAll();
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerCampeonatoTorneio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return campeonatotorneios;
    }

    public void getTorneio(CampeonatoTorneio ct) {

        int idC = ct.getId();
        TorneioDAO cad = new TorneioDAO(this.cb);
        ct.setFaseEliminatoria(cad.getFaseEliminatoria(idC));

    }

    public List<Campeonato> getGruposCampeonato(CampeonatoTorneio ct) {
        List<Campeonato> camps = new ArrayList<>();

        camps.add(ct.getCampeonato1());
        camps.add(ct.getCampeonato2());
        camps.add(ct.getCampeonato3());
        camps.add(ct.getCampeonato4());

        return camps;
    }

    @Override
    public CampeonatoTorneio procuraPorId(int id) throws GenericBslException {
        CampeonatoTorneio campeonatoTorneio = null;
        try {
            campeonatoTorneio = this.CampeonatoTorneioDAO.getById(id);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura por Id of CampeonatoTorneio", daoe);
        }
        return campeonatoTorneio;
    }

    public CampeonatoTorneio getCampeonatoTorneio(int id) throws GenericBslException {
        CampeonatoTorneio campeonatoTorneio = null;
        try {
            campeonatoTorneio = this.CampeonatoTorneioDAO.getCampeonatoTorneio(id);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(ManagerCampeonatoTorneio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (StatementExecuteDAOException ex) {
            Logger.getLogger(ManagerCampeonatoTorneio.class.getName()).log(Level.SEVERE, null, ex);
        }

        return campeonatoTorneio;
    }

}
