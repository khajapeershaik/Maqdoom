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

package com.project.maqdoom.data.remote;

import android.content.Context;

import com.project.maqdoom.data.model.api.AddServiceRequest;
import com.project.maqdoom.data.model.api.AddServiceResponse;
import com.project.maqdoom.data.model.api.DeleteAddRequest;
import com.project.maqdoom.data.model.api.DeleteAddResponse;
import com.project.maqdoom.data.model.api.EditProfileRequest;
import com.project.maqdoom.data.model.api.EditProfileResponse;
import com.project.maqdoom.data.model.api.ForgotPasswordResponse;
import com.project.maqdoom.data.model.api.GetProfileRequest;
import com.project.maqdoom.data.model.api.GetProfileResponse;
import com.project.maqdoom.data.model.api.ImageUploadRequest;
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
import com.project.maqdoom.data.model.api.ImageUploadResponse;

import io.reactivex.Single;


public interface ApiHelper {


    Single<LogoutResponse> doLogoutApiCall();

    Single<MaqDoomLoginResponse> doServerLoginApiCall(MaqdoomLoginRequest.ServerLoginRequest request);

    Single<RegistrationResponse> doServerRegistrationApiCall(RegistrationRequest.ServerRegistrationRequest request);

    Single<TravelCategoryResponse> doTravelCategoryApiCall(TravelCategoryRequest.ServerTravelCategoryRequest request, String userType, String userId);

    Single<TravelCategoryGroupResponse> doTravelCategoryGroupApiCall(TravelCategoryRequest.ServerTravelCategoryRequest request, String userType, String userId);

    Single<AddServiceResponse> doAddPackageApiCall(AddServiceRequest.ServerPackageAddRequest request);

    Single<AddServiceResponse> doAddPackageEditApiCall(AddServiceRequest.UpdatePackageRequest request);

    Single<EditProfileResponse> doEditProfileApiCall(EditProfileRequest.ServerEditProfileRequest request);

    Single<DeleteAddResponse> doDeleteAddApiCall(DeleteAddRequest.ServerDeleteAddRequest request);

    Single<ForgotPasswordResponse> doRequestCodeApiCall(PasswordVerificationRequest.ServerPasswordVerificationRequest request);

    Single<ForgotPasswordResponse> doForgotPasswordApiCall(PasswordResetRequest.ServerPasswordResetRequest request);

    Single<NotificationResponse> doNotificationApiCall();

    Single<SellerPayResponse> doSellerPayApiCall(SellerPayRequest.ServerSellerPayRequest request);

    Single<ImageUploadResponse> doUploadImageApiCall(ImageUploadRequest.ServerUploadImageRequest request);

    Single<SellerPayResponse> dosavePhoneOTP(MaqdoomLoginRequest.ServerLoginRequest request);

    Single<GetProfileResponse> doGetProfile(GetProfileRequest.GetProfile request);

    ApiHeader getApiHeader();


}
