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
import com.project.maqdoom.databinding.FragmentSellerAddPackageBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.services.ServicesChecklistItems;
import com.project.maqdoom.ui.services.ServicesItemAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SellerAddPackageFragment extends BaseFragment<FragmentSellerAddPackageBinding, SellerAddPackageViewModel> implements SellerAddPackageNavigator {

    public static final String TAG = SellerAddPackageFragment.class.getSimpleName();
    public static String GD;
    String guideData = "";

    private static int option;
    Spinner category;
    List<String> options, lan;
    String selectedCategory = "";
    java.util.HashMap<Integer, String> hashMap, images;
    java.util.HashMap<String, String> imageUpload;
    int PICKER_REQUEST_CODE = 30;
    String addId = "";
    String pathsList[];
    RequestQueue reqQueue;

    @Inject
    ViewModelProviderFactory factory;
    private SellerAddPackageViewModel sellerAddPackageViewModel;
    FragmentSellerAddPackageBinding fragmentSellerAddPackageBinding;

    public static SellerAddPackageFragment newInstance(int data) {
        Bundle args = new Bundle();
        args.putInt(GD, data);
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
        if(!isLicense) {
            //Upload images
            new GligarPicker().requestCode(PICKER_REQUEST_CODE).withFragment(SellerAddPackageFragment.this).limit(4).show();
       }else {
            //Upload license
            new GligarPicker().requestCode(31).withFragment(SellerAddPackageFragment.this).limit(4).show();

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
            if(isLicense){

                btnFlightClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentSellerAddPackageBinding.rlLicenseSelected.removeView(getActivity().findViewById(layoutId));
                        if (images.containsKey(layoutId)) {
                            images.remove(layoutId);
                        }

                    }
                });
                fragmentSellerAddPackageBinding.rlLicenseSelected.addView(imageView);
            }else {
                btnFlightClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentSellerAddPackageBinding.rlImageSelected.removeView(getActivity().findViewById(layoutId));
                        if (images.containsKey(layoutId)) {
                            images.remove(layoutId);
                        }

                    }
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
            btnFlightClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentSellerAddPackageBinding.rlImageSelected.removeView(getActivity().findViewById(layoutId));
                    if (images.containsKey(layoutId)) {
                        images.remove(layoutId);
                    }

                }
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
                addImage(pathsList,false);
                break;
            }
            case 31:{
                pathsList = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
                addImage(pathsList,true);
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
 //       String includes = fragmentSellerAddPackageBinding.etPackageDetails.getText().toString();
        String numberOfPeople = fragmentSellerAddPackageBinding.etNoOfPeoples.getText().toString();
        String moreDetails = fragmentSellerAddPackageBinding.etMore.getText().toString();

        String category = fragmentSellerAddPackageBinding.spinnerType.getSelectedItem().toString();
        String country = fragmentSellerAddPackageBinding.spinnerCountry.getSelectedItem().toString();
        String city = fragmentSellerAddPackageBinding.spinnerCity.getText().toString();
        //String language = "activitySellerAddPackageBinding.spinnerLanguage.getSelectedItem().toString()";


        String category_l_1_ShortName = "";
        if (option == 1) {
            category_l_1_ShortName = "TR";
        } else if (option == 2) {
            category_l_1_ShortName = "SS";
        }

        if (option == 1) {
            if (fragmentSellerAddPackageBinding.spinnerType.getSelectedItemId() != 0) {
                if (fragmentSellerAddPackageBinding.spinnerType.getSelectedItemId() == 1) {
                    // Tourist guide
                    if (sellerAddPackageViewModel.isValidFields(name, phone, whatsApp, moreDetails,city)) {
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
                                        sellerAddPackageViewModel.addPackage(nameEntered, category_l_1_ShortName, category_l_2_ShortName, "", name, "", phone, country, location, whatsApp, price, numberOfPeople, moreDetails, city, language,addId,imageUpload);
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
                                Toast toast =Toast.makeText(getActivity(), getString(R.string.select_country_option), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        } else {
                            Toast toast =Toast.makeText(getActivity(), getString(R.string.select_category_option), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getContext(), getString(R.string.fill_all_error), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } else if (fragmentSellerAddPackageBinding.spinnerType.getSelectedItemId() == 2) {
                    //Tourist Group
                    if (sellerAddPackageViewModel.isValid(name, phone, whatsApp, moreDetails, price, numberOfPeople,location)) {
                        if (sellerAddPackageViewModel.validateSpinner(1, category)) {
                           // if (sellerAddPackageViewModel.validateSpinner(2, country)) {
                                //if (sellerAddPackageViewModel.validateSpinner(3, city)) {
                                    String subCategory = fragmentSellerAddPackageBinding.spinnerSubType.getSelectedItem().toString();
                                    if (sellerAddPackageViewModel.validateSpinner(4, subCategory)) {
                                        if (!imageUpload.isEmpty()) {
                                            if (!options.isEmpty()) {
                                                String category_l_3_ShortName = "";
                                                if (getString(R.string.tourist_inside).equalsIgnoreCase(subCategory)) {
                                                    category_l_3_ShortName = "IC";
                                                } else if (getString(R.string.tourist_outside).equalsIgnoreCase(subCategory)) {
                                                    category_l_3_ShortName = "OC";
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
                                                sellerAddPackageViewModel.addPackage(nameEntered, category_l_1_ShortName, category_l_2_ShortName, category_l_3_ShortName, name, selectedCategory, phone, country, location, whatsApp, price, numberOfPeople, moreDetails, city, "", addId,imageUpload);
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
                                        Toast toast = Toast.makeText(getActivity(), getString(R.string.select_sub_category_option), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }
                               /* } else {
                                    Toast toast = Toast.makeText(getActivity(), getString(R.string.select_city_option), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }*/
                           /* } else {
                                Toast toast = Toast.makeText(getActivity(), getString(R.string.select_country_option), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }*/

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
                } else {
                    //Others
                    if (sellerAddPackageViewModel.isValid(name, phone, whatsApp, moreDetails, price, numberOfPeople,location)) {
                        if (sellerAddPackageViewModel.validateSpinner(1, category)) {
                            // if (sellerAddPackageViewModel.validateSpinner(2, country)) {
                            // if (sellerAddPackageViewModel.validateSpinner(3, city)) {
                            if (!imageUpload.isEmpty()) {
                                if (!options.isEmpty()) {
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
                                    sellerAddPackageViewModel.addPackage(nameEntered, category_l_1_ShortName, category_l_2_ShortName, "", name, selectedCategory, phone, "", location, whatsApp, price, numberOfPeople, moreDetails, "", "", addId,imageUpload);
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


                        /*}else {
                            Toast.makeText(this, getString(R.string.select_city_option), Toast.LENGTH_SHORT).show();
                        }*/
                    /*} else {
                        Toast.makeText(this, getString(R.string.select_country_option), Toast.LENGTH_SHORT).show();
                    }*/

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
        }else{
            //Others
            if (sellerAddPackageViewModel.isValid(name, phone, whatsApp, moreDetails, price, numberOfPeople,location)) {
                if (sellerAddPackageViewModel.validateSpinner(1, category)) {
                    // if (sellerAddPackageViewModel.validateSpinner(2, country)) {
                    // if (sellerAddPackageViewModel.validateSpinner(3, city)) {
                    if (!imageUpload.isEmpty()) {
                        if (!options.isEmpty()) {
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
                            String service ="";
                            if(fragmentSellerAddPackageBinding.etServices.getText().toString().length()>1){
                                service = fragmentSellerAddPackageBinding.etServices.getText().toString();
                            }
                            sellerAddPackageViewModel.addPackage(nameEntered, category_l_1_ShortName, category_l_2_ShortName, service, name, selectedCategory, phone, "", location, whatsApp, price, numberOfPeople, moreDetails, "", "", addId,imageUpload);
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


                        /*}else {
                            Toast.makeText(this, getString(R.string.select_city_option), Toast.LENGTH_SHORT).show();
                        }*/
                    /*} else {
                        Toast.makeText(this, getString(R.string.select_country_option), Toast.LENGTH_SHORT).show();
                    }*/

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
        ///




    }

    @Override
    public void openSellerHome() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void setSeller() {

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
        option = (int) getArguments().getSerializable(GD);
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

        fragmentSellerAddPackageBinding.rlServices.setOnClickListener(v -> {

            ServicesItemAdapter servicesItemAdapter;
            List<ServicesChecklistItems> servicesChecklist = new ArrayList<>();
            ArrayList<String>selectedServiceList = new ArrayList<>();
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_services);

            RecyclerView recyclerServices = dialog.findViewById(R.id.recycler_services);

            String[] itemServices = getResources().getStringArray(R.array.item_services);

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
            dialogButtonSave.setOnClickListener(v1->{
                String service = "";
                for(String name :selectedServiceList){

                    if(!service.equalsIgnoreCase("")) {
                        service = service + ", " + name;
                    }else {
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
        setSpinner();
        setUp();

    }

    private void setUp() {


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
                    if (hashMap.containsKey(layoutId)) {
                        hashMap.remove(layoutId);
                    }

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
    private void addImage(String[] data,Boolean isLicense) {
        sellerAddPackageViewModel.InitialMediaUploadRequest(reqQueue, getActivity(), data[0], "", Integer.toString(1),isLicense);
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
    private void setSpinner() {
        category = fragmentSellerAddPackageBinding.spinnerType;
        if (option == 1) {
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
                    if (arg2 == 2) {
                        fragmentSellerAddPackageBinding.tvSubCategory.setVisibility(View.VISIBLE);
                        fragmentSellerAddPackageBinding.spinnerSubType.setVisibility(View.VISIBLE);
                    } else {
                        fragmentSellerAddPackageBinding.tvSubCategory.setVisibility(View.GONE);
                        fragmentSellerAddPackageBinding.spinnerSubType.setVisibility(View.GONE);
                    }
                    if (arg2 == 1) {
                        fragmentSellerAddPackageBinding.tvName.setText("Name");
                        fragmentSellerAddPackageBinding.llLocation.setVisibility(View.GONE);
                        fragmentSellerAddPackageBinding.tvNoOfPeoples.setVisibility(View.GONE);
                        fragmentSellerAddPackageBinding.etNoOfPeoples.setVisibility(View.GONE);
                        fragmentSellerAddPackageBinding.rlPackage.setVisibility(View.GONE);
                        fragmentSellerAddPackageBinding.rlLanguage.setVisibility(View.VISIBLE);
                        fragmentSellerAddPackageBinding.rlLanguageSelected.setVisibility(View.VISIBLE);

                        //country
                        fragmentSellerAddPackageBinding.tvCountry.setVisibility(View.VISIBLE);
                        fragmentSellerAddPackageBinding.spinnerCountry.setVisibility(View.VISIBLE);

                        //city
                        fragmentSellerAddPackageBinding.tvCity.setVisibility(View.VISIBLE);
                        fragmentSellerAddPackageBinding.spinnerCity.setVisibility(View.VISIBLE);


                    } else {
                        fragmentSellerAddPackageBinding.tvName.setText("Package Name");
                        //country
                        fragmentSellerAddPackageBinding.tvCountry.setVisibility(View.GONE);
                        fragmentSellerAddPackageBinding.spinnerCountry.setVisibility(View.GONE);
                        //city
                        fragmentSellerAddPackageBinding.tvCity.setVisibility(View.GONE);
                        fragmentSellerAddPackageBinding.spinnerCity.setVisibility(View.GONE);


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
        } else {
            fragmentSellerAddPackageBinding.tvName.setText("Package Name");
            fragmentSellerAddPackageBinding.tvCity.setVisibility(View.GONE);
            fragmentSellerAddPackageBinding.spinnerCity.setVisibility(View.GONE);
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
                    fragmentSellerAddPackageBinding.tvName.setText("Package Name");
                    //country
                    fragmentSellerAddPackageBinding.tvCountry.setVisibility(View.GONE);
                    fragmentSellerAddPackageBinding.spinnerCountry.setVisibility(View.GONE);
                    //city
                    fragmentSellerAddPackageBinding.tvCity.setVisibility(View.GONE);
                    fragmentSellerAddPackageBinding.spinnerCity.setVisibility(View.GONE);


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
