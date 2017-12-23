package com.clairvoyance.bookmarket;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class SellAddPost extends AppCompatActivity {

    ArrayList<Book> postBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_add_post);

        postBooks = new ArrayList<>();
    }

    private void setButtons(){

        Button addBook = findViewById(R.id.sell_add_book_button);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private AlertDialog newBookDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setView(R.id.add_book_dialog_view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String courseType = ((EditText) findViewById(R.id.sell_course_type_text)).toString();
                String courseNumber = ((EditText) findViewById(R.id.sell_course_number_text)).toString();
                Book newBook = new Book(courseType, courseNumber);

                String bookTitle = ((EditText) findViewById(R.id.sell_book_title_text)).toString();
                String author = ((EditText) findViewById(R.id.sell_author_text)).toString();
                String price = ((EditText) findViewById(R.id.sell_price_text)).toString();
                String vnum = ((EditText) findViewById(R.id.sell_vnum_text)).toString();
                String instructor = ((EditText) findViewById(R.id.sell_instructor_text)).toString();
                String notes = ((EditText) findViewById(R.id.sell_book_notes_text)).toString();

                newBook.set(Book.NAME, bookTitle);
                newBook.set(Book.AUTHOR, author);
                newBook.set(Book.PRICE, price);
                newBook.set(Book.VERSION_NUMBER, vnum);
                newBook.set(Book.NOTES, notes);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }



}
