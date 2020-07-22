package com.project.maqdoom.ui.shopsAddPackage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.data.model.api.TravelCategoryRequest;
import com.project.maqdoom.data.model.api.TravelCategoryResponse;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.ui.customerHoneymoon.HoneymoonNavigator;
import com.project.maqdoom.utils.rx.SchedulerProvider;

import java.util.List;

public class ShopDetailsViewModel extends BaseViewModel<ShopDetailsNavigator> {

    private final MutableLiveData<List<TravelCategoryResponse.Adds>> travelListLiveData;
    public ShopDetailsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        travelListLiveData = new MutableLiveData<>();
        fetchData();
    }

    public void fetchData() {
        setIsLoading(true);
        final String userType = getDataManager().getUserType();
        int userId = getDataManager().getCurrentUserId();
        getCompositeDisposable().add(getDataManager()
                .doTravelCategoryApiCall(new TravelCategoryRequest.ServerTravelCategoryRequest("SHOPS"),userType, String.valueOf(userId))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null && response.getData() != null) {
                        travelListLiveData.setValue(response.getData());
                    }
                    setIsLoading(false);
                }, throwable -> {
                    setIsLoading(false);
                }));
    }
    public LiveData<List<TravelCategoryResponse.Adds>> getTravelListLiveData() {
        return travelListLiveData;
    }
}
