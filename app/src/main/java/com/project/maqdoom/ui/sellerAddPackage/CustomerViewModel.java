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

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomerViewModel implements Serializable ,Comparable<CustomerViewModel>{

    private String ref;
    private String name;
    private String middleName;
    private String lastName;
    private String email;
    private String mobile;
    private String sdn;

    public CustomerViewModel(@Nullable String ref,
                             @NonNull final String name, @NonNull final String middle,
                             @NonNull final String last, @NonNull final String email,
                             @NonNull String sdn, @NonNull String mobile) {
        setSimpleText(ref,name,middle,last,email,sdn,mobile);
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    @NonNull
    public String getEmail()
    {
        return email;
    }


    public String getSdn() {
        return sdn;
    }


    public String getRef() {
        return ref;
    }

    @NonNull
    public String getMobile(){
        return mobile;
    }

    public void setSimpleText(@Nullable String ref ,@NonNull final String name,@NonNull final String middle,
                              @NonNull final String last,@NonNull final String email,
                              @NonNull String sdn, @NonNull String mobile) {
        this.ref = ref;
        this.name = name;
        this.middleName = middle;
        this.lastName = last;
        this.email = email;
        this.sdn = sdn;
        this.mobile = mobile;
    }

    @Override
    public int compareTo(CustomerViewModel o) {

        String thisMobile = getMobile();
        String otherMobile = o.getMobile();

        return thisMobile.compareToIgnoreCase(otherMobile);
    }
}
