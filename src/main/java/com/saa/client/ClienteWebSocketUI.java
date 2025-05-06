package com.saa.client;
import javax.swing.*;
import java.awt.*;
import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
public class ClienteWebSocketUI extends JFrame {
    private JButton btnChamar, btnRepetir, btnEncerrar;
    private JTextArea output;
    private Cliente cliente;
    public ClienteWebSocketUI() {
        setTitle("Painel Chamador");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel painelBotoes = new JPanel(new GridLayout(1, 3, 10, 10));
        btnChamar = new JButton("Chamar Próximo");
        btnRepetir = new JButton("Repetir Chamada");
        btnEncerrar = new JButton("Encerrar Atendimento");
        painelBotoes.add(btnChamar);
        painelBotoes.add(btnRepetir);
        painelBotoes.add(btnEncerrar);
        output = new JTextArea();
        output.setEditable(false);
        add(painelBotoes, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
        btnChamar.addActionListener(e -> cliente.send("chamar_proximo"));
        btnRepetir.addActionListener(e -> cliente.send("repetir_chamada"));
        btnEncerrar.addActionListener(e -> output.append("Atendimento encerrado.\n"));
        conectar();
        setVisible(true);
    }
    private void conectar() {
        try {
            cliente = new Cliente(new URI("ws://localhost:8087"));
            cliente.connectBlocking();
        } catch (Exception e) {
            output.append("Erro de conexão: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        new ClienteWebSocketUI();
        new ClienteWebSocketUI();
    }

    private class Cliente extends WebSocketClient {
        public Cliente(URI serverUri) {
            super(serverUri);
        }
        @Override
        public void onOpen(ServerHandshake handshake) {
            output.append("Conectado ao servidor.\n");
        }
        @Override
        public void onMessage(String message) {
            output.append(message + "\n");
        }
        @Override
        public void onClose(int code, String reason, boolean remote) {
            output.append("Desconectado.\n");
            btnChamar.setEnabled(false);
            btnRepetir.setEnabled(false);
        }
        @Override
        public void onError(Exception ex) {
            output.append("Erro: " + ex.getMessage() + "\n");
            btnChamar.setEnabled(false);
            btnRepetir.setEnabled(false);
        }
    }
}
