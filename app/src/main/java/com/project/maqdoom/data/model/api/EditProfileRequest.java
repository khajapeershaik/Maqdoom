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


public final class EditProfileRequest {

    private EditProfileRequest() {
        // This class is not publicly instantiable
    }


    public static class ServerEditProfileRequest {

        @Expose
        @SerializedName("user_id")
        private String user_id;

        @Expose
        @SerializedName("email")
        private String email;

        @Expose
        @SerializedName("name")
        private String name;

        @Expose
        @SerializedName("phone")
        private String phone;

        @Expose
        @SerializedName("notifications")
        private String notifications;

        @Expose
        @SerializedName("language")
        private String language;

        public ServerEditProfileRequest(String user_id, String email, String name, String phone, String notifications) {
            this.user_id = user_id;
            this.email = email;
            this.name = name;
            this.phone = phone;
            this.notifications = notifications;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getNotifications() {
            return notifications;
        }
    }
}
