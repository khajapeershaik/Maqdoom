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

package com.project.maqdoom.ui.customerTouristGuide;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.maqdoom.R;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;



public class ItemTypeSpinnerAdapter extends BaseAdapter {
    private ArrayList<String> mItems = new ArrayList<>();
    private boolean returnActualCount = false;
    private int textColorRes;
    FragmentActivity activity;
    public ItemTypeSpinnerAdapter(FragmentActivity mActivity) {
        this.activity = mActivity;
        this.textColorRes = R.color.black;
    }

    public void clear() {
        mItems.clear();
    }

    public void addItem(String yourObject) {
        mItems.add(yourObject);
    }


    public void addItems(List<String> yourObjectList) {
        mItems.addAll(yourObjectList);
    }

    public void setTextColor(int colorRes) {
        this.textColorRes = colorRes;
    }

    public void returnActualCount(boolean returnActualCount) {
        this.returnActualCount = returnActualCount;
    }

    @Override
    public int getCount() {


        int count = mItems.size();
        if (returnActualCount)
            return count;
        else
            return count > 0 ? count - 1 : count;
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            view = activity.getLayoutInflater().inflate(R.layout.item_spinner, parent, false);
            view.setTag("DROPDOWN");
        }

        TextView textView = view.findViewById(R.id.list_item);
        textView.setText(getTitle(position));
        textView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        return view;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = activity.getLayoutInflater().inflate(R.layout.item_spinner, parent, false);
            view.setTag("NON_DROPDOWN");
        }
        TextView textView = view.findViewById(R.id.list_item);
        textView.setText(getTitle(position));
        if (position == 0) {
            // Set the hint text color gray
            textView.setTextColor(activity.getResources().getColor(textColorRes));
        } else {
            textView.setTextColor(activity.getResources().getColor(textColorRes));
        }
        if (position == mItems.size()-1) {
            notifyDataSetChanged();
        }
        return view;
    }
    private String getTitle(int position) {
        return position >= 0 && position < mItems.size() ? mItems.get(position) : "";
    }
}