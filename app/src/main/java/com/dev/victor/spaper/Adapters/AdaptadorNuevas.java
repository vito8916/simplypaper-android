package com.dev.victor.spaper.Adapters;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
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
import com.dev.victor.spaper.util.FeedItemNuevas;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.prefs.Prefs;

/**
 * Created by Victor on 28/09/2015.
 */
public class AdaptadorNuevas extends RecyclerView.Adapter<AdaptadorNuevas.CustomViewHolder> {

    private List<FeedItemNuevas> feedItemList;
    private Context mContext;




    public AdaptadorNuevas(Context context, List<FeedItemNuevas> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = mContext;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.items_nuevas, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        RelativeLayout rlitemcategoria = (RelativeLayout)view.findViewById(R.id.itemnuevasripple);
        //ripple
        int colorripple = R.color.colorripple;

        MaterialRippleLayout.on(rlitemcategoria)
                .rippleColor(ContextCompat.getColor(mContext,Prefs.with(mContext).readInt("accentColorByTheme")))
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
        FeedItemNuevas feedItem = feedItemList.get(i);

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
        protected TextView textView2;
        protected String urlinfoImg;

        public CustomViewHolder(View view) {
            super(view);
            this.imgCate2 = (ImageView) view.findViewById(R.id.img_nuevas);
            this.textView = (TextView) view.findViewById(R.id.txttitulonuevas);
            //this.textView2 = (TextView) view.findViewById(R.id.descrip_categoria2);
        }
    }


}