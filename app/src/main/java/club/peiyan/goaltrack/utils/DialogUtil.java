package club.peiyan.goaltrack.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import club.peiyan.goaltrack.plan.OnEditListener;

/**
 * Created by HPY.
 * Time: 2018/7/6.
 * Desc:
 */

public class DialogUtil {

    public static void showDialog(Context mContext, final OnEditListener mOnEditListener) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        AlertDialog mAlertDialog = mBuilder.setMessage("你真的确定退出编辑吗？")
                .setNegativeButton("继续编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("我要退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mOnEditListener.onExit();
                    }
                }).create();

        mAlertDialog.show();
    }

    public static void showSingleDialog(Context mContext, String title, String message, String negBtn,
                                        String posBtn, final DialogListener mListener) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        if (title != null && !title.isEmpty()) {
            mBuilder.setTitle(title);
        }
        AlertDialog mAlertDialog = mBuilder.setMessage(message)
                .setNegativeButton(negBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onNegClickListener();
                        dialog.dismiss();
                    }
                }).setPositiveButton(posBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onPosClickListener();
                        dialog.dismiss();
                    }
                }).create();
        mAlertDialog.show();
    }

    public interface DialogListener {
        void onNegClickListener();

        void onPosClickListener();
    }

}
