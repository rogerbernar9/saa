package com.saa.model;

import java.util.List;

public class Usuario {
    private int id;
    private String nome;
    private String login;
    private String senhaHash;
    private boolean ativo;
    private List<String> roles;
    private UnidadeAtendimento unidade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public UnidadeAtendimento getUnidade() {
        return unidade;
    }

    public void setUnidade(UnidadeAtendimento unidade) {
        this.unidade = unidade;
    }

}
