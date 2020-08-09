package com.project.maqdoom.data.remote.api_rest;

import com.project.maqdoom.data.model.api.DeleteAddRequest;
import com.project.maqdoom.data.model.api.DeleteAddResponse;
import com.project.maqdoom.data.model.api.EditProfileRequest;
import com.project.maqdoom.data.model.api.EditProfileResponse;
import com.project.maqdoom.data.model.api.ProfileResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST ("deleteAdd")
    Call<DeleteAddResponse> deleteAdd(@Body DeleteAddRequest.ServerDeleteAddRequest request);

    @POST("editUserProfile")
    @Multipart
    Call<EditProfileResponse> editProfile(@Part MultipartBody.Part email,@Part MultipartBody.Part language,@Part MultipartBody.Part phone,
                                          @Part MultipartBody.Part name,@Part MultipartBody.Part notifications,@Part MultipartBody.Part user_id,
                                          @Part MultipartBody.Part image);

    @POST("editUserProfile")
    @Multipart
    Call<EditProfileResponse> editProfileNoImage(@Part MultipartBody.Part email,@Part MultipartBody.Part language,@Part MultipartBody.Part phone,
                                          @Part MultipartBody.Part name,@Part MultipartBody.Part notifications,@Part MultipartBody.Part user_id);

}

