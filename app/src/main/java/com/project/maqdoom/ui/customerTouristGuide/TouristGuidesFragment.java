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

package com.project.maqdoom.ui.customerTouristGuide;

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
import com.project.maqdoom.databinding.FragmentTouristGuideBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.customerFamilyTrip.FamilyTripFragment;
import com.project.maqdoom.ui.customerHoneymoon.TouristHoneymoonFragment;
import com.project.maqdoom.ui.customerTouristGroups.TouristGroupFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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


public class TouristGuidesFragment extends BaseFragment<FragmentTouristGuideBinding, TouristGuideViewModel> implements TouristGuideNavigator {

    public static final String TAG = TouristGuidesFragment.class.getSimpleName();
    @Inject
    TouristGuideAdapter mBlogAdapter;
    @Inject
    LinearLayoutManager mLayoutManager;
    @Inject
    ViewModelProviderFactory factory;
    private TouristGuideViewModel touristGuideViewModel;
    FragmentTouristGuideBinding fragmentTouristGuideBinding;
    Spinner country, city, language;

    public static TouristGuidesFragment newInstance() {
        Bundle args = new Bundle();
        TouristGuidesFragment fragment = new TouristGuidesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tourist_guide;
    }

    @Override
    public TouristGuideViewModel getViewModel() {
        touristGuideViewModel = ViewModelProviders.of(this, factory).get(TouristGuideViewModel.class);
        return touristGuideViewModel;
    }

    @Override
    public void goBack() {
        showHome();
    }

    @Override
    public void goToTouristGroup() {
        for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            if (fragment instanceof TouristGroupFragment) {
                this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        getFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                //.setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.parentLayout, TouristGroupFragment.newInstance(), TouristGroupFragment.TAG)
                .commit();

    }

    @Override
    public void gotoHoneymoon() {
        getFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                //.setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.parentLayout, TouristHoneymoonFragment.newInstance(), TouristHoneymoonFragment.TAG)
                .commit();

    }

    @Override
    public void gotoFamilyTrip() {
        getFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                //.setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.parentLayout, FamilyTripFragment.newInstance(), FamilyTripFragment.TAG)
                .commit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        touristGuideViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentTouristGuideBinding = getViewDataBinding();
        setUp();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        onBackPressed();

    }

    private void setUp() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentTouristGuideBinding.blogRecyclerView.setLayoutManager(mLayoutManager);
        fragmentTouristGuideBinding.blogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fragmentTouristGuideBinding.blogRecyclerView.setAdapter(mBlogAdapter);

        if (getActivity() != null) {
            try {
                Timer timerObj = new Timer();
                TimerTask timerTaskObj = new TimerTask() {
                    public void run() {
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                LiveData<List<TravelCategoryResponse.Adds>> countryData = touristGuideViewModel.getTravelListLiveData();
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
            } catch (Exception e) {

            }
        }

    }

    private void setSpinner() {
        country = fragmentTouristGuideBinding.spinnerCountry;
        city = fragmentTouristGuideBinding.spinnerCity;
        language = fragmentTouristGuideBinding.spinnerLanguage;
        ArrayList<String> countyList = new ArrayList<>();
        ArrayList<String> cityList = new ArrayList<>();
        ArrayList<String> languageList = new ArrayList<>();
        List<String> languageListFromServer  = new ArrayList<>();
        LiveData<List<TravelCategoryResponse.Adds>> countryData = touristGuideViewModel.getTravelListLiveData();
        if (countryData != null) {
            for (int i = 0; i < countryData.getValue().size(); i++) {
                if (!countyList.contains(countryData.getValue().get(i).getCountry()) && countryData.getValue().get(i).getCountry() != null) {
                    countyList.add(countryData.getValue().get(i).getCountry());
                }
                if (!cityList.contains(countryData.getValue().get(i).getCity()) && countryData.getValue().get(i).getCity() != null && !"".equalsIgnoreCase(countryData.getValue().get(i).getCity().trim())) {
                    cityList.add(countryData.getValue().get(i).getCity());
                }
                if (!languageList.contains(countryData.getValue().get(i).getLanguage()) && countryData.getValue().get(i).getLanguage() != null && !"".equalsIgnoreCase(countryData.getValue().get(i).getLanguage().trim())) {
                    List<String> temp  = Arrays.asList(countryData.getValue().get(i).getLanguage().split(","));
                    //languageList.add(countryData.getValue().get(i).getLanguage());
                    for (String element : temp) {
                        languageListFromServer.add(element);
                    }
                }
            }
            Set<String> ss = new LinkedHashSet<>(languageListFromServer);
            for (String element : ss) {
                languageList.add(element);
            }
            countyList.add(0, getString(R.string.s_country));
            cityList.add(0, getString(R.string.s_city));
            languageList.add(0, getString(R.string.s_language));
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
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });

            //Language spinner
            ArrayAdapter<String> spinnerLanguageAdapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    languageList);
            language.setAdapter(spinnerLanguageAdapter);
            language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (arg2 != 0) {
                        String selected = language.getItemAtPosition(arg2).toString();
                        updateList(3, selected);
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
                LiveData<List<TravelCategoryResponse.Adds>> data = touristGuideViewModel.getTravelListLiveData();
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
                LiveData<List<TravelCategoryResponse.Adds>> data = touristGuideViewModel.getTravelListLiveData();
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
        }else if (type == 3) {
            if (filteredData.size() > 1) {
                for (int i = 0; i < filteredData.size(); i++) {
                    if (value.equalsIgnoreCase(filteredData.get(i).getLanguage())) {
                        filteredData.add(filteredData.get(i));
                    }
                }
                mBlogAdapter.clearItems();
                mBlogAdapter.notifyDataSetChanged();
                mBlogAdapter.addItems(filteredData);
            }else {
                LiveData<List<TravelCategoryResponse.Adds>> data = touristGuideViewModel.getTravelListLiveData();
                if (data != null) {
                    for (int i = 0; i < data.getValue().size(); i++) {
                        List<String>languageList = Arrays.asList(data.getValue().get(i).getLanguage().split(","));
                        if(languageList.contains(value)){
                            filteredData.add(data.getValue().get(i));
                        }
                        /*if (value.equalsIgnoreCase(data.getValue().get(i).getLanguage())) {
                            filteredData.add(data.getValue().get(i));
                        }*/

                    }
                    mBlogAdapter.clearItems();
                    mBlogAdapter.notifyDataSetChanged();
                    mBlogAdapter.addItems(filteredData);
                }
            }
        }

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
