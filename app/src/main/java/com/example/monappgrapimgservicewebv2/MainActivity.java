package com.example.monappgrapimgservicewebv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickNext (View Main2Activity) {
        Intent monIntent = new Intent(this, Main2Activity.class);
        startActivity(monIntent);
    }
}
