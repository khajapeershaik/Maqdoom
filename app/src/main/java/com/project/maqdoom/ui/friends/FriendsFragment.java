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

package com.project.maqdoom.ui.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.data.model.api.FriendsModel;
import com.project.maqdoom.databinding.FragmentFriendsBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.chat.ChatActivity;
import com.project.maqdoom.ui.customerTouristGroups.TouristGroupFragment;
import com.project.maqdoom.ui.login.LoginActivity;
import com.project.maqdoom.ui.sellerAddPackage.SellerAddPackageActivity;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;


public class FriendsFragment extends BaseFragment<FragmentFriendsBinding, FriendsViewModel> implements FriendsNavigator {

    public static final String TAG = FriendsFragment.class.getSimpleName();
    @Inject
    LinearLayoutManager mLayoutManager;
    @Inject
    ViewModelProviderFactory factory;
    private FriendsViewModel friendsViewModel;
    FragmentFriendsBinding fragmentFriendsBinding;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;

    public static FriendsFragment newInstance() {
        Bundle args = new Bundle();
        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_friends;
    }

    @Override
    public FriendsViewModel getViewModel() {
        friendsViewModel = ViewModelProviders.of(this, factory).get(FriendsViewModel.class);
        return friendsViewModel;
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void goToTouristGroup() {
        assert getFragmentManager() != null;
        getFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                //.setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.parentLayout, TouristGroupFragment.newInstance(), TouristGroupFragment.TAG)
                .commit();

    }

    @Override
    public void logout() {
        for (Fragment fragment : Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().getFragments()) {
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

    }

    @Override
    public void updateProfile() {

    }

    @Override
    public void disableEdit() {

    }

    @Override
    public void showErrorAlert(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentFriendsBinding = getViewDataBinding();
        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);
        setUp();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            firebaseLoginUser();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong. Please log out and try again.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                // handle back button's click listener
                return true;
            }
            return false;
        });

    }

    private void firebaseLoginUser() {
        String email = friendsViewModel.getDataManager().getEmail();
        String password = friendsViewModel.getDataManager().getCreatedDate();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String current_user_id = mAuth.getCurrentUser().getUid();
                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                mDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(aVoid -> showPeopleList());

            } else {
                String task_result = task.getException().getMessage().toString();
                Toast.makeText(getContext(), "Error : " + task_result, Toast.LENGTH_LONG).show();
            }

        });


    }

    private void showPeopleList() {
        try {
            friendsViewModel.setIsLoading(true);
            String userType = "";
            if ("0".equalsIgnoreCase(friendsViewModel.getDataManager().getUserType())){
                userType ="Seller";
            }else{
                userType ="Customer";
            }
            Query firebaseQuery = mUsersDatabase.orderByChild("status").equalTo(userType);
            FirebaseRecyclerOptions<FriendsModel> recyclerOptions = new FirebaseRecyclerOptions.Builder<FriendsModel>()
                    .setQuery(firebaseQuery, FriendsModel.class)
                    .build();

            FirebaseRecyclerAdapter<FriendsModel, FriendsVH> recyclerAdapter = new FirebaseRecyclerAdapter<FriendsModel, FriendsVH>(recyclerOptions) {

                @Override
                protected void onBindViewHolder(@NonNull final FriendsVH holder, int position, @NonNull FriendsModel model) {
                    final String userID = getRef(position).getKey();
                    mUsersDatabase.child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            friendsViewModel.setIsLoading(false);
                            final String userName = dataSnapshot.child("name").getValue().toString();
                            String status = dataSnapshot.child("status").getValue().toString();
                            String active_status = dataSnapshot.child("active_now").getValue().toString();
                            String device_Token = dataSnapshot.child("device_token").getValue().toString();
                            holder.active_icon.setVisibility(View.GONE);
                            if (active_status.contains("true")) {
                                holder.active_icon.setVisibility(View.VISIBLE);
                            } else {
                                holder.active_icon.setVisibility(View.GONE);
                            }
                            holder.date.setText(status);
                            holder.name.setText(userName);
                            Glide.with(getActivity()).load(R.mipmap.ic_launcher_round).into(holder.profile_thumb);
                            holder.itemView.setOnClickListener(v -> {

                                Intent intent = ChatActivity.newIntent(getActivity(), userID, userName,device_Token);
                                startActivity(intent);
                            });
                            /*if (loggedUser.equalsIgnoreCase(dataSnapshot.child("email").getValue().toString())) {
                                holder.rootLayout.setVisibility(View.GONE);
                                *//*final String userName = dataSnapshot.child("name").getValue().toString();
                                String active_status = dataSnapshot.child("status").getValue().toString();
                                holder.active_icon.setVisibility(View.GONE);
                                if (active_status.contains("active_now")) {
                                    holder.active_icon.setVisibility(View.VISIBLE);
                                } else {
                                    holder.active_icon.setVisibility(View.GONE);
                                }
                                holder.date.setText("You");
                                holder.name.setText(userName);
                                Glide.with(getActivity()).load(R.drawable.profile_icon).into(holder.profile_thumb);
                                holder.itemView.setOnClickListener(v -> {

                                    Intent intent = ChatActivity.newIntent(getActivity(), userID, userName);
                                    startActivity(intent);
                                });*//*
                            } else {
                                String userType = "";
                                if ("0".equalsIgnoreCase(friendsViewModel.getDataManager().getUserType())){
                                    userType ="Customer";
                                }else{
                                    userType ="Seller";
                                }
                                if ("0".equalsIgnoreCase(friendsViewModel.getDataManager().getUserType())){
                                    //Customer
                                    String active_status = dataSnapshot.child("status").getValue().toString();
                                    if("Seller".equalsIgnoreCase(active_status)){
                                        holder.rootLayout.setVisibility(View.VISIBLE);
                                        final String userName = dataSnapshot.child("name").getValue().toString();
                                        //String userThumbPhoto = dataSnapshot.child("image").getValue().toString();
                                        holder.active_icon.setVisibility(View.GONE);
                                        if (active_status.contains("active_now")) {
                                            holder.active_icon.setVisibility(View.VISIBLE);
                                        } else {
                                            holder.active_icon.setVisibility(View.GONE);
                                        }
                                        holder.date.setText(active_status);
                                        holder.name.setText(userName);
                                        Glide.with(getActivity()).load(R.mipmap.ic_launcher_round).into(holder.profile_thumb);
                                        holder.itemView.setOnClickListener(v -> {

                                            Intent intent = ChatActivity.newIntent(getActivity(), userID, userName);
                                            startActivity(intent);
                                        });
                                    }else{
                                        holder.rootLayout.setVisibility(View.GONE);
                                    }
                                }else{
                                    //Seller
                                    String active_status = dataSnapshot.child("status").getValue().toString();
                                    if("Customer".equalsIgnoreCase(active_status)){
                                        holder.rootLayout.setVisibility(View.VISIBLE);
                                        final String userName = dataSnapshot.child("name").getValue().toString();
                                        //String userThumbPhoto = dataSnapshot.child("image").getValue().toString();
                                        holder.active_icon.setVisibility(View.GONE);
                                        if (active_status.contains("active_now")) {
                                            holder.active_icon.setVisibility(View.VISIBLE);
                                        } else {
                                            holder.active_icon.setVisibility(View.GONE);
                                        }
                                        holder.date.setText(active_status);
                                        holder.name.setText(userName);
                                        Glide.with(getActivity()).load(R.mipmap.ic_launcher_round).into(holder.profile_thumb);
                                        holder.itemView.setOnClickListener(v -> {

                                            Intent intent = ChatActivity.newIntent(getActivity(), userID, userName);
                                            startActivity(intent);
                                        });
                                    }else{
                                        holder.rootLayout.setVisibility(View.GONE);
                                    }
                                }

                            }*/


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            friendsViewModel.setIsLoading(false);
                        }
                    });


                }

                @NonNull
                @Override
                public FriendsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_single_profile_display, viewGroup, false);
                    return new FriendsVH(view);
                }
            };

            fragmentFriendsBinding.friendsList.setAdapter(recyclerAdapter);
            recyclerAdapter.startListening();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something went wrong. Please log out and try again.", Toast.LENGTH_LONG).show();
        }

    }

    public static class FriendsVH extends RecyclerView.ViewHolder {
        public TextView name;
        TextView date;
        ImageView profile_thumb;
        ImageView active_icon;
        LinearLayout rootLayout;

        public FriendsVH(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.all_user_name);
            date = itemView.findViewById(R.id.all_user_status);
            profile_thumb = itemView.findViewById(R.id.all_user_profile_img);
            active_icon = itemView.findViewById(R.id.activeIcon);
            rootLayout = itemView.findViewById(R.id.rootLayout);

        }
    }

    private void removeFragment() {
        Fragment fragment = getFragmentManager().findFragmentByTag(TAG);
        if (fragment != null)
            getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    private void setUp() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentFriendsBinding.friendsList.setLayoutManager(mLayoutManager);
        fragmentFriendsBinding.friendsList.setItemAnimator(new DefaultItemAnimator());
    }

    private void onBackPressed() {
        Objects.requireNonNull(getView()).setOnKeyListener(new View.OnKeyListener() {
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
        for (Fragment fragment : Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().getFragments()) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }


}
