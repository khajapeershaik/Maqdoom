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

package com.project.maqdoom.ui.sellerPackages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.project.maqdoom.BR;
import com.project.maqdoom.R;
import com.project.maqdoom.ViewModelProviderFactory;
import com.project.maqdoom.databinding.ActivitySellerPackageBinding;
import com.project.maqdoom.ui.base.BaseActivity;
import com.project.maqdoom.ui.login.LoginActivity;
import com.project.maqdoom.ui.sellerAddPackage.SettingsManager;
import com.project.maqdoom.ui.sellerHome.SellerHomeActivity;
import com.project.maqdoom.ui.sellerPackagePayment.SellerPackagePaymentActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import company.tap.gosellapi.GoSellSDK;
import company.tap.gosellapi.internal.api.callbacks.GoSellError;
import company.tap.gosellapi.internal.api.models.Authorize;
import company.tap.gosellapi.internal.api.models.Charge;
import company.tap.gosellapi.internal.api.models.PhoneNumber;
import company.tap.gosellapi.internal.api.models.Token;
import company.tap.gosellapi.open.buttons.PayButtonView;
import company.tap.gosellapi.open.controllers.SDKSession;
import company.tap.gosellapi.open.controllers.ThemeObject;
import company.tap.gosellapi.open.delegate.SessionDelegate;
import company.tap.gosellapi.open.enums.AppearanceMode;
import company.tap.gosellapi.open.enums.CardType;
import company.tap.gosellapi.open.enums.TransactionMode;
import company.tap.gosellapi.open.models.CardsList;
import company.tap.gosellapi.open.models.Customer;
import company.tap.gosellapi.open.models.Receipt;
import company.tap.gosellapi.open.models.TapCurrency;


public class SellerPackageActivity extends BaseActivity<ActivitySellerPackageBinding, SellerPackageViewModel> implements SellerPackageNavigator, SessionDelegate {

    @Inject
    ViewModelProviderFactory factory;

    private SellerPackageViewModel sellerPackageViewModel;
    private final int SDK_REQUEST_CODE = 1001;
    private SDKSession sdkSession;
    private PayButtonView payButtonView;
    private SettingsManager settingsManager;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SellerPackageActivity.class);
        return intent;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_seller_package;
    }

    @Override
    public SellerPackageViewModel getViewModel() {
        sellerPackageViewModel = ViewModelProviders.of(this, factory).get(SellerPackageViewModel.class);
        return sellerPackageViewModel;
    }

    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.newIntent(SellerPackageActivity.this);
        startActivity(intent);
        finish();
    }
    @Override
    public void showErrorAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void openMainActivity() {

    }

    @Override
    public void openSellerHome() {
        Intent intent = SellerHomeActivity.newIntent(SellerPackageActivity.this,0);
        startActivity(intent);
        finish();
    }

    @Override
    public void openPayment() {
        Intent intent = SellerPackagePaymentActivity.newIntent(SellerPackageActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sellerPackageViewModel.setNavigator(this);
        settingsManager = SettingsManager.getInstance();
        settingsManager.setPref(this);
        startSDK();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (settingsManager == null) {
            settingsManager = SettingsManager.getInstance();
            settingsManager.setPref(this);
        }
    }

    @Override
    public void paymentSucceed(@NonNull Charge charge) {
        String transactionId = charge.getId();
        int userId = sellerPackageViewModel.getDataManager().getCurrentUserId();
        sellerPackageViewModel.paymentRequest(String.valueOf(userId), transactionId, "234");
    }

    @Override
    public void paymentFailed(@Nullable Charge charge) {
        Toast.makeText(this, "Payment failed.Please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void authorizationSucceed(@NonNull Authorize authorize) {

    }

    @Override
    public void authorizationFailed(Authorize authorize) {
        Toast.makeText(this, "Payment failed.Please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cardSaved(@NonNull Charge charge) {

    }

    @Override
    public void cardSavingFailed(@NonNull Charge charge) {

    }

    @Override
    public void cardTokenizedSuccessfully(@NonNull Token token) {

    }

    @Override
    public void savedCardsList(@NonNull CardsList cardsList) {

    }

    @Override
    public void sdkError(@Nullable GoSellError goSellError) {

    }

    @Override
    public void sessionIsStarting() {

    }

    @Override
    public void sessionHasStarted() {

    }

    @Override
    public void sessionCancelled() {

    }

    @Override
    public void sessionFailedToStart() {

    }

    @Override
    public void invalidCardDetails() {
        System.out.println("Arun 5");
    }

    @Override
    public void backendUnknownError(String message) {

    }

    @Override
    public void invalidTransactionMode() {

    }

    @Override
    public void invalidCustomerID() {

    }

    @Override
    public void userEnabledSaveCardOption(boolean saveCardEnabled) {

    }

    /**
     * Integrating SDK.
     */
    private void startSDK() {
        /**
         * Required step.
         * Configure SDK with your Secret API key and App Bundle name registered with tap company.
         */
        configureApp();

        /**
         * Optional step
         * Here you can configure your app theme (Look and Feel).
         */
        configureSDKThemeObject();

        /**
         * Required step.
         * Configure SDK Session with all required data.
         */
        configureSDKSession();

        /**
         * Required step.
         * Choose between different SDK modes
         */
        configureSDKMode();

        /**
         * If you included Tap Pay Button then configure it first, if not then ignore this step.
         */
        initPayButton();

    }

    /**
     * Required step.
     * Configure SDK with your Secret API key and App Bundle name registered with tap company.
     */
    private void configureApp() {
        GoSellSDK.init(this, "sk_test_Sgj5AtDivF7rw41n8fdkKQNT", "com.project.maqdoom");  // to be replaced by merchant
        GoSellSDK.setLocale("en");//  language to be set by merchant

    }

    /**
     * Configure SDK Theme
     */
    private void configureSDKThemeObject() {

        ThemeObject.getInstance()
                .setAppearanceMode(AppearanceMode.WINDOWED_MODE)
                .setSdkLanguage("en")

                .setHeaderFont(Typeface.createFromAsset(getAssets(), "fonts/Poppins-Regular.ttf"))
                .setHeaderTextColor(getResources().getColor(R.color.black1))
                .setHeaderTextSize(17)
                .setHeaderBackgroundColor(getResources().getColor(R.color.french_gray_new))


                .setCardInputFont(Typeface.createFromAsset(getAssets(), "fonts/Poppins-Regular.ttf"))
                .setCardInputTextColor(getResources().getColor(R.color.black))
                .setCardInputInvalidTextColor(getResources().getColor(R.color.red))
                .setCardInputPlaceholderTextColor(getResources().getColor(R.color.gray))

                .setSaveCardSwitchOffThumbTint(getResources().getColor(R.color.french_gray_new))
                .setSaveCardSwitchOnThumbTint(getResources().getColor(R.color.navigation_bg))
                .setSaveCardSwitchOffTrackTint(getResources().getColor(R.color.french_gray))
                .setSaveCardSwitchOnTrackTint(getResources().getColor(R.color.navigation_bg))

                .setScanIconDrawable(getResources().getDrawable(R.drawable.btn_card_scanner_normal))

                .setPayButtonResourceId(R.drawable.btn_pay_selector)  //btn_pay_merchant_selector
                .setPayButtonFont(Typeface.createFromAsset(getAssets(), "fonts/Poppins-Regular.ttf"))

                .setPayButtonDisabledTitleColor(getResources().getColor(R.color.white))
                .setPayButtonEnabledTitleColor(getResources().getColor(R.color.white))
                .setPayButtonTextSize(14)
                .setPayButtonLoaderVisible(true)
                .setPayButtonSecurityIconVisible(true)

                // setup dialog textcolor and textsize
                .setDialogTextColor(getResources().getColor(R.color.black1))     // **Optional**
                .setDialogTextSize(17)                // **Optional**

        ;

    }


    /**
     * Configure SDK Session
     */
    private void configureSDKSession() {

        // Instantiate SDK Session
        if (sdkSession == null) sdkSession = new SDKSession();   //** Required **

        // pass your activity as a session delegate to listen to SDK internal payment process follow
        sdkSession.addSessionDelegate(this);    //** Required **

        // initiate PaymentDataSource
        sdkSession.instantiatePaymentDataSource();    //** Required **

        // set transaction currency associated to your account
        sdkSession.setTransactionCurrency(new TapCurrency("KWD"));    //** Required **

        // Using static CustomerBuilder method available inside TAP Customer Class you can populate TAP Customer object and pass it to SDK
        sdkSession.setCustomer(getCustomer());    //** Required **

        // Set Total Amount. The Total amount will be recalculated according to provided Taxes and Shipping
        sdkSession.setAmount(new BigDecimal(234));  //** Required **

        // Set Payment Items array list
        sdkSession.setPaymentItems(new ArrayList<>());// ** Optional ** you can pass empty array list


//       sdkSession.setPaymentType("CARD");   //** Merchant can pass paymentType

        // Set Taxes array list
        sdkSession.setTaxes(new ArrayList<>());// ** Optional ** you can pass empty array list

        // Set Shipping array list
        sdkSession.setShipping(new ArrayList<>());// ** Optional ** you can pass empty array list

        // Post URL
        sdkSession.setPostURL(""); // ** Optional **

        // Payment Description
        sdkSession.setPaymentDescription(""); //** Optional **

        // Payment Extra Info
        sdkSession.setPaymentMetadata(new HashMap<>());// ** Optional ** you can pass empty array hash map

        // Payment Reference
        sdkSession.setPaymentReference(null); // ** Optional ** you can pass null

        // Payment Statement Descriptor
        sdkSession.setPaymentStatementDescriptor(""); // ** Optional **

        // Enable or Disable Saving Card
        sdkSession.isUserAllowedToSaveCard(true); //  ** Required ** you can pass boolean

        // Enable or Disable 3DSecure
        sdkSession.isRequires3DSecure(true);

        //Set Receipt Settings [SMS - Email ]
        sdkSession.setReceiptSettings(new Receipt(false, false)); // ** Optional ** you can pass Receipt object or null

        // Set Authorize Action
        sdkSession.setAuthorizeAction(null); // ** Optional ** you can pass AuthorizeAction object or null

        sdkSession.setDestination(null); // ** Optional ** you can pass Destinations object or null

        sdkSession.setMerchantID(null); // ** Optional ** you can pass merchant id or null

        sdkSession.setCardType(CardType.CREDIT); // ** Optional ** you can pass which cardType[CREDIT/DEBIT] you want.By default it loads all available cards for Merchant.

    }


    /**
     * Configure SDK Theme
     */
    private void configureSDKMode() {

        /**
         * You have to choose only one Mode of the following modes:
         * Note:-
         *      - In case of using PayButton, then don't call sdkSession.start(this); because the SDK will start when user clicks the tap pay button.
         */
        //////////////////////////////////////////////////////    SDK with UI //////////////////////
        /**
         * 1- Start using  SDK features through SDK main activity (With Tap CARD FORM)
         */
        startSDKWithUI();

    }


    /**
     * Start using  SDK features through SDK main activity
     */
    private void startSDKWithUI() {
        if (sdkSession != null) {
            TransactionMode trx_mode = (settingsManager != null) ? settingsManager.getTransactionsMode("key_sdk_transaction_mode") : TransactionMode.PURCHASE;
            // set transaction mode [TransactionMode.PURCHASE - TransactionMode.AUTHORIZE_CAPTURE - TransactionMode.SAVE_CARD - TransactionMode.TOKENIZE_CARD ]
            sdkSession.setTransactionMode(trx_mode);    //** Required **
            // if you are not using tap button then start SDK using the following call
            //sdkSession.start(this);
        }
    }

    /**
     * Include pay button in merchant page
     */
    private void initPayButton() {
        payButtonView = findViewById(R.id.payButtonId);
        if (ThemeObject.getInstance().getPayButtonFont() != null)
            payButtonView.setupFontTypeFace(ThemeObject.getInstance().getPayButtonFont());
        if (ThemeObject.getInstance().getPayButtonDisabledTitleColor() != 0 && ThemeObject.getInstance().getPayButtonEnabledTitleColor() != 0)
            payButtonView.setupTextColor(ThemeObject.getInstance().getPayButtonEnabledTitleColor(),
                    ThemeObject.getInstance().getPayButtonDisabledTitleColor());
        if (ThemeObject.getInstance().getPayButtonTextSize() != 0)
            payButtonView.getPayButton().setTextSize(ThemeObject.getInstance().getPayButtonTextSize());
        if (ThemeObject.getInstance().isPayButtSecurityIconVisible())
            payButtonView.getSecurityIconView().setVisibility(ThemeObject.getInstance().isPayButtSecurityIconVisible() ? View.INVISIBLE : View.INVISIBLE);
        if (ThemeObject.getInstance().getPayButtonResourceId() != 0)
            //payButtonView.setBackgroundSelector(ThemeObject.getInstance().getPayButtonResourceId());

        if (sdkSession != null) {
            TransactionMode trx_mode = sdkSession.getTransactionMode();
            if (trx_mode != null) {
            } else {
                configureSDKMode();
            }
            sdkSession.setButtonView(payButtonView, this, SDK_REQUEST_CODE);
        }


    }

    /**
     * Get customer data
     *
     * @return **
     */
    private Customer getCustomer() {
        final String currentUserName = sellerPackageViewModel.getDataManager().getCurrentUserName();
        final String currentUserEmail = sellerPackageViewModel.getDataManager().getEmail();
        final String phoneNumberSeller = sellerPackageViewModel.getDataManager().getPhone();

        Customer customer = (settingsManager != null) ? settingsManager.getCustomer() : null;
        PhoneNumber phoneNumber = customer != null ? customer.getPhone() : new PhoneNumber("", phoneNumberSeller);
        return new Customer.CustomerBuilder(null).email(currentUserEmail).firstName(currentUserName)
                .lastName("").metadata("").phone(new PhoneNumber(phoneNumber.getCountryCode(), phoneNumber.getNumber()))
                .middleName("").build();


    }
}
