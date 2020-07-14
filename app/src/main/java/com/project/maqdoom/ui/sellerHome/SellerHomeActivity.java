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

package com.project.maqdoom.ui.sellerHome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.ActivitySellerHomeBinding;
import com.project.maqdoom.ui.base.BaseActivity;
import com.project.maqdoom.ui.customerRentalSupplies.CustomerRentalSuppliesFragment;
import com.project.maqdoom.ui.customerTouristGuide.TouristGuidesFragment;
import com.project.maqdoom.ui.friends.FriendsFragment;
import com.project.maqdoom.ui.login.LoginActivity;
import com.project.maqdoom.ui.notification.NotificationFragment;
import com.project.maqdoom.ui.profile.ProfileFragment;
import com.project.maqdoom.ui.sellerAddPackage.SellerAddPackageFragment;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class SellerHomeActivity extends BaseActivity<ActivitySellerHomeBinding, SellerHomeViewModel> implements SellerHomeNavigator, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;
    @Inject
    ViewModelProviderFactory factory;
    private ActivitySellerHomeBinding activitySellerHomeBinding;
    private SellerHomeViewModel sellerHomeViewModel;
    private BottomNavigationView bottomNavigationView;
    boolean doubleBackToExitPressedOnce = false;
    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseReference;
    public FirebaseUser currentUser;
    private DatabaseReference mDatabase;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SellerHomeActivity.class);
        return intent;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_seller_home;
    }

    @Override
    public SellerHomeViewModel getViewModel() {
        sellerHomeViewModel = ViewModelProviders.of(this, factory).get(SellerHomeViewModel.class);
        return sellerHomeViewModel;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void onFragmentDetached(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .remove(fragment)
                    .commitNow();
        }
    }

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }


    @Override
    public void openLoginActivity() {
        startActivity(LoginActivity.newIntent(this));
        finish();
    }

    @Override
    public void openNotification() {

    }

    @Override
    public void openChat() {

    }

    @Override
    public void openProfile() {

    }


    @Override
    public void openCustomerHome() {
        showTouristGuide();
    }

    @Override
    public void openSuppliesHome() {
        showRentalSupplies();
    }

    @Override
    public void openDialog() {
        //OptionDialog.newInstance().show(getSupportFragmentManager());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_option, null);
        alertDialog.setView(customLayout);
        AlertDialog OptionDialog = alertDialog.create();
        OptionDialog.show();
        LinearLayout travel_layout, supplies_layout;
        travel_layout = customLayout.findViewById(R.id.travel_layout);
        supplies_layout = customLayout.findViewById(R.id.supplies_layout);


        travel_layout.setOnClickListener(view -> {
            showAddPackageFragment(1);
            OptionDialog.dismiss();
        });
        supplies_layout.setOnClickListener(view -> {
            showAddPackageFragment(2);
            OptionDialog.dismiss();
        });


        ImageButton close = customLayout.findViewById(R.id.navLogout);
        close.setOnClickListener(view -> OptionDialog.dismiss());
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySellerHomeBinding = getViewDataBinding();
        sellerHomeViewModel.setNavigator(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String user_uID = mAuth.getCurrentUser().getUid();
            userDatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(user_uID);
        }
        setUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        //checking logging, if not login redirect to Login ACTIVITY
        if (currentUser == null) {
            try {
                firebaseLoginUser(); // Return to Login activity
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (currentUser != null) {
            userDatabaseReference.child("active_now").setValue("true");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpFCM();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (currentUser != null) {
            userDatabaseReference.child("active_now").setValue(ServerValue.TIMESTAMP);
        }
    }

    private void firebaseLoginUser() {
        String email = sellerHomeViewModel.getDataManager().getEmail();
        String password = sellerHomeViewModel.getDataManager().getCreatedDate();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String user_uID = mAuth.getCurrentUser().getUid();
                userDatabaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(user_uID);
                userDatabaseReference.child("active_now").setValue("true");
            } else {

            }

        });


    }

    private void setUpFCM() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.w("TAG", token);

                    }
                });
    }

    private void setUp() {
        bottomNavigationView = activitySellerHomeBinding.bottomNavigation;
        setupBottomNavigation();
        sellerHomeViewModel.onNavMenuCreated();
    }


    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navItemHome:
                    showHome();
                    return true;
                case R.id.navItemNotification:
                    if (currentFragment() instanceof NotificationFragment) {
                    } else {
                        showNotification();
                    }
                    return true;
                case R.id.navItemChat:
                    if (currentFragment() instanceof FriendsFragment) {
                    } else {
                        showChats();
                    }
                    return true;
                case R.id.navItemProfile:
                    if (currentFragment() instanceof ProfileFragment) {
                    } else {
                        showProfile();
                    }
                    return true;


            }
            return false;
        });
    }

    private void showChats() {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.clRootView, FriendsFragment.newInstance(), FriendsFragment.TAG)
                .commit();
    }

    private void showAddPackageFragment(int type) {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.clRootView, SellerAddPackageFragment.newInstance(type), SellerAddPackageFragment.TAG)
                .commit();
    }

    private void showHome() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void showTouristGuide() {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.clRootView, TouristGuidesFragment.newInstance(), TouristGuidesFragment.TAG)
                .commit();
    }

    private void showRentalSupplies() {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.clRootView, CustomerRentalSuppliesFragment.newInstance(), CustomerRentalSuppliesFragment.TAG)
                .commit();
    }

    private void showNotification() {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.clRootView, NotificationFragment.newInstance(), NotificationFragment.TAG)
                .commit();
    }

    private void showProfile() {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.clRootView, ProfileFragment.newInstance(), ProfileFragment.TAG)
                .commit();
    }


    public Fragment currentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.clRootView);
    }
}
