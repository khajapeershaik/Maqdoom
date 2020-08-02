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


public final class RegistrationRequest {

    private RegistrationRequest() {
        // This class is not publicly instantiable
    }


    public static class ServerRegistrationRequest {

        @Expose
        @SerializedName("name")
        private String name;

        @Expose
        @SerializedName("email")
        private String email;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        @Expose
        @SerializedName("phone")
        private String phone;



        @Expose
        @SerializedName("is_seller")
        private String is_seller;

        @Expose
        @SerializedName("language")
        private String language;

        public ServerRegistrationRequest(String language,String name,String email, String phone,String isSeller) {
            this.language=language;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.is_seller=isSeller;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            ServerRegistrationRequest that = (ServerRegistrationRequest) object;

            if (email != null ? !email.equals(that.email) : that.email != null) {
                return false;
            }
            return phone != null ? phone.equals(that.phone) : that.phone == null;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getEmail() {
            return email;
        }


        public String getName() {
            return name;
        }


        public String getIs_seller() {
            return is_seller;
        }
    }
}
