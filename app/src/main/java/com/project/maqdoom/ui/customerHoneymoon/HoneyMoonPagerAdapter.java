package com.project.maqdoom.ui.customerHoneymoon;
import com.project.maqdoom.ui.customerTouristGroups.insideCountry.InsideCountryFragment;
import com.project.maqdoom.ui.customerTouristGroups.outsideCountry.OutsideCountryFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class HoneyMoonPagerAdapter extends FragmentStatePagerAdapter {

    private int mTabCount;

    public HoneyMoonPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mTabCount = 0;
    }

    @Override
    public int getCount() {
        return mTabCount;
    }

    public void setCount(int count) {
        mTabCount = count;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return InsideCountryFragment.newInstance("HM");
            case 1:
                return OutsideCountryFragment.newInstance("HM");
            default:
                return null;
        }
    }

}

