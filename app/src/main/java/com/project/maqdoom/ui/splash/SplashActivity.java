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

package com.project.maqdoom.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.crashlytics.internal.common.CrashlyticsCore;
import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.ActivitySplashBinding;
import com.project.maqdoom.ui.base.BaseActivity;
import com.project.maqdoom.ui.customerHome.CustomerHomeActivity;
import com.project.maqdoom.ui.login.LoginActivity;
import com.project.maqdoom.ui.sellerHome.SellerHomeActivity;
import com.project.maqdoom.ui.sellerPackages.SellerPackageActivity;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;

import java.util.Locale;


public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> implements SplashNavigator {

    @Inject
    ViewModelProviderFactory factory;
    
    private SplashViewModel mSplashViewModel;

    private SharedPreferences sharedpreferences;
    public static final String LANGUAGE_REFERENCE = "language_preference" ;
    public static final String LANGUAGE_KEY = "language";


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public SplashViewModel getViewModel() {
        mSplashViewModel = ViewModelProviders.of(this,factory).get(SplashViewModel.class);
        return mSplashViewModel;
    }

    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.newIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity() {

    }

    @Override
    public void openCustomerHome() {
        Intent intent = CustomerHomeActivity.newIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openSellerHome() {
        Intent intent = SellerHomeActivity.newIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openSellerSubscription() {
        Intent intent = SellerPackageActivity.newIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSplashViewModel.setNavigator(this);

        sharedpreferences = getSharedPreferences(LANGUAGE_REFERENCE, Context.MODE_PRIVATE);

        String langPreference = sharedpreferences.getString(LANGUAGE_KEY,"en");
        updateLocale(langPreference);

        mSplashViewModel.startSeeding();
    }

    private void updateLocale(String en) {
        Locale locale = new Locale(en);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        saveLanguagePreference(en);
        Log.v("data",""+sharedpreferences.getString(LANGUAGE_KEY,"en"));
    }

    private void saveLanguagePreference(String lan){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(LANGUAGE_KEY, lan);
        editor.commit();
    }
}
