package com.example.hw1application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import com.bumptech.glide.Glide;

public class Game_Activity extends AppCompatActivity {

    private ShapeableImageView game_IMG_background;
    private ShapeableImageView[] game_IMG_hearts;
    private ShapeableImageView[][] game_IMG_jellyfishes;
    private ShapeableImageView[] game_IMG_spongbobs;
    private ExtendedFloatingActionButton game_BTN_left;
    private ExtendedFloatingActionButton game_BTN_right;

    final int DELAY=1000;
    final Handler handler = new Handler();
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(runnable, DELAY);
            updateMovementUI();
            refreshUI();
        }
    };

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    Game_Manager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        findViews();

        Glide
                .with(Game_Activity.this)
                .load("https://i.etsystatic.com/30910046/r/il/1a4018/3199202546/il_794xN.3199202546_l0qv.jpg")
                .into(game_IMG_background);

        initViews();
        gameManager = new Game_Manager(
                game_IMG_hearts.length,
                game_IMG_jellyfishes.length,
                game_IMG_spongbobs.length);

        initGame();
        System.err.println("num of roads: " + game_IMG_spongbobs.length);
        startTimer();
    }


    private void updateMovementUI() {
        for(int i=0; i<gameManager.getNumOfRoads(); i++){
            if(gameManager.isInBoundary(gameManager.getJellyfishesIndex()[i]))
                game_IMG_jellyfishes[gameManager.getJellyfishesIndex()[i]][i].setVisibility(View.INVISIBLE); // turn off prev position
        }

        gameManager.moveJellyFishes();

        for(int i=0; i<gameManager.getNumOfRoads(); i++)
        {
            if(gameManager.isInBoundary(gameManager.getJellyfishesIndex()[i]))
                game_IMG_jellyfishes[gameManager.getJellyfishesIndex()[i]][i].setVisibility(View.VISIBLE); // turn on next position

        }

    }

    private void initGame(){
        for(int i=0; i< game_IMG_spongbobs.length;i++){
           if(i!= gameManager.getSpongbobIndex())
                game_IMG_spongbobs[i].setVisibility(View.INVISIBLE);
        }



        for(int i=0; i< game_IMG_jellyfishes.length;i++){
            for(int j=0; j< game_IMG_jellyfishes[0].length; j++)
                game_IMG_jellyfishes[i][j].setVisibility(View.INVISIBLE);
        }
    }

    private void initViews() {

        game_BTN_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked(false);
            }
        });

        game_BTN_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked(true);
            }
        });
    }

    private void clicked(boolean direction) {
        game_IMG_spongbobs[gameManager.getSpongbobIndex()].setVisibility(View.INVISIBLE);
        gameManager.moveSpongbob(direction);
        game_IMG_spongbobs[gameManager.getSpongbobIndex()].setVisibility(View.VISIBLE);

        refreshUI();
    }

    private void refreshUI() {

        if(gameManager.isCrash()){
            game_IMG_hearts[gameManager.getLife()].setVisibility(View.INVISIBLE);

                vibrate();
                if(gameManager.getLife()!=0)
                    Toast
                            .makeText(this, "Damnnnn", Toast.LENGTH_SHORT)
                            .show();

        }

        if(gameManager.isGameOver()){
            Toast
                    .makeText(this, "Game Over Man", Toast.LENGTH_SHORT)
                    .show();
            stopTimer();
            //TODO delay
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    finish();
                }
            }, DELAY+2000);

        }
    }

    private void startTimer(){
        handler.postDelayed(runnable,DELAY);
    }

    private void stopTimer(){
        handler.removeCallbacks(runnable);
    }

    private void findViews() {

        game_IMG_background = findViewById(R.id.game_IMG_background);
        game_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)};

        game_IMG_jellyfishes = new ShapeableImageView[][]{
                {findViewById(R.id.game_IMG_jellyfish1),
                        findViewById(R.id.game_IMG_jellyfish2),
                        findViewById(R.id.game_IMG_jellyfish3)},
                {findViewById(R.id.game_IMG_jellyfish4),
                        findViewById(R.id.game_IMG_jellyfish5),
                        findViewById(R.id.game_IMG_jellyfish6)},
                {findViewById(R.id.game_IMG_jellyfish7),
                        findViewById(R.id.game_IMG_jellyfish8),
                        findViewById(R.id.game_IMG_jellyfish9)}
        };

        game_IMG_spongbobs = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_spongbob1),
                findViewById(R.id.game_IMG_spongbob2),
                findViewById(R.id.game_IMG_spongbob3)
        };

        game_BTN_left = findViewById(R.id.game_BTN_left);
        game_BTN_right = findViewById(R.id.game_BTM_right);

    }

}