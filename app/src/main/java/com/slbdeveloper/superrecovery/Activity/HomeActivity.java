package com.slbdeveloper.superrecovery.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.google.android.gms.ads.AdView;
import com.slbdeveloper.superrecovery.BaseActivity;
import com.slbdeveloper.superrecovery.R;
import com.slbdeveloper.superrecovery.Utils.AdsUtils;
import com.slbdeveloper.superrecovery.Utils.Constant;

import java.util.List;
import java.util.Objects;

import static com.slbdeveloper.superrecovery.Utils.Constant.INAPP_SUBSCRIPTION;
import static com.slbdeveloper.superrecovery.Utils.Constant.skuList;
import static com.slbdeveloper.superrecovery.Utils.Util.checkConnection;
import static com.slbdeveloper.superrecovery.Utils.Util.noInternetDialog;
import static com.slbdeveloper.superrecovery.Utils.Util.setBoolInPref;

public class HomeActivity extends BaseActivity implements PurchasesUpdatedListener {

    private static final String TAG = "HomeActivity";

    private CardView card_photo_scan;
    private CardView card_video_scan;
    private CardView card_audio_scan;
    private CardView card_restore_scan;

    private BillingClient billingClient;
    private AdView adomobadview;
    private LinearLayout facebookads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkRunTimePermission();

        initUI();



    }


    private void initUI(){

        card_photo_scan = findViewById(R.id.card_photo_scan);
        card_video_scan = findViewById(R.id.card_video_scan);
        card_audio_scan = findViewById(R.id.card_audio_scan);
        card_restore_scan = findViewById(R.id.card_restore_scan);

        adomobadview = findViewById(R.id.adView);
        facebookads = findViewById(R.id.facebookads);

        card_photo_scan.setOnClickListener(view -> {

            if (checkConnection(this)){
                startPhotoScan();
            }else{
                noInternetDialog(this);
            }
        });

        card_video_scan.setOnClickListener(view -> {
            if (checkConnection(this)){
                startVideoScan();
            }else{
                noInternetDialog(this);
            }
        });

        card_audio_scan.setOnClickListener(view -> {
            if (checkConnection(this)){
                startAudioScan();
            }else{
                noInternetDialog(this);
            }
        });

        card_restore_scan.setOnClickListener(view -> Restore());


    }


    private void startPhotoScan(){

        Intent PhotoScanIntent = new Intent(HomeActivity.this , ScanActivity.class);
        PhotoScanIntent.putExtra(Constant.SCAN_TYPE , Constant.PHOTO_SCAN);
        startActivity(PhotoScanIntent);

    }

    private void startVideoScan(){

        Intent PhotoScanIntent = new Intent(HomeActivity.this , ScanActivity.class);
        PhotoScanIntent.putExtra(Constant.SCAN_TYPE , Constant.VIDEO_SCAN);
        startActivity(PhotoScanIntent);

    }

    private void startAudioScan(){

        Intent PhotoScanIntent = new Intent(HomeActivity.this , ScanActivity.class);
        PhotoScanIntent.putExtra(Constant.SCAN_TYPE , Constant.AUDIO_SCAN);
        startActivity(PhotoScanIntent);

    }

    private void Restore(){
        Intent PhotoScanIntent = new Intent(HomeActivity.this , RestoreActivity.class);
        startActivity(PhotoScanIntent);
    }



    private void isUserHasSubscription(final Context context) {
        billingClient = BillingClient.newBuilder(context).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                final Purchase.PurchasesResult purchasesResult=billingClient.queryPurchases(BillingClient.SkuType.SUBS);

                billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, new PurchaseHistoryResponseListener() {
                    @Override
                    public void onPurchaseHistoryResponse(BillingResult billingResult1, List<PurchaseHistoryRecord> list) {

                        Log.d("billingprocess", "purchasesResult.getPurchasesList():" + purchasesResult.getPurchasesList());

                        if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK &&
                                !Objects.requireNonNull(purchasesResult.getPurchasesList()).isEmpty()) {

                            Constant.isUserSubcribed = true;

                        }
                    }
                });
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.d("billingprocess","onBillingServiceDisconnected");
            }
        });
    }


    private void setupBillingClient() {
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener(){

            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    isUserHasSubscription(HomeActivity.this);
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

    }


    @Override
    public void onPurchasesUpdated(BillingResult billingResult,  List<Purchase> list) {

        int responseCode = billingResult.getResponseCode();

        Log.d(TAG, "onPurchasesUpdated: " + responseCode);

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && list != null) {
            for (Purchase purchase : list) {
                handlePurchase(purchase);
            }
        }
        else
        if (responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            //Log.d(TAG, "User Canceled" + responseCode);
        }
        else if (responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            ///mSharedPreferences.edit().putBoolean(getResources().getString(R.string.pref_remove_ads_key), true).commit();
            ///setAdFree(true);

            if (list != null){
                for (Purchase purchase : list) {
                    setBoolInPref(this,"myPref",purchase.getSku(), true);
                }

            }


        }
        else {
            //Log.d(TAG, "Other code" + responseCode);
            // Handle any other error codes.
        }

    }


    private void handlePurchase(Purchase purchase) {
        if (purchase.getSku().equals(skuList)) {
            ///mSharedPreferences.edit().putBoolean(getResources().getString(R.string.pref_remove_ads_key), true).commit();
            ///setAdFree(true);
            Constant.isUserSubcribed = true;
            setBoolInPref(this, "myPref", purchase.getSku(), true);
            Toast.makeText(this, "Purchase done. you are now a premium member.", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (checkConnection(getApplicationContext())) {

            if (Constant.INAPP_SUBSCRIPTION){

                setupBillingClient();

            }else if(Constant.ADMOB_ADS){
                adomobadview.setVisibility(View.VISIBLE);

                AdsUtils.showGoogleBannerAd(HomeActivity.this,adomobadview);

            }else if (Constant.FACEBOOK_ADS){
                facebookads.setVisibility(View.VISIBLE);
                AdsUtils.showFBBannerAd(HomeActivity.this , facebookads);

            }

        }else {
            noInternetDialog(this);
        }


    }

}