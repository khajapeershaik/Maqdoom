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

package com.project.maqdoom.ui.customerTouristGroups.insideCountry;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.data.model.api.TravelCategoryGroupResponse;
import com.project.maqdoom.data.model.api.TravelCategoryResponse;
import com.project.maqdoom.databinding.FragmentTouristInsideBinding;
import com.project.maqdoom.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

public class InsideCountryFragment extends BaseFragment<FragmentTouristInsideBinding, InsideCountryViewModel>
        implements InsideCountryNavigator, InsideCountryAdapter.TravelGroupAdapterListener {

    @Inject
    InsideCountryAdapter mBlogAdapter;

    FragmentTouristInsideBinding fragmentTouristInsideBinding;
    @Inject
    LinearLayoutManager mLayoutManager;
    @Inject
    ViewModelProviderFactory factory;
    private InsideCountryViewModel insideCountryViewModel;
    Spinner country,price;
    public static InsideCountryFragment newInstance() {
        Bundle args = new Bundle();
        InsideCountryFragment fragment = new InsideCountryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tourist_inside;
    }

    @Override
    public InsideCountryViewModel getViewModel() {
        insideCountryViewModel = ViewModelProviders.of(this, factory).get(InsideCountryViewModel.class);
        return insideCountryViewModel;
    }

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        insideCountryViewModel.setNavigator(this);
    }

    @Override
    public void onRetryClick() {
        insideCountryViewModel.fetchData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentTouristInsideBinding = getViewDataBinding();
        setUp();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void updateBlog(List<TravelCategoryGroupResponse.Adds> blogList) {
        mBlogAdapter.addItems(blogList);
    }

    private void setUp() {
       /* for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            System.out.println(fragment);
        }*/

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentTouristInsideBinding.blogRecyclerView.setLayoutManager(mLayoutManager);
        fragmentTouristInsideBinding.blogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fragmentTouristInsideBinding.blogRecyclerView.setAdapter(mBlogAdapter);

        Timer timerObj = new Timer();
        TimerTask timerTaskObj = new TimerTask() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        LiveData<List<TravelCategoryGroupResponse.Adds>> countryData = insideCountryViewModel.getTravelListLiveData();
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
    private void setSpinner() {
        country = fragmentTouristInsideBinding.spinnerCountry;
        price = fragmentTouristInsideBinding.spinnerPrice;
        ArrayList<String> countyList = new ArrayList<>();
        ArrayList<String> priceList = new ArrayList<>();
        LiveData<List<TravelCategoryGroupResponse.Adds>> countryData = insideCountryViewModel.getTravelListLiveData();
        if (countryData != null) {
            for (int i = 0; i < countryData.getValue().size(); i++) {
                if (!countyList.contains(countryData.getValue().get(i).getCountry()) && countryData.getValue().get(i).getCountry() != null && !"".equalsIgnoreCase(countryData.getValue().get(i).getCountry().trim())) {
                    countyList.add(countryData.getValue().get(i).getCountry());
                }
                if (!priceList.contains(countryData.getValue().get(i).getPrice()) && countryData.getValue().get(i).getPrice() != null && !"".equalsIgnoreCase(countryData.getValue().get(i).getPrice().trim())) {
                    priceList.add(countryData.getValue().get(i).getPrice());
                }

            }
            countyList.add(0, getString(R.string.s_country));
            priceList.add(0, getString(R.string.service_price));
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
                LiveData<List<TravelCategoryGroupResponse.Adds>> data = insideCountryViewModel.getTravelListLiveData();
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
                LiveData<List<TravelCategoryGroupResponse.Adds>> data = insideCountryViewModel.getTravelListLiveData();
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
}
