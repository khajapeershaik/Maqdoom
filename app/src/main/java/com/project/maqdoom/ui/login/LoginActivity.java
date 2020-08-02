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
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.project.maqdoom.BR;
import com.project.maqdoom.MaqdoomApp;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.ActivityLoginBinding;
import com.project.maqdoom.di.component.AppComponent;
import com.project.maqdoom.di.component.DaggerAppComponent;
import com.project.maqdoom.ui.base.BaseActivity;
import com.project.maqdoom.ui.customerHome.CustomerHomeActivity;
import com.project.maqdoom.ui.forgotPassword.ForgotPasswordActivity;
import com.project.maqdoom.ui.registration.RegistrationActivity;
import com.project.maqdoom.ui.sellerHome.SellerHomeActivity;
import com.project.maqdoom.ui.sellerPackages.SellerPackageActivity;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;



public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements LoginNavigator {

    @Inject
    ViewModelProviderFactory factory;
    private LoginViewModel mLoginViewModel;
    private ActivityLoginBinding mActivityLoginBinding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    String mPhone;
    private CountryCodePicker countryCodePicker;
    private PhoneAuthCredential credential;
    private DatabaseReference mDatabase;
    private SharedPreferences sharedpreferences;
    public static final String LANGUAGE_REFERENCE = "language_preference" ;
    public static final String LANGUAGE_KEY = "language";
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


        /*if (mLoginViewModel.isEmailAndPasswordValid(email, otp)) {
            hideKeyboard();
            mLoginViewModel.login(langPreference,email, otp);

        } else {
            Toast.makeText(this, getString(R.string.invalid_email_password), Toast.LENGTH_SHORT).show();
        }*/

        savePhoneOTP();
        submitLogin();
    }

    private void submitLogin(){
        String email = countryCodePicker.getSelectedCountryCode()+ mActivityLoginBinding.etMobileNumber.getText().toString();
        String otp = mActivityLoginBinding.etOTP.getText().toString();

        sharedpreferences = getSharedPreferences(LANGUAGE_REFERENCE, Context.MODE_PRIVATE);

        String langPreference = sharedpreferences.getString(LANGUAGE_KEY,"en");
        mLoginViewModel.login(langPreference,email, otp);
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
        countryCodePicker=mActivityLoginBinding.ccp;


       mActivityLoginBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = "+"+countryCodePicker.getSelectedCountryCode()+mActivityLoginBinding.etMobileNumber.getText().toString();

                Log.d("phone", "Phone No.: " + phoneNum);
                requestPhoneAuth(phoneNum);
            }
        });
        fStore = FirebaseFirestore.getInstance();


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

    private void requestPhoneAuth(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60L, TimeUnit.SECONDS,this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
//                        Toast.makeText(Register.this, "OTP Timeout, Please Re-generate the OTP Again.", Toast.LENGTH_SHORT).show();
//                        resend.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        mActivityLoginBinding.otpLayout.setVisibility(View.VISIBLE);
                    //    savePhoneOTP();
                      //  mActivityLoginBinding.btnSubmit.setVisibility(View.GONE);

                    /*    verificationId = s;
                        token = forceResendingToken;
                        verificationOnProgress = true;
                        progressBar.setVisibility(View.GONE);
                        state.setVisibility(View.GONE);
                        next.setText("Verify");
                        next.setEnabled(true);
                        optEnter.setVisibility(View.VISIBLE);*/
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                        // called if otp is automatically detected by the app
                        verifyAuth(phoneAuthCredential);

                        if (phoneAuthCredential.getSmsCode() != null) {
                            mActivityLoginBinding.etOTP.setText(phoneAuthCredential.getSmsCode());
                        }

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }



    private void verifyAuth(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Phone Verified."+mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                    //checkUserProfile();

                }else {
//                    progressBar.setVisibility(View.GONE);
//                    state.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Can not Verify phone and Create Account.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void savePhoneOTP(){
        String email = countryCodePicker.getSelectedCountryCode()+ mActivityLoginBinding.etMobileNumber.getText().toString();
        String otp = mActivityLoginBinding.etOTP.getText().toString();

        sharedpreferences = getSharedPreferences(LANGUAGE_REFERENCE, Context.MODE_PRIVATE);

        String langPreference = sharedpreferences.getString(LANGUAGE_KEY,"en");
        mLoginViewModel.savePhoneOTP(langPreference,email, otp);

    }
}
