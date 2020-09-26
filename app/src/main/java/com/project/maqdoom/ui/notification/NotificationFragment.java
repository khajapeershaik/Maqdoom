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

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.FragmentNotificationBinding;
import com.project.maqdoom.ui.base.BaseFragment;
import com.project.maqdoom.ui.customerTouristGroups.TouristGroupFragment;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class NotificationFragment extends BaseFragment<FragmentNotificationBinding, NotificationViewModel> implements NotificationNavigator {

    public static final String TAG = NotificationFragment.class.getSimpleName();


    @Inject
    LinearLayoutManager mLayoutManager;
    @Inject
    ViewModelProviderFactory factory;

    /*  @Inject
      NotificationAdapter notificationAdapter;*/
    private NotificationViewModel notificationViewModel;
    FragmentNotificationBinding fragmentNotificationBinding;



    Query query1;
    private DatabaseReference mdatabasereference;
    private ProgressDialog progressDialog;
   // FirebaseRecyclerAdapter<Product, BlogViewHolder> firebaseRecyclerAdapter;

    public static NotificationFragment newInstance() {
        Bundle args = new Bundle();
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_notification;
    }

    @Override
    public NotificationViewModel getViewModel() {
        notificationViewModel = ViewModelProviders.of(this, factory).get(NotificationViewModel.class);
        return notificationViewModel;
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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentNotificationBinding = getViewDataBinding();
        setUp();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        //onBackPressed();
    }

    private void setUp() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // fragmentNotificationBinding.blogRecyclerView.setLayoutManager(mLayoutManager);
        //   fragmentNotificationBinding.blogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // fragmentNotificationBinding.blogRecyclerView.setAdapter(notificationAdapter);

        mdatabasereference = FirebaseDatabase.getInstance().getReference("messages");
     /*   progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Products Please Wait...");
        progressDialog.show();
    */}
    private void showHome() {
        for (Fragment fragment : this.getActivity().getSupportFragmentManager().getFragments()) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
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

    /*@Override
    public void onStart() {
        super.onStart();
        query1 = FirebaseDatabase.getInstance().getReference().child("Message").child("message");
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(query1, Product.class)
                        .build();

        Log.d("Options"," data : "+options);


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, BlogViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BlogViewHolder blogViewHolder, final int i, @NonNull Product product_get_set_v) {


                final String productid=getRef(i).getKey();


                mdatabasereference.child(productid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        blogViewHolder.setname(product_get_set_v.getName());
//                        blogViewHolder.ename.setText(product_get_set_v.getName());





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }

            @NonNull
            @Override
            public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_notification_view, parent, false);
                progressDialog.dismiss();
                return new BlogViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        //   GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);

        fragmentNotificationBinding.blogRecyclerView.setLayoutManager(mLayoutManager);
        fragmentNotificationBinding.blogRecyclerView.setItemAnimator(new DefaultItemAnimator());

        fragmentNotificationBinding.blogRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }*/


    public static class BlogViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        TextView ename;
        public BlogViewHolder(View itemView)
        {
            super(itemView);
            mView=itemView;



        }
        public void setname(String name)
        {
            ename=(TextView)mView.findViewById(R.id.tvPackage);
            ename.setText(name);

        }

      /*  public String setimage(String url)
        {
            ImageView image = (ImageView)mView.findViewById(R.id.productimage);
           // Picasso.get().load(url).into(image);
            return url;
        }*/
    }
}
