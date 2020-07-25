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

package com.project.maqdoom.ui.splash;

import android.content.res.Configuration;
import android.util.Log;

import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.utils.rx.SchedulerProvider;

import java.util.Locale;


public class SplashViewModel extends BaseViewModel<SplashNavigator> {

    public SplashViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void startSeeding() {
        decideNextActivity();

    }

    private void decideNextActivity() {
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(3 * 1000);
                    if (getDataManager().getCurrentUserLoggedInMode() == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
                        getNavigator().openLoginActivity();
                    } else {
                    final String userType = getDataManager().getUserType();

                        Log.v("userType",userType);
                        if ("0".equalsIgnoreCase(userType)) {
                            getNavigator().openCustomerHome();
                       }
//                        else {
//                            final String sellerStatus = getDataManager().getSellerStatus();
//                            if("0".equalsIgnoreCase(sellerStatus)){
//                                getNavigator().openSellerSubscription();
//                                //getNavigator().openSellerHome();
 //                           }
                            else{
                                getNavigator().openSellerHome();
                            }
                        }

                    // }
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();

    }
}
