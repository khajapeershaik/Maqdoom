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

package com.project.maqdoom.ui.customerHome;

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
import com.project.maqdoom.data.model.others.NotificationMessages;
import com.project.maqdoom.databinding.ActivityCustomerHomeBinding;
import com.project.maqdoom.ui.base.BaseActivity;
import com.project.maqdoom.ui.customerRentalSupplies.CustomerRentalSuppliesFragment;
import com.project.maqdoom.ui.customerTouristGuide.TouristGuidesFragment;
import com.project.maqdoom.ui.friends.FriendsFragment;
import com.project.maqdoom.ui.login.LoginActivity;
import com.project.maqdoom.ui.notification.NotificationFragment;
import com.project.maqdoom.ui.profile.ProfileFragment;
import com.project.maqdoom.ui.sellerAddPackage.SellerAddPackageFragment;
import com.project.maqdoom.ui.shops.ShopsFragment;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class CustomerHomeActivity extends BaseActivity<ActivityCustomerHomeBinding, CustomerHomeViewModel> implements CustomerHomeNavigator, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;
    @Inject
    ViewModelProviderFactory factory;
    private ActivityCustomerHomeBinding activityCustomerHomeBinding;
    private CustomerHomeViewModel customerHomeViewModel;
    private BottomNavigationView bottomNavigationView;
    boolean doubleBackToExitPressedOnce = false;
    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseReference;
    public FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    boolean isConsiderNewIntent = false;

    private static final String TAG = "MainActivity";
    ArrayList<NotificationMessages> notificationList = new ArrayList<>();

    private String notificationMessage;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CustomerHomeActivity.class);
        return intent;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_customer_home;
    }

    @Override
    public CustomerHomeViewModel getViewModel() {
        customerHomeViewModel = ViewModelProviders.of(this, factory).get(CustomerHomeViewModel.class);
        return customerHomeViewModel;
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
    public void openShopsHome() {
        showShops();
    }


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCustomerHomeBinding = getViewDataBinding();
        customerHomeViewModel.setNavigator(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // get the 'extra' from the intent

        /*  Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String value = extras.getString("notification");
                Toast.makeText(getApplicationContext(),""+value,Toast.LENGTH_LONG).show();
        }
*/
       /* Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.containsKey("notification"))
            {
                // extract the extra-data in the Notification
                String msg = extras.getString("message");
                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();

            }
        }
*/

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String tmp = "";
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                tmp += key + ": " + value + "\n\n";
                String messageJson = bundle.get("message").toString();
                try {
                    JSONObject reader = new JSONObject(messageJson);
                    if (notificationMessage != null) {
                        notificationMessage = reader.getString("message");
                        notificationData(notificationMessage);
                        Toast.makeText(getApplicationContext(), notificationMessage, Toast.LENGTH_LONG).show();

                        if (getApplicationContext() != null) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .disallowAddToBackStack()
                                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                                    .add(R.id.clRootView, NotificationFragment.newInstance(), NotificationFragment.TAG)
                                    .commit();
                          //  fragment.getAdapter().newAddeddata(company_name.getText().toString());

                        }

                     /*   NotificationFragment fragment = getFragmentManager().findFragmentByTag(NotificationFragment.TAG); //set tag of fragment when you add with fragment manager. and if you are using support library use getSupportFragmentManager()
                        if(fragment!= null){
                            fragment.getAdapter().newAddeddata(company_name.getText().toString());
                        }*/

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            Log.d("notification", tmp);
        }


      /*  Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("notification");
            try {
                JSONObject reader = new JSONObject(value);
                String msg = reader.getString("message");
                Log.d("notifcations",msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

       /* Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String tmp = "";
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                tmp += key + ": " + value + "\n\n";
            }

            Toast.makeText(getApplicationContext(),tmp,Toast.LENGTH_LONG).show();
        }*/


       /* Intent intent = getIntent();
        if (intent != null) {
            Bundle b = intent.getExtras();
            if (b != null) {
                Set<String> keys = b.keySet();
                for (String key : keys) {
                    Log.d(TAG, "Bundle Contains: key=" + key);
                }
            } else {
                Log.w(TAG, "onCreate: BUNDLE is null");
            }
        } else {
            Log.w(TAG, "onCreate: INTENT is null");
        }
*/


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
        if (currentUser == null) {
            try {
                firebaseLoginUser();
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

     /*   Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.containsKey("notification"))
            {
                // extract the extra-data in the Notification
                String msg = extras.getString("message");
                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();

            }
        }*/


       /* Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("notification");
            try {
                JSONObject reader = new JSONObject(value);
                String msg = reader.getString("message");
                Log.d("notifcations",msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

       /* Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("notification");
            try {
                JSONObject reader = new JSONObject(value);
                String msg = reader.getString("message");
                Log.d("notifcations",msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

       /* Intent fintent=getIntent();

        if (fintent != null) {

            String data = fintent.getStringExtra("data");
            Log.v("message",data+"::::::");

            if (data != null) {

                if (getApplicationContext() != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .disallowAddToBackStack()
                            .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                            .add(R.id.clRootView, NotificationFragment.newInstance(), NotificationFragment.TAG)
                            .commit();
                }

            }
        }*/

      /*  if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("notifications", "Key: " + key + " Value: " + value);
                // navigate the app based on param
            }
        }*/


        //setUpFCM();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // from onStop
        if (currentUser != null) {
            userDatabaseReference.child("active_now").setValue(ServerValue.TIMESTAMP);
        }
    }

    private void firebaseLoginUser() {
        String email = customerHomeViewModel.getDataManager().getEmail();
        String password = customerHomeViewModel.getDataManager().getCreatedDate();
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

    private void setUp() {
        bottomNavigationView = activityCustomerHomeBinding.bottomNavigation;
        setupBottomNavigation();
        customerHomeViewModel.onNavMenuCreated();
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

    private void showHome() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void showTouristGuide() {
        if (getApplicationContext() != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .add(R.id.clRootView, TouristGuidesFragment.newInstance(), TouristGuidesFragment.TAG)
                    .commit();
        }
    }

    private void showRentalSupplies() {
        if (getApplicationContext() != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .add(R.id.clRootView, CustomerRentalSuppliesFragment.newInstance(), CustomerRentalSuppliesFragment.TAG)
                    .commit();
        }
    }

    private void showNotification() {
        if (getApplicationContext() != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .add(R.id.clRootView, NotificationFragment.newInstance(), NotificationFragment.TAG)
                    .commit();
        }
    }

    private void showProfile() {
        if (getApplicationContext() != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .add(R.id.clRootView, ProfileFragment.newInstance(), ProfileFragment.TAG)
                    .commit();
        }
    }

    private void showChats() {
        if (getApplicationContext() != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .add(R.id.clRootView, FriendsFragment.newInstance(), FriendsFragment.TAG)
                    .commit();
        }
    }

    private void showShops() {
        if (getApplicationContext() != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .add(R.id.clRootView, ShopsFragment.newInstance(), ShopsFragment.TAG)
                    .commit();
        }
    }


    public Fragment currentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.clRootView);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /* @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        *//*if (intent != null) {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String value = extras.getString("notification");
                try {
                    JSONObject reader = new JSONObject(value);
                    String msg = reader.getString("message");
                    Log.d("notifcations",msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
*//*

      // }

    }*/

    public void notificationData(String notificationMessage) {
        NotificationMessages notificationMessages = new NotificationMessages();
        notificationMessages.setMessage(notificationMessage);
        notificationList.add(notificationMessages);
        // notifyDataSetChanged();
    }
}
