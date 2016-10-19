package android.intellhome;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

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


    List<Entry> entries = new ArrayList<>();

    String mMonths [] = {"Jan", "Feb", "Mar", "Apr",
            "May", "June", "July", "Aug", "Spe", "Oct", "Nov", "Dec"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_activity);

        Intent intent = getIntent();


        mChart = (LineChart) findViewById(R.id.linechart);

        mChart.setDragEnabled(false);
        mChart.setDescription("");

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                
                mChart.highlightValue(h);
            }

            @Override
            public void onNothingSelected() {

            }
        });


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

}
