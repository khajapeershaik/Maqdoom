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

package com.project.maqdoom.ui.notification;

import android.util.Log;

import com.androidnetworking.error.ANError;
import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.data.model.api.NotificationResponse;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.utils.rx.SchedulerProvider;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class NotificationViewModel extends BaseViewModel<NotificationNavigator> {

    private final MutableLiveData<List<NotificationResponse.Adds>> notificationListLiveData;

    public NotificationViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        notificationListLiveData = new MutableLiveData<>();
        fetchData();
    }

    public void fetchData() {
        setIsLoading(true);
        //final String userType = getDataManager().getUserType();
        //int userId = getDataManager().getCurrentUserId();
        getCompositeDisposable().add(getDataManager()
                .doNotificationApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null && response.getData() != null) {
                        notificationListLiveData.setValue(response.getData());
                    }
                    setIsLoading(false);
                }, throwable -> {
                    setIsLoading(false);
                    if (throwable instanceof ANError) {
                        ANError anError = (ANError) throwable;
                        if (anError.getErrorCode() != 0) {
                            Log.d("TAG", "onError errorCode : " + anError.getErrorCode());
                            Log.d("TAG", "onError errorBody : " + anError.getErrorBody());
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        } else {
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    } else {
                        Log.d("TAG", "onError errorMessage : " + throwable.getMessage());
                    }
                }));
    }
    public LiveData<List<NotificationResponse.Adds>> getNotificationLiveData() {
        return notificationListLiveData;
    }
    public void onNavBackClick() {
        getNavigator().goBack();
    }

    public void onTouristGroupClick() {
        getNavigator().goToTouristGroup();
    }

}
