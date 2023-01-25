package com.example.hw1application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

public class Game_Activity extends AppCompatActivity {

    private ShapeableImageView game_IMG_background;
    private ShapeableImageView[] game_IMG_hearts;
    private ShapeableImageView[][] game_IMG_jellyfishes;
    private ShapeableImageView[] game_IMG_spongbobs;
    private ShapeableImageView[][] game_IC_coins;
    private MaterialTextView game_LBL_score;
    private ExtendedFloatingActionButton game_BTN_left;
    private ExtendedFloatingActionButton game_BTN_right;

    

    private StepDetector stepDetector;

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

        Intent previousIntent = getIntent();
        boolean isSensorMode = previousIntent.getExtras().getBoolean("GameMode");

        if(!isSensorMode) // if game was chosen with buttons mode
            initViews();

        else { // sensor mode is on
            game_BTN_right.setVisibility(View.INVISIBLE);
            game_BTN_left.setVisibility(View.INVISIBLE);

            // use sensors
            stepDetector = new StepDetector(this, callBack_steps);
        }

        gameManager = new Game_Manager(
                game_IMG_hearts.length,
                game_IMG_jellyfishes.length,
                game_IMG_spongbobs.length);

        initGame();
        startTimer();
    }

    private StepDetector.CallBack_steps callBack_steps = new StepDetector.CallBack_steps() {
        @Override
        public void oneStep(boolean direction) {
            clicked(direction);
        }
    };

    @Override
    protected void onPause(){
        super.onPause();
        stopTimer();
        if(stepDetector != null)
            stepDetector.stop();
    }

    @Override
    protected void onResume(){
        super.onResume();
        startTimer();
        if(stepDetector != null)
            stepDetector.start();
    }

    private void updateMovementUI() {

        movementHelper(gameManager.getJellyfishesIndex(), game_IMG_jellyfishes, View.INVISIBLE);
        movementHelper(gameManager.getCoinsIndex(), game_IC_coins, View.INVISIBLE);

        gameManager.moveJellyFishes();
        gameManager.moveCoins();

        movementHelper(gameManager.getJellyfishesIndex(), game_IMG_jellyfishes, View.VISIBLE);
        movementHelper(gameManager.getCoinsIndex(), game_IC_coins, View.VISIBLE);

    }

    private void movementHelper(int[] indexArray, ShapeableImageView[][] fallingObjectMatrix, int visibility){
        for(int i=0; i<gameManager.getNumOfRoads(); i++){
            if(gameManager.isInBoundary(indexArray[i]))
                fallingObjectMatrix[indexArray[i]][i].setVisibility(visibility); // turn off prev position
        }


    }

    private void initGame(){
        for(int i=0; i< game_IMG_spongbobs.length;i++){
           if(i!= gameManager.getSpongbobIndex())
                game_IMG_spongbobs[i].setVisibility(View.INVISIBLE);
        }

        initMatrix(game_IMG_jellyfishes);
        initMatrix(game_IC_coins);

    }

    private void initMatrix(ShapeableImageView[][] fallingObjectMatrix){

        for(int i=0; i< gameManager.getDistance();i++){
            for(int j=0; j< gameManager.getNumOfRoads(); j++)
                fallingObjectMatrix[i][j].setVisibility(View.INVISIBLE);
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
            MySignal.getInstance().vibrate();
            MySignal.getInstance().electricicate();
            if(gameManager.getLife()!=0)
                MySignal.getInstance().toast("Damnnnn");

        }

        if (gameManager.isCatch()){
            game_LBL_score.setText(gameManager.getScore() + "");
            MySignal.getInstance().toast("Nice catch man");
        }

        if(gameManager.isGameOver()){
            MySignal.getInstance().toast("Game Over Man!");
           // MySignal.getInstance().toast("Your score is: "+ gameManager.getScore());

            stopTimer();
            //delay to wait for all toats to finish
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    openScoreActivity();
                }
            }, DELAY+3000);
        }
    }
    private void openScoreActivity(){
        Intent intent = new Intent(this, TopTen_Activity.class);
        intent.putExtra("score", gameManager.getScore());
        startActivity(intent);
        finish();

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
                        findViewById(R.id.game_IMG_jellyfish3),
                        findViewById(R.id.game_IMG_jellyfish10),
                        findViewById(R.id.game_IMG_jellyfish11)},
                {findViewById(R.id.game_IMG_jellyfish4),
                        findViewById(R.id.game_IMG_jellyfish5),
                        findViewById(R.id.game_IMG_jellyfish6),
                        findViewById(R.id.game_IMG_jellyfish12),
                        findViewById(R.id.game_IMG_jellyfish13)},
                {findViewById(R.id.game_IMG_jellyfish7),
                        findViewById(R.id.game_IMG_jellyfish8),
                        findViewById(R.id.game_IMG_jellyfish9),
                        findViewById(R.id.game_IMG_jellyfish14),
                        findViewById(R.id.game_IMG_jellyfish15)},
                {findViewById(R.id.game_IMG_jellyfish16),
                        findViewById(R.id.game_IMG_jellyfish17),
                        findViewById(R.id.game_IMG_jellyfish18),
                        findViewById(R.id.game_IMG_jellyfish19),
                        findViewById(R.id.game_IMG_jellyfish20)},
                {findViewById(R.id.game_IMG_jellyfish21),
                        findViewById(R.id.game_IMG_jellyfish22),
                        findViewById(R.id.game_IMG_jellyfish23),
                        findViewById(R.id.game_IMG_jellyfish24),
                        findViewById(R.id.game_IMG_jellyfish25)}
        };

        game_IMG_spongbobs = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_spongbob1),
                findViewById(R.id.game_IMG_spongbob2),
                findViewById(R.id.game_IMG_spongbob3),
                findViewById(R.id.game_IMG_spongbob4),
                findViewById(R.id.game_IMG_spongbob5)
        };

        game_IC_coins = new ShapeableImageView[][]{
                {findViewById(R.id.game_IC_coin1),
                        findViewById(R.id.game_IC_coin2),
                        findViewById(R.id.game_IC_coin3),
                        findViewById(R.id.game_IC_coin10),
                        findViewById(R.id.game_IC_coin11)},
                {findViewById(R.id.game_IC_coin4),
                        findViewById(R.id.game_IC_coin5),
                        findViewById(R.id.game_IC_coin6),
                        findViewById(R.id.game_IC_coin12),
                        findViewById(R.id.game_IC_coin13)},
                {findViewById(R.id.game_IC_coin7),
                        findViewById(R.id.game_IC_coin8),
                        findViewById(R.id.game_IC_coin9),
                        findViewById(R.id.game_IC_coin14),
                        findViewById(R.id.game_IC_coin15)},
                {findViewById(R.id.game_IC_coin16),
                        findViewById(R.id.game_IC_coin17),
                        findViewById(R.id.game_IC_coin18),
                        findViewById(R.id.game_IC_coin19),
                        findViewById(R.id.game_IC_coin20)},
                {findViewById(R.id.game_IC_coin21),
                        findViewById(R.id.game_IC_coin22),
                        findViewById(R.id.game_IC_coin23),
                        findViewById(R.id.game_IC_coin24),
                        findViewById(R.id.game_IC_coin25)}
        };

        game_LBL_score = findViewById(R.id.game_LBL_score);
        game_BTN_left = findViewById(R.id.game_BTN_left);
        game_BTN_right = findViewById(R.id.game_BTM_right);

    }
}