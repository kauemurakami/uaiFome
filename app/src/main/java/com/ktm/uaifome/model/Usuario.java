package com.ktm.uaifome.model;

import com.google.firebase.database.DatabaseReference;
import com.ktm.uaifome.helper.ConfiguracaoFirebase;

public class Usuario {

    String idUsuario, endereco, nome;

    public Usuario() {
    }


    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuarioRef  = firebaseRef
            .child("usuarios")
            .child(getIdUsuario());
        usuarioRef.setValue(this);
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
