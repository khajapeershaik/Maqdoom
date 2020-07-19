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

package com.project.maqdoom.ui.sellerAddPackage;


import org.json.JSONObject;

public interface SellerAddPackageNavigator {

    void handleError(Throwable throwable);

    void addPackage();

    void openSellerHome();

    void setSeller();

    void showOptions();

    void showLanguages();

    void showErrorAlert(String message);

    void goBack();

    void deletePackage(String value);

    void pickImage(Boolean isLicense);

    void getFirstResult(JSONObject data, Boolean isLicense);

    void returnResult(JSONObject data);

}
