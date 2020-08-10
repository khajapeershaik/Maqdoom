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

        @Expose
        @SerializedName("service")
        private String service;

        public ServerPackageAddRequest(String user_id, String guide_name, String level1_category, String level2_category, String level3_category, String packageName, String package_include, String phone, String country, String location, String whatsapp_phone, String price, String people_cnt, String more_details, String city, String language, java.util.HashMap<String, String> imageList, String addId,String service) {
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
            this.image_list = imageList;
            this.add_id = addId;
            this.service = service;
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

        public String getService() {
            return service;
        }
    }

    public  static class UpdatePackageRequest {
        /*{
  "add_id":"1",
  "user_id":"1",
  "guide_name":"",
  "level1_category":"one",
  "level2_category":"two",
  "level3_category":"three",
  "package":"abcPacakge",
  "package_include":"include data",
  "phone":"9866924928",
  "country":"",
  "location":"",
  "whatsapp_phone":"9866924928",
  "price":"5550.50",
  "people_cnt":"4",
  "more_details":"More info",
  "city":"",
  "language":"en",
  "licence_pic_url":"",
  "national_id":"",
  "service":""
}*/

        @Expose
        @SerializedName("add_id")
        private String add_id;

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
        @SerializedName("licence_pic_url")
        private String licence_pic_url;

        @Expose
        @SerializedName("national_id")
        private String national_id;

        @Expose
        @SerializedName("service")
        private String service;

        @Expose
        @SerializedName("image_list")
        private java.util.HashMap<String, String> image_list;

        public UpdatePackageRequest() {
        }

        public HashMap<String, String> getImage_list() {
            return image_list;
        }

        public void setImage_list(HashMap<String, String> image_list) {
            this.image_list = image_list;
        }

        public String getAdd_id() {
            return add_id;
        }

        public void setAdd_id(String add_id) {
            this.add_id = add_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getGuide_name() {
            return guide_name;
        }

        public void setGuide_name(String guide_name) {
            this.guide_name = guide_name;
        }

        public String getLevel1_category() {
            return level1_category;
        }

        public void setLevel1_category(String level1_category) {
            this.level1_category = level1_category;
        }

        public String getLevel2_category() {
            return level2_category;
        }

        public void setLevel2_category(String level2_category) {
            this.level2_category = level2_category;
        }

        public String getLevel3_category() {
            return level3_category;
        }

        public void setLevel3_category(String level3_category) {
            this.level3_category = level3_category;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getPackage_include() {
            return package_include;
        }

        public void setPackage_include(String package_include) {
            this.package_include = package_include;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getWhatsapp_phone() {
            return whatsapp_phone;
        }

        public void setWhatsapp_phone(String whatsapp_phone) {
            this.whatsapp_phone = whatsapp_phone;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPeople_cnt() {
            return people_cnt;
        }

        public void setPeople_cnt(String people_cnt) {
            this.people_cnt = people_cnt;
        }

        public String getMore_details() {
            return more_details;
        }

        public void setMore_details(String more_details) {
            this.more_details = more_details;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getLicence_pic_url() {
            return licence_pic_url;
        }

        public void setLicence_pic_url(String licence_pic_url) {
            this.licence_pic_url = licence_pic_url;
        }

        public String getNational_id() {
            return national_id;
        }

        public void setNational_id(String national_id) {
            this.national_id = national_id;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }
}
