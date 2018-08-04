package club.peiyan.goaltrack.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.data.ScoreBean;
import club.peiyan.goaltrack.data.ScoreList;
import club.peiyan.goaltrack.utils.ListUtil;
import club.peiyan.goaltrack.utils.ShareUtil;
import club.peiyan.goaltrack.utils.ToastUtil;

/**
 * Created by HPY.
 * Time: 2018/7/16.
 * Desc:
 */

public class ScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<ScoreList> mPastScoreList;
    private final Activity mActivity;
    private Typeface mTf;

    public ScoreAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setData(ArrayList<ScoreList> mScoreList) {
        mPastScoreList = mScoreList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder;
        if (viewType == 0) {
            View mInflate = View.inflate(parent.getContext(), R.layout.score_header_layout, null);
            mViewHolder = new BarViewHolder(mInflate);
        } else {
            View mInflate = View.inflate(parent.getContext(), R.layout.score_item_layout, null);
            mViewHolder = new ViewHolder(mInflate);
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            BarViewHolder mHolder = (BarViewHolder) holder;
            initBarChart(mHolder.barChart);
            String mShow = String.format("最近%d天用时", mPastScoreList.size());
            mHolder.tvShow.setText(mShow);
            mHolder.tvShow.setOnClickListener(v -> {
                String mDateTime = String.valueOf(new Date(System.currentTimeMillis()).getTime());
                String mFileName = ("GoalTrack_" + mDateTime + ".jpg").replace(" ", "");
                boolean isSuccess = mHolder.barChart.saveToGallery(mFileName, 100);
                if (isSuccess) {
                    File extBaseDir = Environment.getExternalStorageDirectory();
                    File file = new File(extBaseDir.getAbsolutePath() + "/DCIM");
                    String filePath = file.getAbsolutePath() + "/" + mFileName;
                    String mContent = mShow + "一览表——GoalTrack";
                    ShareUtil.shareImg(mActivity, mContent, "", "Hi there，我在用Goal Track，\n这是我的" + mContent, Uri.parse(filePath));
                }
            });
        } else {
            ViewHolder mHolder = (ViewHolder) holder;
            ScoreList mScoreBeans = mPastScoreList.get(position - 1);
            initPieChart(mHolder.pieChart, mScoreBeans);
            mHolder.ivShare.setOnClickListener(v -> {
                String mDateTime = new Date(System.currentTimeMillis()).toString();
                String mFileName = "GoalTrack_" + mDateTime + ".jpg";
                boolean isSuccess = mHolder.pieChart.saveToGallery(mFileName, 100);
                if (isSuccess) {
                    File extBaseDir = Environment.getExternalStorageDirectory();
                    File file = new File(extBaseDir.getAbsolutePath() + "/DCIM");
                    String filePath = file.getAbsolutePath() + "/" + mFileName;
                    String mDate = mScoreBeans.getScoreBeans().get(0).getDate();
                    String dateShow = "项目一览表——GoalTrack";
                    if (mDate.length() > 0) {
                        String[] mStrings = mDate.split("/");
                        if (mStrings.length == 3) {
                            dateShow = String.format("%s年%s月%s日", mStrings[0], mStrings[1], mStrings[2]) + "项目——GoalTrack";
                        }
                    }
                    ShareUtil.shareImg(mActivity, dateShow, "", "Hi there，我在用Goal Track，\n这是我的" + dateShow, Uri.parse(filePath));
                }
            });
        }
    }

    private void initBarChart(BarChart mChart) {
        Typeface mTfRegular = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Regular.ttf");
        Typeface mTfLight = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Light.ttf");
        Description mDescription = new Description();
        mDescription.setText("   分钟");
        mChart.getDescription().setEnabled(true);
        mDescription.setTextAlign(Paint.Align.LEFT);
        mChart.setDescription(mDescription);
        mChart.setMaxVisibleValueCount(mPastScoreList.size());

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        mChart.getAxisLeft().setDrawGridLines(false);

        // add a nice and smooth animation
        mChart.animateY(2000);

        mChart.getLegend().setEnabled(false);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        if (!ListUtil.isEmpty(mPastScoreList)) {
            for (ScoreList scoreList : mPastScoreList) {
                if (scoreList != null && !ListUtil.isEmpty(scoreList.getScoreBeans())) {
                    long totalScore = 0;
                    for (ScoreBean bean : scoreList.getScoreBeans()) {
                        totalScore += bean.getScore();
                    }
                    scoreList.setTotalScore(totalScore / (1000 * 60));//分钟为单位
                }
            }

            for (int i = 0; i < mPastScoreList.size(); i++) {
                yVals1.add(new BarEntry(i + 1, mPastScoreList.get(i).getTotalScore()));
            }
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            mChart.setData(data);
            mChart.setFitBars(true);
        }

        mChart.invalidate();
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;

                int mIndex = (int) e.getX();
                if (mIndex > 0) {
                    String mDate = mPastScoreList.get(mIndex - 1).getScoreBeans().get(0).getDate();
                    if (mDate.length() > 0) {
                        String[] mStrings = mDate.split("/");
                        if (mStrings.length == 3) {
                            ToastUtil.toast(String.format("%s年%s月%s日", mStrings[0], mStrings[1], mStrings[2]));
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void initPieChart(PieChart mChart, ScoreList mScoreBeans) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        if (mScoreBeans != null && !ListUtil.isEmpty(mScoreBeans.getScoreBeans())) {
            for (ScoreBean bean : mScoreBeans.getScoreBeans()) {
                entries.add(new PieEntry(bean.getScore(), bean.getTitle()));
            }
        }

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mTf = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Light.ttf"));
        String mDate = mScoreBeans.getScoreBeans().get(0).getDate();
        if (mDate.length() > 0) {
            String[] mStrings = mDate.split("/");
            if (mStrings.length == 3) {
                mChart.setCenterText(String.format("%s年%s月%s日", mStrings[0], mStrings[1], mStrings[2]));
            }
        }

        mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//        mChart.setOnChartValueSelectedListener();

        setData(entries, mChart);

        mChart.animateY(1400);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }

    private void setData(ArrayList<PieEntry> entries, PieChart mChart) {

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setUsingSliceColorAsValueLineColor(true);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @Override
    public int getItemCount() {
        return mPastScoreList.size() + 1;//add header
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;//header
        } else {
            return 1;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pieChart)
        PieChart pieChart;
        @BindView(R.id.ivShare)
        ImageView ivShare;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class BarViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.barChart)
        BarChart barChart;
        @BindView(R.id.tvShow)
        TextView tvShow;

        BarViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
