package com.clairvoyance.bookmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtons();
    }

    private void setButtons(){
        Button sellMainButton = findViewById(R.id.main_sell);
        sellMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sellMainActivity = new Intent(getApplicationContext(), SellMainActivity.class);
                startActivity(sellMainActivity);

            }
        });


    }
}
