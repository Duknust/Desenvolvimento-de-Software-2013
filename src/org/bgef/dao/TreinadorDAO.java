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
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.bsl.domains.Treinador;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class TreinadorDAO extends GenericDAO<Treinador> {

    /**
     * Column names
     */
    private final static String COL_ID_TREINADOR = "ID_TREINADOR";
    private final static String COL_NOME = "NOME";
    private final static String COL_DATA_NASCIMENTO = "DATA_NASCIMENTO";
    private final static String COL_NACIONALIDADE = "NACIONALIDADE";
    private final static String COL_SEXO = "SEXO";
    private final static String COL_ID_EQUIPA = "ID_EQUIPA";
    /**
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into TREINADORES ( "
            + COL_NOME + ","
            + COL_DATA_NASCIMENTO + ","
            + COL_NACIONALIDADE + ","
            + COL_SEXO + ","
            + COL_ID_EQUIPA + ") "
            + "values (?, ?, ?, ?, ?)";
    static String DELETE_STATEMENT = "delete from TREINADORES "
            + "where ID_TREINADOR=?";
    static String UPDATE_STATEMENT = "update TREINADORES set "
            + COL_NOME + "=?, "
            + COL_DATA_NASCIMENTO + "=?, "
            + COL_NACIONALIDADE + "=?, "
            + COL_SEXO + "=?, "
            + COL_ID_EQUIPA + "=? "
            + "where ID_TREINADOR=?";

    public TreinadorDAO(IConnectionBroker cb) {
        this.cb = cb;
    }

    @Override
    public List<Treinador> getAll() throws GenericDAOException {
        List<Treinador> treinadores = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet treinadoresRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM treinadores");

            try {
                pstmt.execute();
                treinadoresRs = pstmt.getResultSet();
                Treinador treinador = null;
                while (treinadoresRs.next()) {
                    treinador = new Treinador(
                            treinadoresRs.getString(COL_NOME),
                            treinadoresRs.getString(COL_DATA_NASCIMENTO),
                            treinadoresRs.getString(COL_NACIONALIDADE),
                            treinadoresRs.getString(COL_SEXO),
                            treinadoresRs.getInt(COL_ID_EQUIPA),
                            null);
                    treinador.setId(treinadoresRs.getInt(COL_ID_TREINADOR));
                    treinadores.add(treinador);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Treinadores", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (treinadoresRs != null) {
                        treinadoresRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return treinadores;
    } //done

    @Override
    public boolean insert(Treinador object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            pstmt = conn.prepareStatement(TreinadorDAO.INSERT_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setString(2, object.getDataNascimento());
            pstmt.setString(3, object.getNacionalidade());
            pstmt.setString(4, object.getSexo());
            pstmt.setInt(5, object.getIdEquipa());

            pstmt.execute();

            ResultSet jogadorKeys = pstmt.getGeneratedKeys();
            int idJogador = -1;
            if ((result = pstmt.getUpdateCount() != 0) && jogadorKeys.next()) {
                idJogador = jogadorKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted treinador with ID: " + idJogador);
                object.setId(idJogador);
                pstmt.close();
            }

        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(TreinadorDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean delete(Treinador object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(TreinadorDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(TreinadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Treinador object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(TreinadorDAO.UPDATE_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setString(2, object.getDataNascimento());
            pstmt.setString(3, object.getNacionalidade());
            pstmt.setString(4, object.getSexo());
            pstmt.setInt(5, object.getIdEquipa());
            pstmt.setInt(6, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(TreinadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public Treinador getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        Treinador treinador = null;
        PreparedStatement pstmt = null, treinadorStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM TREINADOES where id_treinador = " + id);
            pstmt.execute();
            ResultSet treinadorRs = pstmt.getResultSet();

            while (treinadorRs.next()) {
                treinador = new Treinador(
                        treinadorRs.getString(COL_NOME),
                        treinadorRs.getString(COL_DATA_NASCIMENTO),
                        treinadorRs.getString(COL_NACIONALIDADE),
                        treinadorRs.getString(COL_SEXO),
                        treinadorRs.getInt(COL_ID_EQUIPA),
                        null);
                treinador.setId(treinadorRs.getInt(COL_ID_TREINADOR));
            }
            pstmt.execute();

        } catch (SQLException sqle) {
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (treinadorStmt != null) {
                    treinadorStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TreinadorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return treinador;
    }

    @Override
    public boolean exists(Treinador object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM TREINADORES where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_TREINADOR + "=" + tmpInt);
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

            if ((tmpString = object.getNacionalidade()) != null) {
                statement.append(COL_NACIONALIDADE + "=" + tmpString);
                fields++;
            }
            if ((tmpString = object.getSexo()) != null) {
                statement.append(COL_SEXO + "=" + tmpString);
                fields++;
            }
            if (fields > 0) {
                stmt = conn.createStatement();
                stmt.executeQuery(statement.toString());
                ResultSet jogadorRs = stmt.getResultSet();
                result = jogadorRs.next();
            }
        } catch (SQLException sqle) {
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException ex) {
                Logger.getLogger(TreinadorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // PROCESSAR DataSource e criar Filmees e inserir na lista a devolver
        return result;
    }

    @Override
    public List<Treinador> getByCriteria(Treinador object) throws GenericDAOException {
        List<Treinador> treinadores = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement treinadorStmt = null;
        TreinadorDAO treinadorDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM TREINADORES where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_TREINADOR + "=" + tmpInt);
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
            if ((tmpString = object.getNacionalidade()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_NACIONALIDADE + " LIKE '%" + tmpString + "%'");
                fields++;
            }
            if ((tmpString = object.getSexo()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_SEXO + " LIKE '%" + tmpString + "%'");
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                ResultSet treinadorRs = stmt.executeQuery(statement.toString());

                Treinador treinador = null;

                while (treinadorRs.next()) {
                    treinador = new Treinador(
                            treinadorRs.getString(COL_NOME),
                            treinadorRs.getString(COL_DATA_NASCIMENTO),
                            treinadorRs.getString(COL_NACIONALIDADE),
                            treinadorRs.getString(COL_SEXO),
                            treinadorRs.getInt(COL_ID_EQUIPA),
                            null);
                    treinador.setId(treinadorRs.getInt(COL_ID_TREINADOR));
                    treinadorStmt.setInt(1, treinador.getId());
                    treinadorStmt.execute();
                    treinadores.add(treinador);
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Treinador by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (treinadorStmt != null) {
                    treinadorStmt.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(TreinadorDAO.class.getName()).log(Level.INFO, null, ex);
            }
        }

        return treinadores;
    }

    public Collection<Treinador> values() {
        Collection<Treinador> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            TreinadorDAO treinadorDAO = new TreinadorDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM treinadores");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new Treinador(rs.getString(COL_NOME), rs.getString(COL_DATA_NASCIMENTO), rs.getString(COL_NACIONALIDADE), rs.getString(COL_SEXO), rs.getInt(COL_ID_EQUIPA), null));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(TreinadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TreinadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return col;
    }
}
