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


public final class SellerCategoryRequest {

    private SellerCategoryRequest() {
        // This class is not publicly instantiable
    }


    public static class ServerTravelCategoryRequest {

        @Expose
        @SerializedName("level2_category")
        private String level2_category;



        public ServerTravelCategoryRequest(String category) {
            this.level2_category = category;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            ServerTravelCategoryRequest that = (ServerTravelCategoryRequest) object;

            if (level2_category != null ? !level2_category.equals(that.level2_category) : that.level2_category != null) {
                return false;
            }
            return level2_category != null ? level2_category.equals(that.level2_category) : that.level2_category == null;
        }


        public String getLevel2_category() {
            return level2_category;
        }
    }
}
