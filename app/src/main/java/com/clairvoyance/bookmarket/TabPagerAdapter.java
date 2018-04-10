package com.clairvoyance.bookmarket;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Sell", "Buy" };
    private Context context;
    private ArrayList<Book> sellBooks;
    private ArrayList<Book> buyBooks;

    TabPagerAdapter(FragmentManager fm, Context context, ArrayList<Book> sellBooks, ArrayList<Book> buyBooks) {
        super(fm);
        this.context = context;
        this.sellBooks = sellBooks;
        this.buyBooks = buyBooks;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        ArrayList<Book> books = new ArrayList<>();
        String type = "NULL";
        switch (position){
            case 0:
                books = sellBooks;
                type = "SELL";
                break;
            case 1:
                books = buyBooks;
                type = "BUY";
                break;
        }

        return BookListFragment.newInstance(books, type);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
