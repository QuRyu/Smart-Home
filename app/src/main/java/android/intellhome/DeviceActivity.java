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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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

    private List<Entry> historyDataset;
    private int numOfDays;
    private int metric;
    private int start;

    private boolean isFirstSearch;

    // mock, to be deleted later
    Random random = new Random();
    private static final int ITEM_COUNT = 12;

    // TODO: 20/10/2016 adjust chart according to data 
    // TODO: 18/10/2016 improve mChart
    // TODO: 24/10/2016 Error Date checking
    LineChart mChart;

    Button mBt_search;
    List<Entry> entries = new ArrayList<>();

    EditText mStartDate;
    EditText mEndDate;

    static String format = "yy/mm/dd";
    static SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);

    int flag;

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
        mStartDate = (EditText) findViewById(R.id.tv_startDate);
        mStartDate.setOnClickListener(textViewOnClickListener);
        mEndDate = (EditText) findViewById(R.id.tv_endDate);
        mEndDate.setOnClickListener(textViewOnClickListener);

        mChart = (LineChart) findViewById(R.id.linechart);

        mChart.setDragEnabled(false);
        mChart.setDescription("");
        mChart.setEnabled(false);

        isFirstSearch = true;

    }


    // mocking, to be deleted later
    private LineData generateLineData() {
        LineData data = new LineData();
        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < ITEM_COUNT; i++)
            entries.add(new Entry(i + 0.5f, getRandom()));

        LineDataSet dataSet = new LineDataSet(entries, "Line DataSet");
        data.addDataSet(dataSet);
        return data;
    }

    private float getRandom() {
        return random.nextFloat() * ITEM_COUNT;
    }

    private void invalidateChart(List<DeviceHistoryData> historyData, int n, int metric, int start, int draw) {
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

        int y = start;

        switch (metric) {
            case DeviceHistoryController.METRIC_DAY:
                fillData(lineData, historyData, start, 30, draw);
                break;
            case DeviceHistoryController.METRIC_MONTH:
                fillData(lineData, historyData, start, 12, draw);
                break;
            case DeviceHistoryController.METRIC_YEAR:
                fillData(lineData, historyData, start, 2055, draw);
                break;
        }
        xAxis.setAxisMaxValue(lineData.getXMax() + 0.25f);
        mChart.setData(lineData);
        mChart.invalidate();
    }

    private void fillData(LineData lineData, List<DeviceHistoryData> historyData, int start, int max, int draw) {
        List<Entry> entries = new ArrayList<>();
        switch (draw) {
            case DRAW_ELECTRICITY:
                for (DeviceHistoryData data: historyData) {
                    entries.add(new Entry(double2Float(data.device_electricity), start++));
                    if (start > max) start = start - max;
                }
                break;
            case DRAW_CURRENT:
                for (DeviceHistoryData daat: historyData) {
                    entries.add(new Entry(double2Float(daat.device_I), start++));
                    if (start > max) start -= max;
                }
                break;
            case DRAW_U:
                for (DeviceHistoryData daat: historyData) {
                    entries.add(new Entry(double2Float(daat.device_U), start++));
                    if (start > max) start -= max;
                }
                break;
        }
        LineDataSet dataSet = new LineDataSet(entries, "data");
        lineData.addDataSet(dataSet);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, "query button clicked");
            isFirstSearch = false;
            controller.requestData(getStartDate(), getEndDate());
        }
    };

    private String getStartDate() {
        return null;
    }

    private String getEndDate() {
        return null;
    }

    private float double2Float(double value) {
        return new Double(value).floatValue();
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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DeviceHistoryController.REQUEST_FAILURE:
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    break;
                case DeviceHistoryController.REQUEST_SUCCESS:
                    Bundle bundle = msg.getData();
                    int metric = bundle.getInt(METRIC);
                    int daysOfDifference = bundle.getInt(DAYS_OF_DIFFERENCE);
                    int start = bundle.getInt(START);
                    invalidateChart((List<DeviceHistoryData>) msg.obj, daysOfDifference, metric, start, DRAW_ELECTRICITY);
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
