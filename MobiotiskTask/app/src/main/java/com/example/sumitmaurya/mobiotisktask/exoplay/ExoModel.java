package com.example.sumitmaurya.mobiotisktask.exoplay;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.example.sumitmaurya.mobiotisktask.others.MobiosticApp;
import com.example.sumitmaurya.mobiotisktask.others.ResumeData;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExoModel implements ExoplayContractorInterface  {

    List<ResumeData> resumeList = new ArrayList<>();
    List<String> firebaselist = new ArrayList<>();


    @Override
    public void getOutputresponse(final OnFinishedListener listener, final OnNewdataListener newdataListener, Activity activity, ExoViewInterface mvi, final  DatabaseReference mFirebaseDatabase, final FirebaseDatabase mFirebaseInstance) {

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("videos")) {
                    DatabaseReference childref = rootRef.child("videos");
                    childref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            resumeList.clear();
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                firebaselist.add(dsp.getKey()); //add result into array list
                                ResumeData data = new ResumeData((String) dsp.child("videoUrl").getValue(), (String) dsp.child("duration").getValue());
                                resumeList.add(data);

                            }
                            listener.onFinished(resumeList,firebaselist);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    Log.d("INFO", "No");
                    mFirebaseInstance.getReference("app_title").setValue("Realtime Store");
                    for (int i = 0; i < MobiosticApp.getInstance().getSingletonResponse().size(); i++) {
                        ResumeData detail = new ResumeData(MobiosticApp.getInstance().getSingletonResponse().get(i).getUrl(), "0");
                        mFirebaseDatabase.child(mFirebaseDatabase.push().getKey()).setValue(detail);
                    }

                    newdataListener.onNewdata("Data is stored first time in firebase");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Override
    public void getExoplayer(Activity activity, SimpleExoPlayerView exoPlayerView, onExoplayersetup exosetuplistener,String VideoUrl, List<ResumeData> resumelist) {

        SimpleExoPlayer exoPlayer;

        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(activity, trackSelector);
            Uri videoURI = Uri.parse(VideoUrl);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
            exoPlayerView.setPlayer(exoPlayer);
            if (resumeList != null) {
                for (int i = 0; i < resumeList.size(); i++) {
                    if (VideoUrl.equalsIgnoreCase(String.valueOf(resumeList.get(i).getVideoUrl()))) {
                        Log.d("pattern", "match");
                        exoPlayer.seekTo(Long.parseLong(resumeList.get(i).getDuration()));
                        break;
                    } } }
            exoPlayer.prepare(mediaSource);

             exosetuplistener.onPlayerset(exoPlayer);

        } catch (Exception e) {
            Log.e("ExoplayActivity", " exoplayer error " + e.toString());
        }

    }



}
