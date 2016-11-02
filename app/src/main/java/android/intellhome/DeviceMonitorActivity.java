package android.intellhome;

import android.content.Intent;
import android.intellhome.utils.CheckboxManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

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
public class DeviceMonitorActivity extends AppCompatActivity implements DeviceMonitorActivity.Draw{

    public static final int HANDLER_UPDATE_CHART = 1;

    static final String TAG = "DeviceMonitorActivity";

    private boolean toggleOn;
    private boolean drawingChart;

    // self-defined helper class
    ChartThread drawingThread;
    CheckboxManager mCheckboxManager;
    DeviceMonitorController controller;


    Random random;

    // UI component
    Button mBT_history;
    ToggleButton mBT_switch;

    TextView mTV_component_name;
    TextView mTV_component_id;

    CheckBox mCB_Current;

    CheckBox mCB_Voltage;
    CheckBox mCB_Electricity;


    // chart
    LineData lineData;
    LineDataSet lineDataSet;
    LineChart mChart;
    LinkedList<Entry> entries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_monitor);

        random = new Random();
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
        map.put(CheckboxManager.CHECKBOX_CURRENT, mCB_Current);
        map.put(CheckboxManager.CHECKBOX_VOLTAGE, mCB_Voltage);
        map.put(CheckboxManager.CHECKBOX_ELECTRICITY, mCB_Electricity);

        mCheckboxManager = new CheckboxManager(map);
    }

    @Override
    public void startDrawChart() {
        if (toggleOn && mCheckboxManager.getCurrentChecked() != CheckboxManager.CHECKBOX_NO_SELECTION) {
            Log.i(TAG, "startDrawChart: start to draw chart");
            drawingChart = true;

            entries = new LinkedList<>();
            drawingThread = new ChartThread();

            drawingThread.start();
        }
    }

    @Override
    public void stopDrawChart() {
        Log.i(TAG, "stopDrawChart: stop drawing chart");
        drawingChart = false;

        drawingThread.interrupt();
        drawingThread = null;

        entries = null;

        lineData.clearValues();
        mChart.clear();
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    private String getCheckboxLabel() {
        switch (mCheckboxManager.getCurrentChecked()) {
            case CheckboxManager.CHECKBOX_CURRENT:
                return getString(R.string.current);
            case CheckboxManager.CHECKBOX_ELECTRICITY:
                return getString(R.string.electricity);
            case CheckboxManager.CHECKBOX_VOLTAGE:
                return getString(R.string.voltage);
            default:
                return "LABEL";
        }
    }

    private View.OnClickListener checkboxListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cb_current:
                    mCheckboxManager.checkToggle(CheckboxManager.CHECKBOX_CURRENT);
                    break;
                case R.id.cb_electricity:
                    mCheckboxManager.checkToggle(CheckboxManager.CHECKBOX_ELECTRICITY);
                    break;
                case R.id.cb_U:
                    mCheckboxManager.checkToggle(CheckboxManager.CHECKBOX_VOLTAGE);
                    break;
            }
            if (mCheckboxManager.getCurrentChecked() !=
                    CheckboxManager.CHECKBOX_NO_SELECTION && !drawingChart) {
                startDrawChart();
            }
            else if (drawingChart)// all checkboxes are unselected
                stopDrawChart();
            else if (mCheckboxManager.getCurrentChecked() !=
                    CheckboxManager.CHECKBOX_NO_SELECTION && drawingChart) {

                stopDrawChart();
                startDrawChart();
            }

        }
    };


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_UPDATE_CHART:
                    lineData = mChart.getLineData();
                    if (lineData == null) {
                        lineData = new LineData();
                        mChart.setData(lineData);
                    }

                    int y = random.nextInt(10);
                    addNewEntry(y);
                    LineDataSet lineDataSet = new LineDataSet(entries, getCheckboxLabel());
                    lineData.removeDataSet(0);
                    lineData.addDataSet(lineDataSet);
                    mChart.notifyDataSetChanged();
                    mChart.invalidate();
                    break;
            }
        }
    };

    public interface Draw {
        void startDrawChart();
        void stopDrawChart();
    }
}
