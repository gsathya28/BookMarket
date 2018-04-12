package com.clairvoyance.bookmarket;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { Book.MY_BOOK_SELL, Book.MY_BOOK_BUY };

    TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        return BookListFragment.newInstance(tabTitles[position]);
    }

    @Nullable
    @Override
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
