package com.dev.victor.spaper;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dev.victor.spaper.Fragments.FragmentoContenedor;
import com.dev.victor.spaper.Fragments.FragmentoCopyright;
import com.dev.victor.spaper.Fragments.Fragmento_descargas;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static Context context;

    private DrawerLayout drawerLayout;
    FloatingActionButton fab;
    private View content;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WAKE_LOCK,Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }

    private void configNavigationBar() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            seleccionarItem(navigationView.getMenu().getItem(0));
        }
    }

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
                Intent ayuda = new Intent(getAppContext(),AyudaSugerencia.class);
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
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://simplypaper.ml")));

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
}