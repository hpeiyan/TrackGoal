package club.peiyan.goaltrack.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void setData(ArrayList<Fragment> mFragments) {
        this.mFragments = mFragments;
    }
}
