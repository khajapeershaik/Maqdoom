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

package com.project.maqdoom.ui.registration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.ActivityRegistrationBinding;
import com.project.maqdoom.ui.base.BaseActivity;
import com.project.maqdoom.ui.login.LoginActivity;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;


public class RegistrationActivity extends BaseActivity<ActivityRegistrationBinding, RegistrationViewModel> implements RegistrationNavigator {

    @Inject
    ViewModelProviderFactory factory;
    private RegistrationViewModel registrationViewModel;
    private ActivityRegistrationBinding activityRegistrationBinding;
    private SharedPreferences sharedpreferences;
    public static final String LANGUAGE_REFERENCE = "language_preference" ;
    public static final String LANGUAGE_KEY = "language";

    public static Intent newIntent(Context context) {
        return new Intent(context, RegistrationActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_registration;
    }


    @Override
    public RegistrationViewModel getViewModel() {
        registrationViewModel = ViewModelProviders.of(this,factory).get(RegistrationViewModel.class);
        return registrationViewModel;
    }

    @Override
    public void handleError(Throwable throwable) {
    }

    @Override
    public void register() {
        String name = activityRegistrationBinding.etName.getText().toString().trim();
        String email = activityRegistrationBinding.etEmail.getText().toString().trim();
        String password = activityRegistrationBinding.etPassword.getText().toString().trim();
        String re_password = activityRegistrationBinding.etRePassword.getText().toString().trim();
        sharedpreferences = getSharedPreferences(LANGUAGE_REFERENCE, Context.MODE_PRIVATE);

        String langPreference = sharedpreferences.getString(LANGUAGE_KEY,"en");

        if (registrationViewModel.isEmailAndPasswordValid(name,email, password,re_password)) {
            if(!password.equals(re_password) ){
                Toast.makeText(this, getString(R.string.password_mismatch), Toast.LENGTH_SHORT).show();
            }else{
                if (activityRegistrationBinding.radioGroup.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(this, getString(R.string.check_box), Toast.LENGTH_SHORT).show();
                }else{
                    hideKeyboard();
                    String type;
                    if (activityRegistrationBinding.radioGroup.getCheckedRadioButtonId() == R.id.optCustomer) {
                        type="0";
                    }else{
                        type="1";
                    }

                    registrationViewModel.registration(langPreference,name,email, password,re_password,type);
                }

            }

        } else {
            Toast.makeText(this, getString(R.string.invalid_email_password), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.newIntent(RegistrationActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void setSeller() {

    }

    @Override
    public void showErrorAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegistrationBinding = getViewDataBinding();
        registrationViewModel.setNavigator(this);
        sharedpreferences = getSharedPreferences(LANGUAGE_REFERENCE, Context.MODE_PRIVATE);

    }
}
