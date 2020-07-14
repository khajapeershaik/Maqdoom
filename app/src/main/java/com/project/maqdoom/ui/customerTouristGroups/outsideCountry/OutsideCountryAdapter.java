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

package com.project.maqdoom.ui.customerTouristGroups.outsideCountry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.maqdoom.R;
import com.project.maqdoom.data.model.api.TravelCategoryGroupResponse;
import com.project.maqdoom.databinding.ItemBlogEmptyViewBinding;
import com.project.maqdoom.databinding.ItemTouristGroupOutsideViewBinding;
import com.project.maqdoom.ui.base.BaseViewHolder;
import com.project.maqdoom.ui.custom.EmptyItemViewModel;
import com.project.maqdoom.ui.touristPackageDetails.TouristPackageDetailsFragment;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


public class OutsideCountryAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;

    public static final int VIEW_TYPE_NORMAL = 1;

    private List<TravelCategoryGroupResponse.Adds> mResponseList;

    private TravelGroupOutsideAdapterListener mListener;

    private boolean status = false;

    public OutsideCountryAdapter(List<TravelCategoryGroupResponse.Adds> travelResponseList) {
        this.mResponseList = travelResponseList;
    }


    @Override
    public int getItemCount() {
        if (mResponseList != null && mResponseList.size() > 0) {
            return mResponseList.size();
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mResponseList != null && !mResponseList.isEmpty()) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                ItemTouristGroupOutsideViewBinding blogViewBinding = ItemTouristGroupOutsideViewBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);
                return new BlogViewHolder(blogViewBinding);
            case VIEW_TYPE_EMPTY:
            default:
                ItemBlogEmptyViewBinding emptyViewBinding = ItemBlogEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);
                return new EmptyViewHolder(emptyViewBinding);
        }
    }

    public void addItems(List<TravelCategoryGroupResponse.Adds> travelList) {
        mResponseList.addAll(travelList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mResponseList.clear();
    }

    public void setListener(TravelGroupOutsideAdapterListener listener) {
        this.mListener = listener;
    }

    public interface TravelGroupOutsideAdapterListener {

        void onRetryClick();
    }

    public class BlogViewHolder extends BaseViewHolder implements CountryOutsideItemViewModel.CountryOutsideViewModelListener {

        private ItemTouristGroupOutsideViewBinding mBinding;

        private CountryOutsideItemViewModel mBlogItemViewModel;

        public BlogViewHolder(ItemTouristGroupOutsideViewBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            final TravelCategoryGroupResponse.Adds adds = mResponseList.get(position);
            mBlogItemViewModel = new CountryOutsideItemViewModel(adds, this);
            mBinding.setViewModel(mBlogItemViewModel);

            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mBinding.executePendingBindings();
        }

        @Override
        public void onItemClick(String data) {
            FragmentManager manager = ((AppCompatActivity)itemView.getContext()).getSupportFragmentManager();
            manager
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .add(R.id.parentLayout, TouristPackageDetailsFragment.newInstance(data), TouristPackageDetailsFragment.TAG)
                    .commit();
        }

        @Override
        public void onPackageClick() {
            if(status == false){
                status=true;
                mBinding.llIncl.setVisibility(View.VISIBLE);
                String inclusions = mResponseList.get(getAdapterPosition()).getPackage_include();
                if ("".equalsIgnoreCase(inclusions)) {
                    mBinding.llIncl.setVisibility(View.GONE);
                } else {
                    mBinding.llIncl.setVisibility(View.VISIBLE);
                    String[] values = inclusions.split(",");
                    for (int i = 0; i < values.length; i++) {
                        String name = values[i].trim();
                        if ("Flights".equalsIgnoreCase(name)) {
                            mBinding.llFlights.setVisibility(View.VISIBLE);
                        }
                        if ("Hotels".equalsIgnoreCase(name)) {
                            mBinding.llHotels.setVisibility(View.VISIBLE);
                        }
                        if ("Sight Seeing".equalsIgnoreCase(name)) {
                            mBinding.llSightSeeing.setVisibility(View.VISIBLE);
                        }
                        if ("Meals".equalsIgnoreCase(name)) {
                            mBinding.llMeals.setVisibility(View.VISIBLE);
                        }
                        if ("Sight Visit".equalsIgnoreCase(name)) {
                            mBinding.llSightVisit.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }else{
                status=false;
                mBinding.llIncl.setVisibility(View.GONE);
            }
        }
    }

    public class EmptyViewHolder extends BaseViewHolder implements EmptyItemViewModel.BlogEmptyItemViewModelListener {

        private ItemBlogEmptyViewBinding mBinding;

        public EmptyViewHolder(ItemBlogEmptyViewBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            EmptyItemViewModel emptyItemViewModel = new EmptyItemViewModel(this);
            mBinding.setViewModel(emptyItemViewModel);
        }

        @Override
        public void onRetryClick() {
            mListener.onRetryClick();
        }
    }
}