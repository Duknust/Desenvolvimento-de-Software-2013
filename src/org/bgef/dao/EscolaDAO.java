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
import org.bgef.bsl.domains.Escola;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class EscolaDAO extends GenericDAO<Escola> {

    /**
     * Column names
     */
    private final static String COL_ID_ESCOLA = "ID_ESCOLA";
    private final static String COL_NOME = "NOME";
    private final static String COL_LOCALIDADE = "LOCALIDADE";
    private final static String COL_INSTALACAO = "ID_INSTALACAO";
    /**
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into ESCOLAS ( "
            + COL_NOME + ","
            + COL_LOCALIDADE + ","
            + COL_INSTALACAO + ") "
            + "values (?, ?, ?)";
    static String DELETE_STATEMENT = "delete from ESCOLAS "
            + "where COL_ID_ESCOLA=?";
    static String UPDATE_STATEMENT = "update ESCOLAS set "
            + COL_NOME + "=?, "
            + COL_LOCALIDADE + "=?, "
            + COL_INSTALACAO + "=? "
            + "where COL_ID_ESCOLA=?";

    public EscolaDAO(IConnectionBroker cb) {
        this.cb = cb;
    }

    @Override
    public List<Escola> getAll() throws GenericDAOException {
        List<Escola> escolas = new ArrayList<Escola>();
        PreparedStatement pstmt = null;
        ResultSet escolasRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM escolas");

            try {
                pstmt.execute();
                escolasRs = pstmt.getResultSet();
                Escola escola = null;
                while (escolasRs.next()) {
                    escola = new Escola(
                            escolasRs.getString(COL_NOME),
                            null,
                            escolasRs.getString(COL_LOCALIDADE),
                            escolasRs.getInt(COL_INSTALACAO),
                            null);
                    escola.setId(escolasRs.getInt(COL_ID_ESCOLA));
                    escolas.add(escola);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Cartoes", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (escolasRs != null) {
                        escolasRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return escolas;
    } //done

    @Override
    public boolean insert(Escola object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();

            pstmt = conn.prepareStatement(EscolaDAO.INSERT_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setString(2, object.getLocalidade());
            pstmt.setInt(3, object.getIdInstalacao());

            pstmt.execute();

            ResultSet cartaoKeys = pstmt.getGeneratedKeys();
            int idEscola = -1;
            if ((result = pstmt.getUpdateCount() != 0) && cartaoKeys.next()) {
                idEscola = cartaoKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted escola with ID: " + idEscola);
                object.setId(idEscola);
                pstmt.close();
            }

        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EscolaDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean delete(Escola object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(EscolaDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            // Do the insertion, check number of rows updated
            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EscolaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Escola object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(EscolaDAO.UPDATE_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setString(2, object.getLocalidade());
            pstmt.setInt(3, object.getIdInstalacao());
            pstmt.setInt(4, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EscolaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public Escola getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        Escola escola = null;
        PreparedStatement pstmt = null, cartaoStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM Escolas where id_escola = " + id + ";");
            pstmt.execute();
            ResultSet escolaRs = pstmt.getResultSet();

            while (escolaRs.next()) {
                escola = new Escola(
                        escolaRs.getString(COL_NOME),
                        null,
                        escolaRs.getString(COL_LOCALIDADE),
                        escolaRs.getInt(COL_INSTALACAO),
                        null);
                escola.setId(escolaRs.getInt(COL_ID_ESCOLA));
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
                Logger.getLogger(EscolaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return escola;
    }

    @Override
    public boolean exists(Escola object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM ESCOLAS where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_ESCOLA + "=" + tmpInt);
                fields++;
            }

            if ((tmpString = object.getNome()) != null) {
                statement.append(COL_NOME + "=" + tmpString);
                fields++;
            }

            if ((tmpString = object.getLocalidade()) != null) {
                statement.append(COL_LOCALIDADE + "=" + tmpString);
                fields++;
            }

            if ((tmpInt = object.getIdInstalacao()) >= 0) {
                statement.append(COL_INSTALACAO + "=" + tmpInt);
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                stmt.executeQuery(statement.toString());
                ResultSet escolaRs = stmt.getResultSet();
                result = escolaRs.next();
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException ex) {
                Logger.getLogger(EscolaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Escola> getByCriteria(Escola object) throws GenericDAOException {
        List<Escola> escolas = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement escolaStmt = null;
        EscolaDAO escolaDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM ESCOLAS where ");
            if ((tmpInt = object.getId()) >= 0) {
                statement.append(COL_ID_ESCOLA + "=" + tmpInt);
                fields++;
            }

            if ((tmpString = object.getNome()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_NOME + " LIKE '%" + tmpString + "%'");
                fields++;
            }

            if ((tmpString = object.getLocalidade()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_LOCALIDADE + " LIKE '%" + tmpString + "%'");
                fields++;
            }

            if ((tmpInt = object.getIdInstalacao()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_INSTALACAO + " LIKE '%" + tmpInt + "%'");
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                ResultSet escolaRs = stmt.executeQuery(statement.toString());

                Escola escola = null;

                while (escolaRs.next()) {
                    escola = new Escola(
                            escolaRs.getString(COL_NOME),
                            null,
                            escolaRs.getString(COL_LOCALIDADE),
                            escolaRs.getInt(COL_INSTALACAO),
                            null);
                    escola.setId(escolaRs.getInt(COL_ID_ESCOLA));
                    escolaStmt.setInt(1, escola.getId());
                    escolaStmt.execute();
                    escolas.add(escola);
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Escola by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (escolaStmt != null) {
                    escolaStmt.close();


                }
            } catch (SQLException ex) {
                Logger.getLogger(EscolaDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return escolas;
    }

    public Collection<Escola> values() {
        Collection<Escola> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            EscolaDAO escolaDAO = new EscolaDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM escolas");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new Escola(rs.getString(COL_NOME), null, rs.getString(COL_LOCALIDADE), rs.getInt(COL_INSTALACAO), null));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(EscolaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EscolaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return col;
    }
}
