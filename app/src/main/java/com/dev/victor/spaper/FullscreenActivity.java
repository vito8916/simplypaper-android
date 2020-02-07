package com.dev.victor.spaper;

import android.Manifest;
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
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
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


public class FullscreenActivity extends AppCompatActivity implements View.OnClickListener{
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

        setContentView(R.layout.activity_fullscreen);
        ImgFulscreen = (TouchImageView)findViewById(R.id.imgFullscreen);
        mContentView = findViewById(R.id.fullscreen_content);



        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;



        final Animation ani4 = AnimationUtils.loadAnimation(this, R.anim.fade_in);



        // includes window decorations (statusbar bar/menu bar)
        // w = getWindowManager();
        // d = w.getDefaultDisplay();
        // d.getMetrics(metrics);
        //widthPixels  = metrics.widthPixels;
        //heightPixels = metrics.heightPixels;
        //DisplayMetrics displaymetrics = new DisplayMetrics();

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

        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width,height);
        ImgFulscreen.setLayoutParams(parms);

        // hideFab();
        hide();


        final Fab fab = (Fab)findViewById(R.id.fab2);
        requestQueue = Volley.newRequestQueue(this);
        colorPalette = new ColorPalette();

        Intent intent = getIntent();
        JSONObject datos = null;
        String jsostring = (String) intent.getExtras().get("detalles");

        try {
            datos = new JSONObject(jsostring);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(datos != null){
            try {
                nombreIMG = datos.getString("title");
                formatoIMG = datos.getString("originalformat");
                urlImagenBig = "https://farm"+datos.getString("farm")+".static.flickr.com/"+datos.getString("server")+"/"+datos.getString("id")+"_"+datos.getString("secret")+"_b."+datos.getString("originalformat")+"";
                urlImagenSmall = "https://farm"+datos.getString("farm")+".static.flickr.com/"+datos.getString("server")+"/"+datos.getString("id")+"_"+datos.getString("secret")+"_s."+datos.getString("originalformat")+"";
                urlImgenOriginal = "https://farm"+datos.getString("farm")+".staticflickr.com/"+datos.getString("server")+"/"+datos.getString("id")+"_"+datos.getString("originalsecret")+"_o."+datos.getString("originalformat")+"";
                urlImagenHight = "https://farm"+datos.getString("farm")+".static.flickr.com/"+datos.getString("server")+"/"+datos.getString("id")+"_"+datos.getString("secret")+"_h."+datos.getString("originalformat")+"";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Verificacion de HEADSIZE de JAVA
            Log.d("URLIMAGEN GRANDE B: ", urlImagenBig);
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            int memoryClass = am.getMemoryClass();
            Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
            if(memoryClass <=32){                                                      //Si la menoria es menor igual que 32mb
                urlIMGdescarga = urlImagenBig;                                       //Se establece el url de descarga como imagen grande
                urlIMGImgViewFull = urlImagenBig;                                       // se establece el url para el imageview que muestra la imagen como imagen grande
                urlIMGwallpaper = urlImagenBig;
                bconfig = Bitmap.Config.RGB_565;
            }else{                                                             // si la memoria es mayor que 32 MB la calidad de imagen cambia
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
                            if(bitmap.getWidth()<bitmap.getHeight()){

                               ImgFulscreen.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }

                            //EXTRAER LOS COLORES DEL BITMAP PARA ASIGNARLO AL FAB
                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette p) {
                                    int Color = 0;

                                    View sheetView = findViewById(R.id.fab_sheet);
                                    //final ImageView overlay = (ImageView) findViewById(R.id.overlay);
                                    View overlay = findViewById(R.id.overlay);
                                    int sheetColor = getResources().getColor(R.color.background_card);

                                    Palette.Swatch vibrant = p.getVibrantSwatch();
                                    Palette.Swatch lightvibrant = p.getLightVibrantSwatch();
                                    Palette.Swatch darkvibrant = p.getDarkVibrantSwatch();
                                    Palette.Swatch muted = p.getMutedSwatch();

                                    // Use generated instance
                                    View fila_fab_expand = findViewById(R.id.fila_fab_expand);
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

                                    materialSheetFab = new MaterialSheetFab<>(fab,sheetView,overlay,sheetColor,Color);
                                    setupFab();

                                }
                            });

                            findViewById(R.id.progress_bar_fullscreen).setVisibility(View.GONE);
                            ShowFab();
                        }
                    }, 0, 0, null, bconfig, //Bitmap.Config.RGB_565 reduce la calidad del bitmap/2
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            imageError(error);
                        }
                    });


            VolleySingleton.getInstance().getRequestQueue().add(request);
           /*Transformation transformation = new Transformation() {

                @Override
                public Bitmap transform(Bitmap source) {



                    int targetHeight = source.getHeight()/2;
                    int targetWidth = source.getWidth()/2;
                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                }

                @Override
                public String key() {
                    return "transformation" + " desiredWidth";
                }
            };*/

        }


    }

    //METODO PARA MOSTRAR EL FAB DESPUES QUE EL RESPONSE SE EFECTUO CORRECTAMENTE
    private void ShowFab(){
        final Animation ani3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_in);

        Fab fab = (Fab)findViewById(R.id.fab2);
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
                Blurry.with(FullscreenActivity.this)
                        .radius(10)
                        .sampling(8)
                        .animate(300)
                        .async()
                        .onto((ViewGroup) findViewById(R.id.fullscreen_content));

                // overlay.setAlpha(1f);
                super.onShowSheet();
            }

            @Override
            public void onHideSheet() {
                Blurry.delete((ViewGroup) findViewById(R.id.fullscreen_content));
                super.onHideSheet();
            }
        });

        //SET ONCLICK LISTENER PARA LOS ELEMENTOS DEL FLOATING BUTTON
        findViewById(R.id.fab_sheet_item_wallpaper).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_establecer).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_download).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_back).setOnClickListener(this);
    }

   //METODO DE ERROR DE CONEXION A INTERNET
    private void imageError(Exception e) {
        Snackbar.make(findViewById(R.id.fullscreen_content),getString(R.string.interneterror),Snackbar.LENGTH_SHORT);
        //Toast.makeText(getApplicationContext(),getString(R.string.interneterror), Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    //METODO PARA OCULTAR EL ACTION BAR O FULLSCREEN
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


            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
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
            case R.id.fab_sheet_item_wallpaper:
                wallpaper();
                showProgressAsWallpaper();
                materialSheetFab.hideSheet();
                break;
            case R.id.fab_sheet_item_download:
                downloadIMG();
               // downloadimg();
                materialSheetFab.hideSheet();
                //showProgress();
                break;
            case R.id.fab_sheet_item_establecer:
                shareBitmap();
                materialSheetFab.hideSheet();
                break;
            case R.id.fab_sheet_item_back:
                materialSheetFab.hideSheet();
                break;
        }
    }

   /* private void showProgress() {
        mProgress = ProgressDialog.show(this, "", getResources().getString(R.string.downloading));
    }*/
    /*private void stopProgress() {
         		mProgress.cancel();
    }*/


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
                                wpManager.setBitmap(bitmapResized);
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
                            Snackbar.make(findViewById(R.id.fullscreen_content),getResources().getString(R.string.msg_setaswallpaper),Snackbar.LENGTH_SHORT).show();

                        } else {
                            Snackbar.make(findViewById(R.id.fullscreen_content), getResources().getString(R.string.msg_err_setaswallpaper), Snackbar.LENGTH_SHORT).show();
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
        ImageRequest imageRequest = new ImageRequest(urlImagenHight,
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



    //METODO 2 PARA DESCARGAR IMAGEN
    public void downloadIMG(){


        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){

        }
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(FullscreenActivity.this, getString(R.string.permisoConcedido), Toast.LENGTH_SHORT).show();

                File dwnFolder;
                if(!rootDir.exists()){
                    rootDir.mkdir();
                }
                dwnFolder = new File(rootDir, nombreIMG+"."+formatoIMG);
                final DownloadTask downloadTask = new DownloadTask(FullscreenActivity.this,dwnFolder,getResources().getString(R.string.downloading));
                downloadTask.execute(urlIMGdescarga);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(FullscreenActivity.this, getString(R.string.permisoDenegado) + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getString(R.string.permisosGuardarMsj))
                .setDeniedMessage(getString(R.string.permisoMsjRechazo))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WAKE_LOCK,Manifest.permission.READ_EXTERNAL_STORAGE)
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
                    Snackbar.make(findViewById(R.id.fullscreen_content), getResources().getString(R.string.cancel_downloding_img), Snackbar.LENGTH_LONG).show();
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
                Snackbar.make(findViewById(R.id.fullscreen_content), getResources().getString(R.string.error_download), Snackbar.LENGTH_SHORT).show();

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


                Snackbar.make(findViewById(R.id.fullscreen_content), getResources().getString(R.string.image_save), Snackbar.LENGTH_SHORT).show();

        }
        @Override
        protected void onCancelled() {

        }
    }


}
