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

package com.project.maqdoom.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.FragmentProfileBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.customerTouristGroups.TouristGroupFragment;
import com.project.maqdoom.ui.login.LoginActivity;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileViewModel> implements ProfileNavigator {

    public static final String TAG = ProfileFragment.class.getSimpleName();
    @Inject
    ViewModelProviderFactory factory;
    private ProfileViewModel profileViewModel;
    FragmentProfileBinding fragmentProfileBinding;

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public ProfileViewModel getViewModel() {
        profileViewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel.class);
        return profileViewModel;
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void goToTouristGroup() {
        getFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                //.setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.parentLayout, TouristGroupFragment.newInstance(), TouristGroupFragment.TAG)
                .commit();

    }

    @Override
    public void logout() {
        for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        startActivity(LoginActivity.newIntent(this.getActivity()));
        this.getActivity().finish();
    }

    @Override
    public void setName() {

    }

    @Override
    public void enableEdit() {
        enableEditOption();
    }

    @Override
    public void updateProfile() {
        String phone = fragmentProfileBinding.etPhone.getText().toString();
        String name = fragmentProfileBinding.etName.getText().toString();
        String eMail = fragmentProfileBinding.etMail.getText().toString();
        if (profileViewModel.isValid(phone, name,eMail)) {
            hideKeyboard();
            profileViewModel.updateProfile(phone, name,eMail);

        } else {
            Toast.makeText(getActivity(), getString(R.string.fill_all_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void disableEdit() {
        disableEditOption();
    }
    @Override
    public void showErrorAlert(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentProfileBinding = getViewDataBinding();
        setUp();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        //onBackPressed();
    }

    private void removeFragment() {
        Fragment fragment = getFragmentManager().findFragmentByTag(TAG);
        if (fragment != null)
            getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    private void setUp() {
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener

                    return true;
                }
                return false;
            }
        });

    }
    private void onBackPressed() {
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        showHome();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void showHome() {
        for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void enableEditOption() {
        fragmentProfileBinding.etPhone.setEnabled(true);
        if("Not available".equalsIgnoreCase(fragmentProfileBinding.etPhone.getText().toString().trim())){
            fragmentProfileBinding.etPhone.setText("");
        }
        fragmentProfileBinding.etPhone.requestFocus();
        fragmentProfileBinding.etName.setEnabled(true);
        fragmentProfileBinding.etMail.setEnabled(true);
        fragmentProfileBinding.rlSave.setVisibility(View.VISIBLE);
    }

    private void disableEditOption() {
        populateData();
        fragmentProfileBinding.etPhone.setEnabled(false);
        fragmentProfileBinding.etName.setEnabled(false);
        fragmentProfileBinding.etMail.setEnabled(false);
        fragmentProfileBinding.rlSave.setVisibility(View.GONE);


    }
    public void populateData() {
        final String currentUserName = profileViewModel.getDataManager().getCurrentUserName();
        if (!TextUtils.isEmpty(currentUserName)) {
            fragmentProfileBinding.etName.setText(currentUserName);
        }

        final String currentUserEmail = profileViewModel.getDataManager().getEmail();
        if (!TextUtils.isEmpty(currentUserEmail)) {
            fragmentProfileBinding.etMail.setText(currentUserEmail);
        }
        final String phoneNumber = profileViewModel.getDataManager().getPhone();
        if (!TextUtils.isEmpty(phoneNumber)) {
            fragmentProfileBinding.etPhone.setText(phoneNumber);
        }else{
            fragmentProfileBinding.etPhone.setText("Not available");
        }

    }
}
