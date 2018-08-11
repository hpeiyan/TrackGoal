package club.peiyan.goaltrack.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import club.peiyan.goaltrack.R;

/**
 * Created by HPY.
 * Time: 2018/8/11.
 * Desc:
 */

public class AboutView {

    private final View mView;
    private AlertDialog mAlertDialog;

    public AboutView(Activity mActivity) {
        mView = mActivity.getLayoutInflater().inflate(R.layout.layout_about, null);
        TextView mTvMessage = mView.findViewById(R.id.tvMessage);
        String message = "Hi,there!\n" +
                "\n" +
                "目标不清晰？半途而废？Excel？\n" +
                "\n" +
                "您的烦恼，Goal Track为您分忧。\n" +
                "\n" +
                "\t分而击之：\n\t\tGoal Track的目标结构：父目标-子目标-任务，配合时间管理模块，将目标细分。\n" +
                "\t不忘初心：\n\t\tGoal Track的番茄工作功能、定时提醒功能，协助您达成目标。\n" +
                "\t自知者明：\n\t\tGoal Track的报表功能，提供达成效果和目标进度反馈。\n" +
                "\n" +
                "邀请码：\n\t鉴于公测阶段，个人服务器负载有限，第一版将持邀请码方可登录，敬请谅解。\n" +
                "\n" +
                "Goal Track是个人非盈利作品，您的反馈和支持是给作者最大的鼓励，作者也将持续维护该项目。\n" +
                "\n" +
                "Goal Track, Tracking our Goals!";
        mTvMessage.setText(message);
    }

    public View getView() {
        return mView;
    }

    public void setAlertDialog(AlertDialog mAlertDialog) {
        this.mAlertDialog = mAlertDialog;
    }
}
