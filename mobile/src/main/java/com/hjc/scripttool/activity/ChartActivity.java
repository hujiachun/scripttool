package com.hjc.scripttool.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.hjc.scripttool.R;
import com.hjc.util.Constants;
import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by hujiachun on 16/2/23.
 */
public class ChartActivity extends AppCompatActivity {
    public static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        setContentView(R.layout.activity_line_chart);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * A fragment containing a line chart.
     */
    public static class PlaceholderFragment extends Fragment {
        public ArrayList<String> memStringList, cpuStringList;
        public ArrayList<Float> memFloatList, cpuFloatList;
        public int numberOfPoints;
        public float MAX_Y;

        private LineChartView chart;
        private LineChartData data;
        float[] randomNumbersTab;
        private boolean hasAxes = true;
        private boolean hasAxesNames = true;
        private boolean hasLines = true;
        private boolean hasPoints = true;
        private ValueShape shape = ValueShape.CIRCLE;
        private boolean isFilled = false;
        private boolean hasLabels = false;
        private boolean isCubic = false;
        private boolean hasLabelForSelected = false;
        private boolean pointsHaveDifferentColor;
        private ArrayList viewList;
        public static Intent intent;


        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
        }

        public PlaceholderFragment() {

        }

        /**
         * 得到内存数据
         *
         * @param intent
         */
        public void getMemData(Intent intent) {
            MAX_Y = Float.parseFloat(intent.getStringExtra(Constants.MAX_MEM));
            memStringList = intent.getStringArrayListExtra(Constants.MEM_LIST);
            memFloatList = new ArrayList<>();
            for (String data : memStringList) {//得到内存
                memFloatList.add(Float.parseFloat(data));
            }

            viewList = memFloatList;
            numberOfPoints = memStringList.size();
        }

        /**
         * 得到CPU数据
         *
         * @param intent
         */
        public void getCpuData(Intent intent) {
            MAX_Y = Float.parseFloat(intent.getStringExtra(Constants.MAX_CPU));
            cpuStringList = intent.getStringArrayListExtra(Constants.CPU_LIST);
            cpuFloatList = new ArrayList();
            for (String data : cpuStringList) {//得到cpu
                cpuFloatList.add(Float.parseFloat(data));
            }
            viewList = cpuFloatList;
            numberOfPoints = cpuStringList.size();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);
            chart = (LineChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());
            intent = ChartActivity.intent;
            getMemData(intent);
            generateValues();
            generateData(Constants.MEM);
            // Disable viewpirt recalculations, see toggleCubic() method for more info.
            chart.setViewportCalculationEnabled(false);

            resetViewport();

            return rootView;
        }

        // MENU
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.line_chart, menu);

        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.ram) {

                getMemData(intent);
//                        reset();
                generateValues();
                generateData(Constants.MEM);
                resetViewport();


                return true;
            }
            if (id == R.id.cpu) {

                cpuData();
                return true;
            }
            if (id == R.id.battery) {
                batteryData();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        /**
         * 造数据
         */
        private void generateValues() {
            randomNumbersTab = new float[numberOfPoints];
            for (int i = 0; i < numberOfPoints; ++i) {
                randomNumbersTab[i] = (float) viewList.get(i);
            }
        }

        private void reset() {
            hasAxes = true;
            hasAxesNames = true;
            hasLines = true;
            hasPoints = true;
            shape = ValueShape.CIRCLE;
            isFilled = false;
            hasLabels = false;
            isCubic = false;
            hasLabelForSelected = false;
            pointsHaveDifferentColor = false;

            chart.setValueSelectionEnabled(hasLabelForSelected);
            resetViewport();
        }


        /**
         * 设置X Y轴
         */
        private void resetViewport() {
            // Reset viewport height range to (0,100)
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = 0;
            v.top = MAX_Y;
            v.left = 0;
            v.right = numberOfPoints;
            chart.setMaximumViewport(v);
            chart.setCurrentViewport(v);
        }

        /**
         * 插入数据
         */
        private void generateData(String type) {
            List<Line> lines = new ArrayList<>();
            List<PointValue> values = new ArrayList<>();
            for (int i = 0; i < numberOfPoints; i++) {
                values.add(new PointValue(i, randomNumbersTab[i]));

            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[0]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            if (pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(0 + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);

            data = new LineChartData(lines);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName(Constants.INDEX);
                    axisY.setName(type);
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            data.setBaseValue(Float.NEGATIVE_INFINITY);
            chart.setLineChartData(data);


        }

        /**
         * Adds lines to data, after that data should be set again with
         * {@link LineChartView#setLineChartData(LineChartData)}. Last 4th line has non-monotonically x values.
         */
        private void cpuData() {
            getCpuData(intent);
            generateValues();
            generateData(Constants.CPU);
            resetViewport();
        }

        private void batteryData() {


        }


        /**
         * To animate values you have to change targets values and then call {@link Chart#startDataAnimation()}
         * method(don't confuse with View.animate()). If you operate on data that was set before you don't have to call
         * {@link LineChartView#setLineChartData(LineChartData)} again.
         */
        private void prepareDataAnimation() {
            for (Line line : data.getLines()) {
                for (PointValue value : line.getValues()) {
                    // Here I modify target only for Y values but it is OK to modify X targets as well.
                    value.setTarget(value.getX(), (float) Math.random() * 100);
                }
            }
        }

        private class ValueTouchListener implements LineChartOnValueSelectListener {

            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                Toast.makeText(getActivity(), "click: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }
    }

}
