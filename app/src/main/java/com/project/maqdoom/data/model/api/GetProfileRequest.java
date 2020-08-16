package com.project.maqdoom.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public final class GetProfileRequest {

    private GetProfileRequest() {
        // This class is not publicly instantiable
    }


    public static class GetProfile {
        @Expose
        @SerializedName("user_id")
        private int user_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public GetProfile(int user_id) {
            this.user_id = user_id;
        }
    }
}
