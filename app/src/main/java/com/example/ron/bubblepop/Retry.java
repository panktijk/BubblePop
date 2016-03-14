package com.example.ron.bubblepop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Retry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_retry);
        Bundle bundle = getIntent().getExtras();

//Extract the data…
        int finalScore = bundle.getInt("finalScore");
        TextView currentscore = (TextView)findViewById(R.id.scoretextview);
        currentscore.setText("Score: "+finalScore);
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        int topscore = prefs.getInt("key", 0); //0 is the default value
        if(topscore<finalScore)
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("key", finalScore);
            editor.commit();
            topscore = prefs.getInt("key",0); //0 is the default value
        }
        TextView highscore = (TextView)findViewById(R.id.highscoretextview);
        highscore.setText("High Score: "+topscore);
    }
    public void onClickRetry(View v)
    {
        View gameView = new GameView(this);
        setContentView(gameView);
    }

    public void onClickHome(View v)
    {
        finishAffinity();
        Intent nextScreen = new Intent(Retry.this, MainActivity.class);
        startActivity(nextScreen);
    }

    @Override
    public void onBackPressed()
    {

    }
}
