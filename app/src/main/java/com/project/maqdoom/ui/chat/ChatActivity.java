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

package com.project.maqdoom.ui.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.data.model.api.Message;
import com.project.maqdoom.databinding.ActivityChatBinding;
import com.project.maqdoom.databinding.ActivityForgotPasswordBinding;
import com.project.maqdoom.ui.base.BaseActivity;
import com.project.maqdoom.ui.forgotPassword.ForgotPasswordNavigator;
import com.project.maqdoom.ui.forgotPassword.ForgotPasswordViewModel;
import com.project.maqdoom.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;


public class ChatActivity extends BaseActivity<ActivityChatBinding, ChatViewModel> implements ChatNavigator {

    /*@Inject
    LinearLayoutManager mLayoutManager;*/
    @Inject
    ViewModelProviderFactory factory;
    private ChatViewModel chatViewModel;
    private ActivityChatBinding activityChatBinding;
    private static String userIdentification, userNameSelected;
    private FirebaseAuth mAuth;
    private StorageReference imageMessageStorageRef;
    private ConnectivityReceiver connectivityReceiver;
    private MessageAdapter messageAdapter;
    private DatabaseReference rootReference;
    private String messageSenderId, download_url;
    private static String messageReceiverID;
    private static String messageReceiverName;
    private final List<Message> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    public static Intent newIntent(Context context, String userId, String userName) {
        messageReceiverID = userId;
        messageReceiverName = userName;
        return new Intent(context, ChatActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public ChatViewModel getViewModel() {
        chatViewModel = ViewModelProviders.of(this, factory).get(ChatViewModel.class);
        return chatViewModel;
    }

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void forgotPassword() {

    }


    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.newIntent(ChatActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void sendEmailVerification() {


    }

    @Override
    public void showErrorAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatBinding = getViewDataBinding();
        chatViewModel.setNavigator(this);
        linearLayoutManager = new LinearLayoutManager(this);
        rootReference = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid();


        imageMessageStorageRef = FirebaseStorage.getInstance().getReference().child("messages_image");

        setUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectivityReceiver = new ConnectivityReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectivityReceiver);
    }

    private void setUp() {
        messageAdapter = new MessageAdapter(messageList);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        activityChatBinding.messageList.setLayoutManager(linearLayoutManager);
        activityChatBinding.messageList.setItemAnimator(new DefaultItemAnimator());
        activityChatBinding.messageList.setAdapter(messageAdapter);
        fetchMessages();
        activityChatBinding.tvUser.setText(messageReceiverName);
        rootReference.child("users").child(messageReceiverID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        activityChatBinding.cSendMessageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void fetchMessages() {
        rootReference.child("messages").child(messageSenderId).child(messageReceiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.exists()) {
                            Message message = dataSnapshot.getValue(Message.class);
                            messageList.add(message);
                            messageAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    private void sendMessage() {
        String message = activityChatBinding.cInputMessage.getText().toString();
        if (TextUtils.isEmpty(message)){
            //SweetToast.info(ChatActivity.this, "Please type a message");
        } else {
            String message_sender_reference = "messages/" + messageSenderId + "/" + messageReceiverID;
            String message_receiver_reference = "messages/" + messageReceiverID + "/" + messageSenderId;

            DatabaseReference user_message_key = rootReference.child("messages").child(messageSenderId).child(messageReceiverID).push();
            String message_push_id = user_message_key.getKey();

            HashMap<String, Object> message_text_body = new HashMap<>();
            message_text_body.put("message", message);
            message_text_body.put("seen", false);
            message_text_body.put("type", "text");
            message_text_body.put("time", ServerValue.TIMESTAMP);
            message_text_body.put("from", messageSenderId);

            HashMap<String, Object> messageBodyDetails = new HashMap<>();
            messageBodyDetails.put(message_sender_reference + "/" + message_push_id, message_text_body);
            messageBodyDetails.put(message_receiver_reference + "/" + message_push_id, message_text_body);

            rootReference.updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if (databaseError != null){
                        Log.e("Sending message", databaseError.getMessage());
                    }
                    activityChatBinding.cInputMessage.setText("");
                }
            });
        }
    }
    public class ConnectivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            activityChatBinding.tvChatConnection.setVisibility(View.GONE);
            if (networkInfo != null && networkInfo.isConnected()) {
                activityChatBinding.tvChatConnection.setText("Internet connected");
                activityChatBinding.tvChatConnection.setTextColor(Color.WHITE);
                activityChatBinding.tvChatConnection.setVisibility(View.VISIBLE);

                // LAUNCH activity after certain time period
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        ChatActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                activityChatBinding.tvChatConnection.setVisibility(View.GONE);
                            }
                        });
                    }
                }, 1200);
            } else {
                activityChatBinding.tvChatConnection.setText("No internet connection! ");
                activityChatBinding.tvChatConnection.setTextColor(Color.WHITE);
                activityChatBinding.tvChatConnection.setBackgroundColor(Color.RED);
                activityChatBinding.tvChatConnection.setVisibility(View.VISIBLE);
            }
        }
    }
}
