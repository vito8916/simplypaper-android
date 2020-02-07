package com.dev.victor.spaper;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.Toast;

import com.dev.victor.spaper.Fragments.FragmentoContenedor;
import com.dev.victor.spaper.Fragments.FragmentoCopyright;
import com.dev.victor.spaper.Fragments.Fragmento_descargas;
import com.dev.victor.spaper.preferences.PrefActivity;
import com.dev.victor.spaper.gcm.QuickstartPreferences;
import com.dev.victor.spaper.gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import es.dmoral.prefs.Prefs;


public class MainActivity extends AppCompatActivity {
    private static Context context;

    private DrawerLayout drawerLayout;
    FloatingActionButton fab;
    private View content;
    private static long back_pressed;

    //GCM CONFIG VARIABLES
    public static String Correo;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Prefs.with(getApplicationContext()).readInt("themesetup"));
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        content = findViewById(R.id.contenedor_principal);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        SharedPreferences getPrefs2 = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        boolean isFirstStart2 = getPrefs2.getBoolean("firstStart", true);




        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, Intro.class);
                    startActivity(i);
                    finish();
                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });
        // Start the thread
        t.start();


       // agregarToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        configNavigationBar();

        if(isFirstStart2){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                solicitarPermisos();
            }
        }


        // CONFIGURACION DE GOOGLE CLOUD MESSAGE
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.d("SE Registró: ",getString(R.string.gcm_send_message));
                } else {
                    Log.d("NO se Regitro: ",getString(R.string.token_error_message));
                }
            }
        };

        //txtRegid = (TextView)findViewById(R.id.textView);
        Correo = getAccount();


        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }



    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    private void solicitarPermisos() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, getString(R.string.permisoConcedido), Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, getString(R.string.permisoDenegado) + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(getString(R.string.permisoMsjRechazo))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        /*Manifest.permission.ACCOUNT_MANAGER,
                        Manifest.permission.GET_ACCOUNTS,*/
                        Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)
                .check();

    }

    private void configNavigationBar() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //configurarColorIconos();
        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            seleccionarItem(navigationView.getMenu().getItem(0));
        }
    }

  /*  private void configurarColorIconos() {
        Drawable iconsetting, iconHome, iconInfo, iconDownload, iconCopyr;
        iconsetting = ContextCompat.getDrawable(this, R.drawable.zzz_settings);
        iconHome = ContextCompat.getDrawable(this, R.drawable.zzz_home);
        iconInfo = ContextCompat.getDrawable(this, R.drawable.zzz_information);
        iconCopyr = ContextCompat.getDrawable(this, R.drawable.zzz_copyright);
        iconDownload = ContextCompat.getDrawable(this, R.drawable.zzz_folder_download);

        iconsetting.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        iconHome.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        iconInfo.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        iconCopyr.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        iconDownload.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }*/


    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

    }

    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        //TabLayout tav2 = (TabLayout) findViewById(R.id.tabLayout);
        switch (itemDrawer.getItemId()) {
            case R.id.nav_home:
                fragmentoGenerico = new FragmentoContenedor();

               // if(tav2.getVisibility() == View.GONE){
               //     tav2.setVisibility(View.VISIBLE);
                //}
                break;
            case R.id.nav_descargas:
                fragmentoGenerico = new Fragmento_descargas();
               AppBarLayout appBar = (AppBarLayout) findViewById(R.id.appbar);

                //appBar.removeView(tav);
                //if (tav2.getVisibility() == View.VISIBLE){
                 //  tav2.setVisibility(View.GONE);
                //}

                break;
            case R.id.nav_acercade:
                // Fragmento para la sección Categorías
                Intent intent = new Intent(getAppContext(), AcercadeActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_ayuda:
                // Iniciar actividad de configuración
                Intent ayuda = new Intent(getAppContext(),PrefActivity.class);
                startActivity(ayuda);
                break;
            case R.id.nav_legal:
                //actividad legal
                fragmentoGenerico = new FragmentoCopyright();
               // if (tav2.getVisibility() == View.VISIBLE){
                //    tav2.setVisibility(View.GONE);
                //}
                break;
        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor_principal, fragmentoGenerico)
                    .commit();
        }

        // Setear título actual
        setTitle(itemDrawer.getTitle());
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings){

            String playStoreLink = "https://play.google.com/store/apps/details?id=" +
                    getPackageName();

            String msg = getString(R.string.sharespaper) + playStoreLink;
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent,getString(R.string.share)));
        }
        if(id == R.id.action_gplus){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/communities/108197617256280498502")));
        }

        if(id == R.id.webpage){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://simplypaper.vicxbox.com")));

        }

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout!=null&&drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else if(back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
        }else {
            Snackbar.make(content,getString(R.string.pressback),Snackbar.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    public static Context getAppContext()
    {
        return MainActivity.context;
    }

    public static boolean networkstate(){
        ConnectivityManager cn=
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo nf=cn.getActiveNetworkInfo();
        if(nf != null && nf.isConnected()==true )
        {
            return true;

        }
        else
        {
            return false;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }



    //Metodo para obtener el correo principal del usuario.
//    private String getAccount(AccountManager accountManager) {
    private String getAccount() {

        /*Account[] accounts = accountManager.getAccounts();
        Account account;*/
        //String emailId;
        String emailComplete;
        emailComplete = "simplypaperwallpaper@gmail.com";
        /*if (accounts.length > 0) {
            account = accounts[0];
            emailComplete = account.name;

        } else {
           //// emailId = "ejemplo@gmail.com";
            emailComplete = "simplypaperwallpaper@gmail.com";
            //Toast.makeText(context,emailComplete,Toast.LENGTH_LONG).show();
        }*/
        return emailComplete;
    }

    public int cambiartema(){
        int tema = 0;

        PreferenceManager.setDefaultValues(this, R.xml.ajustes, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int syncConnPref = sharedPref.getInt("color_key",0);




        if(syncConnPref == ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)){
            tema = R.style.AppTheme;
            Prefs.with(getApplicationContext()).writeInt("accentColorByTheme", R.color.colorAccent);
            Prefs.with(getApplicationContext()).writeInt("themesetup",R.style.AppTheme);
        }
        else if(syncConnPref == ContextCompat.getColor(getApplicationContext(),R.color.primaryColor2)){
            tema = R.style.AppTheme2;
            Prefs.with(getApplicationContext()).writeInt("accentColorByTheme", R.color.primaryColorAccent2);
            Prefs.with(getApplicationContext()).writeInt("themesetup",R.style.AppTheme2);

        }else if(syncConnPref == ContextCompat.getColor(getApplicationContext(),R.color.primaryColor3) ){
            tema = R.style.AppTheme3;
            Prefs.with(getApplicationContext()).writeInt("accentColorByTheme", R.color.primaryColorAccent3);
            Prefs.with(getApplicationContext()).writeInt("themesetup",R.style.AppTheme3);
        }else if(syncConnPref == ContextCompat.getColor(getApplicationContext(),R.color.primaryColor4) ){
            tema = R.style.AppTheme4;
            Prefs.with(getApplicationContext()).writeInt("accentColorByTheme", R.color.primaryColorAccent4);
            Prefs.with(getApplicationContext()).writeInt("themesetup",R.style.AppTheme4);
        }


        return tema;
    }

}
