package com.dev.victor.spaper.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;

import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.dev.victor.spaper.FullscreenDescargas;
import com.dev.victor.spaper.MainActivity;
import com.dev.victor.spaper.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragmento_descargas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragmento_descargas extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MainActivity mainActivity;

    private String mParam1;
    private String mParam2;

    ArrayList<String> f = new ArrayList<>();   // list of available files in  path
    File[] listFile;

    TabLayout tav;
    AppBarLayout appBar;

    public static Fragmento_descargas newInstance(String param1, String param2) {
        Fragmento_descargas fragment = new Fragmento_descargas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragmento_descargas() {
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
        View view = inflater.inflate(R.layout.fragment_fragmento_descargas, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.desc_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        agregarToolbar(toolbar);

        getSdcardImages();
        Animation ani3 = AnimationUtils.loadAnimation(getContext(), R.anim.scale_in);
        Animation ani4 = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        GridView imggrid = (GridView) view.findViewById(R.id.gridView);
        ImageAdapter imgAdapter = new ImageAdapter(getContext(),R.layout.grid_downloads);
        imggrid .setAdapter(imgAdapter);
        if(imgAdapter.isEmpty()){
            TextView emptydonwloadtext = (TextView)view.findViewById(R.id.emptydowloadtext);
            ImageView imgnodownload = (ImageView)view.findViewById(R.id.imgNodownload);
            Picasso.with(getContext()).load(R.drawable.close2)
                    .noFade()
                    .resize(300,300)
                    .centerCrop()
                    .into(imgnodownload);
            imgnodownload.setVisibility(View.VISIBLE);
            imgnodownload.setAnimation(ani3);
            emptydonwloadtext.setText(getString(R.string.emprtydonwloadtext));
            emptydonwloadtext.setAnimation(ani4);
        }

        imggrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("esto es un p::", String.valueOf(listFile[position]));
                Intent detalles = new Intent(getContext(),FullscreenDescargas.class);
                detalles.putExtra("datos",listFile[position].toString());
                getContext().startActivity(detalles);
            }
        });





        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    private void agregarToolbar(Toolbar toolbar) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        final ActionBar ab = activity.getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }


    public void getSdcardImages() {
        //initialize root directory
        String sdCardDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

        File file = new File(sdCardDirectory +"/SimplyPaper/");
        //File file = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/SimplyPaper/");

        if (file.isDirectory()) {
            listFile = file.listFiles();


            if( listFile != null){
                for (int i = 0; i < listFile.length; i++) {

                    f.add(listFile[i].getAbsolutePath());

                }
            }

        }
        Log.d("ffffff:", String.valueOf(f));

    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private int layoutResourceId;
        public ImageAdapter(Context context, int resource) {

            this.mContext = context;
            this.layoutResourceId = resource;
        }

        public int getCount() {
            return f.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            final ViewHolder holder;


            if(v==null){
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

                v =  inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.imageview = (ImageView) v.findViewById(R.id.img_download);



                v.setTag(holder);
            }
            else {
                holder = (ViewHolder) v.getTag();
            }



            //Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
          // holder.imageview.setImageBitmap(myBitmap);
            Log.d("lista de file::", String.valueOf(listFile[position]));
            Picasso.with(mContext).load(listFile[position])
                    .noFade()
                    .resize(300,400)
                    .centerCrop()
                    .into(holder.imageview);
            return v;

        }
    }

    class ViewHolder {
        ImageView imageview;
        RelativeLayout rlitemcategoria;

    }

}
