package club.peiyan.goaltrack;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.umeng.analytics.MobclickAgent;

import club.peiyan.goaltrack.utils.ToastUtil;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

/**
 * Created by HPY.
 * Time: 2018/7/12.
 * Desc:
 */

public class LoadingActivity extends BaseActivity {

    private static final int REQUEST_CODE = 1028;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        getSupportActionBar().hide();
        checkAppPermission();
    }

    private void checkAppPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR);
        if (permissionCheck == PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            ReLoginActivity.startReLoginActivity(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ToastUtil.toast("无法获得权限，部分功能将受到影响");
                }
            }
            ReLoginActivity.startReLoginActivity(this);
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
