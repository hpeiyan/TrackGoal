package club.peiyan.goaltrack.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;

/**
 * Created by HPY.
 * Time: 2018/8/4.
 * Desc:
 */

public class ShareUtil {
    /**
     * 分享图片和文字内容
     *
     * @param dlgTitle 分享对话框标题
     * @param subject  主题
     * @param content  分享内容（文字）
     * @param uri      图片资源URI
     */
    public static void shareImg(Activity mActivity,String dlgTitle, String subject, String content,
                                Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        if (subject != null && !"".equals(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (content != null && !"".equals(content)) {
            intent.putExtra(Intent.EXTRA_TEXT, content);
        }

        // 设置弹出框标题
        if (dlgTitle != null && !"".equals(dlgTitle)) { // 自定义标题
            mActivity.startActivity(Intent.createChooser(intent, dlgTitle));
        } else { // 系统默认标题
            mActivity.startActivity(intent);
        }
    }

    public static String insertImageToSystem(Context context, String imagePath,String picName) {
        String url = "";
        try {
            url = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, picName, "All Right Goal Track Reserved");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return url;
    }
}
