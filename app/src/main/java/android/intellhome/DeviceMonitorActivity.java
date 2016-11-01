package android.intellhome;

import android.content.Intent;
import android.intellhome.utils.CheckboxManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.zcw.togglebutton.ToggleButton;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 * Created by Quentin on 31/10/2016.
 */
public class DeviceMonitorActivity extends AppCompatActivity {

    static final String TAG = "DeviceMonitorActivity";

    // used for identifying checkbox
    static final int CHECKBOX_NO_SELECTION = -1;
    static final int CHECKBOX_CURRENT = 1;
    static final int CHECKBOX_VOLTAGE = 2;
    static final int CHECKBOX_ELECTRICITY = 3;

    static final int maxItemsToShow = 10;

    private boolean toggleOn;
    private boolean drawingChart;

    ChartThread drawingThread;

    LineChart mChart;

    Button mBT_history;
    ToggleButton mBT_switch;

    TextView mTV_component_name;
    TextView mTV_component_id;

    CheckBox mCB_Current;

    CheckBox mCB_Voltage;
    CheckBox mCB_Electricity;

    CheckboxManager mCheckboxManager;
    DeviceMonitorController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_monitor);


        // initialize controller
        controller = new DeviceMonitorController();

        // initialize chart
        mChart = (LineChart) findViewById(R.id.linechart);

        // initialize buttons
        mBT_history = (Button) findViewById(R.id.bt_history);
        mBT_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeviceHistoryActivity.class));
            }
        });

        mBT_switch = (ToggleButton) findViewById(R.id.bt_switch);

        mBT_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleOn = !toggleOn;
                mBT_switch.toggle();
                if (toggleOn)
                    Toast.makeText(getApplicationContext(), "the switch is now on",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "the switch is now off",
                            Toast.LENGTH_SHORT).show();

                if (toggleOn)
                    startDrawChart();
                else if (drawingChart)// the switch is turned off
                    stopDrawChart();
            }
        });

        // initialize textView
        mTV_component_id = (TextView) findViewById(R.id.tv_component_id);
        mTV_component_name = (TextView) findViewById(R.id.tv_component_name);

        mTV_component_name.setText("卧室智能开关");
        mTV_component_id.setText("IP:192");


        // initialize checkbox and checkbox manager
        mCB_Current = (CheckBox) findViewById(R.id.cb_current);
        mCB_Current.setOnClickListener(checkboxListener);
        mCB_Electricity = (CheckBox) findViewById(R.id.cb_electricity);
        mCB_Electricity.setOnClickListener(checkboxListener);
        mCB_Voltage = (CheckBox) findViewById(R.id.cb_U);
        mCB_Voltage.setOnClickListener(checkboxListener);

        Map<Integer, CheckBox> map = new HashMap<>();
        map.put(CHECKBOX_CURRENT, mCB_Current);
        map.put(CHECKBOX_VOLTAGE, mCB_Voltage);
        map.put(CHECKBOX_ELECTRICITY, mCB_Electricity);

        mCheckboxManager = new CheckboxManager(map);
    }

    private void startDrawChart() {
        if (toggleOn && mCheckboxManager.getCurrentChecked() != CHECKBOX_NO_SELECTION) {
            Log.i(TAG, "startDrawChart: start to draw chart");
            drawingChart = true;

//            drawingThread = new ChartThread(mChart, )
            switch (mCheckboxManager.getCurrentChecked()) {
                case CHECKBOX_CURRENT:
                    drawingThread = new ChartThread(mChart, 5, maxItemsToShow, "CURRENT");
                    break;
                case CHECKBOX_ELECTRICITY:
                    drawingThread = new ChartThread(mChart, 100, maxItemsToShow, "ELECTRICITY");
                    break;
                case CHECKBOX_VOLTAGE:
                    drawingThread = new ChartThread(mChart, 225, maxItemsToShow, "VOLTAGE");
                    break;
            }

            drawingThread.start();
        }
    }

    private void stopDrawChart() {
        Log.i(TAG, "stopDrawChart: stop drawing chart");
        drawingChart = false;
        
        drawingThread.interrupt();
    }



    private View.OnClickListener checkboxListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cb_current:
                    mCheckboxManager.checkToggle(CHECKBOX_CURRENT);
                    break;
                case R.id.cb_electricity:
                    mCheckboxManager.checkToggle(CHECKBOX_ELECTRICITY);
                    break;
                case R.id.cb_U:
                    mCheckboxManager.checkToggle(CHECKBOX_VOLTAGE);
                    break;
            }
            if (mCheckboxManager.getCurrentChecked() != CHECKBOX_NO_SELECTION)
                startDrawChart();
            else if (drawingChart)// all checkboxes are unselected
                stopDrawChart();
        }
    };


    // TODO: 01/11/2016 use queue to add new elements
    private class ChartThread extends Thread {
        private LineChart mChart;
        private int range;
        private int maxItems; // max items to show
        private String label; // for legendj

        private Random random;
        private LinkedList<Entry> queue;

        private boolean run = true;


        public ChartThread(LineChart mChart, int range, int maxItems, String label) {
            this.mChart = mChart;
            this.range = range;
            this.maxItems = maxItems;
            this.label = label;

            random = new Random();
            queue = new LinkedList<>();

        }

        @Override
        public synchronized void start() {
            super.start();


        }

        @Override
        public void run() {
            super.run();
            Log.i(TAG, "start: drawing thread running");
            // initialize lineData and lineDataSet
            LineData lineData = mChart.getLineData();
            LineDataSet lineDataSet = null;
            if (lineData == null) {
                lineData = new LineData();
                lineDataSet = new LineDataSet(queue, label);
                lineData.addDataSet(lineDataSet);
            }
            else {
                lineDataSet = (LineDataSet) lineData.getDataSetByIndex(0);
                if (lineDataSet == null) {
                    lineDataSet = new LineDataSet(queue, label);
                    lineData.addDataSet(lineDataSet);
                }
            }


            while (run) {
                int y = random.nextInt(range);
                add(new Entry(0, y));
                mChart.notifyDataSetChanged();
                mChart.invalidate();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();
            run = false;
        }

        private void add(Entry e) {
            allEntriesDecrement();
            if (queue.size() < maxItems) {
                queue.add(e);
            }
            else {
                queue.add(e);
                queue.remove();
            }

        }



        private void allEntriesDecrement() {
            for (Entry entry: queue) {
                entry.setX(entry.getX() - 1);
            }
        }

    }

}
