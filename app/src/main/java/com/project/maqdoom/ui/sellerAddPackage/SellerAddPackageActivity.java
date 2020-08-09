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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.project.maqdoom.databinding.ActivitySellerAddPackageBinding;
import com.project.maqdoom.ui.base.BaseActivity;
import com.project.maqdoom.ui.sellerHome.SellerHomeActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;


public class SellerAddPackageActivity extends BaseActivity<ActivitySellerAddPackageBinding, SellerAddPackageViewModel> implements SellerAddPackageNavigator {

    @Inject
    ViewModelProviderFactory factory;
    private SellerAddPackageViewModel sellerAddPackageViewModel;
    private ActivitySellerAddPackageBinding activitySellerAddPackageBinding;
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

    public static Intent newIntent(Context context, int type) {
        System.out.println("Arun option type --"+option);
        option = type;
        return new Intent(context, SellerAddPackageActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_seller_add_package;
    }


    @Override
    public SellerAddPackageViewModel getViewModel() {
        sellerAddPackageViewModel = ViewModelProviders.of(this, factory).get(SellerAddPackageViewModel.class);
        return sellerAddPackageViewModel;
    }

    @Override
    public void handleError(Throwable throwable) {
    }

    @Override
    public void addPackage() {
        String name = activitySellerAddPackageBinding.etName.getText().toString();
        String phone = activitySellerAddPackageBinding.etPhone.getText().toString();
        String whatsApp = activitySellerAddPackageBinding.etWhatsApp.getText().toString();
        String location = activitySellerAddPackageBinding.etLocation.getText().toString();
        String price = activitySellerAddPackageBinding.etPrice.getText().toString();
        //String includes = "activitySellerAddPackageBinding.etPackageIncludes.getText().toString();";
        String numberOfPeople = activitySellerAddPackageBinding.etNoOfPeoples.getText().toString();
        String moreDetails = activitySellerAddPackageBinding.etMore.getText().toString();

        String category = activitySellerAddPackageBinding.spinnerType.getSelectedItem().toString();
        String country = activitySellerAddPackageBinding.spinnerCountry.getSelectedItem().toString();
        String city = activitySellerAddPackageBinding.spinnerCity.getSelectedItem().toString();
        //String language = "activitySellerAddPackageBinding.spinnerLanguage.getSelectedItem().toString()";

        String category_l_1_ShortName = "";
        if (option == 1) {
            category_l_1_ShortName = "TR";
        } else if (option == 2) {
            category_l_1_ShortName = "SS";
        }

        ///
        if (activitySellerAddPackageBinding.spinnerType.getSelectedItemId() != 0) {
            if (activitySellerAddPackageBinding.spinnerType.getSelectedItemId() == 1) {
                // Tourist guide
                if (sellerAddPackageViewModel.isValidFields(name, phone, whatsApp, moreDetails,"")) {
                    if (sellerAddPackageViewModel.validateSpinner(1, category)) {
                        if (sellerAddPackageViewModel.validateSpinner(2, country)) {
                            if (sellerAddPackageViewModel.validateSpinner(3, city)) {
                                if (!hashMap.isEmpty()) {
                                    if (!imageUpload.isEmpty()) {
                                        String nameEntered = "";
                                        if (activitySellerAddPackageBinding.spinnerType.getSelectedItemId() == 1) {
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
                                        Toast.makeText(this, "Please upload image", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(this, getString(R.string.select_language_option), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, getString(R.string.select_city_option), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.select_country_option), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.select_category_option), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.fill_all_error), Toast.LENGTH_SHORT).show();
                }
            } else if (activitySellerAddPackageBinding.spinnerType.getSelectedItemId() == 2) {
                //Tourist Group
                if (sellerAddPackageViewModel.isValid(name, phone, whatsApp, moreDetails, price, numberOfPeople,"")) {
                    if (sellerAddPackageViewModel.validateSpinner(1, category)) {
                        if (sellerAddPackageViewModel.validateSpinner(2, country)) {
                            if (sellerAddPackageViewModel.validateSpinner(3, city)) {
                                String subCategory = activitySellerAddPackageBinding.spinnerSubType.getSelectedItem().toString();
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
                                            Toast.makeText(this, "Please add Package", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(this, "Please upload image", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(this, getString(R.string.select_sub_category_option), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, getString(R.string.select_city_option), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.select_country_option), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, getString(R.string.select_category_option), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.fill_all_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                //Others
                if (sellerAddPackageViewModel.isValid(name, phone, whatsApp, moreDetails, price, numberOfPeople,"")) {
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
                                Toast.makeText(this, "Please add Package", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(this, "Please upload image", Toast.LENGTH_SHORT).show();
                        }


                        }else {
                            Toast.makeText(this, getString(R.string.select_city_option), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.select_country_option), Toast.LENGTH_SHORT).show();
                    }

                    } /*else {
                        Toast.makeText(this, getString(R.string.select_category_option), Toast.LENGTH_SHORT).show();
                    }*/
                } else {
                    Toast.makeText(this, getString(R.string.fill_all_error), Toast.LENGTH_SHORT).show();
                }
            }


        ///

        /*if (sellerAddPackageViewModel.isValid(name, phone, whatsApp, moreDetails, price, numberOfPeople,"")) {
            if (sellerAddPackageViewModel.validateSpinner(1, category)) {
                if (sellerAddPackageViewModel.validateSpinner(2, country)) {
                    if (sellerAddPackageViewModel.validateSpinner(3, city)) {
                        if (sellerAddPackageViewModel.validateSpinner(4, language)) {
                            //Go here
                            if (activitySellerAddPackageBinding.spinnerType.getSelectedItemId() == 2) {
                                String subCategory = activitySellerAddPackageBinding.spinnerSubType.getSelectedItem().toString();
                                if (sellerAddPackageViewModel.validateSpinner(4, subCategory)) {
                                    String category_l_3_ShortName = "";
                                    if (getString(R.string.tourist_inside).equalsIgnoreCase(subCategory)) {
                                        category_l_3_ShortName = "IC";
                                    } else if (getString(R.string.tourist_outside).equalsIgnoreCase(subCategory)) {
                                        category_l_3_ShortName = "OC";
                                    }

                                    String nameEntered = "";
                                    if (activitySellerAddPackageBinding.spinnerType.getSelectedItemId() == 1) {
                                        nameEntered = name;
                                    }

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
                                    sellerAddPackageViewModel.addPackage(nameEntered, category_l_1_ShortName, category_l_2_ShortName, category_l_3_ShortName, name, selectedCategory, phone, country, location, whatsApp, price, numberOfPeople, moreDetails, city, language);
                                } else {
                                    Toast.makeText(this, getString(R.string.select_sub_category_option), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                String nameEntered = "";
                                if (activitySellerAddPackageBinding.spinnerType.getSelectedItemId() == 1) {
                                    nameEntered = name;
                                }
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
                                sellerAddPackageViewModel.addPackage(nameEntered, category_l_1_ShortName, category_l_2_ShortName, "", name, selectedCategory, phone, country, location, whatsApp, price, numberOfPeople, moreDetails, city, language);

                            }
                        } else {
                            Toast.makeText(this, getString(R.string.select_language_option), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.select_city_option), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.select_country_option), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, getString(R.string.select_category_option), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.fill_all_error), Toast.LENGTH_SHORT).show();
        }*/



    @Override
    public void openSellerHome() {
        Intent intent = SellerHomeActivity.newIntent(SellerAddPackageActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void setSeller() {

    }

    @Override
    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_language_options, null);
        alertDialog.setView(customLayout);
        AlertDialog OptionDialog = alertDialog.create();
        OptionDialog.show();
        LinearLayout llLanguageList = customLayout.findViewById(R.id.llLanguageList);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String[] languageArray = getResources().getStringArray(R.array.language_list);
        for (int i = 1; i < languageArray.length; i++) {
            TextView languageDisplay = new TextView(getApplicationContext());
            languageDisplay.setTextAppearance(getApplicationContext(), R.style.CustomTextStyle);
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
            View ruler = new View(getApplicationContext());
            LinearLayout.LayoutParams paramsView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            paramsView.setMargins(20, 30, 10, 20);
            ruler.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_grey));
            ruler.setLayoutParams(paramsView);
            llLanguageList.addView(languageDisplay);
            llLanguageList.addView(ruler);
        }

        ImageButton close = customLayout.findViewById(R.id.navClose);
        close.setOnClickListener(view -> OptionDialog.dismiss());
    }

    @Override
    public void showErrorAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goBack() {
        Intent intent = SellerHomeActivity.newIntent(SellerAddPackageActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void deletePackage(String value) {
        if (value.equalsIgnoreCase(getString(R.string.package_flights))) {
            activitySellerAddPackageBinding.rlFlight.setVisibility(View.GONE);
        } else if (value.equalsIgnoreCase(getString(R.string.package_hotels))) {
            activitySellerAddPackageBinding.rlHotel.setVisibility(View.GONE);
        } else if (value.equalsIgnoreCase(getString(R.string.package_meals))) {
            activitySellerAddPackageBinding.rlMeals.setVisibility(View.GONE);
        } else if (value.equalsIgnoreCase(getString(R.string.package_sight_seeing))) {
            activitySellerAddPackageBinding.rlSS.setVisibility(View.GONE);
        } else if (value.equalsIgnoreCase(getString(R.string.package_site_visit))) {
            activitySellerAddPackageBinding.rlSV.setVisibility(View.GONE);
        }
        options.remove(value);

    }

    @Override
    public void pickImage(Boolean isLicense) {
        new GligarPicker().requestCode(PICKER_REQUEST_CODE).withActivity(this).limit(5).show();
    }

    @Override
    public void getFirstResult(JSONObject data, Boolean isLicense) {
        if ("success".equalsIgnoreCase(data.optString("response"))) {
            final int layoutId = new Random().nextInt(61) + 20;
            images.put(layoutId, pathsList[0]);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    activitySellerAddPackageBinding.rlImageSelected.removeView(findViewById(layoutId));
                    if (images.containsKey(layoutId)) {
                        images.remove(layoutId);
                    }

                }
            });
            activitySellerAddPackageBinding.rlImageSelected.addView(imageView);
            addId = data.optString("add_id");
            imageUpload.put(data.optString("add_id"), data.optString("path"));
            if (pathsList.length > 1) {
                for (int i = 1; i < pathsList.length; i++) {
                    sellerAddPackageViewModel.uploadMedias(reqQueue, getApplicationContext(), pathsList[i], addId, Integer.toString(i + 1));
                }
            }

        } else {
            Toast.makeText(this, "Image upload failed. Please try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void returnResult(JSONObject data) {
        if ("success".equalsIgnoreCase(data.optString("response"))) {
            final int layoutId = new Random().nextInt(61) + 20;
            images.put(layoutId, pathsList[0]);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    activitySellerAddPackageBinding.rlImageSelected.removeView(findViewById(layoutId));
                    if (images.containsKey(layoutId)) {
                        images.remove(layoutId);
                    }

                }
            });
            activitySellerAddPackageBinding.rlImageSelected.addView(imageView);
            addId = data.optString("add_id");
            imageUpload.put(data.optString("add_id"), data.optString("path"));

        } else {
            Toast.makeText(this, "Image upload failed. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySellerAddPackageBinding = getViewDataBinding();
        sellerAddPackageViewModel.setNavigator(this);
        options = new ArrayList<>();
        lan = new ArrayList<>();
        images = new HashMap<>();
        hashMap = new HashMap<>();
        imageUpload = new HashMap<>();
        reqQueue = Volley.newRequestQueue(getApplicationContext());
        hideKeyboard();
        setSpinner();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 30: {
                pathsList = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
                addImage(pathsList,false);
                break;
            }
        }
    }

    private void setSpinner() {
        System.out.println("Arun option 1--");
        System.out.println("Arun option --"+option);
        category = activitySellerAddPackageBinding.spinnerType;
        System.out.println("Arun option --"+option);
        if (option == 1) {
            ArrayAdapter<String> spinnerTravelAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    getResources().getStringArray(R.array.travel_options));
            category.setAdapter(spinnerTravelAdapter);
            category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (arg2 == 2) {
                        activitySellerAddPackageBinding.tvSubCategory.setVisibility(View.VISIBLE);
                        activitySellerAddPackageBinding.spinnerSubType.setVisibility(View.VISIBLE);
                    } else {
                        activitySellerAddPackageBinding.tvSubCategory.setVisibility(View.GONE);
                        activitySellerAddPackageBinding.spinnerSubType.setVisibility(View.GONE);
                    }
                    if (arg2 == 1) {

                        activitySellerAddPackageBinding.tvName.setText("Name");
                        activitySellerAddPackageBinding.llLocation.setVisibility(View.GONE);
                        activitySellerAddPackageBinding.tvNoOfPeoples.setVisibility(View.GONE);
                        activitySellerAddPackageBinding.etNoOfPeoples.setVisibility(View.GONE);
                        activitySellerAddPackageBinding.rlPackage.setVisibility(View.GONE);
                        activitySellerAddPackageBinding.rlLanguage.setVisibility(View.VISIBLE);
                        activitySellerAddPackageBinding.rlLanguageSelected.setVisibility(View.VISIBLE);

                        //country
                        activitySellerAddPackageBinding.tvCountry.setVisibility(View.VISIBLE);
                        activitySellerAddPackageBinding.spinnerCountry.setVisibility(View.VISIBLE);


                        //city
                        activitySellerAddPackageBinding.tvCity.setVisibility(View.VISIBLE);
                        activitySellerAddPackageBinding.spinnerCity.setVisibility(View.VISIBLE);


                    } else {
                        activitySellerAddPackageBinding.tvName.setText(getString(R.string.package_name));
                        //country
                        activitySellerAddPackageBinding.tvCountry.setVisibility(View.GONE);
                        activitySellerAddPackageBinding.spinnerCountry.setVisibility(View.GONE);


                        //city
                        activitySellerAddPackageBinding.tvCity.setVisibility(View.GONE);
                        activitySellerAddPackageBinding.spinnerCity.setVisibility(View.GONE);


                        activitySellerAddPackageBinding.llLocation.setVisibility(View.VISIBLE);
                        activitySellerAddPackageBinding.tvNoOfPeoples.setVisibility(View.VISIBLE);
                        activitySellerAddPackageBinding.etNoOfPeoples.setVisibility(View.VISIBLE);
                        activitySellerAddPackageBinding.rlPackage.setVisibility(View.VISIBLE);
                        activitySellerAddPackageBinding.rlLanguage.setVisibility(View.GONE);
                        activitySellerAddPackageBinding.rlLanguageSelected.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
        } else {
            activitySellerAddPackageBinding.tvCity.setText("God");
            activitySellerAddPackageBinding.tvCity.setVisibility(View.GONE);
            activitySellerAddPackageBinding.spinnerCity.setVisibility(View.GONE);
            ArrayAdapter<String> spinnerSuppliesAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    getResources().getStringArray(R.array.supply_options));
            category.setAdapter(spinnerSuppliesAdapter);
            category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    activitySellerAddPackageBinding.tvSubCategory.setVisibility(View.VISIBLE);
                    activitySellerAddPackageBinding.spinnerSubType.setVisibility(View.VISIBLE);
                    activitySellerAddPackageBinding.llLocation.setVisibility(View.GONE);
                    activitySellerAddPackageBinding.tvNoOfPeoples.setVisibility(View.GONE);
                    activitySellerAddPackageBinding.etNoOfPeoples.setVisibility(View.GONE);
                    activitySellerAddPackageBinding.rlPackage.setVisibility(View.VISIBLE);
                    activitySellerAddPackageBinding.rlLanguage.setVisibility(View.GONE);
                    activitySellerAddPackageBinding.rlLanguageSelected.setVisibility(View.GONE);
                    activitySellerAddPackageBinding.tvCity.setVisibility(View.GONE);
                    activitySellerAddPackageBinding.spinnerCity.setVisibility(View.GONE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
        }

       /* //Language spinner
        Spinner language = activitySellerAddPackageBinding.spinnerLanguage;
        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View languageView = inflater.inflate(R.layout.item_language_view, null);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10,20,10,10);
                    languageView.setLayoutParams(params);
                    languageView.setId(i);
                    activitySellerAddPackageBinding.rlLanguageSelected.addView(languageView);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

    }

    private void addImage(String[] data,Boolean isLicense) {
        sellerAddPackageViewModel.InitialMediaUploadRequest(reqQueue, getApplicationContext(), data[0], "", Integer.toString(1), isLicense);
    }

    private void addLanguage(String value, int id) {
        if (hashMap.isEmpty()) {
            int layoutId = 1000 + id;
            hashMap.put(layoutId, value);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    activitySellerAddPackageBinding.rlLanguageSelected.removeView(findViewById(layoutId));
                    if (hashMap.containsKey(layoutId)) {
                        hashMap.remove(layoutId);
                    }

                }
            });
            activitySellerAddPackageBinding.rlLanguageSelected.addView(languageView);
        } else {
            int layoutId = 1000 + id;
            if (!hashMap.containsKey(layoutId)) {
                hashMap.put(layoutId, value);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                        activitySellerAddPackageBinding.rlLanguageSelected.removeView(findViewById(layoutId));

                    }
                });
                activitySellerAddPackageBinding.rlLanguageSelected.addView(languageView);
            }

        }

    }


    private void addToList(String option) {

        if (option.equalsIgnoreCase(getString(R.string.package_flights))) {
            activitySellerAddPackageBinding.rlFlight.setVisibility(View.VISIBLE);
        } else if (option.equalsIgnoreCase(getString(R.string.package_hotels))) {
            activitySellerAddPackageBinding.rlHotel.setVisibility(View.VISIBLE);
        } else if (option.equalsIgnoreCase(getString(R.string.package_meals))) {
            activitySellerAddPackageBinding.rlMeals.setVisibility(View.VISIBLE);
        } else if (option.equalsIgnoreCase(getString(R.string.package_sight_seeing))) {
            activitySellerAddPackageBinding.rlSS.setVisibility(View.VISIBLE);
        } else if (option.equalsIgnoreCase(getString(R.string.package_site_visit))) {
            activitySellerAddPackageBinding.rlSV.setVisibility(View.VISIBLE);
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
}
