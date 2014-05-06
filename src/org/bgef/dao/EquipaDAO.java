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
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class EquipaDAO extends GenericDAO<Equipa> {

    /**
     * Column names
     */
    private final static String COL_ID_EQUIPA = "ID_EQUIPA";
    private final static String COL_NOME = "NOME";
    private final static String COL_ESCALAO = "ID_ESCALAO";
    private final static String COL_ESCOLA = "ID_ESCOLA";
    /**
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into Equipas ( "
            + COL_NOME + ","
            + COL_ESCALAO + ","
            + COL_ESCOLA + ") "
            + "values (?, ?, ?)";
    static String DELETE_STATEMENT = "delete from Equipas "
            + "where ID_EQUIPA=?";
    static String UPDATE_STATEMENT = "update Equipas set "
            + COL_NOME + "=?, "
            + COL_ESCALAO + "=?, "
            + COL_ESCOLA + "=? "
            + "where ID_EQUIPA=?";

    public EquipaDAO(IConnectionBroker cb) {
        this.cb = cb;
    }

    @Override
    public List<Equipa> getAll() throws GenericDAOException {
        List<Equipa> equipas = new ArrayList<Equipa>();
        PreparedStatement pstmt = null;
        ResultSet equipasRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM equipas");

            try {
                pstmt.execute();
                equipasRs = pstmt.getResultSet();
                Equipa equipa = null;
                while (equipasRs.next()) {
                    equipa = new Equipa(
                            equipasRs.getString(COL_NOME),
                            null,
                            equipasRs.getInt(COL_ESCALAO),
                            null,
                            equipasRs.getInt(COL_ESCOLA),
                            null);
                    equipa.setId(equipasRs.getInt(COL_ID_EQUIPA));
                    equipas.add(equipa);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Cartoes", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (equipasRs != null) {
                        equipasRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return equipas;
    } //done

    @Override
    public boolean insert(Equipa object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();

            pstmt = conn.prepareStatement(EquipaDAO.INSERT_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setInt(2, object.getIdEscalao());
            pstmt.setInt(3, object.getIdEscola());

            pstmt.execute();

            ResultSet cartaoKeys = pstmt.getGeneratedKeys();
            int idEquipa = -1;
            if ((result = pstmt.getUpdateCount() != 0) && cartaoKeys.next()) {
                idEquipa = cartaoKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted equipa with ID: " + idEquipa);
                object.setId(idEquipa);
                pstmt.close();
            }

        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean delete(Equipa object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(EquipaDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            // Do the insertion, check number of rows updated
            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Equipa object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(EquipaDAO.UPDATE_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setInt(2, object.getIdEscalao());
            pstmt.setInt(3, object.getIdEscola());
            pstmt.setInt(4, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public Equipa getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        Equipa equipa = null;
        PreparedStatement pstmt = null, equipaStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM EQUIPAS where id_equipa = " + id + ";");
            pstmt.execute();
            ResultSet equipaRs = pstmt.getResultSet();

            while (equipaRs.next()) {
                equipa = new Equipa(
                        equipaRs.getString(COL_NOME),
                        null,
                        equipaRs.getInt(COL_ESCALAO),
                        null,
                        equipaRs.getInt(COL_ESCOLA),
                        null);
                equipa.setId(equipaRs.getInt(COL_ID_EQUIPA));
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (equipaStmt != null) {
                    equipaStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return equipa;
    }

    @Override
    public boolean exists(Equipa object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM equipas where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_EQUIPA + "=" + tmpInt);
                fields++;
            }

            if ((tmpInt = object.getIdEscalao()) >= 0) {
                statement.append(COL_ESCALAO + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getIdEscola()) >= 0) {
                statement.append(COL_ESCOLA + "=" + tmpInt);
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
                Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Equipa> getByCriteria(Equipa object) throws GenericDAOException {
        List<Equipa> equipas = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement arbitroStmt = null;
        EquipaDAO equipaDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM equipas where ");
            if ((tmpInt = object.getId()) >= 0) {
                statement.append(COL_ID_EQUIPA + "=" + tmpInt);
                fields++;
            }
            if ((tmpString = object.getNome()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_NOME + " LIKE '%" + tmpString + "%'");
                fields++;
            }
            if ((tmpInt = object.getIdEscalao()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ESCALAO + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getIdEscola()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ESCOLA + " LIKE '%" + tmpInt + "%'");
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                ResultSet equipaRs = stmt.executeQuery(statement.toString());

                Equipa equipa = null;

                while (equipaRs.next()) {
                    equipa = this.getById(equipaRs.getInt(COL_ID_EQUIPA));
                    equipas.add(equipa);
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Equipa by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (arbitroStmt != null) {
                    arbitroStmt.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(EquipaDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return equipas;
    }

    public Collection<Equipa> values() {
        Collection<Equipa> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            EquipaDAO equipaDAO = new EquipaDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM Equipas");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new Equipa(rs.getString(COL_NOME), null, rs.getInt(COL_ESCALAO), null, rs.getInt(COL_ESCOLA), null));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return col;
    }

    public List<Equipa> getAllEquipasdoTorneio(int idTorneio) {
        List<Equipa> equipas = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            EquipaDAO equipaDAO = new EquipaDAO(cb);
            stmt = conn.prepareStatement("SELECT ID_EQUIPA FROM Torneio_equipa where ID_TORNEIO=" + idTorneio);
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                equipas.add(getById(rs.getInt("ID_EQUIPA")));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (StatementExecuteDAOException ex) {
            Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return equipas;
    }

    public List<Equipa> getAllEquipasdoCampeonato(int idCampeonato) {

        List<Equipa> equipas = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            EquipaDAO equipaDAO = new EquipaDAO(cb);
            stmt = conn.prepareStatement("SELECT ID_EQUIPA FROM Campeonato_equipa where ID_CAMPEONATO=" + idCampeonato);
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                equipas.add(getById(rs.getInt("ID_EQUIPA")));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (StatementExecuteDAOException ex) {
            Logger.getLogger(EquipaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return equipas;
    }
}
