package club.peiyan.goaltrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.peiyan.goaltrack.data.Constants;
import club.peiyan.goaltrack.netTask.FeedbackTask;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.DialogUtil;

/**
 * Created by HPY.
 * Time: 2018/7/16.
 * Desc:
 */

public class FeedBackActivity extends BaseActivity implements FeedbackTask.OnFeedbackListener {

    @BindView(R.id.etFeedback)
    EditText mEtFeedback;
    @BindView(R.id.tvUpload)
    TextView mTvUpload;
    @BindView(R.id.etEmail)
    EditText mEtEmail;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static void startFeedbackActivity(MainActivity mMainActivity) {
        Intent mIntent = new Intent(mMainActivity, FeedBackActivity.class);
        mMainActivity.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
    }

    @OnClick(R.id.tvUpload)
    public void onViewClicked() {
        final String mTrim = mEtFeedback.getText().toString().trim();
        final String mEmail = mEtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(mTrim)) {
            Toast.makeText(this, "写点东西吧", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogUtil.showSingleDialog(FeedBackActivity.this, "提交建议吗？", "希望您会喜欢GoalTrack，我也将继续优化提升体验和性能！",
                "继续编辑", "我要提交", new DialogUtil.DialogListener() {
                    @Override
                    public void onNegClickListener() {

                    }

                    @Override
                    public void onPosClickListener() {
                        FeedbackTask mTask = new FeedbackTask(FeedBackActivity.this);
                        mTask.setNote(mTrim);
                        mTask.setEmail(mEmail);
                        mTask.setUserName(AppSp.getString(Constants.USER_NAME, ""));
                        mTask.setRegisterListener(FeedBackActivity.this);
                        new Thread(mTask).start();
                        finish();
                    }
                });
    }

    @Override
    public void onFeedbackSuccess() {

    }

    @Override
    public void onFeedbackFail() {

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
