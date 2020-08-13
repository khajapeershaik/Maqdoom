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

package com.project.maqdoom.ui.customerTouristGroups.insideCountry;

import android.util.Log;

import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.data.model.api.TravelCategoryGroupResponse;
import com.project.maqdoom.data.model.api.TravelCategoryRequest;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class InsideCountryViewModel extends BaseViewModel<InsideCountryNavigator> {

    private final MutableLiveData<List<TravelCategoryGroupResponse.Adds>> travelListLiveData;
    private final List<TravelCategoryGroupResponse.Adds> sortedList;

    public InsideCountryViewModel(DataManager dataManager,
                                  SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        travelListLiveData = new MutableLiveData<>();
        sortedList = new ArrayList<>();
    }

    public void fetchData(String dataType) {
        setIsLoading(true);
        final String userType = getDataManager().getUserType();
        int userId = getDataManager().getCurrentUserId();
        getCompositeDisposable().add(getDataManager()
                .doTravelCategoryGroupApiCall(new TravelCategoryRequest.ServerTravelCategoryRequest(dataType), userType, String.valueOf(userId))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null && response.getData() != null) {
                        for (int i = 0; i < response.getData().size(); i++) {
                            if ((response.getData().get(i).getLevel3_category().equalsIgnoreCase("Domestic"))
                                    || (response.getData().get(i).getLevel3_category().equalsIgnoreCase("المنزلي"))) {
                                sortedList.add(response.getData().get(i));
                            }
                        }
                        travelListLiveData.setValue(sortedList);
                    }
                    setIsLoading(false);
                }, throwable -> {
                    setIsLoading(false);
                    Log.e("Arun ", "onViewCreated throwable");

                    getNavigator().handleError(throwable);
                }));
    }

    public LiveData<List<TravelCategoryGroupResponse.Adds>> getTravelListLiveData() {
        return travelListLiveData;
    }

    public String getUserType() {
        return getDataManager().getUserType();
    }

}
