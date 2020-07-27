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

import com.androidnetworking.common.Priority;
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
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

/**
 *
 */

@Singleton
public class AppApiHelper implements ApiHelper {

    private ApiHeader mApiHeader;

    @Inject
    public AppApiHelper(ApiHeader apiHeader) {
        mApiHeader = apiHeader;
    }


    @Override
    public Single<LogoutResponse> doLogoutApiCall() {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_LOGOUT)
                .addHeaders(mApiHeader.getProtectedApiHeader())
                .build()
                .getObjectSingle(LogoutResponse.class);
    }

    @Override
    public Single<MaqDoomLoginResponse> doServerLoginApiCall(MaqdoomLoginRequest.ServerLoginRequest request) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", request.getEmail());
            jsonObject.put("password", request.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_LOGIN)
                .setContentType("application/json; charset=utf-8")
                .addHeaders(mApiHeader.getPublicApiHeader())
                //.addBodyParameter(request)
                .addJSONObjectBody(jsonObject)
                .build()
                .getObjectSingle(MaqDoomLoginResponse.class);
    }

    @Override
    public Single<RegistrationResponse> doServerRegistrationApiCall(RegistrationRequest.ServerRegistrationRequest request) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("language", request.getLanguage());
            jsonObject.put("name", request.getName());
            jsonObject.put("email", request.getEmail());
            jsonObject.put("password", request.getPassword());
            jsonObject.put("confirm_password", request.getConfirm_password());
            jsonObject.put("is_seller", request.getIs_seller());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_REGISTRATION)
                .setContentType("application/json; charset=utf-8")
                //.addBodyParameter(request)
                .addJSONObjectBody(jsonObject)
                .build()
                .getObjectSingle(RegistrationResponse.class);
    }

    @Override
    public Single<TravelCategoryResponse> doTravelCategoryApiCall(TravelCategoryRequest.ServerTravelCategoryRequest request, String userType, String userId) {

        JSONObject jsonObject = new JSONObject();
        try {
            if ("1".equalsIgnoreCase(userType)) {
                jsonObject.put("user_id", userId);
            }
            jsonObject.put("level2_category", request.getLevel2_category());
            jsonObject.put("language", request.getLanguage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_TRAVEL_CATEGORY)
                .setContentType("application/json; charset=utf-8")
                //.addBodyParameter(request)
                .addJSONObjectBody(jsonObject)
                .build()
                .getObjectSingle(TravelCategoryResponse.class);
    }

    @Override
    public Single<TravelCategoryGroupResponse> doTravelCategoryGroupApiCall(TravelCategoryRequest.ServerTravelCategoryRequest request, String userType, String userId) {
        JSONObject jsonObject = new JSONObject();
        try {
            if ("1".equalsIgnoreCase(userType)) {
                jsonObject.put("user_id", userId);
            }
            jsonObject.put("level2_category", request.getLevel2_category());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_TRAVEL_CATEGORY)
                .setContentType("application/json; charset=utf-8")
                //.addBodyParameter(request)
                .addJSONObjectBody(jsonObject)
                .build()
                .getObjectSingle(TravelCategoryGroupResponse.class);
    }

    @Override
    public Single<AddServiceResponse> doAddPackageApiCall(AddServiceRequest.ServerPackageAddRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("add_id", request.getAdd_id());
            jsonObject.put("user_id", request.getUser_id());
            jsonObject.put("guide_name", request.getGuide_name());
            jsonObject.put("level1_category", request.getLevel1_category());
            jsonObject.put("level2_category", request.getLevel2_category());
            jsonObject.put("level3_category", request.getLevel3_category());
            jsonObject.put("package", request.getPackageName());
            jsonObject.put("package_include", request.getPackage_include());
            jsonObject.put("phone", request.getPhone());
            jsonObject.put("country", request.getCountry());
            jsonObject.put("location", request.getLocation());
            jsonObject.put("whatsapp_phone", request.getWhatsapp_phone());
            jsonObject.put("price", request.getPrice());
            jsonObject.put("people_cnt", request.getPeople_cnt());
            jsonObject.put("more_details", request.getMore_details());
            jsonObject.put("city", request.getCity());
            jsonObject.put("language", request.getLanguage());
            JSONArray jsonArray = new JSONArray();
            JSONObject obj = null;
            if (request.getImage_list() != null) {
                for (String value : request.getImage_list().values()) {
                    obj = new JSONObject();
                    try {
                        obj.put("image", value);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    jsonArray.put(obj);
                }
               /* for(int j=0; j<request.getImage_list().size(); j++)
                {
                    obj = new JSONObject();
                    try {
                        obj.put("image", request.getImage_list().get(j));

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    jsonArray.put(obj);
                }*/
            }
            jsonObject.put("image_path", jsonArray);
            System.out.println("Arun json --" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_CREATE_SERVICE)
                .setContentType("application/json; charset=utf-8")
                //.addBodyParameter(request)
                .addJSONObjectBody(jsonObject)
                .build()
                .getObjectSingle(AddServiceResponse.class);
    }

    @Override
    public Single<EditProfileResponse> doEditProfileApiCall(EditProfileRequest.ServerEditProfileRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("language",request.getLanguage());
            jsonObject.put("user_id", request.getUser_id());
            jsonObject.put("name", request.getName());
            jsonObject.put("email", request.getEmail());
            jsonObject.put("phone", request.getPhone());
            jsonObject.put("notification", request.getNotifications());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_EDIT_PROFILE)
                .setContentType("application/json; charset=utf-8")
                .addJSONObjectBody(jsonObject)
                .build()
                .getObjectSingle(EditProfileResponse.class);
    }

    @Override
    public Single<DeleteAddResponse> doDeleteAddApiCall(DeleteAddRequest.ServerDeleteAddRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("add_id", request.getAdd_id());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_DELETE_ADD)
                .setContentType("application/json; charset=utf-8")
                .addJSONObjectBody(jsonObject)
                .build()
                .getObjectSingle(DeleteAddResponse.class);
    }

    @Override
    public Single<ForgotPasswordResponse> doRequestCodeApiCall(PasswordVerificationRequest.ServerPasswordVerificationRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", request.getEmail());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_REQUEST_CODE)
                .setContentType("application/json; charset=utf-8")
                .addJSONObjectBody(jsonObject)
                .build()
                .getObjectSingle(ForgotPasswordResponse.class);
    }

    @Override
    public Single<ForgotPasswordResponse> doForgotPasswordApiCall(PasswordResetRequest.ServerPasswordResetRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", request.getEmail());
            jsonObject.put("password", request.getPassword());
            jsonObject.put("confirm_key", request.getConfirm_key());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_FORGOT_PASSWORD)
                .setContentType("application/json; charset=utf-8")
                .addJSONObjectBody(jsonObject)
                .build()
                .getObjectSingle(ForgotPasswordResponse.class);
    }

    @Override
    public Single<NotificationResponse> doNotificationApiCall() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SERVER_GET_NOTIFICATION)
                .build()
                .getObjectSingle(NotificationResponse.class);
    }

    @Override
    public Single<SellerPayResponse> doSellerPayApiCall(SellerPayRequest.ServerSellerPayRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("seller_id", request.getSeller_id());
            jsonObject.put("transaction_id", request.getTransaction_id());
            jsonObject.put("amt", request.getAmt());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_GET_PAYMENT_DETAILS)
                .setContentType("application/json; charset=utf-8")
                .addJSONObjectBody(jsonObject)
                .build()
                .getObjectSingle(SellerPayResponse.class);
    }

    @Override
    public Single<ImageUploadResponse> doUploadImageApiCall(ImageUploadRequest.ServerUploadImageRequest request) {

        return Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_SERVER_IMAGE_UPLOAD)
                .setPriority(Priority.HIGH)
                .addMultipartFile("image", request.getImage())
                .addMultipartParameter("add_id", request.getAdd_id())
                .build()
                .getObjectSingle(ImageUploadResponse.class);
    }


    @Override
    public ApiHeader getApiHeader() {
        return mApiHeader;
    }


}
