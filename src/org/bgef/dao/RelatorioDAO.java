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
import org.bgef.bsl.domains.Relatorio;
import org.bgef.dao.db.IConnectionBroker;
import org.bgef.dao.exceptions.DatabaseConnectionDAOException;
import org.bgef.dao.exceptions.GenericDAOException;
import org.bgef.dao.exceptions.StatementExecuteDAOException;

/**
 *
 * @author duarteduarte
 */
public class RelatorioDAO extends GenericDAO<Relatorio> {

    /**
     * Column names
     */
    private final static String COL_ID_RELATORIO = "ID_RELATORIO";
    private final static String COL_CARTOES_VERMELHOS = "CARTOES_VERMELHOS";
    private final static String COL_CARTOES_AMARELOS = "CARTOES_AMARELOS";
    private final static String COL_FALTAS = "FALTAS";
    private final static String COL_GOLOS_CASA = "GOLOS_CASA";
    private final static String COL_GOLOS_FORA = "GOLOS_FORA";
    private final static String COL_ID_ARBITRO = "ID_ARBITRO";
    /**
     * Prepared statements
     */
    static String INSERT_STATEMENT = "insert into RELATORIOS ( "
            + COL_CARTOES_AMARELOS + ","
            + COL_CARTOES_VERMELHOS + ","
            + COL_FALTAS + ","
            + COL_GOLOS_CASA + ","
            + COL_GOLOS_FORA + ","
            + COL_ID_ARBITRO + ") "
            + "values (?, ?, ?, ?, ?, ?)";
    static String DELETE_STATEMENT = "delete from RELATORIOS "
            + "where ID_RELATORIO=?";
    static String UPDATE_STATEMENT = "update RELATORIOS set "
            + COL_CARTOES_AMARELOS + "=?, "
            + COL_CARTOES_VERMELHOS + "=?, "
            + COL_FALTAS + "=?, "
            + COL_GOLOS_CASA + "=?, "
            + COL_GOLOS_FORA + "=?, "
            + COL_ID_RELATORIO + "=? "
            + "where ID_RELATORIO=?";

    public RelatorioDAO(IConnectionBroker cb) {
        this.cb = cb;
    }

    @Override
    public List<Relatorio> getAll() throws GenericDAOException {
        List<Relatorio> relatorios = new ArrayList<Relatorio>();
        PreparedStatement pstmt = null;
        ResultSet relatoriosRs = null;

        try {
            Connection conn = cb.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM relatorios");

            try {
                pstmt.execute();
                relatoriosRs = pstmt.getResultSet();
                Relatorio relatorio = null;
                while (relatoriosRs.next()) {
                    relatorio = new Relatorio(
                            relatoriosRs.getInt(COL_CARTOES_AMARELOS),
                            relatoriosRs.getInt(COL_CARTOES_VERMELHOS),
                            relatoriosRs.getInt(COL_FALTAS),
                            relatoriosRs.getInt(COL_GOLOS_CASA),
                            relatoriosRs.getInt(COL_GOLOS_FORA),
                            relatoriosRs.getInt(COL_ID_ARBITRO));
                    relatorio.setId(relatoriosRs.getInt(COL_ID_RELATORIO));
                    relatorios.add(relatorio);
                }
            } catch (SQLException ex) {
                throw new StatementExecuteDAOException("Nao consegui obter a lista de todos os Relatorios", ex);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (relatoriosRs != null) {
                        relatoriosRs.close();
                    }
                } catch (SQLException e) {
                }
            }
        } catch (SQLException sqle) {
            throw new DatabaseConnectionDAOException("Nao consegui abrir ligacao a BD", sqle);
        } catch (GenericDAOException gde) {
            throw gde;
        }

        return relatorios;
    } //done

    @Override
    public boolean insert(Relatorio object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();

            pstmt = conn.prepareStatement(RelatorioDAO.INSERT_STATEMENT);

            pstmt.setInt(1, object.getCartoesAmarelos());
            pstmt.setInt(2, object.getCartoesVermelhos());
            pstmt.setInt(3, object.getnFaltas());
            pstmt.setInt(4, object.getGolosCasa());
            pstmt.setInt(5, object.getGolosFora());
            pstmt.setInt(6, object.getIdArbitro());

            pstmt.execute();

            ResultSet relatorioKeys = pstmt.getGeneratedKeys();
            int idRelatorio = -1;
            if ((result = pstmt.getUpdateCount() != 0) && relatorioKeys.next()) {
                idRelatorio = relatorioKeys.getInt(1);
                System.out.println(pstmt.getUpdateCount() + " rows updated");
                System.out.println("Inserted relatorio with ID: " + idRelatorio);
                object.setId(idRelatorio);
                pstmt.close();
            }

        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing insert statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(RelatorioDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean delete(Relatorio object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(RelatorioDAO.DELETE_STATEMENT);
            pstmt.setInt(1, object.getId());

            // Do the insertion, check number of rows updated
            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;
            System.out.println(pstmt.getUpdateCount() + " rows updated");
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing delete statement ", sqle);
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(RelatorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Relatorio object) throws StatementExecuteDAOException, DatabaseConnectionDAOException {
        boolean result = false;
        try {
            Connection conn = this.cb.getConnection();
            PreparedStatement pstmt
                    = conn.prepareStatement(RelatorioDAO.UPDATE_STATEMENT);

            pstmt.setInt(1, object.getCartoesAmarelos());
            pstmt.setInt(2, object.getCartoesVermelhos());
            pstmt.setInt(3, object.getnFaltas());
            pstmt.setInt(4, object.getGolosCasa());
            pstmt.setInt(5, object.getGolosFora());
            pstmt.setInt(6, object.getIdArbitro());
            pstmt.setInt(7, object.getId());

            pstmt.execute();
            result = pstmt.getUpdateCount() != 0;

        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(RelatorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error executing update statement ", sqle);
        }
        return result;
    }

    @Override
    public Relatorio getById(int id) throws DatabaseConnectionDAOException, StatementExecuteDAOException {
        Relatorio relatorio = null;
        PreparedStatement pstmt = null, cartaoStmt = null;
        Connection conn = cb.getConnection();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM RELATORIOS where id_relatorio = " + id + ";");
            pstmt.execute();
            ResultSet relatorioRs = pstmt.getResultSet();

            while (relatorioRs.next()) {
                relatorio = new Relatorio(
                        relatorioRs.getInt(COL_CARTOES_AMARELOS),
                        relatorioRs.getInt(COL_CARTOES_VERMELHOS),
                        relatorioRs.getInt(COL_FALTAS),
                        relatorioRs.getInt(COL_GOLOS_CASA),
                        relatorioRs.getInt(COL_GOLOS_FORA),
                        relatorioRs.getInt(COL_ID_ARBITRO));
                relatorio.setId(relatorioRs.getInt(COL_ID_RELATORIO));
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
                Logger.getLogger(RelatorioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return relatorio;
    }

    @Override
    public boolean exists(Relatorio object) throws GenericDAOException {
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM RELATORIOS where ");
            if ((tmpInt = object.getId()) > 0) {
                statement.append(COL_ID_RELATORIO + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getCartoesAmarelos()) >= 0) {
                statement.append(COL_CARTOES_AMARELOS + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getCartoesVermelhos()) >= 0) {
                statement.append(COL_CARTOES_VERMELHOS + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getnFaltas()) >= 0) {
                statement.append(COL_FALTAS + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getGolosCasa()) >= 0) {
                statement.append(COL_GOLOS_CASA + "=" + tmpInt);
                fields++;
            }
            if ((tmpInt = object.getGolosFora()) >= 0) {
                statement.append(COL_GOLOS_FORA + "=" + tmpInt);
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
                Logger.getLogger(RelatorioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Relatorio> getByCriteria(Relatorio object) throws GenericDAOException {
        List<Relatorio> relatorios = new ArrayList<>();
        Connection conn = cb.getConnection();
        int fields = 0;
        String tmpString = null;
        int tmpInt = 0;
        boolean result = false;
        Statement stmt = null;
        PreparedStatement relatorioStmt = null;
        RelatorioDAO relatorioDAO = null;

        try {
            StringBuilder statement = new StringBuilder("SELECT * FROM RELATORIOS where ");
            if ((tmpInt = object.getId()) >= 0) {
                statement.append(COL_ID_RELATORIO + "=" + tmpInt);
                fields++;
            }

            if ((tmpInt = object.getCartoesAmarelos()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_CARTOES_AMARELOS + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getCartoesVermelhos()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_CARTOES_VERMELHOS + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getnFaltas()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_FALTAS + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getGolosCasa()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_GOLOS_CASA + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getGolosFora()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_GOLOS_FORA + " LIKE '%" + tmpInt + "%'");
                fields++;
            }
            if ((tmpInt = object.getIdArbitro()) >= 0) {
                statement.append(((fields != 0) ? " AND " : "") + COL_ID_ARBITRO + " LIKE '%" + tmpInt + "%'");
                fields++;
            }

            if (fields > 0) {
                stmt = conn.createStatement();
                ResultSet relatorioRs = stmt.executeQuery(statement.toString());

                Relatorio relatorio = null;

                while (relatorioRs.next()) {
                    relatorio = new Relatorio(
                            relatorioRs.getInt(COL_CARTOES_AMARELOS),
                            relatorioRs.getInt(COL_CARTOES_VERMELHOS),
                            relatorioRs.getInt(COL_FALTAS),
                            relatorioRs.getInt(COL_GOLOS_CASA),
                            relatorioRs.getInt(COL_GOLOS_FORA),
                            relatorioRs.getInt(COL_ID_ARBITRO));
                    relatorioStmt.setInt(1, relatorio.getId());
                    relatorioStmt.execute();
                    relatorios.add(relatorio);
                }
            }
        } catch (SQLException sqle) {
            throw new StatementExecuteDAOException("Error getting relatorio by criteria", sqle);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (relatorioStmt != null) {
                    relatorioStmt.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(RelatorioDAO.class
                        .getName()).log(Level.INFO, null, ex);
            }
        }

        return relatorios;
    }

    public Collection<Relatorio> values() {
        Collection<Relatorio> col = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = cb.getConnection();
            RelatorioDAO relatorioDAO = new RelatorioDAO(cb);
            stmt = conn.prepareStatement("SELECT * FROM Relatorios");
            ResultSet rs = stmt.executeQuery();
            for (; rs.next();) {
                col.add(new Relatorio(rs.getInt(COL_CARTOES_AMARELOS), rs.getInt(COL_CARTOES_VERMELHOS), rs.getInt(COL_FALTAS), rs.getInt(COL_GOLOS_CASA), rs.getInt(COL_GOLOS_FORA), rs.getInt(COL_ID_ARBITRO)));
            }
        } catch (DatabaseConnectionDAOException ex) {
            Logger.getLogger(RelatorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RelatorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return col;
    }
}
