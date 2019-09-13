package com.ktm.uaifome.model;

import com.google.firebase.database.DatabaseReference;
import com.ktm.uaifome.helper.ConfiguracaoFirebase;

public class Produto {

    private String idUsuario, nome, descricao;
    private Double preco;

    public void salvar(){
        DatabaseReference databaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = databaseRef
            .child("produtos")
            .child(getIdUsuario())
            .push();
        produtoRef.setValue(this);
    }

    public Produto() {

    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
