package com.dev.victor.spaper.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.dev.victor.spaper.Adapters.AdaptadorCategorias;
import com.dev.victor.spaper.Adapters.AdaptadorNuevas;
import com.dev.victor.spaper.FullScreenActivity2;
import com.dev.victor.spaper.FullscreenActivity;
import com.dev.victor.spaper.R;
import com.dev.victor.spaper.util.FeedItemNuevas;
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


public class Fragmento_nuevas extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private LinearLayoutManager linearLayout;
    private static Context context;
    JSONObject[] objectos;
    private static final String TAG = "RecyclerViewExample";
    private List<FeedItemNuevas> feedsList;
    private RecyclerView mRecyclerView;
    private AdaptadorNuevas adapter;
    private ProgressWheel progressBar;


    public static Fragmento_nuevas newInstance(String param1, String param2) {
        Fragmento_nuevas fragment = new Fragmento_nuevas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragmento_nuevas() {
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
        final View view = inflater.inflate(R.layout.fragmento_nuevas, container, false);
        context = view.getContext();
        PreferenceManager.setDefaultValues(getActivity(), R.xml.ajustes, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int numOfColumn = sharedPref.getInt(getString(R.string.key_nunofcolumn_pref),2);

        String urlGaleria = "https://api.flickr.com/services/rest/?method=flickr.people.getPhotos&api_key=3a486f912da87a2011d3f5a03e01be7c&user_id=134427773%40N06&per_page=10&extras=original_format&format=json&nojsoncallback=1";
        mRecyclerView = (RecyclerView)view.findViewById(R.id.reciclador);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),numOfColumn));
        mRecyclerView.setItemAnimator(new ScaleInAnimator());

        progressBar = (ProgressWheel) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setBarColor(ContextCompat.getColor(context, Prefs.with(getActivity()).readInt("accentColorByTheme")));
        feedsList = new ArrayList<>();

        JsonObjectRequest jor = new JsonObjectRequest(
                Request.Method.GET,urlGaleria,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    final JSONArray fotos = response.getJSONObject("photos").getJSONArray("photo");
                    objectos = new JSONObject[fotos.length()];
                    for(int i =0; i< objectos.length; i++)
                    {

                        FeedItemNuevas item = new FeedItemNuevas();
                        item.setIdPhoto(fotos.getJSONObject(i).getString("id"));
                        item.setTitle(fotos.getJSONObject(i).getString("title"));
                        item.setUrlimg("https://farm" + fotos.getJSONObject(i).getString("farm") + ".static.flickr.com/" + fotos.getJSONObject(i).get("server") + "/" + fotos.getJSONObject(i).getString("id") + "_" + fotos.getJSONObject(i).getString("secret") + "_c.jpg");

                        feedsList.add(item);
                    }

                    adapter = new AdaptadorNuevas(getContext(), feedsList);
                    mRecyclerView.setHasFixedSize(true);
                    ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(adapter);

                    mRecyclerView.setAdapter(new SlideInBottomAnimationAdapter(alphaAdapter));
                    mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context,
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    // TODO Handle item click
                                    Log.e("@@@@@", "" + position);

                                    Intent detalles = new Intent(context, FullScreenActivity2.class);
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
        return  view;
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
