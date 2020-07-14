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


public final class SellerPayRequest {

    private SellerPayRequest() {
        // This class is not publicly instantiable
    }


    public static class ServerSellerPayRequest {

        @Expose
        @SerializedName("seller_id")
        private String seller_id;

        @Expose
        @SerializedName("transaction_id")
        private String transaction_id;

        @Expose
        @SerializedName("amt")
        private String amt;

        public ServerSellerPayRequest(String seller_id, String transaction_id, String amt) {
            this.seller_id = seller_id;
            this.transaction_id = transaction_id;
            this.amt = amt;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public String getAmt() {
            return amt;
        }
    }
}
