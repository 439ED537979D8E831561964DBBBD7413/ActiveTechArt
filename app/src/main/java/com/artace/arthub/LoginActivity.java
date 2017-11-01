package com.artace.arthub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView btnRegister;
    Button btnRegisterSeniman,btnRegisterEO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewActions();

    }

    private void viewActions(){
        btnRegister = (TextView) findViewById(R.id.login_btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.register_choose);

                btnRegisterSeniman = (Button) findViewById(R.id.register_choose_seniman);
                btnRegisterSeniman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LoginActivity.this,RegisterSenimanActivity.class);
                        startActivity(intent);
                    }
                });

                btnRegisterEO = (Button) findViewById(R.id.register_choose_eo);
            }
        });
        //TODO : ONCLICK REGISTER IO
    }
}
