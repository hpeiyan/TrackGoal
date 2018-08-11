package club.peiyan.goaltrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.peiyan.goaltrack.data.Constants;
import club.peiyan.goaltrack.netTask.RegisterTask;
import club.peiyan.goaltrack.netTask.VerifyTask;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.ThreadUtil;
import club.peiyan.goaltrack.utils.ViewUtil;

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
    @BindView(R.id.btnVisitor)
    TextView mBtnVisitor;
    @BindView(R.id.rlLogin)
    RelativeLayout mRlLogin;
    private String mName;

    public static void startReLoginActivity(Activity mActivity, int delay) {
        Intent mIntent = new Intent(mActivity, ReLoginActivity.class);
        ThreadUtil.uiPostDelay(() -> {
            mActivity.startActivity(mIntent);
            mActivity.finish();
        }, delay);
    }

    public static void startReLoginActivityForce(Activity mActivity) {
        Intent mIntent = new Intent(mActivity, ReLoginActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mIntent.putExtra(Constants.FORCE_RELOGIN, true);
        mActivity.startActivity(mIntent);
        mActivity.finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        boolean isForceLogin = getIntent().getBooleanExtra(Constants.FORCE_RELOGIN, false);
        if (!isForceLogin && !AppSp.getString(Constants.USER_NAME, "").isEmpty()) {
            MainActivity.startMainActivity(ReLoginActivity.this, AppSp.getString(Constants.USER_NAME, ""), false);
        }
    }


    @OnClick({R.id.btnRegister, R.id.btnLogin, R.id.btnVisitor})
    public void onViewClicked(View view) {
        mName = mEtName.getText().toString().trim();
        String mPass = mEtPass.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btnRegister:
                if (mName.isEmpty() || mPass.isEmpty()) {
                    Toast.makeText(this, "信息缺失", Toast.LENGTH_SHORT).show();
                    return;
                }
                RegisterTask mTask = new RegisterTask(ReLoginActivity.this);
                mTask.setRegisterListener(this);
                mTask.setUserName(mName);
                mTask.setPassword(mPass);
                new Thread(mTask).start();
                AppSp.putBoolean(Constants.IS_REGISTER, true);
                ViewUtil.setVisible(mRlLogin);
                break;
            case R.id.btnLogin:
                if (mName.isEmpty() || mPass.isEmpty()) {
                    Toast.makeText(this, "信息缺失", Toast.LENGTH_SHORT).show();
                    return;
                }
                VerifyTask mVerifyTask = new VerifyTask(ReLoginActivity.this);
                mVerifyTask.setOnVerifyListener(this);
                mVerifyTask.setUserName(mName);
                mVerifyTask.setPassword(mPass);
                new Thread(mVerifyTask).start();
                AppSp.putBoolean(Constants.IS_REGISTER, true);
                ViewUtil.setVisible(mRlLogin);
                break;
            case R.id.btnVisitor:
                MobclickAgent.onProfileSignIn("Visitor");
                MainActivity.startMainActivity(ReLoginActivity.this, "Goal Track", false);
                AppSp.putBoolean(Constants.IS_REGISTER, false);
                break;
        }
    }

    @Override
    public void onVerifySuccess() {
        ViewUtil.setGone(mRlLogin);
        MobclickAgent.onProfileSignIn(mName);
        MainActivity.startMainActivity(ReLoginActivity.this, mName, true);
    }

    @Override
    public void onVerifyFail() {
        ViewUtil.setGone(mRlLogin);
    }

    @Override
    public void onRegisterSuccess() {
        ViewUtil.setGone(mRlLogin);
        MobclickAgent.onProfileSignIn(mName);
//        //登出
//        MobclickAgent.onProfileSignOff();
        MainActivity.startMainActivity(ReLoginActivity.this, mName, false);
    }

    @Override
    public void onRegisterFail() {
        ViewUtil.setGone(mRlLogin);
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
