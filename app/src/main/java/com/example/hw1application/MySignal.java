package com.example.hw1application;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

public class MySignal {

    private static MySignal mySignal= null;
    private Context context;

    private MySignal(Context context){
        this.context = context;

    }

    public static void init(Context context){
        if(mySignal == null)
            mySignal = new MySignal(context);
    }


    public static MySignal getInstance(){
        return mySignal;
    }

    public void toast(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void vibrate(){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }

    }

    public void electricicate(){
        ElectricitySound electricitySound = new ElectricitySound();
        electricitySound.execute();
    }



    public class ElectricitySound extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            MediaPlayer player = MediaPlayer.create(context, R.raw.msc_electricity);
            //   player.setLooping(true); // Set looping
            player.setVolume(1.0f, 1.0f);
            player.start();

            return null;
        }

    }


}
