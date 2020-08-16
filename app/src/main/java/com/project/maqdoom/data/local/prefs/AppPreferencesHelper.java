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

package com.project.maqdoom.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.di.PreferenceInfo;
import com.project.maqdoom.utils.AppConstants;

import javax.inject.Inject;

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";

    private static final String PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL";

    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";

    private static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";

    private static final String PREF_KEY_CURRENT_USER_PROFILE_PIC_URL = "PREF_KEY_CURRENT_USER_PROFILE_PIC_URL";

    private static final String PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE";

    private static final String PREF_KEY_USER_LOGGED_IN_EMAIL = "PREF_KEY_USER_LOGGED_IN_EMAIL";

    private static final String PREF_KEY_USER_LOGGED_IN_PHONE = "PREF_KEY_USER_LOGGED_IN_PHONE";

    private static final String PREF_KEY_USER_LOGGED_CREATED_DATE = "PREF_KEY_USER_LOGGED_CREATED_DATE";

    private static final String PREF_KEY_USER_TYPE = "PREF_KEY_USER_TYPE";

    private static final String PREF_KEY_SELLER_STATUS = "PREF_KEY_SELLER_STATUS";

    private static final String PREF_KEY_USER_SELECTED_LANGUAGE = "PREF_KEY_USER_SELECTED_LANGUAGE";

    private static final String PREF_KEY_IMAGE_URL = "PREF_KEY_IMAGE_URL";

    private static final String PREF_KEY_AR_USERNAME = "PREF_KEY_AR_USERNAME";

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public String getCurrentUserEmail() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_EMAIL, null);
    }

    @Override
    public void setCurrentUserEmail(String email) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_EMAIL, email).apply();
    }

    @Override
    public int getCurrentUserId() {
        int userId = mPrefs.getInt(PREF_KEY_CURRENT_USER_ID, AppConstants.NULL_INT_INDEX);
        return userId == AppConstants.NULL_INT_INDEX ? 0 : userId;
    }



    @Override
    public void setCurrentUserId(int userId) {
        int id = userId == 0 ? AppConstants.NULL_INT_INDEX : userId;
        mPrefs.edit().putInt(PREF_KEY_CURRENT_USER_ID, id).apply();
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return mPrefs.getInt(PREF_KEY_USER_LOGGED_IN_MODE,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType());
    }

    @Override
    public void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode) {
        mPrefs.edit().putInt(PREF_KEY_USER_LOGGED_IN_MODE, mode.getType()).apply();
    }

    @Override
    public String getCurrentUserName() {
        System.out.println("arun mr setCurrentUserName--"+mPrefs.getString(PREF_KEY_CURRENT_USER_NAME, null));
        return mPrefs.getString(PREF_KEY_CURRENT_USER_NAME, null);
    }

    @Override
    public void setCurrentUserName(String userName) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_NAME, userName).apply();
    }

    @Override
    public String getCurrentUserProfilePicUrl() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, null);
    }

    @Override
    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, profilePicUrl).apply();
    }

    @Override
    public void setEmail(String email) {
        mPrefs.edit().putString(PREF_KEY_USER_LOGGED_IN_EMAIL, email).apply();
    }

    @Override
    public String getEmail() {
        return mPrefs.getString(PREF_KEY_USER_LOGGED_IN_EMAIL, null);
    }

    @Override
    public void setLanguage(String language) {
        mPrefs.edit().putString(PREF_KEY_USER_SELECTED_LANGUAGE, language).apply();

    }

    @Override
    public String getLanguage() {
        return mPrefs.getString(PREF_KEY_USER_SELECTED_LANGUAGE, null);
    }

    @Override
    public void setPhone(String phone) {
        mPrefs.edit().putString(PREF_KEY_USER_LOGGED_IN_PHONE, phone).apply();
    }

    @Override
    public String getPhone() {
        return mPrefs.getString(PREF_KEY_USER_LOGGED_IN_PHONE, null);
    }

    @Override
    public void setCreatedDate(String date) {
        mPrefs.edit().putString(PREF_KEY_USER_LOGGED_CREATED_DATE, date).apply();
    }

    @Override
    public String getCreatedDate() {
        return mPrefs.getString(PREF_KEY_USER_LOGGED_CREATED_DATE, null);
    }

    @Override
    public String getUserType() {
        return mPrefs.getString(PREF_KEY_USER_TYPE, null);
    }

    @Override
    public void setUserType(String data) {
        mPrefs.edit().putString(PREF_KEY_USER_TYPE, data).apply();
    }

    @Override
    public String getSellerStatus() {
        return mPrefs.getString(PREF_KEY_SELLER_STATUS, null);
    }

    @Override
    public void setSellerStatus(String data) {
        mPrefs.edit().putString(PREF_KEY_SELLER_STATUS, data).apply();
    }

    @Override
    public String getCreatedImage() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return mPrefs.getString(PREF_KEY_IMAGE_URL, null);

    }





    @Override
    public void setImageUrl(String imageUrl) {
        mPrefs.edit().putString(PREF_KEY_IMAGE_URL, imageUrl).apply();
    }

    @Override
    public String getarUserName() {
        return mPrefs.getString(PREF_KEY_AR_USERNAME, null);

    }

    @Override
    public void setArUserName(String username) {

        mPrefs.edit().putString(PREF_KEY_AR_USERNAME, username).apply();
    }
}
