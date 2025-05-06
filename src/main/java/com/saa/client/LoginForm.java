package com.saa.client;

import com.saa.dao.UsuarioDAO;
import com.saa.model.Usuario;
import com.saa.security.Criptografia;
import com.saa.session.Sessao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JPanel panel;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    public LoginForm() {
        setTitle("Login - Sistema de Atendimento");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 180);
        setLocationRelativeTo(null);
        setResizable(false);

        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Usuário:"));
        txtLogin = new JTextField();
        panel.add(txtLogin);
        panel.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        panel.add(txtSenha);
        btnEntrar = new JButton("Entrar");
        panel.add(new JLabel());
        panel.add(btnEntrar);
        setContentPane(panel);

        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    autenticar();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                    throw new RuntimeException(ex);
                }
            }
        });
        setVisible(true);
    }
    private void autenticar() throws SQLException {
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.buscarPorLogin(login);
        System.out.println(usuario.getNome()+ " "+usuario.getId());

        if (login.equals("admin") && Criptografia.verificarSenha(senha, usuario.getSenhaHash())) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
            Sessao.getInstance().setUsuario(usuario);

            dispose();
            new PrincipalUI();
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        new LoginForm();
    }
}
