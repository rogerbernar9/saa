package com.saa.client;

import com.saa.websocket.ServidorFilaWebSocket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrincipalUI extends JFrame {
    private JButton btnCadastrarUnidade;
    private JButton btnStartarWebSocket;
    private JButton btnCadastrarUsuarios;

    private JButton btnPararWebSocket;
    private JLabel lblStatus;
    private JTextArea txtConsole;
    private ServidorFilaWebSocket servidor;
    public PrincipalUI() {
        setTitle("Sistema de Atendimento - Painel Administrativo");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        JPanel painelTopo = new JPanel(new GridLayout(5, 1, 10, 10));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        btnCadastrarUnidade = new JButton("Cadastrar Unidade");
        btnCadastrarUsuarios = new JButton("Cadastrar Usuários");
        btnStartarWebSocket = new JButton("Iniciar WebSocket");
        btnPararWebSocket = new JButton("Parar WebSocket");
        btnPararWebSocket.setEnabled(false);
        lblStatus = new JLabel("Status: WebSocket não iniciado");
        painelTopo.add(btnCadastrarUnidade);
        painelTopo.add(btnCadastrarUsuarios);
        painelTopo.add(btnStartarWebSocket);
        painelTopo.add(btnPararWebSocket);
        painelTopo.add(lblStatus);
        txtConsole = new JTextArea();
        txtConsole.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtConsole);
        painel.add(painelTopo, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        setContentPane(painel);

        btnCadastrarUnidade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(painel, "Abrir tela de cadastro de unidade (em desenvolvimento).");
            }
        });

        btnStartarWebSocket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarWebSocket();
            }
        });

        btnPararWebSocket.addActionListener(e -> pararWebSocket());


        btnCadastrarUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(painel, "Abrir tela de cadastro de usuários (em desenvolvimento).");
            }
        });

        setVisible(true);

    }
    private void iniciarWebSocket() {
//        log(String.valueOf(servidor == null));
//        log(String.valueOf(servidor.estaRodando()));

        if (servidor != null && servidor.estaRodando()) {
            log("Servidor já está rodando.");
            return;
        }
        try {
            servidor = new ServidorFilaWebSocket() {
                @Override
                public void onMessage(org.java_websocket.WebSocket conn, String message) {
                    super.onMessage(conn, message);
                    log("Recebido: " + message);
                }
                @Override
                public void onOpen(org.java_websocket.WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
                    super.onOpen(conn, handshake);
                    log("Nova conexão: " + conn.getRemoteSocketAddress());
                }
                @Override
                public void onClose(org.java_websocket.WebSocket conn, int code, String reason, boolean remote) {
                    super.onClose(conn, code, reason, remote);
                    log("Conexão encerrada.");
                }
                @Override
                public void onError(org.java_websocket.WebSocket conn, Exception ex) {
                    super.onError(conn, ex);
                    log("Erro: " + ex.getMessage());
                }
            };
            servidor.iniciarEmThread();
            int tentativas = 0;
            while (!servidor.estaRodando() && tentativas < 20) {
                Thread.sleep(100);
                tentativas++;
            }
            if (servidor.estaRodando()) {
                log("Servidor WebSocket iniciado.");
                lblStatus.setText("Status: WebSocket em execução");
                btnStartarWebSocket.setEnabled(false);
                btnPararWebSocket.setEnabled(true);
            } else {
                throw new IllegalStateException("O servidor não respondeu dentro do tempo esperado.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log("Erro ao iniciar WebSocket: " + ex.getMessage());
        }
    }

    private void pararWebSocket() {
        if (servidor != null && servidor.estaRodando()) {
            try {
                servidor.stop();
                servidor.pararServidor();
                log("Servidor WebSocket parado.");
                lblStatus.setText("Status: WebSocket parado");
                btnStartarWebSocket.setEnabled(true);
                btnPararWebSocket.setEnabled(false);
            } catch (Exception e) {
                log("Erro ao parar o servidor: " + e.getMessage());
            }
        } else {
            log("Servidor não está em execução.");
        }
    }

    private void log(String mensagem) {
        txtConsole.append(mensagem + "\n");
        txtConsole.setCaretPosition(txtConsole.getDocument().getLength());
    }

}
