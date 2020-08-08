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

import android.text.TextUtils;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.data.model.api.MaqDoomLoginResponse;
import com.project.maqdoom.data.model.api.MaqdoomLoginRequest;
import com.project.maqdoom.data.model.api.ProfileResponse;
import com.project.maqdoom.data.remote.api_rest.ApiClient;
import com.project.maqdoom.data.remote.api_rest.ApiInterface;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.utils.CommonUtils;
import com.project.maqdoom.utils.rx.SchedulerProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginViewModel extends BaseViewModel<LoginNavigator> {

    String imageUrl = "";

    public LoginViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);

    }

    public boolean isEmailAndPasswordValid(String email, String password) {
        // validate email and password
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        if (!CommonUtils.isEmailValid(email)) {
            return false;
        }
        return !TextUtils.isEmpty(password);
    }

    public void login(String language, String phone, String otp) {
        setIsLoading(true);
        getCompositeDisposable().add(getDataManager()
                .doServerLoginApiCall(new MaqdoomLoginRequest.ServerLoginRequest(language, phone, otp))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    if ("fail".equals(response.getResponse())) {
                        getNavigator().showErrorAlert(response.getMessage());
                    } else {
                        if (response.getData() != null) {
                            if (!"".equals(response.getData().getId())) {
                                getDataManager()
                                        .updateUserInfo(
                                                Integer.valueOf(response.getData().getId()),
                                                DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER,
                                                response.getData().getName(),
                                                response.getData().getEmail(),
                                                response.getData().getCreated_at(),
                                                response.getData().getIs_seller(),
                                                response.getData().getSeller_subscrption_status(),
                                                response.getData().getPhone()

                                        );
                                imageUrl = response.getData().getDpimg();

                                if ("0".equalsIgnoreCase(response.getData().getIs_seller())) {
                                    getNavigator().openCustomerHome();
                                } else {
//                                    if("0".equalsIgnoreCase(response.getData().getSeller_subscrption_status())){
//                                        getNavigator().openSellerSubscription();
//                                        //getNavigator().openSellerHome();
//                                    }else{
                                    getNavigator().openSellerHome();
                                    // }
                                }
                            } else {
                                getNavigator().showErrorAlert(response.getMessage());
                            }
                        } else {
                            getNavigator().showErrorAlert(response.getMessage());
                        }

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
                    throwable.printStackTrace();
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));
    }

    public void savePhoneOTP(String language, String phone, String otp) {
        setIsLoading(true);
        getCompositeDisposable().add(getDataManager()
                .dosavePhoneOTP(new MaqdoomLoginRequest.ServerLoginRequest(language, phone, otp))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    if ("success".equals(response.getResponse())) {
                        login(language, phone, otp);
                    }
                }, throwable -> {
                    if (throwable instanceof ANError) {
                        ANError anError = (ANError) throwable;
                        if (anError.getErrorCode() != 0) {

                        } else {
                        }
                    } else {
                    }
                    throwable.printStackTrace();
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));

    }

    public String getProfileImage() {
        return imageUrl;
    }

    public void onSignUpClick() {
        getNavigator().openSignUp();
    }

    public void onForgotPasswordClick() {
        getNavigator().openForgotPassword();
    }

    public void onServerLoginClick() {
        getNavigator().login();
    }
}
