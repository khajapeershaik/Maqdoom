package com.project.maqdoom.ui.shops;


import com.project.maqdoom.data.model.api.TravelCategoryGroupResponse;

import java.util.List;

public interface ShopsNavigator {
    void handleError(Throwable throwable);
    void goBack();
}
