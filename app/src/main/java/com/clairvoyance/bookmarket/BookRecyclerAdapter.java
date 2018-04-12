package com.clairvoyance.bookmarket;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.clairvoyance.bookmarket.BookListFragment.OnListFragmentInteractionListener;

import java.util.HashMap;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Book} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder> {

    private final List<Book> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final String mType;
    private final User mainUser;

    private final HashMap<String, String> bookRequests;

    BookRecyclerAdapter(List<Book> items, String type, OnListFragmentInteractionListener listener, User user) {
        mValues = items;
        mListener = listener;
        mType = type;
        mainUser = user;
        bookRequests = mainUser.getMyRequestIDs();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = parent;
        if(mType.equals(Book.MY_BOOK_SELL) || mType.equals(Book.MY_BOOK_BUY)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_fragment_book, parent, false);
        }
        else if(mType.equals(Book.ALL_BOOK_SELL) || mType.equals(Book.ALL_BOOK_BUY)){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_fragment_book, parent, false);
        }
        return new ViewHolder(view);
    }

    private void setButtonLayout(Button button){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        int bottomValueInPx = (int) button.getContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        bottomValueInPx = bottomValueInPx / 2;
        int valueInPx = (int) button.getContext().getResources().getDimension(R.dimen.activity_horizontal_margin);

        if(mType.equals(Book.MY_BOOK_SELL) || mType.equals(Book.MY_BOOK_BUY)){
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomValueInPx*2);
        }

        button.setGravity(Gravity.START);
        button.setGravity(Gravity.CENTER_VERTICAL);
        button.setTextColor(Color.WHITE);
        if(mType.equals(Book.ALL_BOOK_SELL) || mType.equals(Book.MY_BOOK_SELL)) {
            button.setBackgroundColor(Color.parseColor("#2aa22a"));
        }else if(mType.equals(Book.ALL_BOOK_BUY) || mType.equals(Book.MY_BOOK_BUY)){
            button.setBackgroundColor(Color.parseColor("#3385ff"));
        }
        button.setSingleLine();
        button.setEllipsize(TextUtils.TruncateAt.END);
        button.setPadding(valueInPx, button.getPaddingTop(), valueInPx, button.getPaddingBottom());
        button.setLayoutParams(params);
    }

    private void setReqButtonLayout(ToggleButton reqButton){
        LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        reqButton.setLayoutParams(checkParams);
        reqButton.setText(R.string.request);
        reqButton.setTextOff("Request");
        reqButton.setTextOn("Unrequest");
    }

    private void setBookLayout(LinearLayout bookLayout){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        int topValueInPx = (int) bookLayout.getContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        int bottomValueInPx = (int) bookLayout.getContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        bottomValueInPx = bottomValueInPx / 2;
        int valueInPx = (int) bookLayout.getContext().getResources().getDimension(R.dimen.activity_horizontal_margin);

        params.setMargins(params.leftMargin, topValueInPx, valueInPx/2, bottomValueInPx);

        bookLayout.setGravity(Gravity.CENTER_VERTICAL);
        bookLayout.setOrientation(LinearLayout.HORIZONTAL);
        bookLayout.setLayoutParams(params);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String buttonText = holder.mItem.getCourseSubj() + " " + holder.mItem.getCourseNumber() + " - " + holder.mItem.getTitle();
        if(holder.mView instanceof Button){ // Personal Book
            Button button = (Button) holder.mView;
            button.setText(buttonText);
            setButtonLayout(button);
        }
        else if(holder.mView instanceof LinearLayout){ // Public Book

            LinearLayout bookLayout = (LinearLayout) holder.mView;
            setBookLayout(bookLayout);

            Context context = bookLayout.getContext();

            final ToggleButton reqButton = new ToggleButton(context);
            bookLayout.addView(reqButton);
            setReqButtonLayout(reqButton);

            Button infoButton = new Button(context);
            infoButton.setText(buttonText);
            bookLayout.addView(infoButton);
            setButtonLayout(infoButton);

            // Check if the book is already requested by the User
            Book book = holder.mItem;
            if(bookRequests.containsKey(book.getBookID())){
                reqButton.setChecked(true);
            }



        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        public Book mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
