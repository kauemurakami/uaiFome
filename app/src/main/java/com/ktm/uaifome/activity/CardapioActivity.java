package com.ktm.uaifome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ktm.uaifome.R;
import com.ktm.uaifome.adapter.AdapterProduto;
import com.ktm.uaifome.helper.ConfiguracaoFirebase;
import com.ktm.uaifome.model.Empresa;
import com.ktm.uaifome.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardapioActivity extends AppCompatActivity {

    private RecyclerView recyclerProcutoCardapio;
    private ImageView imageEmpresaCardapio;
    private TextView nomeEmpresaCardapio;
    private Empresa empresaSelecionada;

    private List<Produto> produtos = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private AdapterProduto adapterProduto;

    private String idEmpresa;

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

        //recuperando produtos
        recuperarProdutos();

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

    private void inicializarComponentes(){
        this.recyclerProcutoCardapio = findViewById(R.id.recyclerProdutoCardapio);
        this.imageEmpresaCardapio    = findViewById(R.id.imageEmpresaCardapio);
        this.nomeEmpresaCardapio     = findViewById(R.id.textNomeEmpresaCardapio);
    }
}
