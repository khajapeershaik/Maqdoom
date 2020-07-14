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


public final class DeleteAddResponse {

    @Expose
    @SerializedName("response")
    private String response;



    @Expose
    @SerializedName("message")
    private String message;


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }


        DeleteAddResponse that = (DeleteAddResponse) object;

        if (message != null ? !message.equals(that.message) : that.message != null) {
            return false;
        }

        return response != null ? response.equals(that.response) : that.response == null;

    }


    public String getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }
}
