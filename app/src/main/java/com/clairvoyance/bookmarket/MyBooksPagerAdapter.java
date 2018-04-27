package com.clairvoyance.bookmarket;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyBooksPagerAdapter extends FragmentPagerAdapter {

    private String bookListTypes[] = new String[] { Book.MY_BOOK_SELL, Book.MY_BOOK_BUY };
    private User mainUser;

    MyBooksPagerAdapter(FragmentManager fm, User user) {
        super(fm);
        mainUser = user;
    }

    @Override
    public int getCount() {
        return bookListTypes.length;
    }

    @Override
    public Fragment getItem(int position) {
        return BookListFragment.newInstance(bookListTypes[position], mainUser);
    }

    @Nullable
    @Override
    // Tab Titles (Mandatory for making sure tab titles are appropriate)
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "SELL";
            case 1:
                return "BUY";
        }
        return super.getPageTitle(position);
    }
}
