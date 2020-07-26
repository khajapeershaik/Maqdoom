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

package com.project.maqdoom.ui.customerTouristGroups.outsideCountry;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.data.model.api.TravelCategoryGroupResponse;
import com.project.maqdoom.data.model.api.TravelCategoryResponse;
import com.project.maqdoom.databinding.FragmentTouristOutsideBinding;
import com.project.maqdoom.ui.base.BaseFragment;

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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

public class OutsideCountryFragment extends BaseFragment<FragmentTouristOutsideBinding, OutsideCountryViewModel>
        implements OutsideCountryNavigator, OutsideCountryAdapter.TravelGroupOutsideAdapterListener {

    @Inject
    OutsideCountryAdapter mBlogAdapter;

    FragmentTouristOutsideBinding fragmentTouristOutsideBinding;
    @Inject
    LinearLayoutManager mLayoutManager;
    @Inject
    ViewModelProviderFactory factory;
    private OutsideCountryViewModel outsideCountryViewModel;
    Spinner country, price, city, service;
    public static OutsideCountryFragment newInstance() {
        Bundle args = new Bundle();
        OutsideCountryFragment fragment = new OutsideCountryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tourist_outside;
    }

    @Override
    public OutsideCountryViewModel getViewModel() {
        outsideCountryViewModel = ViewModelProviders.of(this, factory).get(OutsideCountryViewModel.class);
        return outsideCountryViewModel;
    }

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        outsideCountryViewModel.setNavigator(this);
        //mBlogAdapter.setListener(this);
    }

    @Override
    public void onRetryClick() {
        outsideCountryViewModel.fetchData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentTouristOutsideBinding = getViewDataBinding();
        setUp();
    }

    @Override
    public void updateBlog(List<TravelCategoryResponse.Adds> blogList) {
        //mBlogAdapter.addItems(blogList);
    }

    private void setUp() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentTouristOutsideBinding.outsideCountryRecyclerView.setLayoutManager(mLayoutManager);
        fragmentTouristOutsideBinding.outsideCountryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fragmentTouristOutsideBinding.outsideCountryRecyclerView.setAdapter(mBlogAdapter);

        if (getActivity() != null) {
            Timer timerObj = new Timer();
            TimerTask timerTaskObj = new TimerTask() {
                public void run() {
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            LiveData<List<TravelCategoryGroupResponse.Adds>> countryData = outsideCountryViewModel.getTravelOutSideCountryListLiveData();
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
    }

    private void observeData() {
        outsideCountryViewModel.getTravelOutSideCountryListLiveData().observe(this, adds -> {
            mBlogAdapter.addItems(adds);
            mBlogAdapter.notifyDataSetChanged();
        });
    }
    private void setSpinner() {
        country = fragmentTouristOutsideBinding.spinnerCountry;
        price = fragmentTouristOutsideBinding.spinnerPrice;

        city = fragmentTouristOutsideBinding.spinnerCity;
        service = fragmentTouristOutsideBinding.spinnerService;


        ArrayList<String> cityList = new ArrayList<>();
        ArrayList<String> serviceList = new ArrayList<>();
        ArrayList<String> countyList = new ArrayList<>();
        ArrayList<String> priceList = new ArrayList<>();

        LiveData<List<TravelCategoryGroupResponse.Adds>> countryData = outsideCountryViewModel.getTravelOutSideCountryListLiveData();
        if (countryData != null) {
            for (int i = 0; i < countryData.getValue().size(); i++) {
                if (!countyList.contains(countryData.getValue().get(i).getCountry()) && countryData.getValue().get(i).getCountry() != null && !"".equalsIgnoreCase(countryData.getValue().get(i).getCountry().trim())) {
                    countyList.add(countryData.getValue().get(i).getCountry());
                }
                if (!cityList.contains(countryData.getValue().get(i).getCity()) && countryData.getValue().get(i).getCity() != null
                        && (countryData.getValue().get(i).getCity().trim().length() > 0)) {
                    cityList.add(countryData.getValue().get(i).getCity());
                }
                if (!serviceList.contains(countryData.getValue().get(i).getServices()) && countryData.getValue().get(i).getServices() != null && !"".equalsIgnoreCase(countryData.getValue().get(i).getServices().trim())) {
                    List<String> list = Arrays.asList(countryData.getValue().get(i).getServices().split(","));
                    for(String s:list){
                        if(!serviceList.contains(s)){
                            serviceList.add(s);
                        }
                    }
                }
            }
            priceList.add("Low-High");
            priceList.add("High-Low");

            countyList.add(0, getString(R.string.s_country));
            priceList.add(0, getString(R.string.service_price));
            cityList.add(0, "City");
            serviceList.add(0, "Service");
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
                    else {
                        mBlogAdapter.clearItems();
                        observeData();

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
                    cityList);
            city.setAdapter(spinnerCityAdapter);
            city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (arg2 != 0) {
                        String selected = price.getItemAtPosition(arg2).toString();
                        updateList(2, selected);
                    }
                    else {
                        mBlogAdapter.clearItems();
                        observeData();

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });

            ArrayAdapter<String> spinnerPriceAdapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    priceList);
            price.setAdapter(spinnerPriceAdapter);
            price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (arg2 != 0) {
                        String selected = price.getItemAtPosition(arg2).toString();
                        updateList(3, selected);
                    }
                    else {
                        mBlogAdapter.clearItems();
                        observeData();

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });

            //Service
            ArrayAdapter<String> spinnerServiceAdapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    serviceList);
            service.setAdapter(spinnerServiceAdapter);
            service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (arg2 != 0) {
                        String selected = service.getItemAtPosition(arg2).toString();
                        updateList(4, selected);
                    }
                    else {
                        mBlogAdapter.clearItems();
                        observeData();

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
        Type 3--> Price
        Type 4 -> Service
        */

        List<TravelCategoryGroupResponse.Adds> filteredData = new ArrayList<>();
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
                LiveData<List<TravelCategoryGroupResponse.Adds>> data = outsideCountryViewModel.getTravelOutSideCountryListLiveData();
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
                LiveData<List<TravelCategoryGroupResponse.Adds>> data = outsideCountryViewModel.getTravelOutSideCountryListLiveData();
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
        }else if (type == 3) {
            if (filteredData.size() > 1) {
                Collections.sort(filteredData, TravelCategoryGroupResponse.Adds.PRICE);
                if (value.equalsIgnoreCase("Low-High")) {
                    Log.v("filteredData", "" + filteredData);
                } else {
                    Collections.reverse(filteredData);
                }

                mBlogAdapter.clearItems();
                mBlogAdapter.notifyDataSetChanged();
                mBlogAdapter.addItems(filteredData);
            } else {
                LiveData<List<TravelCategoryGroupResponse.Adds>> data = outsideCountryViewModel.getTravelOutSideCountryListLiveData();
                if (data != null) {
                    for (int i = 0; i < data.getValue().size(); i++) {
                        filteredData.add(data.getValue().get(i));
                    }
                    Collections.sort(filteredData, TravelCategoryGroupResponse.Adds.PRICE);
                    if (value.equalsIgnoreCase("Low-High")) {
                        Log.v("filteredData", "" + filteredData);
                    } else {
                        Collections.reverse(filteredData);
                    }
                    mBlogAdapter.clearItems();
                    mBlogAdapter.notifyDataSetChanged();
                    mBlogAdapter.addItems(filteredData);
                }
            }
        } else if (type == 4) {
            if (filteredData.size() > 1) {
                for (int i = 0; i < filteredData.size(); i++) {
                    if (value.equalsIgnoreCase(filteredData.get(i).getServices())) {
                        filteredData.add(filteredData.get(i));
                    }
                }
                mBlogAdapter.clearItems();
                mBlogAdapter.notifyDataSetChanged();
                mBlogAdapter.addItems(filteredData);
            } else {
                LiveData<List<TravelCategoryGroupResponse.Adds>> data = outsideCountryViewModel.getTravelOutSideCountryListLiveData();
                if (data != null) {
                    for (int i = 0; i < data.getValue().size(); i++) {
                        List<String> languageList = Arrays.asList(data.getValue().get(i).getServices().split(","));
                        if (languageList.contains(value)) {
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
}
