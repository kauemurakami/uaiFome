package com.ktm.uaifome.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.ktm.uaifome.R;

public class AuthenticationActivity extends AppCompatActivity {

    private Button botaoAcessar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        getSupportActionBar().hide();

        inicializarComponentes();



    }


    public void inicializarComponentes(){
        campoEmail   = findViewById(R.id.edtCadastroEmail);
        campoSenha   = findViewById(R.id.edtCadastroSenha);
        botaoAcessar = findViewById(R.id.btnAcesso);
        tipoAcesso   = findViewById(R.id.switchAcesso);
    }
}
