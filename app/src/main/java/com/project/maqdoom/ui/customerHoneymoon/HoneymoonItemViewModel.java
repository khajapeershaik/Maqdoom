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

import android.util.Log;

import com.project.maqdoom.data.model.api.TravelCategoryGroupResponse;
import com.project.maqdoom.data.model.api.TravelCategoryResponse;

import org.json.JSONObject;

import java.util.List;

import androidx.databinding.ObservableField;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class HoneymoonItemViewModel {

    public final ObservableField<String> packageName;

    public final ObservableField<String> country;

    public final ObservableField<String> description;

    public final ObservableField<String> noOfPeople;

    public final HoneymoonViewModelListener mListener;

    public final ObservableField<String> price;

    public final ObservableField<String> imageUrl;

    private final TravelCategoryResponse.Adds mAdds;
    private String userType;

    public HoneymoonItemViewModel(TravelCategoryResponse.Adds adds, HoneymoonViewModelListener listener,String user) {
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
        userType = user;
    }
    public void onPackageExpand() {
        mListener.onPackageClick();

    }
    public int editVisibility(){
        Log.v("userTypeHoneymoon",userType);
        if(userType.equalsIgnoreCase("1")){
            return VISIBLE;
        }else {
            return GONE;
        }
    }
    public void deleteButtonClicked(){
        mListener.onDeleteButtonClick(mAdds.getAdd_id());
    }

    public void editButtonClicked(){
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
                item.put("name",mAdds.getGuide_name());
                item.put("level1_category",mAdds.getLevel1_category());
                item.put("level2_category",mAdds.getLevel2_category());
                item.put("level3_category",mAdds.getLevel3_category());
                item.put("services",mAdds.getServices());
                item.put("location",mAdds.getLocation());
                item.put("no_of_people",mAdds.getPeople_cnt());
                item.put("licence_pic_url",mAdds.getLicence_pic_url());

                if(mAdds.getLevel1_category().equalsIgnoreCase("TR")){
                    mListener.onEditButtonClick(1,item.toString());
                }else{
                    mListener.onEditButtonClick(2,item.toString());
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }).start();

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

        void onDeleteButtonClick(String appId);

        void onEditButtonClick(int type ,String blogUrl);
    }
}
