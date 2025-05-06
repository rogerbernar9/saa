package com.saa.dao;

import com.saa.db.DatabaseConnection;
import com.saa.model.Usuario;
import com.saa.model.UnidadeAtendimento;
import com.saa.security.Criptografia;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class UsuarioDAO {
    private final UnidadeDAO unidadeDAO = new UnidadeDAO();
    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, login, senha_hash, ativo, roles, unidade_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, Criptografia.gerarHash(usuario.getSenhaHash()));
            stmt.setBoolean(4, usuario.isAtivo());
            stmt.setString(5, String.join(",", usuario.getRoles()));
//            stmt.setInt(6, usuario.getUnidade().getId());
            stmt.executeUpdate();
        }
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenhaHash(rs.getString("senha_hash"));
                usuario.setAtivo(rs.getBoolean("ativo"));
                String rolesString = rs.getString("roles");
                List<String> roles = rolesString != null ? Arrays.asList(rolesString.split(",")) : new ArrayList<>();
                usuario.setRoles(roles);
                int unidadeId = rs.getInt("unidade_id");
                UnidadeAtendimento unidade = unidadeDAO.buscarPorId(unidadeId);
                usuario.setUnidade(unidade);
                return usuario;
            }
        }
        return null;
    }


    public Usuario buscarPorLogin(String login) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE login = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenhaHash(rs.getString("senha_hash"));
                usuario.setAtivo(rs.getBoolean("ativo"));
                String rolesString = rs.getString("roles");
                List<String> roles = rolesString != null ? Arrays.asList(rolesString.split(",")) : new ArrayList<>();
                usuario.setRoles(roles);
                int unidadeId = rs.getInt("unidade_id");
                UnidadeAtendimento unidade = unidadeDAO.buscarPorId(unidadeId);
                usuario.setUnidade(unidade);
                return usuario;
            }
        }
        return null;
    }

//    public static void main(String[] args) throws SQLException {
//        Usuario usuario1 = new Usuario();
//        usuario1.setAtivo(true);
//        usuario1.setNome("Pedro");
//        usuario1.setLogin("pedrosilva");
//        usuario1.setSenhaHash("123");
//        usuario1.setRoles(Arrays.asList("SUPER_AMIN"));
//        usuario1.setUnidade(null);
//        UsuarioDAO usuarioDAO = new UsuarioDAO();
//        usuarioDAO.inserir(usuario1);
//
//    }

}