package com.example.sumitmaurya.mobiotisktask.home;

import android.app.Activity;
import android.support.v7.widget.SearchView;

import com.example.sumitmaurya.mobiotisktask.models.Output;

import java.util.List;

public interface HomeContractorInterface {


    // Network request interface

    interface OnFinishedListener {
        void onFinished(List<Output> wikiresponse);
    }

    // Error listener interface

    interface OnErrorListener {
        void onError(String error);
    }

    // Main Action interface

    interface MainPresenterInterface {

        void onButtonClick( Activity activity);
    }


    // VIew interface

    interface MainViewInterface {

        void showToast(String s);
        void showProgressBar();
        void hideProgressBar();
        void displayVideodata(List<Output> response);
        void displayError(String s);
    }

    // Method to get response and send to presenter

    void getOutputresponse(OnFinishedListener listener,OnErrorListener errorListener, Activity activity, HomeContractorInterface.MainViewInterface mvi);

}
