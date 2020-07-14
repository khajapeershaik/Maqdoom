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

import java.io.File;


public final class ImageUploadRequest {

    private ImageUploadRequest() {
        // This class is not publicly instantiable
    }


    public static class ServerUploadImageRequest {

        @Expose
        @SerializedName("add_id")
        private String add_id;

        @SerializedName("image")
        private File image;

        public ServerUploadImageRequest(String add_id, File image) {
            this.add_id = add_id;
            this.image = image;
        }

        public String getAdd_id() {
            return add_id;
        }

        public File getImage() {
            return image;
        }
    }
}
