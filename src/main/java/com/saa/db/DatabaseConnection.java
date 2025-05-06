package com.saa.db;

import com.saa.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:"+ DatabaseUtil.getPath();
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String createUnidade = """
                CREATE TABLE IF NOT EXISTS unidade (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    codigo TEXT NOT NULL,
                    ativo BOOLEAN NOT NULL
                );
            """;
            String createUsuario = """
                CREATE TABLE IF NOT EXISTS usuario (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    login TEXT NOT NULL UNIQUE,
                    senha_hash TEXT NOT NULL,
                    ativo BOOLEAN NOT NULL,
                    roles TEXT NOT NULL,
                    unidade_id INTEGER,
                    FOREIGN KEY (unidade_id) REFERENCES unidade(id)
                );
            """;
            String createFilaAtendimento = """
                CREATE TABLE IF NOT EXISTS fila_atendimento (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    sigla TEXT NOT NULL,
                    prioridade INTEGER NOT NULL,
                    ativo BOOLEAN NOT NULL,
                    unidade_id INTEGER,
                    FOREIGN KEY (unidade_id) REFERENCES unidade(id)
                );
            """;
            String createSenha = """
                CREATE TABLE IF NOT EXISTS senha (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    numero INTEGER NOT NULL,
                    tipo TEXT NOT NULL,
                    status TEXT NOT NULL,
                    data_hora_criacao TEXT NOT NULL,
                    data_hora_chamada TEXT,
                    fila_id INTEGER,
                    atendente_id INTEGER,
                    FOREIGN KEY (fila_id) REFERENCES fila_atendimento(id),
                    FOREIGN KEY (atendente_id) REFERENCES usuario(id)
                );
            """;
            String createChamado = """
                CREATE TABLE IF NOT EXISTS chamado (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    senha_id INTEGER NOT NULL,
                    atendente_id INTEGER NOT NULL,
                    guiche TEXT NOT NULL,
                    data_hora_chamada TEXT NOT NULL,
                    data_hora_finalizacao TEXT,
                    repetido BOOLEAN NOT NULL,
                    FOREIGN KEY (senha_id) REFERENCES senha(id),
                    FOREIGN KEY (atendente_id) REFERENCES usuario(id)
                );
            """;
            // Execução
            stmt.execute(createUnidade);
            stmt.execute(createUsuario);
            stmt.execute(createFilaAtendimento);
            stmt.execute(createSenha);
            stmt.execute(createChamado);
            System.out.println("Banco de dados e tabelas criados com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao criar o banco: " + e.getMessage());
        }
    }
}
