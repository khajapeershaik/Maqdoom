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

package com.project.maqdoom.ui.customerHoneymoon;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.FragmentHoneymoonBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.customerFamilyTrip.FamilyTripFragment;
import com.project.maqdoom.ui.customerTouristGroups.TouristGroupFragment;
import com.project.maqdoom.ui.customerTouristGuide.TouristGuidesFragment;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class TouristHoneymoonFragment extends BaseFragment<FragmentHoneymoonBinding, TouristHoneymoonViewModel> implements HoneymoonNavigator {

    public static final String TAG = TouristHoneymoonFragment.class.getSimpleName();

    FragmentHoneymoonBinding fragmentHoneymoonBinding;
    @Inject
    ViewModelProviderFactory factory;

    private TouristHoneymoonViewModel honeymoonViewModel;

    public static TouristHoneymoonFragment newInstance() {
        Bundle args = new Bundle();
        TouristHoneymoonFragment fragment = new TouristHoneymoonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_honeymoon;
    }

    @Override
    public TouristHoneymoonViewModel getViewModel() {
        honeymoonViewModel = ViewModelProviders.of(this, factory).get(TouristHoneymoonViewModel.class);
        return honeymoonViewModel;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        honeymoonViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentHoneymoonBinding = getViewDataBinding();
        setUp();
        onBackPressed();
    }

    private void setUp() {
        HoneyMoonPagerAdapter mPagerAdapter = new HoneyMoonPagerAdapter(getFragmentManager());
        mPagerAdapter.setCount(2);
        fragmentHoneymoonBinding.pager.setAdapter(mPagerAdapter);
        fragmentHoneymoonBinding.tabLayout.addTab(fragmentHoneymoonBinding.tabLayout.newTab().setText(getString(R.string.domestic_text)));
        fragmentHoneymoonBinding.tabLayout.addTab(fragmentHoneymoonBinding.tabLayout.newTab().setText(getString(R.string.international_text)));


        //fragmentTouristGroupBinding.feedViewPager.setOffscreenPageLimit(fragmentTouristGroupBinding.tabLayout.getTabCount());
        fragmentHoneymoonBinding.pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(fragmentHoneymoonBinding.tabLayout));
        fragmentHoneymoonBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentHoneymoonBinding.pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void goBack() {
        /*getBaseActivity().onFragmentDetached(TAG);
        getBaseActivity().onFragmentDetached(TouristGuidesFragment.TAG);
        getBaseActivity().onFragmentDetached(TouristGroupFragment.TAG);
        getBaseActivity().onFragmentDetached(FamilyTripFragment.TAG);*/

        showHome();
    }

    @Override
    public void goToTouristGroup() {
        for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            if (fragment instanceof TouristGroupFragment) {
                this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }

        if (getActivity() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .add(R.id.parentLayout, TouristGroupFragment.newInstance(), TouristGroupFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void gotoTouristGuide() {
        if (getActivity() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .add(R.id.parentLayout, TouristGuidesFragment.newInstance(), TouristGuidesFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void gotoFamilyTrip() {
        for (Fragment fragment : Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().getFragments()) {
            if (fragment instanceof FamilyTripFragment){
                this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        if (getActivity() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .add(R.id.parentLayout, FamilyTripFragment.newInstance(), FamilyTripFragment.TAG)
                    .commit();
        }
    }


    private void showHome() {
        for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void onBackPressed() {
        Objects.requireNonNull(getView()).setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    showHome();
                    return true;
                }
            }
            return false;
        });
    }
}
