package com.dev.victor.spaper.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dev.victor.spaper.ActividadDetalleCategoria;
import com.dev.victor.spaper.Adapters.AdaptadorCategorias;
import com.dev.victor.spaper.FeedItem;
import com.dev.victor.spaper.R;
import com.dev.victor.spaper.util.RecyclerItemClickListener;
import com.dev.victor.spaper.util.VolleySingleton;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.prefs.Prefs;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoCategorias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoCategorias extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private LinearLayoutManager linearLayout;
    private static Context context;
    JSONObject[] objectos;
    JSONArray categorias;
    private static final String TAG = "RecyclerViewExample";
    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private AdaptadorCategorias adapter;
    private ProgressWheel progressBar;


    public static FragmentoCategorias newInstance(String param1, String param2) {
        FragmentoCategorias fragment = new FragmentoCategorias();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentoCategorias (){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragmento_categorias, container, false);
        context = view.getContext();
        progressBar = (ProgressWheel) view.findViewById(R.id.progress_bar_categorias);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setBarColor(ContextCompat.getColor(context, Prefs.with(getActivity()).readInt("accentColorByTheme")));

        String urlALbums = "https://api.flickr.com/services/rest/?method=flickr.photosets.getList&api_key=3a486f912da87a2011d3f5a03e01be7c&user_id=134427773%40N06&format=json&nojsoncallback=1";
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recicladorCategorias);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new ScaleInAnimator());

        //progressBar = (ProgressWheel) view.findViewById(R.id.progress_bar_categorias);
       // progressBar.setVisibility(View.VISIBLE);
        feedsList = new ArrayList<>();

        JsonObjectRequest jor = new JsonObjectRequest(
                Request.Method.GET,urlALbums,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    categorias = response.getJSONObject("photosets").getJSONArray("photoset");
                    objectos = new JSONObject[categorias.length()];
                    for(int i =0; i< objectos.length; i++)
                    {

                        FeedItem item = new FeedItem();
                        item.setTitle(categorias.getJSONObject(i).getJSONObject("title").getString("_content"));
                        item.setDescription(categorias.getJSONObject(i).getJSONObject("description").getString("_content"));
                        item.setFarm(categorias.getJSONObject(i).getString("farm"));
                        item.setSecret(categorias.getJSONObject(i).getString("secret"));
                        item.setId(categorias.getJSONObject(i).getString("primary"));
                        item.setServer(categorias.getJSONObject(i).getString("server"));
                        item.setNumFotos(categorias.getJSONObject(i).getString("photos"));

                        feedsList.add(item);
                    }

                    adapter = new AdaptadorCategorias(getContext(), feedsList);
                    mRecyclerView.setHasFixedSize(true);
                    ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(adapter);

                    mRecyclerView.setAdapter(new SlideInBottomAnimationAdapter(alphaAdapter));
                    mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                            new RecyclerItemClickListener.OnItemClickListener()
                                {
                                    @Override
                                     public void onItemClick(View view, int position) {
                                            // TODO Handle item click
                                             Log.e("@@@@@", "" + position);

                                        Intent detalles = new Intent(getContext(),ActividadDetalleCategoria.class);
                                        try {
                                            detalles.putExtra("detalles", categorias.getJSONObject(position).toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        getContext().startActivity(detalles);

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



        return  view;
    }

    @Override
    public void onPause() {
        getActivity().overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
        super.onPause();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
