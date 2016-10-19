package android.intellhome;

import android.content.Intent;
import android.graphics.Color;
import android.intellhome.utils.RegExp;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeviceActivity extends AppCompatActivity {
    // mock, to be deleted later
    Random random = new Random();
    private static final int ITEM_COUNT = 12;



    // TODO: 18/10/2016 Http request
    // TODO: 18/10/2016 improve mChart
    LineChart mChart;

    Button mBt_search;
    List<Entry> entries = new ArrayList<>();

    EditText mET_startDate;
    EditText mET_endDate;

    String mMonths [] = {"Jan", "Feb", "Mar", "Apr",
            "May", "June", "July", "Aug", "Spe", "Oct", "Nov", "Dec"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_history);

        // to be deleted later
        ((Button) findViewById(R.id.bt_history)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DemoActivity.class));
            }
        });

        mBt_search = (Button) findViewById(R.id.bt_search);
        mET_startDate = (EditText) findViewById(R.id.et_startDate);
        mET_endDate = (EditText) findViewById(R.id.et_endDate);

        mChart = (LineChart) findViewById(R.id.linechart);

        mChart.setDragEnabled(false);
        mChart.setDescription("");

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

        LineData data = generateLineData();
        data.setValueTextColor(Color.BLUE);
        xAxis.setAxisMaxValue(data.getXMax() + 0.25f);
        mChart.setData(data);
        mChart.invalidate();
    }


    // mocking, to be deleted later
    private LineData generateLineData() {
        LineData data = new LineData();
        List<Entry> entries = new ArrayList<>();

        for (int i=0; i < ITEM_COUNT; i++)
            entries.add(new Entry(i + 0.5f, getRandom()));

        LineDataSet dataSet = new LineDataSet(entries, "Line DataSet");
        data.addDataSet(dataSet);
        return data;
    }

    private float getRandom() {
        return random.nextFloat() * ITEM_COUNT;
    }

    private void checkTextAndQuery() {
        String startDate = mET_startDate.getText().toString();
        String endDate = mET_endDate.getText().toString();

        if (checkText(startDate, endDate)) {
            // run the query
            queryHistory();
        }

    }

    private boolean checkText(String startDate, String endDate) {
        boolean isStartDateCorrect = RegExp.isExpFormatCorrect(startDate);
        boolean isEndDateCorect = RegExp.isExpFormatCorrect(endDate);

        View focusView = null;

        if (!isStartDateCorrect || !isEndDateCorect) { // at least the format of one EditText is wong
            // tell users which EditText is incorrect
            if (!isStartDateCorrect) {
                mET_startDate.setError(getString(R.string.field_required_startDate));
                focusView = mET_startDate;
            }
            if (!isEndDateCorect) {
                mET_endDate.setError(getString(R.string.filed_required_endDate));
                focusView = mET_endDate;
            }
            focusView.requestFocus();
        }

        return isStartDateCorrect && isEndDateCorect;
    }

    private void queryHistory() {}

    private void invalidateChart() {}

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkTextAndQuery();
        }
    };

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                checkTextAndQuery();
                handled = true;
            }
            return handled;
        }
    };

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
        }
    };
}
