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
import org.bgef.bsl.domains.CampeonatoTorneio;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class CampeonatoTorneioDAO extends GenericDAO<CampeonatoTorneio> {

    /**
     * Column names
     */
    private final static String COL_ID_CAMPEONATO_TORNEIO = "ID_CAMPEONATO_TORNEIO";
    private final static String COL_NOME = "NOME";
    private final static String COL_EPOCA = "EPOCA";
    private final static String COL_ESTADO = "ESTADO";
    private final static String COL_ID_CAMPEONATO1 = "ID_CAMPEONATO_1";
    private final static String COL_ID_CAMPEONATO2 = "ID_CAMPEONATO_2";
    private final static String COL_ID_CAMPEONATO3 = "ID_CAMPEONATO_3";
    private final static String COL_ID_CAMPEONATO4 = "ID_CAMPEONATO_4";
    private final static String COL_ID_TORNEIO = "ID_TORNEIO";
    /**
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into Campeonato_torneios ( "
            + COL_NOME + ","
            + COL_EPOCA + ", "
            + COL_ESTADO + ","
            + COL_ID_CAMPEONATO1 + ", "
            + COL_ID_CAMPEONATO2 + ","
            + COL_ID_CAMPEONATO3 + ", "
            + COL_ID_CAMPEONATO4 + ","
            + COL_ID_TORNEIO + ") "
            + "values (?, ?, ?, ?, ?, ?, ?, ?)";
    static String DELETE_STATEMENT = "delete from Campeonato_torneios "
            + "where ID_CAMPEONATO_TORNEIO=?";
    static String UPDATE_STATEMENT = "update Campeonato_torneios set "
            + COL_NOME + "=?, "
            + COL_EPOCA + "=?, "
            + COL_ESTADO + "=?, "
            + COL_ID_CAMPEONATO1 + "=?, "
            + COL_ID_CAMPEONATO2 + "=?, "
            + COL_ID_CAMPEONATO3 + "=?, "
            + COL_ID_CAMPEONATO4 + "=?, "
            + COL_ID_TORNEIO + "=? "
            + "where ID_CAMPEONATO_TORNEIO=?";

    public CampeonatoTorneioDAO(IConnectionBroker cb) {
        this.cb = cb;
    }

    @Override
    public List<CampeonatoTorneio> getAll() throws GenericDAOException {
        List<CampeonatoTorneio> campTors = new ArrayList<CampeonatoTorneio>();
        PreparedStatement pstmt = null;
        ResultSet campTorRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Campeonato_torneios");

            try {
                pstmt.execute();
                campTorRs = pstmt.getResultSet();
                CampeonatoTorneio campTor = null;
                while (campTorRs.next()) {
                    campTor = new CampeonatoTorneio(
                            campTorRs.getString(COL_NOME),
                            campTorRs.getInt(COL_EPOCA),
                            campTorRs.getInt(COL_ID_CAMPEONATO1),
                            campTorRs.getInt(COL_ID_CAMPEONATO2),
                            campTorRs.getInt(COL_ID_CAMPEONATO3),
                            campTorRs.getInt(COL_ID_CAMPEONATO4),
                            null,
                            null,
                            null,
                            null,
                            campTorRs.getInt(COL_ID_TORNEIO),
                            null,
                            campTorRs.getInt(COL_ESTADO));
                    campTor.setId(campTorRs.getInt(COL_ID_CAMPEONATO_TORNEIO));
                    campTors.add(campTor);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os CampeonatoTorneio", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (campTorRs != null) {
                        campTorRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return campTors;
    } //done

    @Override
    public boolean insert(CampeonatoTorneio object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();

            pstmt = conn.prepareStatement(CampeonatoTorneioDAO.INSERT_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setInt(2, object.getEpoca());
            pstmt.setInt(3, object.getEstado());
            pstmt.setInt(4, object.getIdCampeonato1());
            pstmt.setInt(5, object.getIdCampeonato2());
            pstmt.setInt(6, object.getIdCampeonato3());
            pstmt.setInt(7, object.getIdCampeonato4());
            pstmt.setInt(8, object.getIdTorneio());

            pstmt.execute();

            ResultSet campTornKeys = pstmt.getGeneratedKeys();
            int idCampTor = -1;
            if ((result = pstmt.getUpdateCount() != 0) && campTornKeys.next()) {
                idCampTor = campTornKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted CampeonatoTorneio with ID: " + idCampTor);
                object.setId(idCampTor);
                pstmt.close();
            }

        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(CampeonatoTorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean delete(CampeonatoTorneio object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(CampeonatoTorneioDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            // Do the insertion, check number of rows updated
            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(CampeonatoTorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(CampeonatoTorneio object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(CampeonatoTorneioDAO.UPDATE_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setInt(2, object.getEpoca());
            pstmt.setInt(3, object.getEstado());
            pstmt.setInt(4, object.getIdCampeonato1());
            pstmt.setInt(5, object.getIdCampeonato2());
            pstmt.setInt(6, object.getIdCampeonato3());
            pstmt.setInt(7, object.getIdCampeonato4());
            pstmt.setInt(8, object.getIdTorneio());
            pstmt.setInt(9, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(CampeonatoTorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public CampeonatoTorneio getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        CampeonatoTorneio campTor = null;
        PreparedStatement pstmt = null, campTorStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM Campeonato_torneios where ID_CAMPEONATO_TORNEIO = " + id + ";");
            pstmt.execute();
            ResultSet campTorRs = pstmt.getResultSet();

            while (campTorRs.next()) {
                campTor = new CampeonatoTorneio(
                        campTorRs.getString(COL_NOME),
                        campTorRs.getInt(COL_EPOCA),
                        campTorRs.getInt(COL_ID_CAMPEONATO1),
                        campTorRs.getInt(COL_ID_CAMPEONATO2),
                        campTorRs.getInt(COL_ID_CAMPEONATO3),
                        campTorRs.getInt(COL_ID_CAMPEONATO4),
                        null,
                        null,
                        null,
                        null,
                        campTorRs.getInt(COL_ID_TORNEIO),
                        null,
                        campTorRs.getInt(COL_ESTADO));
                campTor.setId(campTorRs.getInt(COL_ID_CAMPEONATO_TORNEIO));
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (campTorStmt != null) {
                    campTorStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CampeonatoTorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return campTor;
    }

    @Override
    public boolean exists(CampeonatoTorneio object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM Campeonato_torneios where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_CAMPEONATO_TORNEIO + "=" + tmpInt);
                fields++;
            }

            if ((tmpString = object.getNome()) != null) {
                statement.append(COL_NOME + "=" + tmpString);
                fields++;
            }

            if ((tmpInt = object.getEpoca()) >= 0) {
                statement.append(COL_EPOCA + "=" + tmpInt);
                fields++;
            }

            if ((tmpInt = object.getIdCampeonato1()) >= 0) {
                statement.append(COL_ID_CAMPEONATO1 + "=" + tmpInt);
                fields++;
            }

            if ((tmpInt = object.getIdCampeonato2()) >= 0) {
                statement.append(COL_ID_CAMPEONATO2 + "=" + tmpInt);
                fields++;
            }

            if ((tmpInt = object.getIdCampeonato3()) >= 0) {
                statement.append(COL_ID_CAMPEONATO3 + "=" + tmpInt);
                fields++;
            }

            if ((tmpInt = object.getIdCampeonato4()) >= 0) {
                statement.append(COL_ID_CAMPEONATO4 + "=" + tmpInt);
                fields++;
            }

            if ((tmpInt = object.getIdTorneio()) >= 0) {
                statement.append(COL_ID_TORNEIO + "=" + tmpInt);
                fields++;
            }

            if ((tmpInt = object.getEstado()) >= 0) {
                statement.append(COL_ESTADO + "=" + tmpInt);
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                stmt.executeQuery(statement.toString());
                ResultSet campTorRs = stmt.getResultSet();
                result = campTorRs.next();
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException ex) {
                Logger.getLogger(CampeonatoTorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<CampeonatoTorneio> getByCriteria(CampeonatoTorneio object) throws GenericDAOException {
        List<CampeonatoTorneio> campTorns = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement campTornStmt = null;
        CampeonatoTorneioDAO campTorDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM Campeonato_torneios where ");
            if ((tmpInt = object.getId()) >= 0) {
                statement.append(COL_ID_CAMPEONATO_TORNEIO + "=" + tmpInt);
                fields++;
            }
            if ((tmpString = object.getNome()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_NOME + " LIKE '%" + tmpString + "%'");
                fields++;
            }
            if ((tmpInt = object.getEpoca()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_EPOCA + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getIdCampeonato1()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ID_CAMPEONATO1 + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getIdCampeonato2()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ID_CAMPEONATO2 + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getIdCampeonato3()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ID_CAMPEONATO3 + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getIdCampeonato4()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ID_CAMPEONATO4 + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getIdTorneio()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ID_TORNEIO + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getEstado()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ESTADO + " LIKE '%" + tmpInt + "%'");
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                ResultSet campTorRs = stmt.executeQuery(statement.toString());

                CampeonatoTorneio camTor = null;

                while (campTorRs.next()) {
                    camTor = this.getById(campTorRs.getInt(COL_ID_CAMPEONATO_TORNEIO));
                    campTorns.add(camTor);
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Campeonato_torneios by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (campTornStmt != null) {
                    campTornStmt.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(CampeonatoTorneioDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return campTorns;
    }

    public Collection<CampeonatoTorneio> values() {
        Collection<CampeonatoTorneio> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            CampeonatoTorneioDAO campTornDAO = new CampeonatoTorneioDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM Campeonato_torneios");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new CampeonatoTorneio(rs.getString(COL_NOME), rs.getInt(COL_EPOCA), rs.getInt(COL_ID_CAMPEONATO1), rs.getInt(COL_ID_CAMPEONATO2), rs.getInt(COL_ID_CAMPEONATO3), rs.getInt(COL_ID_CAMPEONATO4), null, null, null, null, rs.getInt(COL_ID_TORNEIO), null, rs.getInt(COL_ESTADO)));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(CampeonatoTorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CampeonatoTorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return col;
    }

    public CampeonatoTorneio getCampeonatoTorneio(int idCampeonato) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        CampeonatoTorneio campTor = null;
        PreparedStatement pstmt = null, campTorStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM Campeonato_torneios where "
                    + "ID_CAMPEONATO_1 = " + idCampeonato
                    + " or ID_CAMPEONATO_2 = " + idCampeonato
                    + " or ID_CAMPEONATO_3 = " + idCampeonato
                    + " or ID_CAMPEONATO_4 = " + idCampeonato + ";");
            pstmt.execute();
            ResultSet campTorRs = pstmt.getResultSet();

            while (campTorRs.next()) {
                campTor = new CampeonatoTorneio(
                        campTorRs.getString(COL_NOME),
                        campTorRs.getInt(COL_EPOCA),
                        campTorRs.getInt(COL_ID_CAMPEONATO1),
                        campTorRs.getInt(COL_ID_CAMPEONATO2),
                        campTorRs.getInt(COL_ID_CAMPEONATO3),
                        campTorRs.getInt(COL_ID_CAMPEONATO4),
                        null,
                        null,
                        null,
                        null,
                        campTorRs.getInt(COL_ID_TORNEIO),
                        null,
                        campTorRs.getInt(COL_ESTADO));
                campTor.setId(campTorRs.getInt(COL_ID_CAMPEONATO_TORNEIO));
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (campTorStmt != null) {
                    campTorStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CampeonatoTorneioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return campTor;
    }
}
