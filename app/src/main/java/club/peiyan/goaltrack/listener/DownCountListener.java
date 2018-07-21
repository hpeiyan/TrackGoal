package club.peiyan.goaltrack.listener;

/**
 * Created by HPY.
 * Time: 2018/7/21.
 * Desc:
 */

public interface DownCountListener {
    void onTick(long count,long countOrigin);
    void onFinish(boolean isFinish);
}
