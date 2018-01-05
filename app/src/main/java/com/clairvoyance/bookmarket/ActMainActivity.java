package com.clairvoyance.bookmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User mainUser = WebServiceHandler.generateMainUser();
        if (mainUser == null){
            Intent intent = new Intent(this, ActLoginActivity.class);
            startActivity(intent);
        }

        setButtons();

    }

    private void setButtons(){
        Button sellMainButton = findViewById(R.id.main_sell);
        sellMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sellMainActivity = new Intent(getApplicationContext(), ActSellMainActivity.class);
                startActivity(sellMainActivity);
            }
        });
    }
}
