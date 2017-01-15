package com.dzartek.wineoisseur.apicall;

import com.dzartek.wineoisseur.pojodessert.DessertWine;
import com.dzartek.wineoisseur.pojomodel.SnoothWine;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dzarrillo on 10/11/2016.
 */

public interface WineApi {
    String BASE_URL = "http://api.snooth.com/";

    @GET("wines/")
    Call<SnoothWine> getWines(@Query("akey") String api_key,
                              @Query("q") String varietal,
                              @Query("n") String results,
                              @Query("a") String all_wines,
                              @Query("c") String region,
                              @Query("s") String sortby,
                              @Query("mp") String min_price,
                              @Query("xp") String max_price,
                              @Query("mr") String min_rank,
                              @Query("xr") String max_rank);


    @GET("wines/")
    Call<DessertWine> getDesserts(@Query("akey") String api_key,
                              @Query("t") String dessert,
                              @Query("mr") String min_rank,
                              @Query("xr") String max_rank,
                              @Query("mp") String min_price,
                              @Query("xp") String max_price,
                              @Query("s") String sortby,
                              @Query("n") String results);


    class Factory {
        private static WineApi service;

        public static WineApi getInstance() {
            if (service == null) {

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(WineApi.class);
                return service;
            } else {
                return service;
            }
        }
    }
}
