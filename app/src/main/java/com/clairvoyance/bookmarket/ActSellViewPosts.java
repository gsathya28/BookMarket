package com.clairvoyance.bookmarket;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActSellViewPosts extends AppCompatActivity {

    User mainUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_view_posts);

        mainUser = LocalDataHandler.parseMainUserData(getApplicationContext());

        setLayout();
    }

    private void setLayout(){
        LinearLayout mainLayout = findViewById(R.id.sell_my_post_layout);

        TextView titleText = new TextView(getApplicationContext());
        titleText.setText("My Posts");

        mainLayout.addView(titleText);

        ArrayList<SellPost> sellPosts = mainUser.getMySellPosts();

        for (SellPost post : sellPosts){
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
            StringBuilder builder = new StringBuilder();
            builder.append("Books:");
            for (Book book : books){

                String courseSubj = book.get(Book.COURSE_SUBJECT);
                String courseNum = book.get(Book.COURSE_NUMBER);
                String bookName = book.get(Book.TITLE);

                builder.append(System.getProperty("line.separator"));
                builder.append(courseSubj);
                builder.append(" ");
                builder.append(courseNum);
                builder.append(" - ");
                builder.append(bookName);
            }


            builder.append(System.getProperty("line.separator"));
            builder.append(System.getProperty("line.separator"));
            builder.append("Posted: ");
            builder.append(DateFormat.getTimeInstance(DateFormat.SHORT).format(post.getPostDate().getTime()));
            builder.append(" - ");
            builder.append(DateFormat.getDateInstance().format(post.getPostDate().getTime()));

            postButton.setText(builder.toString());
            postButton.setTextColor(Color.parseColor("#FFFFFF"));
            mainLayout.addView(postButton);
            postButton.setGravity(Gravity.START);
            postButton.setPadding(leftValueInPx, postButton.getPaddingTop(), postButton.getPaddingRight(), postButton.getPaddingBottom());
        }
    }
}
