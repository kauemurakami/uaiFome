package com.ktm.uaifome.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ktm.uaifome.R;
import com.ktm.uaifome.helper.ConfiguracaoFirebase;
import com.ktm.uaifome.helper.UsuarioFirebase;
import com.ktm.uaifome.model.Empresa;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ConfiguracoesEmpresaActivity extends AppCompatActivity {

    private EditText edtNomeEmpresa, edtEmpresaCategoria, edtEmpresaTempo, edtEmpresaTaxa;
    private ImageView imagemPerfilEmpresa;

    private static final int SELECAO_GALERIA = 200;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String idUsuarioLogado;
    private String urlImagemSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_empresa);

        //configurações iniciais
        inicializarComponentes();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        databaseReference = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        //config toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //evento de click imagem para escolha da imagem
        imagemPerfilEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        );
                if (i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });
        recuperarDadosEmpresa();

    }

    private void recuperarDadosEmpresa(){
        DatabaseReference empresaRef = databaseReference
                .child("empresas")
                .child(idUsuarioLogado);
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    Empresa empresa = dataSnapshot.getValue(Empresa.class);
                    edtNomeEmpresa.setText(empresa.getNome());
                    edtEmpresaCategoria.setText(empresa.getCategoria());
                    edtEmpresaTaxa.setText(empresa.getPrecoEntrega().toString());
                    edtEmpresaTempo.setText(empresa.getTempo());

                    urlImagemSelecionada = empresa.getUrlDaImagem();
                    if (!urlImagemSelecionada.isEmpty()){
                        Picasso.get()
                                .load(urlImagemSelecionada)
                                .into(imagemPerfilEmpresa);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Bitmap imagem = null;
            try {
                switch (requestCode){
                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images
                                .Media
                                .getBitmap(
                                        getContentResolver(),
                                        localImagem
                                );
                        break;
                }

                if (imagem != null){
                    imagemPerfilEmpresa.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("empresas")
                            .child(idUsuarioLogado + "jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Erro ao fazer upload da imagem", Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            urlImagemSelecionada = taskSnapshot.getDownloadUrl().toString();
                            Toast.makeText(getApplicationContext(), "Imagem atribuida", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }catch (Exception e){

            }
        }
    }

    private void inicializarComponentes(){
        edtEmpresaCategoria = findViewById(R.id.edtCategoriaEmpresa);
        edtEmpresaTaxa      = findViewById(R.id.edtTaxaEntrega);
        edtEmpresaTempo     = findViewById(R.id.edtTempoEntrega);
        edtNomeEmpresa      = findViewById(R.id.edtNomeEmpresa);
        imagemPerfilEmpresa = findViewById(R.id.profile_image);
    }

    //validação dos dados
    public void validarDadosEmpresa(View v){
        String nome, taxa, categoria, tempo;
        nome = edtNomeEmpresa.getText().toString();
        taxa = edtEmpresaTaxa.getText().toString();
        categoria = edtEmpresaCategoria.getText().toString();
        tempo = edtEmpresaTempo.getText().toString();

        if (!nome.isEmpty()){
            if (!categoria.isEmpty()){
                if (taxa.isEmpty()) {
                    taxa = String.valueOf(0);
                }
                if (tempo.isEmpty()){
                    tempo = "Não definido";
                }

                Empresa empresa = new Empresa();
                empresa.setIdUsuario(idUsuarioLogado);
                empresa.setNome(nome);
                empresa.setCategoria(categoria);
                empresa.setPrecoEntrega(Double.parseDouble(taxa));
                empresa.setTempo(tempo);
                empresa.setUrlDaImagem(urlImagemSelecionada);
                try {
                    if (!urlImagemSelecionada.isEmpty()){
                        empresa.salvar();
                        exibirMensagem("Salvo");
                        finish();
                    }
                }catch (Exception e){
                    exibirMensagem("adicione uma imagem ao seu negócio");
                }
            }else{
                exibirMensagem("Descreva a categoria de sua empresa");
            }
        }else{
            exibirMensagem("Digite um nome para sua empresa");
        }


    }

    public void exibirMensagem(String texto){
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_LONG).show();
    }
}
