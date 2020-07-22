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

package com.project.maqdoom;

import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.ui.chat.ChatViewModel;
import com.project.maqdoom.ui.customerFamilyTrip.FamilyTripViewModel;
import com.project.maqdoom.ui.customerHome.CustomerHomeViewModel;
import com.project.maqdoom.ui.customerHoneymoon.TouristHoneymoonViewModel;
import com.project.maqdoom.ui.customerRentalSupplies.CustomerRentalSuppliesViewModel;
import com.project.maqdoom.ui.customerSuppliesCruises.CustomerCruiseSuppliesViewModel;
import com.project.maqdoom.ui.customerTouristGroups.TouristGroupViewModel;
import com.project.maqdoom.ui.customerTouristGroups.insideCountry.InsideCountryViewModel;
import com.project.maqdoom.ui.customerTouristGroups.outsideCountry.OutsideCountryViewModel;
import com.project.maqdoom.ui.customerTouristGuide.TouristGuideViewModel;
import com.project.maqdoom.ui.forgotPassword.ForgotPasswordViewModel;
import com.project.maqdoom.ui.friends.FriendsViewModel;
import com.project.maqdoom.ui.login.LoginViewModel;
import com.project.maqdoom.ui.notification.NotificationViewModel;
import com.project.maqdoom.ui.profile.ProfileViewModel;
import com.project.maqdoom.ui.registration.RegistrationViewModel;
import com.project.maqdoom.ui.sellerAddPackage.SellerAddPackageViewModel;
import com.project.maqdoom.ui.sellerHome.SellerHomeViewModel;
import com.project.maqdoom.ui.sellerHome.option.OptionViewModel;
import com.project.maqdoom.ui.sellerPackagePayment.SellerPackagePaymentViewModel;
import com.project.maqdoom.ui.sellerPackages.SellerPackageViewModel;
import com.project.maqdoom.ui.shops.ShopsViewModel;
import com.project.maqdoom.ui.splash.SplashViewModel;
import com.project.maqdoom.ui.touristGuideDetails.TouristGuideDetailsViewModel;
import com.project.maqdoom.ui.touristPackageDetails.TouristPackageDetailsViewModel;
import com.project.maqdoom.utils.rx.SchedulerProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


@Singleton
public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {

    private final DataManager dataManager;
    private final SchedulerProvider schedulerProvider;

    @Inject
    public ViewModelProviderFactory(DataManager dataManager,
                                    SchedulerProvider schedulerProvider) {
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            //noinspection unchecked
            return (T) new LoginViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(SplashViewModel.class)) {
            //noinspection unchecked
            return (T) new SplashViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(RegistrationViewModel.class)) {
            //noinspection unchecked
            return (T) new RegistrationViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(ForgotPasswordViewModel.class)) {
            //noinspection unchecked
            return (T) new ForgotPasswordViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(CustomerHomeViewModel.class)) {
            //noinspection unchecked
            return (T) new CustomerHomeViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(TouristGuideViewModel.class)) {
            //noinspection unchecked
            return (T) new TouristGuideViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(TouristGroupViewModel.class)) {
            //noinspection unchecked
            return (T) new TouristGroupViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(TouristHoneymoonViewModel.class)) {
            //noinspection unchecked
            return (T) new TouristHoneymoonViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(FamilyTripViewModel.class)) {
            //noinspection unchecked
            return (T) new FamilyTripViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(CustomerRentalSuppliesViewModel.class)) {
            //noinspection unchecked
            return (T) new CustomerRentalSuppliesViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(InsideCountryViewModel.class)) {
            //noinspection unchecked
            return (T) new InsideCountryViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(OutsideCountryViewModel.class)) {
            //noinspection unchecked
            return (T) new OutsideCountryViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(TouristGuideDetailsViewModel.class)) {
            //noinspection unchecked
            return (T) new TouristGuideDetailsViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(TouristPackageDetailsViewModel.class)) {
            //noinspection unchecked
            return (T) new TouristPackageDetailsViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(CustomerCruiseSuppliesViewModel.class)) {
            //noinspection unchecked
            return (T) new CustomerCruiseSuppliesViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(NotificationViewModel.class)) {
            //noinspection unchecked
            return (T) new NotificationViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            //noinspection unchecked
            return (T) new ProfileViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(SellerPackageViewModel.class)) {
            //noinspection unchecked
            return (T) new SellerPackageViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(SellerHomeViewModel.class)) {
            //noinspection unchecked
            return (T) new SellerHomeViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(SellerPackagePaymentViewModel.class)) {
            //noinspection unchecked
            return (T) new SellerPackagePaymentViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(SellerAddPackageViewModel.class)) {
            //noinspection unchecked
            return (T) new SellerAddPackageViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(FriendsViewModel.class)) {
            //noinspection unchecked
            return (T) new FriendsViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(ChatViewModel.class)) {
            //noinspection unchecked
            return (T) new ChatViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(OptionViewModel.class)) {
            //noinspection unchecked
            return (T) new OptionViewModel(dataManager, schedulerProvider);
        }
        else if (modelClass.isAssignableFrom(ShopsViewModel.class)) {
            //noinspection unchecked
            return (T) new ShopsViewModel(dataManager, schedulerProvider);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}