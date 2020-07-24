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

package com.project.maqdoom.data;

import android.content.Context;

import com.google.gson.Gson;
import com.project.maqdoom.data.local.db.DbHelper;
import com.project.maqdoom.data.local.prefs.PreferencesHelper;
import com.project.maqdoom.data.model.api.AddServiceRequest;
import com.project.maqdoom.data.model.api.AddServiceResponse;
import com.project.maqdoom.data.model.api.DeleteAddRequest;
import com.project.maqdoom.data.model.api.DeleteAddResponse;
import com.project.maqdoom.data.model.api.EditProfileRequest;
import com.project.maqdoom.data.model.api.EditProfileResponse;
import com.project.maqdoom.data.model.api.ForgotPasswordResponse;
import com.project.maqdoom.data.model.api.ImageUploadRequest;
import com.project.maqdoom.data.model.api.ImageUploadResponse;
import com.project.maqdoom.data.model.api.LogoutResponse;
import com.project.maqdoom.data.model.api.MaqDoomLoginResponse;
import com.project.maqdoom.data.model.api.MaqdoomLoginRequest;
import com.project.maqdoom.data.model.api.NotificationResponse;
import com.project.maqdoom.data.model.api.PasswordResetRequest;
import com.project.maqdoom.data.model.api.PasswordVerificationRequest;
import com.project.maqdoom.data.model.api.RegistrationRequest;
import com.project.maqdoom.data.model.api.RegistrationResponse;
import com.project.maqdoom.data.model.api.SellerPayRequest;
import com.project.maqdoom.data.model.api.SellerPayResponse;
import com.project.maqdoom.data.model.api.TravelCategoryGroupResponse;
import com.project.maqdoom.data.model.api.TravelCategoryRequest;
import com.project.maqdoom.data.model.api.TravelCategoryResponse;
import com.project.maqdoom.data.model.db.User;
import com.project.maqdoom.data.remote.ApiHeader;
import com.project.maqdoom.data.remote.ApiHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 *
 */
@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private final ApiHelper mApiHelper;

    private final Context mContext;

    private final DbHelper mDbHelper;

    private final Gson mGson;

    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public AppDataManager(Context context, DbHelper dbHelper, PreferencesHelper preferencesHelper, ApiHelper apiHelper, Gson gson) {
        mContext = context;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
        mGson = gson;
    }

    @Override
    public Single<LogoutResponse> doLogoutApiCall() {
        return mApiHelper.doLogoutApiCall();
    }

    @Override
    public Single<MaqDoomLoginResponse> doServerLoginApiCall(MaqdoomLoginRequest.ServerLoginRequest request) {
        return mApiHelper.doServerLoginApiCall(request);
    }

    @Override
    public Single<RegistrationResponse> doServerRegistrationApiCall(RegistrationRequest.ServerRegistrationRequest request) {
        return mApiHelper.doServerRegistrationApiCall(request);
    }

    @Override
    public Single<TravelCategoryResponse> doTravelCategoryApiCall(TravelCategoryRequest.ServerTravelCategoryRequest request, String userType,String userId) {
        return mApiHelper.doTravelCategoryApiCall(request,userType,userId);
    }

    @Override
    public Single<TravelCategoryGroupResponse> doTravelCategoryGroupApiCall(TravelCategoryRequest.ServerTravelCategoryRequest request, String userType,String userId) {
        return mApiHelper.doTravelCategoryGroupApiCall(request,userType,userId);
    }

    @Override
    public Single<AddServiceResponse> doAddPackageApiCall(AddServiceRequest.ServerPackageAddRequest request) {
        return mApiHelper.doAddPackageApiCall(request);
    }

    @Override
    public Single<EditProfileResponse> doEditProfileApiCall(EditProfileRequest.ServerEditProfileRequest request) {
        return mApiHelper.doEditProfileApiCall(request);
    }

    @Override
    public Single<DeleteAddResponse> doDeleteAddApiCall(DeleteAddRequest.ServerDeleteAddRequest request) {
        return mApiHelper.doDeleteAddApiCall(request);
    }

    @Override
    public Single<ForgotPasswordResponse> doRequestCodeApiCall(PasswordVerificationRequest.ServerPasswordVerificationRequest request) {
        return mApiHelper.doRequestCodeApiCall(request);
    }

    @Override
    public Single<ForgotPasswordResponse> doForgotPasswordApiCall(PasswordResetRequest.ServerPasswordResetRequest request) {
        return mApiHelper.doForgotPasswordApiCall(request);
    }

    @Override
    public Single<NotificationResponse> doNotificationApiCall() {
        return mApiHelper.doNotificationApiCall();
    }

    @Override
    public Single<SellerPayResponse> doSellerPayApiCall(SellerPayRequest.ServerSellerPayRequest request) {
        return mApiHelper.doSellerPayApiCall(request);
    }

    @Override
    public Single<ImageUploadResponse> doUploadImageApiCall(ImageUploadRequest.ServerUploadImageRequest request) {
        return mApiHelper.doUploadImageApiCall(request);
    }

    @Override
    public String getAccessToken() {
        return mPreferencesHelper.getAccessToken();
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPreferencesHelper.setAccessToken(accessToken);
        mApiHelper.getApiHeader().getProtectedApiHeader().setAccessToken(accessToken);
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHelper.getApiHeader();
    }


    @Override
    public String getCurrentUserEmail() {
        return mPreferencesHelper.getCurrentUserEmail();
    }

    @Override
    public void setCurrentUserEmail(String email) {
        mPreferencesHelper.setCurrentUserEmail(email);
    }

    @Override
    public int getCurrentUserId() {
        return mPreferencesHelper.getCurrentUserId();
    }

    @Override
    public void setCurrentUserId(int userId) {
        mPreferencesHelper.setCurrentUserId(userId);
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return mPreferencesHelper.getCurrentUserLoggedInMode();
    }

    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode mode) {
        mPreferencesHelper.setCurrentUserLoggedInMode(mode);
    }

    @Override
    public String getCurrentUserName() {
        return mPreferencesHelper.getCurrentUserName();
    }

    @Override
    public void setCurrentUserName(String userName) {
        mPreferencesHelper.setCurrentUserName(userName);
    }

    @Override
    public String getCurrentUserProfilePicUrl() {
        return mPreferencesHelper.getCurrentUserProfilePicUrl();
    }

    @Override
    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        mPreferencesHelper.setCurrentUserProfilePicUrl(profilePicUrl);
    }

    @Override
    public void setEmail(String email) {
        mPreferencesHelper.setEmail(email);
    }

    @Override
    public String getEmail() {
        return mPreferencesHelper.getEmail();
    }

    @Override
    public void setLanguage(String language) {
        mPreferencesHelper.setEmail(language);

    }

    @Override
    public String getLanguage() {
        return mPreferencesHelper.getLanguage();
    }

    @Override
    public void setPhone(String phone) {
        mPreferencesHelper.setPhone(phone);
    }

    @Override
    public String getPhone() {
        return mPreferencesHelper.getPhone();
    }

    @Override
    public void setCreatedDate(String date) {
        mPreferencesHelper.setCreatedDate(date);
    }

    @Override
    public String getCreatedDate() {
        return mPreferencesHelper.getCreatedDate();
    }

    @Override
    public String getUserType() {
        return mPreferencesHelper.getUserType();
    }

    @Override
    public void setUserType(String user) {
        mPreferencesHelper.setUserType(user);
    }

    @Override
    public String getSellerStatus() {
        return mPreferencesHelper.getSellerStatus();
    }

    @Override
    public void setSellerStatus(String status) {
        mPreferencesHelper.setSellerStatus(status);
    }

    @Override
    public String getCreatedImage() {
        return null;
    }


    @Override
    public Observable<Boolean> insertUser(User user) {
        return mDbHelper.insertUser(user);
    }

    @Override
    public void setUserAsLoggedOut() {
        updateUserInfo(
                0,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT,
                null,
                "",
                "",
                "",
                "",
                "");
    }

    @Override
    public void updateUserInfo(int userId, LoggedInMode loggedInMode, String userName, String email, String createdDate, String user, String sellerStatus, String phone) {
        setCurrentUserId(userId);
        setCurrentUserLoggedInMode(loggedInMode);
        setCurrentUserName(userName);
        setEmail(email);
        setCreatedDate(createdDate);
        setUserType(user);
        setSellerStatus(sellerStatus);
        setPhone(phone);
    }


}
