package com.clairvoyance.bookmarket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;



/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BookListFragment extends Fragment {

    RecyclerView recyclerView;
    private ArrayList<Book> books = new ArrayList<>();


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

        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        // Color Scheme
        if(mType.equals(Book.ALL_BOOK_SELL) || mType.equals(Book.MY_BOOK_SELL)) {
            view.setBackgroundColor(Color.parseColor("#c2efc2"));
        }
        else if(mType.equals(Book.ALL_BOOK_BUY) || mType.equals(Book.MY_BOOK_BUY)){
            view.setBackgroundColor(Color.parseColor("#cce0ff"));
        }

        // Set the query which sets the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            try {
                recyclerView.setAdapter(new BookRecyclerAdapter(books, mType, mainUser));
            }catch (IllegalAccessException iae){
                illegalAccess(context);
            }
        }
        return view;
    }


    private void illegalAccess(Context context){
        Intent intent = new Intent(context, ActLoginActivity.class);
        startActivity(intent);
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
