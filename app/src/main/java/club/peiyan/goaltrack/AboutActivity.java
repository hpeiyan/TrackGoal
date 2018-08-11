package club.peiyan.goaltrack;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.utils.ToastUtil;

/**
 * Created by HPY.
 * Time: 2018/7/16.
 * Desc:
 */

public class AboutActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.llLogo)
    LinearLayout mLlLogo;
    @BindView(R.id.tvVersion)
    TextView mTvVersion;
    @BindView(R.id.lineView)
    View mLineView;
    @BindView(R.id.tvMessage)
    TextView mTvMessage;

    private static final String message = "Hi,there!\n" +
            "\n" +
            "目标不清晰？半途而废？Excel？\n" +
            "\n" +
            "您的烦恼，Goal Track为您分忧。\n" +
            "\n" +
            "\t1.分而击之：\n\t\tGoal Track的目标结构：父目标-子目标-任务，配合时间管理模块，将目标细分。\n" +
            "\n\t2.不忘初心：\n\t\tGoal Track的番茄工作功能、定时提醒功能，协助您达成目标。\n" +
            "\n\t3.自知者明：\n\t\tGoal Track的报表功能，提供达成效果和目标进度反馈。\n" +
            "\n" +
            "邀请码：\n\t鉴于公测阶段，个人服务器负载有限，第一版将持邀请码方可登录，敬请谅解。\n" +
            "\n" +
            "Goal Track是个人非盈利作品，您的反馈和支持是给作者最大的鼓励，作者也将持续维护该项目。\n" +
            "\n" +
            "Goal Track, Tracking our Goals!";
    @BindView(R.id.tvPay)
    ImageView mTvPay;

    public static void startAboutActivity(MainActivity mMainActivity) {
        Intent mIntent = new Intent(mMainActivity, AboutActivity.class);
        mMainActivity.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().hide();
        initView();
    }

    private void initView() {
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            mTvVersion.setText("当前版本：" + info.versionName);
        } catch (PackageManager.NameNotFoundException mE) {
            mE.printStackTrace();
        }
        mTvMessage.setText(message);
        mTvPay.setOnLongClickListener(v -> {
            Vibrator vib = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
//            long[] pattern = {1000,2000};
            //两个参数，一个是自定义震动模式，
            //数组中数字的含义依次是静止的时长，震动时长，静止时长，震动时长。。。时长的单位是毫秒
            //第二个是“是否反复震动”,-1 不重复震动
            //第二个参数必须小于pattern的长度，不然会抛ArrayIndexOutOfBoundsException
//            vib.vibrate(pattern, 1);
            if (saveToGallery("pay.jpg", "", "", Bitmap.CompressFormat.PNG, 100)) {
                ToastUtil.toast("已保存本地，去微信打赏");
                vib.vibrate(200);//只震动一秒，一次
            }
            return false;
        });
    }

    /**
     * Saves the current state of the chart to the gallery as an image type. The
     * compression must be set for JPEG only. 0 == maximum compression, 100 = low
     * compression (high quality). NOTE: Needs permission WRITE_EXTERNAL_STORAGE
     *
     * @param fileName        e.g. "my_image"
     * @param subFolderPath   e.g. "ChartPics"
     * @param fileDescription e.g. "Chart details"
     * @param format          e.g. Bitmap.CompressFormat.PNG
     * @param quality         e.g. 50, min = 0, max = 100
     * @return returns true if saving was successful, false if not
     */
    public boolean saveToGallery(String fileName, String subFolderPath, String fileDescription, Bitmap.CompressFormat
            format, int quality) {
        // restrain quality
        if (quality < 0 || quality > 100)
            quality = 50;

        long currentTime = System.currentTimeMillis();

        File extBaseDir = Environment.getExternalStorageDirectory();
        File file = new File(extBaseDir.getAbsolutePath() + "/DCIM/" + subFolderPath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return false;
            }
        }

        String mimeType = "";
        switch (format) {
            case PNG:
                mimeType = "image/png";
                if (!fileName.endsWith(".png"))
                    fileName += ".png";
                break;
            case WEBP:
                mimeType = "image/webp";
                if (!fileName.endsWith(".webp"))
                    fileName += ".webp";
                break;
            case JPEG:
            default:
                mimeType = "image/jpeg";
                if (!(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")))
                    fileName += ".jpg";
                break;
        }

        String filePath = file.getAbsolutePath() + "/" + fileName;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);

            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.we_chat_pay);
            b.compress(format, quality, out);

            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        long size = new File(filePath).length();

        ContentValues values = new ContentValues(8);

        // store the details
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.DATE_ADDED, currentTime);
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        values.put(MediaStore.Images.Media.DESCRIPTION, fileDescription);
        values.put(MediaStore.Images.Media.ORIENTATION, 0);
        values.put(MediaStore.Images.Media.DATA, filePath);
        values.put(MediaStore.Images.Media.SIZE, size);

        return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) != null;
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
