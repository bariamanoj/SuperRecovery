package com.slbdeveloper.superrecovery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.slbdeveloper.superrecovery.Utils.Constant;
import com.slbdeveloper.superrecovery.View.HoleView;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overridePendingTransition(0,0);

    }


    public void checkRunTimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Constant.PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Constant.PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Constant.PERMISSIONS[2]) != PackageManager.PERMISSION_GRANTED

            ) {

                requestPermissions(Constant.PERMISSIONS, Constant.requestForPermission);
            } else {
                // allowed
                //rlParent.setVisibility(View.VISIBLE);
            }
        } else {
            // less than Marshmallow
            //rlParent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constant.requestForPermission: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Constant.PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED
                                || checkSelfPermission(Constant.PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
                                || checkSelfPermission(Constant.PERMISSIONS[2]) != PackageManager.PERMISSION_GRANTED
                        ) {
                            Toast.makeText(this, "Permission must be required for this app", Toast.LENGTH_SHORT).show();
                            requestPermissions(Constant.PERMISSIONS, Constant.requestForPermission);
                        } else {
                            //allowed
                            //  rlParent.setVisibility(View.VISIBLE);

                        }
                    }
                } else {
                }

                break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}