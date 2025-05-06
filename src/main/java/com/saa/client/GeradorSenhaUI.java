package com.saa.client;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
public class GeradorSenhaUI extends JFrame {
    private JButton btnNovaSenha;
    private JTextArea area;
    private Cliente cliente;
    private int contadorSenha = 0;
    public GeradorSenhaUI() {
        setTitle("Totem de Senhas");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        btnNovaSenha = new JButton("Retirar Senha");
        area = new JTextArea();
        area.setEditable(false);
        add(btnNovaSenha, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
        btnNovaSenha.addActionListener(e -> {
            contadorSenha++;
            String senha = String.format("S%03d", contadorSenha);
            area.append("Senha gerada: " + senha + "\n");
            cliente.send("nova_senha:" + senha);
        });
        conectar();
        setVisible(true);
    }
    private void conectar() {
        try {
            cliente = new Cliente(new URI("ws://localhost:8087"));
            cliente.connectBlocking();
        } catch (Exception e) {
            area.append("Erro de conexão: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        new GeradorSenhaUI();
    }

    private class Cliente extends WebSocketClient {

        public Cliente(URI serverUri) {
            super(serverUri);
        }
        @Override
        public void onOpen(ServerHandshake handshake) {
            area.append("Conectado ao servidor.\n");
            btnNovaSenha.setEnabled(true);
        }
        @Override
        public void onMessage(String message) {
            area.append(message + "\n");
        }
        @Override
        public void onClose(int code, String reason, boolean remote) {
            area.append("Desconectado.\n");
            btnNovaSenha.setEnabled(false);
        }
        @Override
        public void onError(Exception ex) {
            area.append("Erro: " + ex.getMessage() + "\n");
            btnNovaSenha.setEnabled(false);
        }

    }
}