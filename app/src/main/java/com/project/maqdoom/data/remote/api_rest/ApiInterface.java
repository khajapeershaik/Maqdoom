package com.project.maqdoom.data.remote.api_rest;

import com.project.maqdoom.data.model.api.DeleteAddRequest;
import com.project.maqdoom.data.model.api.DeleteAddResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST ("deleteAdd")
    Call<DeleteAddResponse> deleteAdd(@Body DeleteAddRequest.ServerDeleteAddRequest request);
}

