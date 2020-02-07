package com.dev.victor.spaper.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.dev.victor.spaper.Adapters.AdaptadorTodas;
import com.dev.victor.spaper.FullScreenActivity2;
import com.dev.victor.spaper.MainActivity;
import com.dev.victor.spaper.R;
import com.dev.victor.spaper.util.FeedItemTodas;
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
 * Use the {@link Fragmento_todas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragmento_todas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static Context context;
    JSONObject[] objectos;
    private static final String TAG = "RecyclerViewExample";
    private List<FeedItemTodas> feedsList;
    private RecyclerView mRecyclerView;
    private AdaptadorTodas adapterT;
    private ProgressWheel progressBar;
    private String urlGaleria;
    private SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayout mSwipyRefreshLayout;
    private JSONArray fotos;
    Button btnRetry;
    ImageView imgRtry ;
    TextView txtRetry;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragmento_todas.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragmento_todas newInstance(String param1, String param2) {
        Fragmento_todas fragment = new Fragmento_todas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragmento_todas() {
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragmento_todas, container, false);
        context = view.getContext();

        urlGaleria = "https://api.flickr.com/services/rest/?method=flickr.people.getPhotos&api_key=3a486f912da87a2011d3f5a03e01be7c&user_id=134427773%40N06&extras=original_format&format=json&nojsoncallback=1c";

       // refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh);
            mSwipyRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipyrefreshlayout) ;
        btnRetry = (Button)view.findViewById(R.id.btnRtry);
        imgRtry = (ImageView)view.findViewById(R.id.imgRtry);
        txtRetry = (TextView)view.findViewById(R.id.txtRetry);
        progressBar = (ProgressWheel) view.findViewById(R.id.progress_bar_todas);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setBarColor(ContextCompat.getColor(context,Prefs.with(getActivity()).readInt("accentColorByTheme")));
        feedsList = new ArrayList<>();

        //CHECK NETWORKCONNECTION
        Boolean networkState;
        networkState = MainActivity.networkstate();


        if(!networkState )
        {
            Snackbar.make(container, getResources().getString(R.string.networkNoavalible), Snackbar.LENGTH_LONG).show();

            imgRtry.setVisibility(View.VISIBLE);
            btnRetry.setVisibility(View.VISIBLE);
            txtRetry.setText(getResources().getString(R.string.networkNoavalible));
            txtRetry.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    obtener();
                    progressBar.setVisibility(View.VISIBLE);
                }
            });

        }

        mSwipyRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                obtener();
                Boolean networkState;
                networkState = MainActivity.networkstate();

                if(networkState )
                {
                    feedsList.clear();

                }
                else
                {
                    Snackbar.make(container, getResources().getString(R.string.networkNoavalible), Snackbar.LENGTH_LONG).show();

                }
            }
        });


        mSwipyRefreshLayout.setColorSchemeResources(Prefs.with(getActivity()).readInt("accentColorByTheme"));
        mSwipyRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
               mSwipyRefreshLayout.setRefreshing(true);

                obtener();
            }
        });

        PreferenceManager.setDefaultValues(getActivity(), R.xml.ajustes, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int numOfColumn = sharedPref.getInt(getString(R.string.key_nunofcolumn_pref),2);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recicladorTodas);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),numOfColumn));
        mRecyclerView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (mSwipyRefreshLayout.isRefreshing()) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );



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

    private void obtener(){
        JsonObjectRequest jor = new JsonObjectRequest(
                Request.Method.GET,urlGaleria,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                if(btnRetry.getVisibility()== View.VISIBLE){
                    btnRetry.setVisibility(View.GONE);
                    imgRtry.setVisibility(View.GONE);
                    txtRetry.setVisibility(View.GONE);
                    //imgRtry.setVisibility(View.GONE);
                }
                try {
                    fotos = response.getJSONObject("photos").getJSONArray("photo");
                    objectos = new JSONObject[fotos.length()];
                    for(int i =0; i< objectos.length; i++)
                    {

                        FeedItemTodas item = new FeedItemTodas();
                        item.setIdPhoto(fotos.getJSONObject(i).getString("id"));
                        item.setTitle(fotos.getJSONObject(i).getString("title"));
                        //item.setUrlimg("http://farm" + fotos.getJSONObject(i).getString("farm") + ".static.flickr.com/" + fotos.getJSONObject(i).get("server") + "/" + fotos.getJSONObject(i).getString("id") + "_" + fotos.getJSONObject(i).getString("secret") + "_c.jpg");

                        item.setUrlimg("https://farm" + fotos.getJSONObject(i).getString("farm") + ".staticflickr.com/" + fotos.getJSONObject(i).get("server") + "/" + fotos.getJSONObject(i).getString("id") + "_" + fotos.getJSONObject(i).getString("secret") + "_c.jpg");

                        feedsList.add(item);

                    }
                    mRecyclerView.setItemAnimator(new ScaleInAnimator());

                    adapterT = new AdaptadorTodas(getContext(), feedsList);
                    adapterT.notifyDataSetChanged();
                    mRecyclerView.setHasFixedSize(true);
                    ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(adapterT);
                    mRecyclerView.setAdapter(new SlideInBottomAnimationAdapter(alphaAdapter));


                    //nuevo final

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onItemsLoadComplete();
                // notifying list adapter about data changes
                // so that it renders the list view with updated data


                adapterT.notifyDataSetChanged();
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //refreshLayout.setRefreshing(false);
                mSwipyRefreshLayout.setRefreshing(false);
            }
        }
        );



        VolleySingleton.getInstance().getRequestQueue().add(jor);
    }
    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        //adapterT.clear();
       adapterT.addAll(feedsList);
        // ...
        adapterT.notifyDataSetChanged();
        // Stop refresh animation
        //refreshLayout.setRefreshing(false);
        mSwipyRefreshLayout.setRefreshing(false);
    }

}
