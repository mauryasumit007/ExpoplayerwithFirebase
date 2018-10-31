package com.example.sumitmaurya.mobiotisktask.home;

import android.app.Activity;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.example.sumitmaurya.mobiotisktask.models.Output;

import java.util.List;

public class HomePresenter implements HomeContractorInterface.MainPresenterInterface,HomeContractorInterface.OnFinishedListener,HomeContractorInterface.OnErrorListener {


    HomeContractorInterface.MainViewInterface viewInterface;
    private HomeContractorInterface mainContractorInterface;


    public HomePresenter(HomeContractorInterface.MainViewInterface viewInterface, HomeContractorInterface mainContractorInterface) {
        this.viewInterface = viewInterface;
        this.mainContractorInterface = mainContractorInterface;
    }


    @Override
    public void onFinished(List<Output> wikiresponse) {


        if(wikiresponse!=null)
        {


            viewInterface.displayWikidata(wikiresponse);

        }
    }

    @Override
    public void onError(String error) {
        viewInterface.displayError(error);
    }

    @Override
    public void onButtonClick( Activity activity) {
     mainContractorInterface.getOutputresponse(this,this,activity, viewInterface);
    }
}
