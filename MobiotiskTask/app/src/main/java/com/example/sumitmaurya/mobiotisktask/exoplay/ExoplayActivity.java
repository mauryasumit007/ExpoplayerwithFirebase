package com.example.sumitmaurya.mobiotisktask.exoplay;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.example.sumitmaurya.mobiotisktask.R;
import com.example.sumitmaurya.mobiotisktask.adapters.ExpoAdapter;
import com.example.sumitmaurya.mobiotisktask.adapters.HomeAdapter;
import com.example.sumitmaurya.mobiotisktask.home.HomeActivity;
import com.example.sumitmaurya.mobiotisktask.models.Output;
import com.example.sumitmaurya.mobiotisktask.others.ItemClickListener;
import com.example.sumitmaurya.mobiotisktask.others.MobiosticApp;
import com.example.sumitmaurya.mobiotisktask.others.ResumeData;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExoplayActivity extends AppCompatActivity implements ItemClickListener {

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView exoPlayerView;

    @BindView(R.id.exolist)
    RecyclerView exolist;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    boolean time = false;
    SimpleExoPlayer exoPlayer;
    String videoURL = "";
    String videoTitle="";
    String oldvideourl = "";
    ExpoAdapter homeAdapter;
    List<Output> playlist = new ArrayList<>();
    List<Output> showlist = new ArrayList<>();
    List<String> firebaselist = new ArrayList<>();
    List<ResumeData> resumeList = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplay);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backy);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exoPlayer.stop();
                Storepausetime();
                finish();
            }
        });

        mFirebaseInstance = FirebaseDatabase.getInstance();
        StoreinFirebase();
        videoURL = getIntent().getStringExtra("VideoUrl");
        videoTitle=getIntent().getStringExtra("VideoTitle");



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                setToolbarTitle(videoTitle);
                setup();
            }
        }, 1000);

        setupList("");


    }

    private void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }


    private void StoreinFirebase() {

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

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    Log.d("INFO", "No");

                    mFirebaseDatabase = mFirebaseInstance.getReference("videos");
                    mFirebaseInstance.getReference("app_title").setValue("Realtime Store");

                    for (int i = 0; i < MobiosticApp.getInstance().getSingletonResponse().size(); i++) {
                        ResumeData detail = new ResumeData(MobiosticApp.getInstance().getSingletonResponse().get(i).getUrl(), "0");
                        mFirebaseDatabase.child(mFirebaseDatabase.push().getKey()).setValue(detail);
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }






    private void setupList(String s) {

        if (s.equals("")) {
            showlist.clear();
            playlist.clear();
            exolist.setLayoutManager(new LinearLayoutManager(this));
            showlist.addAll(MobiosticApp.getInstance().getSingletonResponse());
            for (int i = 0; i < showlist.size(); i++) {
                if (showlist.get(i).getUrl().equals(videoURL)) {
                    playlist.add(showlist.get(i));
                    showlist.remove(i);
                    break; } } }

               else {
            exolist.setLayoutManager(new LinearLayoutManager(this));
            if (!oldvideourl.equals("")) {
                showlist.add(showlist.get(0));
                showlist.remove(0);
            }
            if (!time) {
                for (int i = 0; i < showlist.size(); i++) {
                    if (!showlist.get(i).getUrl().equals(playlist.get(0).getUrl())) {
                        showlist.addAll(playlist);
                        break;
                    }
                }
                time = true;
            }
        }
        homeAdapter = new ExpoAdapter(showlist, ExoplayActivity.this);
        exolist.setAdapter(homeAdapter);
        homeAdapter.setClickListener(this);

    }




    private void setup() {

        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            Uri videoURI = Uri.parse(videoURL);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
            exoPlayerView.setPlayer(exoPlayer);
            if (resumeList != null) {
                for (int i = 0; i < resumeList.size(); i++) {
                    if (videoURL.equalsIgnoreCase(String.valueOf(resumeList.get(i).getVideoUrl()))) {
                        Log.d("pattern", "match");
                        exoPlayer.seekTo(Long.parseLong(resumeList.get(i).getDuration()));
                        break;
                    } } }
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
            exoplayerListener();
        } catch (Exception e) {
            Log.e("ExoplayActivity", " exoplayer error " + e.toString());
        }

    }



    @Override
    public void onResume() {
        super.onResume();
   //     StoreinFirebase();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Storepausetime();
    }

    @Override
    public void onClick(View view, int position) {
        exoPlayer.stop();
        Storepausetime();
        videoURL = showlist.get(position).getUrl();
        setToolbarTitle(showlist.get(position).getTitle());
        setup();
        setupList("");
    }


    private void exoplayerListener() {

        exoPlayer.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
            }
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }
            @Override
            public void onLoadingChanged(boolean isLoading) {
            }
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == exoPlayer.STATE_ENDED) {
                    oldvideourl = videoURL;
                    videoURL = showlist.get(0).getUrl();
                    setToolbarTitle(showlist.get(0).getTitle());
                    setup();
                    setupList("old");
                } else if (playWhenReady && playbackState == exoPlayer.STATE_READY) {
                    Log.d("INFO", "1");
                } else if (playWhenReady) {
                    Log.d("INFO", "2");
                } else {
                    Log.d("INFO", "paused");
                    Storepausetime();
                }
            }
            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }
            @Override
            public void onPositionDiscontinuity() {

            }
            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }
        });

    }





    private void Storepausetime() {

        Log.d("DURATION", String.valueOf(exoPlayer.getCurrentPosition()));
        for (int i = 0; i < MobiosticApp.getInstance().getSingletonResponse().size(); i++) {
            if (MobiosticApp.getInstance().getSingletonResponse().get(i).getUrl().matches(videoURL)) {
                mFirebaseDatabase = mFirebaseInstance.getReference("videos");
                StoreinFirebase();
                ResumeData detail = new ResumeData(videoURL, String.valueOf(exoPlayer.getCurrentPosition()));
                try {
                    mFirebaseDatabase.child(firebaselist.get(i)).setValue(detail);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                break;

            } } }
      }
