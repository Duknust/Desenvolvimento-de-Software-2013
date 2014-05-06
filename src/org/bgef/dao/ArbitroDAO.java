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
import org.bgef.bsl.domains.Arbitro;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class ArbitroDAO extends GenericDAO<Arbitro> {

    /**
     * Column names
     */
    private final static String COL_ID_ARBITRO = "ID_ARBITRO";
    private final static String COL_NOME = "NOME";
    private final static String COL_DATA_NASCIMENTO = "DATA_NASCIMENTO";
    private final static String COL_SEXO = "SEXO";
    //private final static String COL_IMAGEM = "IMAGEM";
    /**
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into ARBITROS ( "
            + COL_NOME + ","
            + COL_DATA_NASCIMENTO + ","
            + COL_SEXO + ") "
            + "values (?, ?, ?)";
    static String DELETE_STATEMENT = "delete from ARBITROS "
            + "where ID_ARBITRO=?";
    static String UPDATE_STATEMENT = "update ARBITROS set "
            + COL_NOME + "=?, "
            + COL_DATA_NASCIMENTO + "=?, "
            + COL_SEXO + "=? "
            + "where ID_ARBITRO=?";

    public ArbitroDAO(IConnectionBroker cb) {
        this.cb = cb;

    }

    @Override
    public List<Arbitro> getAll() throws GenericDAOException {
        List<Arbitro> arbitros = new ArrayList<Arbitro>();
        PreparedStatement pstmt = null;
        ResultSet arbitrosRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM arbitros");

            try {
                pstmt.execute();
                arbitrosRs = pstmt.getResultSet();
                Arbitro arbitro = null;
                while (arbitrosRs.next()) {
                    arbitro = new Arbitro(
                            arbitrosRs.getString(COL_NOME),
                            arbitrosRs.getString(COL_DATA_NASCIMENTO),
                            arbitrosRs.getString(COL_SEXO));
                    arbitro.setId(arbitrosRs.getInt(COL_ID_ARBITRO));
                    arbitros.add(arbitro);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Arbitros", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (arbitrosRs != null) {
                        arbitrosRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return arbitros;
    } //done

    @Override
    public boolean insert(Arbitro object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();

            pstmt = conn.prepareStatement(ArbitroDAO.INSERT_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setString(2, object.getDataNascimento());
            pstmt.setString(3, object.getSexo());

            pstmt.execute();

            ResultSet arbitroKeys = pstmt.getGeneratedKeys();
            int idArbitro = -1;
            if ((result = pstmt.getUpdateCount() != 0) && arbitroKeys.next()) {
                idArbitro = arbitroKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted arbitro with ID: " + idArbitro);
                object.setId(idArbitro);
                pstmt.close();
            }

        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(ArbitroDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean delete(Arbitro object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(ArbitroDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            // Do the insertion, check number of rows updated
            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(ArbitroDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Arbitro object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(ArbitroDAO.UPDATE_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setString(2, object.getDataNascimento());
            pstmt.setString(3, object.getSexo());
            pstmt.setInt(4, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(ArbitroDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public Arbitro getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        Arbitro arbitro = null;
        PreparedStatement pstmt = null, arbitroStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM ARBITROS where id_arbitro = " + id + ";");
            pstmt.execute();
            ResultSet arbitroRs = pstmt.getResultSet();

            while (arbitroRs.next()) {
                arbitro = new Arbitro(
                        arbitroRs.getString(COL_NOME),
                        arbitroRs.getString(COL_DATA_NASCIMENTO),
                        arbitroRs.getString(COL_SEXO));
                arbitro.setId(arbitroRs.getInt(COL_ID_ARBITRO));
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
                Logger.getLogger(ArbitroDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return arbitro;
    }

    @Override
    public boolean exists(Arbitro object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM ARBITROS where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_ARBITRO + "=" + tmpInt);
                fields++;
            }

            if ((tmpString = object.getNome()) != null) {
                statement.append(COL_NOME + "=" + tmpString);
                fields++;
            }
            if ((tmpString = object.getDataNascimento()) != null) {
                statement.append(COL_DATA_NASCIMENTO + "=" + tmpString);
                fields++;
            }

            if ((tmpString = object.getSexo()) != null) {
                statement.append(COL_SEXO + "=" + tmpString);
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                stmt.executeQuery(statement.toString());
                ResultSet arbitroRs = stmt.getResultSet();
                result = arbitroRs.next();
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException ex) {
                Logger.getLogger(ArbitroDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // PROCESSAR DataSource e criar Filmees e inserir na lista a devolver
        return result;
    }

    @Override
    public List<Arbitro> getByCriteria(Arbitro object) throws GenericDAOException {
        List<Arbitro> arbitros = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement arbitroStmt = null;
        ArbitroDAO arbitroDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM ARBITROS where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_ARBITRO + "=" + tmpInt);
                fields++;
            }

            if ((tmpString = object.getNome()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_NOME + " LIKE '%" + tmpString + "%'");
                fields++;
            }
            if ((tmpString = object.getDataNascimento()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_DATA_NASCIMENTO + " LIKE '%" + tmpString + "%'");
                fields++;
            }
            if ((tmpString = object.getSexo()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_SEXO + " LIKE '%" + tmpString + "%'");
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                ResultSet arbitroRs = stmt.executeQuery(statement.toString());

                Arbitro arbitro = null;

                while (arbitroRs.next()) {
                    arbitro = new Arbitro(
                            arbitroRs.getString(COL_NOME),
                            arbitroRs.getString(COL_DATA_NASCIMENTO),
                            arbitroRs.getString(COL_SEXO));
                    arbitro.setId(arbitroRs.getInt(COL_ID_ARBITRO));
                    arbitroStmt.setInt(1, arbitro.getId());
                    arbitroStmt.execute();
                    arbitros.add(arbitro);
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Arbitro by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (arbitroStmt != null) {
                    arbitroStmt.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(ArbitroDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return arbitros;
    }

    public Collection<Arbitro> values() {
        Collection<Arbitro> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            ArbitroDAO arbitroDAO = new ArbitroDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM Arbitros");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new Arbitro(rs.getString(COL_NOME), rs.getString(COL_DATA_NASCIMENTO), rs.getString(COL_SEXO)));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(ArbitroDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ArbitroDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return col;
    }
}
