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
import org.bgef.bsl.domains.Jogo;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.BslInsertionFailedException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.dao.EquipaDAO;
import org.bgef.dao.JogoDAO;
import org.bgef.dao.db.ConnectionBrokerFactory;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class ManagerJogo extends GenericManager<Jogo> {

    private IConnectionBroker cb = null;
    private JogoDAO jogoDAO = null;

    public ManagerJogo(Properties props) throws BslConnectionBrokerUnavailableException {
        try {
            Properties connectionBrokerProperties = new Properties();
            connectionBrokerProperties.load(new FileInputStream(props.getProperty("bgef.connectionbroker.props")));
            this.cb = (props != null) ? ConnectionBrokerFactory.giveMeConnectionBrokerByName(props.getProperty("bgef.connectionbroker.impl"), connectionBrokerProperties) : ConnectionBrokerFactory.giveMeConnectionBrokerFromProperties();
            jogoDAO = new JogoDAO(cb);
        } catch (GenericDAOException ex) {
            throw new BslConnectionBrokerUnavailableException("Unable to load a connection broker", ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerArbitro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean insereNovo(Jogo object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.jogoDAO.insert(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed insertion of Jogo", daoe);
        }
        return result;
    }

    @Override
    public boolean remove(Jogo object) throws GenericBslException {
        boolean result = false;
        try {
            result = this.jogoDAO.delete(object);
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed remove of Jogo", daoe);
        }
        return result;
    }

    @Override
    public boolean update(Jogo object) throws GenericBslException {
        boolean result = false;
        try {
            if (valida(object)) {
                result = this.jogoDAO.update(object);
            }
        } catch (GenericDAOException daoe) {
            throw new BslInsertionFailedException("Failed update of Jogo", daoe);
        }

        return result;
    }

    @Override
    protected boolean valida(Jogo object) {
        /*if (object.getIdEquipaCasa() < 0) {
         return false;
         }
         if (object.getIdEquipaFora() < 0) {
         return false;
         }*/
        if (object.getSemana() < 0) {
            return false;
        }
        if (object.getAno() < 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<Jogo> procuraPorCaracteristicas(Jogo object) {
        List<Jogo> jogos = new ArrayList<>();
        this.jogoDAO = new JogoDAO(cb);
        try {
            jogos = jogoDAO.getByCriteria(object);
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerJogo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jogos;
    }

    @Override
    public List<Jogo> getAll() {
        List<Jogo> jogos = null;
        jogoDAO = new JogoDAO(cb);
        try {
            jogos = jogoDAO.getAll();
        } catch (GenericDAOException ex) {
            Logger.getLogger(ManagerJogo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jogos;
    }

    public void getEquipaCasa(Jogo c) {

        int idC = c.getIdEquipaCasa();
        EquipaDAO cad = new EquipaDAO(this.cb);
        try {
            c.setEquipaCasa(cad.getById(idC));
        } catch (DatabaseConnectionDAOException | StatementExecuteDAOException ex) {
            Logger.getLogger(ManagerJogador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getEquipaFora(Jogo c) {

        int idC = c.getIdEquipaFora();
        EquipaDAO cad = new EquipaDAO(this.cb);
        try {
            c.setEquipaFora(cad.getById(idC));
        } catch (DatabaseConnectionDAOException | StatementExecuteDAOException ex) {
            Logger.getLogger(ManagerJogador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Jogo procuraPorId(int id) throws GenericBslException {
        Jogo jogo = null;
        try {
            jogo = this.jogoDAO.getById(id);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura por Id of Jogo", daoe);
        }
        return jogo;
    }

    public List<Jogo> getAllJogosTorneio() throws GenericBslException {
        List<Jogo> jogos = null;
        try {
            jogos = this.jogoDAO.getAllJogosTorneio();
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura dos Jogos", daoe);
        }
        return jogos;
    }

    public List<Jogo> getAllJogosCampeonato() throws GenericBslException {
        List<Jogo> jogos = null;
        try {
            jogos = this.jogoDAO.getAllJogosCampeonato();
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura dos Jogos", daoe);
        }
        return jogos;
    }

    public List<Jogo> getJogosSemRelatorio() throws GenericBslException {
        List<Jogo> jogos = null;
        try {
            jogos = this.jogoDAO.getJogosSemRelatorio();
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura dos Jogos", daoe);
        }
        return jogos;
    }

    public List<Jogo> getJogosTorneio(int idTorneio) {
        List<Jogo> jogos = null;
        try {
            jogos = this.jogoDAO.getJogosTorneio(idTorneio);
        } catch (GenericDAOException daoe) {
            new BslInsertionFailedException("Failed procura dos Jogos", daoe);
        }
        return jogos;
    }
}
