package com.ktm.uaifome.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ktm.uaifome.R;
import com.ktm.uaifome.adapter.AdapterProduto;
import com.ktm.uaifome.helper.ConfiguracaoFirebase;
import com.ktm.uaifome.helper.UsuarioFirebase;
import com.ktm.uaifome.listener.RecyclerItemClickListener;
import com.ktm.uaifome.model.Empresa;
import com.ktm.uaifome.model.ItensPedido;
import com.ktm.uaifome.model.Produto;
import com.ktm.uaifome.model.Usuario;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class CardapioActivity extends AppCompatActivity {

    private RecyclerView recyclerProcutoCardapio;
    private ImageView imageEmpresaCardapio;
    private TextView nomeEmpresaCardapio;
    private Empresa empresaSelecionada;
    private AlertDialog dialog;
    private List<Produto> produtos = new ArrayList<>();
    private List<ItensPedido> itensPedido = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private AdapterProduto adapterProduto;
    private String idEmpresa, idUSuarioLogado;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        //inicializarComponentes
        this.inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        //recuperando empresa seecionada
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            empresaSelecionada = (Empresa) bundle.getSerializable("empresa");
            idEmpresa = empresaSelecionada.getIdUsuario(); //id usuario é o id da empresa, ou do usuario que a registrou no caso
            idUSuarioLogado = UsuarioFirebase.getIdUsuario();

            nomeEmpresaCardapio.setText(empresaSelecionada.getNome());
            String urlImagem = empresaSelecionada.getUrlDaImagem();
            Picasso.get()
                    .load(urlImagem)
                    .into(imageEmpresaCardapio);
        }

        //config toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cardápio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //configurando recycler view
        //configurando recylerView
        this.recyclerProcutoCardapio.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerProcutoCardapio.setHasFixedSize(true);
        this.adapterProduto = new AdapterProduto(produtos, this);
        this.recyclerProcutoCardapio.setAdapter(adapterProduto);

        //Configurando evento de clique
        recyclerProcutoCardapio.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerProcutoCardapio,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        confirmarQuantidade(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));

        //recuperando produtos
        recuperarProdutos();
        //recuperar dados do usuario
        recuperarDadosUsuario();

    }

    private void confirmarQuantidade(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quantidade");
        builder.setMessage("Digite a quantidade");
        final EditText edtQtd = new EditText(this);
        edtQtd.setText("1");
        edtQtd.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(edtQtd);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String quantidade = edtQtd.getText().toString();
                Produto produtoSelec = produtos.get(position);
                ItensPedido itemPedido = new ItensPedido();

                itemPedido.setIdProduto(produtoSelec.getIdProduto());
                itemPedido.setNomeProduto(produtoSelec.getNome());
                itemPedido.setPreco(produtoSelec.getPreco());
                itemPedido.setQuantidade(Integer.parseInt(quantidade));

                /// ################# validar se o carrinho ja nao esta no carrinho
                //com laço no nosso array de itens do carrinho
                itensPedido.add(itemPedido);



            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void recuperarDadosUsuario(){
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando dados")
                .setCancelable(false)
                .build();
        dialog.show();

        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child(idUSuarioLogado);
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    usuario = dataSnapshot.getValue(Usuario.class);
                }
                recuperarPedido();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void recuperarPedido(){

        dialog.dismiss();
    }

    private void recuperarProdutos(){
        DatabaseReference produtosRef = firebaseRef
                .child("produtos")
                .child(idEmpresa);

        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                produtos.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    produtos.add(ds.getValue(Produto.class));
                }
                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //configurando menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cardapio, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuConfirmarPedido :

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inicializarComponentes(){
        this.recyclerProcutoCardapio = findViewById(R.id.recyclerProdutoCardapio);
        this.imageEmpresaCardapio    = findViewById(R.id.imageEmpresaCardapio);
        this.nomeEmpresaCardapio     = findViewById(R.id.textNomeEmpresaCardapio);
    }
}
