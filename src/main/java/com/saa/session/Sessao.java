package com.saa.session;

import com.saa.model.Usuario;

public class Sessao {
    private static Sessao instancia;
    private Usuario usuarioLogado;
    private Sessao() {
    }

    public static Sessao getInstance() {
        if (instancia == null) {
            instancia = new Sessao();
        }
        return instancia;
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public Usuario getUsuario() {
        return this.usuarioLogado;
    }

    public void limpar() {
        usuarioLogado = null;
    }}
