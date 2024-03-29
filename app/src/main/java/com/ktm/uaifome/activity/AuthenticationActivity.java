package com.ktm.uaifome.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.ktm.uaifome.R;
import com.ktm.uaifome.helper.ConfiguracaoFirebase;
import com.ktm.uaifome.helper.UsuarioFirebase;

public class AuthenticationActivity extends AppCompatActivity {

    private Button botaoAcessar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso, tipoUsuario;
    private LinearLayout linearTipoUsuario;
    private FirebaseAuth autenticacao;
    private TextView cadastrar, entrar, usuarioT, empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        verificarUsuarioLogado();
        entrar.setTypeface(null, Typeface.BOLD);
        usuarioT.setTypeface(null, Typeface.BOLD);


        //verificando se o switch esta ativado
        tipoAcesso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){  //cadastrar
                    linearTipoUsuario.setVisibility(View.VISIBLE);
                    botaoAcessar.setText("Cadastrar");
                    cadastrar.setTypeface(null, Typeface.BOLD);
                    entrar.setTypeface(null, Typeface.NORMAL);

                    tipoUsuario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b){
                                usuarioT.setTypeface(null, Typeface.NORMAL);
                                empresa.setTypeface(null, Typeface.BOLD);
                            }else{
                                empresa.setTypeface(null, Typeface.NORMAL);
                                usuarioT.setTypeface(null, Typeface.BOLD);
                            }
                        }
                    });

                }else{ //logar
                    linearTipoUsuario.setVisibility(View.GONE);
                    botaoAcessar.setText("Acessar");
                    entrar.setTypeface(null, Typeface.BOLD);
                    cadastrar.setTypeface(null, Typeface.NORMAL);
                }
            }
        });


        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if(!email.isEmpty()){
                    if (!senha.isEmpty()){
                        //verifica switch logar off <> cadastro on
                        if (tipoAcesso.isChecked()){ //cadastro
                            autenticacao.createUserWithEmailAndPassword(
                                    email, senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso", Toast.LENGTH_LONG).show();
                                        String tipoUsuario = getTipoUsuario(); // recuperando tipo do usuário
                                        UsuarioFirebase.atualizarTipoUsuario(tipoUsuario);
                                        abrirTelaprincipal(tipoUsuario);
                                    }else{

                                        String erroExcecao = "";

                                        try {
                                            throw task.getException();
                                        }catch(FirebaseAuthWeakPasswordException e){
                                            erroExcecao = "Digite uma senha mais forte!";
                                        }catch(FirebaseAuthInvalidCredentialsException e){
                                            erroExcecao = "Digite um email válido!";
                                        }catch (FirebaseAuthUserCollisionException e){
                                            erroExcecao = "Esta conta já está cadastrada";
                                        }catch (Exception e){
                                            erroExcecao = "Ao cadastrar usuário: "+e.getMessage();
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(getApplicationContext(), "Erro: "+ erroExcecao, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{ // login # #

                            autenticacao.signInWithEmailAndPassword(
                                    email, senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Bem vindo :)", Toast.LENGTH_LONG).show();
                                        abrirTelaprincipal(task.getResult().getUser().getDisplayName()); // recuperando tipo de usuario da task.
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Erro ao fazer login", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Insira sua senha", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Insira seu email", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirTelaprincipal(String tipoUsuario){
        if (tipoUsuario.equals("E")){ // empresa
            startActivity(new Intent(getApplicationContext(), EmpresaActivity.class));
        }else { // usuario
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
    }

    private void verificarUsuarioLogado(){
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if (usuarioAtual != null){
            String tipoUsuario = usuarioAtual.getDisplayName();
            abrirTelaprincipal(tipoUsuario);
        }
    }

    //retorna tipo do usuario
    private String getTipoUsuario(){
        return tipoUsuario.isChecked() ? "E" : "U";
    }

    public void inicializarComponentes(){
        campoEmail        = findViewById(R.id.edtCadastroEmail);
        campoSenha        = findViewById(R.id.edtCadastroSenha);
        botaoAcessar      = findViewById(R.id.btnAcesso);
        tipoAcesso        = findViewById(R.id.switchAcesso);
        tipoUsuario       = findViewById(R.id.switchTipoUsuario);
        linearTipoUsuario = findViewById(R.id.linearTipoUsuario);

        usuarioT          =findViewById(R.id.txtUsuario);
        cadastrar         = findViewById(R.id.txtcadastrar);
        empresa           = findViewById(R.id.txtEmpresa);
        entrar            = findViewById(R.id.txtLogar);
    }
}
