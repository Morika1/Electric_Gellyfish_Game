package com.example.hw1application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class OpenPage_Activity extends AppCompatActivity {

    private MaterialButton open_BTN_sensormode;
    private MaterialButton open_BTN_buttonmode;
    private MaterialButton open_LBL_records;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_page);

        findViews();
        initViews();

    }


    private void findViews(){
        open_BTN_sensormode = findViewById(R.id.open_BTN_sensormode);
        open_BTN_buttonmode = findViewById(R.id.open_BTN_buttonmode);
        open_LBL_records = findViewById(R.id.open_LBL_records);
    }


    private void openNewPage(boolean isSensorModeOn){

            Intent intent = new Intent(this, Game_Activity.class);
            intent.putExtra("GameMode", isSensorModeOn);
            startActivity(intent);
            finish();
    }

    private void openTopTenPage(){

        Intent intent = new Intent(this, TopTen_Activity.class);
        intent.putExtra("score", 0);
        startActivity(intent);
        finish();
    }

    private void initViews(){

        open_BTN_sensormode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start game with sensors mode
                openNewPage(true);
            }
        });

        open_BTN_buttonmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start game with buttons mode
                openNewPage(false);
            }
        });

        open_LBL_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTopTenPage();
            }
        });



    }
}