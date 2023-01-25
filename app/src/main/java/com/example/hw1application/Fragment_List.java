package com.example.hw1application;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;


public class Fragment_List extends Fragment {

    private MaterialButton[] list_BTN_users;
    private CallBack_UserProtocol callBack_userProtocol;
    private CallBack_viewReadyProtocol callBack_viewReadyProtocol;
    private TopTen topTen;

    public void setCallBack_viewReadyProtocol(CallBack_viewReadyProtocol callBack_viewReadyProtocol) {
        this.callBack_viewReadyProtocol = callBack_viewReadyProtocol;
    }

    public void setCallBack_userProtocol(CallBack_UserProtocol callBack_userProtocol){
        this.callBack_userProtocol = callBack_userProtocol;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);


        findViews(view);
        initViews();
        topTen = new TopTen();
        callBack_viewReadyProtocol.continueWork();

        return view;
    }

    public TopTen getTopTen(){ return topTen;}

    private void updateListView() {
        for (int i = 0; i < topTen.getTopScores().size(); i++)
            list_BTN_users[i].setText(topTen.getTopScores().get(i).toString());
    }

    public Score handleScore( int score) {
        if(topTen == null){
            topTen = new TopTen();

        }
        read();

        if(score ==0) {
            if(!topTen.isEmpty())
                updateListView();

            return null;
        }

        Score newScore = null;

        if (topTen.isEmpty()){
            topTen.addScore(new Score().
                    setTheScore(score));
            newScore = topTen.getTopScores().get(0);
        }

        else if (topTen.isFull()){
            if(topTen.getTopScores().get(9).getTheScore() < score){
                topTen.getTopScores().remove(9);
                topTen.addScore(new Score().
                        setTheScore(score));
                newScore = topTen.getTopScores().get(9);
                topTen.sortScores();
            }

        }else{
            topTen.addScore(new Score().
                    setTheScore(score));

            newScore = topTen.getTopScores().get(topTen.getTopScores().size()-1) ;
            topTen.sortScores();

        }
        updateListView();
        write();

        return newScore;
    }


    private void read(){
       String myJsonString= MySPV.getIntance().getString("List", null);
       topTen = new Gson().fromJson(myJsonString, TopTen.class);
       if(topTen == null)
           topTen = new TopTen();

    }

    private void write(){
        MySPV.getIntance().putString("List", new Gson().toJson(topTen));
    }

    private void initViews() {
        for (int i=0; i<list_BTN_users.length; i++){
            int finalI = i;
            list_BTN_users[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(callBack_userProtocol != null)
                        callBack_userProtocol.user(topTen.getTopScores().get(finalI));
                }
            });
        }
    }

    private void findViews(@NonNull View view) {

        list_BTN_users = new MaterialButton[]{
                view.findViewById(R.id.list_BTN_one),
                view.findViewById(R.id.list_BTN_two),
                view.findViewById(R.id.list_BTN_three),
                view.findViewById(R.id.list_BTN_four),
                view.findViewById(R.id.list_BTN_five),
                view.findViewById(R.id.list_BTN_six),
                view.findViewById(R.id.list_BTN_seven),
                view.findViewById(R.id.list_BTN_eight),
                view.findViewById(R.id.list_BTN_nine),
                view.findViewById(R.id.list_BTN_ten)};
    }

}
