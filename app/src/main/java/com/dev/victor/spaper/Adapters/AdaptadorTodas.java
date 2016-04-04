package com.dev.victor.spaper.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.balysv.materialripple.MaterialRippleLayout;
import com.dev.victor.spaper.FeedItem;
import com.dev.victor.spaper.R;
import com.dev.victor.spaper.util.FeedItemTodas;
import com.dev.victor.spaper.util.VolleySingleton;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 27/09/2015.
 */
public class AdaptadorTodas extends RecyclerView.Adapter<AdaptadorTodas.CustomViewHolder> {

    private List<FeedItemTodas> feedItemList;
    private Context mContext;




    public AdaptadorTodas(Context context, List<FeedItemTodas> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = mContext;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.items_todas, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        RelativeLayout rlitemcategoria = (RelativeLayout)view.findViewById(R.id.itemtodasripple);
        //ripple
        int colorripple = R.color.colorripple;

        MaterialRippleLayout.on(rlitemcategoria)
                .rippleColor(Color.parseColor("#FF4081"))
                .rippleOverlay(true)
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .create();

        Animation ani2 = AnimationUtils.loadAnimation(mContext, R.anim.abc_slide_in_bottom);

        //view.startAnimation(ani2);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        FeedItemTodas feedItem = feedItemList.get(i);

        Picasso.with(mContext)
                .load(feedItem.getUrlimg())
                .noFade()
                .resize(300,330)
                .centerCrop()
                .tag(mContext)
                .into(customViewHolder.imgCate2);

        //Setting text view title
        //customViewHolder.textView.setText((feedItem.getTitle()));
        try {
            customViewHolder.textView.setText(feedItem.getTitle());
        }catch (Exception e){
            e.printStackTrace();
        }


        customViewHolder.imgCate2.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public int getItemCount() {

        return (null != feedItemList ? feedItemList.size() : 0);
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imgCate2;
        protected TextView textView;


        public CustomViewHolder(View view) {
            super(view);
            this.imgCate2 = (ImageView) view.findViewById(R.id.img_todas);
            this.textView = (TextView) view.findViewById(R.id.txttitulo);

        }
    }
    public void swap(List list){
        if(feedItemList != null){
            feedItemList.clear();
            //feedItemList.addAll(list);
        }else{
            feedItemList = list;
        }
        notifyDataSetChanged();
    }

    /*
    Añade una lista completa de items
     */
    public void addAll(List<FeedItemTodas> lista){
       feedItemList.addAll(lista);
        notifyDataSetChanged();
    }

    /*
    Permite limpiar todos los elementos del recycler
     */
    public void clear(){
        feedItemList.clear();
        notifyDataSetChanged();
    }


}