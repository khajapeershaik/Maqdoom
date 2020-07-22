package com.project.maqdoom.ui.shops;

import android.util.Log;

import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.data.model.api.TravelCategoryGroupResponse;
import com.project.maqdoom.data.model.api.TravelCategoryRequest;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.utils.rx.SchedulerProvider;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ShopsViewModel extends BaseViewModel<ShopsNavigator> {

    private final MutableLiveData<List<TravelCategoryGroupResponse.Adds>> shopsListLiveData;

    public ShopsViewModel(DataManager dataManager,
                                  SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        shopsListLiveData = new MutableLiveData<>();
        fetchData();
    }

    public void fetchData() {
        setIsLoading(true);
        final String userType = getDataManager().getUserType();
        int userId = getDataManager().getCurrentUserId();
        Log.v("userId",""+userId);
        getCompositeDisposable().add(getDataManager()
                .doTravelCategoryGroupApiCall(new TravelCategoryRequest.ServerTravelCategoryRequest(""),userType, String.valueOf(userId))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null && response.getData() != null) {
                        Log.e("Arun ","onViewCreated getData");
                        /*for(int i=0;i<response.getData().size(); i++){
                            if(!"IC".equalsIgnoreCase(response.getData().get(i).getLevel3_category())){
                                response.getData().remove(i);
                                Log.e("Arun ","onViewCreated response.getData().remove(i)");
                            }
                        }*/
                        shopsListLiveData.setValue(response.getData());
                    }
                    setIsLoading(false);
                }, throwable -> {
                    setIsLoading(false);
                    Log.e("Arun ","onViewCreated throwable");

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
