package com.gps.dao;

/**
 *
 * @author Henry Wallace
 */

import com.gps.config.Conexao;
import com.gps.model.GrupoTrabalho;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GrupoTrabalhoDAO {

    // Create
    public void create(GrupoTrabalho grupo) throws SQLException {
        String sql = "INSERT INTO GrupoTrabalho (nomeGrupo) VALUES (?)";
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = Conexao.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, grupo.getNomeGrupo());

            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new SQLException("Erro ao cadastrar Grupo de Trabalho: " + ex.getMessage(), ex);
        } finally {
            Conexao.closeConnection(con, stmt);
        }
    }

    // ReadALL
    public List<GrupoTrabalho> readAll() throws SQLException {
        String sql = "SELECT * FROM GrupoTrabalho ORDER BY nomeGrupo";
        List<GrupoTrabalho> grupos = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = Conexao.getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                GrupoTrabalho grupo = new GrupoTrabalho();
                grupo.setIdGrupo(rs.getInt("idGrupo"));
                grupo.setNomeGrupo(rs.getString("nomeGrupo"));
                grupos.add(grupo);
            }
        } catch (SQLException ex) {
            throw new SQLException("Erro ao listar grupos: " + ex.getMessage(), ex);
        } finally {
            Conexao.closeConnection(con, stmt, rs);
        }
        return grupos;
    }
    
    // Delete
    public void delete(int idGrupo) throws SQLException {
        String sql = "DELETE FROM GrupoTrabalho WHERE idGrupo = ?";
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = Conexao.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, idGrupo);

            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new SQLException("Erro ao deletar Grupo de Trabalho: " + ex.getMessage(), ex);
        } finally {
            Conexao.closeConnection(con, stmt);
        }
    }
    
}