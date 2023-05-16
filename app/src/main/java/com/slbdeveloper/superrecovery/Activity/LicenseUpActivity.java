package com.slbdeveloper.superrecovery.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.slbdeveloper.superrecovery.Adapter.SubscriptionAdapter;
import com.slbdeveloper.superrecovery.R;
import com.slbdeveloper.superrecovery.Utils.Constant;

import java.util.List;

import static com.slbdeveloper.superrecovery.Utils.Constant.skuList;
import static com.slbdeveloper.superrecovery.Utils.Util.checkConnection;
import static com.slbdeveloper.superrecovery.Utils.Util.setBoolInPref;


public class LicenseUpActivity extends AppCompatActivity  implements PurchasesUpdatedListener {

    private static final String TAG = "LicenseUpActivity";
    private SubscriptionAdapter subscriptionAdapter;


    private BillingClient billingClient;
    private SkuDetails mSkuDetails;
    RecyclerView recyclerView;
    private String Clicked_Sku;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_up);

        //subscriptionAdapter = new SubscriptionAdapter(LicenseUpActivity.this, demo.banner_images);

        recyclerView = findViewById(R.id.rv_upgrade_options);
        loading = findViewById(R.id.loading);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView close_image_view = findViewById(R.id.close_image_view);
        close_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (checkConnection(getApplicationContext())) {
            setupBillingClient();

        }else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LicenseUpActivity.this);
            alertDialogBuilder.setTitle("No Internet Connection");
            alertDialogBuilder.setMessage("You are offline please check your internet connection");
            Toast.makeText(LicenseUpActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    //Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }



    }

    private void setupBillingClient() {
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener(){

            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                Log.d(TAG, "onBillingSetupFinished: ");
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is setup successfully
                    Log.d(TAG, "onBillingSetupFinished: OK ");
                    loadAllSKUs();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.d(TAG, "onBillingServiceDisconnected: ");
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

    }

    private void loadAllSKUs() {

        if (billingClient.isReady())
        {

            SkuDetailsParams params = SkuDetailsParams.newBuilder()
                    .setSkusList(skuList)
                    .setType(BillingClient.SkuType.SUBS)
                    .build();

            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {

                    loading.setVisibility(View.GONE);

                    Log.d(TAG, "onSkuDetailsResponse: " + billingResult.getResponseCode());
                    Log.d(TAG, "onSkuDetailsResponse: " + skuDetailsList.toString());

                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                            && !skuDetailsList.isEmpty())
                    {

                        Log.d(TAG, "onSkuDetailsResponse: " + skuDetailsList.toString());
                        
                        subscriptionAdapter = new SubscriptionAdapter(LicenseUpActivity.this, skuDetailsList);
                        recyclerView.setAdapter(subscriptionAdapter);
                        recyclerView.setHasFixedSize(true);

                        subscriptionAdapter.setOnSubClick(new SubscriptionAdapter.onSubClick() {
                            @Override
                            public void onsubClick(SkuDetails skuDetails, String sku) {

                                Clicked_Sku = sku;


                                BillingFlowParams billingFlowParams = BillingFlowParams
                                        .newBuilder()
                                        .setSkuDetails(skuDetails)
                                        .build();
                                billingClient.launchBillingFlow(LicenseUpActivity.this, billingFlowParams);

                            }


                        });

                    }
                }
            });
        }
        else
            Toast.makeText(this, "billingclient not ready", Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> list) {

        int responseCode = billingResult.getResponseCode();
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
            setBoolInPref(this,"myPref",Clicked_Sku, true );
        }
        else {
            //Log.d(TAG, "Other code" + responseCode);
            // Handle any other error codes.
        }

    }


    private void handlePurchase(Purchase purchase) {
        if (purchase.getSku().equals(Clicked_Sku)) {
            ///mSharedPreferences.edit().putBoolean(getResources().getString(R.string.pref_remove_ads_key), true).commit();
            ///setAdFree(true);

            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {

                //This is for Consumable product
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                        Log.d("purchase", "Purchase Acknowledged");
                    }
                });

                Constant.isUserSubcribed = true;
                setBoolInPref(this,"myPref",Clicked_Sku, true );
                Toast.makeText(this, "Purchase done. you are now a premium member.", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
