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

import com.balysv.materialripple.MaterialRippleLayout;
import com.dev.victor.spaper.R;
import com.dev.victor.spaper.util.FeedItemAlbum;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Victor on 29/09/2015.
 */
public class AdaptadorAlbum extends RecyclerView.Adapter<AdaptadorAlbum.CustomViewHolder> {
    private List<FeedItemAlbum> feedItemList;
    private Context mContext;

    public AdaptadorAlbum(Context context, List<FeedItemAlbum> feedItemAlbums){
        this.mContext = context;
        this.feedItemList = feedItemAlbums;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = mContext;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_detalle_categoria, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        RelativeLayout rlitemcategoria = (RelativeLayout)view.findViewById(R.id.item_categoria_detalle_ripple);
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
    public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {
        FeedItemAlbum feedItemAlbum = feedItemList.get(position);

        Picasso.with(mContext)
                .load(feedItemAlbum.getUlrAlbumPhotolist())
                .noFade()
                .resize(300,330)
                .centerCrop()
                .tag(mContext)
                .into(customViewHolder.imgCate2);

        //Setting text view title
        //customViewHolder.textView.setText((feedItem.getTitle()));
        try {
            customViewHolder.textView.setText(feedItemAlbum.getTitulo());
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
            this.imgCate2 = (ImageView) view.findViewById(R.id.img_GridCategorias);
            this.textView = (TextView) view.findViewById(R.id.txtGridCategorias);

        }
    }
}
