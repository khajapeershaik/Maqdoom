package com.project.maqdoom.ui.shops;


import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.data.model.api.TravelCategoryGroupResponse;
import com.project.maqdoom.data.model.api.TravelCategoryRequest;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ShopsViewModel extends BaseViewModel<ShopsNavigator> {

    private final MutableLiveData<List<TravelCategoryGroupResponse.Adds>> shopsListLiveData;

    List<TravelCategoryGroupResponse.Adds>shopsList;
    public ShopsViewModel(DataManager dataManager,
                                  SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        shopsListLiveData = new MutableLiveData<>();
        shopsList = new ArrayList<>();
        fetchData();
    }

    public void fetchData() {
        setIsLoading(true);
        final String userType = getDataManager().getUserType();
        int userId = getDataManager().getCurrentUserId();
        getCompositeDisposable().add(getDataManager()
                .doTravelCategoryGroupApiCall(new TravelCategoryRequest.ServerTravelCategoryRequest(""),userType, String.valueOf(userId))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null && response.getData() != null) {
                        for(int i=0;i<response.getData().size(); i++){
                            if(response.getData().get(i).getLevel1_category().equalsIgnoreCase("shops")){
                                shopsList.add(response.getData().get(i));
                            }
                        }
                        shopsListLiveData.setValue(shopsList);
                    }
                    setIsLoading(false);
                }, throwable -> {
                    setIsLoading(false);

                    getNavigator().handleError(throwable);
                }));
    }

    public LiveData<List<TravelCategoryGroupResponse.Adds>> getShopsListLiveData() {
        return shopsListLiveData;
    }
    public void onNavBackClick() {
        getNavigator().goBack();
    }

}
