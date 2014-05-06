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
import org.bgef.bsl.domains.Instalacao;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class InstalacaoDAO extends GenericDAO<Instalacao> {

    /**
     * Column names
     */
    private final static String COL_ID_INSTALACAO = "ID_INSTALACAO";
    private final static String COL_NOME = "NOME";
    private final static String COL_LOCALIDADE = "LOCALIDADE";
    private final static String COL_CAPACIDADE = "CAPACIDADE";
    /**
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into Instalacoes ( "
            + COL_NOME + ","
            + COL_LOCALIDADE + ","
            + COL_CAPACIDADE + ") "
            + "values (?, ?, ?)";
    static String DELETE_STATEMENT = "delete from Instalacoes "
            + "where COL_ID_INSTALACAO=?";
    static String UPDATE_STATEMENT = "update Instalacoes set "
            + COL_NOME + "=?, "
            + COL_LOCALIDADE + "=?, "
            + COL_CAPACIDADE + "=? "
            + "where ID_INSTALACAO=?";

    public InstalacaoDAO(IConnectionBroker cb) {
        this.cb = cb;
    }

    @Override
    public List<Instalacao> getAll() throws GenericDAOException {
        List<Instalacao> instalacoes = new ArrayList<Instalacao>();
        PreparedStatement pstmt = null;
        ResultSet instalacoesRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM instalacoes");

            try {
                pstmt.execute();
                instalacoesRs = pstmt.getResultSet();
                Instalacao instalacao = null;
                while (instalacoesRs.next()) {
                    instalacao = new Instalacao(
                            instalacoesRs.getString(COL_NOME),
                            instalacoesRs.getString(COL_LOCALIDADE),
                            instalacoesRs.getInt(COL_CAPACIDADE));
                    instalacao.setId(instalacoesRs.getInt(COL_ID_INSTALACAO));
                    instalacoes.add(instalacao);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Instalacao", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (instalacoesRs != null) {
                        instalacoesRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return instalacoes;
    } //done

    @Override
    public boolean insert(Instalacao object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();

            pstmt = conn.prepareStatement(InstalacaoDAO.INSERT_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setString(2, object.getLocalidade());
            pstmt.setInt(3, object.getCapacidade());

            pstmt.execute();

            ResultSet instalacaoKeys = pstmt.getGeneratedKeys();
            int idInstalacao = -1;
            if ((result = pstmt.getUpdateCount() != 0) && instalacaoKeys.next()) {
                idInstalacao = instalacaoKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted instalacao with ID: " + idInstalacao);
                object.setId(idInstalacao);
                pstmt.close();
            }

        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(InstalacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean delete(Instalacao object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(InstalacaoDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            // Do the insertion, check number of rows updated
            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(InstalacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Instalacao object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(InstalacaoDAO.UPDATE_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setString(2, object.getLocalidade());
            pstmt.setInt(3, object.getCapacidade());
            pstmt.setInt(4, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(InstalacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public Instalacao getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        Instalacao instalacao = null;
        PreparedStatement pstmt = null, cartaoStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM INSTALACOES where id_instalacao = " + id + ";");
            pstmt.execute();
            ResultSet instalacaoRs = pstmt.getResultSet();

            while (instalacaoRs.next()) {
                instalacao = new Instalacao(
                        instalacaoRs.getString(COL_NOME),
                        instalacaoRs.getString(COL_LOCALIDADE),
                        instalacaoRs.getInt(COL_CAPACIDADE));
                instalacao.setId(instalacaoRs.getInt(COL_ID_INSTALACAO));
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
                Logger.getLogger(InstalacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return instalacao;
    }

    @Override
    public boolean exists(Instalacao object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM INSTALACOES where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_INSTALACAO + "=" + tmpInt);
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
            if ((tmpInt = object.getCapacidade()) >= 0) {
                statement.append(COL_CAPACIDADE + "=" + tmpInt);
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                stmt.executeQuery(statement.toString());
                ResultSet instalacaoRs = stmt.getResultSet();
                result = instalacaoRs.next();
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException ex) {
                Logger.getLogger(InstalacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Instalacao> getByCriteria(Instalacao object) throws GenericDAOException {
        List<Instalacao> instalacoes = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement instalacaoStmt = null;
        InstalacaoDAO instalacaoDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM INSTALACOES where ");
            if ((tmpInt = object.getId()) >= 0) {
                statement.append(COL_ID_INSTALACAO + "=" + tmpInt);
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

            if ((tmpInt = object.getCapacidade()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_CAPACIDADE + " LIKE '%" + tmpInt + "%'");
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                ResultSet arbitroRs = stmt.executeQuery(statement.toString());

                Instalacao instalacao = null;

                while (arbitroRs.next()) {
                    instalacao = new Instalacao(
                            arbitroRs.getString(COL_NOME),
                            arbitroRs.getString(COL_LOCALIDADE),
                            arbitroRs.getInt(COL_CAPACIDADE));
                    instalacao.setId(arbitroRs.getInt(COL_ID_INSTALACAO));
                    instalacaoStmt.setInt(1, instalacao.getId());
                    instalacaoStmt.execute();
                    instalacoes.add(instalacao);
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Instalacao by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (instalacaoStmt != null) {
                    instalacaoStmt.close();


                }
            } catch (SQLException ex) {
                Logger.getLogger(InstalacaoDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return instalacoes;
    }

    public Collection<Instalacao> values() {
        Collection<Instalacao> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            InstalacaoDAO instalacaoDAO = new InstalacaoDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM Instalacoes");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new Instalacao(rs.getString(COL_NOME), rs.getString(COL_LOCALIDADE), rs.getInt(COL_CAPACIDADE)));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(InstalacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(InstalacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return col;
    }
}
