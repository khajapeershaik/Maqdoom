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

package com.project.maqdoom.ui.customerTouristGroups.outsideCountry;

import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.data.model.api.TravelCategoryGroupResponse;
import com.project.maqdoom.data.model.api.TravelCategoryRequest;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class OutsideCountryViewModel extends BaseViewModel<OutsideCountryNavigator> {

    private final MutableLiveData<List<TravelCategoryGroupResponse.Adds>> travelListLiveData;
    private final List<TravelCategoryGroupResponse.Adds> sortedList;

    public OutsideCountryViewModel(DataManager dataManager,
                                   SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        travelListLiveData = new MutableLiveData<>();
        sortedList = new ArrayList<>();

    }

    public void fetchData(String type) {
        setIsLoading(true);
        final String userType = getDataManager().getUserType();
        int userId = getDataManager().getCurrentUserId();
        getCompositeDisposable().add(getDataManager()
                .doTravelCategoryGroupApiCall(new TravelCategoryRequest.ServerTravelCategoryRequest(type), userType, String.valueOf(userId))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null && response.getData() != null) {
                        for (int i = 0; i < response.getData().size(); i++) {
                            if ((response.getData().get(i).getLevel3_category().equalsIgnoreCase("International"))
                                    || (response.getData().get(i).getLevel3_category().equalsIgnoreCase("دولي"))) {
                                sortedList.add(response.getData().get(i));
                            }
                        }
                        travelListLiveData.setValue(sortedList);
                    }
                    setIsLoading(false);
                }, throwable -> {
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));
    }

    public LiveData<List<TravelCategoryGroupResponse.Adds>> getTravelOutSideCountryListLiveData() {
        return travelListLiveData;
    }
}
