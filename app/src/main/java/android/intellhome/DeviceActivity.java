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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Locale;

public class DeviceActivity extends AppCompatActivity {

    public static final String TAG = "DeviceActivity";

    private static final int FLAG_START_DATE = 1;
    private static final int FLAG_END_DATE = 2;


    public static final int HANDLER_WHAT_REQUEST_SUCCESS = 1;
    public static final int HANDLER_WHAT_REQUEST_FAILURE = 2;
    private static final int HANDLER_WHAT_UPDATE_LABEL = 3;


    public static final String METRIC = "metric";
    public static final String DAYS_OF_DIFFERENCE = "daysOfDifference";
    public static final String START = "start";

    static final int DRAW_ELECTRICITY = 1;
    static final int DRAW_CURRENT = 2;
    static final int DRAW_U = 3;

    private List<DeviceHistoryData> historyDataset;
    private int numOfDays;
    private int metric;
    private int start;

    // for branching
    private boolean isFirstSearch;
    private int flag;
    private int currentChecked = -1;

    // mock, to be deleted later
    private static final int ITEM_COUNT = 12;

    // TODO: 20/10/2016 adjust chart according to data 
    // TODO: 18/10/2016 improve mChart
    // TODO: 24/10/2016 Error Date checking
    LineChart mChart;

    Button mBt_search;
    List<Entry> entries = new ArrayList<>();

    EditText mStartDate;
    EditText mEndDate;

    CheckBox mCB_Electricity;
    CheckBox mCB_U;
    CheckBox mCB_I;

    static String format = "yy/mm/dd";
    static SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);



    String mMonths[] = {"Jan", "Feb", "Mar", "Apr",
            "May", "June", "July", "Aug", "Spe", "Oct", "Nov", "Dec"};

    DeviceHistoryController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_history);

        Log.i(TAG, "onCreate method init");

        controller = new DeviceHistoryController(mHandler);

        mBt_search = (Button) findViewById(R.id.bt_search);
        mBt_search.setOnClickListener(buttonOnClickListener);


        mStartDate = (EditText) findViewById(R.id.tv_startDate);
        mStartDate.setOnClickListener(textViewOnClickListener);
        mEndDate = (EditText) findViewById(R.id.tv_endDate);
        mEndDate.setOnClickListener(textViewOnClickListener);

        mCB_Electricity = (CheckBox) findViewById(R.id.cb_electricity);
        mCB_Electricity.setOnClickListener(checkboxOnClickListener);
        mCB_U = (CheckBox) findViewById(R.id.cb_U);
        mCB_U.setOnClickListener(checkboxOnClickListener);
        mCB_I = (CheckBox) findViewById(R.id.cb_current);
        mCB_I.setOnClickListener(checkboxOnClickListener);


        mChart = (LineChart) findViewById(R.id.linechart);

        mChart.setDragEnabled(false);
        mChart.setDescription("");
        mChart.setEnabled(false);

        isFirstSearch = true;


    }

    private void invalidateChart(List<DeviceHistoryData> historyData, int n, int metric, int start) {
        historyDataset = historyData;
        numOfDays = n;
        this.metric = metric;
        this.start = start;


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinValue(0f);
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mMonths[(int) value % mMonths.length];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        LineData lineData = new LineData();
        lineData.setValueTextColor(Color.BLUE);

        switch (metric) {
            case DeviceHistoryController.METRIC_DAY:
                fillData(lineData, historyData, start, 30);
                break;
            case DeviceHistoryController.METRIC_MONTH:
                fillData(lineData, historyData, start, 12);
                break;
            case DeviceHistoryController.METRIC_YEAR:
                fillData(lineData, historyData, start, 2055);
                break;
        }

        xAxis.setAxisMaxValue(lineData.getXMax() + 0.25f);
        mChart.setData(lineData);
        mChart.invalidate();
        Log.i(TAG, "finish rendering chart");
    }

    private void fillData(LineData lineData, List<DeviceHistoryData> historyData, int start, int max) {
        Log.i(TAG, "start to fill chart with data");
        int x = start;
        List<Entry> entries = new ArrayList<>();
        switch (currentChecked) {
            case DRAW_ELECTRICITY:
                for (DeviceHistoryData data: historyData) {
                    entries.add(new Entry(x++, double2Float(data.device_electricity)));
                    if (x > max) x = x - max;
                }
                break;
            case DRAW_CURRENT:
                for (DeviceHistoryData daat: historyData) {
                    entries.add(new Entry(x++, double2Float(daat.device_I)));
                    if (x > max) x -= max;
                }
                break;
            case DRAW_U:
                for (DeviceHistoryData daat: historyData) {
                    entries.add(new Entry(x++, double2Float(daat.device_U)));
                    if (x > max) x -= max;
                }
                break;
        }
        LineDataSet dataSet = new LineDataSet(entries, "data");
        lineData.addDataSet(dataSet);
    }

    private String getStartDate() {
        return null;
    }

    private String getEndDate() {
        return null;
    }

    private float double2Float(double value) {
        return new Double(value).floatValue();
    }

    private View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, "query button clicked");
            try {
                controller.requestData(getStartDate(), getEndDate());
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(DeviceHistoryController.REQUEST_FAILURE);
            }
            if (isFirstSearch && currentChecked == -1) {
                isFirstSearch = false;
                currentChecked = DRAW_ELECTRICITY;
                mCB_Electricity.setChecked(true);
            } else if (isFirstSearch) {
                isFirstSearch = false;
            }
        }
    };

    private void setAllCheckboxOff() {
        mCB_I.setChecked(false);
        mCB_U.setChecked(false);
        mCB_Electricity.setChecked(false);
    }

    private void drawIfNotForTheFirstTime() {
        if (!isFirstSearch)
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
                    flag = FLAG_START_DATE;
                    break;
                case R.id.tv_endDate:
                    flag = FLAG_END_DATE;
                    break;
                default:
                    throw new IllegalArgumentException("cannot match view");
            }
            GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
            new DatePickerDialog(DeviceActivity.this,
                    dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    private View.OnClickListener checkboxOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cb_current:
                    if (currentChecked != DRAW_CURRENT) {
                        setAllCheckboxOff();
                        mCB_I.setChecked(true);
                        currentChecked = DRAW_CURRENT;
                        drawIfNotForTheFirstTime();
                    }
                    break;
                case R.id.cb_electricity:
                    if (currentChecked != DRAW_ELECTRICITY) {
                        setAllCheckboxOff();
                        mCB_Electricity.setChecked(true);
                        currentChecked = DRAW_ELECTRICITY;
                        drawIfNotForTheFirstTime();
                    }
                    break;
                case R.id.cb_U:
                    if (currentChecked != DRAW_U) {
                        setAllCheckboxOff();
                        mCB_U.setChecked(true);
                        currentChecked = DRAW_U;
                        drawIfNotForTheFirstTime();
                    }
                    break;
            }
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DeviceHistoryController.REQUEST_FAILURE:
                    Log.i(TAG, "request failure, ask the user to reconnect");
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    break;
                case DeviceHistoryController.REQUEST_SUCCESS:
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
                    switch (flag) {
                        case FLAG_START_DATE:
                            mStartDate.setText(text);
                            break;
                        case FLAG_END_DATE:
                            mEndDate.setText(text);
                            break;
                        default:
                            throw new IllegalArgumentException("wrong flag");
                    }
                    break;
            }

        }
    };

}
