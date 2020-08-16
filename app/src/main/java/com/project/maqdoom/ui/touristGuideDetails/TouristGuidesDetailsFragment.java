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

package com.project.maqdoom.ui.touristGuideDetails;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.FragmentGuideDetailsBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.customerTouristGroups.TouristGroupFragment;
import com.project.maqdoom.ui.friends.FriendsFragment;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class TouristGuidesDetailsFragment extends BaseFragment<FragmentGuideDetailsBinding, TouristGuideDetailsViewModel> implements TouristGuideDetailsNavigator {

    public static final String TAG = TouristGuidesDetailsFragment.class.getSimpleName();
    public static String GD;
    String guideData = "";
    String packageName = "";
    private String add_id = "",whatsAppNumber ="", phoneNumber="";
    @Inject
    ViewModelProviderFactory factory;
    private TouristGuideDetailsViewModel touristGuideViewModel;
    FragmentGuideDetailsBinding fragmentTouristGuideBinding;

    public static TouristGuidesDetailsFragment newInstance(String data) {
        Bundle args = new Bundle();
        args.putString(GD, data);
        TouristGuidesDetailsFragment fragment = new TouristGuidesDetailsFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_guide_details;
    }

    @Override
    public TouristGuideDetailsViewModel getViewModel() {
        touristGuideViewModel = ViewModelProviders.of(this, factory).get(TouristGuideDetailsViewModel.class);
        return touristGuideViewModel;
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
    public void goHome() {
        showHome();
    }

    @Override
    public void logout() {

    }

    @Override
    public void setName() {

    }

    @Override
    public void deletePackage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(getResources().getString(R.string.sure_delete));
        String positiveText = getString(R.string.Ok);
        String negativeText = getString(R.string.Cancel);
        builder.setPositiveButton(positiveText,
                (dialog, which) -> {
                    dialog.dismiss();
                    touristGuideViewModel.deleteAdd(add_id);
                });
        builder.setNegativeButton(negativeText, (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void showErrorAlert(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openWhatsApp() {
        openWhatsAppFromDevice();
    }

    @Override
    public void openCall() {
        openCallDialer();
    }

    @Override
    public void openChat() {
            getFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .add(R.id.parentLayout, FriendsFragment.newInstance(), FriendsFragment.TAG)
                    .commit();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        guideData = (String) getArguments().getSerializable(GD);
        touristGuideViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentTouristGuideBinding = getViewDataBinding();
        setUp();
    }

    private void setUp() {
        final String userType = touristGuideViewModel.getDataManager().getUserType();
        if ("1".equalsIgnoreCase(userType)) {
            fragmentTouristGuideBinding.llDelete.setVisibility(View.VISIBLE);
        }else{
            fragmentTouristGuideBinding.llDelete.setVisibility(View.GONE);
        }
        try{
            JSONObject jsonObj = new JSONObject(guideData);
            add_id = jsonObj.optString("id").toString();
            packageName = jsonObj.optString("package");
            fragmentTouristGuideBinding.tvGuide.setText(jsonObj.optString("name").toString());
            fragmentTouristGuideBinding.tvProfileDesc.setText(jsonObj.optString("profile").toString());
            fragmentTouristGuideBinding.tvLanguageDesc.setText(jsonObj.optString("language").toString());
            fragmentTouristGuideBinding.tvCountryDesc.setText(jsonObj.optString("city")+","+jsonObj.optString("country").toString());
            List<String> imList = Arrays.asList(jsonObj.optString("images").split("\t"));

            Glide.with(getActivity()).load(imList.get(0).trim()).into(fragmentTouristGuideBinding.coverImageView);

            whatsAppNumber = jsonObj.optString("whatsApp");
            phoneNumber = jsonObj.optString("phone");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void  openCallDialer(){
        try{
            if(!"".equalsIgnoreCase(phoneNumber)){
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(),"Phone number not available",Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            Toast.makeText(getActivity(),"Something went wrong !!",Toast.LENGTH_LONG).show();
        }

    }
    private void openWhatsAppFromDevice(){
        if(!"".equalsIgnoreCase(whatsAppNumber)){
            boolean isWhatsAppInstalled = whatsAppInstalledOrNot("com.whatsapp");
            if (isWhatsAppInstalled) {
                Uri uri = Uri.parse("smsto:" + whatsAppNumber);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hai");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            } else {
                Toast.makeText(getActivity(), "WhatsApp not Installed",
                        Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);

            }
        }else{
            Toast.makeText(getActivity(),"WhatsApp number not available",Toast.LENGTH_LONG).show();
        }

    }
    private boolean whatsAppInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
    private void showHome() {
        for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
}
