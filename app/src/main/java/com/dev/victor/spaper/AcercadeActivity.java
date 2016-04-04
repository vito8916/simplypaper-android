package com.dev.victor.spaper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcercadeActivity extends AppCompatActivity {
    ImageView banner, icon;
    ImageView dev, free_p, flickimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acercade);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();


        banner = (ImageView)findViewById(R.id.backdrop);
        icon = (ImageView)findViewById(R.id.imgIcon);
        dev = (ImageView)findViewById(R.id.imgDeveloper);
        free_p = (ImageView)findViewById(R.id.free_p);
        flickimg = (ImageView)findViewById(R.id.flickrimg);

        Picasso.with(getApplicationContext())
                .load(R.drawable.banner2_about)
                .noFade()

                .into(banner);
        //banner.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Animation ani2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        banner.setAnimation(ani2);



        Picasso.with(getApplicationContext())
                .load(R.drawable.nuevo_icono_spaper2)
                .resize(150, 150)
                .noFade()
                .centerCrop()
                .into(icon);
        Animation ani3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_in);
        icon.setAnimation(ani3);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderWidthDp(1.0f)
                .borderColor(Color.DKGRAY)
                .cornerRadiusDp(6)
                .oval(false)
                .build();

        Picasso.with(getApplicationContext())
                .load(R.drawable.vic)
                .resize(150,150)
                .centerCrop()
                .noFade()
                .transform(transformation)
                .into(dev);
        dev.setAnimation(ani3);

        Picasso.with(getApplicationContext())
                .load(R.drawable.fp24)
                .resize(150, 150)
                .centerCrop()
                .noFade()
                .transform(transformation)
                .into(free_p);
        free_p.setAnimation(ani3);

        Picasso.with(getApplicationContext())
                .load(R.drawable.flickr)
                .resize(150,150)
                .centerCrop()
                .noFade()
                .transform(transformation)
                .into(flickimg);

        if (bar != null) {

            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setHomeButtonEnabled(true);


        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setAnimation(ani3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mail = new Intent(Intent.ACTION_SEND);

                mail.putExtra(Intent.EXTRA_EMAIL,new String[]{"vito8916@gmail.com"});

                mail.putExtra(Intent.EXTRA_SUBJECT,"subject");

                mail.putExtra(Intent.EXTRA_TEXT,"mensaje");

                mail.setType("messege/rfc822");

                startActivity(Intent.createChooser(mail, "Elige un cliente de correo"));
            }
        });
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
