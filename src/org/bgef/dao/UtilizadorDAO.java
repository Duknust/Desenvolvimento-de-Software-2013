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
import org.bgef.bsl.domains.Utilizador;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class UtilizadorDAO extends GenericDAO<Utilizador> {

    /**
     * Column names
     */
    private final static String COL_ID_UTILIZADOR = "ID_UTILIZADOR";
    private final static String COL_USERNAME = "USERNAME";
    private final static String COL_PASSWORD = "PASSWORD";
    private final static String COL_ID_ARBITRO = "ID_ARBITRO";
    /**
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into UTILIZADORES ( "
            + COL_USERNAME + ","
            + COL_PASSWORD + ","
            + COL_ID_ARBITRO + ") "
            + "values (?, ?, ?)";
    static String DELETE_STATEMENT = "delete from UTILIZADORES "
            + "where ID_UTILIZADOR=?";
    static String UPDATE_STATEMENT = "update UTILIZADORES set "
            + COL_USERNAME + "=?, "
            + COL_PASSWORD + "=?, "
            + COL_ID_ARBITRO + "=? "
            + "where ID_UTILIZADOR=?";

    public UtilizadorDAO(IConnectionBroker cb) {
        this.cb = cb;
    }

    @Override
    public List<Utilizador> getAll() throws GenericDAOException {
        List<Utilizador> utilizadores = new ArrayList<Utilizador>();
        PreparedStatement pstmt = null;
        ResultSet utilizadoresRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM utilizadores");

            try {
                pstmt.execute();
                utilizadoresRs = pstmt.getResultSet();
                Utilizador utilizador = null;
                while (utilizadoresRs.next()) {
                    utilizador = new Utilizador(
                            utilizadoresRs.getString(COL_USERNAME),
                            utilizadoresRs.getString(COL_PASSWORD),
                            utilizadoresRs.getInt(COL_ID_ARBITRO));
                    utilizador.setId(utilizadoresRs.getInt(COL_ID_UTILIZADOR));
                    utilizadores.add(utilizador);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Utilizadores", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (utilizadoresRs != null) {
                        utilizadoresRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return utilizadores;
    } //done

    @Override
    public boolean insert(Utilizador object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();

            pstmt = conn.prepareStatement(UtilizadorDAO.INSERT_STATEMENT);

            pstmt.setString(1, object.getUsername());
            pstmt.setString(2, object.getPassword());
            pstmt.setInt(3, object.getIdArbitro());

            pstmt.execute();

            ResultSet utilizadorKeys = pstmt.getGeneratedKeys();
            int idUtilizador = -1;
            if ((result = pstmt.getUpdateCount() != 0) && utilizadorKeys.next()) {
                idUtilizador = utilizadorKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted utilizador with ID: " + idUtilizador);
                object.setId(idUtilizador);
                pstmt.close();
            }

        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(UtilizadorDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean delete(Utilizador object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(UtilizadorDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            // Do the insertion, check number of rows updated
            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(UtilizadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Utilizador object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(UtilizadorDAO.UPDATE_STATEMENT);

            pstmt.setString(1, object.getUsername());
            pstmt.setString(2, object.getPassword());
            pstmt.setInt(3, object.getIdArbitro());
            pstmt.setInt(4, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(UtilizadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public Utilizador getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        Utilizador utilizador = null;
        PreparedStatement pstmt = null, arbitroStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM UTILIZADORES where id_utilizador = " + id + ";");
            pstmt.execute();
            ResultSet arbitroRs = pstmt.getResultSet();

            while (arbitroRs.next()) {
                utilizador = new Utilizador(
                        arbitroRs.getString(COL_USERNAME),
                        arbitroRs.getString(COL_PASSWORD),
                        arbitroRs.getInt(COL_ID_ARBITRO));
                utilizador.setId(arbitroRs.getInt(COL_ID_ARBITRO));
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (arbitroStmt != null) {
                    arbitroStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UtilizadorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return utilizador;
    }

    @Override
    public boolean exists(Utilizador object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM UTILIZADORES where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_ARBITRO + "=" + tmpInt);
                fields++;
            }

            if ((tmpString = object.getUsername()) != null) {
                statement.append(COL_USERNAME + "=" + tmpString);
                fields++;
            }
            if ((tmpString = object.getPassword()) != null) {
                statement.append(COL_PASSWORD + "=" + tmpString);
                fields++;
            }

            if ((tmpInt = object.getIdArbitro()) >= 0) {
                statement.append(COL_ID_ARBITRO + "=" + tmpInt);
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                stmt.executeQuery(statement.toString());
                ResultSet utilizadorRs = stmt.getResultSet();
                result = utilizadorRs.next();
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException ex) {
                Logger.getLogger(UtilizadorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // PROCESSAR DataSource e criar Filmees e inserir na lista a devolver
        return result;
    }

    @Override
    public List<Utilizador> getByCriteria(Utilizador object) throws GenericDAOException {
        List<Utilizador> utilizadores = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement utilizadorStmt = null;
        UtilizadorDAO utilizadorDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM UTILIZADORES where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_ARBITRO + "=" + tmpInt);
                fields++;
            }

            if ((tmpString = object.getUsername()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_USERNAME + " LIKE '%" + tmpString + "%'");
                fields++;
            }
            if ((tmpString = object.getPassword()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_PASSWORD + " LIKE '%" + tmpString + "%'");
                fields++;
            }
            if ((tmpInt = object.getIdArbitro()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ID_ARBITRO + " LIKE '%" + tmpInt + "%'");
                fields++;
            }

            if (fields > 0) {

                stmt = conn.createStatement();
                ResultSet utilizadorRs = stmt.executeQuery(statement.toString());

                Utilizador utilizador = null;
                while (utilizadorRs.next()) {
                    utilizador = this.getById(utilizadorRs.getInt(COL_ID_UTILIZADOR));
                    utilizadores.add(utilizador);
                }

            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Utilizador by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (utilizadorStmt != null) {
                    utilizadorStmt.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(UtilizadorDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return utilizadores;
    }

    public Collection<Utilizador> values() {
        Collection<Utilizador> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            UtilizadorDAO arbitroDAO = new UtilizadorDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM Utilizadores");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new Utilizador(rs.getString(COL_USERNAME), rs.getString(COL_PASSWORD), rs.getInt(COL_ID_ARBITRO)));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(UtilizadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UtilizadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return col;
    }
}
