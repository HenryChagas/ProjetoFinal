package com.gps.dao;

/**
 *
 * @author Henry Wallace
 */

import com.gps.config.Conexao;
import com.gps.model.OrdemServico;
import com.gps.model.GrupoTrabalho;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdemServicoDAO {

    // Create (Cadastro de Nova O.S.)
    public void create(OrdemServico os) throws SQLException {
        String sql = "INSERT INTO OrdemServico (idGrupo_fk, solicitante, localAndar, localPorta, danoDescricao, ramal, entregue, horaEntrega, numeroOSImpressa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = Conexao.getConnection();
            stmt = con.prepareStatement(sql);

            stmt.setInt(1, os.getGrupo().getIdGrupo());
            stmt.setString(2, os.getSolicitante());
            stmt.setInt(3, os.getLocalAndar());
            stmt.setString(4, os.getLocalPorta());
            stmt.setString(5, os.getDanoDescricao());
            stmt.setString(6, os.getRamal());
            stmt.setBoolean(7, os.isEntregue());
            // Converte java.util.Date para java.sql.Timestamp (para compatibilidade com DATETIME do MySQL)
            if (os.getHoraEntrega() != null) {
                 stmt.setTimestamp(8, new Timestamp(os.getHoraEntrega().getTime()));
            } else {
                 stmt.setTimestamp(8, null);
            }
            stmt.setString(9, os.getNumeroOSImpressa());

            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new SQLException("Erro ao salvar a Ordem de Serviço: " + ex.getMessage(), ex);
        } finally {
            Conexao.closeConnection(con, stmt);
    }
}

    // ReadAll (Consulta Completa com JOIN)
    public List<OrdemServico> readAll() throws SQLException {
        // SQL com JOIN para buscar o nome do grupo
        String sql = "SELECT OS.*, GT.nomeGrupo " +
                     "FROM OrdemServico OS " +
                     "INNER JOIN GrupoTrabalho GT ON OS.idGrupo_fk = GT.idGrupo " +
                     "ORDER BY OS.idOS DESC";

        List<OrdemServico> ordens = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = Conexao.getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                OrdemServico os = new OrdemServico();
                
                // Popula os dados da O.S.
                os.setIdOS(rs.getInt("idOS"));
                os.setSolicitante(rs.getString("solicitante"));
                os.setLocalAndar(rs.getInt("localAndar"));
                os.setLocalPorta(rs.getString("localPorta"));
                os.setDanoDescricao(rs.getString("danoDescricao"));
                os.setRamal(rs.getString("ramal"));
                os.setEntregue(rs.getBoolean("entregue"));
                os.setNumeroOSImpressa(rs.getString("numeroOSImpressa"));

                // Trata as datas de Entrega
                Timestamp tsEntrega = rs.getTimestamp("horaEntrega");
                if (tsEntrega != null) {
                    os.setHoraEntrega(new Date(tsEntrega.getTime()));
                }
                // Trata as datas de Devolução
                Timestamp tsDevolucao = rs.getTimestamp("horaDevolucao");
                if (tsDevolucao != null) {
                    os.setHoraDevolucao(new Date(tsDevolucao.getTime()));
                }

                // Popula o objeto Grupo de Trabalho (Chave Estrangeira)
                GrupoTrabalho grupo = new GrupoTrabalho();
                grupo.setIdGrupo(rs.getInt("idGrupo_fk"));
                grupo.setNomeGrupo(rs.getString("nomeGrupo")); 
                os.setGrupo(grupo);

                ordens.add(os);
            }
        } catch (SQLException ex) {
            throw new SQLException("Erro ao listar Ordens de Serviço: " + ex.getMessage(), ex);
        } finally {
            Conexao.closeConnection(con, stmt, rs); // Fechamento seguro
        }
        return ordens;
    }
    
    // ReadById (Busca uma única O.S. para Finalização)
    public OrdemServico readById(int idOS) throws SQLException {
        String sql = "SELECT OS.*, GT.nomeGrupo " +
                     "FROM OrdemServico OS " +
                     "INNER JOIN GrupoTrabalho GT ON OS.idGrupo_fk = GT.idGrupo " +
                     "WHERE OS.idOS = ?";

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        OrdemServico os = null;

        try {
            con = Conexao.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, idOS); 
            rs = stmt.executeQuery();

            if (rs.next()) {
                os = new OrdemServico();
                // Popule o objeto 'os' (mesmo padrão do readAll)
                os.setIdOS(rs.getInt("idOS"));
                os.setSolicitante(rs.getString("solicitante"));
                os.setLocalAndar(rs.getInt("localAndar"));
                os.setLocalPorta(rs.getString("localPorta"));
                os.setDanoDescricao(rs.getString("danoDescricao"));
                os.setRamal(rs.getString("ramal"));
                os.setEntregue(rs.getBoolean("entregue"));
                
                // Trata as datas
                Timestamp tsEntrega = rs.getTimestamp("horaEntrega");
                if (tsEntrega != null) { os.setHoraEntrega(new Date(tsEntrega.getTime())); }
                Timestamp tsDevolucao = rs.getTimestamp("horaDevolucao");
                if (tsDevolucao != null) { os.setHoraDevolucao(new Date(tsDevolucao.getTime())); }
                
                // Popula o objeto Grupo
                GrupoTrabalho grupo = new GrupoTrabalho();
                grupo.setIdGrupo(rs.getInt("idGrupo_fk"));
                grupo.setNomeGrupo(rs.getString("nomeGrupo"));
                os.setGrupo(grupo);
            }
        } catch (SQLException ex) {
            throw new SQLException("Erro ao buscar a O.S. por ID: " + ex.getMessage(), ex);
        } finally {
            Conexao.closeConnection(con, stmt, rs); // Fechamento seguro
        }
        return os;
    }

    // Update (Finalização/Devolução da O.S.)
    public void updateDevolucao(OrdemServico os) throws SQLException {
        // Atualiza três campos: entregue, horaEntrega e horaDevolucao
        String sql = "UPDATE OrdemServico SET entregue = ?, horaEntrega = ?, horaDevolucao = ? WHERE idOS = ?";
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = Conexao.getConnection();
            stmt = con.prepareStatement(sql);

            // Garante que entregue seja TRUE
            stmt.setBoolean(1, true); 
            
            // Seta a hora de Entrega (se for nula no BD, usamos a data/hora atual para preencher)
            Date horaEntrega = (os.getHoraEntrega() != null) ? os.getHoraEntrega() : new Date();
            stmt.setTimestamp(2, new Timestamp(horaEntrega.getTime()));

            // Seta a hora de Devolução
            stmt.setTimestamp(3, new Timestamp(os.getHoraDevolucao().getTime()));
            
            // Condição WHERE
            stmt.setInt(4, os.getIdOS());

            stmt.executeUpdate();
            
        } catch (SQLException ex) {
            throw new SQLException("Erro ao finalizar a Ordem de Serviço: " + ex.getMessage(), ex);
        } finally {
            Conexao.closeConnection(con, stmt); // Fechamento seguro
        }
    }
}