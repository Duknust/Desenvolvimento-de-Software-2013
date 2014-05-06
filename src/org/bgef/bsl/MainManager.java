/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.bgef.bsl.domains.Arbitro;
import org.bgef.bsl.domains.Campeonato;
import org.bgef.bsl.domains.CampeonatoTorneio;
import org.bgef.bsl.domains.Cartao;
import org.bgef.bsl.domains.Equipa;
import org.bgef.bsl.domains.Escalao;
import org.bgef.bsl.domains.Escola;
import org.bgef.bsl.domains.Estatisticas;
import org.bgef.bsl.domains.Instalacao;
import org.bgef.bsl.domains.Jogador;
import org.bgef.bsl.domains.Jogo;
import org.bgef.bsl.domains.Relatorio;
import org.bgef.bsl.domains.Torneio;
import org.bgef.bsl.domains.Treinador;
import org.bgef.bsl.domains.Utilizador;
import org.bgef.bsl.exceptions.BslConnectionBrokerUnavailableException;
import org.bgef.bsl.exceptions.GenericBslException;
import org.bgef.bsl.exceptions.MainManagerInitException;

/**
 *
 * @author MrFabio
 */
public class MainManager {

    private static Properties props;
    private ManagerArbitro mArbitro;
    private ManagerUtilizador mUtilizador;
    private ManagerEscalao mEscalao;
    private ManagerInstalacao mInstalacao;
    private ManagerEquipa mEquipa;
    private ManagerRelatorio mRelatorio;
    private ManagerTreinador mTreinador;
    private ManagerEscola mEscola;
    private ManagerJogador mJogador;
    private ManagerTorneio mTorneio;
    private ManagerCampeonato mCampeonato;
    private ManagerCampeonatoTorneio mCampeonatoTorneio;
    private ManagerJogo mJogo;
    private ManagerCartao mCartao;

    public MainManager() throws MainManagerInitException {
        this.props = new Properties();
        try {
            this.props.load(new FileInputStream("props/bgef.properties"));
        } catch (FileNotFoundException ex) {
            throw new MainManagerInitException("Ficheiro properties não encontrado", ex);
        } catch (IOException ex) {
            throw new MainManagerInitException("Não foi possível ler ficheiro properties", ex);
        }

        try {
            this.mArbitro = new ManagerArbitro(props);
            this.mUtilizador = new ManagerUtilizador(props);
            this.mEscalao = new ManagerEscalao(props);
            this.mInstalacao = new ManagerInstalacao(props);
            this.mEquipa = new ManagerEquipa(props);
            this.mRelatorio = new ManagerRelatorio(props);
            this.mTreinador = new ManagerTreinador(props);
            this.mEscola = new ManagerEscola(props);
            this.mJogador = new ManagerJogador(props);
            this.mTorneio = new ManagerTorneio(props);
            this.mCampeonato = new ManagerCampeonato(props);
            this.mCampeonatoTorneio = new ManagerCampeonatoTorneio(props);
            this.mJogo = new ManagerJogo(props);
            this.mCartao = new ManagerCartao(props);
        } catch (BslConnectionBrokerUnavailableException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean insereArbitro(Arbitro arbitro) {
        boolean res = false;
        if (arbitro == null) {
            throw new IllegalArgumentException("Arbitro não pode ser null");
        }

        try {
            res = mArbitro.insereNovo(arbitro);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean insereCartao(Cartao cartao) {
        boolean res = false;
        if (cartao == null) {
            throw new IllegalArgumentException("Cartão não pode ser null");
        }

        try {
            res = mCartao.insereNovo(cartao);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public static Properties getProps() {
        return props;
    }

    public List<Arbitro> getArbitros() {
        if (mArbitro == null) {
            throw new IllegalArgumentException("Manager Arbitros nao pode ser null");
        }
        return mArbitro.getAll();
    }

    public List<Arbitro> getArbitrosByCriteria(Arbitro arbitro) {
        List<Arbitro> arbitros = null;
        if (mArbitro != null) {
            arbitros = mArbitro.procuraPorCaracteristicas(arbitro);
        }
        return arbitros;
    }

    public List<Treinador> getTreinadoresByCriteria(Treinador treinador) {
        List<Treinador> treinadores = null;
        if (mArbitro != null) {
            treinadores = mTreinador.procuraPorCaracteristicas(treinador);
        }
        return treinadores;
    }

    public List<Jogo> getJogosByCriteria(Jogo jogo) {
        List<Jogo> jogadores = null;
        if (mJogo != null) {
            jogadores = mJogo.procuraPorCaracteristicas(jogo);
        }
        return jogadores;
    }

    public boolean updateEscalao(Escalao escalao) {
        boolean res = false;
        if (escalao == null) {
            throw new IllegalArgumentException("Escalao não pode ser null");
        }

        try {
            res = mEscalao.update(escalao);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean updateInstalacao(Instalacao instalacao) {
        boolean res = false;
        if (instalacao == null) {
            throw new IllegalArgumentException("Instalacao não pode ser null");
        }

        try {
            res = mInstalacao.update(instalacao);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean updateUtilizador(Utilizador utilizador) {
        boolean res = false;
        if (utilizador == null) {
            throw new IllegalArgumentException("Utilizador não pode ser null");
        }

        try {
            res = mUtilizador.update(utilizador);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean updateArbitro(Arbitro arbitro) {
        boolean res = false;
        if (arbitro == null) {
            throw new IllegalArgumentException("Arbitro não pode ser null");
        }

        try {
            res = mArbitro.update(arbitro);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean updateRelatorio(Relatorio relatorio) {
        boolean res = false;
        if (relatorio == null) {
            throw new IllegalArgumentException("Relatorio não pode ser null");
        }

        try {
            res = mRelatorio.update(relatorio);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean updateTreinador(Treinador treinador) {
        boolean res = false;
        if (treinador == null) {
            throw new IllegalArgumentException("Treinador não pode ser null");
        }

        try {
            res = mTreinador.update(treinador);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean removeEscalao(Escalao escalao) {
        boolean res = false;
        if (escalao == null) {
            throw new IllegalArgumentException("Escalao não pode ser null");
        }

        try {
            res = mEscalao.remove(escalao);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean removeInstalacao(Instalacao instalacao) {
        boolean res = false;
        if (instalacao == null) {
            throw new IllegalArgumentException("Instalacao não pode ser null");
        }

        try {
            res = mInstalacao.remove(instalacao);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean removeUtilizador(Utilizador utilizador) {
        boolean res = false;
        if (utilizador == null) {
            throw new IllegalArgumentException("Utilizador não pode ser null");
        }

        try {
            res = mUtilizador.remove(utilizador);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean removeCampeonato(Campeonato campeonato) {
        boolean res = false;
        if (campeonato == null) {
            throw new IllegalArgumentException("Campeonato não pode ser null");
        }

        try {
            res = mCampeonato.remove(campeonato);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean removeTorneio(Torneio torneio) {
        boolean res = false;
        if (torneio == null) {
            throw new IllegalArgumentException("Torneio não pode ser null");
        }

        try {
            res = mTorneio.remove(torneio);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean updateJogador(Jogador jogador) {
        boolean res = false;
        if (jogador == null) {
            throw new IllegalArgumentException("Jogador não pode ser null");
        }

        try {
            res = mJogador.update(jogador);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean updateEquipa(Equipa equipa) {
        boolean res = false;
        if (equipa == null) {
            throw new IllegalArgumentException("Equipa não pode ser null");
        }

        try {
            res = mEquipa.update(equipa);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean updateEscola(Escola escola) {
        boolean res = false;
        if (escola == null) {
            throw new IllegalArgumentException("Escola não pode ser null");
        }

        try {
            res = mEscola.update(escola);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean updateJogo(Jogo jogo) {
        boolean res = false;
        if (jogo == null) {
            throw new IllegalArgumentException("Jogo não pode ser null");
        }

        try {
            res = mJogo.update(jogo);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean updateCampeonato(Campeonato campeonato) {
        boolean res = false;
        if (campeonato == null) {
            throw new IllegalArgumentException("Campeonato não pode ser null");
        }

        try {
            res = mCampeonato.update(campeonato);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean updateTorneio(Torneio torneio) {
        boolean res = false;
        if (torneio == null) {
            throw new IllegalArgumentException("Torneio não pode ser null");
        }

        try {
            res = mTorneio.update(torneio);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public boolean updateCampeonatoTorneio(CampeonatoTorneio ct) {
        boolean res = false;
        if (ct == null) {
            throw new IllegalArgumentException("Campeonato Torneio não pode ser null");
        }

        try {
            res = mCampeonatoTorneio.update(ct);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public List<Jogador> getJogadoresByCriteria(Jogador jogador) {
        List<Jogador> jogadores = null;
        if (mJogador != null) {
            jogadores = mJogador.procuraPorCaracteristicas(jogador);
        }
        return jogadores;
    }

    public List<Instalacao> getInstalacoesByCriteria(Instalacao instalacao) {
        List<Instalacao> instalacoes = null;
        if (mInstalacao != null) {
            instalacoes = mInstalacao.procuraPorCaracteristicas(instalacao);
        }
        return instalacoes;
    }

    public List<Escalao> getEscaloesByCriteria(Escalao escalao) {
        List<Escalao> escaloes = null;
        if (mEscalao != null) {
            escaloes = mEscalao.procuraPorCaracteristicas(escalao);
        }
        return escaloes;
    }

    public List<Escola> getEscolasByCriteria(Escola escola) {
        List<Escola> escolas = null;
        if (mEscola != null) {
            escolas = mEscola.procuraPorCaracteristicas(escola);
        }
        return escolas;
    }

    public List<Equipa> getEquipasByCriteria(Equipa equipa) {
        List<Equipa> equipas = null;
        if (mEscola != null) {
            equipas = mEquipa.procuraPorCaracteristicas(equipa);
        }
        return equipas;
    }

    public List<Utilizador> getUtilizadores() {
        List<Utilizador> utilizadores = null;
        if (mUtilizador != null) {
            utilizadores = mUtilizador.getAll();
        }
        return utilizadores;
    }

    public List<Utilizador> getUtilizadoresByCriteria(Utilizador utilizador) {
        List<Utilizador> utilizadores = null;
        if (mUtilizador != null) {
            utilizadores = mUtilizador.procuraPorCaracteristicas(utilizador);
        }
        return utilizadores;
    }

    public boolean insereUtilizador(Utilizador novo) {
        boolean res = true;
        if (mUtilizador != null) {
            try {
                mUtilizador.insereNovo(novo);
            } catch (GenericBslException ex) {
                Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            res = false;
        }
        return res;
    }

    public List<Escalao> getEscaloes() {
        List<Escalao> escaloes = null;
        if (mEscalao != null) {
            escaloes = mEscalao.getAll();
        }
        return escaloes;
    }

    public List<Instalacao> getInstalacoes() {
        List<Instalacao> instalacoes = null;
        if (mEscalao != null) {
            instalacoes = mInstalacao.getAll();
        }
        return instalacoes;
    }

    public List<Relatorio> getRelatorios() {
        List<Relatorio> relatorios = null;
        if (mRelatorio != null) {
            relatorios = mRelatorio.getAll();
        }
        return relatorios;
    }

    public List<Treinador> getTreinadores() {
        List<Treinador> treinadores = null;
        if (mTreinador != null) {
            treinadores = mTreinador.getAll();
        }
        return treinadores;
    }

    public List<Escola> getEscolas() {
        List<Escola> escolas = null;
        if (mEscola != null) {
            escolas = mEscola.getAll();
        }
        return escolas;
    }

    public List<Jogo> getJogos() {
        List<Jogo> jogos = null;
        if (mJogo != null) {
            jogos = mJogo.getAll();
            for (Jogo j : jogos) {
                int idEqCasa = j.getIdEquipaCasa();
                int idEqFora = j.getIdEquipaFora();
                Equipa eqCasa = this.getEquipaById(idEqCasa);
                Equipa eqFora = this.getEquipaById(idEqFora);
                j.setEquipaCasa(eqCasa);
                j.setEquipaFora(eqFora);
            }
        }
        return jogos;
    }

    public List<Jogador> getJogadores() {
        List<Jogador> jogadores = null;
        if (mJogador != null) {
            jogadores = mJogador.getAll();
        }
        return jogadores;
    }

    public List<Equipa> getEquipas() {
        List<Equipa> equipas = null;
        if (mEquipa != null) {
            equipas = mEquipa.getAll();
        }
        return equipas;
    }

    public List<Campeonato> getCampeonatos() {
        List<Campeonato> campeonatos = null;
        if (mCampeonato != null) {
            campeonatos = mCampeonato.getAll();
        }
        return campeonatos;
    }

    public void calculaTabelaClassificativa(Campeonato camp, DefaultTableModel dtm) {

        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }

        int i = 1;

        this.getEquipasCampeonato(camp);

        class tabequipa implements Comparable<tabequipa> {

            public String Nome;
            public int Jogos;
            public int Ganhos;
            public int Perdidos;
            public int Empatados;
            public int GolosM;
            public int GolosS;
            public int Pontos;

            public tabequipa(String Nome) {
                this.Nome = Nome;
                this.Jogos = 0;
                this.Ganhos = 0;
                this.Perdidos = 0;
                this.Empatados = 0;
                this.GolosM = 0;
                this.GolosS = 0;
                this.Pontos = 0;
            }

            @Override
            public int compareTo(tabequipa o) {
                if (this.Pontos < o.Pontos) {
                    return 1;
                } else if (this.GolosM - this.GolosS < o.GolosM - o.GolosS) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }

        List<Equipa> lista = camp.getEquipas();
        HashMap<String, tabequipa> tabelaequipas = new HashMap<>();

        for (Equipa e : lista) {
            tabequipa tab = new tabequipa(e.getNome());
            tabelaequipas.put(e.getNome(), tab);

        }

        List<Jogo> listajogos = this.getJogosCampeonato(camp.getId());

        for (Jogo j : listajogos) {

            Relatorio rel = this.getRelatorioJogo(j.getId());
            if (rel != null) {
                //NABO
                if (j.getEquipaCasa() == null && j.getEquipaFora() == null) {
                    Equipa equipaCasa = this.getEquipaById(j.getIdEquipaCasa());
                    Equipa equipaFora = this.getEquipaById(j.getIdEquipaFora());
                    j.setEquipaCasa(equipaCasa);
                    j.setEquipaFora(equipaFora);
                }
                String NomeA = j.getEquipaCasa().getNome();
                String NomeB = j.getEquipaFora().getNome();

                int GolosA = rel.getGolosCasa();
                int GolosB = rel.getGolosFora();

                tabequipa equipaA = tabelaequipas.get(NomeA);
                tabequipa equipaB = tabelaequipas.get(NomeB);

                //EQUIPA DA CASA GANHA
                if (GolosA > GolosB) {

                    equipaA.Jogos++;
                    equipaA.Ganhos++;
                    equipaA.GolosM += GolosA;
                    equipaA.GolosS += GolosB;
                    equipaA.Pontos += 3;

                    equipaB.Jogos++;
                    equipaB.Perdidos++;
                    equipaB.GolosM += GolosB;
                    equipaB.GolosS += GolosA;

                } else if (GolosA < GolosB)//EQUIPA FORA GANHA
                {

                    equipaA.Jogos++;
                    equipaA.Perdidos++;
                    equipaA.GolosM += GolosA;
                    equipaA.GolosS += GolosB;

                    equipaB.Jogos++;
                    equipaB.Ganhos++;
                    equipaB.GolosM += GolosB;
                    equipaB.GolosS += GolosA;
                    equipaB.Pontos += 3;

                }//EMPATE
                else {

                    equipaA.Jogos++;
                    equipaA.Empatados++;
                    equipaA.GolosM += GolosA;
                    equipaA.GolosS += GolosB;
                    equipaA.Pontos += 1;

                    equipaB.Jogos++;
                    equipaB.Empatados++;
                    equipaB.GolosM += GolosB;
                    equipaB.GolosS += GolosA;
                    equipaB.Pontos += 1;
                }
            }
        }

        Set<tabequipa> listav = new TreeSet<>();

        for (tabequipa a : tabelaequipas.values()) {
            listav.add(a);
        }

        for (tabequipa a : listav) {
            dtm.addRow(new Object[]{i, a.Nome, a.Jogos, a.Ganhos, a.Empatados, a.Perdidos, a.GolosM, a.GolosS, a.GolosM - a.GolosS, a.Pontos});
            i++;
        }
    }

    public void getEquipasCampeonato(Campeonato c) {
        if (mCampeonato == null) {
            throw new IllegalArgumentException("Manager Campeonato nao pode ser null");
        } else {
            mCampeonato.getEquipas(c);
        }
    }

    public void getJogadoresEquipa(Equipa e) {
        if (mEquipa == null) {
            throw new IllegalArgumentException("Manager Equipa nao pode ser null");
        } else {
            mEquipa.getJogadores(e);
        }
    }

    public Cartao getCartaoById(int idCartao) {
        Cartao cartao = null;
        if (mCartao != null) {
            cartao = mCartao.procuraPorId(idCartao);
        }
        return cartao;
    }

    public Instalacao getInstalacaoById(int idInstalacao) {
        Instalacao instalacao = null;
        if (mInstalacao != null) {
            instalacao = mInstalacao.procuraPorId(idInstalacao);
        }
        return instalacao;
    }

    public Jogador getJogadorById(int idJogador) {
        Jogador jogador = null;
        if (mJogador != null) {
            jogador = mJogador.procuraPorId(idJogador);
        }
        return jogador;
    }

    public Escola getEscolaById(int idEscola) {
        Escola escola = null;
        if (mEscola != null) {
            escola = mEscola.procuraPorId(idEscola);
        }
        return escola;
    }

    public Escalao getEscalaoById(int idEscalao) {
        Escalao escalao = null;
        if (mEscalao != null) {
            escalao = mEscalao.procuraPorId(idEscalao);
        }
        return escalao;
    }

    public List<Torneio> getTorneios() {
        List<Torneio> torneios = null;
        if (mTorneio != null) {
            torneios = mTorneio.getAll();
        }
        return torneios;
    }

    public List<CampeonatoTorneio> getCampeonatoTorneios() {
        List<CampeonatoTorneio> campeonatos = null;
        if (mCampeonatoTorneio != null) {
            campeonatos = mCampeonatoTorneio.getAll();
        }
        return campeonatos;
    }

    public Campeonato getCampeonatoById(int idCampeonato) {
        Campeonato campeonato = null;
        if (mCampeonato != null) {
            campeonato = mCampeonato.procuraPorId(idCampeonato);
        }
        return campeonato;
    }

    public List<Jogo> getJogosCampeonato(int idCampeonato) {
        List<Jogo> jogos = null;
        if (mJogo != null) {
            Jogo jogo = new Jogo();
            jogo.setIdCampeonato(idCampeonato);
            jogos = mJogo.procuraPorCaracteristicas(jogo);
        }
        return jogos;
    }

    public List<Jogo> getJogosTorneio(int idTorneio) {
        List<Jogo> jogos = null;
        if (mJogo != null) {
            jogos = mJogo.getJogosTorneio(idTorneio);
        }
        return jogos;
    }

    public List<Cartao> getCartoesJogo(int idJogo) {
        List<Cartao> cartoes = null;
        if (mCartao != null) {
            Cartao cartao = new Cartao();
            cartao.setIdJogo(idJogo);
            cartoes = mCartao.procuraPorCaracteristicas(cartao);
        }
        return cartoes;
    }

    public Relatorio getRelatorioJogo(int idJogo) {
        Relatorio relatorio = null;
        if (mJogo != null) {
            try {
                Jogo jogo = mJogo.procuraPorId(idJogo);
                if (jogo.getRelatorio() == null) {
                    jogo.setRelatorio(this.getRelatorioById(jogo.getIdRelatorio()));
                }
                relatorio = jogo.getRelatorio();
            } catch (GenericBslException ex) {
                Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return relatorio;
    }

    public Equipa getEquipaById(int idEquipa) {
        Equipa equipa = null;
        if (mEquipa != null) {
            equipa = mEquipa.procuraPorId(idEquipa);
        }
        return equipa;
    }

    public Relatorio getRelatorioById(int idRelatorio) {
        Relatorio relatorio = null;
        if (mRelatorio != null) {
            relatorio = mRelatorio.procuraPorId(idRelatorio);
        }
        return relatorio;
    }

    public boolean insereEscalao(Escalao escalao) {
        boolean res = false;
        if (escalao == null) {
            throw new IllegalArgumentException("Escalao não pode ser null");
        }

        try {
            res = mEscalao.insereNovo(escalao);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public boolean insereInstalacao(Instalacao instalacao) {
        boolean res = false;
        if (instalacao == null) {
            throw new IllegalArgumentException("Instalacao não pode ser null");
        }

        try {
            res = mInstalacao.insereNovo(instalacao);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public boolean insereEscola(Escola escola) {
        boolean res = false;
        if (escola == null) {
            throw new IllegalArgumentException("Escola não pode ser null");
        }

        try {
            res = mEscola.insereNovo(escola);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public boolean insereEquipa(Equipa equipa) {
        boolean res = false;
        if (equipa == null) {
            throw new IllegalArgumentException("Equipa não pode ser null");
        }

        try {
            res = mEquipa.insereNovo(equipa);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public boolean insereJogador(Jogador jogador) {
        boolean res = false;
        if (jogador == null) {
            throw new IllegalArgumentException("Jogador não pode ser null");
        }

        try {
            res = mJogador.insereNovo(jogador);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public boolean insereTreinador(Treinador treinador) {
        boolean res = false;
        if (treinador == null) {
            throw new IllegalArgumentException("Jogador não pode ser null");
        }

        try {
            res = mTreinador.insereNovo(treinador);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public boolean insereRelatorio(Relatorio relatorio) {
        boolean res = false;
        if (relatorio == null) {
            throw new IllegalArgumentException("Jogador não pode ser null");
        }

        try {
            res = mRelatorio.insereNovo(relatorio);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public Torneio getTorneioById(int idTorneio) {

        Torneio torneio = null;
        if (mTorneio != null) {
            torneio = mTorneio.procuraPorId(idTorneio);
        }
        return torneio;
    }

    public void getEquipasTorneio(Torneio torn) {
        if (mTorneio == null) {
            throw new IllegalArgumentException("Manager Campeonato nao pode ser null");
        } else {
            mTorneio.getEquipas(torn);
        }
    }

    private Map<Integer, Estatisticas> calculaEstatisticasEpoca(int epoca) {
        List<Jogo> jogos = null;
        Map<Integer, Estatisticas> estatisticas = null;
        if (mJogo == null || mRelatorio == null) {
            throw new IllegalArgumentException("Manager jogo e/ou manager relatorio nao podem ser null");
        } else {
            estatisticas = new HashMap<>();
            if (epoca == -1) {
                jogos = mJogo.getAll();
            } else {
                Jogo jogo = new Jogo();
                jogo.setAno(epoca);
                jogos = mJogo.procuraPorCaracteristicas(jogo);
            }
            for (Jogo j : jogos) {
                estatisticas.put(j.getIdEquipaCasa(), new Estatisticas(j.getIdEquipaCasa()));
                estatisticas.put(j.getIdEquipaFora(), new Estatisticas(j.getIdEquipaFora()));
            }
        }
        if (jogos != null) {
            for (Jogo j : jogos) {
                Estatisticas estatActualizadasCasa = estatisticas.get(j.getIdEquipaCasa());
                Estatisticas estatActualizadasFora = estatisticas.get(j.getIdEquipaFora());
                int idrelatorio = j.getIdRelatorio();
                Relatorio relatorio = this.getRelatorioById(idrelatorio);
                if (relatorio != null) {
                    estatActualizadasCasa.actualizaEstatisticas(relatorio.getGolosCasa(), relatorio.getGolosFora(), relatorio.getCartoesAmarelos(), relatorio.getCartoesVermelhos(), 0, 0, 0);
                    estatActualizadasFora.actualizaEstatisticas(relatorio.getGolosFora(), relatorio.getGolosCasa(), relatorio.getCartoesAmarelos(), relatorio.getCartoesVermelhos(), 0, 0, 0);
                }
            }
        }
        return estatisticas;
    }

    public List<Estatisticas> estatisticasGolosMarcadosEpoca(int epoca) {
        Map<Integer, Estatisticas> estatisticas = this.calculaEstatisticasEpoca(epoca);
        List<Estatisticas> calculadas = new ArrayList<>();
        Estatisticas total = new Estatisticas(-1);
        calculadas.add(0, new Estatisticas(-1));
        calculadas.add(1, new Estatisticas(-1));
        calculadas.add(2, new Estatisticas(-1));
        for (Estatisticas est : estatisticas.values()) {
            if (est.getGolosMarcados() > calculadas.get(0).getGolosMarcados()) {
                calculadas.set(2, calculadas.get(1));
                calculadas.set(1, calculadas.get(0));
                calculadas.set(0, est);
            } else if (est.getGolosMarcados() > calculadas.get(1).getGolosMarcados()) {
                calculadas.set(2, calculadas.get(1));
                calculadas.set(1, est);
            } else if (est.getGolosMarcados() > calculadas.get(2).getGolosMarcados()) {
                calculadas.set(2, est);
            }
            total.setTotalGolos(total.getTotalGolos() + est.getGolosMarcados() + est.getGolosSofridos());
        }
        if (calculadas.size() > 0) {
            calculadas.get(0).setNomeEquipa(this.getEquipaById(calculadas.get(0).getIdEquipa()).getNome());
        }
        if (calculadas.size() > 1) {
            calculadas.get(1).setNomeEquipa(this.getEquipaById(calculadas.get(1).getIdEquipa()).getNome());
        }
        if (calculadas.size() > 2) {
            calculadas.get(2).setNomeEquipa(this.getEquipaById(calculadas.get(2).getIdEquipa()).getNome());
        }
        calculadas.add(total);
        return calculadas;
    }

    public List<Estatisticas> estatisticasCartoesAmarelosEpoca(int epoca) {
        Map<Integer, Estatisticas> estatisticas = this.calculaEstatisticasEpoca(epoca);
        List<Estatisticas> calculadas = new ArrayList<>();
        Estatisticas total = new Estatisticas(-1);
        calculadas.add(0, new Estatisticas(-1));
        calculadas.add(1, new Estatisticas(-1));
        calculadas.add(2, new Estatisticas(-1));
        for (Estatisticas est : estatisticas.values()) {
            if (est.getCartoesAmarelos() >= calculadas.get(0).getCartoesAmarelos()) {
                calculadas.set(2, calculadas.get(1));
                calculadas.set(1, calculadas.get(0));
                calculadas.set(0, est);
            } else if (est.getCartoesAmarelos() >= calculadas.get(1).getCartoesAmarelos()) {
                calculadas.set(2, calculadas.get(1));
                calculadas.set(1, est);
            } else if (est.getCartoesAmarelos() >= calculadas.get(2).getCartoesAmarelos()) {
                calculadas.set(2, est);
            }
            total.setTotalCartoesAmarelos(total.getTotalCartoesAmarelos() + est.getCartoesAmarelos());
        }
        if (calculadas.size() > 0) {
            Estatisticas es = calculadas.get(0);
            if (es.getIdEquipa() > 0) {
                es.setNomeEquipa(this.getEquipaById(es.getIdEquipa()).getNome());
                calculadas.set(0, es);
            }
        }
        if (calculadas.size() > 1) {
            Estatisticas es = calculadas.get(1);
            if (es.getIdEquipa() > 0) {
                es.setNomeEquipa(this.getEquipaById(es.getIdEquipa()).getNome());
                calculadas.set(1, es);
            }
        }
        if (calculadas.size() > 2) {
            Estatisticas es = calculadas.get(2);
            if (es.getIdEquipa() > 0) {
                es.setNomeEquipa(this.getEquipaById(es.getIdEquipa()).getNome());
                calculadas.set(2, es);
            }
        }
        calculadas.add(total);
        return calculadas;
    }

    public List<Estatisticas> estatisticasCartoesVermelhosEpoca(int epoca) {
        Map<Integer, Estatisticas> estatisticas = this.calculaEstatisticasEpoca(epoca);
        List<Estatisticas> calculadas = new ArrayList<>();
        Estatisticas total = new Estatisticas(-1);
        calculadas.add(0, new Estatisticas(-1));
        calculadas.add(1, new Estatisticas(-1));
        calculadas.add(2, new Estatisticas(-1));
        for (Estatisticas est : estatisticas.values()) {
            if (est.getCartoesVermelhos() > calculadas.get(0).getCartoesVermelhos()) {
                calculadas.set(2, calculadas.get(1));
                calculadas.set(1, calculadas.get(0));
                calculadas.set(0, est);
            } else if (est.getCartoesVermelhos() > calculadas.get(1).getCartoesVermelhos()) {
                calculadas.set(2, calculadas.get(1));
                calculadas.set(1, est);
            } else if (est.getCartoesVermelhos() > calculadas.get(2).getCartoesVermelhos()) {
                calculadas.set(2, est);
            }
            total.setTotalCartoesVermelhos(total.getTotalCartoesVermelhos() + est.getCartoesVermelhos());
        }
        if (calculadas.size() > 0) {
            Estatisticas es = calculadas.get(0);
            if (es.getIdEquipa() > 0) {
                es.setNomeEquipa(this.getEquipaById(es.getIdEquipa()).getNome());
                calculadas.set(0, es);
            }
        }
        if (calculadas.size() > 1) {
            Estatisticas es = calculadas.get(1);
            if (es.getIdEquipa() > 0) {
                es.setNomeEquipa(this.getEquipaById(es.getIdEquipa()).getNome());
                calculadas.set(1, es);
            }
        }
        if (calculadas.size() > 2) {
            Estatisticas es = calculadas.get(2);
            if (es.getIdEquipa() > 0) {
                es.setNomeEquipa(this.getEquipaById(es.getIdEquipa()).getNome());
                calculadas.set(2, es);
            }
        }
        calculadas.add(total);
        return calculadas;
    }

    public Estatisticas estatisticasLenhadorEpoca(int epoca) {
        Map<Integer, Estatisticas> estatisticas = this.calculaEstatisticasEpoca(epoca);
        Estatisticas calculada = new Estatisticas(-1);
        for (Estatisticas est : estatisticas.values()) {
            if (est.getCartoesVermelhos() + est.getCartoesAmarelos() > calculada.getCartoesVermelhos() + calculada.getCartoesAmarelos()) {
                calculada = est;
            }
            if (calculada.getIdEquipa() > 0) {
                calculada.setNomeEquipa(this.getEquipaById(calculada.getIdEquipa()).getNome());
            }
        }
        return calculada;
    }

    public boolean insereCampeonato(Campeonato campeonato) {
        boolean res = false;
        if (campeonato == null) {
            throw new IllegalArgumentException("Campeonato não pode ser null");
        }

        try {
            res = mCampeonato.insereNovo(campeonato);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public boolean insereJogo(Jogo jogo) {
        boolean res = false;
        if (jogo == null) {
            throw new IllegalArgumentException("Jogo não pode ser null");
        }

        try {
            res = mJogo.insereNovo(jogo);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public List<Jogo> getJogosAllTorneios() {
        List<Jogo> jogos = null;
        if (mJogo == null) {
            throw new IllegalArgumentException("Manager Arbitros nao pode ser null");
        }
        try {
            jogos = mJogo.getAllJogosTorneio();
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jogos;
    }

    public List<Jogo> getJogosAllCampeonatos() {
        List<Jogo> jogos = null;
        if (mJogo == null) {
            throw new IllegalArgumentException("Manager Arbitros nao pode ser null");
        }
        try {
            jogos = mJogo.getAllJogosCampeonato();
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jogos;
    }

    public boolean insereTorneio(Torneio torneio) {
        boolean res = false;
        if (torneio == null) {
            throw new IllegalArgumentException("Jogo não pode ser null");
        }

        try {
            res = mTorneio.insereNovo(torneio);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public boolean insereCampeonatoTorneio(CampeonatoTorneio ct) {
        boolean res = false;
        if (ct == null) {
            throw new IllegalArgumentException("Jogo não pode ser null");
        }

        try {
            res = mCampeonatoTorneio.insereNovo(ct);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public List<Jogo> getJogosSemRelatorio() {
        List<Jogo> jogos = null;
        if (mJogo == null) {
            throw new IllegalArgumentException("Manager Arbitros nao pode ser null");
        }
        try {
            jogos = mJogo.getJogosSemRelatorio();
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jogos;
    }

    public CampeonatoTorneio getCampeonatoTorneio(int idCampeonato) {
        CampeonatoTorneio campt = null;
        if (mCampeonatoTorneio == null) {
            throw new IllegalArgumentException("Manager CampeonatoTorneio nao pode ser null");
        }
        try {
            campt = mCampeonatoTorneio.getCampeonatoTorneio(idCampeonato);
        } catch (GenericBslException ex) {
            Logger.getLogger(MainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return campt;
    }
}
