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

package com.project.maqdoom.ui.touristPackageDetails;

import android.util.Log;

import com.androidnetworking.error.ANError;
import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.data.model.api.DeleteAddRequest;
import com.project.maqdoom.data.model.api.EditProfileRequest;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.ui.touristGuideDetails.TouristGuideDetailsNavigator;
import com.project.maqdoom.utils.rx.SchedulerProvider;


public class TouristPackageDetailsViewModel extends BaseViewModel<TouristPackageDetailsNavigator> {


    public TouristPackageDetailsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);

    }

    public void deleteAdd(String id) {
        setIsLoading(true);
        int userId = getDataManager().getCurrentUserId();
        getCompositeDisposable().add(getDataManager()
                .doDeleteAddApiCall(new DeleteAddRequest.ServerDeleteAddRequest(id))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    if ("fail".equals(response.getResponse())) {
                        getNavigator().showErrorAlert(response.getMessage());
                    } else {
                        getNavigator().showErrorAlert(response.getMessage());
                        getNavigator().goHome();
                    }
                }, throwable -> {
                    if (throwable instanceof ANError) {
                        ANError anError = (ANError) throwable;
                        if (anError.getErrorCode() != 0) {
                        } else {
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    } else {
                        Log.d("TAG", "onError errorMessage : " + throwable.getMessage());
                    }
                    throwable.printStackTrace();
                    setIsLoading(false);
                }));
    }

    public void onNavBackClick() {
        getNavigator().goBack();
    }

    public void onTouristGroupClick() {
        getNavigator().goToTouristGroup();
    }

    public void onDeletePackageClick() {
        getNavigator().deletePackage();
    }

    public void onWhatsAppClick(){
        getNavigator().openWhatsApp();
    }

    public void onCallClick(){
        getNavigator().openCall();
    }

    public void onChatClick(){
        getNavigator().openChat();
    }
}
