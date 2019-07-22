package com.dev.victor.spaper.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dev.victor.spaper.MainActivity;
import com.dev.victor.spaper.R;

import es.dmoral.prefs.Prefs;

/**
 * Created by vic_a on 1/6/2016.
 */
public class PrefActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_PREF_SYNC_CONN = "color_key";
    SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    //listener on changed sort order preference:
    SharedPreferences prefs;




    @Override protected void onCreate(Bundle savedInstanceState) {
        setTheme(cambiartema());
        super.onCreate(savedInstanceState);
        MainActivity obj = new MainActivity();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new SettingsFragment())
                    .commit();
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {


                if (key.equals(getString(R.string.key_palette_pref))) {

                    TaskStackBuilder.create(getApplicationContext())
                            .addNextIntent(new Intent(getApplicationContext(), MainActivity.class))
                            .addNextIntent(getIntent())
                            .startActivities();
                    Toast.makeText(getApplicationContext(),getString(R.string.cambio_de_tema),Toast.LENGTH_LONG).show();
                    recreate();
                }

                if (key.equals(getString(R.string.key_nunofcolumn_pref))) {
                    TaskStackBuilder.create(getApplicationContext())
                            .addNextIntent(new Intent(getApplicationContext(), MainActivity.class))
                            .addNextIntent(getIntent())
                            .startActivities();

                }

            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefListener);


    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        ContentFrameLayout root = (ContentFrameLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.setting_layout, root, false);
        root.addView(bar, 0); // insert at top
        bar.setTitle(R.string.titulo1_pref);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_switch_activarGcm))) {


            Toast.makeText(getApplicationContext(),"Cambio tema",Toast.LENGTH_LONG).show();
            this.recreate();
        }

        }


    public static class SettingsFragment extends PreferenceFragment {
        private SwitchPreference switchPreference;

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.ajustes);
            //switchPreference = (SwitchPreference) findPreference(getString(R.string.key_switch_activarGcm));

        }

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // Allow super to try and create a view first
        final View result = super.onCreateView(name, context, attrs);
        if (result != null) {
            return result;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // If we're running pre-L, we need to 'inject' our tint aware Views in place of the
            // standard framework versions
            switch (name) {
                case "EditText":
                    return new AppCompatEditText(this, attrs);
                case "Spinner":
                    return new AppCompatSpinner(this, attrs);
                case "CheckBox":
                    return new AppCompatCheckBox(this, attrs);
                case "RadioButton":
                    return new AppCompatRadioButton(this, attrs);
                case "CheckedTextView":
                    return new AppCompatCheckedTextView(this, attrs);
            }
        }

        return null;
    }

    public int cambiartema(){
        int tema = 0;

        PreferenceManager.setDefaultValues(this, R.xml.ajustes, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int syncConnPref = sharedPref.getInt(getString(R.string.key_palette_pref),0);




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
        }else if(syncConnPref == ContextCompat.getColor(getApplicationContext(),R.color.primaryColor5) ) {
            tema = R.style.AppTheme5;
            Prefs.with(getApplicationContext()).writeInt("accentColorByTheme", R.color.primaryColorAccent5);
            Prefs.with(getApplicationContext()).writeInt("themesetup", R.style.AppTheme5);
        }else if(syncConnPref == ContextCompat.getColor(getApplicationContext(),R.color.primaryColor6) ) {
            tema = R.style.AppTheme6;
            Prefs.with(getApplicationContext()).writeInt("accentColorByTheme", R.color.primaryColorAccent6);
            Prefs.with(getApplicationContext()).writeInt("themesetup", R.style.AppTheme6);
        }

        return tema;
    }

}
