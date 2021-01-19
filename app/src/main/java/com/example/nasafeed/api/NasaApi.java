package com.example.nasafeed.api;

import com.example.nasafeed.api.model.DateDTO;
import com.example.nasafeed.api.model.PhotoDTO;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

//спец интерфейс для библиотеки retrofit
//позволяет описывать сетевые запросы как функцию
public interface NasaApi {
    @GET("natural/all")
    Single<List<DateDTO>> getDatesWithPhoto();

    @GET("natural/date/{date}")
    Single<List<PhotoDTO>> getPhotosForDate(@Path("date") String date);
}
