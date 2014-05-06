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
import org.bgef.bsl.domains.Escalao;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class EscalaoDAO extends GenericDAO<Escalao> {

    /**
     * Column names
     */
    private final static String COL_ID_ESCALAO = "ID_ESCALAO";
    private final static String COL_NOME = "NOME";
    private final static String COL_IDADE_MAXIMA = "IDADE_MAXIMA";
    /**
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into Escaloes ( "
            + COL_NOME + ","
            + COL_IDADE_MAXIMA + ") "
            + "values (?, ?)";
    static String DELETE_STATEMENT = "delete from Escaloes "
            + "where ID_ESCALAO=?";
    static String UPDATE_STATEMENT = "update Escaloes set "
            + COL_NOME + "=?, "
            + COL_IDADE_MAXIMA + "=? "
            + "where ID_ESCALAO=?";

    public EscalaoDAO(IConnectionBroker cb) {
        this.cb = cb;
    }

    @Override
    public List<Escalao> getAll() throws GenericDAOException {
        List<Escalao> escaloes = new ArrayList<Escalao>();
        PreparedStatement pstmt = null;
        ResultSet escaloesRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM escaloes");

            try {
                pstmt.execute();
                escaloesRs = pstmt.getResultSet();
                Escalao escalao = null;
                while (escaloesRs.next()) {
                    escalao = new Escalao(
                            escaloesRs.getString(COL_NOME),
                            escaloesRs.getInt(COL_IDADE_MAXIMA));
                    escalao.setId(escaloesRs.getInt(COL_ID_ESCALAO));
                    escaloes.add(escalao);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Escaloes", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (escaloesRs != null) {
                        escaloesRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return escaloes;
    } //done

    @Override
    public boolean insert(Escalao object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();

            pstmt = conn.prepareStatement(EscalaoDAO.INSERT_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setInt(2, object.getIdadeMaxima());

            pstmt.execute();

            ResultSet escalaoKeys = pstmt.getGeneratedKeys();
            int idEscalao = -1;
            if ((result = pstmt.getUpdateCount() != 0) && escalaoKeys.next()) {
                idEscalao = escalaoKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted escalao with ID: " + idEscalao);
                object.setId(idEscalao);
                pstmt.close();
            }

        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EscalaoDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean delete(Escalao object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(EscalaoDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            // Do the insertion, check number of rows updated
            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EscalaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Escalao object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(EscalaoDAO.UPDATE_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setInt(2, object.getIdadeMaxima());
            pstmt.setInt(3, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EscalaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public Escalao getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        Escalao escalao = null;
        PreparedStatement pstmt = null, escalaoStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM ESCALOES where id_escalao = " + id + ";");
            pstmt.execute();
            ResultSet cartaoRs = pstmt.getResultSet();

            while (cartaoRs.next()) {
                escalao = new Escalao(
                        cartaoRs.getString(COL_NOME),
                        cartaoRs.getInt(COL_IDADE_MAXIMA));
                escalao.setId(cartaoRs.getInt(COL_ID_ESCALAO));
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (escalaoStmt != null) {
                    escalaoStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EscalaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return escalao;
    }

    @Override
    public boolean exists(Escalao object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM ESCALOES where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_ESCALAO + "=" + tmpInt);
                fields++;
            }

            if ((tmpString = object.getNome()) != null) {
                statement.append(COL_NOME + "=" + tmpString);
                fields++;
            }

            if ((tmpInt = object.getIdadeMaxima()) >= 0) {
                statement.append(COL_IDADE_MAXIMA + "=" + tmpInt);
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                stmt.executeQuery(statement.toString());
                ResultSet escalaoRs = stmt.getResultSet();
                result = escalaoRs.next();
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException ex) {
                Logger.getLogger(EscalaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Escalao> getByCriteria(Escalao object) throws GenericDAOException {
        List<Escalao> escaloes = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement escalaoStmt = null;
        EscalaoDAO escalaoDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM ESCALOES where ");
            if ((tmpInt = object.getId()) >= 0) {
                statement.append(COL_ID_ESCALAO + "=" + tmpInt);
                fields++;
            }
            if ((tmpString = object.getNome()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_NOME + " LIKE '%" + tmpString + "%'");
                fields++;
            }
            if ((tmpInt = object.getIdadeMaxima()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_IDADE_MAXIMA + " LIKE '%" + tmpInt + "%'");
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                ResultSet escalaoRs = stmt.executeQuery(statement.toString());

                Escalao escalao = null;

                while (escalaoRs.next()) {
                    escalao = new Escalao(
                            escalaoRs.getString(COL_NOME),
                            escalaoRs.getInt(COL_IDADE_MAXIMA));
                    escalao.setId(escalaoRs.getInt(COL_ID_ESCALAO));
                    escalaoStmt.setInt(1, escalao.getId());
                    escalaoStmt.execute();
                    escaloes.add(escalao);
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Escalao by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (escalaoStmt != null) {
                    escalaoStmt.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(EscalaoDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return escaloes;
    }

    public Collection<Escalao> values() {
        Collection<Escalao> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            EscalaoDAO escalaoDAO = new EscalaoDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM Escaloes");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new Escalao(rs.getString(COL_NOME), rs.getInt(COL_IDADE_MAXIMA)));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EscalaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EscalaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return col;
    }
}
