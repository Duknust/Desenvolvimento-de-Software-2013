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
import org.bgef.bsl.domains.Jogo;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class JogoDAO extends GenericDAO<Jogo> {

    /**
     * Column names
     */
    private final static String COL_ID_JOGO = "ID_JOGO";
    private final static String COL_EQUIPA_CASA = "ID_EQUIPA_CASA";
    private final static String COL_EQUIPA_FORA = "ID_EQUIPA_FORA";
    private final static String COL_RELATORIO = "ID_RELATORIO";
    private final static String COL_SEMANA = "SEMANA";
    private final static String COL_ANO = "ANO";
    private final static String COL_ID_CAMPEONATO = "ID_CAMPEONATO";
    private final static String COL_ID_TORNEIO = "ID_TORNEIO";
    private final static String COL_FASE = "FASE";
    /**
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into Jogos ( "
            + COL_EQUIPA_CASA + ","
            + COL_EQUIPA_FORA + ","
            + COL_RELATORIO + ", "
            + COL_SEMANA + ", "
            + COL_ANO + ","
            + COL_ID_CAMPEONATO + ", "
            + COL_ID_TORNEIO + ", "
            + COL_FASE + ") "
            + "values (?, ?, ?, ?, ?, ?, ?, ?)";
    static String DELETE_STATEMENT = "delete from Jogos "
            + "where ID_JOGO=?";
    static String UPDATE_STATEMENT = "update Jogos set "
            + COL_EQUIPA_CASA + "=?, "
            + COL_EQUIPA_FORA + "=?, "
            + COL_ID_CAMPEONATO + "=?, "
            + COL_SEMANA + "=?, "
            + COL_ANO + "=?, "
            + COL_ID_TORNEIO + "=?, "
            + COL_RELATORIO + "=?, "
            + COL_FASE + "=? "
            + "where ID_JOGO=?";

    public JogoDAO(IConnectionBroker cb) {
        this.cb = cb;
    }

    @Override
    public List<Jogo> getAll() throws GenericDAOException {
        List<Jogo> jogos = new ArrayList<Jogo>();
        PreparedStatement pstmt = null;
        ResultSet jogosRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM jogos");

            try {
                pstmt.execute();
                jogosRs = pstmt.getResultSet();
                Jogo jogo = null;
                while (jogosRs.next()) {
                    jogo = new Jogo(
                            jogosRs.getInt(COL_EQUIPA_CASA),
                            null,
                            jogosRs.getInt(COL_EQUIPA_FORA),
                            null,
                            jogosRs.getInt(COL_RELATORIO),
                            null,
                            jogosRs.getInt(COL_SEMANA),
                            jogosRs.getInt(COL_ANO),
                            jogosRs.getInt(COL_ID_CAMPEONATO),
                            null,
                            jogosRs.getInt(COL_ID_TORNEIO),
                            null,
                            jogosRs.getInt(COL_FASE));
                    jogo.setId(jogosRs.getInt(COL_ID_JOGO));
                    jogos.add(jogo);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Jogos", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (jogosRs != null) {
                        jogosRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return jogos;
    } //done

    @Override
    public boolean insert(Jogo object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();

            pstmt = conn.prepareStatement(JogoDAO.INSERT_STATEMENT);

            pstmt.setInt(1, object.getIdEquipaCasa());
            pstmt.setInt(2, object.getIdEquipaFora());
            pstmt.setInt(3, object.getIdRelatorio());
            pstmt.setInt(4, object.getSemana());
            pstmt.setInt(5, object.getAno());
            pstmt.setInt(6, object.getIdCampeonato());
            pstmt.setInt(7, object.getIdTorneio());
            pstmt.setInt(8, object.getFase());

            pstmt.execute();

            ResultSet jogoKeys = pstmt.getGeneratedKeys();
            int idJogo = -1;
            if ((result = pstmt.getUpdateCount() != 0) && jogoKeys.next()) {
                idJogo = jogoKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted jogo with ID: " + idJogo);
                object.setId(idJogo);
                pstmt.close();
            }

        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean delete(Jogo object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(JogoDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Jogo object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(JogoDAO.UPDATE_STATEMENT);
            pstmt.setInt(1, object.getIdEquipaCasa());
            pstmt.setInt(2, object.getIdEquipaFora());
            pstmt.setInt(3, object.getIdCampeonato());
            pstmt.setInt(4, object.getSemana());
            pstmt.setInt(5, object.getAno());
            pstmt.setInt(6, object.getIdTorneio());
            pstmt.setInt(7, object.getIdRelatorio());
            pstmt.setInt(8, object.getFase());
            pstmt.setInt(9, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public Jogo getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        Jogo jogo = null;
        PreparedStatement pstmt = null, cartaoStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM JOGOS where " + COL_ID_JOGO + " = ?");
            pstmt.setInt(1, id);
            pstmt.execute();
            ResultSet jogoRs = pstmt.getResultSet();

            while (jogoRs.next()) {
                jogo = new Jogo(
                        jogoRs.getInt(COL_EQUIPA_CASA),
                        null,
                        jogoRs.getInt(COL_EQUIPA_FORA),
                        null,
                        jogoRs.getInt(COL_RELATORIO),
                        null,
                        jogoRs.getInt(COL_SEMANA),
                        jogoRs.getInt(COL_ANO),
                        jogoRs.getInt(COL_ID_CAMPEONATO),
                        null,
                        jogoRs.getInt(COL_ID_TORNEIO),
                        null,
                        jogoRs.getInt(COL_FASE));
                jogo.setId(jogoRs.getInt(COL_ID_JOGO));
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
                Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return jogo;
    }

    @Override
    public boolean exists(Jogo object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM JOGOS where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_JOGO + "=" + tmpInt);
                fields++;
            }

            if ((tmpInt = object.getIdEquipaCasa()) >= 0) {
                statement.append(COL_EQUIPA_CASA + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getIdEquipaFora()) >= 0) {
                statement.append(COL_EQUIPA_FORA + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getIdRelatorio()) >= 0) {
                statement.append(COL_RELATORIO + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getSemana()) >= 0) {
                statement.append(COL_SEMANA + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getAno()) >= 0) {
                statement.append(COL_ANO + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getIdCampeonato()) >= 0) {
                statement.append(COL_ID_CAMPEONATO + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getIdTorneio()) >= 0) {
                statement.append(COL_FASE + "=" + tmpInt);
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
                Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Jogo> getByCriteria(Jogo object) throws GenericDAOException {
        List<Jogo> jogos = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement jogoStmt = null;
        JogoDAO jogoDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM JOGOS where ");
            if ((tmpInt = object.getId()) >= 0) {
                statement.append(COL_ID_JOGO + "=" + tmpInt);
                fields++;
            }

            if ((tmpInt = object.getIdEquipaCasa()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_EQUIPA_CASA + " = " + tmpInt);
                fields++;
            }

            if ((tmpInt = object.getIdEquipaFora()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_EQUIPA_FORA + " = " + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getIdRelatorio()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_RELATORIO + " = " + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getSemana()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_SEMANA + " = " + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getAno()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ANO + " = " + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getIdCampeonato()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ID_CAMPEONATO + " =" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getIdTorneio()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ID_TORNEIO + " = " + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getFase()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_FASE + " = " + tmpInt);
                fields++;
            }
            statement.append(" ORDER BY ID_JOGO");
            if (fields > 0) {
                stmt = conn.createStatement();
                ResultSet jogoRs = stmt.executeQuery(statement.toString());

                Jogo jogo = null;

                while (jogoRs.next()) {
                    jogo = this.getById(jogoRs.getInt(COL_ID_JOGO));
                    jogos.add(jogo);
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Jogo by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (jogoStmt != null) {
                    jogoStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(JogoDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return jogos;
    }

    public Collection<Jogo> values() {
        Collection<Jogo> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            JogoDAO jogoDAO = new JogoDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM Jogos");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new Jogo(rs.getInt(COL_EQUIPA_CASA), null, rs.getInt(COL_EQUIPA_FORA), null, rs.getInt(COL_RELATORIO), null, rs.getInt(COL_SEMANA), rs.getInt(COL_ANO), rs.getInt(COL_ID_CAMPEONATO), null, rs.getInt(COL_ID_TORNEIO), null, rs.getInt(COL_FASE)));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return col;
    }

    public List<Jogo> getAllJogosTorneio() throws GenericDAOException {
        List<Jogo> jogos = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement jogoStmt = null;
        JogoDAO jogoDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM JOGOS where " + COL_ID_TORNEIO + ">=0");

            stmt = conn.createStatement();
            ResultSet jogoRs = stmt.executeQuery(statement.toString());

            Jogo jogo = null;

            while (jogoRs.next()) {
                jogo = this.getById(jogoRs.getInt(COL_ID_JOGO));
                jogos.add(jogo);
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Jogo by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (jogoStmt != null) {
                    jogoStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(JogoDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return jogos;
    }

    public List<Jogo> getAllJogosCampeonato() throws GenericDAOException {
        List<Jogo> jogos = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement jogoStmt = null;
        JogoDAO jogoDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM JOGOS where " + COL_ID_CAMPEONATO + ">=0");

            stmt = conn.createStatement();
            ResultSet jogoRs = stmt.executeQuery(statement.toString());

            Jogo jogo = null;

            while (jogoRs.next()) {
                jogo = this.getById(jogoRs.getInt(COL_ID_JOGO));
                jogos.add(jogo);
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Jogo by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (jogoStmt != null) {
                    jogoStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(JogoDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return jogos;
    }

    public List<Jogo> getJogosSemRelatorio() throws GenericDAOException {
        List<Jogo> jogos = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement jogoStmt = null;
        JogoDAO jogoDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM JOGOS where " + COL_RELATORIO + "= -1");

            stmt = conn.createStatement();
            ResultSet jogoRs = stmt.executeQuery(statement.toString());

            Jogo jogo = null;

            while (jogoRs.next()) {
                jogo = this.getById(jogoRs.getInt(COL_ID_JOGO));
                jogos.add(jogo);
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Jogo by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (jogoStmt != null) {
                    jogoStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(JogoDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return jogos;
    }

    public List<Jogo> getJogosTorneio(int idTorneio) throws GenericDAOException {
        List<Jogo> jogos = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement jogoStmt = null;
        JogoDAO jogoDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM JOGOS where " + COL_ID_TORNEIO + " = " + idTorneio);

            stmt = conn.createStatement();
            ResultSet jogoRs = stmt.executeQuery(statement.toString());
//JA

            Jogo jogo = null;

            while (jogoRs.next()) {
                jogo = this.getById(jogoRs.getInt(COL_ID_JOGO));
                jogos.add(jogo);
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Jogos do Torneio", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (jogoStmt != null) {
                    jogoStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(JogoDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return jogos;
    }
}
