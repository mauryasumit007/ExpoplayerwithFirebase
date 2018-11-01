package com.example.sumitmaurya.mobiotisktask.exoplay;

import android.app.Activity;

import com.example.sumitmaurya.mobiotisktask.models.Output;
import com.example.sumitmaurya.mobiotisktask.others.ResumeData;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public interface ExoplayContractorInterface {

    // Network request interface

    interface OnFinishedListener {
        void onFinished(List<ResumeData> resumelist, List<String> firebaselist);
    }

    // Error listener interface

    interface OnNewdataListener {
        void onNewdata(String error);
    }

    interface onExoplayersetup {
        void onPlayerset(SimpleExoPlayer exoPlayer);
    }


    // Main Action interface

    interface ExoPresenterInterface {

        void loadFirebasedata(Activity activity, DatabaseReference mFirebaseDatabase, FirebaseDatabase mFirebaseInstance);
        void initExoplayer(Activity activity, SimpleExoPlayerView exoPlayerView,String videoUrl, List<ResumeData> resumelist);

    }


    // VIew interface

    interface ExoViewInterface {

        void displayExpolistData(List<ResumeData> resumelist,List<String> firebaselist);
        void displaynewdataMsg(String s);
        void displayexoplayer(SimpleExoPlayer exoPlayer);
    }

    // Method to get response and send to presenter

    void getOutputresponse(OnFinishedListener listener, OnNewdataListener errorListener, Activity activity, ExoViewInterface mvi, DatabaseReference mFirebaseDatabase, FirebaseDatabase mFirebaseInstance);

    void getExoplayer( Activity activity, SimpleExoPlayerView exoPlayerView,onExoplayersetup exosetuplistener,String VideoUrl, List<ResumeData> resumelist);


}
