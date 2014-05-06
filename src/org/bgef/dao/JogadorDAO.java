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
import org.bgef.bsl.domains.Jogador;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class JogadorDAO extends GenericDAO<Jogador> {

    /**
     * Column names
     */
    private final static String COL_ID_JOGADOR = "ID_JOGADOR";
    private final static String COL_NOME = "NOME";
    private final static String COL_DATA_NASCIMENTO = "DATA_NASCIMENTO";
    private final static String COL_SEXO = "SEXO";
    private final static String COL_ID_EQUIPA = "ID_EQUIPA";
    /**
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into JOGADORES ( "
            + COL_NOME + ","
            + COL_DATA_NASCIMENTO + ","
            + COL_SEXO + ","
            + COL_ID_EQUIPA + ") "
            + "values (?, ?, ?, ?)";
    static String DELETE_STATEMENT = "delete from JOGADORES "
            + "where ID_JOGADOR=?";
    static String UPDATE_STATEMENT = "update JOGADORES set "
            + COL_NOME + "=?, "
            + COL_DATA_NASCIMENTO + "=?, "
            + COL_SEXO + "=?, "
            + COL_ID_EQUIPA + "=? "
            + "where ID_JOGADOR=?";

    public JogadorDAO(IConnectionBroker cb) {
        this.cb = cb;
    }

    @Override
    public List<Jogador> getAll() throws GenericDAOException {
        List<Jogador> jogadores = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet jogadoresRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM jogadores");

            try {
                pstmt.execute();
                jogadoresRs = pstmt.getResultSet();
                Jogador jogador = null;
                while (jogadoresRs.next()) {
                    jogador = new Jogador(
                            jogadoresRs.getString(COL_NOME),
                            jogadoresRs.getString(COL_DATA_NASCIMENTO),
                            jogadoresRs.getString(COL_SEXO),
                            jogadoresRs.getInt(COL_ID_EQUIPA),
                            null);
                    jogador.setId(jogadoresRs.getInt(COL_ID_JOGADOR));
                    jogadores.add(jogador);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Jogadores", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (jogadoresRs != null) {
                        jogadoresRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return jogadores;
    } //done

    @Override
    public boolean insert(Jogador object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            pstmt = conn.prepareStatement(JogadorDAO.INSERT_STATEMENT);
            pstmt.setString(1, object.getNome());
            pstmt.setString(2, object.getDataNascimento());
            pstmt.setString(3, object.getSexo());
            pstmt.setInt(4, object.getIdEquipa());
            pstmt.execute();

            ResultSet jogadorKeys = pstmt.getGeneratedKeys();
            int idJogador = -1;
            if ((result = pstmt.getUpdateCount() != 0) && jogadorKeys.next()) {
                idJogador = jogadorKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted jogador with ID: " + idJogador);
                object.setId(idJogador);
                pstmt.close();
            }

        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(JogadorDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean delete(Jogador object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(JogadorDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(JogadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Jogador object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(JogadorDAO.UPDATE_STATEMENT);

            pstmt.setString(1, object.getNome());
            pstmt.setString(2, object.getDataNascimento());
            pstmt.setString(3, object.getSexo());
            pstmt.setInt(4, object.getIdEquipa());
            pstmt.setInt(5, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(JogadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public Jogador getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        Jogador jogador = null;
        PreparedStatement pstmt = null, arbitroStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM JOGADORES where id_jogador = " + id + ";");
            pstmt.execute();
            ResultSet jogadorRs = pstmt.getResultSet();

            while (jogadorRs.next()) {
                jogador = new Jogador(
                        jogadorRs.getString(COL_NOME),
                        jogadorRs.getString(COL_DATA_NASCIMENTO),
                        jogadorRs.getString(COL_SEXO),
                        jogadorRs.getInt(COL_ID_EQUIPA),
                        null);
                jogador.setId(jogadorRs.getInt(COL_ID_JOGADOR));
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
                Logger.getLogger(JogadorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return jogador;
    }

    @Override
    public boolean exists(Jogador object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM JOGADORES where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_JOGADOR + "=" + tmpInt);
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
                Logger.getLogger(JogadorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Jogador> getByCriteria(Jogador object) throws GenericDAOException {
        List<Jogador> jogadores = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement jogadorStmt = null;
        JogadorDAO jogadorDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM JOGADORES where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_JOGADOR + "=" + tmpInt);
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
            if ((tmpInt = object.getIdEquipa()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ID_EQUIPA + " = " + tmpInt);
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                ResultSet jogadorRs = stmt.executeQuery(statement.toString());

                Jogador jogador = null;

                while (jogadorRs.next()) {
                    jogador = new Jogador(
                            jogadorRs.getString(COL_NOME),
                            jogadorRs.getString(COL_DATA_NASCIMENTO),
                            jogadorRs.getString(COL_SEXO),
                            jogadorRs.getInt(COL_ID_EQUIPA),
                            null);
                    jogador.setId(jogadorRs.getInt(COL_ID_JOGADOR));
                    jogadores.add(jogador);
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Jogador by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (jogadorStmt != null) {
                    jogadorStmt.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(JogadorDAO.class.getName()).log(Level.INFO, null, ex);
            }
        }

        return jogadores;
    }

    public Collection<Jogador> values() {
        Collection<Jogador> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            JogadorDAO jogadorDAO = new JogadorDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM Jogadores");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new Jogador(rs.getString(COL_NOME),
                        rs.getString(COL_DATA_NASCIMENTO),
                        rs.getString(COL_SEXO),
                        rs.getInt(COL_ID_EQUIPA),
                        null));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(JogadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(JogadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(JogadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return col;
    }

    public List<Jogador> getAllJogadoresporEquipa(int idEquipa) {
        List<Jogador> jogadores = new ArrayList<>();

        Collection<Jogador> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            JogadorDAO jogadorDAO = new JogadorDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM Jogadores where id_equipa=" + idEquipa);
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                jogadores.add(new Jogador(rs.getString(COL_NOME),
                        rs.getString(COL_DATA_NASCIMENTO),
                        rs.getString(COL_SEXO),
                        rs.getInt(COL_ID_EQUIPA),
                        null));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(JogadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(JogadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(JogadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jogadores;
    }

    public List<Jogador> getAllDaEquipa(int idEquipa) throws GenericDAOException {
        List<Jogador> jogadores = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet jogadoresRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM jogadores where id_equipa=" + idEquipa);

            try {
                pstmt.execute();
                jogadoresRs = pstmt.getResultSet();
                Jogador jogador = null;
                while (jogadoresRs.next()) {
                    jogador = new Jogador(
                            jogadoresRs.getString(COL_NOME),
                            jogadoresRs.getString(COL_DATA_NASCIMENTO),
                            jogadoresRs.getString(COL_SEXO),
                            jogadoresRs.getInt(COL_ID_EQUIPA),
                            null);
                    jogador.setId(jogadoresRs.getInt(COL_ID_JOGADOR));
                    jogadores.add(jogador);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Jogadores", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (jogadoresRs != null) {
                        jogadoresRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return jogadores;
    }
}
