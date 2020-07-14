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

        @Expose
        @SerializedName("password")
        private String password;

        @Expose
        @SerializedName("confirm_password")
        private String confirm_password;

        @Expose
        @SerializedName("is_seller")
        private String is_seller;

        public ServerRegistrationRequest(String name,String email, String password,String confirmPassword,String isSeller) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.confirm_password=confirmPassword;
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
            return password != null ? password.equals(that.password) : that.password == null;
        }



        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getName() {
            return name;
        }

        public String getConfirm_password() {
            return confirm_password;
        }

        public String getIs_seller() {
            return is_seller;
        }
    }
}
