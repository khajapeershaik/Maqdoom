package com.project.maqdoom.ui.shops;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.data.model.api.TravelCategoryGroupResponse;
import com.project.maqdoom.databinding.FragmentShopsDetailBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.customerTouristGroups.insideCountry.InsideCountryAdapter;

import java.util.ArrayList;
import java.util.Arrays;
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

public class ShopsFragment extends BaseFragment<FragmentShopsDetailBinding, ShopsViewModel> implements ShopsNavigator {

    public static final String TAG = ShopsFragment.class.getSimpleName();
    public FragmentShopsDetailBinding fragmentShopsBinding;
    public ShopsViewModel shopsViewModel;
    @Inject
    InsideCountryAdapter mBlogAdapter;
    @Inject
    ViewModelProviderFactory factory;
    Spinner country, price, city, service;
    @Inject
    LinearLayoutManager mLayoutManager;

    public static ShopsFragment newInstance() {
        Bundle args = new Bundle();
        ShopsFragment fragment = new ShopsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shops_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopsViewModel.setNavigator(this);
        //mBlogAdapter.setListener(this);
        //setUp();
    }

    @Override
    public ShopsViewModel getViewModel() {
        shopsViewModel = ViewModelProviders.of(this, factory).get(ShopsViewModel.class);
        return shopsViewModel;
    }

    private void showHome() {
        for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentShopsBinding = getViewDataBinding();
        onBackPressed();
        setUp();
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

    @Override
    public void handleError(Throwable throwable) {

    }

    @Override
    public void goBack() {
        showHome();
    }

    private void setUp() {

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentShopsBinding.blogRecyclerView.setLayoutManager(mLayoutManager);
        fragmentShopsBinding.blogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fragmentShopsBinding.blogRecyclerView.setAdapter(mBlogAdapter);
        observeShopsData();
        Timer timerObj = new Timer();
        TimerTask timerTaskObj = new TimerTask() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        LiveData<List<TravelCategoryGroupResponse.Adds>> countryData = shopsViewModel.getShopsListLiveData();
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

    private void observeShopsData(){
        shopsViewModel.getShopsListLiveData().observe(this, adds -> {
            mBlogAdapter.addItems(adds);
            mBlogAdapter.notifyDataSetChanged();
        });
    }

    private void setSpinner() {
        country = fragmentShopsBinding.spinnerCountry;
        price = fragmentShopsBinding.spinnerPrice;
        city = fragmentShopsBinding.spinnerCity;
        service = fragmentShopsBinding.spinnerService;

        ArrayList<String> countyList = new ArrayList<>();
        ArrayList<String> priceList = new ArrayList<>();
        ArrayList<String> cityList = new ArrayList<>();
        ArrayList<String> serviceList = new ArrayList<>();
        serviceList.add("Parachute");
        serviceList.add("Diving");
        serviceList.add("Mountaineer");

        LiveData<List<TravelCategoryGroupResponse.Adds>> countryData = shopsViewModel.getShopsListLiveData();
        if (countryData != null) {
            for (int i = 0; i < countryData.getValue().size(); i++) {
                if (!countyList.contains(countryData.getValue().get(i).getCountry()) && countryData.getValue().get(i).getCountry() != null && !"".equalsIgnoreCase(countryData.getValue().get(i).getCountry().trim())) {
                    countyList.add(countryData.getValue().get(i).getCountry());
                }
                if (!priceList.contains(countryData.getValue().get(i).getPrice()) && countryData.getValue().get(i).getPrice() != null && !"".equalsIgnoreCase(countryData.getValue().get(i).getPrice().trim())) {
                    priceList.add(countryData.getValue().get(i).getPrice());
                }
                if (!cityList.contains(countryData.getValue().get(i).getCity()) && countryData.getValue().get(i).getCity() != null
                        && (countryData.getValue().get(i).getCity().trim().length() > 0)) {
                    cityList.add(countryData.getValue().get(i).getCity());
                }

            }
            countyList.add(0, getString(R.string.s_country));
            priceList.add(0, getString(R.string.service_price));
            cityList.add(0, "City");
            serviceList.add(0, "Service");

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
                    }else {
                        mBlogAdapter.clearItems();
                        observeShopsData();
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
                        observeShopsData();
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
                    }else {
                        mBlogAdapter.clearItems();
                        observeShopsData();
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
                    }else {
                        mBlogAdapter.clearItems();
                        observeShopsData();
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
                LiveData<List<TravelCategoryGroupResponse.Adds>> data = shopsViewModel.getShopsListLiveData();
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
                LiveData<List<TravelCategoryGroupResponse.Adds>> data = shopsViewModel.getShopsListLiveData();
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
        } else if (type == 3) {

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
                LiveData<List<TravelCategoryGroupResponse.Adds>> data = shopsViewModel.getShopsListLiveData();
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
                LiveData<List<TravelCategoryGroupResponse.Adds>> data = shopsViewModel.getShopsListLiveData();
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
