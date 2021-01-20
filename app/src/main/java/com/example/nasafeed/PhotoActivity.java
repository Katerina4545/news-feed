package com.example.nasafeed;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class PhotoActivity extends Fragment {
    private static final String EXTRA_URL = "PhotoActivity.EXTRA_URL";

    private SubsamplingScaleImageView imageView;
    private Toolbar toolbar;

    private Bitmap photo;

    public PhotoActivity(Context caller, String url){
        super(R.layout.activity_photo);
        Intent intent = new Intent(caller, PhotoActivity.class);
        intent.putExtra(EXTRA_URL, url);
        caller.startActivity(intent);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.image);

        ImageLoader.getInstance().loadImage(EXTRA_URL, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    photo = loadedImage;
                    imageView.setImage(ImageSource.cachedBitmap(loadedImage));

            }
        });
    }
}
