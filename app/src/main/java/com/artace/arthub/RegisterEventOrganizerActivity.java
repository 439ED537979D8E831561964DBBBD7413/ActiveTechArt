package com.artace.arthub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.toolbox.ImageLoader;
import com.artace.arthub.ViewPlugins.CircularNetworkImageView;
import com.artace.arthub.controller.AppController;

public class RegisterEventOrganizerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_event_organizer);

//        CircularNetworkImageView image = (CircularNetworkImageView) findViewById(R.id.item_event_list_imageEvent);
//        ImageLoader netImageLoader = AppController.getInstance().getImageLoader();
//        image.setImageUrl("https://kenzorack.files.wordpress.com/2013/01/penari-kecak.jpg", netImageLoader);
    }
}
