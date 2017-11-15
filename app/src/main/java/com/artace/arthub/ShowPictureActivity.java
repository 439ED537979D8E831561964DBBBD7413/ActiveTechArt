package com.artace.arthub;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.toolbox.NetworkImageView;
import com.artace.arthub.connection.DatabaseConnection;
import com.artace.arthub.controller.AppController;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ShowPictureActivity extends AppCompatActivity {

    NetworkImageView imageView;
    PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);

//        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) findViewById(R.id.show_picture_ImageView);
//
        imageView = (NetworkImageView) findViewById(R.id.show_picture_ImageView);

        Bundle dataExtra = getIntent().getExtras();
        String imageViewExtra = dataExtra.getString("imageViewExtra");

        imageView.setImageUrl(imageViewExtra, AppController.getInstance().getImageLoader());
        mAttacher = new PhotoViewAttacher(imageView);

//        imageView.setImage(ImageSource.uri(imageViewExtra));

    }
}
