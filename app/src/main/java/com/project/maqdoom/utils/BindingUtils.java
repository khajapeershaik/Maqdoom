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

package com.project.maqdoom.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.project.maqdoom.data.model.api.NotificationResponse;
import com.project.maqdoom.data.model.api.TravelCategoryGroupResponse;
import com.project.maqdoom.data.model.api.TravelCategoryResponse;
import com.project.maqdoom.ui.customerFamilyTrip.TouristFamilyAdapter;
import com.project.maqdoom.ui.customerHoneymoon.HoneyMoonAdapter;
import com.project.maqdoom.ui.customerRentalSupplies.RentalSuppliesAdapter;
import com.project.maqdoom.ui.customerSuppliesCruises.CruiseSuppliesAdapter;
import com.project.maqdoom.ui.customerTouristGroups.insideCountry.InsideCountryAdapter;
import com.project.maqdoom.ui.customerTouristGroups.outsideCountry.OutsideCountryAdapter;
import com.project.maqdoom.ui.customerTouristGuide.TouristGuideAdapter;
import com.project.maqdoom.ui.notification.NotificationAdapter;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;



public final class BindingUtils {

    private BindingUtils() {
        // This class is not publicly instantiable
    }




    @BindingAdapter({"adapter_new"})
    public static void addOutsideCountryItems(RecyclerView recyclerView, List<TravelCategoryGroupResponse.Adds> data) {
        OutsideCountryAdapter adapter = (OutsideCountryAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(data);
        }
    }

    @BindingAdapter({"adapter"})
    public static void addInsideCountryItems(RecyclerView recyclerView, List<TravelCategoryGroupResponse.Adds> data) {
        InsideCountryAdapter adapter = (InsideCountryAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(data);
        }
    }

    @BindingAdapter({"adapter_guide"})
    public static void addTouristGuideItems(RecyclerView recyclerView, List<TravelCategoryResponse.Adds> data) {
        TouristGuideAdapter adapter = (TouristGuideAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(data);
        }
    }
    @BindingAdapter({"adapter_cruise"})
    public static void addCruiseSuppliesItems(RecyclerView recyclerView, List<TravelCategoryResponse.Adds> data) {
        CruiseSuppliesAdapter adapter = (CruiseSuppliesAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(data);
        }
    }
    @BindingAdapter({"adapter_rental"})
    public static void addRentalSuppliesItems(RecyclerView recyclerView, List<TravelCategoryResponse.Adds> data) {
        RentalSuppliesAdapter adapter = (RentalSuppliesAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(data);
        }
    }
    @BindingAdapter({"adapter_family"})
    public static void addTouristFamilyItems(RecyclerView recyclerView, List<TravelCategoryResponse.Adds> data) {
        TouristFamilyAdapter adapter = (TouristFamilyAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(data);
        }
    }
    @BindingAdapter({"adapter_hm"})
    public static void addTouristHoneymoonItems(RecyclerView recyclerView, List<TravelCategoryResponse.Adds> data) {
        HoneyMoonAdapter adapter = (HoneyMoonAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(data);
        }
    }
    @BindingAdapter({"adapter_notification"})
    public static void addNotificationItems(RecyclerView recyclerView, List<NotificationResponse.Adds> data) {
        NotificationAdapter adapter = (NotificationAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(data);
        }
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }
}
