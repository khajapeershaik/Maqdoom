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

package com.project.maqdoom.ui.touristPackageDetails;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.indicators.PagerIndicator;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.FragmentPackageDetailsBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.chat.ChatActivity;
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


public class TouristPackageDetailsFragment extends BaseFragment<FragmentPackageDetailsBinding, TouristPackageDetailsViewModel> implements TouristPackageDetailsNavigator, BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    public static final String TAG = TouristPackageDetailsFragment.class.getSimpleName();
    private static String GD ="";
    String packageName = "";
    private String guideData = "";
    private String add_id = "", whatsAppNumber = "", phoneNumber = "";
    @Inject
    ViewModelProviderFactory factory;
    FragmentPackageDetailsBinding fragmentTouristGuideBinding;
    private TouristPackageDetailsViewModel touristGuideViewModel;
    private SliderLayout mDemoSlider;
    private PagerIndicator pagerIndicator;

    public static TouristPackageDetailsFragment newInstance(String data) {
        Bundle args = new Bundle();
        args.putString(GD, data);
        TouristPackageDetailsFragment fragment = new TouristPackageDetailsFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_package_details;
    }

    @Override
    public TouristPackageDetailsViewModel getViewModel() {
        touristGuideViewModel = ViewModelProviders.of(this, factory).get(TouristPackageDetailsViewModel.class);
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
    public void goHome() {
        showHome();
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

//        Intent intent = ChatActivity.newIntent(getActivity(), userID, userName);
//        startActivity(intent);
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
        mDemoSlider = fragmentTouristGuideBinding.slider;
        pagerIndicator = fragmentTouristGuideBinding.indicator;
        setUp();
    }

    private void setUp() {

        final String userType = touristGuideViewModel.getDataManager().getUserType();
        if ("1".equalsIgnoreCase(userType)) {
            fragmentTouristGuideBinding.llDelete.setVisibility(View.VISIBLE);
        } else {
            fragmentTouristGuideBinding.llDelete.setVisibility(View.GONE);
        }
        try {
            JSONObject jsonObj = new JSONObject(guideData);
            add_id = jsonObj.optString("id");
            whatsAppNumber = jsonObj.optString("whatsApp");
            phoneNumber = jsonObj.optString("phone");
            packageName = jsonObj.optString("package");
            fragmentTouristGuideBinding.tvPackageName.setText(jsonObj.optString("package"));
            fragmentTouristGuideBinding.tvDetails.setText(jsonObj.optString("details"));
            fragmentTouristGuideBinding.tvPackagePrice.setText(jsonObj.optString("price"));
            fragmentTouristGuideBinding.tvServices.setText(jsonObj.optString("services"));
            fragmentTouristGuideBinding.tvTravelAgent.setText(jsonObj.optString("supplier_name"));
            //List<TravelCategoryResponse.Adds.Images> img = jsonObj.optString("images").to;
            //System.out.println("Arun images--"+ img.size());
            List<String> imList = Arrays.asList(jsonObj.optString("images").split("\t"));
            setImageSlide(imList);


            String inclusions = jsonObj.optString("package_include");
            if ("".equalsIgnoreCase(inclusions)) {
                fragmentTouristGuideBinding.llIncl.setVisibility(View.GONE);
            } else {
                fragmentTouristGuideBinding.llIncl.setVisibility(View.VISIBLE);
                String[] values = inclusions.split(",");
                for (int i = 0; i < values.length; i++) {
                    String name = values[i].trim();
                    if (getResources().getString(R.string.package_flights).equalsIgnoreCase(name)) {
                        fragmentTouristGuideBinding.llFlights.setVisibility(View.VISIBLE);
                    }
                    if (getResources().getString(R.string.package_hotels).equalsIgnoreCase(name)) {
                        fragmentTouristGuideBinding.llHotels.setVisibility(View.VISIBLE);
                    }
                    if (getResources().getString(R.string.package_sight_seeing).equalsIgnoreCase(name)) {
                        fragmentTouristGuideBinding.llSightSeeing.setVisibility(View.VISIBLE);
                    }
                    if (getResources().getString(R.string.package_meals).equalsIgnoreCase(name)) {
                        fragmentTouristGuideBinding.llMeals.setVisibility(View.VISIBLE);
                    }
                    if (getResources().getString(R.string.package_site_visit).equalsIgnoreCase(name)) {
                        fragmentTouristGuideBinding.llSightVisit.setVisibility(View.VISIBLE);
                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void openCallDialer() {
        try {
            if (!"".equalsIgnoreCase(phoneNumber)) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Phone number not available", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong !!", Toast.LENGTH_LONG).show();
        }

    }

    private void openWhatsAppFromDevice() {
        if (!"".equalsIgnoreCase(whatsAppNumber)) {
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
        } else {
            Toast.makeText(getActivity(), "WhatsApp number not available", Toast.LENGTH_LONG).show();
        }

    }

    private void setImageSlide(List<String> imList) {
        //ArrayList<String> listUrl = new ArrayList<>();

        //listUrl.add("http://www.gstatic.com/webp/gallery/1.webp");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        //.diskCacheStrategy(DiskCacheStrategy.NONE)
        //.placeholder(R.drawable.placeholder)
        //.error(R.drawable.placeholder);

        for (int i = 0; i < imList.size(); i++) {
            DefaultSliderView sliderView = new DefaultSliderView(getActivity());
            final int finalI =  i;
            sliderView
                    .image(imList.get(i).trim())
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true);

            sliderView.image(imList.get(i))
                    .setOnSliderClickListener(slider -> {

                        Intent intent = new Intent(getActivity(),FullImageActivity.class);
                        intent.putExtra("imageurls", imList.get(finalI));
                        startActivity(intent);

                    });
            mDemoSlider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

        //mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(400000000);
        mDemoSlider.stopCyclingWhenTouch(false);
        mDemoSlider.setCustomIndicator(pagerIndicator);
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

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(), slider.getBundle().getString("extra") + "", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Toast.makeText(getActivity(),position,Toast.LENGTH_LONG).show();
        Log.d("scrolpositionis", ""+ position);
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("onPageSelected", ""+ position);
        Toast.makeText(getActivity(),position,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
