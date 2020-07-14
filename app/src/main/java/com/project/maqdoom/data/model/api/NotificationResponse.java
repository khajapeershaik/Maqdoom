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


public final class NotificationResponse {

    @Expose
    @SerializedName("response")
    private String response;

    @Expose
    @SerializedName("data")
    private List<Adds> data;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }


        NotificationResponse that = (NotificationResponse) object;

        if (response != null ? !response.equals(that.response) : that.response != null) {
            return false;
        }

        return response != null ? response.equals(that.response) : that.response == null;

    }


    public String getResponse() {
        return response;
    }

    public List<Adds> getData() {
        return data;
    }

    public static class Adds {



        @Expose
        @SerializedName("package")
        private String tourist_package;

        @Expose
        @SerializedName("price")
        private String price;



        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof NotificationResponse.Adds)) {
                return false;
            }

            NotificationResponse.Adds adds = (NotificationResponse.Adds) o;

            return price.equals(adds.price);

        }

        public String getTourist_package() {
            return tourist_package;
        }

        public String getPrice() {
            return price;
        }


    }
}
