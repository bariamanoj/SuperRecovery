package com.slbdeveloper.superrecovery.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.slbdeveloper.superrecovery.Adapter.AdapterImage;
import com.slbdeveloper.superrecovery.Adapter.RestoreAdapter;
import com.slbdeveloper.superrecovery.AsyncTask.RestoredImagesAsyncTask;
import com.slbdeveloper.superrecovery.Callback.AudioCount;
import com.slbdeveloper.superrecovery.Callback.ScannerCount;
import com.slbdeveloper.superrecovery.Callback.VideoCount;
import com.slbdeveloper.superrecovery.Model.ImageData;
import com.slbdeveloper.superrecovery.R;
import com.slbdeveloper.superrecovery.Utils.AdsUtils;
import com.slbdeveloper.superrecovery.Utils.Constant;

import java.util.ArrayList;

public class RestoreActivity extends AppCompatActivity implements ScannerCount , VideoCount, AudioCount {

    private static final String TAG = "RestoreActivity";

    private ImageView ivLoading;
    private RestoreAdapter adapterImage , adapterVideo  ,adapterAudio;
    private MyDataHandler myDataHandler;
    private RecyclerView gvAllPics;
    private ArrayList<ImageData> alImageData = new ArrayList();
    private ArrayList<ImageData> videoList = new ArrayList();
    private ArrayList<ImageData> audioList = new ArrayList();
    private TextView photo_count_text_view , video_count_text_view , audio_count_text_view;
    public static Toolbar toolbar;
    private ScannerCount scannerCount;
    private VideoCount videoCount;
    private AudioCount audioCount;
    private AdView adomobadview;
    private LinearLayout facebookads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);

        init();

    }


    public void init(){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scannerCount=this;
        videoCount=this;
        audioCount=this;

        myDataHandler = new MyDataHandler();
        gvAllPics = findViewById(R.id.gvGallery);

        adomobadview = findViewById(R.id.adView);
        facebookads = findViewById(R.id.facebookads);

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        gvAllPics.setLayoutManager(linearLayoutManager);
        gvAllPics.setItemAnimator(new DefaultItemAnimator());
        adapterImage = new RestoreAdapter(this, this.alImageData);
        gvAllPics.setHasFixedSize(true);
        gvAllPics.setAdapter(adapterImage);

        photo_count_text_view = findViewById(R.id.photo_count_text_view);
        video_count_text_view = findViewById(R.id.video_count_text_view);
        audio_count_text_view = findViewById(R.id.audio_count_text_view);
        ivLoading = findViewById(R.id.iv_start_scan_anim);
        ivLoading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scan_animation));
        new RestoredImagesAsyncTask(getApplicationContext(), this.myDataHandler , scannerCount , videoCount , audioCount).execute();
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

                photo_count_text_view.setText(sbProgressMessage.toString());

            }
        });

    }

    @Override
    public void audiocount(Integer[] arr) {

        final StringBuilder sbProgressMessage = new StringBuilder();
        sbProgressMessage.append(arr[0]);
        sbProgressMessage.append(" ");
        sbProgressMessage.append("files found");

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                audio_count_text_view.setText(sbProgressMessage.toString());

            }
        });

    }

    @Override
    public void videocount(Integer[] arr) {

        final StringBuilder sbProgressMessage = new StringBuilder();
        sbProgressMessage.append(arr[0]);
        sbProgressMessage.append(" ");
        sbProgressMessage.append("files found");

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                video_count_text_view.setText(sbProgressMessage.toString());

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
                ivLoading.setVisibility(View.GONE);
            }
        }

        //        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == Constant.DATA) {
                Animation animation = new AlphaAnimation(1.0f, 0.0f);
                animation.setDuration(500);
                animation.setFillAfter(true);
                animation.setAnimationListener(new AnimationHandlerClass());

                alImageData.clear();
                alImageData.addAll((ArrayList) message.obj);
                adapterImage.notifyDataSetChanged();

                Log.d(TAG, "handleMessage: " + alImageData.size());

                //tvScannedPics.setVisibility(View.GONE);
                ivLoading.clearAnimation();
                ivLoading.startAnimation(animation);
            } else if (message.what == Constant.REPAIR) {
                adapterImage.notifyDataSetChanged();
            } else if (message.what == Constant.UPDATE) {

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Constant.ADMOB_ADS){
            adomobadview.setVisibility(View.VISIBLE);
            AdsUtils.showGoogleBannerAd(RestoreActivity.this,adomobadview);
        }else if(Constant.FACEBOOK_ADS){
            facebookads.setVisibility(View.VISIBLE);
            AdsUtils.showFBBannerAd(RestoreActivity.this , facebookads);
        }

    }

}