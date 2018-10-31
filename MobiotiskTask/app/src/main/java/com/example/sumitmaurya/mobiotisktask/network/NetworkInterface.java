package com.example.sumitmaurya.mobiotisktask.network;


import com.example.sumitmaurya.mobiotisktask.models.Output;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface NetworkInterface {


    @GET("media.json?print=pretty")
    Observable<List<Output>> getoutputResult();


}
