package com.example.sumitmaurya.mobiotisktask.home;

import android.app.Activity;

import android.util.Log;
import com.example.sumitmaurya.mobiotisktask.models.Output;
import com.example.sumitmaurya.mobiotisktask.network.NetworkClient;
import com.example.sumitmaurya.mobiotisktask.network.NetworkInterface;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class HomeModel implements HomeContractorInterface{
    @Override
    public void getOutputresponse(OnFinishedListener listener, OnErrorListener errorListener, Activity activity, MainViewInterface mvi) {

        getObservable().subscribeWith(getObserver(listener,errorListener));

    }

    private Observable getObservable() {
        return NetworkClient.getRetrofit().create(NetworkInterface.class)
                .getoutputResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    private DisposableObserver getObserver(final OnFinishedListener listener, final OnErrorListener errorListener) {
        return new DisposableObserver<List<Output>>() {

            @Override
            public void onNext(@NonNull List<Output> response) {

                listener.onFinished(response);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("LOG","Error"+e);
                e.printStackTrace();

                errorListener.onError("Some Error Occured");

            }

            @Override
            public void onComplete() {
                Log.d("LOG","Completed");

            }
        };


    }







}
