package com.clairvoyance.bookmarket;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BookListFragment extends Fragment {

    RecyclerView recyclerView;
    private ArrayList<Book> books = new ArrayList<>();

    Query query;
    private ValueEventListener mBookData = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            books.clear();
            for(DataSnapshot d: dataSnapshot.getChildren()){
                Book book = d.getValue(Book.class);
                if (book != null){
                    books.add(book);
                }
            }
            recyclerView.setAdapter(new BookRecyclerAdapter(books, mType, mListener, mainUser));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private ValueEventListener allBookData = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            books.clear();
            for(DataSnapshot d: dataSnapshot.getChildren()){
                Book book = d.getValue(Book.class);
                if (book != null){
                    books.add(book);
                }
            }
            // Get rid of own books - may move this somewhere else...
            Iterator iterator = books.iterator();
            while (iterator.hasNext()){
                Book book = (Book) iterator.next();
                if(book.getUid().equals(FirebaseHandler.getUID())){
                    iterator.remove();
                }
            }

            Collections.reverse(books);
            recyclerView.setAdapter(new BookRecyclerAdapter(books, mType, mListener, mainUser));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private OnListFragmentInteractionListener mListener;

    private static final String ARG_TYPE = "type";
    private String mType;

    private static final String ARG_USER = "user";
    private User mainUser;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BookListFragment newInstance(String type, User user) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
            mainUser = (User) getArguments().getSerializable(ARG_USER);
            if(mainUser != null)
                Log.d("UserData:", Integer.toString(mainUser.getMyRequestIDs().size()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Layout selection
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        // Color Scheme
        if(mType.equals(Book.ALL_BOOK_SELL) || mType.equals(Book.MY_BOOK_SELL)) {
            view.setBackgroundColor(Color.parseColor("#c2efc2"));
        }
        else if(mType.equals(Book.ALL_BOOK_BUY) || mType.equals(Book.MY_BOOK_BUY)){
            view.setBackgroundColor(Color.parseColor("#cce0ff"));
        }
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            loadPrivateData();
        }
        return view;
    }

    private void loadPrivateData(){
        if(mType.equals(Book.MY_BOOK_SELL)) {
            query = FirebaseHandler.mySellBooks;
            query.addListenerForSingleValueEvent(mBookData);
        }else if(mType.equals(Book.ALL_BOOK_SELL)){
            query = FirebaseHandler.allSellBooks;
            query.addListenerForSingleValueEvent(allBookData);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Detach any continuous listeners??
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Book item);
    }
}
