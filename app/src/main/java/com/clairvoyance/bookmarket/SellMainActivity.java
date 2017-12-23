package com.clairvoyance.bookmarket;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SellMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_main);
        setOptionButtons();
        setLayout();
    }

    private void setOptionButtons(){
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

    private void setLayout(){
        LinearLayout mainLayout = findViewById(R.id.buyer_post_layout);

        TextView titleText = new TextView(getApplicationContext());
        titleText.setText(R.string.sell_main_layout_title);

        ArrayList<BuyPost> buyPosts = WebServiceHandler.getBuyPosts();

        for (BuyPost post : buyPosts){
            Button postButton = new Button(getApplicationContext());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            int topValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_vertical_margin);
            int bottomValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_vertical_margin);
            bottomValueInPx = bottomValueInPx / 2;
            int leftValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_horizontal_margin);
            /*
            if (i != 0)
            {
                topValueInPx = topValueInPx / 2;
            }
            */
            params.setMargins(leftValueInPx, topValueInPx, leftValueInPx, bottomValueInPx);
            postButton.setLayoutParams(params);

            postButton.setBackgroundColor(Color.parseColor("#267326"));
            // Set text using the post and books
            ArrayList<Book> books = post.getBooks();
            for (Book book : books){
                String courseSubj = book.get(Book.COURSE_SUBJECT);
                String courseNum = book.get(Book.COURSE_NUMBER);
                String bookName = book.get(Book.NAME);

                postButton.append(courseSubj);
                postButton.append(" ");
                postButton.append(courseNum);
                postButton.append(" - ");
                postButton.append(bookName);
                postButton.append(System.getProperty("line.separator"));
            }

            postButton.setTextColor(Color.parseColor("#FFFFFF"));
            mainLayout.addView(postButton);
        }



    }

}
