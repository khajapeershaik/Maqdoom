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

package com.project.maqdoom.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public final class SaveUserTokenRequest {

    private SaveUserTokenRequest() {
        // This class is not publicly instantiable
    }


    public static class ServerSaveUserTokenRequest {

        @Expose
        @SerializedName("user_id")
        private String user_id;

        @Expose
        @SerializedName("token")
        private String token;

        @Expose
        @SerializedName("device")
        private String device;


        public ServerSaveUserTokenRequest(String user_id, String token, String device) {
            this.user_id = user_id;
            this.token = token;
            this.device = device;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }
    }
}