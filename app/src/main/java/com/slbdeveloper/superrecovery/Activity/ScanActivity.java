package com.slbdeveloper.superrecovery.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.slbdeveloper.superrecovery.Adapter.AdapterImage;
import com.slbdeveloper.superrecovery.AsyncTask.RecoverPhotosAsyncTask;
import com.slbdeveloper.superrecovery.AsyncTask.ScanImagesAsyncTask;
import com.slbdeveloper.superrecovery.BaseActivity;
import com.slbdeveloper.superrecovery.Callback.ScannerCount;
import com.slbdeveloper.superrecovery.Model.ImageData;
import com.slbdeveloper.superrecovery.R;
import com.slbdeveloper.superrecovery.Utils.AdsUtils;
import com.slbdeveloper.superrecovery.Utils.Constant;
import com.slbdeveloper.superrecovery.View.ScanButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScanActivity extends BaseActivity implements ScannerCount {

    private static final String TAG = "ScanActivity";

    private static final int HIDE_THRESHOLD = 10;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    public static Toolbar toolbar;
    ScanButton scanButton;
    private TextView scanCount , tvScannedPics;
    private AdapterImage adapterImage;
    private ScanActivity.MyDataHandler myDataHandler;
    private RecyclerView gvAllPics;
    private AppCompatButton restore_button;
    private ArrayList<ImageData> alImageData = new ArrayList();
    private ScannerCount scannerCount;
    private LinearLayout button_container;
    Intent intent;
    private AdView adomobadview;
    private LinearLayout facebookads;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scannerCount=this;

        myDataHandler = new ScanActivity.MyDataHandler();

        initUI();


    }

    private void initUI(){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adomobadview = findViewById(R.id.adView);
        facebookads = findViewById(R.id.facebookads);

        toolbar.setVisibility(View.GONE);

        button_container = findViewById(R.id.button_container);
        tvScannedPics = findViewById(R.id.tvItems);
        scanCount = findViewById(R.id.scanCount);

        scanButton = findViewById(R.id.scan_button);
        restore_button = findViewById(R.id.restore_button);

        gvAllPics = findViewById(R.id.scanitemrv);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        linearLayoutManager.setReverseLayout(true);
        gvAllPics.setLayoutManager(linearLayoutManager);
        gvAllPics.setItemAnimator(new DefaultItemAnimator());
        adapterImage = new AdapterImage(this, this.alImageData);
        gvAllPics.setHasFixedSize(true);
        gvAllPics.setAdapter(adapterImage);

        intent = getIntent();

        new ScanImagesAsyncTask(getApplicationContext(), myDataHandler , scannerCount).execute(intent.getStringExtra(Constant.SCAN_TYPE));
        tvScannedPics.setVisibility(View.VISIBLE);

        scanButton.setVisibility(View.VISIBLE);
        scanButton.f4441f.setImageResource(R.drawable.ic_scan_progress_circle);
        scanButton.f4442g.start();


        gvAllPics.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    Log.d(TAG, "onScrolled: show dialog " );
                    controlsVisible = false;
                    scrolledDistance = 0;

                    if (Constant.INAPP_SUBSCRIPTION){
                        showDialog(ScanActivity.this);
                    }

                }else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    controlsVisible = true;
                    scrolledDistance = 0;
                }

                if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
                    scrolledDistance += dy;
                }
            }
        });


        restore_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                if (Constant.INAPP_SUBSCRIPTION){

                    if (Constant.isUserSubcribed) {
                        repairingPictures(adapterImage.getSelectedItem());
                    } else {

                        startActivity(new Intent(ScanActivity.this, LicenseUpActivity.class));

                    }

                }else{
                    RestoreImage();
                }

            }
        });

    }



    private void RestoreImage(){

        if (adapterImage.getSelectedItem().size() > 0){
            repairingPictures(adapterImage.getSelectedItem());
        }else{
            Toast.makeText(ScanActivity.this, "Select Restore Images!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void scancount(Integer[] arr) {

        final StringBuilder sbProgressMessage = new StringBuilder();
        sbProgressMessage.append(arr[0]);
        sbProgressMessage.append(" ");
        sbProgressMessage.append("files found");

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                tvScannedPics.setText(sbProgressMessage.toString());
                scanCount.setText(sbProgressMessage.toString());

            }
        });

    }


    public class MyDataHandler extends Handler {

        class AnimationHandlerClass implements Animation.AnimationListener {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            AnimationHandlerClass() {
            }

            public void onAnimationEnd(Animation animation) {
                ///ivLoading.setVisibility(View.GONE);

            }
        }

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1000) {
                Animation animation = new AlphaAnimation(1.0f, 0.0f);
                animation.setDuration(2000);
                animation.setFillAfter(true);
                animation.setAnimationListener(new ScanActivity.MyDataHandler.AnimationHandlerClass());
                scanButton.setVisibility(View.GONE);
                tvScannedPics.setVisibility(View.GONE);
                //ivLoading.clearAnimation();

                if (Constant.ADMOB_ADS){
                    AdsUtils.LoadGoogleInterstitialAd(ScanActivity.this);
                }else if(Constant.FACEBOOK_ADS){
                    AdsUtils.FBInterstitialAdsINIT(ScanActivity.this);
                    AdsUtils.showFacebookInterstitial();
                }


                Collections.sort(alImageData, new Comparator<ImageData>() {
                    public int compare(ImageData photoModel, ImageData photoModel2) {
                        return Long.valueOf(photoModel2.getSizeFile()).compareTo(Long.valueOf(photoModel.getSizeFile()));
                    }
                });
                alImageData.clone();
                Collections.reverse(alImageData);

                adapterImage.notifyDataSetChanged();

                button_container.setVisibility(View.VISIBLE);

            } else if (message.what == 2000) {
                adapterImage.setAllImagesUnseleted();
                adapterImage.notifyDataSetChanged();
            } else if (message.what == 3000) {
                alImageData.add((ImageData) message.obj);
                adapterImage.notifyDataSetChanged();
                gvAllPics.scrollToPosition(adapterImage.getItemCount()-1);


            }
        }
    }

    public void repairingPictures(ArrayList<ImageData> arrayList) {
        RecoverPhotosAsyncTask repairTask = new RecoverPhotosAsyncTask(this, arrayList, myDataHandler);
        repairTask.execute(intent.getStringExtra(Constant.SCAN_TYPE));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Constant.ADMOB_ADS){
            adomobadview.setVisibility(View.VISIBLE);
            AdsUtils.showGoogleBannerAd(ScanActivity.this,adomobadview);
        }else if(Constant.FACEBOOK_ADS){
            facebookads.setVisibility(View.VISIBLE);
            AdsUtils.showFBBannerAd(ScanActivity.this , facebookads);
        }

    }


    public void showDialog(Context activity) {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_promote_after_scan_complete);

        ImageView cancel_button =  dialog.findViewById(R.id.cancel_button);
        Button subscribe_button = dialog.findViewById(R.id.subscribe_button);

        TextView scanned_count_text_view = dialog.findViewById(R.id.scanned_count_text_view);

        scanned_count_text_view.setText(tvScannedPics.getText().toString());

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        subscribe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, LicenseUpActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();

    }

}