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

import android.text.TextUtils;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.data.model.api.PasswordResetRequest;
import com.project.maqdoom.data.model.api.PasswordVerificationRequest;
import com.project.maqdoom.data.model.api.RegistrationRequest;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.utils.CommonUtils;
import com.project.maqdoom.utils.rx.SchedulerProvider;


public class ForgotPasswordViewModel extends BaseViewModel<ForgotPasswordNavigator> {

    public ForgotPasswordViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public boolean isEmailAndPasswordValid(String email, String code, String password) {
        // validate email and password
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        if (!CommonUtils.isEmailValid(email)) {
            return false;
        }
        if (TextUtils.isEmpty(code)) {
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }
    public boolean isEmailValid(String email ) {
        // validate email and password
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        if (!CommonUtils.isEmailValid(email)) {
            return false;
        }

        return true;
    }
    public void forgotPassword(String email, String password,String code) {
        setIsLoading(true);
        getCompositeDisposable().add(getDataManager()
                .doForgotPasswordApiCall(new PasswordResetRequest.ServerPasswordResetRequest(email, password, code))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    if ("fail".equals(response.getResponse())) {
                        getNavigator().showErrorAlert(response.getMessage());
                    } else {
                        getNavigator().showErrorAlert(response.getMessage());
                        getNavigator().openLoginActivity();
                    }
                }, throwable -> {
                    if (throwable instanceof ANError) {
                        ANError anError = (ANError) throwable;
                        if (anError.getErrorCode() != 0) {
                            // received ANError from server
                            // error.getErrorCode() - the ANError code from server
                            // error.getErrorBody() - the ANError body from server
                            // error.getErrorDetail() - just a ANError detail
                            Log.d("TAG", "onError errorCode : " + anError.getErrorCode());
                            Log.d("TAG", "onError errorBody : " + anError.getErrorBody());
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    } else {
                        Log.d("TAG", "onError errorMessage : " + throwable.getMessage());
                    }
                    System.out.println("arun false");
                    System.out.println(throwable.getStackTrace());
                    System.out.println("Arun print");
                    throwable.printStackTrace();
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));
    }
    public void requestVerificationCode(String email) {
        setIsLoading(true);
        getCompositeDisposable().add(getDataManager()
                .doRequestCodeApiCall(new PasswordVerificationRequest.ServerPasswordVerificationRequest(email))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    if ("fail".equals(response.getResponse())) {
                        getNavigator().showErrorAlert(response.getMessage());
                    } else {
                        getNavigator().showErrorAlert(response.getMessage());
                    }
                }, throwable -> {
                    if (throwable instanceof ANError) {
                        ANError anError = (ANError) throwable;
                        if (anError.getErrorCode() != 0) {
                            // received ANError from server
                            // error.getErrorCode() - the ANError code from server
                            // error.getErrorBody() - the ANError body from server
                            // error.getErrorDetail() - just a ANError detail
                            Log.d("TAG", "onError errorCode : " + anError.getErrorCode());
                            Log.d("TAG", "onError errorBody : " + anError.getErrorBody());
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    } else {
                        Log.d("TAG", "onError errorMessage : " + throwable.getMessage());
                    }
                    System.out.println("arun false");
                    System.out.println(throwable.getStackTrace());
                    System.out.println("Arun print");
                    throwable.printStackTrace();
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));
    }

    public void OnVerificationClick(){
        getNavigator().sendEmailVerification();
    }

    public void onServerFPClick() {
        getNavigator().forgotPassword();
    }
}
