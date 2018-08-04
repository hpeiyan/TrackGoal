package club.peiyan.goaltrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.peiyan.goaltrack.data.Constants;
import club.peiyan.goaltrack.netTask.RegisterTask;
import club.peiyan.goaltrack.netTask.VerifyTask;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.UIThread;

/**
 * Created by HPY.
 * Time: 2018/7/12.
 * Desc:
 */

public class ReLoginActivity extends BaseActivity implements RegisterTask.OnRegisterListener, VerifyTask.OnVerifyListener {

    @BindView(R.id.etName)
    EditText mEtName;
    @BindView(R.id.etPass)
    EditText mEtPass;
    @BindView(R.id.btnRegister)
    Button mBtnRegister;
    private String mName;

    public static void startReLoginActivity(LoadingActivity mActivity) {
        Intent mIntent = new Intent(mActivity, ReLoginActivity.class);
        UIThread.postDelay(() -> {
            mActivity.startActivity(mIntent);
            if (!mActivity.isDestroyed()) {
                mActivity.finish();
            }
        }, 500);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        if (!AppSp.getString(Constants.USER_NAME, "").isEmpty()) {
            MainActivity.startMainActivity(ReLoginActivity.this, AppSp.getString(Constants.USER_NAME, ""), false);
        }
    }


    @OnClick({R.id.btnRegister, R.id.btnLogin})
    public void onViewClicked(View view) {
        mName = mEtName.getText().toString().trim();
        String mPass = mEtPass.getText().toString().trim();
        if (mName.isEmpty() || mPass.isEmpty()) {
            Toast.makeText(this, "信息缺失", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()) {
            case R.id.btnRegister:
                RegisterTask mTask = new RegisterTask(ReLoginActivity.this);
                mTask.setRegisterListener(this);
                mTask.setUserName(mName);
                mTask.setPassword(mPass);
                new Thread(mTask).start();
                break;
            case R.id.btnLogin:
                VerifyTask mVerifyTask = new VerifyTask(ReLoginActivity.this);
                mVerifyTask.setOnVerifyListener(this);
                mVerifyTask.setUserName(mName);
                mVerifyTask.setPassword(mPass);
                new Thread(mVerifyTask).start();
                break;
        }
    }

    @Override
    public void onVerifySuccess() {
        MobclickAgent.onProfileSignIn(mName);
        MainActivity.startMainActivity(ReLoginActivity.this, mName, true);
    }

    @Override
    public void onVerifyFail() {

    }

    @Override
    public void onRegisterSuccess() {
        MobclickAgent.onProfileSignIn(mName);
//        //登出
//        MobclickAgent.onProfileSignOff();
        MainActivity.startMainActivity(ReLoginActivity.this, mName, false);
    }

    @Override
    public void onRegisterFail() {

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
