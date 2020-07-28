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

package com.project.maqdoom.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.FragmentProfileBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.customerTouristGroups.TouristGroupFragment;
import com.project.maqdoom.ui.login.LoginActivity;
import com.project.maqdoom.ui.splash.SplashActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;


public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileViewModel> implements ProfileNavigator {

    public static final String TAG = ProfileFragment.class.getSimpleName();
    @Inject
    ViewModelProviderFactory factory;
    private ProfileViewModel profileViewModel;
    FragmentProfileBinding fragmentProfileBinding;
    private String defaultLanguage;
    private SharedPreferences sharedpreferences;
    public static final String LANGUAGE_REFERENCE = "language_preference" ;
    public static final String LANGUAGE_KEY = "language";


    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public ProfileViewModel getViewModel() {
        profileViewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel.class);
        return profileViewModel;
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void goToTouristGroup() {
        assert getFragmentManager() != null;
        getFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                //.setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.parentLayout, TouristGroupFragment.newInstance(), TouristGroupFragment.TAG)
                .commit();

    }

    @Override
    public void logout() {
        for (Fragment fragment : Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().getFragments()) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        startActivity(LoginActivity.newIntent(this.getActivity()));
        this.getActivity().finish();
    }

    @Override
    public void setName() {

    }

    @Override
    public void enableEdit() {
        enableEditOption();
    }

    @Override
    public void updateProfile() {
        String phone = fragmentProfileBinding.etPhone.getText().toString();
        String name = fragmentProfileBinding.etName.getText().toString();
        String eMail = fragmentProfileBinding.etMail.getText().toString();
        if (profileViewModel.isValid(phone, name,eMail)) {
            hideKeyboard();
            profileViewModel.updateProfile(phone, name,eMail);

        } else {
            Toast.makeText(getActivity(), getString(R.string.fill_all_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void disableEdit() {
        disableEditOption();
    }
    @Override
    public void showErrorAlert(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentProfileBinding = getViewDataBinding();

        sharedpreferences = getActivity().getSharedPreferences(LANGUAGE_REFERENCE, Context.MODE_PRIVATE);

        String langPreference = sharedpreferences.getString(LANGUAGE_KEY,"en");

            if (langPreference.equalsIgnoreCase("en")) {
                defaultLanguage = "English";
            } else {
                defaultLanguage = "Arabic";
            }

        fragmentProfileBinding.languageButton.setText(defaultLanguage);
        fragmentProfileBinding.languageButton.setOnClickListener(view1 -> {
            showLanguages();
        });
        setUp();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        //onBackPressed();
    }

    private void updateLocale(String en) {
        Locale locale = new Locale(en);
        Configuration config = getResources().getConfiguration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());
        saveLanguagePreference(en);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
       startActivity(new Intent(getActivity(), SplashActivity.class));
//        if (Build.VERSION.SDK_INT >= 26) {
//            ft.setReorderingAllowed(false);
//        }
//        startActivity(getActivity().getIntent());
//        ft.detach(this).attach(this).commit();
//        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    private void saveLanguagePreference(String lan){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(LANGUAGE_KEY, lan);
        editor.commit();
    }

    public void showLanguages() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        final View customLayout = getLayoutInflater().inflate(R.layout.layout_profile_language_selector, null);
        alertDialog.setView(customLayout);
        AlertDialog OptionDialog = alertDialog.create();
        OptionDialog.show();
        ListView llLanguageList = customLayout.findViewById(R.id.lv_language);
        String[] languageArray = {"English","Arabic"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1
                ,languageArray);
        llLanguageList.setAdapter(adapter);
        llLanguageList.setOnItemClickListener((adapterView, view, i, l) -> {

            if(i==1){
                profileViewModel.getDataManager().setLanguage("ar");
                Log.v("selected","ar");
                fragmentProfileBinding.languageButton.setText("Arabic");
                updateLocale("ar");
            }
            else{
                profileViewModel.getDataManager().setLanguage("en");
                Log.v("selected","en");
                fragmentProfileBinding.languageButton.setText("English");
                updateLocale("en");
            }
            OptionDialog.dismiss();
        });
        ImageButton close = customLayout.findViewById(R.id.navClose);
        close.setOnClickListener(view -> OptionDialog.dismiss());
    }

    private void removeFragment() {
        Fragment fragment = getFragmentManager().findFragmentByTag(TAG);
        if (fragment != null)
            getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    private void setUp() {
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

                    return true;
                }
                return false;
            }
        });

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

    private void showHome() {
        for (Fragment fragment : Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().getFragments()) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void enableEditOption() {
        fragmentProfileBinding.etPhone.setEnabled(true);
        if("Not available".equalsIgnoreCase(fragmentProfileBinding.etPhone.getText().toString().trim())){
            fragmentProfileBinding.etPhone.setText("");
        }
        fragmentProfileBinding.etPhone.requestFocus();
        fragmentProfileBinding.etName.setEnabled(true);
        fragmentProfileBinding.etMail.setEnabled(true);
        fragmentProfileBinding.rlSave.setVisibility(View.VISIBLE);
    }

    private void disableEditOption() {
        populateData();
        fragmentProfileBinding.etPhone.setEnabled(false);
        fragmentProfileBinding.etName.setEnabled(false);
        fragmentProfileBinding.etMail.setEnabled(false);
        fragmentProfileBinding.rlSave.setVisibility(View.GONE);


    }
    public void populateData() {
        final String currentUserName = profileViewModel.getDataManager().getCurrentUserName();
        if (!TextUtils.isEmpty(currentUserName)) {
            fragmentProfileBinding.etName.setText(currentUserName);
        }

        final String currentUserEmail = profileViewModel.getDataManager().getEmail();
        if (!TextUtils.isEmpty(currentUserEmail)) {
            fragmentProfileBinding.etMail.setText(currentUserEmail);
        }
        final String phoneNumber = profileViewModel.getDataManager().getPhone();
        if (!TextUtils.isEmpty(phoneNumber)) {
            fragmentProfileBinding.etPhone.setText(phoneNumber);
        }else{
            fragmentProfileBinding.etPhone.setText("Not available");
        }

    }
}
