package com.slbdeveloper.superrecovery.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.slbdeveloper.superrecovery.Model.ImageData;
import com.slbdeveloper.superrecovery.Utils.Constant;
import com.slbdeveloper.superrecovery.Utils.MediaScanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RecoverPhotosAsyncTask extends AsyncTask<String, String, String> {
    private final String TAG = getClass().getName();
    private ArrayList<ImageData> alImageData;
    private Context mContext;
    private Handler handler;
    private ProgressDialog progressDialog;
    private String scanType;

    public RecoverPhotosAsyncTask(Context context, ArrayList<ImageData> alImageData, Handler handler) {
        this.mContext = context;
        this.handler = handler;
        this.alImageData = alImageData;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog = new ProgressDialog(this.mContext);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Restoring");
        this.progressDialog.show();
    }

    protected String doInBackground(String... strAr) {

        scanType = strAr[0];

        for (int strArr = 0; strArr < this.alImageData.size(); strArr++) {
            File sourceFile = new File((this.alImageData.get(strArr)).getFilePath());
            File fileDirectory = new File(Constant.IMAGE_RECOVER_DIRECTORY);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Constant.IMAGE_RECOVER_DIRECTORY);
            stringBuilder.append(File.separator);
            stringBuilder.append(getFileName(((ImageData) this.alImageData.get(strArr)).getFilePath() , scanType));
            File destinationFile = new File(stringBuilder.toString());
            try {
                if (!destinationFile.exists()) {
                    fileDirectory.mkdirs();
                }
                copy(sourceFile, destinationFile);
                if (VERSION.SDK_INT >= 19) {
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(Uri.fromFile(destinationFile));
                    this.mContext.sendBroadcast(intent);
                }
                new MediaScanner(this.mContext, destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void copy(File file, File file2) throws IOException {
        FileChannel source = null;
        FileChannel destination = null;

        source = new FileInputStream(file).getChannel();
        destination = new FileOutputStream(file2).getChannel();

        source.transferTo(0, source.size(), destination);
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }


    public String getFileName(String filepath , String fileType) {
        String substring = filepath.substring(filepath.lastIndexOf("/") + 1);
        String str2;
        if (fileType.equalsIgnoreCase("photo")) {
            str2 = ".jpg";
            if (substring.endsWith(str2) || substring.endsWith(".jpeg") || substring.endsWith(".gif")) {
                return substring;
            }
            return substring + str2;
        }else if (fileType.equalsIgnoreCase("video")){
            str2 = ".mp4";
            if (substring.endsWith(str2)) {
                return substring;
            }
            return substring + str2;
        }
        str2 = ".mp3";
        if (substring.endsWith(str2)) {
            return substring;
        }
        return substring + str2;



    }

    protected void onPostExecute(String str) {
        Toast.makeText(mContext, "Restored successfully", Toast.LENGTH_SHORT).show();
        if (this.progressDialog != null) {
            this.progressDialog.cancel();
            this.progressDialog = null;
        }
        if (this.handler != null) {
            Message obtain = Message.obtain();
            obtain.what = Constant.REPAIR;
            obtain.obj = str;
            this.handler.sendMessage(obtain);
        }
        super.onPostExecute(str);
    }
}
