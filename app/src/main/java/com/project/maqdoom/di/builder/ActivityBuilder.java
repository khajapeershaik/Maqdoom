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

package com.project.maqdoom.di.builder;

import com.project.maqdoom.ui.chat.ChatActivity;
import com.project.maqdoom.ui.customerFamilyTrip.FamilyTripFragmentModule;
import com.project.maqdoom.ui.customerFamilyTrip.FamilyTripFragmentProvider;
import com.project.maqdoom.ui.customerHome.CustomerHomeActivity;
import com.project.maqdoom.ui.customerHoneymoon.HoneymoonFragmentProvider;
import com.project.maqdoom.ui.customerHoneymoon.TouristHoneymoonFragmentModule;
import com.project.maqdoom.ui.customerRentalSupplies.CustomerRentalSuppliesFragmentProvider;
import com.project.maqdoom.ui.customerSuppliesCruises.CustomerCruiseSuppliesFragmentProvider;
import com.project.maqdoom.ui.customerTouristGroups.TouristGroupFragmentModule;
import com.project.maqdoom.ui.customerTouristGroups.TouristGroupFragmentProvider;
import com.project.maqdoom.ui.customerTouristGroups.insideCountry.InsideCountryFragmentProvider;
import com.project.maqdoom.ui.customerTouristGroups.outsideCountry.OutsideCountryFragmentProvider;
import com.project.maqdoom.ui.customerTouristGuide.TouristGuideFragmentProvider;
import com.project.maqdoom.ui.forgotPassword.ForgotPasswordActivity;
import com.project.maqdoom.ui.friends.FriendsFragmentProvider;
import com.project.maqdoom.ui.login.LoginActivity;
import com.project.maqdoom.ui.notification.NotificationFragmentProvider;
import com.project.maqdoom.ui.profile.ProfileFragmentProvider;
import com.project.maqdoom.ui.registration.RegistrationActivity;
import com.project.maqdoom.ui.sellerAddPackage.SellerAddPackageActivity;
import com.project.maqdoom.ui.sellerAddPackage.SellerAddPackageFragmentProvider;
import com.project.maqdoom.ui.sellerHome.SellerHomeActivity;
import com.project.maqdoom.ui.sellerHome.option.OptionDialogProvider;
import com.project.maqdoom.ui.sellerPackagePayment.SellerPackagePaymentActivity;
import com.project.maqdoom.ui.sellerPackages.SellerPackageActivity;
import com.project.maqdoom.ui.shops.ShopsFragmentModule;
import com.project.maqdoom.ui.shops.ShopsFrgamentProvoder;
import com.project.maqdoom.ui.splash.SplashActivity;
import com.project.maqdoom.ui.touristGuideDetails.TouristGuideDetailsFragmentProvider;
import com.project.maqdoom.ui.touristPackageDetails.TouristPackageDetailsFragmentProvider;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityBuilder {


    @ContributesAndroidInjector
    abstract RegistrationActivity bindRegistrationActivity();

    @ContributesAndroidInjector
    abstract ForgotPasswordActivity bindForgotPasswordActivity();

    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector(modules = {
            TouristGuideFragmentProvider.class,
            //TouristGuideFragmentModule.class,
            TouristGroupFragmentProvider.class,
            TouristGroupFragmentModule.class,
            HoneymoonFragmentProvider.class,
            TouristHoneymoonFragmentModule.class,
            FamilyTripFragmentProvider.class,
            FamilyTripFragmentModule.class,
            InsideCountryFragmentProvider.class,
            OutsideCountryFragmentProvider.class,
            TouristGuideDetailsFragmentProvider.class,
            TouristPackageDetailsFragmentProvider.class,
            CustomerRentalSuppliesFragmentProvider.class,
            CustomerCruiseSuppliesFragmentProvider.class,
            NotificationFragmentProvider.class,
            FriendsFragmentProvider.class,
            ProfileFragmentProvider.class,
            ShopsFrgamentProvoder.class
    })
    abstract CustomerHomeActivity bindCustomerHomeActivity();

    @ContributesAndroidInjector(modules = {
            TouristGuideFragmentProvider.class,
            TouristGroupFragmentProvider.class,
            TouristGroupFragmentModule.class,
            HoneymoonFragmentProvider.class,
            TouristHoneymoonFragmentModule.class,
            FamilyTripFragmentProvider.class,
            FamilyTripFragmentModule.class,
            InsideCountryFragmentProvider.class,
            OutsideCountryFragmentProvider.class,
            TouristGuideDetailsFragmentProvider.class,
            TouristPackageDetailsFragmentProvider.class,
            CustomerRentalSuppliesFragmentProvider.class,
            CustomerCruiseSuppliesFragmentProvider.class,
            NotificationFragmentProvider.class,
            ProfileFragmentProvider.class,
            SellerAddPackageFragmentProvider.class,
            FriendsFragmentProvider.class,
            OptionDialogProvider.class,
            ShopsFrgamentProvoder.class

    })
    abstract SellerHomeActivity bindSellerHomeActivity();


    @ContributesAndroidInjector
    abstract SplashActivity bindSplashActivity();


    @ContributesAndroidInjector
    abstract SellerPackageActivity bindSellerPackageActivity();

    @ContributesAndroidInjector
    abstract SellerPackagePaymentActivity bindSellerPackagePaymentActivity();

    @ContributesAndroidInjector
    abstract SellerAddPackageActivity bindSellerAddPackageActivity();

    @ContributesAndroidInjector
    abstract ChatActivity bindChatActivity();
}
