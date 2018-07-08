package club.peiyan.goaltrack.plan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import club.peiyan.goaltrack.R;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class TomorrowFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.tomorrow_layout, null);
        return mView;
    }
}
