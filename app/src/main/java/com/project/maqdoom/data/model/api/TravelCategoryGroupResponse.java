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


public final class TravelCategoryGroupResponse {

    @Expose
    @SerializedName("response")
    private String response;

    @Expose
    @SerializedName("adds")
    private List<Adds> data;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }


        TravelCategoryGroupResponse that = (TravelCategoryGroupResponse) object;

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
        @SerializedName("add_id")
        private String add_id;

        @Expose
        @SerializedName("guide_name")
        private String guide_name;

        @Expose
        @SerializedName("level1_category")
        private String level1_category;

        @Expose
        @SerializedName("level2_category")
        private String level2_category;

        @Expose
        @SerializedName("level3_category")
        private String level3_category;

        @Expose
        @SerializedName("package")
        private String tourist_package;

        @Expose
        @SerializedName("package_include")
        private String package_include;

        @Expose
        @SerializedName("phone")
        private String phone;

        @Expose
        @SerializedName("country")
        private String country;

        @Expose
        @SerializedName("language")
        private String language;

        @Expose
        @SerializedName("services")
        private String services;

        @Expose
        @SerializedName("city")
        private String city;

        @Expose
        @SerializedName("location")
        private String location;

        @Expose
        @SerializedName("whatsapp_phone")
        private String whatsapp_phone;

        @Expose
        @SerializedName("price")
        private String price;

        @Expose
        @SerializedName("people_cnt")
        private String people_cnt;

        @Expose
        @SerializedName("more_details")
        private String more_details;

        @Expose
        @SerializedName("updated_at")
        private String updated_at;

        @Expose
        @SerializedName("created_at")
        private String created_at;

        @Expose
        @SerializedName("image_path")
        private List<Images> image_path;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TravelCategoryGroupResponse.Adds)) {
                return false;
            }

            TravelCategoryGroupResponse.Adds adds = (TravelCategoryGroupResponse.Adds) o;

            return add_id.equals(adds.add_id);

        }

        public String getServices() {
            return services;
        }
        public String getAdd_id() {
            return add_id;
        }

        public String getLevel1_category() {
            return level1_category;
        }

        public String getLevel2_category() {
            return level2_category;
        }

        public String getLevel3_category() {
            return level3_category;
        }

        public String getTourist_package() {
            return tourist_package;
        }

        public String getPackage_include() {
            return package_include;
        }

        public String getPhone() {
            return phone;
        }

        public String getCountry() {
            return country;
        }

        public String getLanguage() {
            return language;
        }

        public String getCity() {
            return city;
        }

        public String getLocation() {
            return location;
        }

        public String getWhatsapp_phone() {
            return whatsapp_phone;
        }

        public String getPrice() {
            return price;
        }

        public String getPeople_cnt() {
            return people_cnt;
        }

        public String getMore_details() {
            return more_details;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getGuide_name() {
            return guide_name;
        }

        public List<Images> getImage_path() {
            return image_path;
        }

        public static class Images {

            @Expose
            @SerializedName("path")
            private String path;

            public String getPath() {
                return path;
            }
        }
    }
}
