package com.dev.victor.spaper;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.dev.victor.spaper.util.ColorPalette;
import com.dev.victor.spaper.util.Fab;
import com.dev.victor.spaper.util.VolleySingleton;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

public class FullScreenActivity2 extends AppCompatActivity implements View.OnClickListener {

    private MaterialSheetFab materialSheetFab;
    View.OnClickListener mOnClickListener;
    private View mContentView;
    String urlImgenOriginal;                     //variable para almacenar el url de imagen tamaño original 3K
    String urlImagenHight;      //variable para almacenar el url de imagen tamaño Hight Definition
    String urlImagenBig;        //variable para almacenar el url de imagen tamaño Big (Grande)
    String urlImagenSmall;      //variable para almacenar el url de imagen tamaño small (Pequeño)

    String urlIMGdescarga;      //variable Para Definir Que Tipo De URL se usará para Descargar la Imagen
    String urlIMGImgViewFull;   //variable Para Definir Que Tipo De URL se usará para Mostrar la Imagen
    String urlIMGwallpaper;     //variable Para Definir Que Tipo De URL se usará para Establecer como wallpaper la Imagen

    String nombreIMG;
    String formatoIMG;
    TouchImageView ImgFulscreen;    //Contenedor ImageView Donde se Muestra la Imagen a fullscreen

    RequestQueue requestQueue;
    private ProgressDialog mProgress;
    ColorPalette colorPalette;

    View vWallpaper, vShare, vDownload, vBack;
    Bitmap.Config bconfig; //variable para manejar la compresion de las imagagenes segun el HEADSIZE de java


    DisplayMetrics metrics;
    int height;
    int width;

    //initialize our progress dialog/bar
    ProgressDialog mProgressDialog;
    //int DIALOG_DOWNLOAD_PROGRESS = 0;

    //initialize root directory
    String sdCardDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

    File rootDir = new File(sdCardDirectory +"/SimplyPaper/");
    // public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_screen2);

        ImgFulscreen = (TouchImageView)findViewById(R.id.imgFullscreen_2);
        mContentView = findViewById(R.id.fullscreen_content_2);

        //OBTNER EL TAMAÑO DE LA PANTALLA DEL SMARTPHONE
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        if (Build.VERSION.SDK_INT >= 14) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(metrics, realSize);
                //widthPixels = realSize.x;
                // heightPixels = realSize.y;
                width = realSize.x;
                height = realSize.y;
            } catch (Exception ignored) {
            }
        }

        //RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width,height);
       // ImgFulscreen.setLayoutParams(parms);

        //ANIMACIÓN CON LA QUE APARECERA LA IMAGEN AL SER CARGADA
        final Animation ani4 = AnimationUtils.loadAnimation(this, R.anim.fade_in);




        final Fab fab = (Fab)findViewById(R.id.fab2_2);
        requestQueue = Volley.newRequestQueue(this);
        colorPalette = new ColorPalette();

        Intent intent = getIntent();
        JSONObject datos = null;
        String jsostring = (String) intent.getExtras().get("detalles");

        try {
            datos = new JSONObject(jsostring);
            Log.d("OBJETO", "" + datos);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(datos != null) {
            try {
                nombreIMG = datos.getString("title");
                formatoIMG = datos.getString("originalformat");
                urlImagenBig = "https://live.staticflickr.com/"
                        + datos.getString("server")
                        + "/" + datos.getString("id")
                        + "_" + datos.getString("secret") + "_b."
                        + datos.getString("originalformat")
                        + "";
                urlImagenSmall = "https://live.staticflickr.com/"
                        + datos.getString("server")
                        + "/" + datos.getString("id")
                        + "_" + datos.getString("secret")
                        + "_b." + datos.getString("originalformat")
                        + "";
                urlImgenOriginal = "https://live.staticflickr.com/"
                        + datos.getString("server")
                        + "/" + datos.getString("id")
                        + "_" + datos.getString("originalsecret")
                        + "_b." + datos.getString("originalformat")
                        + "";
                urlImagenHight = "https://live.staticflickr.com/"
                        + datos.getString("server")
                        + "/" + datos.getString("id")
                        + "_" + datos.getString("secret")
                        + "_b." + datos.getString("originalformat")
                        + "";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Verificacion de HEADSIZE de JAVA
            Log.d("URLIMAGEN GRANDE B: ", urlImagenBig);
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            int memoryClass = am.getMemoryClass();
            Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
            if (memoryClass <= 32) {                                                      //Si la menoria es menor igual que 32mb
                urlIMGdescarga = urlImagenBig;                                       //Se establece el url de descarga como imagen grande
                urlIMGImgViewFull = urlImagenBig;                                       // se establece el url para el imageview que muestra la imagen como imagen grande
                urlIMGwallpaper = urlImagenBig;
                bconfig = Bitmap.Config.RGB_565;
            } else if(memoryClass > 32 && memoryClass < 128){
                urlIMGdescarga = urlImagenHight;                          //la calidad de la imagen de descarga es la hight
                urlIMGImgViewFull = urlImagenHight;                       //la calidad para mostrar en el imageview es hight
                urlIMGwallpaper = urlImagenHight;
                bconfig = Bitmap.Config.RGB_565;
            }else {                                                             // si la memoria es mayor que 32 MB la calidad de imagen cambia
                urlIMGdescarga = urlImgenOriginal;                          //la calidad de la imagen de descarga es la original
                urlIMGImgViewFull = urlImagenHight;                       //la calidad para mostrar en el imageview es hight
                urlIMGwallpaper = urlImagenHight;
                bconfig = Bitmap.Config.ARGB_8888;
            }


            //IMAGEN EN FULL SCREN VISTA DE DETALLE

            ImageRequest request = new ImageRequest(urlIMGImgViewFull,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {

                            // int width = bitmap.getWidth();
                            int height = bitmap.getHeight();

                            ImgFulscreen.setImageBitmap(bitmap);//
                            ImgFulscreen.setAnimation(ani4);
                            Log.d("ALTO", String.valueOf(height));
                            if (bitmap.getWidth() < bitmap.getHeight()) {

                                ImgFulscreen.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }

                            //EXTRAER LOS COLORES DEL BITMAP PARA ASIGNARLO AL FAB
                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette p) {
                                    int Color = 0;

                                    View sheetView = findViewById(R.id.fab_sheet_2);
                                    //final ImageView overlay = (ImageView) findViewById(R.id.overlay);
                                    View overlay = findViewById(R.id.overlay_2);
                                    int sheetColor = getResources().getColor(R.color.background_card);

                                    Palette.Swatch vibrant = p.getVibrantSwatch();
                                    Palette.Swatch lightvibrant = p.getLightVibrantSwatch();
                                    Palette.Swatch darkvibrant = p.getDarkVibrantSwatch();
                                    Palette.Swatch muted = p.getMutedSwatch();

                                    // Use generated instance
                                    View fila_fab_expand = findViewById(R.id.fila_fab_expand_2);
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
                                    if (fila_fab_expand != null) {
                                        fila_fab_expand.setBackgroundColor(Color);
                                    }
                                    if (fab != null) {
                                        fab.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{Color}));
                                    }

                                    if (sheetView != null) {
                                        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, Color);
                                    }
                                    setupFab();

                                }
                            });

                            findViewById(R.id.progress_bar_fullscreen_2).setVisibility(View.GONE);
                            ShowFab();
                        }
                    }, 0, 0, null, bconfig, //Bitmap.Config.RGB_565 reduce la calidad del bitmap/2
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            imageError(error);
                        }
                    });


            VolleySingleton.getInstance().getRequestQueue().add(request);
        }
    }

    //METODO PARA MOSTRAR EL FAB DESPUES QUE EL RESPONSE SE EFECTUO CORRECTAMENTE
    private void ShowFab(){
        final Animation ani3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_in);
        //configurarIconColor();
        Fab fab = (Fab)findViewById(R.id.fab2_2);
        if (fab != null) {
            fab.show();
            fab.setAnimation(ani3);
        }


    }

    //METODO PARA CONFIGURAR EL FLOATING BUTTON
    private void setupFab(){
        ColorPalette colorPalette = new ColorPalette();




        //set material sheeet FAB
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                Blurry.with(FullScreenActivity2.this)
                        .radius(10)
                        .sampling(8)
                        .animate(300)
                        .async()
                        .onto((ViewGroup) mContentView);

                // overlay.setAlpha(1f);
                super.onShowSheet();
            }

            @Override
            public void onHideSheet() {
                Blurry.delete((ViewGroup) mContentView);
                super.onHideSheet();
            }
        });


        //SET ONCLICK LISTENER PARA LOS ELEMENTOS DEL FLOATING BUTTON
        vWallpaper = findViewById(R.id.fab_sheet_item_wallpaper_2);
        vDownload = findViewById(R.id.fab_sheet_item_download_2);
        vShare = findViewById(R.id.fab_sheet_item_establecer_2);
        vBack = findViewById(R.id.fab_sheet_item_back_2);

        vWallpaper.setOnClickListener(this);
        vShare.setOnClickListener(this);
        vDownload.setOnClickListener(this);
        vBack.setOnClickListener(this);
    }

    /*private void configurarIconColor() {
        Drawable iconImage, iconArrowLeft, iconDownload, iconShare;
        iconImage = ContextCompat.getDrawable(this, R.drawable.zzz_tooltip_image);
        iconArrowLeft = ContextCompat.getDrawable(this, R.drawable.zzz_arrow_left);
        iconDownload = ContextCompat.getDrawable(this, R.drawable.zzz_cloud_download);
        iconShare = ContextCompat.getDrawable(this, R.drawable.zzz_share_variant);

        iconImage.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        iconArrowLeft.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        iconDownload.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        iconShare.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }*/

    //METODO DE ERROR DE CONEXION A INTERNET
    private void imageError(Exception e) {
        Snackbar.make(mContentView,getString(R.string.interneterror),Snackbar.LENGTH_SHORT);
        //Toast.makeText(getApplicationContext(),getString(R.string.interneterror), Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    //METODO PARA OCULTAR EL ACTION BAR O FULLSCREEN
    private void hide() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }

        }

        if (Build.VERSION.SDK_INT <= 18) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.fab_sheet_item_wallpaper_2:
                wallpaper();
                showProgressAsWallpaper();
                materialSheetFab.hideSheet();
                break;
            case R.id.fab_sheet_item_download_2:
                downloadIMG();
                // downloadimg();
                materialSheetFab.hideSheet();
                //showProgress();
                break;
            case R.id.fab_sheet_item_establecer_2:
                shareBitmap();
                materialSheetFab.hideSheet();
                break;
            case R.id.fab_sheet_item_back_2:
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

    //METODO PARA DETECTAR SI EL USUARIO PRESIONA EL BOTON BACK.
    @Override
    public void onBackPressed() {
        if (materialSheetFab == null){
            super.onBackPressed();
        }else if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else {

            super.onBackPressed();
        }
    }

    //METODO PARA ESTABLECER EL BITMAP COMO FONDO DE PANTALLA
    private void wallpaper(){

        ImageRequest imageRequest = new ImageRequest(urlIMGwallpaper,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        boolean success = false;
                        WallpaperManager wpManager = WallpaperManager.getInstance(getBaseContext());

                        try {
                            if(Build.VERSION.SDK_INT < 19){
                                Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, width, height, true);
                                wpManager.setWallpaperOffsetSteps(1, 1);
                                wpManager.suggestDesiredDimensions(width, height);
                                wpManager.setBitmap(bitmap);
                            }else{
                                Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, width, height, true);
                                wpManager.setWallpaperOffsetSteps(1, 1);
                                wpManager.suggestDesiredDimensions(width, height);
                                wpManager.setBitmap(bitmap);
                            }

                            success = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (success) {
                            stopProgressAsWallpaper();
                            Snackbar.make(mContentView,getResources().getString(R.string.msg_setaswallpaper),Snackbar.LENGTH_SHORT).show();
                            executeDelayed();

                        } else {
                            Snackbar.make(mContentView, getResources().getString(R.string.msg_err_setaswallpaper), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }, width, height, null,Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });Log.d("ANCHOaLTO: ", String.valueOf(width)+"ffffffff"+String.valueOf(height));

        VolleySingleton.getInstance().getRequestQueue().add(imageRequest);
    }

    //METODO PARA COMPARTIR EL BITMAP MEDIANTE OTRAS APPS
    private void shareBitmap(){
        ImageRequest imageRequest = new ImageRequest(urlIMGImgViewFull,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        //Canvas canvas = new Canvas(bitmap);

                        // String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image Description", null);
                        //   Uri uri = Uri.parse(path);
                        // Log.d("DIREC D IMG SHR:", path);


                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/SimplyPaper/Spaper/"); //Creates app specific folder
                        if(!path.exists()){
                            path.mkdirs();
                        }

                        File imageFile = new File(path.getAbsolutePath(), nombreIMG+"."+formatoIMG); // Imagename.png
                        FileOutputStream out;

                        try{
                            out = new FileOutputStream(imageFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // Compress Image
                            out.flush();
                            out.close();
                            bitmap.recycle();


                        } catch(Exception e) {
                            e.printStackTrace();
                        }

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

                                intent.putExtra(Intent.EXTRA_TEXT, msg);

                                intent.putExtra(android.content.Intent.EXTRA_TITLE, getString(R.string.compatidaatraves));
                                intent.putExtra(Intent.EXTRA_STREAM, uri);
                                intent.setType("image/jpeg");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivityForResult(Intent.createChooser(intent, getString(R.string.ayuda_compartir)), 1);
                            }
                        });

                    }

                }, 0, 0, null,bconfig,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        VolleySingleton.getInstance().getRequestQueue().add(imageRequest);



    }

    //METODO PARA ELIMINAR LA IMAGEN UNA VEZ QUE ESTA ES COMPARTIDA
    protected void onResume() {

        //executeDelayed();
        hide();
        final Runnable r = new Runnable() {
            public void run() {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/SimplyPaper/Spaper/"); //Creates app specific folder

                File imageFile = new File(path.getAbsolutePath(), nombreIMG+"."+formatoIMG); // Imagename.png
                File file= new File(String.valueOf(imageFile));
                if(file.exists())
                {
                    file.delete();
                    path.delete();
                }

            }
        };
        new Handler().postDelayed(r, 3000 );

        super.onResume();
    }

    //POST DELAY PARA EJECUTAR EL METODO HIDE() DESPUES DE UNOS SEGUNDOS
    private void executeDelayed() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // execute after 500ms
                hide();
            }
        }, 800);
    }

    //METODO 2 PARA DESCARGAR IMAGEN
    public void downloadIMG(){


        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){

        }
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(FullScreenActivity2.this, getString(R.string.permisoConcedido), Toast.LENGTH_SHORT).show();

                File dwnFolder;
                if(!rootDir.exists()){
                    rootDir.mkdir();
                }
                dwnFolder = new File(rootDir, nombreIMG+"."+formatoIMG);
                final DownloadTask downloadTask = new DownloadTask(FullScreenActivity2.this,dwnFolder,getResources().getString(R.string.downloading));
                downloadTask.execute(urlIMGdescarga);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(FullScreenActivity2.this, getString(R.string.permisoDenegado) + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getString(R.string.permisosGuardarMsj))
                .setDeniedMessage(getString(R.string.permisoMsjRechazo))
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.WAKE_LOCK, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();


    }

    //CLASE ASYNC PARA DESCARGA DE ARCHIVOS CON PROGRESS DIALOG
    public class DownloadTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog mPDialog;
        private Context mContext;
        private PowerManager.WakeLock mWakeLock;
        private File mTargetFile;
        //Constructor parameters :
        // @context (current Activity)
        // @targetFile (File object to write,it will be overwritten if exist)
        // @dialogMessage (message of the ProgresDialog)
        public DownloadTask(Context context,File targetFile,String dialogMessage) {
            this.mContext = context;
            this.mTargetFile = targetFile;
            mPDialog = new ProgressDialog(context);

            mPDialog.setMessage(dialogMessage);
            mPDialog.setIndeterminate(true);
            mPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mPDialog.setCancelable(true);
            // reference to instance to use inside listener
            final DownloadTask me = this;
            mPDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    me.cancel(true);
                    Snackbar.make(mContentView, getResources().getString(R.string.cancel_downloding_img), Snackbar.LENGTH_LONG).show();
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/SimplyPaper/"); //Creates app specific folder

                    File imageFile = new File(path.getAbsolutePath(), nombreIMG+"."+formatoIMG); // Imagename.png
                    File file= new File(String.valueOf(imageFile));
                    if(file.exists())
                    {
                        file.delete();
                        path.delete();
                    }

                }
            });
            Log.i("DownloadTask","Constructor done");
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }
                Log.i("DownloadTask","Response " + connection.getResponseCode());

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(mTargetFile,false);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        Log.i("DownloadTask","Cancelled");
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();

            mPDialog.show();


        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            //super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mPDialog.setIndeterminate(false);
            mPDialog.setMax(100);
            mPDialog.setProgress(progress[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("DownloadTask", "Work Done! PostExecute");
            mWakeLock.release();
            mPDialog.dismiss();
            File dd =  new File(rootDir, nombreIMG+"."+formatoIMG);//Este es el archivo recien descargado que será escaneado
            if (result != null)
                Snackbar.make(mContentView, getResources().getString(R.string.error_download), Snackbar.LENGTH_SHORT).show();

            else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                sendBroadcast(new Intent(new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(dd)))
                );
            }else{
                sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse(dd.toString()
                                + Environment.getExternalStorageDirectory())));
            }


            Snackbar.make(mContentView, getResources().getString(R.string.image_save), Snackbar.LENGTH_SHORT).show();

        }
        @Override
        protected void onCancelled() {

        }
    }
}
