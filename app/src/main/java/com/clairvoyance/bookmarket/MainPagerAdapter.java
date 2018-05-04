package com.clairvoyance.bookmarket;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private final String HOME = "HOME";
    private String pageTypes[] = new String[] {Book.ALL_BOOK_SELL, HOME, Book.ALL_BOOK_BUY};
    private User mainUser;

    MainPagerAdapter(FragmentManager fm, User user){
        super(fm);
        mainUser = user;
    }

    @Override
    public int getCount() {
        return pageTypes.length;
    }

    @Override
    public Fragment getItem(int position) {

        if(pageTypes[position].equals(HOME)){
            return HomeFragment.newInstance(mainUser);
        }else {
            return BookListFragment.newInstance(pageTypes[position], mainUser);
        }

    }

}
