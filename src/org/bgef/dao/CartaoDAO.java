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
import org.bgef.bsl.domains.Cartao;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class CartaoDAO extends GenericDAO<Cartao> {

    /**
     * Column names
     */
    private final static String COL_ID_CARTAO = "ID_CARTAO";
    private final static String COL_JOGADOR = "ID_JOGADOR";
    private final static String COL_JOGO = "ID_JOGO";
    private final static String COL_TIPO = "TIPO";
    /**
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into CARTOES ( "
            + COL_JOGO + ","
            + COL_JOGADOR + ","
            + COL_TIPO + ") "
            + "values (?, ?, ?)";
    static String DELETE_STATEMENT = "delete from CARTOES "
            + "where ID_CARTAO=?";
    static String UPDATE_STATEMENT = "update CARTOES set "
            + COL_JOGO + "=?, "
            + COL_JOGADOR + "=?, "
            + COL_TIPO + "=? "
            + "where ID_CARTAO=?";

    public CartaoDAO(IConnectionBroker cb) {
        this.cb = cb;
    }

    @Override
    public List<Cartao> getAll() throws GenericDAOException {
        List<Cartao> cartoes = new ArrayList<Cartao>();
        PreparedStatement pstmt = null;
        ResultSet cartoesRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM cartoes");

            try {
                pstmt.execute();
                cartoesRs = pstmt.getResultSet();
                Cartao cartao = null;
                while (cartoesRs.next()) {
                    cartao = new Cartao(
                            cartoesRs.getInt(COL_JOGADOR),
                            null,
                            cartoesRs.getInt(COL_JOGO),
                            cartoesRs.getString(COL_TIPO));
                    cartao.setId(cartoesRs.getInt(COL_ID_CARTAO));
                    cartoes.add(cartao);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Cartoes", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (cartoesRs != null) {
                        cartoesRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return cartoes;
    } //done

    @Override
    public boolean insert(Cartao object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();

            pstmt = conn.prepareStatement(CartaoDAO.INSERT_STATEMENT);

            pstmt.setInt(1, object.getIdJogo());
            pstmt.setInt(2, object.getIdJogador());
            pstmt.setString(3, object.getTipo());

            pstmt.execute();

            ResultSet cartaoKeys = pstmt.getGeneratedKeys();
            int idCartao = -1;
            if ((result = pstmt.getUpdateCount() != 0) && cartaoKeys.next()) {
                idCartao = cartaoKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted cartao with ID: " + idCartao);
                object.setId(idCartao);
                pstmt.close();
            }

        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(CartaoDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean delete(Cartao object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(CartaoDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            // Do the insertion, check number of rows updated
            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(CartaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Cartao object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(CartaoDAO.UPDATE_STATEMENT);

            pstmt.setInt(1, object.getIdJogo());
            pstmt.setInt(2, object.getIdJogador());
            pstmt.setString(3, object.getTipo());
            pstmt.setInt(4, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(CartaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public Cartao getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        Cartao cartao = null;
        PreparedStatement pstmt = null, cartaoStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM CARTOES where id_cartao = " + id + ";");
            pstmt.execute();
            ResultSet cartaoRs = pstmt.getResultSet();

            while (cartaoRs.next()) {
                cartao = new Cartao(
                        cartaoRs.getInt(COL_JOGADOR),
                        null,
                        cartaoRs.getInt(COL_JOGO),
                        cartaoRs.getString(COL_TIPO));
                cartao.setId(cartaoRs.getInt(COL_ID_CARTAO));
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
                Logger.getLogger(CartaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return cartao;
    }

    @Override
    public boolean exists(Cartao object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM CARTOES where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_CARTAO + "=" + tmpInt);
                fields++;
            }

            if ((tmpInt = object.getIdJogador()) >= 0) {
                statement.append(COL_JOGADOR + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getIdJogo()) >= 0) {
                statement.append(COL_JOGO + "=" + tmpInt);
                fields++;
            }

            if ((tmpString = object.getTipo()) != null) {
                statement.append(COL_TIPO + "=" + tmpString);
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
                Logger.getLogger(CartaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Cartao> getByCriteria(Cartao object) throws GenericDAOException {
        List<Cartao> cartoes = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement arbitroStmt = null;
        CartaoDAO cartaoDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM CARTOES where ");
            if ((tmpInt = object.getId()) >= 0) {
                statement.append(COL_ID_CARTAO + "=" + tmpInt);
                fields++;
            }

            if ((tmpInt = object.getIdJogador()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_JOGADOR + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getIdJogo()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_JOGO + " = " + tmpInt);
                fields++;
            }
            if ((tmpString = object.getTipo()) != null) {
                statement.append(((fields != 0) ? " AND " : "") + COL_TIPO + " LIKE '%" + tmpString + "%'");
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                ResultSet cartaoRs = stmt.executeQuery(statement.toString());

                Cartao cartao = null;

                while (cartaoRs.next()) {
                    cartao = new Cartao(
                            cartaoRs.getInt(COL_JOGADOR),
                            null,
                            cartaoRs.getInt(COL_JOGO),
                            cartaoRs.getString(COL_TIPO));
                    cartao.setId(cartaoRs.getInt(COL_ID_CARTAO));
                    cartoes.add(cartao);
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting Cartao by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (arbitroStmt != null) {
                    arbitroStmt.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(CartaoDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return cartoes;
    }

    public Collection<Cartao> values() {
        Collection<Cartao> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            CartaoDAO cartaoDAO = new CartaoDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM Cartoes");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new Cartao(rs.getInt(COL_JOGADOR), null, rs.getInt(COL_JOGO), rs.getString(COL_TIPO)));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(CartaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CartaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return col;
    }
}
