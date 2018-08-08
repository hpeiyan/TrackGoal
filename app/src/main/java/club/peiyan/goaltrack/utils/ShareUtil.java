package club.peiyan.goaltrack.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import club.peiyan.goaltrack.data.Constants;

/**
 * Created by HPY.
 * Time: 2018/8/4.
 * Desc:
 */

public class ShareUtil {

    private static final int THUMB_SIZE = 150;




    public static void shareToWeChat(Context mContext, String path) {
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID);

        int mTargetScene = SendMessageToWX.Req.WXSceneSession;
//        int  mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
//        int mTargetScene = SendMessageToWX.Req.WXSceneFavorite;

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = WeChatUtil.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
