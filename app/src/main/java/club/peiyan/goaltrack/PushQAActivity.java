package club.peiyan.goaltrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HPY.
 * Time: 2018/7/16.
 * Desc:
 */

public class PushQAActivity extends BaseActivity {


    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static void startPushActivity(MainActivity mMainActivity) {
        Intent mIntent = new Intent(mMainActivity, PushQAActivity.class);
        mMainActivity.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_qa_layout);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mWebview.loadUrl("file:///android_asset/push.html");
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
