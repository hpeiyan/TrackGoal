package club.peiyan.goaltrack;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import club.peiyan.goaltrack.utils.LogUtil;
import club.peiyan.goaltrack.utils.ThreadUtil;
import club.peiyan.goaltrack.utils.ToastUtil;

/**
 * Created by HPY.
 * Time: 2018/7/12.
 * Desc:
 */

public class LoadingActivity extends BaseActivity {

    private static final int REQUEST_CODE = 1028;

    private final static String[] mPermissionList = new String[]{
//            Manifest.permission.WRITE_CALENDAR
            Manifest.permission.WRITE_EXTERNAL_STORAGE
//            , Manifest.permission.ACCESS_FINE_LOCATION
//            , Manifest.permission.CALL_PHONE
//            , Manifest.permission.READ_LOGS
//            , Manifest.permission.READ_PHONE_STATE
//            , Manifest.permission.READ_EXTERNAL_STORAGE
//            , Manifest.permission.SET_DEBUG_APP
//            , Manifest.permission.SYSTEM_ALERT_WINDOW
//            , Manifest.permission.GET_ACCOUNTS
//            , Manifest.permission.WRITE_APN_SETTINGS
    };
    private static final java.lang.String TAG = "LoadingActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        copyDB();
        getSupportActionBar().hide();
        checkAppPermission();
    }

    private void copyDB() {
        ThreadUtil.ioThread(() -> {
            File mDBFile = getDatabasePath("UserTrackGoal.db");
            if (mDBFile == null || !mDBFile.exists()) {
                LogUtil.logi(TAG, "start copy database");
                initData();
            }
        });
    }

    /**
     * 拷贝数据库
     */
    private void initData() {
        AssetManager assetManager = getAssets();
        InputStream in;
        OutputStream out;
        try {
            in = assetManager.open("UserTrackGoal.db");
            String mS = getFilesDir().getAbsolutePath();
            String mDatabasePath = mS.substring(0, mS.length() - 5) + "databases";
            File outFile = new File(mDatabasePath, "UserTrackGoal.db");
            LogUtil.logi(mS);
            LogUtil.logi(outFile.getAbsolutePath());
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset UserTrackGoal.db ", e);
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void checkAppPermission() {
        ActivityCompat.requestPermissions(this,
                mPermissionList, REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                for (int result :
                        grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        ToastUtil.toast("部分权限受限，相关功能将受到影响");
                        break;
                    }
                }

            }
            ReLoginActivity.startReLoginActivity(this,500);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
