package android.intellhome;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.intellhome.entity.DeviceHistoryData;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DeviceHistoryActivity extends AppCompatActivity {

    public static final String TAG = "DeviceHistoryActivity";

    private static final int FLAG_START_DATE = 1;
    private static final int FLAG_END_DATE = 2;


    public static final int HANDLER_WHAT_REQUEST_SUCCESS = 1;
    public static final int HANDLER_WHAT_REQUEST_FAILURE = 2;
    private static final int HANDLER_WHAT_UPDATE_LABEL = 3;


    public static final String METRIC = "metric";
    public static final String DAYS_OF_DIFFERENCE = "daysOfDifference";
    public static final String START = "start";

    private List<DeviceHistoryData> historyDataset;
    private int numOfDays;
    private int metric;
    private int start;

    // for branching
    private boolean isFirstSearch;
    private boolean isCheckboxSelected;
    private int flag_date;

    CombinedChart mChart;

    Button mBT_search;

    EditText mET_StartDate;
    EditText mET_EndDate;

    CheckBox mCB_Electricity;
    CheckBox mCB_Voltage;
    CheckBox mCB_Current;

    DeviceHistoryHistoryController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_history);

        Log.i(TAG, "onCreate method init");

        controller = new DeviceHistoryHistoryController(mHandler);

        mBT_search = (Button) findViewById(R.id.bt_search);
        mBT_search.setOnClickListener(buttonOnClickListener);


        mET_StartDate = (EditText) findViewById(R.id.tv_startDate);
        mET_StartDate.setOnClickListener(textViewOnClickListener);
        mET_EndDate = (EditText) findViewById(R.id.tv_endDate);
        mET_EndDate.setOnClickListener(textViewOnClickListener);

        mCB_Electricity = (CheckBox) findViewById(R.id.cb_electricity);
        mCB_Electricity.setOnClickListener(checkboxOnClickListener);
        mCB_Voltage = (CheckBox) findViewById(R.id.cb_U);
        mCB_Voltage.setOnClickListener(checkboxOnClickListener);
        mCB_Current = (CheckBox) findViewById(R.id.cb_current);
        mCB_Current.setOnClickListener(checkboxOnClickListener);


        mChart = (CombinedChart) findViewById(R.id.linechart);

        mChart.setDragEnabled(false);
        mChart.setDescription("");
        mChart.setEnabled(false);

        isFirstSearch = true;
        isCheckboxSelected = false;

    }

    // every time any checkbox is selected or query is enacted
    // draw the chart
    private void invalidateChart(List<DeviceHistoryData> historyData, int n, int metric, int start) {
        // determine the checkboxes that are selected
        historyDataset = historyData;
        numOfDays = n;
        this.metric = metric;
        this.start = start;


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinValue(start);


        CombinedData combinedData = new CombinedData();
        combinedData.setValueTextColor(Color.BLUE);

        switch (metric) {
            case DeviceHistoryHistoryController.METRIC_DAY:
                // TODO: 31/10/2016 get the maximum day of the month of start date
                fillData(combinedData, historyData, start, 30);
//                xAxis.setValueFormatter(new AxisValueFormatter() {
//                    @Override
//                    public String getFormattedValue(float value, AxisBase axis) {
//                        return String.valueOf(value % 30);
//                    }
//
//                    @Override
//                    public int getDecimalDigits() {
//                        return 0;
//                    }
//                });
                break;
            case DeviceHistoryHistoryController.METRIC_MONTH:
                fillData(combinedData, historyData, start, 12);
                xAxis.setValueFormatter(new AxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return String.valueOf(value % 12);
                    }

                    @Override
                    public int getDecimalDigits() {
                        return 0;
                    }
                });
                break;
            case DeviceHistoryHistoryController.METRIC_YEAR:
                fillData(combinedData, historyData, start, 2055);
                xAxis.setValueFormatter(new AxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return String.valueOf(value);
                    }

                    @Override
                    public int getDecimalDigits() {
                        return 0;
                    }
                });
                break;
        }

        xAxis.setAxisMaxValue(historyData.size() + start + 0.25f);
        mChart.setData(combinedData);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
        Log.i(TAG, "finish rendering chart");
    }

    private void fillData(CombinedData combinedData, List<DeviceHistoryData> historyData, int start, int max) {
        Log.i(TAG, "start to fill chart with data");

        // TODO: 31/10/2016 if circle is drawn, there could only be one line
        if (mCB_Electricity.isChecked())
            combinedData.setData(fillElectricityData(historyData, start, max));
        if (mCB_Current.isChecked())
            combinedData.setData(fillCurrentData(historyData, start, max));
        if (mCB_Voltage.isChecked())
            combinedData.setData(fillVoltageData(historyData, start, max));

    }

    private LineData fillCurrentData(final List<DeviceHistoryData> historyDatas, final int start, int max) {
        Log.i(TAG, "fillCurrentData: current chart is selected, rendering");
        int x = start;
        ArrayList<Entry> entries = new ArrayList<>();

        for (DeviceHistoryData data: historyDatas) {
            entries.add(new Entry(x++, data.device_I));
            x = x % max;
        }

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.current));
        dataSet.setColor(getColor(R.color.current));
        dataSet.setDrawCircles(false);
//        dataSet.setCircleColor(getColor(R.color.current));
        dataSet.setFillFormatter(new FillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return start + historyDatas.size();
            }
        });

        LineData lineData = new LineData();
        lineData.addDataSet(dataSet);
        return lineData;
    }

    private BarData fillElectricityData(final List<DeviceHistoryData> historyDatas, final int start, int max) {
        Log.i(TAG, "fillElectricityData: electricity chart is selected, rendering");
        int x = start;
        List<BarEntry> entries = new ArrayList<>();

        for (DeviceHistoryData data: historyDatas) {
            entries.add(new BarEntry(x++, double2Float(data.device_electricity)));
        }

//        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.electricity));
//        dataSet.setColor(getColor(R.color.electricity));
//        dataSet.setCircleColor(getColor(R.color.electricity));
//        dataSet.setFillFormatter(new FillFormatter() {
//            @Override
//            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                return start + historyDatas.size();
//            }
//        });
//
//        LineData lineData = new LineData(dataSet);
//        return lineData;

        BarDataSet dataSet = new BarDataSet(entries, getString(R.string.electricity));
        dataSet.setColor(getColor(R.color.electricity));
        BarData barData = new BarData();
        barData.addDataSet(dataSet);
        return barData;
    }

    private LineData fillVoltageData(final List<DeviceHistoryData> historyDatas, final int start, int max) {
        Log.i(TAG, "fillVoltageData: voltage chart selected, rendering");
        int x = start;
        ArrayList<Entry> entries = new ArrayList<>();

        for (DeviceHistoryData data: historyDatas) {
            entries.add(new Entry(x++, data.device_U));
            x = x % max;
        }

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.voltage));
        dataSet.setColor(getColor(R.color.voltage));
        dataSet.setDrawCircles(false);
//        dataSet.setCircleColor(getColor(R.color.voltage));
        dataSet.setFillFormatter(new FillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return start + historyDatas.size();
            }
        });

        LineData lineData = new LineData();
        lineData.addDataSet(dataSet);
        return lineData;
    }

    private String getStartDate() {
        return null;
    }

    private String getEndDate() {
        return null;
    }

    private float double2Float(double value) {
        return Double.valueOf(value).floatValue();
    }

    private View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, "query mBT_switch clicked");
            try {
                controller.requestData(getStartDate(), getEndDate());
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(DeviceHistoryHistoryController.REQUEST_FAILURE);
            }
            if (isFirstSearch && !isCheckboxSelected) {
                isFirstSearch = false;
                mCB_Electricity.setChecked(true);
                Log.i(TAG, "onClick: isFirstSearch set to false");
            } else if (isFirstSearch) {
                isFirstSearch = false;
                Log.i(TAG, "onClick: isFirstSearch set to false");
            }
        }
    };

    // each time a checkbox is selected, this method is called to render chart
    private void drawChartAtResponseToCheckbox() {
        if (isFirstSearch) {
            return;
        }
        invalidateChart(historyDataset, numOfDays, metric, start);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Log.i(TAG, "date selected");
            GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            Message message = mHandler.obtainMessage();
            message.what = HANDLER_WHAT_UPDATE_LABEL;
            message.obj = calendar;
            message.sendToTarget();
        }
    };

    private View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, "EditText click listener activated");
            switch (v.getId()) {
                case R.id.tv_startDate:
                    flag_date = FLAG_START_DATE;
                    break;
                case R.id.tv_endDate:
                    flag_date = FLAG_END_DATE;
                    break;
                default:
                    throw new IllegalArgumentException("cannot match view");
            }
            GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
            new DatePickerDialog(DeviceHistoryActivity.this,
                    dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    private View.OnClickListener checkboxOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cb_current:
                    if (mCB_Current.isChecked() && mCB_Voltage.isChecked())
                        mCB_Voltage.setChecked(false);
                    isCheckboxSelected = true;
                    drawChartAtResponseToCheckbox();
                    break;
                case R.id.cb_electricity:
                    isCheckboxSelected = true;
                    drawChartAtResponseToCheckbox();
                    break;
                case R.id.cb_U:
                    if (mCB_Current.isChecked() && mCB_Voltage.isChecked())
                        mCB_Current.setChecked(false);
                    isCheckboxSelected = true;
                    drawChartAtResponseToCheckbox();
                    break;
            }
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DeviceHistoryHistoryController.REQUEST_FAILURE:
                    Log.i(TAG, "request failure, ask the user to reconnect");
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    break;
                case DeviceHistoryHistoryController.REQUEST_SUCCESS:
                    Log.i(TAG, "request success, start to render data");
                    Bundle bundle = msg.getData();
                    int metric = bundle.getInt(METRIC);
                    int daysOfDifference = bundle.getInt(DAYS_OF_DIFFERENCE);
                    int start = bundle.getInt(START);
                    Log.i(TAG, "metric:" + metric + ", days of difference:" + daysOfDifference + ", start date:" + start);
                    invalidateChart((List<DeviceHistoryData>) msg.obj, daysOfDifference, metric, start);
                    break;
                case HANDLER_WHAT_UPDATE_LABEL:
                    Log.i(TAG, "handler ==> update label");
                    Calendar calendar = (Calendar) msg.obj;
                    String text = calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH);
                    switch (flag_date) {
                        case FLAG_START_DATE:
                            mET_StartDate.setText(text);
                            break;
                        case FLAG_END_DATE:
                            mET_EndDate.setText(text);
                            break;
                        default:
                            throw new IllegalArgumentException("wrong flag_date");
                    }
                    break;
            }

        }
    };

}
