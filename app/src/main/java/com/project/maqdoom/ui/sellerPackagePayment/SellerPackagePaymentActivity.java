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

package com.project.maqdoom.ui.sellerPackagePayment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.ActivitySellerPackagePaymentBinding;
import com.project.maqdoom.ui.base.BaseActivity;
import com.project.maqdoom.ui.customerHome.CustomerHomeActivity;
import com.project.maqdoom.ui.login.LoginActivity;
import com.project.maqdoom.ui.sellerHome.SellerHomeActivity;
import com.project.maqdoom.ui.sellerPackages.SellerPackageActivity;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;


public class SellerPackagePaymentActivity extends BaseActivity<ActivitySellerPackagePaymentBinding, SellerPackagePaymentViewModel> implements SellerPackagePaymentNavigator {

    @Inject
    ViewModelProviderFactory factory;
    ActivitySellerPackagePaymentBinding activitySellerPackagePaymentBinding;
    private SellerPackagePaymentViewModel sellerPackagePaymentViewModel;
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SellerPackagePaymentActivity.class);
        return intent;
    }
    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_seller_package_payment;
    }

    @Override
    public SellerPackagePaymentViewModel getViewModel() {
        sellerPackagePaymentViewModel = ViewModelProviders.of(this,factory).get(SellerPackagePaymentViewModel.class);
        return sellerPackagePaymentViewModel;
    }

    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.newIntent(SellerPackagePaymentActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity() {

    }

    @Override
    public void openSellerHome() {
        Intent intent = SellerHomeActivity.newIntent(SellerPackagePaymentActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openPayment() {

    }

    @Override
    public void proceedToPayment() {
        String card = activitySellerPackagePaymentBinding.etCardNumber.getText().toString();
        String name = activitySellerPackagePaymentBinding.etCardNumber.getText().toString();
        String date = activitySellerPackagePaymentBinding.etExpiry.getText().toString();
        String expiry = activitySellerPackagePaymentBinding.etSecurity.getText().toString();
        if (sellerPackagePaymentViewModel.isValid(card, name,date,expiry)) {
            hideKeyboard();
            sellerPackagePaymentViewModel.proceedToPayment(card, name,date,expiry);

        } else {
            Toast.makeText(this, getString(R.string.invalid_email_password), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void goBack() {
        Intent intent = SellerPackageActivity.newIntent(SellerPackagePaymentActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sellerPackagePaymentViewModel.setNavigator(this);
        activitySellerPackagePaymentBinding = getViewDataBinding();

    }
}
