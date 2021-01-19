package com.example.nasafeed;

import android.app.Application;

import com.example.nasafeed.api.NasaService;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

//инициализация всего необходимого перед запуском программы
public class App extends Application {
    private NasaService nasaService;

    @Override
    public void onCreate() {
        super.onCreate();

        //иниц сетевого сервиса к которому будем делать запросы к серверу
        nasaService = new NasaService();

        //иниц загрузчика картинок
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true) //говорим, что нужен кэш в памяти
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)

                .memoryCache(new LruMemoryCache(20 * 1024 * 1024))
                .memoryCacheSize(20 * 1024 * 1024)
                .build();

        //вызываем функцию иниц, после этого загрузчик картинок готов к работе
        ImageLoader.getInstance().init(config);
    }

    public NasaService getNasaService() {
        return nasaService;
    }
}
