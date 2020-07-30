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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.data.model.api.DeleteAddRequest;
import com.project.maqdoom.data.model.api.DeleteAddResponse;
import com.project.maqdoom.data.model.api.TravelCategoryGroupResponse;
import com.project.maqdoom.data.remote.api_rest.ApiClient;
import com.project.maqdoom.data.remote.api_rest.ApiInterface;
import com.project.maqdoom.databinding.FragmentTouristInsideBinding;
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
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import retrofit2.Call;
import retrofit2.Response;

public class InsideCountryFragment extends BaseFragment<FragmentTouristInsideBinding, InsideCountryViewModel>
        implements InsideCountryNavigator, InsideCountryAdapter.TravelGroupAdapterListener {

    @Inject
    InsideCountryAdapter mBlogAdapter;

    FragmentTouristInsideBinding fragmentTouristInsideBinding;
    @Inject
    LinearLayoutManager mLayoutManager;
    @Inject
    ViewModelProviderFactory factory;
    Spinner country, price, city, service;
    private InsideCountryViewModel insideCountryViewModel;

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

    public void onDeleteButtonClick(String appId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        builder.setTitle((getActivity().getResources().getString(R.string.app_name)));
        builder.setMessage((getActivity().getResources().getString(R.string.sure_delete)));
        String positiveText = getActivity().getString(R.string.Ok);
        String negativeText = getActivity().getString(R.string.Cancel);
        builder.setPositiveButton(positiveText,
                (dialog, which) -> {
                    Log.v("InsideFragment", appId);
                    deleteAdd(appId);
                    dialog.dismiss();
                });
        builder.setNegativeButton(negativeText, (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void deleteAdd(String id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<DeleteAddResponse> deleteRequest = apiService.deleteAdd(new DeleteAddRequest.ServerDeleteAddRequest(id));
        deleteRequest.enqueue(new retrofit2.Callback<DeleteAddResponse>() {
            @Override
            public void onResponse(Call<DeleteAddResponse> call, Response<DeleteAddResponse> response) {
                if (response.isSuccessful()) {
                    if ("fail".equals(response.body().getResponse())) {
                        Toast.makeText(getContext(), "Something went wrong ,Please try again", Toast.LENGTH_LONG).show();
                    } else {
                        mBlogAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onFailure(Call<DeleteAddResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong ,Please try again", Toast.LENGTH_LONG).show();
            }
        });
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

        if (getActivity() != null) {
            Timer timerObj = new Timer();
            TimerTask timerTaskObj = new TimerTask() {
                public void run() {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        LiveData<List<TravelCategoryGroupResponse.Adds>> countryData = insideCountryViewModel.getTravelListLiveData();
                        if (countryData.getValue() != null) {
                            setSpinner();
                            timerObj.cancel();
                            timerObj.purge();
                        }

                    });

                }
            };
            timerObj.schedule(timerTaskObj, 0, 1000);
        }
        mBlogAdapter.setOnDeleteListener(appId -> onDeleteButtonClick(appId));
    }

    private void observeData() {
        insideCountryViewModel.getTravelListLiveData().observe(this, adds -> {
            mBlogAdapter.addItems(adds);
            mBlogAdapter.notifyDataSetChanged();
        });
    }

    private void setSpinner() {
        country = fragmentTouristInsideBinding.spinnerCountry;
        price = fragmentTouristInsideBinding.spinnerPrice;
        city = fragmentTouristInsideBinding.spinnerCity;
        service = fragmentTouristInsideBinding.spinnerService;
        ArrayList<String> countyList = new ArrayList<>();
        ArrayList<String> priceList = new ArrayList<>();

        ArrayList<String> cityList = new ArrayList<>();
        ArrayList<String> serviceList = new ArrayList<>();


        LiveData<List<TravelCategoryGroupResponse.Adds>> countryData = insideCountryViewModel.getTravelListLiveData();
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
                    String[] list = countryData.getValue().get(i).getServices().split(",");
                    for (String s : list) {
                        if (!serviceList.contains(s)) {
                            serviceList.add(s);
                        }
                    }
                }
            }
            priceList.add(getString(R.string.str_low_high));
            priceList.add(getString(R.string.str_high_low));

            countyList.add(0, getString(R.string.s_country));
            priceList.add(0, getString(R.string.service_price));
            cityList.add(0, getString(R.string.s_city));
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
                    } else {
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
                    } else {
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
                    } else {
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
                    } else {
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
                    if (value.equalsIgnoreCase(filteredData.get(i).getCity())) {
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
                Collections.sort(filteredData, TravelCategoryGroupResponse.Adds.PRICE);
                if (value.equalsIgnoreCase(getString(R.string.str_low_high))) {
                    Log.v("filteredData", "" + filteredData);
                } else {
                    Collections.reverse(filteredData);
                }

                mBlogAdapter.clearItems();
                mBlogAdapter.notifyDataSetChanged();
                mBlogAdapter.addItems(filteredData);
            } else {
                LiveData<List<TravelCategoryGroupResponse.Adds>> data = insideCountryViewModel.getTravelListLiveData();
                if (data != null) {
                    for (int i = 0; i < data.getValue().size(); i++) {
                        filteredData.add(data.getValue().get(i));
                    }
                    Collections.sort(filteredData, TravelCategoryGroupResponse.Adds.PRICE);
                    if (value.equalsIgnoreCase(getString(R.string.str_low_high))) {
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
                LiveData<List<TravelCategoryGroupResponse.Adds>> data = insideCountryViewModel.getTravelListLiveData();
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
