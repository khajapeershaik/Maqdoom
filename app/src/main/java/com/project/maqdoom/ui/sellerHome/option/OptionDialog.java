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

package com.project.maqdoom.ui.sellerHome.option;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.DialogOptionBinding;
import com.project.maqdoom.ui.base.BaseDialog;
import com.project.maqdoom.ui.sellerAddPackage.SellerAddPackageActivity;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.AndroidSupportInjection;



public class OptionDialog extends BaseDialog implements OptionCallback {

    private static final String TAG = OptionDialog.class.getSimpleName();
    @Inject
    ViewModelProviderFactory factory;
    private OptionViewModel mRateUsViewModel;

    public static OptionDialog newInstance() {
        OptionDialog fragment = new OptionDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void dismissDialog() {
        dismissDialog(TAG);
    }

    @Override
    public void openAddPackage(int option) {
        dismissDialog(TAG);
        Intent intent = SellerAddPackageActivity.newIntent(getActivity(),option);
        startActivity(intent);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DialogOptionBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_option, container, false);
        View view = binding.getRoot();

        AndroidSupportInjection.inject(this);
        mRateUsViewModel = ViewModelProviders.of(this,factory).get(OptionViewModel.class);
        binding.setViewModel(mRateUsViewModel);

        mRateUsViewModel.setNavigator(this);

        return view;
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }
}
