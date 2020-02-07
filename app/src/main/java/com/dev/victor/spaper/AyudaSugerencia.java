package com.dev.victor.spaper;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;


public class AyudaSugerencia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda_sugerencia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bartool = getSupportActionBar();
        final String DEVELOPER_KEY = getString(R.string.youtubekey);

        //INFLAR LOS TEXT VIEWS PARA LAS AYUDAS (CAMBIAR ESTO POR UNA LISTA DINAMICA MAS ADELANTE)
        TextView setaswall = (TextView)findViewById(R.id.Hwallpaper);
        TextView setshare = (TextView)findViewById(R.id.Hcompartir);
        TextView setdownload = (TextView)findViewById(R.id.Hdescargar);
        TextView setas = (TextView)findViewById(R.id.Hdefinircomo);
        TextView sectiondescargas = (TextView)findViewById(R.id.Hs_descargas);


        //EVENTOS LISTENERS PARA CLICK EN TEXTVIEWS
        setaswall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(getApplicationContext())) {
                    //Opens in the StandAlonePlayer but in "Light box" mode
                    startActivity(YouTubeStandalonePlayer.createVideoIntent(AyudaSugerencia.this,
                            DEVELOPER_KEY, "sNvIKAi9qeE", 0, true, true));
                }
            }
        });

        setshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(getApplicationContext())) {
                    //Opens in the StandAlonePlayer but in "Light box" mode
                    startActivity(YouTubeStandalonePlayer.createVideoIntent(AyudaSugerencia.this,
                            DEVELOPER_KEY, "_LHU_Fm8iX8", 0, true, true));
                }
            }
        });

        setdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(getApplicationContext())) {
                    //Opens in the StandAlonePlayer but in "Light box" mode
                    startActivity(YouTubeStandalonePlayer.createVideoIntent(AyudaSugerencia.this,
                            DEVELOPER_KEY, "XMiVWxeUfsY", 0, true, true));
                }
            }
        });

        setas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(getApplicationContext())) {
                    //Opens in the StandAlonePlayer but in "Light box" mode
                    startActivity(YouTubeStandalonePlayer.createVideoIntent(AyudaSugerencia.this,
                            DEVELOPER_KEY, "3oARYiZRqow", 0, true, true));
                }
            }
        });

        sectiondescargas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(getApplicationContext())) {
                    //Opens in the StandAlonePlayer but in "Light box" mode
                    startActivity(YouTubeStandalonePlayer.createVideoIntent(AyudaSugerencia.this,
                            DEVELOPER_KEY, "1ljyYQ5QTmk", 0, true, true));
                }
            }
        });


        final TextInputLayout tilNombre = (TextInputLayout)findViewById(R.id.text_input_nombre);
        final TextInputLayout tilTitulo = (TextInputLayout)findViewById(R.id.text_input_titulo);
        final TextInputLayout tilComentario = (TextInputLayout)findViewById(R.id.text_input_comentario);
        EditText sgrNombre = (EditText)findViewById(R.id.content_text_input_nombre);
        EditText sgrTitulo = (EditText)findViewById(R.id.content_text_input_titulo);
        EditText sgrComentario = (EditText)findViewById(R.id.content_text_input_comentario);

        //ESCHAS PARA SABER CUANDO EL TEXTO CAMBIA Y SI TIENE MENSAJE DE ERROR QUITARLO
        sgrNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tilNombre.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sgrTitulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!s.equals("")){
                        tilTitulo.setError(null);
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sgrComentario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!s.equals("")||s==null){
                        tilComentario.setError(null);
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //EVENTO DEL BOTON ENVIAR
        Button btnEnviar = (Button)findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //LLAMADA AL METODO ISVACIO. SI ESTE DEVUELVE TRUE OPTENEMOS
                    //EL TEXTO DE C/U DE LOS EDITTEXT Y LO ENVIAMOS COOMO CORREO
                    if(!isVacio()){
                        String sgrNombre = ((EditText)findViewById(R.id.content_text_input_nombre)).getText().toString().trim();
                        String sgrTitulo = ((EditText)findViewById(R.id.content_text_input_titulo)).getText().toString().trim();
                        String sgrComentario = ((EditText)findViewById(R.id.content_text_input_comentario)).getText().toString().trim();

                        Intent mail = new Intent(Intent.ACTION_SEND);

                        mail.putExtra(Intent.EXTRA_EMAIL,new String[]{"vito8916@gmail.com"});

                        mail.putExtra(Intent.EXTRA_SUBJECT,sgrTitulo);

                        mail.putExtra(Intent.EXTRA_TEXT,sgrNombre +"\n"+ sgrComentario);

                        mail.setType("messege/rfc822");

                        startActivity(Intent.createChooser(mail, "Elige un cliente de correo"));
                    }




            }
        });
        if (bartool != null) {

            bartool.setDisplayHomeAsUpEnabled(true);
            bartool.setDisplayShowHomeEnabled(true);
            bartool.setHomeButtonEnabled(true);


        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();

    }

    public boolean isVacio(){

        TextInputLayout tilNombre = (TextInputLayout)findViewById(R.id.text_input_nombre);
        TextInputLayout tilTitulo = (TextInputLayout)findViewById(R.id.text_input_titulo);
        TextInputLayout tilComentario = (TextInputLayout)findViewById(R.id.text_input_comentario);

        String sgrNombre = ((EditText)findViewById(R.id.content_text_input_nombre)).getText().toString().trim();
        String sgrTitulo = ((EditText)findViewById(R.id.content_text_input_titulo)).getText().toString().trim();
        String sgrComentario = ((EditText)findViewById(R.id.content_text_input_comentario)).getText().toString().trim();

        if((sgrNombre==null || sgrNombre.equals("")) && (sgrTitulo==null || sgrTitulo.equals("")) && (sgrComentario== null || sgrComentario.equals(""))){
            tilComentario.setError(getString(R.string.sugerenci_error));
            tilNombre.setError(getString(R.string.sugerenci_error));
            tilTitulo.setError(getString(R.string.sugerenci_error));
            return true;
        }

        if(sgrNombre==null || sgrNombre.equals("")){
            tilNombre.setError(getString(R.string.sugerenci_error));
            return true;
        }

        if(sgrTitulo==null || sgrTitulo.equals("")){
            tilTitulo.setError(getString(R.string.sugerenci_error));
            return true;
        }

        if(sgrComentario== null || sgrComentario.equals("")){
            tilComentario.setError(getString(R.string.sugerenci_error));
            return true;
        }

        return false;

    }

}
