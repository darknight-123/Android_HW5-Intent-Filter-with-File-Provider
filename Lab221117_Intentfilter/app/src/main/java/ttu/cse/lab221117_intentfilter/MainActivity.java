package ttu.cse.lab221117_intentfilter;
import static android.content.ContentValues.TAG;

import static androidx.core.content.FileProvider.getUriForFile;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.Manifest;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import ttu.cse.lab221117_intentfilter.R;

public class MainActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    public static String PACKAGE_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.VmPolicy.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            builder = new StrictMode.VmPolicy.Builder();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setVmPolicy(builder.build());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        PACKAGE_NAME = getApplicationContext().getPackageName();

    }

    public void onClick(View v) throws IOException {
        Intent it = new Intent();

        it.setAction(android.content.Intent.ACTION_VIEW);
        it.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        it.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        switch(v.getId()) {//讀取按鈕的 Id 來做相關處理
            case R.id.buttonEmail:   //指定 E-mail 地址
                it.setData(Uri.parse("mailto:mis@ttu.edu.tw"));
                it.putExtra(Intent.EXTRA_CC,                  //設定副本收件人
                        new String[] {"test@ttu.edu.tw"});
                it.putExtra(Intent.EXTRA_SUBJECT, "您好");  //設定主旨
                it.putExtra(Intent.EXTRA_TEXT, "謝謝！");   //設定內容
                break;
            case R.id.buttonSms:  //指定簡訊的傳送對象及內容
                it.setData(Uri.parse("sms:0999-123456?body=您好！"));
                break;
            case R.id.buttonWeb:  //指定網址
                it.setData(Uri.parse("http://www.ttu.edu.tw"));
                break;
            case R.id.buttonGps:  //指定 GPS 座標：台北火車站
                it.setData(Uri.parse("geo:25.047095,121.517308"));
                break;
            case R.id.buttonWebSearch:  //搜尋 Web 資料
                it.setAction(Intent.ACTION_WEB_SEARCH);  //將動作改為搜尋
                it.putExtra(SearchManager.QUERY, "大同大學");
                break;
            case R.id.buttonMapSearch:    //搜尋資料
                it.setData(Uri.parse("geo:0,0?q=大安森林公園"));
                break;
            case R.id.tel:  //指定電話
                // it.setAction(Intent.ACTION_DIAL);  //將動作改為撥號
                // it.setAction(Intent.ACTION_CALL); // 直接call指定電話 (need "permission")
                // working fine in the previous version (Lollipop and kitkat) but unfortunately isn't on Marshmallow
                // In Android 6.0 (API level 23) or higher, dangerous permissions must be declared in the manifest AND you
                // must explicitly request that permission from the user. CALL_PHONE is considered a dangerous permission.
                // 參 https://developer.android.com/guide/topics/permissions/overview#permission-groups
                // 以下參 https://developer.android.com/training/permissions/requesting
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.READ_CONTACTS)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                        // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    it.setAction(Intent.ACTION_CALL);
                }
                it.setData(Uri.parse("tel:0800-123123"));
                break;
            case R.id.mp3:
                //File file = new File("/sdcard/Download/Yeah - Usher.mp3");
                //it.setDataAndType(FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file),"audio/*");
                //it.setDataAndType(Uri.fromFile(new File("/sdcard/Music/ye.mp3")), "audio/*");
                File MusicPath = new File("/sdcard/Music/ye.mp3");
                //Uri contentUri_0 = getUriForFile(this, getPackageName() +".fileProvider", MusicPath);
                it.setDataAndType(Uri.fromFile(MusicPath),"audio/*");
                break;
            case R.id.pic:

                File ImagePath = new File("/sdcard/Pictures/pexels.jpg");

                //Uri contentUri_0 = getUriForFile(this, getPackageName() +".fileProvider", MusicPath);
                it.setDataAndType(Uri.fromFile(ImagePath),"image/*");

                break;
        }

            startActivity(it);


        }



    public static void grantPermissions(Context context, Intent intent, Uri uri, boolean writeAble) {
        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        if (writeAble) {
            flag |= Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        }
        intent.addFlags(flag);
        List<ResolveInfo> resInfoList = context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, flag);
        }
    }
    }


