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


public final class PasswordResetRequest {

    private PasswordResetRequest() {
        // This class is not publicly instantiable
    }


    public static class ServerPasswordResetRequest {

        @Expose
        @SerializedName("email")
        private String email;

        @SerializedName("password")
        private String password;

        @SerializedName("confirm_key")
        private String confirm_key;

        public ServerPasswordResetRequest(String email, String password, String confirm_key) {
            this.email = email;
            this.password = password;
            this.confirm_key = confirm_key;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getConfirm_key() {
            return confirm_key;
        }
    }
}
