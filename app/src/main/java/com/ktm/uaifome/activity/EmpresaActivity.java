package com.ktm.uaifome.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.ktm.uaifome.R;
import com.ktm.uaifome.helper.ConfiguracaoFirebase;

public class EmpresaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //config toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("uaiFome - empresa");
        setSupportActionBar(toolbar);

    }

    //configurando menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSair :
                deslogarUsuario();
                break;
            case R.id.menuConfigurações :
                abrirConfiguracoes();
                break;
            case R.id.menuNovoProduto :
                abrirNovoProduto();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //deslogar usuario
    private void deslogarUsuario(){
        try {
            autenticacao.signOut();
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void abrirConfiguracoes(){
        startActivity(new Intent(getApplicationContext(), ConfiguracoesEmpresaActivity.class));
    }

    private void abrirNovoProduto(){
        startActivity(new Intent(getApplicationContext(), NovoProdutoEmpresaActivity.class));
    }
}