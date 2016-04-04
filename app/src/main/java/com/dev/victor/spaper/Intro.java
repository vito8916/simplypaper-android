package com.dev.victor.spaper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;


/**
 * Created by Victor on 29/12/2015.
 */
public class Intro extends AppIntro {
    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        addSlide(AppIntroFragment.newInstance(getString(R.string.intro1), getString(R.string.intro1des), (R.drawable.nuevo_icono_spaper2), Color.parseColor("#3F51B5")));
        addSlide(AppIntroFragment.newInstance(getString(R.string.intro2), getString(R.string.intro2des), (R.drawable.intro2), Color.parseColor("#37474F")));
        addSlide(AppIntroFragment.newInstance(getString(R.string.intro3), getString(R.string.intro3des), (R.drawable.intro6), Color.parseColor("#EF5350")));
        addSlide(AppIntroFragment.newInstance(getString(R.string.intro4), getString(R.string.intro4des), (R.drawable.intro4), Color.parseColor("#FFD54F")));
        addSlide(AppIntroFragment.newInstance(getString(R.string.intro5), getString(R.string.intro5des), (R.drawable.introfinal), Color.parseColor("#37474F")));
        setFadeAnimation();
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void getStarted(View v) {
        loadMainActivity();
    }



}
