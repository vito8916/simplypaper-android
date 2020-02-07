package com.dev.victor.spaper.Adapters;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
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
import com.dev.victor.spaper.FeedItem;
import com.dev.victor.spaper.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONObject;

import java.util.List;

import es.dmoral.prefs.Prefs;

/**
 * Created by Victor on 21/09/2015.
 */
public class AdaptadorCategorias extends RecyclerView.Adapter<AdaptadorCategorias.CustomViewHolder> {
    String p;
    JSONObject JObj = null;
    private List<FeedItem> feedItemList;
    private Context mContext;



    public AdaptadorCategorias(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = mContext;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.items_categorias, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        CardView cartacategoria = (CardView)view.findViewById(R.id.cartaCategoria);
        RelativeLayout rlitemcategoria = (RelativeLayout)view.findViewById(R.id.rlitemcategoria);
        //ripple
        int colorripple = R.color.colorripple;

        MaterialRippleLayout.on(rlitemcategoria)
                .rippleColor(ContextCompat.getColor(mContext,Prefs.with(mContext).readInt("accentColorByTheme")))
                .rippleOverlay(true)
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .create();

        Animation ani2 = AnimationUtils.loadAnimation(mContext, R.anim.abc_slide_in_bottom);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        FeedItem feedItem = feedItemList.get(i);

        //Download image using picasso library



        //Setting text view title
        customViewHolder.textView.setText((feedItem.getTitle()));
        try {
            customViewHolder.textView2.setText((feedItem.getDescription()));
            customViewHolder.NumeroFotos.setText((feedItem.getNumFotos()));
        }catch (Exception e){
            e.printStackTrace();
        }

        String urlImgListCate = "https://farm"+feedItem.getFarm()+".staticflickr.com/"+feedItem.getServer()+"/"+feedItem.getId()+"_"+feedItem.getSecret()+"_m.jpg";
        Transformation transformation = new RoundedTransformationBuilder()

                .cornerRadiusDp(10)
                .oval(false)
                .build();

        Picasso.with(mContext).load(urlImgListCate)
                .noFade()
                .resize(200,200)
                .centerCrop()
                .transform(transformation)
                .into(customViewHolder.imgCate2);
       /* switch (feedItem.getTitle()){
            case "Music":
                Picasso.with(mContext).load(R.drawable.music).noFade().transform(transformation).into(customViewHolder.imgCate2);
                break;
            case "Technology":
                Picasso.with(mContext).load(R.drawable.tecnologia).noFade().transform(transformation).into(customViewHolder.imgCate2);
                break;
            case "Minimalistas":
                Picasso.with(mContext).load(R.drawable.minimalistas).noFade().transform(transformation).into(customViewHolder.imgCate2);
                break;
            case "Degradados":
                Picasso.with(mContext).load(R.drawable.gradients).noFade().transform(transformation).into(customViewHolder.imgCate2);
                break;
            case "Vehiculos":
                Picasso.with(mContext).load(R.drawable.vehiculos).noFade().transform(transformation).into(customViewHolder.imgCate2);
                break;
            case "Landscape":
                Picasso.with(mContext).load(R.drawable.landscape).noFade().transform(transformation).into(customViewHolder.imgCate2);
                break;
            case "Animals":
                Picasso.with(mContext).load(R.drawable.animals).noFade().transform(transformation).into(customViewHolder.imgCate2);
                break;
            case "Portrait":
                Picasso.with(mContext).load(R.drawable.portrait_icon).noFade().transform(transformation).into(customViewHolder.imgCate2);
                break;
            case "Polygon":
                Picasso.with(mContext).load(R.drawable.icon_polygon).noFade().transform(transformation).into(customViewHolder.imgCate2);
                break;
            default:
                Picasso.with(mContext).load(R.drawable.material_background).noFade().transform(transformation).into(customViewHolder.imgCate2);
        }*/
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
        protected TextView NumeroFotos;


        public CustomViewHolder(View view) {
            super(view);
            this.imgCate2 = (ImageView) view.findViewById(R.id.img_categoria);
            this.textView = (TextView) view.findViewById(R.id.titulo_categoria);
            this.textView2 = (TextView) view.findViewById(R.id.descrip_categoria);
            this.NumeroFotos = (TextView) view.findViewById(R.id.NumFotos);

        }
    }


}
