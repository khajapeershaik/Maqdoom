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

import java.util.List;


public final class MaqDoomLoginResponse {

    @Expose
    @SerializedName("response")
    private String response;

    @Expose
    @SerializedName("message")
    private String message;


    @Expose
    @SerializedName("data")
    private User data;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        MaqDoomLoginResponse that = (MaqDoomLoginResponse) object;

        return response != null ? response.equals(that.response) : that.response == null;

    }

    public String getResponse() {
        return response;
    }

    public User getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class User {



        @Expose
        @SerializedName("id")
        private String id;

        @Expose
        @SerializedName("name")
        private String name;

        @Expose
        @SerializedName("email")
        private String email;

        @Expose
        @SerializedName("active")
        private String active;

        @Expose
        @SerializedName("is_seller")
        private String is_seller;

        @Expose
        @SerializedName("updated_at")
        private String updated_at;

        @Expose
        @SerializedName("created_at")
        private String created_at;

        @Expose
        @SerializedName("phone")
        private String phone;


        @Expose
        @SerializedName("seller_subscrption_status")
        private String seller_subscrption_status;


        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof User)) {
                return false;
            }

            User user = (User) o;
            return name.equals(user.name);

        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getActive() {
            return active;
        }

        public String getIs_seller() {
            return is_seller;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getId() {
            return id;
        }

        public String getSeller_subscrption_status() {
            return seller_subscrption_status;
        }

        public String getPhone() {
            return phone;
        }
    }
}
