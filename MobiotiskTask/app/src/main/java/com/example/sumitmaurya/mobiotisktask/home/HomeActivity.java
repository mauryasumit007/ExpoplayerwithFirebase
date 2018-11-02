package com.example.sumitmaurya.mobiotisktask.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sumitmaurya.mobiotisktask.R;
import com.example.sumitmaurya.mobiotisktask.adapters.HomeAdapter;
import com.example.sumitmaurya.mobiotisktask.exoplay.ExoplayActivity;
import com.example.sumitmaurya.mobiotisktask.models.Output;
import com.example.sumitmaurya.mobiotisktask.others.ItemClickListener;
import com.example.sumitmaurya.mobiotisktask.others.MobiosticApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeContractorInterface.MainViewInterface,ItemClickListener{

    @BindView(R.id.recyclerViewVideos)
    RecyclerView recyclerViewVideos;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.label)
    TextView label;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String TAG = "HomeActivity";
    HomePresenter mainPresenter;

    HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        setupMVP();
        setupViews();


    }

    private void setupMVP() {
        mainPresenter = new HomePresenter(this,new HomeModel());
        mainPresenter.onButtonClick(HomeActivity.this);


    }
    private void setupViews() {
        setSupportActionBar(toolbar);
        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void showToast(String s) {
        Toast.makeText(HomeActivity.this,s,Toast.LENGTH_LONG).show();

    }

    @Override
    public void showProgressBar() {
        recyclerViewVideos.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        label.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgressBar() {
        label.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerViewVideos.setVisibility(View.VISIBLE);

    }

    @Override
    public void displayVideodata(List<Output> response) {
        if(response!=null ) {

            hideProgressBar();

            Log.d("size",String.valueOf(response.size()));

            MobiosticApp.getInstance().setSingletonResponse(response);
            homeAdapter = new HomeAdapter(response, HomeActivity.this);
            recyclerViewVideos.setAdapter(homeAdapter);
            homeAdapter.setClickListener(this);

        }else{
            showProgressBar();
            Log.d(TAG,"No response");
            Toast.makeText(HomeActivity.this,"This content not available",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void displayError(String s) {

    }

    @Override
    public void onClick(View view, int position) {

        Intent movetoExoplay=new Intent(HomeActivity.this, ExoplayActivity.class);
        movetoExoplay.putExtra("VideoUrl",MobiosticApp.getInstance().getSingletonResponse().get(position).getUrl());
        movetoExoplay.putExtra("VideoTitle",MobiosticApp.getInstance().getSingletonResponse().get(position).getTitle());

        startActivity(movetoExoplay);

    }
}

