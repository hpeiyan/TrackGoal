package club.peiyan.goaltrack.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class MyRecycleView extends RecyclerView {

    private boolean isEditMode;

    public MyRecycleView(Context context) {
        super(context);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        if (e.getAction() != MotionEvent.ACTION_MOVE && isEditMode) {
//            return true;
//        }
        return super.onTouchEvent(e);

    }

    public void setEditMode(boolean mEditMode) {
        isEditMode = mEditMode;
    }

}
