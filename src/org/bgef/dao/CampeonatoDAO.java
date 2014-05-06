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
import org.bgef.bsl.domains.Campeonato;
import org.bgef.bsl.domains.Equipa;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class CampeonatoDAO extends GenericDAO<Campeonato> {

    /**
     * Column names
     */
    private final static String COL_ID_CAMPEONATO = "ID_CAMPEONATO";
    private final static String COL_NOME = "NOME";
    private final static String COL_EPOCA = "EPOCA";
    private final static String COL_ESTADO = "ESTADO";
    private final static String COL_MOSTRAR = "MOSTRAR";
    private final static String COL_ID_ESCALAO = "ID_ESCALAO";
    private static String ADD_RELATED_EQUIPAS = "insert into campeonato_equipa (ID_CAMPEONATO,ID_EQUIPA) values (?, ?)";
    private static String DELETE_RELATED_EQUIPAS = "delete from campeonato_equipa where ID_CAMPEONATO=?";
    /**
     *
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into CAMPEONATOS ( "
            + COL_NOME + ","
            + COL_EPOCA + ","
            + COL_ESTADO + ","
            + COL_MOSTRAR + ","
            + COL_ID_ESCALAO + ") "
            + "values (?, ?, ?, ?, ?)";
    static String DELETE_STATEMENT = "delete from CAMPEONATOS "
            + "where ID_CAMPEONATO=?";
    static String UPDATE_STATEMENT = "update CAMPEONATOS set "
            + COL_NOME + "=?, "
            + COL_EPOCA + "=?, "
            + COL_ESTADO + "=?, "
            + COL_MOSTRAR + "=?, "
            + COL_ID_ESCALAO + "=? "
            + "where ID_CAMPEONATO=?";

    public CampeonatoDAO(IConnectionBroker cb) {
        this.cb = cb;
    }

    @Override
    public List<Campeonato> getAll() throws GenericDAOException {
        List<Campeonato> campeonatos = new ArrayList<Campeonato>();
        PreparedStatement pstmt = null;
        ResultSet campeonatoRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM campeonatos");

            try {
                pstmt.execute();
                campeonatoRs = pstmt.getResultSet();
                Campeonato campeonato = null;
                while (campeonatoRs.next()) {
                    campeonato = new Campeonato(
                            campeonatoRs.getString(COL_NOME),
                            campeonatoRs.getInt(COL_EPOCA),
                            null,
                            campeonatoRs.getInt(COL_ESTADO),
                            campeonatoRs.getInt(COL_MOSTRAR),
                            campeonatoRs.getInt(COL_ID_ESCALAO),
                            null);
                    campeonato.setId(campeonatoRs.getInt(COL_ID_CAMPEONATO));
                    campeonatos.add(campeonato);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Campeonatos", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (campeonatoRs != null) {
                        campeonatoRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return campeonatos;
    } //done

    @Override
    public boolean insert(Campeonato object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();

            pstmt = conn.prepareStatement(CampeonatoDAO.INSERT_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setInt(2, object.getEpoca());
            pstmt.setInt(3, object.getEstado());
            pstmt.setInt(4, object.getMostrar());
            pstmt.setInt(5, object.getIdEscalao());

            pstmt.execute();

            ResultSet campeonatoKeys = pstmt.getGeneratedKeys();
            int idCampeonato = -1;
            if ((result = pstmt.getUpdateCount() != 0) && campeonatoKeys.next()) {
                idCampeonato = campeonatoKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted campeonato with ID: " + idCampeonato);
                object.setId(idCampeonato);
                pstmt.close();
            }

            List<Equipa> equipasCampeonato = null;
            if ((equipasCampeonato = object.getEquipas()) != null) {
                for (Equipa eq : equipasCampeonato) {
                    if (eq.getId() > 0) {
                        pstmt = conn.prepareStatement(ADD_RELATED_EQUIPAS);
                        pstmt.setInt(1, object.getId());
                        pstmt.setInt(2, eq.getId());
                        System.out.println("Query: " + pstmt.toString());
                        pstmt.execute();
                        pstmt.close();
                    }
                }
            }

        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(CampeonatoDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean delete(Campeonato object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(CampeonatoDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            // Do the insertion, check number of rows updated
            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(CampeonatoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Campeonato object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(CampeonatoDAO.UPDATE_STATEMENT);


            pstmt.setString(1, object.getNome());
            pstmt.setInt(2, object.getEpoca());
            pstmt.setInt(3, object.getEstado());
            pstmt.setInt(4, object.getMostrar());
            pstmt.setInt(5, object.getIdEscalao());
            pstmt.setInt(6, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(CampeonatoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public Campeonato getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        Campeonato campeonato = null;
        PreparedStatement pstmt = null, cartaoStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM CAMPEONATOS where id_campeonato = " + id + ";");
            pstmt.execute();
            ResultSet campeonatoRs = pstmt.getResultSet();

            while (campeonatoRs.next()) {
                campeonato = new Campeonato(
                        campeonatoRs.getString(COL_NOME),
                        campeonatoRs.getInt(COL_EPOCA),
                        null,
                        campeonatoRs.getInt(COL_ESTADO),
                        campeonatoRs.getInt(COL_MOSTRAR),
                        campeonatoRs.getInt(COL_ID_ESCALAO),
                        null);
                campeonato.setId(campeonatoRs.getInt(COL_ID_CAMPEONATO));
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
                Logger.getLogger(CampeonatoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return campeonato;
    }

    @Override
    public boolean exists(Campeonato object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM CAMPEONATOS where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_CAMPEONATO + "=" + tmpInt);
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
            if ((tmpInt = object.getEstado()) >= 0) {
                statement.append(COL_ESTADO + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getMostrar()) >= 0) {
                statement.append(COL_MOSTRAR + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getIdEscalao()) >= 0) {
                statement.append(COL_ID_ESCALAO + "=" + tmpInt);
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
                Logger.getLogger(CampeonatoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Campeonato> getByCriteria(Campeonato object) throws GenericDAOException {
        List<Campeonato> campeonatos = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement arbitroStmt = null;
        CampeonatoDAO campeonatoDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM Campeonatos where ");
            if ((tmpInt = object.getId()) >= 0) {
                statement.append(COL_ID_CAMPEONATO + "=" + tmpInt);
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
            if ((tmpInt = object.getEstado()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ESTADO + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getMostrar()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_MOSTRAR + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getIdEscalao()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ID_ESCALAO + " LIKE '%" + tmpInt + "%'");
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                ResultSet campeonatoRs = stmt.executeQuery(statement.toString());

                Campeonato campeonato = null;

                while (campeonatoRs.next()) {
                    campeonato = new Campeonato(
                            campeonatoRs.getString(COL_NOME),
                            campeonatoRs.getInt(COL_EPOCA),
                            null,
                            campeonatoRs.getInt(COL_ESTADO),
                            campeonatoRs.getInt(COL_MOSTRAR),
                            campeonatoRs.getInt(COL_ID_ESCALAO),
                            null);
                    campeonato.setId(campeonatoRs.getInt(COL_ID_CAMPEONATO));
                    arbitroStmt.setInt(1, campeonato.getId());
                    arbitroStmt.execute();
                    campeonatos.add(campeonato);
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Campeonato by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (arbitroStmt != null) {
                    arbitroStmt.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(CampeonatoDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return campeonatos;
    }

    public Collection<Campeonato> values() {
        Collection<Campeonato> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            CampeonatoDAO cartaoDAO = new CampeonatoDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM Campeonatos");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new Campeonato(rs.getString(COL_NOME), rs.getInt(COL_EPOCA), null, rs.getInt(COL_ESTADO), rs.getInt(COL_MOSTRAR), rs.getInt(COL_ID_ESCALAO), null));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(CampeonatoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CampeonatoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return col;
    }

    public List<Campeonato> getAllFasesGrupo(int idC) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
