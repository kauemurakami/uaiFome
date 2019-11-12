package com.ktm.uaifome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dmax.dialog.SpotsDialog;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ktm.uaifome.R;
import com.ktm.uaifome.adapter.AdapterPedido;
import com.ktm.uaifome.helper.ConfiguracaoFirebase;
import com.ktm.uaifome.helper.UsuarioFirebase;
import com.ktm.uaifome.listener.RecyclerItemClickListener;
import com.ktm.uaifome.model.Pedido;

import java.util.ArrayList;
import java.util.List;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPedidos;
    private AdapterPedido adapterPedido;
    private List<Pedido> pedidos = new ArrayList<>();
    private AlertDialog dialog;
    private DatabaseReference firebaseRef;
    private String idEmpresa;
    private Pedido pedido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        this.inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idEmpresa = UsuarioFirebase.getIdUsuario();

        //configurando toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pedidos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //configurar recycler view
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewPedidos.setHasFixedSize(true);
        adapterPedido = new AdapterPedido(pedidos);
        recyclerViewPedidos.setAdapter(adapterPedido);

        recuperarPedidos();

        //evento de click no rv
        recyclerViewPedidos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerViewPedidos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                pedido = pedidos.get(position);
                                pedido.setStats("finalizado");
                                pedido.atualizarStatus();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );
    }

    private void recuperarPedidos() {

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando")
                .setCancelable(false)
                .build();

        dialog.show();

        DatabaseReference pedidosRef = firebaseRef
                .child("pedidos")
                .child(idEmpresa);

        Query pedidoPesquisa = pedidosRef.orderByChild("stats")
                .equalTo("confirmado");

        pedidoPesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                pedidos.clear();
                if(dataSnapshot.getValue() != null ){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Pedido pedido = ds.getValue(Pedido.class);
                        pedidos.add(pedido);
                    }
                    adapterPedido.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inicializarComponentes(){
        this.recyclerViewPedidos = findViewById(R.id.recyclerPedidos);
    }

}
