package com.dev.victor.spaper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;

import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.widget.Toast;



import com.dev.victor.spaper.util.ColorPalette;
import com.dev.victor.spaper.util.Fab;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.wasabeef.blurry.Blurry;


public class FullscreenDescargas extends AppCompatActivity implements View.OnClickListener{
    TouchImageView imgfulldescargas;
    private View mContentView;
    private MaterialSheetFab materialSheetFab;
    View.OnClickListener mOnClickListener;
    String nombreIMG;

    ColorPalette colorPalette;
    WindowManager w;
    Display d;
    int widthPixels;
    int heightPixels;
    int height;
    int width;
    String img;
    DisplayMetrics metrics;
    private ProgressDialog mProgress;
    Bitmap bitmap20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_descargas);

        mContentView = findViewById(R.id.contentfull);
        Intent intent = getIntent();



        //mBlurringView = (BlurringView) findViewById(R.id.blurring_view);

        w = getWindowManager();
        d = w.getDefaultDisplay();
        metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        widthPixels  = metrics.widthPixels;
        heightPixels = metrics.heightPixels;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        }
        imgfulldescargas = (TouchImageView) findViewById(R.id.imgfullDescargas);
        final Animation ani4 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width,height);
        imgfulldescargas.setLayoutParams(parms);

        final Fab fab = (Fab)findViewById(R.id.fab3);

        img = (String) intent.getExtras().get("datos");
        Log.d("direeccion:;:",img);
        File imageFile = null;
        if (img != null) {
            imageFile = new File(img);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap20 = BitmapFactory.decodeFile(img,options);

        hide();

        Picasso.with(getApplicationContext())
                .load(imageFile)
                .tag(this)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .noFade()

                .into(imgfulldescargas, new Callback() {
                    @Override
                    public void onSuccess() {
                        //imgDetalle.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        imgfulldescargas.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imgfulldescargas.setAnimation(ani4);
                        findViewById(R.id.progress_bar_fulldescargas).setVisibility(View.GONE);

                        Bitmap bitmap = ((BitmapDrawable) imgfulldescargas.getDrawable()).getBitmap();

                        //EXTRAER LOS COLORES DEL BITMAP PARA ASIGNARLO AL FAB
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette p) {
                                int Color = 0;

                                View sheetView = findViewById(R.id.fab_sheet3);
                                //final ImageView overlay = (ImageView) findViewById(R.id.overlay);
                                View overlay = findViewById(R.id.overlay3);
                                int sheetColor = getResources().getColor(R.color.background_card);

                                Palette.Swatch vibrant = p.getVibrantSwatch();
                                Palette.Swatch lightvibrant = p.getLightVibrantSwatch();
                                Palette.Swatch darkvibrant = p.getDarkVibrantSwatch();
                                Palette.Swatch muted = p.getMutedSwatch();

                                // Use generated instance
                                View fila_fab_expand = findViewById(R.id.fila_fab_expand3);
                                Log.d("COLOR VIBRANT", String.valueOf(p.getVibrantColor(0)));
                                if (vibrant != null) {
                                    Color = vibrant.getRgb();
                                    // colorPalette.setVibrant(vibrant.getRgb());
                                } else if (darkvibrant != null) {
                                    Color = darkvibrant.getRgb();
                                    //colorPalette.setDarkVibrant(darkvibrant.getRgb());
                                } else if (muted != null) {
                                    Color = muted.getRgb();
                                    //colorPalette.setMuted(muted.getRgb());
                                } else if (lightvibrant != null) {
                                    Color = lightvibrant.getRgb();
                                    //colorPalette.setLightVibrant(lightvibrant.getRgb());
                                } else {
                                    Color = R.color.colorAccent;
                                }

                                /// Log.d("COLOR DE EXPANSION 2:::", String.valueOf(ColorDeExpansion));
                                Log.d("COLOR EXTRAIDO DE:::", String.valueOf(Color));
                                fila_fab_expand.setBackgroundColor(Color);
                                fab.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{Color}));

                                materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, Color);
                                setupFab();
                            }
                        });

                        findViewById(R.id.progress_bar_fulldescargas).setVisibility(View.GONE);
                        ShowFab();

                    }

                    @Override
                    public void onError() {

                    }
                });


    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mContentView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}

        if (Build.VERSION.SDK_INT <= 18) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }*/


    }

    //METODO PARA MOSTRAR EL FAB DESPUES QUE EL RESPONSE SE EFECTUO CORRECTAMENTE
    private void ShowFab(){
        final Animation ani3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_in);

        Fab fab = (Fab)findViewById(R.id.fab3);
        fab.show();
        fab.setAnimation(ani3);

    }

    //METODO PARA CONFIGURAR EL FLOATING BUTTON
    private void setupFab(){
        ColorPalette colorPalette = new ColorPalette();



        //set material sheeet FAB
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {

                try {
                    Blurry.with(FullscreenDescargas.this)
                            .radius(10)
                            .sampling(8)
                            .animate(300)
                            .color(R.color.blur)
                            .async()
                            .onto((ViewGroup) findViewById(R.id.contentfull));
                }catch (Exception e){
                    e.printStackTrace();
                }


                // overlay.setAlpha(1f);
                super.onShowSheet();
            }

            @Override
            public void onHideSheet() {

                Blurry.delete((ViewGroup) findViewById(R.id.contentfull));
                super.onHideSheet();
            }
        });

        //SET ONCLICK LISTENER PARA LOS ELEMENTOS DEL FLOATING BUTTON
        findViewById(R.id.fab_sheet_item_wallpaper3).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_compartir).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_set_as).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_back3).setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.fab_sheet_item_wallpaper3:
                wallpaper(bitmap20);

                //showProgressAsWallpaper();
                materialSheetFab.hideSheet();
                break;
            case R.id.fab_sheet_item_compartir:
                //downloadimg();
                shareImg(img);
                materialSheetFab.hideSheet();
                //showProgress();
                break;
            case R.id.fab_sheet_item_set_as:
                //shareBitmap();
                setAs(img);
                materialSheetFab.hideSheet();
                break;
            case R.id.fab_sheet_item_back3:
                materialSheetFab.hideSheet();
                break;
        }
    }

    private void showProgressAsWallpaper(){
        mProgress = ProgressDialog.show(this, "", getResources().getString(R.string.setwallpaper));

    }
    private void stopProgressAsWallpaper(){
        mProgress.cancel();
    }

    private void wallpaper(Bitmap bitmap) {


        boolean success = false;
        WallpaperManager wpManager = WallpaperManager.getInstance(getApplicationContext());

        try {

                Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, width, height, true);
                wpManager.setWallpaperOffsetSteps(1, 1);
                wpManager.suggestDesiredDimensions(bitmapResized.getWidth(), bitmapResized.getHeight());
                wpManager.setBitmap(bitmapResized);



            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (success) {
            //stopProgressAsWallpaper();
            Snackbar.make(findViewById(R.id.contentfull),getResources().getString(R.string.msg_setaswallpaper),Snackbar.LENGTH_SHORT).show();

        } else {
            Snackbar.make(findViewById(R.id.contentfull), getResources().getString(R.string.msg_err_setaswallpaper), Snackbar.LENGTH_SHORT).show();
        }
    }

    //METODO PARA ENVIAR LA IMAGEN "COMO" Y PODER DEFINIRLA DE DISTINTAS MANERAS.
    private void setAs(String dir){

        File dirImg = new File(dir);
        Log.d("DIREC EN SETAS:",dir);
        Uri sendUri = Uri.fromFile(dirImg);
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.setDataAndType(sendUri, "image/jpg");
        intent.putExtra("mimeType", "image/jpg");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.ayuda_definircomo)), 200);
    }

    private void shareImg(String dir){

        File imageFile = new File(dir); // Imagename.png
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(dir);
        FileOutputStream out;


        try{
            out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // Compress Image
            out.flush();
            out.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(getApplicationContext(),new String[] { imageFile.getAbsolutePath() }, null,new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                    String playStoreLink = "https://play.google.com/store/apps/details?id=" +
                            getPackageName();

                    String msg = getString(R.string.sharespaper) + playStoreLink;
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_TEXT, msg);

                    intent.putExtra(android.content.Intent.EXTRA_TITLE, getString(R.string.compatidaatraves));
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.ayuda_compartir)), 1);
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
            imageError(e);
        }
    }

    private void imageError(Exception e) {
        Toast.makeText(getApplicationContext(), getString(R.string.interneterror), Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }



}
