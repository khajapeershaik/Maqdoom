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

package com.project.maqdoom.ui.sellerAddPackage;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.androidnetworking.error.ANError;
import com.project.maqdoom.data.DataManager;
import com.project.maqdoom.data.model.api.AddServiceRequest;
import com.project.maqdoom.data.remote.ApiEndPoint;
import com.project.maqdoom.ui.base.BaseViewModel;
import com.project.maqdoom.utils.VolleyMultipartRequest;
import com.project.maqdoom.utils.rx.SchedulerProvider;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SellerAddPackageViewModel extends BaseViewModel<SellerAddPackageNavigator> {

    public SellerAddPackageViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public boolean validateSpinner(int spinnerType, String value) {

        if (spinnerType == 1) {
            if ("Please select category".equalsIgnoreCase(value)) {
                return false;
            }
        } else if (spinnerType == 2) {
            if ("Please select country".equalsIgnoreCase(value)) {
                return false;
            }
        } else if (spinnerType == 3) {
            if ("Please select city".equalsIgnoreCase(value)) {
                return false;
            }
        } else if (spinnerType == 4) {
            if ("Please select Language".equalsIgnoreCase(value)) {
                return false;
            }
        } else if (spinnerType == 5) {
            if ("Please select sub category".equalsIgnoreCase(value)) {
                return false;
            }
        }

        return true;
    }

    public boolean isValidFields(String name, String phone, String whatsApp, String more, String city) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        if (TextUtils.isEmpty(whatsApp)) {
            return false;
        }
        if (TextUtils.isEmpty(more)) {
            return false;
        }
        if (TextUtils.isEmpty(city)) {
            return false;
        }
        return true;
    }

    /*public boolean isValid(String name, String phone, String whatsApp, String more, String price, String people_cnt) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        if (TextUtils.isEmpty(whatsApp)) {
            return false;
        }
        if (TextUtils.isEmpty(more)) {
            return false;
        }
        if (TextUtils.isEmpty(price)) {
            return false;
        }
        if (TextUtils.isEmpty(people_cnt)) {
            return false;
        }
        return true;
    }*/

    public boolean isValid(String name, String phone, String whatsApp, String more, String price, String people_cnt,String location) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        if (TextUtils.isEmpty(whatsApp)) {
            return false;
        }
        if (TextUtils.isEmpty(more)) {
            return false;
        }
        if (TextUtils.isEmpty(price)) {
            return false;
        }
        if (TextUtils.isEmpty(people_cnt)) {
            return false;
        }
        if (TextUtils.isEmpty(location)) {
            return false;
        }
        return true;
    }
    public void addPackage(String guide_name, String level1_category, String level2_category, String level3_category, String packageName, String package_include, String phone, String country, String location, String whatsapp_phone, String price, String people_cnt, String more_details, String city, String language, String addId,java.util.HashMap<String, String> imageList,String services,String nationalId) {
        setIsLoading(true);
        int userId = getDataManager().getCurrentUserId();
        getCompositeDisposable().add(getDataManager()
                .doAddPackageApiCall(new AddServiceRequest.ServerPackageAddRequest(String.valueOf(userId), guide_name, level1_category, level2_category, level3_category, packageName, package_include, phone, country, location, whatsapp_phone, price, people_cnt, more_details, city, language,imageList,addId,services,nationalId))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    if ("fail".equals(response.getResponse())) {
                        getNavigator().showErrorAlert(response.getMessage());
                    } else {
                        getNavigator().showErrorAlert(response.getMessage());
                        getNavigator().openSellerHome();
                    }
                }, throwable -> {
                    if (throwable instanceof ANError) {
                        ANError anError = (ANError) throwable;
                        if (anError.getErrorCode() != 0) {
                            // received ANError from server
                            // error.getErrorCode() - the ANError code from server
                            // error.getErrorBody() - the ANError body from server
                            // error.getErrorDetail() - just a ANError detail
                            Log.d("TAG", "onError errorCode : " + anError.getErrorCode());
                            Log.d("TAG", "onError errorBody : " + anError.getErrorBody());
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    } else {
                        Log.d("TAG", "onError errorMessage : " + throwable.getMessage());
                    }
                    throwable.printStackTrace();
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));
    }

    public void upDatePackage(AddServiceRequest.UpdatePackageRequest updatePackageRequest) {
        setIsLoading(true);
        getCompositeDisposable().add(getDataManager()
                .doAddPackageEditApiCall(updatePackageRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    if ("fail".equals(response.getResponse())) {
                        getNavigator().showErrorAlert(response.getMessage());
                    } else {
                        getNavigator().showErrorAlert(response.getMessage());
                        getNavigator().setSeller();
                    }
                }, throwable -> {
                    if (throwable instanceof ANError) {
                        ANError anError = (ANError) throwable;
                        if (anError.getErrorCode() != 0) {
                            // received ANError from server
                            // error.getErrorCode() - the ANError code from server
                            // error.getErrorBody() - the ANError body from server
                            // error.getErrorDetail() - just a ANError detail
                            Log.d("TAG", "onError errorCode : " + anError.getErrorCode());
                            Log.d("TAG", "onError errorBody : " + anError.getErrorBody());
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    } else {
                        Log.d("TAG", "onError errorMessage : " + throwable.getMessage());
                    }
                    throwable.printStackTrace();
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));
    }
    public void InitialMediaUploadRequest(RequestQueue reqQueue, Context context, String path, String addId, String imageIndex, Boolean isLicense) {
        System.out.println("Arun -path :"+path);
        System.out.println("Arun -addId :"+addId);
        System.out.println("Arun -imageIndex :"+imageIndex);
        setIsLoading(true);
        int userId = getDataManager().getCurrentUserId();
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                ApiEndPoint.ENDPOINT_SERVER_IMAGE_UPLOAD, response -> {
                    Log.e("Arun onResponse 1: ", response.toString());
                    setIsLoading(false);
                    try {
                        if ("success".equalsIgnoreCase(response.optString("response"))){
                            //getNavigator().showErrorAlert(response.optString("response"));
                            getNavigator().getFirstResult(response,isLicense);
                        }else {
                            getNavigator().showErrorAlert(response.getString("response"));
                        }
                    } catch (JSONException e) {
                        getNavigator().showErrorAlert("Something went wrong  !!");
                    }
                }, error -> {
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                getNavigator().showErrorAlert("Something went wrong  !!");
            }
        }) {
            @Override
            protected Map<String, ArrayList<DataPart>> getByteData() {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                Map<String, ArrayList<DataPart>> imageList = new HashMap<>();
                ArrayList<DataPart> dataPart = new ArrayList<>();
                String fileName = "";
                String extension ="";
                byte[] inputData = new byte[0];
                //for (int i=0; i<selectedList.size(); i++){
                Uri picUri = Uri.parse("file:///"+path);
                InputStream iStream = null;
                try {
                    iStream = context.getContentResolver().openInputStream(picUri);
                    inputData = getBytes(iStream);
                    File tempFile = new File("file:///"+path);
                    fileName = tempFile.getName();
                    extension = MimeTypeMap.getFileExtensionFromUrl(picUri.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                VolleyMultipartRequest.DataPart dp = new VolleyMultipartRequest.DataPart(fileName, inputData, "image/"+extension);
                dataPart.add(dp);
                imageList.put("image", dataPart);
                return imageList;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("add_id", addId);
                params.put("seller_id", Integer.toString(userId));
                params.put("img_index", imageIndex);
                return params;
            }
        };
        int socketTimeout = 5000000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
        reqQueue.add(multipartRequest);
    }
    public void uploadMedias(RequestQueue reqQueue,Context context, String path,String addId,String imageIndex) {

        System.out.println("Arun -path :"+path);
        System.out.println("Arun -addId :"+addId);
        System.out.println("Arun -imageIndex :"+imageIndex);
        setIsLoading(true);
        int userId = getDataManager().getCurrentUserId();
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                ApiEndPoint.ENDPOINT_SERVER_IMAGE_UPLOAD, response -> {
                    Log.e("Arun onResponse 2: ", response.toString());
                    setIsLoading(false);
                    try {
                        if ("success".equalsIgnoreCase(response.optString("response"))){
                            //getNavigator().showErrorAlert(response.optString("response"));
                            getNavigator().returnResult(response);
                        }else {
                            getNavigator().showErrorAlert(response.getString("response"));
                        }
                    } catch (JSONException e) {
                        getNavigator().showErrorAlert("Something went wrong  !!");
                    }
                }, error -> {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        getNavigator().showErrorAlert("Something went wrong  !!");
                    }
                }) {

            @Override
            protected Map<String, ArrayList<DataPart>> getByteData() {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                Map<String, ArrayList<DataPart>> imageList = new HashMap<>();
                ArrayList<DataPart> dataPart = new ArrayList<>();
                String fileName = "";
                String extension ="";
                byte[] inputData = new byte[0];
                //for (int i=0; i<selectedList.size(); i++){
                Uri picUri = Uri.parse("file:///"+path);
                InputStream iStream = null;
                try {
                    iStream = context.getContentResolver().openInputStream(picUri);
                    inputData = getBytes(iStream);
                    File tempFile = new File("file:///"+path);
                    fileName = tempFile.getName();
                    extension = MimeTypeMap.getFileExtensionFromUrl(picUri.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                VolleyMultipartRequest.DataPart dp = new VolleyMultipartRequest.DataPart(fileName, inputData, "image/"+extension);
                dataPart.add(dp);
                // }
                imageList.put("image", dataPart);
                return imageList;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("add_id", addId);
                params.put("seller_id", Integer.toString(userId));
                params.put("img_index", imageIndex);
                return params;
            }
        };
        int socketTimeout = 5000000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
        reqQueue.add(multipartRequest);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    public void onSubmitButtonClick() {
        getNavigator().addPackage();
    }

    public void goBackToPrevious() {
        getNavigator().goBack();
    }

    public void showOptionAlert() {
        getNavigator().showOptions();
    }

    public void showLanguageAlert() {
        getNavigator().showLanguages();
    }

    public void onDeletePackage(String name) {
        getNavigator().deletePackage(name);
    }

    public void onSelectImage() {
        Boolean isLicense = false;
        getNavigator().pickImage(isLicense);
    }
    public void onSelectLicenseImage() {
        Boolean isLicense =true;
        getNavigator().pickImage(isLicense);
    }
}
