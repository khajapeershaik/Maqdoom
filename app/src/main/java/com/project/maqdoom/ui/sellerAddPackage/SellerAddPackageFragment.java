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

package com.project.maqdoom.ui.sellerAddPackage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.opensooq.supernova.gligar.GligarPicker;
import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.data.model.api.AddServiceRequest;
import com.project.maqdoom.databinding.FragmentSellerAddPackageBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.services.ServicesChecklistItems;
import com.project.maqdoom.ui.services.ServicesItemAdapter;
import com.project.maqdoom.ui.splash.SplashActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SellerAddPackageFragment extends BaseFragment<FragmentSellerAddPackageBinding, SellerAddPackageViewModel> implements SellerAddPackageNavigator {

    public static final String TAG = SellerAddPackageFragment.class.getSimpleName();
    public static String GD = "gd";
    public static String DATA_BUNDLE = "bundle";
    private static int option;
    String guideData = "";
    Spinner category;
    List<String> options, lan;
    String selectedCategory = "";
    java.util.HashMap<Integer, String> hashMap, images;
    java.util.HashMap<String, String> imageUpload;
    int PICKER_REQUEST_CODE = 30;
    String addId = "";
    String edtAddId = "";
    String[] pathsList;
    RequestQueue reqQueue;
    @Inject
    ViewModelProviderFactory factory;
    FragmentSellerAddPackageBinding fragmentSellerAddPackageBinding;
    private int isEditRequest = 0;
    private SellerAddPackageViewModel sellerAddPackageViewModel;
    private String country;

    public static SellerAddPackageFragment newInstance(int type, String jsonData) {
        Bundle args = new Bundle();
        args.putInt(GD, type);
        args.putString(DATA_BUNDLE, jsonData);
        SellerAddPackageFragment fragment = new SellerAddPackageFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_seller_add_package;
    }

    @Override
    public SellerAddPackageViewModel getViewModel() {
        sellerAddPackageViewModel = ViewModelProviders.of(this, factory).get(SellerAddPackageViewModel.class);
        return sellerAddPackageViewModel;
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void deletePackage(String value) {
        if (value.equalsIgnoreCase(getString(R.string.package_flights))) {
            fragmentSellerAddPackageBinding.rlFlight.setVisibility(View.GONE);
        } else if (value.equalsIgnoreCase(getString(R.string.package_hotels))) {
            fragmentSellerAddPackageBinding.rlHotel.setVisibility(View.GONE);
        } else if (value.equalsIgnoreCase(getString(R.string.package_meals))) {
            fragmentSellerAddPackageBinding.rlMeals.setVisibility(View.GONE);
        } else if (value.equalsIgnoreCase(getString(R.string.package_sight_seeing))) {
            fragmentSellerAddPackageBinding.rlSS.setVisibility(View.GONE);
        } else if (value.equalsIgnoreCase(getString(R.string.package_site_visit))) {
            fragmentSellerAddPackageBinding.rlSV.setVisibility(View.GONE);
        }
        options.remove(value);
    }

    @Override
    public void pickImage(Boolean isLicense) {
        if (!isLicense) {
            //Upload images
            new GligarPicker().requestCode(PICKER_REQUEST_CODE).withFragment(SellerAddPackageFragment.this).limit(4).disableCamera(false).show();
        } else {
            //Upload license
            new GligarPicker().requestCode(31).withFragment(SellerAddPackageFragment.this).limit(4).disableCamera(false).show();

        }
    }

    @Override
    public void getFirstResult(JSONObject data, Boolean isLicense) {
        if ("success".equalsIgnoreCase(data.optString("response"))) {
            final int layoutId = new Random().nextInt(61) + 20;
            images.put(layoutId, pathsList[0]);
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View imageView = inflater.inflate(R.layout.item_language_view, null);
            TextView tvOptions = imageView.findViewById(R.id.tvFlight);
            ImageButton btnFlightClose = imageView.findViewById(R.id.btnFlightClose);
            try {
                //File objFile = new File(pathsList[0]);
                tvOptions.setText(data.optString("image_name"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 15, 20, 10);
            imageView.setLayoutParams(params);
            imageView.setId(layoutId);
            if (isLicense) {

                btnFlightClose.setOnClickListener(v -> {
                    fragmentSellerAddPackageBinding.rlLicenseSelected.removeView(getActivity().findViewById(layoutId));
                    images.remove(layoutId);

                });
                fragmentSellerAddPackageBinding.rlLicenseSelected.addView(imageView);
            } else {
                btnFlightClose.setOnClickListener(v -> {
                    fragmentSellerAddPackageBinding.rlImageSelected.removeView(getActivity().findViewById(layoutId));
                    images.remove(layoutId);

                });
                fragmentSellerAddPackageBinding.rlImageSelected.addView(imageView);
            }
            addId = data.optString("add_id");
            imageUpload.put(data.optString("add_id"), data.optString("path"));
            if (pathsList.length > 1) {
                for (int i = 1; i < pathsList.length; i++) {
                    sellerAddPackageViewModel.uploadMedias(reqQueue, getActivity(), pathsList[i], addId, Integer.toString(i + 1));
                }
            }

        } else {
            Toast.makeText(getActivity(), "Image upload failed. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void returnResult(JSONObject data) {
        if ("success".equalsIgnoreCase(data.optString("response"))) {
            final int layoutId = new Random().nextInt(61) + 20;
            images.put(layoutId, pathsList[0]);
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View imageView = inflater.inflate(R.layout.item_language_view, null);
            TextView tvOptions = imageView.findViewById(R.id.tvFlight);
            ImageButton btnFlightClose = imageView.findViewById(R.id.btnFlightClose);
            try {
                //File objFile = new File(pathsList[0]);
                tvOptions.setText(data.optString("image_name"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 15, 20, 10);
            imageView.setLayoutParams(params);
            imageView.setId(layoutId);
            btnFlightClose.setOnClickListener(v -> {
                fragmentSellerAddPackageBinding.rlImageSelected.removeView(getActivity().findViewById(layoutId));
                images.remove(layoutId);

            });
            fragmentSellerAddPackageBinding.rlImageSelected.addView(imageView);
            addId = data.optString("add_id");
            imageUpload.put(data.optString("add_id"), data.optString("path"));

        } else {
            Toast.makeText(getActivity(), "Image upload failed. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Arun 1");
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 30: {
                System.out.println("Arun 2");
                pathsList = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
                addImage(pathsList, false);
                break;
            }
            case 31: {
                pathsList = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
                addImage(pathsList, true);
                break;
            }
        }
    }

    @Override
    public void handleError(Throwable throwable) {

    }

    @Override
    public void addPackage() {
        String name = fragmentSellerAddPackageBinding.etName.getText().toString();
        String phone = fragmentSellerAddPackageBinding.etPhone.getText().toString();
        String whatsApp = fragmentSellerAddPackageBinding.etWhatsApp.getText().toString();
        String location = fragmentSellerAddPackageBinding.etLocation.getText().toString();
        String price = fragmentSellerAddPackageBinding.etPrice.getText().toString();
        //      String includes = fragmentSellerAddPackageBinding.etPackageDetails.getText().toString();
        String numberOfPeople = fragmentSellerAddPackageBinding.etNoOfPeoples.getText().toString();
        String moreDetails = fragmentSellerAddPackageBinding.etMore.getText().toString();




        //String language = "activitySellerAddPackageBinding.spinnerLanguage.getSelectedItem().toString()";

        String category_l_1_ShortName = "";
        if (option == 1) {
            category_l_1_ShortName = "TR";
        } else if (option == 2) {
            category_l_1_ShortName = "SS";
        } else if (option == 3) {
            category_l_1_ShortName = "Shops";
        }

        if (option == 1) {
            if (fragmentSellerAddPackageBinding.spinnerType.getSelectedItemId() != 0) {
                if (fragmentSellerAddPackageBinding.spinnerType.getSelectedItemId() == 1) {
                    // Tourist guide
                  String   country = fragmentSellerAddPackageBinding.spinnerCountry.getSelectedItem().toString();
                    String city = fragmentSellerAddPackageBinding.spinnerCity.getText().toString();
                    if (sellerAddPackageViewModel.isValidFields(name, phone, whatsApp, moreDetails, city)) {
                        String category = fragmentSellerAddPackageBinding.spinnerType.getSelectedItem().toString();
                        if (sellerAddPackageViewModel.validateSpinner(1, category)) {
                            if (sellerAddPackageViewModel.validateSpinner(2, country)) {
                                //if (sellerAddPackageViewModel.validateSpinner(3, city)) {
                                if (!hashMap.isEmpty()) {
                                    if (!imageUpload.isEmpty()) {
                                        String nameEntered = "";
                                        if (fragmentSellerAddPackageBinding.spinnerType.getSelectedItemId() == 1) {
                                            nameEntered = name;
                                        }
                                        for (String value : hashMap.values()) {
                                            lan.add(value);
                                        }
                                        String language = TextUtils.join(",", lan);
                                        String category_l_2_ShortName = "";
                                        if (getString(R.string.t_guide).equalsIgnoreCase(category)) {
                                            category_l_2_ShortName = "TG";
                                        } else if (getString(R.string.group_title).equalsIgnoreCase(category)) {
                                            category_l_2_ShortName = "TGP";
                                        } else if (getString(R.string.honeymoon_title).equalsIgnoreCase(category)) {
                                            category_l_2_ShortName = "HM";
                                        } else if (getString(R.string.family_trip_title).equalsIgnoreCase(category)) {
                                            category_l_2_ShortName = "FT";
                                        } else if (getString(R.string.supplies_menu_1).equalsIgnoreCase(category)) {
                                            category_l_2_ShortName = "WT";
                                        } else if (getString(R.string.supplies_menu_2).equalsIgnoreCase(category)) {
                                            category_l_2_ShortName = "CUS";
                                        }
                                        String services = "";

                                        if (isEditRequest == 0) {
                                            sellerAddPackageViewModel.addPackage(nameEntered, category_l_1_ShortName, category_l_2_ShortName, "", name, "", phone, country, location, whatsApp, price, numberOfPeople, moreDetails, city, language, addId, imageUpload, services,"");
                                        } else {
                                            AddServiceRequest.UpdatePackageRequest updatePackageRequest = new AddServiceRequest.UpdatePackageRequest();
                                            updatePackageRequest.setAdd_id(edtAddId);
                                            updatePackageRequest.setUser_id(String.valueOf(sellerAddPackageViewModel.getDataManager().getCurrentUserId()));
                                            updatePackageRequest.setGuide_name(nameEntered);
                                            updatePackageRequest.setLevel1_category(category_l_1_ShortName);
                                            updatePackageRequest.setLevel2_category(category_l_2_ShortName);
                                            updatePackageRequest.setLevel3_category("");
                                            updatePackageRequest.setPackageName(name);
                                            updatePackageRequest.setPackage_include("");
                                            updatePackageRequest.setPhone(phone);
                                            updatePackageRequest.setCountry(country);
                                            updatePackageRequest.setLocation(location);
                                            updatePackageRequest.setWhatsapp_phone(whatsApp);
                                            updatePackageRequest.setPrice(price);
                                            updatePackageRequest.setPeople_cnt(numberOfPeople);
                                            updatePackageRequest.setMore_details(moreDetails);
                                            updatePackageRequest.setCity(city);
                                            updatePackageRequest.setLanguage(language);
                                            updatePackageRequest.setLicence_pic_url("");
                                            updatePackageRequest.setNational_id("");
                                            updatePackageRequest.setService(services);
                                            updatePackageRequest.setImage_list(imageUpload);
                                            sellerAddPackageViewModel.upDatePackage(updatePackageRequest);
                                        }
                                    } else {
                                        Toast toast = Toast.makeText(getActivity(), "Please upload image", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }

                                } else {
                                    Toast toast = Toast.makeText(getActivity(), getString(R.string.select_language_option), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                           /* } else {
                                Toast toast = Toast.makeText(getActivity(), getString(R.string.select_city_option), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }*/
                            } else {
                                Toast toast = Toast.makeText(getActivity(), getString(R.string.select_country_option), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        } else {
                            Toast toast = Toast.makeText(getActivity(), getString(R.string.select_category_option), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getContext(), getString(R.string.fill_all_error), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } else {
                    //Tourist Group//HoneyMoon/Family
                    if (sellerAddPackageViewModel.isValid(name, phone, whatsApp, moreDetails, price, numberOfPeople, location)) {
                        String category = fragmentSellerAddPackageBinding.spinnerType.getSelectedItem().toString();
                        if (sellerAddPackageViewModel.validateSpinner(1, category)) {
                            // if (sellerAddPackageViewModel.validateSpinner(2, country)) {
                            //if (sellerAddPackageViewModel.validateSpinner(3, city)) {
                            String subCategory = fragmentSellerAddPackageBinding.spinnerSubType.getSelectedItem().toString();
                            String country = fragmentSellerAddPackageBinding.spinnerCountry.getSelectedItem().toString();
                            String city = fragmentSellerAddPackageBinding.spinnerCity.getText().toString();
                            if (sellerAddPackageViewModel.validateSpinner(4, subCategory)) {
                                if (sellerAddPackageViewModel.validateSpinner(2, country)) {
                                    if (sellerAddPackageViewModel.validateSpinner(3, city)) {
                                        if (!imageUpload.isEmpty()) {
                                            if (!options.isEmpty()) {

                                                String category_l_3_ShortName = "";
                                                if ("Domestic".equalsIgnoreCase(subCategory)) {
                                                    category_l_3_ShortName = "Domestic";
                                                }  else if ("مجموعة سياحية داخل الدولة".equalsIgnoreCase(subCategory)) {
                                                    category_l_3_ShortName = "المنزلي";
                                                }
                                                else if ("International".equalsIgnoreCase(subCategory)) {
                                                    category_l_3_ShortName = subCategory;
                                                }
                                                else if("مجموعة سياحية خارج الدولة".equalsIgnoreCase(subCategory)){
                                                    category_l_3_ShortName = "دولي";
                                                }

                                                String nameEntered = "";

                                                String category_l_2_ShortName = "";
                                                if (getString(R.string.t_guide).equalsIgnoreCase(category)) {
                                                    category_l_2_ShortName = "TG";
                                                } else if (getString(R.string.group_title).equalsIgnoreCase(category)) {
                                                    category_l_2_ShortName = "TGP";
                                                } else if (getString(R.string.honeymoon_title).equalsIgnoreCase(category)) {
                                                    category_l_2_ShortName = "HM";
                                                } else if (getString(R.string.family_trip_title).equalsIgnoreCase(category)) {
                                                    category_l_2_ShortName = "FT";
                                                } else if (getString(R.string.supplies_menu_1).equalsIgnoreCase(category)) {
                                                    category_l_2_ShortName = "WT";
                                                } else if (getString(R.string.supplies_menu_2).equalsIgnoreCase(category)) {
                                                    category_l_2_ShortName = "CUS";
                                                }

                                                String services = "";
                                                if (fragmentSellerAddPackageBinding.etServices.getText().toString().trim().length() > 0) {
                                                    services = fragmentSellerAddPackageBinding.etServices.getText().toString();
                                                }
                                                if (isEditRequest == 0) {
                                                    sellerAddPackageViewModel.addPackage(nameEntered, category_l_1_ShortName, category_l_2_ShortName, category_l_3_ShortName, name, selectedCategory, phone, country, location, whatsApp, price, numberOfPeople, moreDetails, city, "", addId, imageUpload, services,"");
                                                } else {
                                                    AddServiceRequest.UpdatePackageRequest updatePackageRequest = new AddServiceRequest.UpdatePackageRequest();
                                                    updatePackageRequest.setAdd_id(edtAddId);
                                                    updatePackageRequest.setUser_id(String.valueOf(sellerAddPackageViewModel.getDataManager().getCurrentUserId()));
                                                    updatePackageRequest.setGuide_name(nameEntered);
                                                    updatePackageRequest.setLevel1_category(category_l_1_ShortName);
                                                    updatePackageRequest.setLevel2_category(category_l_2_ShortName);
                                                    updatePackageRequest.setLevel3_category(category_l_3_ShortName);
                                                    updatePackageRequest.setPackageName(name);
                                                    updatePackageRequest.setPackage_include(selectedCategory);
                                                    updatePackageRequest.setPhone(phone);
                                                    updatePackageRequest.setCountry(country);
                                                    updatePackageRequest.setLocation(location);
                                                    updatePackageRequest.setWhatsapp_phone(whatsApp);
                                                    updatePackageRequest.setPrice(price);
                                                    updatePackageRequest.setPeople_cnt(numberOfPeople);
                                                    updatePackageRequest.setMore_details(moreDetails);
                                                    updatePackageRequest.setCity(city);
                                                    updatePackageRequest.setLanguage("");
                                                    updatePackageRequest.setLicence_pic_url("");
                                                    updatePackageRequest.setNational_id("");
                                                    updatePackageRequest.setService(services);
                                                    updatePackageRequest.setImage_list(imageUpload);
                                                    sellerAddPackageViewModel.upDatePackage(updatePackageRequest);
                                                }
                                            } else {
                                                Toast toast = Toast.makeText(getActivity(), "Please add Package", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                            }

                                        } else {
                                            Toast toast = Toast.makeText(getActivity(), "Please upload image", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }

                                    } else {
                                        Toast toast = Toast.makeText(getActivity(), getString(R.string.select_city_option), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }
                                } else {
                                    Toast toast = Toast.makeText(getActivity(), getString(R.string.select_country_option), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            } else {
                                Toast toast = Toast.makeText(getActivity(), getString(R.string.select_sub_category_option), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }

                        } else {
                            Toast toast = Toast.makeText(getActivity(), getString(R.string.select_category_option), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getActivity(), getString(R.string.fill_all_error), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }
        } else if (option == 2) {
            //Others
            if (sellerAddPackageViewModel.isValid(name, phone, whatsApp, moreDetails, price, numberOfPeople, location)) {
                String category = fragmentSellerAddPackageBinding.spinnerType.getSelectedItem().toString();
                String country = fragmentSellerAddPackageBinding.spinnerCountry.getSelectedItem().toString();
                String city = fragmentSellerAddPackageBinding.spinnerCity.getText().toString();
                if (sellerAddPackageViewModel.validateSpinner(1, category)) {
                    if (sellerAddPackageViewModel.validateSpinner(2, country)) {
                        if (sellerAddPackageViewModel.validateSpinner(3, city)) {
                            if (!imageUpload.isEmpty()) {
                                if (!options.isEmpty()) {
                                    String nameEntered = "";
                                    String category_l_2_ShortName = "";
                                     if (getString(R.string.supplies_menu_1).equalsIgnoreCase(category)) {
                                        category_l_2_ShortName = "WT";
                                    } else if (getString(R.string.supplies_menu_2).equalsIgnoreCase(category)) {
                                        category_l_2_ShortName = "CUS";
                                    }
                                    String service = "";
                                    if (fragmentSellerAddPackageBinding.etServices.getText().toString().length() > 1) {
                                        service = fragmentSellerAddPackageBinding.etServices.getText().toString();
                                    }
                                    String nationalId ="";
                                    if (fragmentSellerAddPackageBinding.etNationalId.getText().toString().length() > 1) {
                                        nationalId = fragmentSellerAddPackageBinding.etNationalId.getText().toString();
                                    }
                                    if (isEditRequest == 0) {
                                        sellerAddPackageViewModel.addPackage(nameEntered, category_l_1_ShortName, category_l_2_ShortName, "", name, selectedCategory, phone, country, location, whatsApp, price, numberOfPeople, moreDetails, city, "", addId, imageUpload, service,nationalId);
                                    } else {

                                        AddServiceRequest.UpdatePackageRequest updatePackageRequest = new AddServiceRequest.UpdatePackageRequest();
                                        updatePackageRequest.setAdd_id(edtAddId);
                                        updatePackageRequest.setUser_id(String.valueOf(sellerAddPackageViewModel.getDataManager().getCurrentUserId()));
                                        updatePackageRequest.setGuide_name(nameEntered);
                                        updatePackageRequest.setLevel1_category(category_l_1_ShortName);
                                        updatePackageRequest.setLevel2_category(category_l_2_ShortName);
                                        updatePackageRequest.setLevel3_category("");
                                        updatePackageRequest.setPackageName(name);
                                        updatePackageRequest.setPackage_include(selectedCategory);
                                        updatePackageRequest.setPhone(phone);
                                        updatePackageRequest.setCountry(country);
                                        updatePackageRequest.setLocation(location);
                                        updatePackageRequest.setWhatsapp_phone(whatsApp);
                                        updatePackageRequest.setPrice(price);
                                        updatePackageRequest.setPeople_cnt(numberOfPeople);
                                        updatePackageRequest.setMore_details(moreDetails);
                                        updatePackageRequest.setCity(city);
                                        updatePackageRequest.setLanguage("");
                                        updatePackageRequest.setLicence_pic_url("");
                                        updatePackageRequest.setNational_id(nationalId);
                                        updatePackageRequest.setService(service);
                                        updatePackageRequest.setImage_list(imageUpload);
                                        sellerAddPackageViewModel.upDatePackage(updatePackageRequest);
                                    }
                                } else {
                                    Toast toast = Toast.makeText(getActivity(), "Please add Package", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }

                            } else {
                                Toast toast = Toast.makeText(getActivity(), "Please upload image", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        } else {
                            Toast toast = Toast.makeText(getActivity(), getString(R.string.select_city_option), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getActivity(), getString(R.string.select_country_option), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.select_category_option), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.fill_all_error), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else if (option == 3) {

            if (sellerAddPackageViewModel.isValid(name, phone, whatsApp, moreDetails, price, numberOfPeople, location)) {
                String country = fragmentSellerAddPackageBinding.spinnerCountry.getSelectedItem().toString();
                String city = fragmentSellerAddPackageBinding.spinnerCity.getText().toString();
                    if (sellerAddPackageViewModel.validateSpinner(2, country)) {
                        if (sellerAddPackageViewModel.validateSpinner(3, city)) {
                            if (!imageUpload.isEmpty()) {
                                if (!options.isEmpty()) {
                                    String nameEntered = "";
                                    String category_l_2_ShortName = "";
                                    String category_l_3_ShortName = "";
                                    String service = "";
                                    if (fragmentSellerAddPackageBinding.etServices.getText().toString().length() > 1) {
                                        service = fragmentSellerAddPackageBinding.etServices.getText().toString();
                                    }
                                    if (isEditRequest == 0) {
                                        sellerAddPackageViewModel.addPackage(nameEntered, category_l_1_ShortName, category_l_2_ShortName, category_l_3_ShortName, name, selectedCategory, phone, country, location, whatsApp, price, numberOfPeople, moreDetails, city, "", addId, imageUpload, service,"");
                                    } else {

                                        AddServiceRequest.UpdatePackageRequest updatePackageRequest = new AddServiceRequest.UpdatePackageRequest();
                                        updatePackageRequest.setAdd_id(edtAddId);
                                        updatePackageRequest.setUser_id(String.valueOf(sellerAddPackageViewModel.getDataManager().getCurrentUserId()));
                                        updatePackageRequest.setGuide_name(nameEntered);
                                        updatePackageRequest.setLevel1_category(category_l_1_ShortName);
                                        updatePackageRequest.setLevel2_category(category_l_2_ShortName);
                                        updatePackageRequest.setLevel3_category(category_l_3_ShortName);
                                        updatePackageRequest.setPackageName(name);
                                        updatePackageRequest.setPackage_include(selectedCategory);
                                        updatePackageRequest.setPhone(phone);
                                        updatePackageRequest.setCountry(country);
                                        updatePackageRequest.setLocation(location);
                                        updatePackageRequest.setWhatsapp_phone(whatsApp);
                                        updatePackageRequest.setPrice(price);
                                        updatePackageRequest.setPeople_cnt(numberOfPeople);
                                        updatePackageRequest.setMore_details(moreDetails);
                                        updatePackageRequest.setCity(city);
                                        updatePackageRequest.setLanguage("");
                                        updatePackageRequest.setLicence_pic_url("");
                                        updatePackageRequest.setNational_id("");
                                        updatePackageRequest.setService(service);
                                        updatePackageRequest.setImage_list(imageUpload);
                                        sellerAddPackageViewModel.upDatePackage(updatePackageRequest);
                                    }
                                } else {
                                    Toast toast = Toast.makeText(getActivity(), "Please add Package", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }

                            } else {
                                Toast toast = Toast.makeText(getActivity(), "Please upload image", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        } else {
                            Toast toast = Toast.makeText(getActivity(), getString(R.string.select_city_option), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getActivity(), getString(R.string.select_country_option), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

            } else {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.fill_all_error), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        ///

    }

    @Override
    public void openSellerHome() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void setSeller() {
        getBaseActivity().onFragmentDetached(TAG);
        startActivity(new Intent(getActivity(), SplashActivity.class));
    }

    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    @Override
    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_dialog_options, null);
        alertDialog.setView(customLayout);
        AlertDialog OptionDialog = alertDialog.create();
        OptionDialog.show();
        LinearLayout flight_layout, hotel_layout, meals_layout, ss_layout, sv_layout;
        flight_layout = customLayout.findViewById(R.id.flight_layout);
        hotel_layout = customLayout.findViewById(R.id.hotel_layout);
        meals_layout = customLayout.findViewById(R.id.meals_layout);
        ss_layout = customLayout.findViewById(R.id.ss_layout);
        sv_layout = customLayout.findViewById(R.id.sv_layout);

        flight_layout.setOnClickListener(view -> {
            addToList(getString(R.string.package_flights));
            OptionDialog.dismiss();
        });
        hotel_layout.setOnClickListener(view -> {
            addToList(getString(R.string.package_hotels));
            OptionDialog.dismiss();
        });
        meals_layout.setOnClickListener(view -> {
            addToList(getString(R.string.package_meals));
            OptionDialog.dismiss();
        });
        ss_layout.setOnClickListener(view -> {
            addToList(getString(R.string.package_sight_seeing));
            OptionDialog.dismiss();
        });
        sv_layout.setOnClickListener(view -> {
            addToList(getString(R.string.package_site_visit));
            OptionDialog.dismiss();
        });

        ImageButton close = customLayout.findViewById(R.id.navClose);
        close.setOnClickListener(view -> OptionDialog.dismiss());
    }

    @Override
    public void showLanguages() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_language_options, null);
        alertDialog.setView(customLayout);
        AlertDialog OptionDialog = alertDialog.create();
        OptionDialog.show();
        LinearLayout llLanguageList = customLayout.findViewById(R.id.llLanguageList);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String[] languageArray = getResources().getStringArray(R.array.language_list);
        for (int i = 1; i < languageArray.length; i++) {
            TextView languageDisplay = new TextView(getActivity());
            languageDisplay.setTextAppearance(getActivity(), R.style.CustomTextStyle);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 30, 10, 30);
            languageDisplay.setLayoutParams(params);
            languageDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            languageDisplay.setText(languageArray[i]);
            languageDisplay.setId(i);
            int finalI = i;
            languageDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addLanguage(languageDisplay.getText().toString(), finalI);
                    OptionDialog.dismiss();
                }
            });
            //languageDisplay.setOnClickListener(v -> addLanguage(languageDisplay.getText().toString(), finalI));
            View ruler = new View(getActivity());
            LinearLayout.LayoutParams paramsView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            paramsView.setMargins(20, 30, 10, 20);
            ruler.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            ruler.setLayoutParams(paramsView);
            llLanguageList.addView(languageDisplay);
            llLanguageList.addView(ruler);
        }

        ImageButton close = customLayout.findViewById(R.id.navClose);
        close.setOnClickListener(view -> OptionDialog.dismiss());
    }

    @Override
    public void showErrorAlert(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sellerAddPackageViewModel.setNavigator(this);
        option = getArguments().getInt(GD);
        guideData = getArguments().getString(DATA_BUNDLE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentSellerAddPackageBinding = getViewDataBinding();
        options = new ArrayList<>();
        lan = new ArrayList<>();
        images = new HashMap<>();
        hashMap = new HashMap<>();
        imageUpload = new HashMap<>();

      /*  fragmentSellerAddPackageBinding.spinnerSubType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){
                    fragmentSellerAddPackageBinding.tvDomesticCity.setVisibility(View.VISIBLE);
                    fragmentSellerAddPackageBinding.spinnerCountry.setVisibility(View.GONE);
                    country=fragmentSellerAddPackageBinding.tvDomesticCity.getText().toString();
                }
                if(position==2){
                    fragmentSellerAddPackageBinding.tvDomesticCity.setVisibility(View.GONE);
                    fragmentSellerAddPackageBinding.spinnerCountry.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        fragmentSellerAddPackageBinding.etServices.setOnClickListener(v -> {

            ServicesItemAdapter servicesItemAdapter;
            String[] itemServices = new String[0];
            List<ServicesChecklistItems> servicesChecklist = new ArrayList<>();
            ArrayList<String> selectedServiceList = new ArrayList<>();
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_services);

            RecyclerView recyclerServices = dialog.findViewById(R.id.recycler_services);

                    /*Travel*/
                    if(option ==1){
                        itemServices = getResources().getStringArray(R.array.item_services);
                    }
                    /*Supplier*/
                    else if(option==2){
                        if(fragmentSellerAddPackageBinding.spinnerType.getSelectedItemPosition()==1) {
                            itemServices = getResources().getStringArray(R.array.supplier_services);
                        }
                        /*Cruise*/
                        else {
                            itemServices = getResources().getStringArray(R.array.cruises_services);
                        }
                    }
                    /*Shops*/
                    else if(option==3){
                        itemServices = getResources().getStringArray(R.array.shops_services);
                    }



            for (int i = 0; i < itemServices.length; i++) {

                ServicesChecklistItems servicesChecklistItems = new ServicesChecklistItems(itemServices[i], false);
                servicesChecklist.add(servicesChecklistItems);
            }

            servicesItemAdapter = new ServicesItemAdapter(getActivity(), servicesChecklist);

            recyclerServices.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerServices.setAdapter(servicesItemAdapter);

            servicesItemAdapter.setOnItemClickListener((view1, position, data) -> selectedServiceList.add(data));

            Button dialogButtonCancel = dialog.findViewById(R.id.btn_cancel);
            // if button is clicked, close the custom dialog
            dialogButtonCancel.setOnClickListener(v1 -> dialog.dismiss());

            Button dialogButtonSave = dialog.findViewById(R.id.btn_submit);
            dialogButtonSave.setOnClickListener(v1 -> {
                String service = "";
                for (String name : selectedServiceList) {

                    if (!service.equalsIgnoreCase("")) {
                        service = service + ", " + name;
                    } else {
                        service = name;
                    }
                }
                fragmentSellerAddPackageBinding.etServices.setText(service);
                dialog.dismiss();
            });
            dialog.show();
        });
        reqQueue = Volley.newRequestQueue(getActivity());
        hideKeyboard();
        setSpinner(option);

        if (!guideData.equalsIgnoreCase("")) {
            isEditRequest = 1;
            setUp();
        }
    }

    private void setUp() {
        try {

            JSONObject jsonObj = new JSONObject(guideData);
            edtAddId = jsonObj.optString("id");
            fragmentSellerAddPackageBinding.etWhatsApp.setText(jsonObj.optString("whatsApp"));
            fragmentSellerAddPackageBinding.etPhone.setText(jsonObj.optString("phone"));
            fragmentSellerAddPackageBinding.etName.setText(jsonObj.optString("package"));
            fragmentSellerAddPackageBinding.etMore.setText(jsonObj.optString("details"));
            fragmentSellerAddPackageBinding.etPrice.setText(jsonObj.optString("price"));
            if (!jsonObj.optString("country").equalsIgnoreCase("")) {
                List<String> countryList = Arrays.asList(getResources().getStringArray(R.array.country_list));
                fragmentSellerAddPackageBinding.spinnerCountry.setSelection(
                        countryList.indexOf(jsonObj.optString("country")));
            }
            fragmentSellerAddPackageBinding.spinnerCity.setText(jsonObj.optString("city"));
            String level1_Category = jsonObj.optString("level1_category");
            String level2_Category = jsonObj.optString("level2_category");
            String level3_Category = jsonObj.optString("level3_category");

            if (jsonObj.optString("licence_pic_url").trim().length() > 0) {
                List<String> licenseList = Arrays.asList(jsonObj.optString("licence_pic_url").split(","));
                addLayout(licenseList, "licenseList");
            }

            List<String> imList = Arrays.asList(jsonObj.optString("images").split("\t"));
            addLayout(imList, "imageList");
            for (int i = 0; i < imList.size(); i++) {
                imageUpload.put(edtAddId, imList.get(i));
            }
            fragmentSellerAddPackageBinding.etServices.setText(jsonObj.optString("services"));
            fragmentSellerAddPackageBinding.etLocation.setText(jsonObj.optString("location"));
            fragmentSellerAddPackageBinding.etNoOfPeoples.setText(jsonObj.optString("no_of_people"));
            List<String> packageList = Arrays.asList(jsonObj.optString("package_include").split(","));
            options.addAll(packageList);
            addPackageList(packageList);

            int selectedCategory = 0;
            if (level1_Category.equalsIgnoreCase("TR")) {
                option = 1;
                setSpinner(1);
                if (level2_Category.equalsIgnoreCase("TG")) {
                    selectedCategory = 1;
                } else if (level2_Category.equalsIgnoreCase("TGP")) {
                    selectedCategory = 2;
                    if ((level3_Category.equalsIgnoreCase("Domestic") || (level3_Category.equalsIgnoreCase("المنزلي")))) {
                        fragmentSellerAddPackageBinding.spinnerSubType.setSelection(1);
                    } else if ((level3_Category.equalsIgnoreCase("International")) || (level3_Category.equalsIgnoreCase("دولي"))) {
                        fragmentSellerAddPackageBinding.spinnerSubType.setSelection(2);
                    } else {
                        fragmentSellerAddPackageBinding.spinnerSubType.setSelection(0);
                    }
                } else if (level2_Category.equalsIgnoreCase("HM")) {
                    if ((level3_Category.equalsIgnoreCase("Domestic") || (level3_Category.equalsIgnoreCase("المنزلي")))) {
                        fragmentSellerAddPackageBinding.spinnerSubType.setSelection(1);
                    } else if ((level3_Category.equalsIgnoreCase("International")) || (level3_Category.equalsIgnoreCase("دولي"))) {
                        fragmentSellerAddPackageBinding.spinnerSubType.setSelection(2);
                    } else {
                        fragmentSellerAddPackageBinding.spinnerSubType.setSelection(0);
                    }
                    selectedCategory = 3;
                } else if (level2_Category.equalsIgnoreCase("FT")) {
                    selectedCategory = 4;
                    if ((level3_Category.equalsIgnoreCase("Domestic") || (level3_Category.equalsIgnoreCase("المنزلي")))) {
                        fragmentSellerAddPackageBinding.spinnerSubType.setSelection(1);
                    } else if ((level3_Category.equalsIgnoreCase("International")) || (level3_Category.equalsIgnoreCase("دولي"))) {
                        fragmentSellerAddPackageBinding.spinnerSubType.setSelection(2);
                    } else {
                        fragmentSellerAddPackageBinding.spinnerSubType.setSelection(0);
                    }
                }
                fragmentSellerAddPackageBinding.spinnerType.setSelection(selectedCategory);

            } else if (level1_Category.equalsIgnoreCase("SS")) {
                option = 2;
                setSpinner(2);
                if (level2_Category.equalsIgnoreCase("WT")) {
                    selectedCategory = 1;
                } else if (level2_Category.equalsIgnoreCase("CUS")) {
                    selectedCategory = 2;
                }
                fragmentSellerAddPackageBinding.spinnerType.setSelection(selectedCategory);
            } else if (level1_Category.equalsIgnoreCase("Shops")) {
                option = 3;
                setSpinner(3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPackageList(List<String> packageList) {

        for (int i = 0; i < packageList.size(); i++) {
            if (packageList.get(i).equalsIgnoreCase(getString(R.string.package_flights))) {
                fragmentSellerAddPackageBinding.rlFlight.setVisibility(View.VISIBLE);
            } else if (packageList.get(i).equalsIgnoreCase(getString(R.string.package_hotels))) {
                fragmentSellerAddPackageBinding.rlHotel.setVisibility(View.VISIBLE);
            } else if (packageList.get(i).equalsIgnoreCase(getString(R.string.package_meals))) {
                fragmentSellerAddPackageBinding.rlMeals.setVisibility(View.VISIBLE);
            } else if (packageList.get(i).equalsIgnoreCase(getString(R.string.package_sight_seeing))) {
                fragmentSellerAddPackageBinding.rlSS.setVisibility(View.VISIBLE);
            } else if (packageList.get(i).equalsIgnoreCase(getString(R.string.package_site_visit))) {
                fragmentSellerAddPackageBinding.rlSV.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addLayout(List<String> imList, String listType) {

        for (int i = 0; i < imList.size(); i++) {
            final int layoutId = new Random().nextInt(61) + 20;
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View imageView = inflater.inflate(R.layout.item_language_view, null);
            TextView tvOptions = imageView.findViewById(R.id.tvFlight);
            ImageButton btnFlightClose = imageView.findViewById(R.id.btnFlightClose);
            try {
                //File objFile = new File(pathsList[0]);
                tvOptions.setText(imList.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 15, 20, 10);
            imageView.setLayoutParams(params);
            imageView.setId(layoutId);
            btnFlightClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listType.equalsIgnoreCase("imageList")) {
                        fragmentSellerAddPackageBinding.rlImageSelected.removeView(getActivity().findViewById(layoutId));
                    } else if (listType.equalsIgnoreCase("licenseList")) {
                        fragmentSellerAddPackageBinding.rlLicenseSelected.removeView(getActivity().findViewById(layoutId));
                    }
                    images.remove(layoutId);

                }
            });
            if (listType.equalsIgnoreCase("imageList")) {
                fragmentSellerAddPackageBinding.rlImageSelected.addView(imageView);
            } else if (listType.equalsIgnoreCase("licenseList")) {
                fragmentSellerAddPackageBinding.rlLicenseSelected.addView(imageView);
            }


        }

    }

    private void setSpinner(int option) {
        category = fragmentSellerAddPackageBinding.spinnerType;
        if (option == 1) {
            initialiseCategorySpinnerTraveler();
        } else if (option == 2) {
            initialiseCategorySpinnerSupplier();
        } else {
            initiateShopsAddView();
        }
    }

    private void initiateShopsAddView() {

        fragmentSellerAddPackageBinding.tvCategory.setVisibility(View.GONE);
        fragmentSellerAddPackageBinding.spinnerType.setVisibility(View.GONE);

        fragmentSellerAddPackageBinding.tvnNationalId.setVisibility(View.GONE);
        fragmentSellerAddPackageBinding.etNationalId.setVisibility(View.GONE);

        fragmentSellerAddPackageBinding.tvUpload.setVisibility(View.VISIBLE);
        fragmentSellerAddPackageBinding.rlUploadPic.setVisibility(View.VISIBLE);

        fragmentSellerAddPackageBinding.tvSubCategory.setVisibility(View.GONE);
        fragmentSellerAddPackageBinding.spinnerSubType.setVisibility(View.GONE);

        fragmentSellerAddPackageBinding.tvName.setText(getString(R.string.package_name));

        //country
        fragmentSellerAddPackageBinding.tvCountry.setVisibility(View.VISIBLE);
        fragmentSellerAddPackageBinding.spinnerCountry.setVisibility(View.VISIBLE);
        //city
        fragmentSellerAddPackageBinding.tvCity.setVisibility(View.VISIBLE);
        fragmentSellerAddPackageBinding.spinnerCity.setVisibility(View.VISIBLE);

        fragmentSellerAddPackageBinding.llLocation.setVisibility(View.VISIBLE);
        fragmentSellerAddPackageBinding.tvNoOfPeoples.setVisibility(View.VISIBLE);
        fragmentSellerAddPackageBinding.etNoOfPeoples.setVisibility(View.VISIBLE);
        fragmentSellerAddPackageBinding.rlPackage.setVisibility(View.VISIBLE);
        fragmentSellerAddPackageBinding.rlLanguage.setVisibility(View.GONE);
        fragmentSellerAddPackageBinding.rlLanguageSelected.setVisibility(View.GONE);
    }

    private void initialiseCategorySpinnerSupplier() {

        fragmentSellerAddPackageBinding.tvName.setText(getString(R.string.package_name));
        fragmentSellerAddPackageBinding.tvCity.setVisibility(View.GONE);
        fragmentSellerAddPackageBinding.spinnerCity.setVisibility(View.GONE);

        fragmentSellerAddPackageBinding.tvUpload.setVisibility(View.INVISIBLE);
        fragmentSellerAddPackageBinding.rlUploadPic.setVisibility(View.INVISIBLE);

        fragmentSellerAddPackageBinding.tvnNationalId.setVisibility(View.VISIBLE);
        fragmentSellerAddPackageBinding.etNationalId.setVisibility(View.VISIBLE);

        ArrayAdapter<String> spinnerSuppliesAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.supply_options));
        category.setAdapter(spinnerSuppliesAdapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                fragmentSellerAddPackageBinding.tvName.setText(getString(R.string.package_name));
                //country
                fragmentSellerAddPackageBinding.tvCountry.setVisibility(View.VISIBLE);
                fragmentSellerAddPackageBinding.spinnerCountry.setVisibility(View.VISIBLE);
                //city
                fragmentSellerAddPackageBinding.tvCity.setVisibility(View.VISIBLE);
                fragmentSellerAddPackageBinding.spinnerCity.setVisibility(View.VISIBLE);


                fragmentSellerAddPackageBinding.llLocation.setVisibility(View.VISIBLE);
                fragmentSellerAddPackageBinding.tvNoOfPeoples.setVisibility(View.VISIBLE);
                fragmentSellerAddPackageBinding.etNoOfPeoples.setVisibility(View.VISIBLE);
                fragmentSellerAddPackageBinding.rlPackage.setVisibility(View.VISIBLE);
                fragmentSellerAddPackageBinding.rlLanguage.setVisibility(View.GONE);
                fragmentSellerAddPackageBinding.rlLanguageSelected.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initialiseCategorySpinnerTraveler() {

        fragmentSellerAddPackageBinding.tvnNationalId.setVisibility(View.GONE);
        fragmentSellerAddPackageBinding.etNationalId.setVisibility(View.GONE);

        fragmentSellerAddPackageBinding.tvUpload.setVisibility(View.VISIBLE);
        fragmentSellerAddPackageBinding.rlUploadPic.setVisibility(View.VISIBLE);

        ArrayAdapter<String> spinnerTravelAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.travel_options));
        category.setAdapter(spinnerTravelAdapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (arg2 == 1) {
                    fragmentSellerAddPackageBinding.tvName.setText("Name");
                    fragmentSellerAddPackageBinding.llLocation.setVisibility(View.GONE);
                    fragmentSellerAddPackageBinding.tvNoOfPeoples.setVisibility(View.GONE);
                    fragmentSellerAddPackageBinding.etNoOfPeoples.setVisibility(View.GONE);
                    fragmentSellerAddPackageBinding.rlPackage.setVisibility(View.GONE);
                    fragmentSellerAddPackageBinding.rlLanguage.setVisibility(View.VISIBLE);
                    fragmentSellerAddPackageBinding.rlLanguageSelected.setVisibility(View.VISIBLE);
                    fragmentSellerAddPackageBinding.tvSubCategory.setVisibility(View.GONE);
                    fragmentSellerAddPackageBinding.spinnerSubType.setVisibility(View.GONE);

                    fragmentSellerAddPackageBinding.tvServices.setVisibility(View.GONE);
                    fragmentSellerAddPackageBinding.etServices.setVisibility(View.GONE);
                    //country
                    fragmentSellerAddPackageBinding.tvCountry.setVisibility(View.VISIBLE);
                    fragmentSellerAddPackageBinding.spinnerCountry.setVisibility(View.VISIBLE);

                    //city
                    fragmentSellerAddPackageBinding.tvCity.setVisibility(View.VISIBLE);
                    fragmentSellerAddPackageBinding.spinnerCity.setVisibility(View.VISIBLE);


                } else {
                    fragmentSellerAddPackageBinding.tvSubCategory.setVisibility(View.VISIBLE);
                    fragmentSellerAddPackageBinding.spinnerSubType.setVisibility(View.VISIBLE);

                    fragmentSellerAddPackageBinding.tvName.setText(getString(R.string.package_name));
                    //country
                    fragmentSellerAddPackageBinding.tvCountry.setVisibility(View.VISIBLE);
                    fragmentSellerAddPackageBinding.spinnerCountry.setVisibility(View.VISIBLE);
                    //city
                    fragmentSellerAddPackageBinding.tvCity.setVisibility(View.VISIBLE);
                    fragmentSellerAddPackageBinding.spinnerCity.setVisibility(View.VISIBLE);

                    fragmentSellerAddPackageBinding.tvServices.setVisibility(View.VISIBLE);
                    fragmentSellerAddPackageBinding.etServices.setVisibility(View.VISIBLE);

                    fragmentSellerAddPackageBinding.llLocation.setVisibility(View.VISIBLE);
                    fragmentSellerAddPackageBinding.tvNoOfPeoples.setVisibility(View.VISIBLE);
                    fragmentSellerAddPackageBinding.etNoOfPeoples.setVisibility(View.VISIBLE);
                    fragmentSellerAddPackageBinding.rlPackage.setVisibility(View.VISIBLE);
                    fragmentSellerAddPackageBinding.rlLanguage.setVisibility(View.GONE);
                    fragmentSellerAddPackageBinding.rlLanguageSelected.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void addLanguage(String value, int id) {
        if (hashMap.isEmpty()) {
            int layoutId = 1000 + id;
            hashMap.put(layoutId, value);
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View languageView = inflater.inflate(R.layout.item_language_view, null);
            TextView tvOptions = languageView.findViewById(R.id.tvFlight);
            ImageButton btnFlightClose = languageView.findViewById(R.id.btnFlightClose);
            tvOptions.setText(value);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 15, 20, 10);
            languageView.setLayoutParams(params);
            languageView.setId(layoutId);
            btnFlightClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentSellerAddPackageBinding.rlLanguageSelected.removeView(getActivity().findViewById(layoutId));
                    hashMap.remove(layoutId);

                }
            });
            fragmentSellerAddPackageBinding.rlLanguageSelected.addView(languageView);
        } else {
            int layoutId = 1000 + id;
            if (!hashMap.containsKey(layoutId)) {
                hashMap.put(layoutId, value);
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View languageView = inflater.inflate(R.layout.item_language_view, null);
                TextView tvOptions = languageView.findViewById(R.id.tvFlight);
                ImageButton btnFlightClose = languageView.findViewById(R.id.btnFlightClose);
                tvOptions.setText(value);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(20, 15, 20, 10);
                languageView.setLayoutParams(params);
                languageView.setId(layoutId);
                btnFlightClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentSellerAddPackageBinding.rlLanguageSelected.removeView(getActivity().findViewById(layoutId));

                    }
                });
                fragmentSellerAddPackageBinding.rlLanguageSelected.addView(languageView);
            }

        }

    }

    private void addImage(String[] data, Boolean isLicense) {
        sellerAddPackageViewModel.InitialMediaUploadRequest(reqQueue, getActivity(), data[0], "", Integer.toString(1), isLicense);
    }

    private void addToList(String option) {

        if (option.equalsIgnoreCase(getString(R.string.package_flights))) {
            fragmentSellerAddPackageBinding.rlFlight.setVisibility(View.VISIBLE);
        } else if (option.equalsIgnoreCase(getString(R.string.package_hotels))) {
            fragmentSellerAddPackageBinding.rlHotel.setVisibility(View.VISIBLE);
        } else if (option.equalsIgnoreCase(getString(R.string.package_meals))) {
            fragmentSellerAddPackageBinding.rlMeals.setVisibility(View.VISIBLE);
        } else if (option.equalsIgnoreCase(getString(R.string.package_sight_seeing))) {
            fragmentSellerAddPackageBinding.rlSS.setVisibility(View.VISIBLE);
        } else if (option.equalsIgnoreCase(getString(R.string.package_site_visit))) {
            fragmentSellerAddPackageBinding.rlSV.setVisibility(View.VISIBLE);
        }
        if (options.size() == 0) {
            options.add(option);
        } else {
            if (!options.contains(option)) {
                options.add(option);
            }
        }
        setPackageData();
    }


    private void setPackageData() {
        selectedCategory = TextUtils.join(",", options);
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    getBaseActivity().onFragmentDetached(TAG);
                    return true;
                }
                return false;
            }
        });

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

    private void showHome() {
        for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

}
