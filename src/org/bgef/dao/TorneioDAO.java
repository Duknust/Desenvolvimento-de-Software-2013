/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bgef.bsl.domains.Equipa;
import org.bgef.bsl.domains.Torneio;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class TorneioDAO extends GenericDAO<Torneio> {

    /**
     * Column names
     */
    private final static String COL_ID_TORNEIO = "ID_TORNEIO";
    private final static String COL_NOME = "NOME";
    private final static String COL_EPOCA = "EPOCA";
    private final static String COL_ESTADO = "ESTADO";
    private final static String COL_MOSTRAR = "MOSTRAR";
    private final static String COL_ID_ESCALAO = "ID_ESCALAO";
    /**
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into TORNEIOS ( "
            + COL_NOME + ","
            + COL_EPOCA + ","
            + COL_ESTADO + ","
            + COL_MOSTRAR + ","
            + COL_ID_ESCALAO + ") "
            + "values (?, ?, ?, ?, ?)";
    static String DELETE_STATEMENT = "delete from TORNEIOS "
            + "where ID_TORNEIO=?";
    static String UPDATE_STATEMENT = "update TORNEIOS set "
            + COL_NOME + "=?, "
            + COL_EPOCA + "=?, "
            + COL_ESTADO + "=?, "
            + COL_MOSTRAR + "=?, "
            + COL_ID_ESCALAO + "=? "
            + "where ID_TORNEIO=?";
    private static String ADD_RELATED_EQUIPAS = "insert into torneio_equipa (ID_TORNEIO,ID_EQUIPA) values (?, ?)";
    private static String DELETE_RELATED_EQUIPAS = "delete from torneio_equipa where ID_TORNEIO=?";

    public TorneioDAO(IConnectionBroker cb) {
        this.cb = cb;
    }

    @Override
    public List<Torneio> getAll() throws GenericDAOException {
        List<Torneio> torneios = new ArrayList<Torneio>();
        PreparedStatement pstmt = null;
        ResultSet torneiosRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM torneios");

            try {
                pstmt.execute();
                torneiosRs = pstmt.getResultSet();
                Torneio torneio = null;
                while (torneiosRs.next()) {
                    torneio = new Torneio(
                            torneiosRs.getString(COL_NOME),
                            torneiosRs.getInt(COL_EPOCA),
                            null,
                            torneiosRs.getInt(COL_ESTADO),
                            torneiosRs.getInt(COL_MOSTRAR),
                            torneiosRs.getInt(COL_ID_ESCALAO),
                            null);
                    torneio.setId(torneiosRs.getInt(COL_ID_TORNEIO));
                    torneios.add(torneio);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Torneios", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (torneiosRs != null) {
                        torneiosRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return torneios;
    } //done

    @Override
    public boolean insert(Torneio object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();

            pstmt = conn.prepareStatement(TorneioDAO.INSERT_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setInt(2, object.getEpoca());
            pstmt.setInt(3, object.getEstado());
            pstmt.setInt(4, object.getMostrar());
            pstmt.setInt(5, object.getIdEscalao());

            pstmt.execute();
            ResultSet torneioKeys = pstmt.getGeneratedKeys();
            int idTorneio = -1;
            if ((result = pstmt.getUpdateCount() != 0) && torneioKeys.next()) {
                idTorneio = torneioKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted torneio with ID: " + idTorneio);
                object.setId(idTorneio);
                pstmt.close();
                List<Equipa> equipasTorneio = null;
                if ((equipasTorneio = object.getEquipas()) != null) {
                    for (Equipa e : equipasTorneio) {
                        if (e.getId() > 0) {
                            pstmt = conn.prepareStatement(ADD_RELATED_EQUIPAS);
                            pstmt.setInt(1, object.getId());
                            pstmt.setInt(2, e.getId());
                            System.out.println("Query: " + pstmt.toString());
                            pstmt.execute();
                            pstmt.close();
                        }
                    }
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(TorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
            }
        }
        return result;
    }

    @Override
    public boolean delete(Torneio object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(TorneioDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            // Do the insertion, check number of rows updated
            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(TorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Torneio object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(TorneioDAO.UPDATE_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setInt(2, object.getEpoca());
            pstmt.setInt(3, object.getEstado());
            pstmt.setInt(4, object.getMostrar());
            pstmt.setInt(5, object.getIdEscalao());
            pstmt.setInt(6, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(TorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public Torneio getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        Torneio torneio = null;
        PreparedStatement pstmt = null, cartaoStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM TORNEIOS where id_torneio = " + id + ";");
            pstmt.execute();
            ResultSet torneioRs = pstmt.getResultSet();

            while (torneioRs.next()) {
                torneio = new Torneio(
                        torneioRs.getString(COL_NOME),
                        torneioRs.getInt(COL_EPOCA),
                        null,
                        torneioRs.getInt(COL_ESTADO),
                        torneioRs.getInt(COL_MOSTRAR),
                        torneioRs.getInt(COL_ID_ESCALAO),
                        null);
                torneio.setId(torneioRs.getInt(COL_ID_TORNEIO));
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (cartaoStmt != null) {
                    cartaoStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return torneio;
    }

    @Override
    public boolean exists(Torneio object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM TORNEIOS where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_TORNEIO + "=" + tmpInt);
                fields++;
            }

            if ((tmpString = object.getNome()) != null) {
                statement.append(COL_NOME + "=" + tmpString);
                fields++;
            }
            if ((tmpInt = object.getEstado()) > 0) {
                statement.append(COL_ESTADO + "=" + tmpInt);
                fields++;
            }
            if (fields > 0) {
                stmt = conn.createStatement();
                stmt.executeQuery(statement.toString());
                ResultSet cartaoRs = stmt.getResultSet();
                result = cartaoRs.next();
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException ex) {
                Logger.getLogger(TorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Torneio> getByCriteria(Torneio object) throws GenericDAOException {
        List<Torneio> torneios = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement arbitroStmt = null;
        TorneioDAO torneioDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM TORNEIOS where ");
            if ((tmpInt = object.getId()) >= 0) {
                statement.append(COL_ID_TORNEIO + "=" + tmpInt);
                fields++;
            }
            if ((tmpString = object.getNome()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_NOME + " LIKE '%" + tmpString + "%'");
                fields++;
            }
            if ((tmpInt = object.getEpoca()) > 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_EPOCA + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getEstado()) > 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ESTADO + " LIKE '%" + tmpInt + "%'");
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                ResultSet torneioRs = stmt.executeQuery(statement.toString());

                Torneio torneio = null;

                while (torneioRs.next()) {
                    torneio = new Torneio(
                            torneioRs.getString(COL_NOME),
                            torneioRs.getInt(COL_EPOCA),
                            null,
                            torneioRs.getInt(COL_ESTADO),
                            torneioRs.getInt(COL_MOSTRAR),
                            torneioRs.getInt(COL_ID_ESCALAO),
                            null);
                    torneio.setId(torneioRs.getInt(COL_ID_TORNEIO));
                    arbitroStmt.setInt(1, torneio.getId());
                    arbitroStmt.execute();
                    torneios.add(torneio);
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Torneio by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (arbitroStmt != null) {
                    arbitroStmt.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(TorneioDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return torneios;
    }

    public Collection<Torneio> values() {
        Collection<Torneio> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            TorneioDAO torneioDAO = new TorneioDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM Torneios");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new Torneio(rs.getString(COL_NOME), rs.getInt(COL_EPOCA), null, rs.getInt(COL_ESTADO), rs.getInt(COL_MOSTRAR), rs.getInt(COL_ID_ESCALAO), null));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(TorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return col;
    }

    public int getFaseEliminatoria(int idTorneio) {
        int idFase = -1;
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            //EquipaDAO equipaDAO = new EquipaDAO(cb);
            stmt = conn.prepareStatement("SELECT ID_FASE FROM Campeonato_torneios where ID_TORNEIO=" + idTorneio);
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                idFase = rs.getInt("ID_FASE");
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idFase;
    }
}
