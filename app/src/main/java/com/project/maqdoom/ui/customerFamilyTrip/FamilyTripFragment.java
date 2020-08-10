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

package com.project.maqdoom.ui.customerFamilyTrip;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.data.model.api.DeleteAddRequest;
import com.project.maqdoom.data.model.api.DeleteAddResponse;
import com.project.maqdoom.data.model.api.TravelCategoryResponse;
import com.project.maqdoom.data.remote.api_rest.ApiClient;
import com.project.maqdoom.data.remote.api_rest.ApiInterface;
import com.project.maqdoom.databinding.FragmentFamilyTripBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.customerHoneymoon.HoneyMoonPagerAdapter;
import com.project.maqdoom.ui.customerHoneymoon.TouristHoneymoonFragment;
import com.project.maqdoom.ui.customerTouristGroups.TouristGroupFragment;
import com.project.maqdoom.ui.customerTouristGuide.TouristGuidesFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import retrofit2.Call;
import retrofit2.Response;


public class FamilyTripFragment extends BaseFragment<FragmentFamilyTripBinding, FamilyTripViewModel> implements FamilyTripNavigator {

    public static final String TAG = FamilyTripFragment.class.getSimpleName();

    @Inject
    ViewModelProviderFactory factory;
    private FamilyTripViewModel familyTripViewModel;

    FragmentFamilyTripBinding fragmentTouristFamilyBinding;

    public static FamilyTripFragment newInstance() {
        Bundle args = new Bundle();
        FamilyTripFragment fragment = new FamilyTripFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_family_trip;
    }

    @Override
    public FamilyTripViewModel getViewModel() {
        familyTripViewModel = ViewModelProviders.of(this, factory).get(FamilyTripViewModel.class);
        return familyTripViewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        familyTripViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentTouristFamilyBinding = getViewDataBinding();
        setUp();
        onBackPressed();
    }

    private void setUp() {
        FamilyTripPagerAdapter mPagerAdapter = new FamilyTripPagerAdapter(getFragmentManager());
        mPagerAdapter.setCount(2);
        fragmentTouristFamilyBinding.newpager.setAdapter(mPagerAdapter);
        fragmentTouristFamilyBinding.tabLayout.addTab(fragmentTouristFamilyBinding.tabLayout.newTab().setText(getString(R.string.domestic_text)));
        fragmentTouristFamilyBinding.tabLayout.addTab(fragmentTouristFamilyBinding.tabLayout.newTab().setText(getString(R.string.international_text)));


        fragmentTouristFamilyBinding.newpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(fragmentTouristFamilyBinding.tabLayout));
        fragmentTouristFamilyBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentTouristFamilyBinding.newpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
        });

    }
    @Override
    public void goBack() {

        showHome();
    }

    @Override
    public void goToTouristGroup() {
        for (Fragment fragment : Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().getFragments()) {
            if (fragment instanceof TouristGroupFragment){
                this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        if(getActivity()!=null) {
            getFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .add(R.id.parentLayout, TouristGroupFragment.newInstance(), TouristGroupFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void goToTouristGuide() {
        if(getActivity()!=null) {
            getFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .add(R.id.parentLayout, TouristGuidesFragment.newInstance(), TouristGuidesFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void goToTouristHoneymoon() {

        for (Fragment fragment : Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().getFragments()) {
            if (fragment instanceof TouristHoneymoonFragment){
                this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        if(getActivity()!=null) {
            getFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .add(R.id.parentLayout, TouristHoneymoonFragment.newInstance(), TouristHoneymoonFragment.TAG)
                    .commit();
        }
    }

    private void showHome() {
        for (Fragment fragment : Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().getFragments()) {
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
