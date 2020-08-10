/*
 * Copyright 2020 Maqdoom. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.project.maqdoom.ui.customerTouristGroups;

import com.project.maqdoom.R;
import com.project.maqdoom.ui.customerTouristGroups.insideCountry.InsideCountryFragment;
import com.project.maqdoom.ui.customerTouristGroups.outsideCountry.OutsideCountryFragment;
import com.project.maqdoom.ui.customerTouristGuide.TouristGuidesFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class TouristGroupPagerAdapter extends FragmentStatePagerAdapter {

    private int mTabCount;

    public TouristGroupPagerAdapter(FragmentManager fragmentManager) {
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
                return InsideCountryFragment.newInstance("TGP");
            case 1:
                return OutsideCountryFragment.newInstance("TGP");
            default:
                return null;
        }
    }


}
