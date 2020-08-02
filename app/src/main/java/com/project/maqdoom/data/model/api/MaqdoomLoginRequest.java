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


public final class MaqdoomLoginRequest {

    private MaqdoomLoginRequest() {
        // This class is not publicly instantiable
    }


    public static class ServerLoginRequest {


        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        @Expose
        @SerializedName("phone")
        private String phone;

        @Expose
        @SerializedName("otp")
        private String otp;

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        @Expose
        @SerializedName("language")
        private String language;
        public ServerLoginRequest(String language,String phone, String otp) {
            this.language=language;
            this.phone = phone;
            this.otp = otp;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            ServerLoginRequest that = (ServerLoginRequest) object;

            if (phone != null ? !phone.equals(that.phone) : that.phone != null) {
                return false;
            }
            return otp != null ? otp.equals(that.otp) : that.otp == null;
        }

     /*   @Override
        public int hashCode() {
            int result = email != null ? email.hashCode() : 0;
            result = 31 * result + (password != null ? password.hashCode() : 0);
            return result;
        }
*/

    }
}
