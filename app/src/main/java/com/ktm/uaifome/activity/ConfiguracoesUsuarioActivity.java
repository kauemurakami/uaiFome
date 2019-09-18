package com.ktm.uaifome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ktm.uaifome.R;
import com.ktm.uaifome.helper.ConfiguracaoFirebase;
import com.ktm.uaifome.helper.UsuarioFirebase;
import com.ktm.uaifome.model.Usuario;

public class ConfiguracoesUsuarioActivity extends AppCompatActivity {

    private EditText edtNomeUsuario, edtEnderecoUsuario;
    private String idUsuario;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        //inicializandocomponentes
        this.inicializarComponentes();
        idUsuario = UsuarioFirebase.getIdUsuario();
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        //config toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações u");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.recuperarDadosUsuario();
    }

    //recuperando dados no momento da chamada
    private void recuperarDadosUsuario(){
        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child(idUsuario);
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    edtNomeUsuario.setText(usuario.getNome());
                    edtEnderecoUsuario.setText(usuario.getEndereco());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //validando dadaos
    public void validarDadosUsuario(View view){
        String nome     = edtNomeUsuario.getText().toString();
        String endereco = edtEnderecoUsuario.getText().toString();
        if (!nome.isEmpty()){
            if (!endereco.isEmpty()){
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(idUsuario);
                usuario.setNome(nome);
                usuario.setEndereco(endereco);
                usuario.salvar();

                exibirMensagem("Dados atualizados");
                finish();
            }else exibirMensagem("insira seu endereço primário");
        }else exibirMensagem("Insira seu nome");


    }

    public void exibirMensagem(String texto){
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_LONG).show();
    }

    private void inicializarComponentes(){
        edtNomeUsuario     = findViewById(R.id.edtNomeUsuario);
        edtEnderecoUsuario = findViewById(R.id.edtEnderecoUsuario);

    }
}
