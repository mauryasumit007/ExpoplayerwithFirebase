package com.example.sumitmaurya.mobiotisktask.exoplay;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.sumitmaurya.mobiotisktask.R;
import com.example.sumitmaurya.mobiotisktask.adapters.ExpoAdapter;
import com.example.sumitmaurya.mobiotisktask.models.Output;
import com.example.sumitmaurya.mobiotisktask.others.ItemClickListener;
import com.example.sumitmaurya.mobiotisktask.others.MobiosticApp;
import com.example.sumitmaurya.mobiotisktask.others.ResumeData;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExoplayActivity extends AppCompatActivity implements ItemClickListener, ExoplayContractorInterface.ExoViewInterface {

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
    String videoTitle = "";
    String oldvideourl = "";
    ExpoAdapter homeAdapter;
    List<Output> playList = new ArrayList<>();
    List<Output> showList = new ArrayList<>();
    List<String> firebaseList = new ArrayList<>();
    List<ResumeData> resumeList = new ArrayList<>();
    ExoPresenter exoPresenter;


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
        exoPresenter = new ExoPresenter(this, new ExoModel());
        exoPresenter.loadFirebasedata(ExoplayActivity.this, mFirebaseDatabase, mFirebaseInstance);
        videoURL = getIntent().getStringExtra("VideoUrl");
        videoTitle = getIntent().getStringExtra("VideoTitle");


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setToolbarTitle(videoTitle);
                exoPresenter.initExoplayer(ExoplayActivity.this, exoPlayerView, videoURL, resumeList);
            }
        }, 3000);

        setupList("");


    }

    private void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }


    private void setupList(String s) {
        if (s.equals("")) {
            showList.clear();
            playList.clear();
            exolist.setLayoutManager(new LinearLayoutManager(this));
            showList.addAll(MobiosticApp.getInstance().getSingletonResponse());
            for (int i = 0; i < showList.size(); i++) {
                if (showList.get(i).getUrl().equals(videoURL)) {
                    playList.add(showList.get(i));
                    showList.remove(i);
                    break;
                }
            }
        } else {
            exolist.setLayoutManager(new LinearLayoutManager(this));
            if (!oldvideourl.equals("")) {
                showList.add(showList.get(0));
                showList.remove(0);
            }
            if (!time) {
                for (int i = 0; i < showList.size(); i++) {
                    if (!showList.get(i).getUrl().equals(playList.get(0).getUrl())) {
                        showList.addAll(playList);
                        break;
                    }
                }
                time = true;
            }
        }
        homeAdapter = new ExpoAdapter(showList, ExoplayActivity.this);
        exolist.setAdapter(homeAdapter);
        homeAdapter.setClickListener(this);

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
        videoURL = showList.get(position).getUrl();
        setToolbarTitle(showList.get(position).getTitle());
        exoPresenter.initExoplayer(ExoplayActivity.this, exoPlayerView, videoURL, resumeList);
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
                    videoURL = showList.get(0).getUrl();
                    setToolbarTitle(showList.get(0).getTitle());
                    exoPresenter.initExoplayer(ExoplayActivity.this, exoPlayerView, videoURL, resumeList);
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
        for (int i = 0; i < MobiosticApp.getInstance().getSingletonResponse().size(); i++) {
            if (MobiosticApp.getInstance().getSingletonResponse().get(i).getUrl().matches(videoURL)) {
                mFirebaseDatabase = mFirebaseInstance.getReference("videos");
                exoPresenter.loadFirebasedata(ExoplayActivity.this, mFirebaseDatabase, mFirebaseInstance);
                ResumeData detail = new ResumeData(videoURL, String.valueOf(exoPlayer.getCurrentPosition()));
                try {
                    mFirebaseDatabase.child(firebaseList.get(i)).setValue(detail);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                break;

            }
        }
    }

    @Override
    public void displayExpolistData(List<ResumeData> resumelist, List<String> firebaselistt) {
        resumeList.clear();
        resumeList.addAll(resumelist);
        firebaseList.addAll(firebaselistt);

    }

    @Override
    public void displaynewdataMsg(String s) {

        Log.d("Info", s);

    }

    @Override
    public void displayexoplayer(SimpleExoPlayer exoPlayerr) {
        exoPlayer = exoPlayerr;
        try {
            exoPlayer.setPlayWhenReady(true);
            exoplayerListener();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
