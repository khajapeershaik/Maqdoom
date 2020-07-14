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

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.FragmentTouristGroupBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.customerFamilyTrip.FamilyTripFragment;
import com.project.maqdoom.ui.customerHoneymoon.TouristHoneymoonFragment;
import com.project.maqdoom.ui.customerTouristGuide.TouristGuidesFragment;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class TouristGroupFragment extends BaseFragment<FragmentTouristGroupBinding, TouristGroupViewModel> implements TouristGroupNavigator/*, BlogAdapter.BlogAdapterListener*/ {

    public static final String TAG = TouristGroupFragment.class.getSimpleName();



    @Inject
    ViewModelProviderFactory factory;

    public TouristGroupViewModel touristGroupViewModel;

    public FragmentTouristGroupBinding fragmentTouristGroupBinding;

    public static TouristGroupFragment newInstance() {
        Bundle args = new Bundle();
        TouristGroupFragment fragment = new TouristGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tourist_group;
    }

    @Override
    public TouristGroupViewModel getViewModel() {
        touristGroupViewModel = ViewModelProviders.of(this, factory).get(TouristGroupViewModel.class);
        return touristGroupViewModel;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        touristGroupViewModel.setNavigator(this);
        //mBlogAdapter.setListener(this);
        //setUp();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentTouristGroupBinding = getViewDataBinding();
        setUp();
        onBackPressed();
    }


    private void setUp() {
        TouristGroupPagerAdapter mPagerAdapter = new TouristGroupPagerAdapter(getFragmentManager());
        mPagerAdapter.setCount(2);
        fragmentTouristGroupBinding.feedViewPager.setAdapter(mPagerAdapter);
        fragmentTouristGroupBinding.tabLayout.addTab(fragmentTouristGroupBinding.tabLayout.newTab().setText(getString(R.string.tourist_inside)));
        fragmentTouristGroupBinding.tabLayout.addTab(fragmentTouristGroupBinding.tabLayout.newTab().setText(getString(R.string.tourist_outside)));


        //fragmentTouristGroupBinding.feedViewPager.setOffscreenPageLimit(fragmentTouristGroupBinding.tabLayout.getTabCount());
        fragmentTouristGroupBinding.feedViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(fragmentTouristGroupBinding.tabLayout));
        fragmentTouristGroupBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentTouristGroupBinding.feedViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void goBack() {
       /* getBaseActivity().onFragmentDetached(TAG);
        getBaseActivity().onFragmentDetached(TouristGuidesFragment.TAG);
        getBaseActivity().onFragmentDetached(TouristHoneymoonFragment.TAG);
        getBaseActivity().onFragmentDetached(FamilyTripFragment.TAG);*/

        showHome();
    }

    @Override
    public void goToTouristGuide() {
        getFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .add(R.id.parentLayout, TouristGuidesFragment.newInstance(), TouristGuidesFragment.TAG)
                .commit();
    }

    @Override
    public void gotoHoneymoon() {
        getFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .add(R.id.parentLayout, TouristHoneymoonFragment.newInstance(), TouristGroupFragment.TAG)
                .commit();
    }

    @Override
    public void gotoFamilyTrip() {
        getFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .add(R.id.parentLayout, FamilyTripFragment.newInstance(), FamilyTripFragment.TAG)
                .commit();
    }

    private void showHome() {
        for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void onBackPressed() {
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        showHome();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
