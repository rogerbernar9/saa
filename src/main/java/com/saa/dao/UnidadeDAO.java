package com.saa.dao;
import com.saa.db.DatabaseConnection;
import com.saa.model.UnidadeAtendimento;
import java.sql.*;

public class UnidadeDAO {
    public void inserir(UnidadeAtendimento unidade) throws SQLException {
        String sql = "INSERT INTO unidade (nome, codigo, ativo) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, unidade.getNome());
            stmt.setString(2, unidade.getCodigo());
            stmt.setBoolean(3, unidade.isAtivo());
            stmt.executeUpdate();
        }
    }
    public UnidadeAtendimento buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM unidade WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UnidadeAtendimento unidade = new UnidadeAtendimento();
                unidade.setId(rs.getInt("id"));
                unidade.setNome(rs.getString("nome"));
                unidade.setCodigo(rs.getString("codigo"));
                unidade.setAtivo(rs.getBoolean("ativo"));
                return unidade;
            }
        }
        return null;
    }
}
