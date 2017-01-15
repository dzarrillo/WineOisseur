package com.dzartek.wineoisseur.apicall;



import com.dzartek.wineoisseur.pojolocation.LiquorStore;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dzarrillo on 11/1/2016.
 */

public interface WineStoreApi {
    String BASE_URL = "https://maps.googleapis.com/maps/api/place/";

    @GET("nearbysearch/json")
    Call<LiquorStore> getWineStores(@Query("location") String location,
                                    @Query("radius") String radius,
                                    @Query("type") String type,
                                    @Query("key") String key);


    class Factory {
        private static WineStoreApi service;

        public static WineStoreApi getInstance() {
            if (service == null) {

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(WineStoreApi.class);
                return service;
            } else {
                return service;
            }
        }
    }
}
