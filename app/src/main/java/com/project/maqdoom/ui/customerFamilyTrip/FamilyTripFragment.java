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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.data.model.api.TravelCategoryResponse;
import com.project.maqdoom.databinding.FragmentFamilyTripBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.customerHoneymoon.TouristHoneymoonFragment;
import com.project.maqdoom.ui.customerTouristGroups.TouristGroupFragment;
import com.project.maqdoom.ui.customerTouristGuide.TouristGuidesFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;


public class FamilyTripFragment extends BaseFragment<FragmentFamilyTripBinding, FamilyTripViewModel> implements FamilyTripNavigator {

    public static final String TAG = FamilyTripFragment.class.getSimpleName();
    @Inject
    TouristFamilyAdapter mBlogAdapter;
    @Inject
    LinearLayoutManager mLayoutManager;
    @Inject
    ViewModelProviderFactory factory;
    private FamilyTripViewModel familyTripViewModel;
    FragmentFamilyTripBinding fragmentTouristFamilyBinding;
    Spinner country,price;
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
    public void goBack() {

        showHome();
    }

    @Override
    public void goToTouristGroup() {
        for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            if (fragment instanceof TouristGroupFragment){
                this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        getFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .add(R.id.parentLayout, TouristGroupFragment.newInstance(), TouristGroupFragment.TAG)
                .commit();
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
    public void goToTouristHoneymoon() {
        getFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .add(R.id.parentLayout, TouristHoneymoonFragment.newInstance(), TouristHoneymoonFragment.TAG)
                .commit();
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
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentTouristFamilyBinding.blogRecyclerView.setLayoutManager(mLayoutManager);
        fragmentTouristFamilyBinding.blogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fragmentTouristFamilyBinding.blogRecyclerView.setAdapter(mBlogAdapter);

        Timer timerObj = new Timer();
        TimerTask timerTaskObj = new TimerTask() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        LiveData<List<TravelCategoryResponse.Adds>> countryData = familyTripViewModel.getTravelListLiveData();
                        if (countryData.getValue() != null) {
                            setSpinner();
                            timerObj.cancel();
                            timerObj.purge();
                        }

                    }
                });

            }
        };
        timerObj.schedule(timerTaskObj, 0, 1000);

    }
    private void showHome() {
        for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
    private void setSpinner() {
        country = fragmentTouristFamilyBinding.spinnerCountry;
        price = fragmentTouristFamilyBinding.spinnerCity;
        ArrayList<String> countyList = new ArrayList<>();
        ArrayList<String> priceList = new ArrayList<>();
        LiveData<List<TravelCategoryResponse.Adds>> countryData = familyTripViewModel.getTravelListLiveData();
        if (countryData != null) {
            for (int i = 0; i < countryData.getValue().size(); i++) {
                if (!countyList.contains(countryData.getValue().get(i).getCountry()) && countryData.getValue().get(i).getCountry() != null && !"".equalsIgnoreCase(countryData.getValue().get(i).getCountry().trim())) {
                    countyList.add(countryData.getValue().get(i).getCountry());
                }
                if (!priceList.contains(countryData.getValue().get(i).getPrice()) && countryData.getValue().get(i).getPrice() != null && !"".equalsIgnoreCase(countryData.getValue().get(i).getPrice().trim())) {
                    priceList.add(countryData.getValue().get(i).getPrice());
                }

            }
            countyList.add(0,getString(R.string.s_country));
            priceList.add(0,getString(R.string.service_price));
            //Country spinner
            ArrayAdapter<String> spinnerCountryAdapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    countyList);
            country.setAdapter(spinnerCountryAdapter);
            country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (arg2 != 0) {
                        String selected = country.getItemAtPosition(arg2).toString();
                        updateList(1, selected);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });

            //City spinner
            ArrayAdapter<String> spinnerCityAdapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    priceList);
            price.setAdapter(spinnerCityAdapter);
            price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (arg2 != 0) {
                        String selected = price.getItemAtPosition(arg2).toString();
                        updateList(2, selected);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });

        }
    }
    private void updateList(int type, String value) {
       /* Type 1--> Country
        Type 2--> City
        Type 3--> Language  */
        List<TravelCategoryResponse.Adds> filteredData = new ArrayList<>();
        if (type == 1) {
            if (filteredData.size() > 1) {
                for (int i = 0; i < filteredData.size(); i++) {
                    if (value.equalsIgnoreCase(filteredData.get(i).getCountry())) {
                        //data.getValue().remove(i);
                        filteredData.add(filteredData.get(i));
                    }
                }
                mBlogAdapter.clearItems();
                mBlogAdapter.notifyDataSetChanged();
                mBlogAdapter.addItems(filteredData);
            } else {
                LiveData<List<TravelCategoryResponse.Adds>> data = familyTripViewModel.getTravelListLiveData();
                if (data != null) {
                    for (int i = 0; i < data.getValue().size(); i++) {
                        if (value.equalsIgnoreCase(data.getValue().get(i).getCountry())) {
                            filteredData.add(data.getValue().get(i));
                        }
                    }
                    mBlogAdapter.clearItems();
                    mBlogAdapter.notifyDataSetChanged();
                    mBlogAdapter.addItems(filteredData);
                }
            }

        } else if (type == 2) {
            if (filteredData.size() > 1) {
                for (int i = 0; i < filteredData.size(); i++) {
                    if (value.equalsIgnoreCase(filteredData.get(i).getPrice())) {
                        filteredData.add(filteredData.get(i));
                    }
                }
                mBlogAdapter.clearItems();
                mBlogAdapter.notifyDataSetChanged();
                mBlogAdapter.addItems(filteredData);
            } else {
                LiveData<List<TravelCategoryResponse.Adds>> data = familyTripViewModel.getTravelListLiveData();
                if (data != null) {
                    for (int i = 0; i < data.getValue().size(); i++) {
                        if (value.equalsIgnoreCase(data.getValue().get(i).getPrice())) {
                            filteredData.add(data.getValue().get(i));
                        }
                    }
                    mBlogAdapter.clearItems();
                    mBlogAdapter.notifyDataSetChanged();
                    mBlogAdapter.addItems(filteredData);
                }
            }
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
