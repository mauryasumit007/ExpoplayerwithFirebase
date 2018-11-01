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


public class ExoPresenter implements ExoplayContractorInterface.ExoPresenterInterface,ExoplayContractorInterface.OnFinishedListener,ExoplayContractorInterface.OnNewdataListener,ExoplayContractorInterface.onExoplayersetup {

    ExoplayContractorInterface.ExoViewInterface viewInterface;
    private ExoplayContractorInterface exoplayContractorInterface;

    public ExoPresenter(ExoplayContractorInterface.ExoViewInterface viewInterface, ExoplayContractorInterface exoplayContractorInterface) {
        this.viewInterface = viewInterface;
        this.exoplayContractorInterface = exoplayContractorInterface;
    }



    @Override
    public void loadFirebasedata(Activity activity, DatabaseReference mFirebaseDatabase, FirebaseDatabase mFirebaseInstance) {

        mFirebaseDatabase = mFirebaseInstance.getReference("videos");


        exoplayContractorInterface.getOutputresponse(this,this,activity, viewInterface,mFirebaseDatabase,mFirebaseInstance);

    }

    @Override
    public void initExoplayer(Activity activity, SimpleExoPlayerView exoPlayerView,String Videourl, List<ResumeData> resumeList) {

        exoplayContractorInterface.getExoplayer(activity,exoPlayerView,this,Videourl,resumeList);

    }

    @Override
    public void onFinished(List<ResumeData> resumelist, List<String> firebaselist) {

      viewInterface.displayExpolistData(resumelist,firebaselist);

    }

    @Override
    public void onNewdata(String msg) {

       viewInterface.displaynewdataMsg(msg);

    }

    @Override
    public void onPlayerset(SimpleExoPlayer exoPlayer) {
viewInterface.displayexoplayer(exoPlayer);
    }
}
