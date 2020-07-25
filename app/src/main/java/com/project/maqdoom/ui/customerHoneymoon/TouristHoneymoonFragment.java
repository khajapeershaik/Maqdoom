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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.data.model.api.TravelCategoryResponse;
import com.project.maqdoom.databinding.FragmentHoneymoonBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.customerFamilyTrip.FamilyTripFragment;
import com.project.maqdoom.ui.customerTouristGroups.TouristGroupFragment;
import com.project.maqdoom.ui.customerTouristGuide.TouristGuidesFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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


public class TouristHoneymoonFragment extends BaseFragment<FragmentHoneymoonBinding, TouristHoneymoonViewModel> implements HoneymoonNavigator {

    public static final String TAG = TouristHoneymoonFragment.class.getSimpleName();

    @Inject
    HoneyMoonAdapter mBlogAdapter;

    FragmentHoneymoonBinding fragmentHoneymoonBinding;
    @Inject
    ViewModelProviderFactory factory;

    @Inject
    LinearLayoutManager mLayoutManager;

    private TouristHoneymoonViewModel honeymoonViewModel;
    Spinner country, price, city, service;
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
        honeymoonViewModel = ViewModelProviders.of(this,factory).get(TouristHoneymoonViewModel.class);
        return honeymoonViewModel;
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
    public void gotoTouristGuide() {
        getFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .add(R.id.parentLayout, TouristGuidesFragment.newInstance(), TouristGuidesFragment.TAG)
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
   //     onBackPressed();
    }
    private void setUp() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentHoneymoonBinding.blogRecyclerView.setLayoutManager(mLayoutManager);
        fragmentHoneymoonBinding.blogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fragmentHoneymoonBinding.blogRecyclerView.setAdapter(mBlogAdapter);

        if (getActivity() != null) {
            Timer timerObj = new Timer();
            TimerTask timerTaskObj = new TimerTask() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            LiveData<List<TravelCategoryResponse.Adds>> countryData = honeymoonViewModel.getTravelListLiveData();
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
    private void showHome() {
        for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void observeData() {
        honeymoonViewModel.getTravelListLiveData().observe(this, adds -> {
            mBlogAdapter.addItems(adds);
            mBlogAdapter.notifyDataSetChanged();
        });
    }
    private void setSpinner() {
        country = fragmentHoneymoonBinding.spinnerCountry;
        price = fragmentHoneymoonBinding.spinnerPrice;
        city = fragmentHoneymoonBinding.spinnerCity;
        service = fragmentHoneymoonBinding.spinnerService;

        ArrayList<String> countyList = new ArrayList<>();
        ArrayList<String> priceList = new ArrayList<>();

        ArrayList<String> cityList = new ArrayList<>();
        ArrayList<String> serviceList = new ArrayList<>();

        LiveData<List<TravelCategoryResponse.Adds>> countryData = honeymoonViewModel.getTravelListLiveData();
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
            cityList.add(0, getString(R.string.city));
            serviceList.add(0, getString(R.string.service));


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
                        String selected = city.getItemAtPosition(arg2).toString();
                        updateList(2, selected);
                    }else {
                        mBlogAdapter.clearItems();
                        observeData();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
            // Price
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
                    else{
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
                    else{
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
                LiveData<List<TravelCategoryResponse.Adds>> data = honeymoonViewModel.getTravelListLiveData();
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
                    if (value.equalsIgnoreCase(filteredData.get(i).getCity())) {
                        filteredData.add(filteredData.get(i));
                    }
                }
                mBlogAdapter.clearItems();
                mBlogAdapter.notifyDataSetChanged();
                mBlogAdapter.addItems(filteredData);
            } else {
                LiveData<List<TravelCategoryResponse.Adds>> data = honeymoonViewModel.getTravelListLiveData();
                if (data != null) {
                    for (int i = 0; i < data.getValue().size(); i++) {
                        if (value.equalsIgnoreCase(data.getValue().get(i).getCity())) {
                            filteredData.add(data.getValue().get(i));
                        }
                    }
                    mBlogAdapter.clearItems();
                    mBlogAdapter.notifyDataSetChanged();
                    mBlogAdapter.addItems(filteredData);
                }
            }
        }
        else if (type == 3) {
            if (filteredData.size() > 1) {
                Collections.sort(filteredData, TravelCategoryResponse.Adds.PRICE);
                if (value.equalsIgnoreCase("Low-High")) {
                    Log.v("filteredData", "" + filteredData);
                } else {
                    Collections.reverse(filteredData);
                }

                mBlogAdapter.clearItems();
                mBlogAdapter.notifyDataSetChanged();
                mBlogAdapter.addItems(filteredData);
            } else {
                LiveData<List<TravelCategoryResponse.Adds>> data = honeymoonViewModel.getTravelListLiveData();
                if (data != null) {
                    for (int i = 0; i < data.getValue().size(); i++) {
                        filteredData.add(data.getValue().get(i));
                    }
                    Collections.sort(filteredData, TravelCategoryResponse.Adds.PRICE);
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
                LiveData<List<TravelCategoryResponse.Adds>> data = honeymoonViewModel.getTravelListLiveData();
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
    private void onBackPressed() {
        getView().setOnKeyListener((v, keyCode, event) -> {
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
