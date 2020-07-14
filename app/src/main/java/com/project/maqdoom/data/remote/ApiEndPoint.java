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

package com.project.maqdoom.data.remote;

import com.project.maqdoom.BuildConfig;

/**
 *
 */

public final class ApiEndPoint {

    public static final String ENDPOINT_BLOG = BuildConfig.BASE_URL + "/5926ce9d11000096006ccb30";

    public static final String ENDPOINT_FACEBOOK_LOGIN = BuildConfig.BASE_URL + "/588d15d3100000ae072d2944";

    public static final String ENDPOINT_GOOGLE_LOGIN = BuildConfig.BASE_URL + "/588d14f4100000a9072d2943";

    public static final String ENDPOINT_LOGOUT = BuildConfig.BASE_URL + "/588d161c100000a9072d2946";

    public static final String ENDPOINT_OPEN_SOURCE = BuildConfig.BASE_URL + "/5926c34212000035026871cd";

    public static final String ENDPOINT_SERVER_LOGIN = BuildConfig.BASE_SERVER_URL + "/login";

    public static final String ENDPOINT_SERVER_REGISTRATION = BuildConfig.BASE_SERVER_URL + "/createUser";

    public static final String ENDPOINT_SERVER_TRAVEL_CATEGORY = BuildConfig.BASE_SERVER_URL + "/getTravelCategoryAdds";

    public static final String ENDPOINT_SERVER_CREATE_SERVICE = BuildConfig.BASE_SERVER_URL + "/createAdd";

    public static final String ENDPOINT_SERVER_EDIT_PROFILE = BuildConfig.BASE_SERVER_URL + "/editProfile";

    public static final String ENDPOINT_SERVER_DELETE_ADD = BuildConfig.BASE_SERVER_URL + "/deleteAdd";

    public static final String ENDPOINT_SERVER_REQUEST_CODE = BuildConfig.BASE_SERVER_URL + "/sendEmailVerificationCode";

    public static final String ENDPOINT_SERVER_FORGOT_PASSWORD = BuildConfig.BASE_SERVER_URL + "/changePassword";

    public static final String ENDPOINT_SERVER_GET_NOTIFICATION = BuildConfig.BASE_SERVER_URL + "/notification";

    public static final String ENDPOINT_SERVER_GET_PAYMENT_DETAILS = BuildConfig.BASE_SERVER_URL + "/SellerPay";

    public static final String ENDPOINT_SERVER_IMAGE_UPLOAD = BuildConfig.BASE_SERVER_URL + "/insertAddImage";

    private ApiEndPoint() {
        // This class is not publicly instantiable
    }
}
