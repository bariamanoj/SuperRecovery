package com.slbdeveloper.superrecovery.Utils;

import android.Manifest;
import android.app.Activity;
import android.os.Environment;

import com.slbdeveloper.superrecovery.Activity.HomeActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Constant {


    public static  List<String> skuList = new ArrayList();
    public static String sku_oneyear = "binappsub03";
    public static String sku_threemonth = "binappsub02";
    public static String sku_onemonth= "binappsub01";

    public static boolean isUserSubcribed = false;

    public static boolean ADMOB_ADS = true;
    public static boolean FACEBOOK_ADS = false;
    public static boolean INAPP_SUBSCRIPTION = false;


    public static final int requestForPermission=0;
    public static final String [] PERMISSIONS={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };

    public static final int DATA = 1000;
    public static final int REPAIR = 2000;
    public static final int UPDATE = 3000;
    public static final String IMAGE_RECOVER_DIRECTORY;

    public static String SCAN_TYPE = "scanType";
    public static String PHOTO_SCAN = "photo";
    public static String VIDEO_SCAN = "video";
    public static String AUDIO_SCAN = "audio";


    static {
        StringBuilder sbDirectory = new StringBuilder();
        sbDirectory.append(Environment.getExternalStorageDirectory());
        sbDirectory.append(File.separator);
        sbDirectory.append("RestoredPhotos");
        IMAGE_RECOVER_DIRECTORY = sbDirectory.toString();
    }

}
