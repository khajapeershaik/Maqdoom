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

import android.text.TextUtils;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.data.model.api.RegistrationRequest;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.utils.CommonUtils;
import com.project.maqdoom.utils.rx.SchedulerProvider;


public class RegistrationViewModel extends BaseViewModel<RegistrationNavigator> {

    public RegistrationViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public boolean isEmailAndPasswordValid(String name, String email, String password, String re_password) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        if (!CommonUtils.isEmailValid(email)) {
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        if (TextUtils.isEmpty(re_password)) {
            return false;
        }

        return true;
    }

    public void registration(String name, String email, String password, String confirmPassword, String isSeller) {
        setIsLoading(true);
        getCompositeDisposable().add(getDataManager()
                .doServerRegistrationApiCall(new RegistrationRequest.ServerRegistrationRequest(name, email, password, confirmPassword, isSeller))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    if ("fail".equals(response.getResponse())) {
                        getNavigator().showErrorAlert(response.getMessage());
                    } else {
                        if (!"".equals(response.getUserId())) {
                            getNavigator().openLoginActivity();
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
                    System.out.println("arun false");
                    System.out.println(throwable.getStackTrace());
                    System.out.println("Arun print");
                    throwable.printStackTrace();
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));
    }


    public void onServerLoginClick() {
        getNavigator().register();
    }

    public void onSellerSelected() {
        getNavigator().setSeller();
    }

    public void onSignInClicked() {
        getNavigator().openLoginActivity();
    }
}
