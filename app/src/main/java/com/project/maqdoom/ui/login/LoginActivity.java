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

package com.project.maqdoom.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.ActivityLoginBinding;
import com.project.maqdoom.ui.base.BaseActivity;
import com.project.maqdoom.ui.customerHome.CustomerHomeActivity;
import com.project.maqdoom.ui.forgotPassword.ForgotPasswordActivity;
import com.project.maqdoom.ui.registration.RegistrationActivity;
import com.project.maqdoom.ui.sellerHome.SellerHomeActivity;
import com.project.maqdoom.ui.sellerPackages.SellerPackageActivity;

import java.util.HashMap;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;



public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements LoginNavigator {

    @Inject
    ViewModelProviderFactory factory;
    private LoginViewModel mLoginViewModel;
    private ActivityLoginBinding mActivityLoginBinding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginViewModel getViewModel() {
        mLoginViewModel = ViewModelProviders.of(this,factory).get(LoginViewModel.class);
        return mLoginViewModel;
    }

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void login() {
        String email = mActivityLoginBinding.etEmail.getText().toString();
        String password = mActivityLoginBinding.etPassword.getText().toString();
        if (mLoginViewModel.isEmailAndPasswordValid(email, password)) {
            hideKeyboard();
            mLoginViewModel.login(email, password);

        } else {
            Toast.makeText(this, getString(R.string.invalid_email_password), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openMainActivity() {

    }

    @Override
    public void openSignUp() {
        Intent intent = RegistrationActivity.newIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openForgotPassword() {
        Intent intent = ForgotPasswordActivity.newIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openCustomerHome() {
        registerForChat();
        Intent intent = CustomerHomeActivity.newIntent(LoginActivity.this);
        startActivity(intent);
        finish();

    }

    @Override
    public void showErrorAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openSellerHome() {
        registerForChat();
        Intent intent = SellerHomeActivity.newIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openSellerSubscription() {
        registerForChat();
        Intent intent = SellerPackageActivity.newIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding = getViewDataBinding();
        mLoginViewModel.setNavigator(this);
        mAuth = FirebaseAuth.getInstance();

    }

    private void registerForChat(){
        String email = mLoginViewModel.getDataManager().getEmail();
        String password = mLoginViewModel.getDataManager().getCreatedDate();
        String name = mLoginViewModel.getDataManager().getCurrentUserName();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = current_user.getUid();
                String userType = "";
                if ("0".equalsIgnoreCase(mLoginViewModel.getDataManager().getUserType())){
                    userType ="Customer";
                }else{
                    userType ="Seller";
                }

                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                String device_token = FirebaseInstanceId.getInstance().getToken();
                String val = ServerValue.TIMESTAMP.toString();
                HashMap<String, String> userMap = new HashMap<>();
                userMap.put("name", name);
                userMap.put("status", userType);
                userMap.put("email", email);
                userMap.put("image", "default");
                userMap.put("thumb_image", "default");
                userMap.put("active_now", "false");
                userMap.put("device_token", device_token);
                mDatabase.setValue(userMap).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){

                    }
                });
            } else {
                firebaseLoginUser(email,password);

            }

        });
    }
    private void firebaseLoginUser(String email, String password) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String current_user_id = mAuth.getCurrentUser().getUid();
                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                mDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });

            } else {
               /* String task_result = task.getException().getMessage().toString();
                Toast.makeText(LoginActivity.this, "Error : " + task_result, Toast.LENGTH_LONG).show();*/
            }

        });


    }
}
