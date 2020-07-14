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

package com.project.maqdoom.ui.customerTouristGroups;

import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.utils.rx.SchedulerProvider;

/**
 * Created by Jyoti on 29/07/17.
 */

public class TouristGroupViewModel extends BaseViewModel<TouristGroupNavigator> {

    public TouristGroupViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void onNavBackClick() {
        getNavigator().goBack();
    }

    public void onTouristGuideClick() {
        getNavigator().goToTouristGuide();
    }

    public void onHoneymoonClick() {
        getNavigator().gotoHoneymoon();
    }

    public void onFamilyTripClick() {
        getNavigator().gotoFamilyTrip();
    }

}
