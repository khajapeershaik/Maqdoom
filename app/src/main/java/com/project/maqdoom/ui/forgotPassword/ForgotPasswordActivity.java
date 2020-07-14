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

package com.project.maqdoom.ui.forgotPassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.ActivityForgotPasswordBinding;
import com.project.maqdoom.ui.base.BaseActivity;
import com.project.maqdoom.ui.login.LoginActivity;
import com.project.maqdoom.ui.registration.RegistrationActivity;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;


public class ForgotPasswordActivity extends BaseActivity<ActivityForgotPasswordBinding, ForgotPasswordViewModel> implements ForgotPasswordNavigator {

    @Inject
    ViewModelProviderFactory factory;
    private ForgotPasswordViewModel forgotPasswordViewModel;
    private ActivityForgotPasswordBinding activityForgotPasswordBinding;

    public static Intent newIntent(Context context) {
        return new Intent(context, ForgotPasswordActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    public ForgotPasswordViewModel getViewModel() {
        forgotPasswordViewModel = ViewModelProviders.of(this,factory).get(ForgotPasswordViewModel.class);
        return forgotPasswordViewModel;
    }

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void forgotPassword() {
        String email = activityForgotPasswordBinding.etEmail.getText().toString();
        String password = activityForgotPasswordBinding.etRePassword.getText().toString();
        String code = activityForgotPasswordBinding.etName.getText().toString();
        if (forgotPasswordViewModel.isEmailAndPasswordValid( email,code,password)) {
            forgotPasswordViewModel.forgotPassword( email,password,code);
        }else{
            Toast.makeText(this, getString(R.string.fill_all_error), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.newIntent(ForgotPasswordActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void sendEmailVerification() {
        String email = activityForgotPasswordBinding.etEmail.getText().toString();
        if (forgotPasswordViewModel.isEmailValid(email)) {
            forgotPasswordViewModel.requestVerificationCode(email);
        }else{
            Toast.makeText(this, getString(R.string.fill_all_error), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void showErrorAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityForgotPasswordBinding = getViewDataBinding();
        forgotPasswordViewModel.setNavigator(this);
    }
}
