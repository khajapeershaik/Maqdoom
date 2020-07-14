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

package com.project.maqdoom.ui.sellerPackagePayment;

import android.text.TextUtils;

import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.ui.sellerPackages.SellerPackageNavigator;
import com.project.maqdoom.utils.CommonUtils;
import com.project.maqdoom.utils.rx.SchedulerProvider;


public class SellerPackagePaymentViewModel extends BaseViewModel<SellerPackagePaymentNavigator> {

    public SellerPackagePaymentViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
    public boolean isValid(String cardNumber, String name, String date, String cvv) {
        if (TextUtils.isEmpty(cardNumber)) {
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (TextUtils.isEmpty(date)) {
            return false;
        }
        if (TextUtils.isEmpty(cvv)) {
            return false;
        }
        return true;
    }

    public void proceedToPayment(String cardNumber, String name, String date, String cvv){
        getDataManager().setSellerStatus("1");
        getNavigator().openSellerHome();
    }
    public void onPaymentClick() {
        getNavigator().openPayment();
    }

    public void proceedWithPayment(){

        getNavigator().proceedToPayment();
    }

    public void goBackToPrevious(){
        getNavigator().goBack();
    }
}
