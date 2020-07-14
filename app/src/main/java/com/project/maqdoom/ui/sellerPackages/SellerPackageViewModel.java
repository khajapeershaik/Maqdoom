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

package com.project.maqdoom.ui.sellerPackages;

import android.util.Log;

import com.androidnetworking.error.ANError;
import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.data.model.api.MaqdoomLoginRequest;
import com.project.maqdoom.data.model.api.SellerPayRequest;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.ui.splash.SplashNavigator;
import com.project.maqdoom.utils.rx.SchedulerProvider;


public class SellerPackageViewModel extends BaseViewModel<SellerPackageNavigator> {

    public SellerPackageViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void paymentRequest(String seller_id, String transaction_id, String amt) {
        setIsLoading(true);
        getCompositeDisposable().add(getDataManager()
                .doSellerPayApiCall(new SellerPayRequest.ServerSellerPayRequest( seller_id,  transaction_id,  amt))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    if ("fail".equals(response.getResponse())) {
                        getNavigator().showErrorAlert(response.getMessage());
                    } else {
                        getDataManager().setSellerStatus("1");
                        getNavigator().showErrorAlert(response.getMessage());
                        getNavigator().openSellerHome();

                    }
                }, throwable -> {
                    if (throwable instanceof ANError) {
                        ANError anError = (ANError) throwable;
                        if (anError.getErrorCode() != 0) {
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
                }));
    }
    public void onPaymentClick() {
        getNavigator().openPayment();
    }

}
