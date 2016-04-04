package com.dev.victor.spaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dev.victor.spaper.Adapters.AdaptadorAlbum;
import com.dev.victor.spaper.util.FeedItemAlbum;
import com.dev.victor.spaper.util.RecyclerItemClickListener;
import com.dev.victor.spaper.util.VolleySingleton;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class ActividadDetalleCategoria extends AppCompatActivity {
    private static Context context;
    JSONObject[] objectos;
    private static final String TAG = "RecyclerViewExample";
    private List<FeedItemAlbum> feedsList;
    private RecyclerView mRecyclerView;
    private AdaptadorAlbum adapterA;
    private ProgressWheel progressBar;
    String urlAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actividad_detalle_categoria);
        context = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();


       // TextView id_album = (TextView)findViewById(R.id.txtidde_album);

        Intent intent = getIntent();
        JSONObject JObj = null;
        String jsostring = (String) intent.getExtras().get("detalles");

        try {
            JObj = new JSONObject(jsostring);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(JObj != null){
            try {
                urlAlbum = "https://api.flickr.com/services/rest/?method=flickr.photosets.getPhotos&api_key=3a486f912da87a2011d3f5a03e01be7c&photoset_id="+JObj.getString("id")+"&user_id=134427773%40N06&extras=original_format&format=json&nojsoncallback=1";
                Log.d("IDDEALBUM", urlAlbum);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mRecyclerView = (RecyclerView)findViewById(R.id.recicladorGridCategoria);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context,2));
        mRecyclerView.setItemAnimator(new ScaleInAnimator());

        progressBar = (ProgressWheel)findViewById(R.id.progress_bar_GridCategoria);
        progressBar.setVisibility(View.VISIBLE);
        feedsList = new ArrayList<>();

        JsonObjectRequest jor = new JsonObjectRequest(
                Request.Method.GET,urlAlbum,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    final JSONArray fotos = response.getJSONObject("photoset").getJSONArray("photo");
                    objectos = new JSONObject[fotos.length()];
                    for(int i =0; i< objectos.length; i++)
                    {

                        FeedItemAlbum item = new FeedItemAlbum();
                        item.setId(fotos.getJSONObject(i).getString("id"));
                        item.setTitulo(fotos.getJSONObject(i).getString("title"));

                        item.setSecret(fotos.getJSONObject(i).getString("secret"));
                        item.setFarm(fotos.getJSONObject(i).getString("farm"));
                        item.setServer(fotos.getJSONObject(i).getString("server"));

                        item.setUlrAlbumPhotolist("http://farm" + fotos.getJSONObject(i).getString("farm") + ".static.flickr.com/" + fotos.getJSONObject(i).get("server") + "/" + fotos.getJSONObject(i).getString("id") + "_" + fotos.getJSONObject(i).getString("secret") + "_c.jpg");

                        feedsList.add(item);
                    }

                    adapterA = new AdaptadorAlbum(context, feedsList);
                    mRecyclerView.setHasFixedSize(true);
                    ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(adapterA);

                    mRecyclerView.setAdapter(new SlideInBottomAnimationAdapter(alphaAdapter));
                    mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context,
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    // TODO Handle item click
                                    Log.e("@@@@@", "" + position);

                                    Intent detalles = new Intent(context, FullscreenActivity.class);
                                    try {
                                        detalles.putExtra("detalles", fotos.getJSONObject(position).toString());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(detalles);
                                }
                            }));


                    //nuevo final

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        }
        );



        VolleySingleton.getInstance().getRequestQueue().add(jor);




        if (bar != null) {
            try {
                if (JObj != null) {
                    bar.setTitle(JObj.getJSONObject("title").getString("_content"));
                    toolbar.setTitle(JObj.getJSONObject("title").getString("_content"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setHomeButtonEnabled(true);


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

}
