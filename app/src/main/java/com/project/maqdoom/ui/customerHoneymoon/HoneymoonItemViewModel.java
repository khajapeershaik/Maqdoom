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

package com.project.maqdoom.ui.customerHoneymoon;

import com.project.maqdoom.data.model.api.TravelCategoryResponse;

import org.json.JSONObject;

import java.util.List;

import androidx.databinding.ObservableField;


public class HoneymoonItemViewModel {

    public final ObservableField<String> packageName;

    public final ObservableField<String> country;

    public final ObservableField<String> description;

    public final ObservableField<String> noOfPeople;

    public final HoneymoonViewModelListener mListener;

    public final ObservableField<String> price;

    public final ObservableField<String> imageUrl;

    private final TravelCategoryResponse.Adds mAdds;

    public HoneymoonItemViewModel(TravelCategoryResponse.Adds adds, HoneymoonViewModelListener listener) {
        List<TravelCategoryResponse.Adds.Images> img = adds.getImage_path();
        String listString = "";
        if(img != null && img.size() >0){
            listString = img.get(0).getPath();
        }
        this.mAdds = adds;
        this.mListener = listener;
        packageName = new ObservableField<>(mAdds.getTourist_package());
        country = new ObservableField<>(mAdds.getCountry());
        description = new ObservableField<>(mAdds.getMore_details());
        noOfPeople = new ObservableField<>(mAdds.getPeople_cnt());
        price = new ObservableField<>(mAdds.getPrice());
        imageUrl = new ObservableField<>(listString.trim());
    }
    public void onPackageExpand() {
        mListener.onPackageClick();

    }
    public void onItemClick() {
        new Thread(() -> {
            try{
                List<TravelCategoryResponse.Adds.Images> img = mAdds.getImage_path();
                String listString = "";
                for(int i=0;i<img.size();i++){
                    listString += img.get(i).getPath() + "\t";
                }
                JSONObject item = new JSONObject();
                item.put("id", mAdds.getAdd_id());
                item.put("package_include", mAdds.getPackage_include());
                item.put("package", mAdds.getTourist_package());
                item.put("price", mAdds.getPrice());
                item.put("details", mAdds.getMore_details());
                item.put("whatsApp", mAdds.getWhatsapp_phone());
                item.put("phone", mAdds.getPhone());
                item.put("images", listString);
                mListener.onItemClick(item.toString());
            }catch (Exception e){
                e.printStackTrace();
            }

        }).start();
    }

    public interface HoneymoonViewModelListener {

        void onItemClick(String blogUrl);

        void onPackageClick();
    }
}
