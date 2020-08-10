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

package com.project.maqdoom.ui.profile;

import android.text.TextUtils;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.data.model.api.EditProfileRequest;
import com.project.maqdoom.data.model.api.EditProfileResponse;
import com.project.maqdoom.data.model.api.MaqdoomLoginRequest;
import com.project.maqdoom.data.model.api.ProfileResponse;
import com.project.maqdoom.data.remote.api_rest.ApiClient;
import com.project.maqdoom.data.remote.api_rest.ApiInterface;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.utils.CommonUtils;
import com.project.maqdoom.utils.rx.SchedulerProvider;

import java.io.File;

import androidx.databinding.ObservableField;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileViewModel extends BaseViewModel<ProfileNavigator> {

    private final ObservableField<String> userEmail = new ObservableField<>();
    ;
    private final ObservableField<String> userName = new ObservableField<>();
    ;
    private final ObservableField<String> date = new ObservableField<>();
    ;
    private final ObservableField<String> phone = new ObservableField<>();
    ;
    private final ObservableField<String> address = new ObservableField<>();
    ;

    public ProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        populateData();

    }

    public ObservableField<String> getUserEmail() {
        return userEmail;
    }

    public ObservableField<String> getUserName() {
        return userName;
    }

    public ObservableField<String> getDate() {
        return date;
    }

    public ObservableField<String> getPhone() {
        return phone;
    }

    public ObservableField<String> getAddress() {
        return address;
    }

    public void populateData() {
        final String currentUserName = getDataManager().getCurrentUserName();
        if (!TextUtils.isEmpty(currentUserName)) {
            userName.set(currentUserName);
        }

        final String currentUserEmail = getDataManager().getEmail();
        if (!TextUtils.isEmpty(currentUserEmail)) {
            userEmail.set(currentUserEmail);
        }

        final String crDate = getDataManager().getCreatedDate();
        if (!TextUtils.isEmpty(crDate)) {
            date.set(crDate);
        }

        final String phoneNumber = getDataManager().getPhone();
        if (!TextUtils.isEmpty(phoneNumber)) {
            phone.set(phoneNumber);
        }else{
            phone.set("Not available");
        }

        address.set("Not available");
    }
    public boolean isValid(String phone, String name, String email) {
        // validate email and password
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        if (!CommonUtils.isEmailValid(email)) {
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        return true;
    }
    public void updateProfile(String phone, String name, String email,String imageurl,String lang) {
        setIsLoading(true);
        int userId = getDataManager().getCurrentUserId();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<EditProfileResponse> profileResponse;

        if(!imageurl.equalsIgnoreCase("")) {
            File file = new File(imageurl);
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), body);
            MultipartBody.Part userid = MultipartBody.Part.createFormData("user_id", String.valueOf(userId));
            MultipartBody.Part emails = MultipartBody.Part.createFormData("email", email);
            MultipartBody.Part names = MultipartBody.Part.createFormData("name", name);
            MultipartBody.Part noti = MultipartBody.Part.createFormData("notifications", "1");
            MultipartBody.Part lan = MultipartBody.Part.createFormData("language",lang );
            MultipartBody.Part phon = MultipartBody.Part.createFormData("phone",phone );
            profileResponse = apiService.editProfile(emails,lan,phon,names,noti,userid,image);
        }else {
            MultipartBody.Part userid = MultipartBody.Part.createFormData("user_id", String.valueOf(userId));
            MultipartBody.Part emails = MultipartBody.Part.createFormData("email", email);
            MultipartBody.Part names = MultipartBody.Part.createFormData("name", name);
            MultipartBody.Part noti = MultipartBody.Part.createFormData("notifications", "1");
            MultipartBody.Part lan = MultipartBody.Part.createFormData("language", lang);
            MultipartBody.Part phon = MultipartBody.Part.createFormData("phone", phone);
            profileResponse = apiService.editProfileNoImage(emails,lan,phon,names,noti,userid);
        }
        profileResponse.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                setIsLoading(false);
                    if ("fail".equals(response.message())) {
                        getNavigator().showErrorAlert(response.message());
                    } else {
                        getDataManager().setPhone(phone);
                        getDataManager().setCurrentUserName(name);
                        getDataManager().setEmail(email);
                        getNavigator().disableEdit();
                        getNavigator().showErrorAlert(response.message());
                    }
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                setIsLoading(false);
                getNavigator().showErrorAlert(t.getMessage());
            }
        });

    }
    public void onNavBackClick() {
        getNavigator().goBack();
    }

    public void onNavLogoutClick() {
        getDataManager().setUserAsLoggedOut();
        getNavigator().logout();
    }

    public void onTouristGroupClick() {
        getNavigator().goToTouristGroup();
    }

    public void onEditClick() {
        getNavigator().enableEdit();
    }

    public void onUpdateProfile() {
        getNavigator().updateProfile();
    }

    public void onCancelEdit() {
        getNavigator().disableEdit();

    }

    public void onSelectImage() {
        getNavigator().pickImage();
    }
}
