package com.ktm.uaifome.model;

import com.google.firebase.database.DatabaseReference;
import com.ktm.uaifome.helper.ConfiguracaoFirebase;

public class Produto {

    private String idUsuario;
    private String idProduto;
    private String nome;

    private String descricao;
    private Double preco;

    public void salvar(){
        DatabaseReference databaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = databaseRef
            .child("produtos")
            .child(getIdUsuario())
            .child(getIdProduto());
        produtoRef.setValue(this);
    }

    public void remover(){
        DatabaseReference databaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = databaseRef
                .child("produtos")
                .child(getIdUsuario())
                .child(getIdProduto());
        produtoRef.removeValue();
    }

    public Produto() {
        DatabaseReference databaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = databaseRef
                .child("produtos");
        setIdProduto(produtoRef.push().getKey());
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
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
