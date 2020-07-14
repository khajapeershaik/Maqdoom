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

import java.util.HashMap;
import java.util.List;


public final class AddServiceRequest {

    private AddServiceRequest() {
        // This class is not publicly instantiable
    }


    public static class ServerPackageAddRequest {

        @Expose
        @SerializedName("user_id")
        private String user_id;

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
        private String packageName;

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
        @SerializedName("city")
        private String city;

        @Expose
        @SerializedName("language")
        private String language;

        @Expose
        @SerializedName("image_list")
        private java.util.HashMap<String, String> image_list;

        @Expose
        @SerializedName("add_id")
        private String add_id;

        public ServerPackageAddRequest(String user_id, String guide_name, String level1_category, String level2_category, String level3_category, String packageName, String package_include, String phone, String country, String location, String whatsapp_phone, String price, String people_cnt, String more_details, String city, String language,java.util.HashMap<String, String> imageList, String addId ) {
            this.user_id = user_id;
            this.guide_name = guide_name;
            this.level1_category = level1_category;
            this.level2_category = level2_category;
            this.level3_category = level3_category;
            this.packageName = packageName;
            this.package_include = package_include;
            this.phone = phone;
            this.country = country;
            this.location = location;
            this.whatsapp_phone = whatsapp_phone;
            this.price = price;
            this.people_cnt = people_cnt;
            this.more_details = more_details;
            this.city = city;
            this.language = language;
            this.image_list= imageList;
            this.add_id = addId;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getGuide_name() {
            return guide_name;
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

        public String getPackageName() {
            return packageName;
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

        public String getCity() {
            return city;
        }

        public String getLanguage() {
            return language;
        }

        public HashMap<String, String> getImage_list() {
            return image_list;
        }

        public String getAdd_id() {
            return add_id;
        }
    }
}
