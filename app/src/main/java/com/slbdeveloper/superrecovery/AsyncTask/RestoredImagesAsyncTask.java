package com.slbdeveloper.superrecovery.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.core.os.EnvironmentCompat;

import com.slbdeveloper.superrecovery.Callback.AudioCount;
import com.slbdeveloper.superrecovery.Callback.ScannerCount;
import com.slbdeveloper.superrecovery.Callback.VideoCount;
import com.slbdeveloper.superrecovery.Model.ImageData;
import com.slbdeveloper.superrecovery.Utils.Constant;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class RestoredImagesAsyncTask extends AsyncTask<String, ImageData, ArrayList<ImageData>> {
    private final String TAG = getClass().getName();
    private ArrayList<ImageData> alImageData;
    private Context context;
    private Handler handler;
    //private String scanType;
    private ProgressDialog progressDialog;
    int i = 0;
    int v = 0;
    int a = 0;
    private ScannerCount scannerCount;
    private final VideoCount videoCount;
    private final AudioCount audioCount;

    public RestoredImagesAsyncTask(Context context, Handler handler, ScannerCount scannerCount, VideoCount videoCount, AudioCount audioCount) {
        this.context = context;
        this.handler = handler;
        this.alImageData = new ArrayList();
        this.scannerCount = scannerCount;
        this.videoCount = videoCount;
        this.audioCount = audioCount;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected ArrayList<ImageData> doInBackground(String... strAr) {

        String strArr = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RestoredPhotos";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("root = ");
        stringBuilder.append(strArr);
        getSdCardImage();
        checkFileOfDirectoryVideo(strArr, getFileList(strArr));
        //checkFileOfDirectory(getFileList(strArr));

        return this.alImageData;
    }


    public void getSdCardImage() {

        Log.d(TAG, "getSdCardImage: ");

        String[] externalStorageDirectories = getExternalStorageDirectories();
        if (externalStorageDirectories != null && externalStorageDirectories.length > 0) {
            for (String str : externalStorageDirectories) {
                File file = new File(str);
                if (file.exists()) {
                    //checkFileOfDirectory(file.listFiles());
                    checkFileOfDirectoryVideo(str, file.listFiles());
                }
            }
        }
    }

    public void checkFileOfDirectoryVideo(String str, File[] fileArr) {

        Log.d(TAG, "checkFileOfDirectoryVideo: ");

        File[] fileArr2 = fileArr;
        if (fileArr2 != null) {

            for (int i = 0; i < fileArr.length; i++) {

                Integer[] numArr = new Integer[1];
                Integer[] video_numArr = new Integer[1];
                Integer[] audio_numArr = new Integer[1];

                if (fileArr2[i].isDirectory()) {
                    String path = fileArr2[i].getPath();
                    File[] fileList = getFileList(fileArr2[i].getPath());
                    if (!(path == null || fileList == null || fileList.length <= 0)) {

                        checkFileOfDirectoryVideo(path, fileList);
                    }
                } else {
                    Options options = new Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(fileArr2[i].getPath(), options);

                    //if (scanType.equalsIgnoreCase(Constant.PHOTO_SCAN)) {

                    if (!(options.outWidth == -1 || options.outHeight == -1)) {

                        if (fileArr[i].getPath().endsWith(".jpg") || fileArr[i].getPath().endsWith(".jpeg") || fileArr[i].getPath().endsWith(".png") || fileArr[i].getPath().endsWith(".gif")) {

                            File file = new File(fileArr[i].getPath());
                            int parseInt = Integer.parseInt(String.valueOf(file.length()));
                            if (parseInt > 10000) {
                                this.alImageData.add(new ImageData(fileArr[i].getPath(), false, file.lastModified(), (long) parseInt));
                                //ScanFileActivity.this.arrFileScan.add(new ItemFileScan(false, fileArr2[i].getPath(), file.lastModified(), (long) parseInt));
                                int i2 = this.i++ + 1;
                                numArr[0] = i2;
                                scannerCount.scancount(numArr);
                                publishProgress(new ImageData(fileArr[i].getPath(), false, file.lastModified(), (long) parseInt));
                            }

                        } else {

                            File file2 = new File(fileArr2[i].getPath());
                            int parseInt2 = Integer.parseInt(String.valueOf(file2.length()));
                            if (parseInt2 > 50000) {
                                File file22 = file2;
                                this.alImageData.add(new ImageData(fileArr[i].getPath(), false, file2.lastModified(), (long) parseInt2));

                                int i2 = this.i++ + 1;
                                numArr[0] = i2;

                                scannerCount.scancount(numArr);

                                publishProgress(new ImageData(fileArr[i].getPath(), false, file2.lastModified(), (long) parseInt2));
                            }

                        }

                    }

                    //} else if (scanType.equalsIgnoreCase(Constant.VIDEO_SCAN)) {

                    else if (fileArr2[i].getPath().endsWith(".3gp") || fileArr2[i].getPath().endsWith(".mp4") || fileArr2[i].getPath().endsWith(".mkv") || fileArr2[i].getPath().endsWith(".flv")) {

                        File file = new File(fileArr2[i].getPath());

                        int parseInt = Integer.parseInt(String.valueOf(file.length()));

                        this.alImageData.add(new ImageData(fileArr[i].getPath(), false, file.lastModified(), (long) parseInt));


                        int i2 = this.v++ + 1;

                        video_numArr[0] = i2;

                        videoCount.videocount(video_numArr);

                        publishProgress(new ImageData(fileArr[i].getPath(), false, file.lastModified(), (long) parseInt));

                    } else if (fileArr2[i].getPath().endsWith(".mp3")) {

                        File file = new File(fileArr2[i].getPath());

                        int parseInt = Integer.parseInt(String.valueOf(file.length()));

                        this.alImageData.add(new ImageData(fileArr[i].getPath(), false, file.lastModified(), (long) parseInt));

                        int i2 = this.a++ + 1;
                        audio_numArr[0] = i2;

                        audioCount.audiocount(audio_numArr);

                        publishProgress(new ImageData(fileArr[i].getPath(), false, file.lastModified(), (long) parseInt));

                    }

                }
            }

        }

    }


    public File[] getFileList(String str) {
        File file = new File(str);
        Log.d(TAG, "getFileList: " + file.getAbsolutePath());
        if (!file.isDirectory()) {
            return null;
        }
        return file.listFiles();
    }

    protected void onProgressUpdate(ImageData... numArr) {
        if (this.handler != null) {
            Message message = Message.obtain();
            message.what = Constant.UPDATE;
            message.obj = numArr[0];
            this.handler.sendMessage(message);
        }
    }

    protected void onPostExecute(ArrayList<ImageData> arrayList) {
        if (this.progressDialog != null) {
            this.progressDialog.cancel();
            this.progressDialog = null;
        }
        if (this.handler != null) {
            Message message = Message.obtain();
            message.what = Constant.DATA;
            message.obj = arrayList;
            this.handler.sendMessage(message);
        }
        super.onPostExecute(arrayList);
    }


    public String[] getExternalStorageDirectories() {
        int length;
        ArrayList arrayList = new ArrayList();
        int i = 0;
        if (Build.VERSION.SDK_INT >= 19) {
            File[] externalFilesDirs = context.getExternalFilesDirs(null);
            File[] externalFilesDirs2 = externalFilesDirs;
            if (externalFilesDirs != null && externalFilesDirs2.length > 0) {
                for (File file : externalFilesDirs2) {
                    if (file != null) {
                        String[] split = file.getPath().split("/Android");
                        String[] split2 = split;
                        if (split != null && split2.length > 0) {
                            boolean z;
                            String str = split2[0];
                            if (Build.VERSION.SDK_INT >= 21) {
                                z = Environment.isExternalStorageRemovable(file);
                            } else {
                                z = "mounted".equals(EnvironmentCompat.getStorageState(file));
                            }
                            if (z) {
                                arrayList.add(str);
                            }
                        }
                    }
                }
            }
        }
        if (arrayList.isEmpty()) {
            String str2 = "";
            try {
                Process start = new ProcessBuilder(new String[0]).command(new String[]{"mount | grep /dev/block/vold"}).redirectErrorStream(true).start();
                start.waitFor();
                InputStream inputStream2 = start.getInputStream();
                byte[] bArr = new byte[1024];
                while (inputStream2.read(bArr) != -1) {
                    str2 = str2 + new String(bArr);
                }
                inputStream2.close();
            } catch (Exception e) {
            }
            if (!str2.trim().isEmpty()) {
                String[] split22 = str2.split("\n");
                if (split22.length > 0) {
                    length = split22.length;
                    while (i < length) {
                        arrayList.add(split22[i].split(" ")[2]);
                        i++;
                    }
                }
            }
        }
        String[] strArr = new String[arrayList.size()];
        for (i = 0; i < arrayList.size(); i++) {
            strArr[i] = (String) arrayList.get(i);
        }
        return strArr;
    }
}
