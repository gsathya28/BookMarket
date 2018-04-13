package com.clairvoyance.bookmarket;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private String pageTitles[] = new String[] {Book.ALL_BOOK_SELL, "MAIN", Book.ALL_BOOK_BUY};
    private User mainUser;

    MainPagerAdapter(FragmentManager fm, User user){
        super(fm);
        mainUser = user;
    }

    @Override
    public int getCount() {
        return pageTitles.length;
    }

    @Override
    public Fragment getItem(int position) {

        if(pageTitles[position].equals("MAIN")){
            return MainFragment.newInstance(mainUser);
        }else {
            return BookListFragment.newInstance(pageTitles[position], mainUser);
        }

    }

}
