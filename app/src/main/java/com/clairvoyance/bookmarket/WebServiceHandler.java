package com.clairvoyance.bookmarket;

import android.support.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Sathya on 12/21/2017.
 * Amazon Web Service Handler
 */

class WebServiceHandler {


    final static int RC_SIGN_IN = 2899;
    final static String WEB_CLIENT_ID = "483082602147-bmhfbbj3k1proa5r2ll3hr694d9s5mrr.apps.googleusercontent.com";
    private static FirebaseAuth mAuth;
    private static FirebaseUser mUser;
    private static User loadedUser;
    final static ArrayList<Post> publicPosts = new ArrayList<>();

    // Value Event Listeners

    static DatabaseReference mPosts = FirebaseDatabase.getInstance().getReference().child("posts");
    static DatabaseReference mBooks = FirebaseDatabase.getInstance().getReference().child("books");
    static Query mPublicPosts = FirebaseDatabase.getInstance().getReference().child("posts").limitToFirst(10);

    private static boolean isMainUserAuthenticated(){
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        return mUser != null;
    }

    @Nullable
    static User generateMainUser(){
        if (isMainUserAuthenticated()){
            // KeyLine
            User user = new User(mUser.getUid(), mUser.getEmail());

            user.setEmailVerified(mUser.isEmailVerified());
            user = loadMainUserData(user);
            return user;
        }
        else {
            return null; // Re-authentication intent set up in onCreate
        }
    }

    private static User loadMainUserData(final User user){

        if (loadedUser == null) {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = rootRef.child("users").child(mUser.getUid());

            // Read Data
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        // Create new user - with values
                        createNewUserData(user);
                        loadedUser = user;
                    }
                    else {
                        loadedUser = dataSnapshot.getValue(User.class);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return loadedUser;
    }

    private static void createNewUserData(User user){
        if (isMainUserAuthenticated()){
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
            userRef.child(mUser.getUid()).setValue(user);
        }
        else {
            throw new IllegalStateException("User not authorized");
        }
    }

    static void updateMainUserData(User user){
        if (isMainUserAuthenticated()){
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
            userRef.child(user.getUid()).setValue(user);
        }
        else {
            throw new IllegalStateException("User not authorized");
        }
    }

    static ArrayList<Post> getPosts(ArrayList<String> postIDs){
        final ArrayList<Post> posts = new ArrayList<>();
        DatabaseReference postListRef = FirebaseDatabase.getInstance().getReference().child("posts");
        for (String id: postIDs){
            DatabaseReference postRef = postListRef.child(id);
            postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        posts.add(dataSnapshot.getValue(Post.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return posts;
    }

    static ArrayList<Post> getPublicPosts(){
        loadPublicPosts();
        return publicPosts;
    }

    static void loadPublicPosts(){

        Query postListRef = FirebaseDatabase.getInstance().getReference().child("posts").limitToFirst(10);
        postListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Post post = d.getValue(Post.class);
                    post.setPostDate(post.getPostDateInSecs());
                    publicPosts.add(post);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    static void addPublicPost(Post post){
        if (isMainUserAuthenticated()) { // Add function to only allow certain people to post
            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("posts");
            postRef.child(post.getPostID()).setValue(post);
        }
    }

    static void addPublicBook(Book book){
        if (isMainUserAuthenticated()) { // Add function to only allow certain people to post
            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("books");
            postRef.child(book.getBookID()).setValue(book);
        }
    }

}
