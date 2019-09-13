package com.ktm.uaifome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ktm.uaifome.R;
import com.ktm.uaifome.helper.UsuarioFirebase;
import com.ktm.uaifome.model.Empresa;
import com.ktm.uaifome.model.Produto;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {

    private EditText edtNomeProduto, edtPrecoProduto, edtDescricaoProduto;
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_empresa);

        inicializarComponentes();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        //config toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void exibirMensagem(String texto){
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_LONG).show();
    }


    private void inicializarComponentes(){
        edtNomeProduto      = findViewById(R.id.edtNomeProduto);
        edtPrecoProduto     = findViewById(R.id.edtPrecoProduto);
        edtDescricaoProduto = findViewById(R.id.edtDescricaoProduto);
    }

    //validação dos dados
    public void validarDadosProduto(View v){
        String nome, preco, descricao;
        nome = edtNomeProduto.getText().toString();
        descricao = edtDescricaoProduto.getText().toString();
        preco = edtPrecoProduto.getText().toString();

        if (!nome.isEmpty()){
            if (!descricao.isEmpty()){
                if (!preco.isEmpty()) {

                    Produto produto = new Produto();
                    produto.setIdUsuario(idUsuarioLogado);
                    produto.setNome(nome);
                    produto.setDescricao(descricao);
                    produto.setPreco(Double.parseDouble(preco));

                    produto.salvar();
                    finish();
                    exibirMensagem("Salvo");

                }else{
                    exibirMensagem("Digite um preço para o produto");
                }

            }else{
                exibirMensagem("Descreva o produto");
            }
        }else{
            exibirMensagem("Digite um nome para o produto");
        }


    }
}
