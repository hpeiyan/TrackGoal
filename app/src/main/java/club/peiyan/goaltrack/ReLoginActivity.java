package club.peiyan.goaltrack;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.peiyan.goaltrack.data.Constants;
import club.peiyan.goaltrack.netTask.RegisterTask;
import club.peiyan.goaltrack.netTask.VerifyTask;
import club.peiyan.goaltrack.utils.AppSp;

/**
 * Created by HPY.
 * Time: 2018/7/12.
 * Desc:
 */

public class ReLoginActivity extends Activity implements RegisterTask.OnRegisterListener, VerifyTask.OnVerifyListener {

    @BindView(R.id.etName)
    EditText mEtName;
    @BindView(R.id.etPass)
    EditText mEtPass;
    @BindView(R.id.btnRegister)
    Button mBtnRegister;
    private String mName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        ButterKnife.bind(this);
        if (!AppSp.getString(Constants.USER_NAME, "").isEmpty()) {
            MainActivity.startMainActivity(ReLoginActivity.this, AppSp.getString(Constants.USER_NAME, ""), false);
        }
    }


    @OnClick({R.id.btnRegister, R.id.btnLogin})
    public void onViewClicked(View view) {
        mName = mEtName.getText().toString().trim();
        String mPass = mEtPass.getText().toString().trim();
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
        MainActivity.startMainActivity(ReLoginActivity.this, mName, true);
    }

    @Override
    public void onVerifyFail() {

    }

    @Override
    public void onRegisterSuccess() {
        MainActivity.startMainActivity(ReLoginActivity.this, mName, false);
    }

    @Override
    public void onRegisterFail() {

    }
}
