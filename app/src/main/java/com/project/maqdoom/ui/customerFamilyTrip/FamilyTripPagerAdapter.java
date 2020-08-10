package com.project.maqdoom.ui.customerFamilyTrip;
import com.project.maqdoom.ui.customerTouristGroups.insideCountry.InsideCountryFragment;
import com.project.maqdoom.ui.customerTouristGroups.outsideCountry.OutsideCountryFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FamilyTripPagerAdapter extends FragmentStatePagerAdapter {

    private int mTabCount;

    public FamilyTripPagerAdapter(FragmentManager fragmentManager) {
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
                return InsideCountryFragment.newInstance("FT");
            case 1:
                return OutsideCountryFragment.newInstance("FT");
            default:
                return null;
        }
    }

}

