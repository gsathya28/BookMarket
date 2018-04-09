package com.clairvoyance.bookmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActMainActivity extends AppCompatActivity {

    User mainUser;

    // Public Book Data
    ArrayList<Book> mSellBooks = new ArrayList<>();

    ArrayList<Book> mBuyBooks = new ArrayList<>();

    ValueEventListener mSellBookData = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot d: dataSnapshot.getChildren()){
                Book book = d.getValue(Book.class);
                if (book != null){
                    mSellBooks.add(book);
                    mBuyBooks.add(book);
                }
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mainUser = WebServiceHandler.generateMainUser();
        }catch (IllegalAccessException i) {
            Intent intent = new Intent(this, ActLoginActivity.class);
            startActivity(intent);
        }

        // Load Data
        loadPublicData();
        loadPrivateData();


        if(mSellBooks.size() == 0){
            Log.d("Book Data", "No Books Loaded!");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void loadPublicData(){

    }

    private void loadPrivateData(){
        try {
            Query query = WebServiceHandler.getRootRef().child("books").orderByChild("uid").equalTo(WebServiceHandler.getUID());
            query.addListenerForSingleValueEvent(mSellBookData);
        }
        catch (IllegalAccessException i){
            illegalAccess();
        }
    }

    private void illegalAccess(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
