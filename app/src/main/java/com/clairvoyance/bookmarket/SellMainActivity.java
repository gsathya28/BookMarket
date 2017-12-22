package com.clairvoyance.bookmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SellMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_main);
    }

    private void setButtons(){
        Button addPostButton = findViewById(R.id.sell_add_post);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPostIntent = new Intent(getApplicationContext(), SellAddPost.class);
                startActivity(addPostIntent);
            }
        });

        Button viewPostsButton = findViewById(R.id.sell_view_posts);
        viewPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewPostsIntent = new Intent(getApplicationContext(), SellViewPosts.class);
                startActivity(viewPostsIntent);
            }
        });


    }

}
