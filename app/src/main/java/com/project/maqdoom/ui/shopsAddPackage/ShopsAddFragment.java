package com.project.maqdoom.ui.shopsAddPackage;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.data.model.api.TravelCategoryResponse;
import com.project.maqdoom.databinding.FragmentShopsBinding;
import com.project.maqdoom.databinding.FragmentTouristGuideBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.customerHoneymoon.HoneyMoonAdapter;
import com.project.maqdoom.ui.customerTouristGuide.TouristGuideViewModel;
import com.project.maqdoom.ui.customerTouristGuide.TouristGuidesFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

public class ShopsAddFragment extends BaseFragment<FragmentShopsBinding, ShopDetailsViewModel> implements ShopDetailsNavigator {

    Spinner country, city, language;
    public static final String TAG = ShopsAddFragment.class.getSimpleName();


    @Inject
    LinearLayoutManager mLayoutManager;
    @Inject
    ViewModelProviderFactory factory;
    private ShopDetailsViewModel shopDetailsViewModel;

    FragmentShopsBinding fragmentShopsBinding;
    public static ShopsAddFragment newInstance() {
        Bundle args = new Bundle();
        ShopsAddFragment fragment = new ShopsAddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopDetailsViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentShopsBinding = getViewDataBinding();
        setUp();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
      //  onBackPressed();

    }

    private void setUp() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       /* fragmentShopsBinding.shopsRecyclerView.setLayoutManager(mLayoutManager);
        fragmentShopsBinding.shopsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fragmentShopsBinding.shopsRecyclerView.setAdapter(shopDetailAdapter);*/

        Timer timerObj = new Timer();
        TimerTask timerTaskObj = new TimerTask() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        LiveData<List<TravelCategoryResponse.Adds>> countryData = shopDetailsViewModel.getTravelListLiveData();
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
        country = fragmentShopsBinding.spinnerCountry;
        city = fragmentShopsBinding.spinnerCity;
       // language = fragmentShopsBinding.spinnerLanguage;
        ArrayList<String> countyList = new ArrayList<>();
        ArrayList<String> cityList = new ArrayList<>();
        ArrayList<String> languageList = new ArrayList<>();
        List<String> languageListFromServer  = new ArrayList<>();
        LiveData<List<TravelCategoryResponse.Adds>> countryData = shopDetailsViewModel.getTravelListLiveData();
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
                       // updateList(1, selected);
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
                      //  updateList(2, selected);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });


        }


    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shops;
    }

    @Override
    public ShopDetailsViewModel getViewModel() {
        shopDetailsViewModel = ViewModelProviders.of(this, factory).get(ShopDetailsViewModel.class);
        return shopDetailsViewModel;
    }

    @Override
    public void goBack() {

    }

    @Override
    public void goHome() {

    }

    @Override
    public void showErrorAlert(String message) {

    }

/*
    private void updateList(int type, String value) {
       */
/* Type 1--> Country
        Type 2--> City
        Type 3--> Language  *//*

        List<TravelCategoryResponse.Adds> filteredData = new ArrayList<>();
        if (type == 1) {
            if (filteredData.size() > 1) {
                for (int i = 0; i < filteredData.size(); i++) {
                    if (value.equalsIgnoreCase(filteredData.get(i).getCountry())) {
                        //data.getValue().remove(i);
                        filteredData.add(filteredData.get(i));
                    }
                }
                shopDetailAdapter.clearItems();
                shopDetailAdapter.notifyDataSetChanged();
                shopDetailAdapter.addItems(filteredData);
            } else {
                LiveData<List<TravelCategoryResponse.Adds>> data = shopDetailsViewModel.getTravelListLiveData();
                if (data != null) {
                    for (int i = 0; i < data.getValue().size(); i++) {
                        if (value.equalsIgnoreCase(data.getValue().get(i).getCountry())) {
                            filteredData.add(data.getValue().get(i));
                        }
                    }
                    shopDetailAdapter.clearItems();
                    shopDetailAdapter.notifyDataSetChanged();
                    shopDetailAdapter.addItems(filteredData);
                }
            }

        } else if (type == 2) {
            if (filteredData.size() > 1) {
                for (int i = 0; i < filteredData.size(); i++) {
                    if (value.equalsIgnoreCase(filteredData.get(i).getPrice())) {
                        filteredData.add(filteredData.get(i));
                    }
                }
                shopDetailAdapter.clearItems();
                shopDetailAdapter.notifyDataSetChanged();
                shopDetailAdapter.addItems(filteredData);
            } else {
                LiveData<List<TravelCategoryResponse.Adds>> data = shopDetailsViewModel.getTravelListLiveData();
                if (data != null) {
                    for (int i = 0; i < data.getValue().size(); i++) {
                        if (value.equalsIgnoreCase(data.getValue().get(i).getPrice())) {
                            filteredData.add(data.getValue().get(i));
                        }
                    }
                    shopDetailAdapter.clearItems();
                    shopDetailAdapter.notifyDataSetChanged();
                    shopDetailAdapter.addItems(filteredData);
                }
            }
        }

    }
*/

}
