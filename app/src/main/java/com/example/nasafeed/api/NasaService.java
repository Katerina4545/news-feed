package com.example.nasafeed.api;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NasaService {
    //спец ключ для обращения к серверу, позволяет совершать до 1000 запросов/сутки
    public static String KEY = "cfpxftjqccQbNxFWLSglQafdDGsFvE7sVMlGAfxo";

    NasaApi api;

    public NasaService() {
        Retrofit retrofit = createRetrofit();
        api = retrofit.create(NasaApi.class);
    }

    public NasaApi getApi() {
        return api;
    }

    //инициализация http клиента
    private OkHttpClient createOkHttpClient() {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //Interceptor - выстраивает цепочку обработки запроса
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                final Request original = chain.request();
                final HttpUrl originalHttpUrl = original.url();
                //ко всем запросам добавляем параметр api_key - ключ доступа к серверу
                final HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", KEY)
                        .build();
                final Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                final Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        //логгирование обращения к серверы, история запросов
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        return httpClient.build();
    }

    //инициализация rest клиента
    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/EPIC/api/")
                .addConverterFactory(GsonConverterFactory.create()) //конвертор JSON в java объекты
                .client(createOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //спец адаптер для работы в запросами
                .build();
    }
}
