package android.intellhome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DeviceActivity extends AppCompatActivity {
    // TODO: 18/10/2016 Http request
    // TODO: 18/10/2016  
    LineChart chart;

    List<Entry> entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_activity);

        Intent intent = getIntent();


        chart = (LineChart) findViewById(R.id.linechart);
        entries.add(new Entry(1.0f, 2.0f));
        entries.add(new Entry(2.0f, 3.0f));
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        chart.setData(new LineData(dataSet));
        chart.invalidate();


    }
}
