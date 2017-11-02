package com.artace.arthub;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

public class RegisterSenimanActivity extends AppCompatActivity {

    EditText password;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seniman_list_tawaran);

        //START : TOOLBAR

//        mToolbar = (Toolbar) findViewById(R.id.register_seniman_toolbar);
//        setSupportActionBar(mToolbar);
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setTitle("Register Seniman");

        //END : TOOLBAR

        //password = (EditText) findViewById(R.id.register_seniman_password);
        //password.setTransformationMethod(new PasswordTransformationMethod());

    }
}
