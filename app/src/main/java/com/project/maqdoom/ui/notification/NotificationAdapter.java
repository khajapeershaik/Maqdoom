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

package com.project.maqdoom.ui.notification;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.maqdoom.data.model.api.NotificationResponse;
import com.project.maqdoom.databinding.ItemBlogEmptyViewBinding;
import com.project.maqdoom.databinding.ItemNotificationViewBinding;
import com.project.maqdoom.ui.base.BaseViewHolder;
import com.project.maqdoom.ui.custom.EmptyItemViewModel;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class NotificationAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;

    public static final int VIEW_TYPE_NORMAL = 1;

    private List<NotificationResponse.Adds> mResponseList;

    private TravelGroupAdapterListener mListener;

    public NotificationAdapter(List<NotificationResponse.Adds> notificationResponseList) {
        this.mResponseList = notificationResponseList;
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
                ItemNotificationViewBinding blogViewBinding = ItemNotificationViewBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);
                return new BlogViewHolder(blogViewBinding);
            case VIEW_TYPE_EMPTY:
            default:
                ItemBlogEmptyViewBinding emptyViewBinding = ItemBlogEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);
                return new EmptyViewHolder(emptyViewBinding);
        }
    }

    public void addItems(List<NotificationResponse.Adds> travelList) {
        mResponseList.addAll(travelList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mResponseList.clear();
    }

    public void setListener(TravelGroupAdapterListener listener) {
        this.mListener = listener;
    }

    public interface TravelGroupAdapterListener {

        void onRetryClick();
    }

    public class BlogViewHolder extends BaseViewHolder implements NotificationItemViewModel.NotificationViewModelListener {

        private ItemNotificationViewBinding mBinding;

        private NotificationItemViewModel notificationItemViewModel;

        public BlogViewHolder(ItemNotificationViewBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            final NotificationResponse.Adds adds = mResponseList.get(position);
            notificationItemViewModel = new NotificationItemViewModel(adds, this);
            mBinding.setViewModel(notificationItemViewModel);

            mBinding.executePendingBindings();
        }

        @Override
        public void onItemClick() {

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