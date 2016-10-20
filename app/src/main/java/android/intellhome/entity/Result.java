package android.intellhome.entity;

import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

/**
 * Created by Quentin on 20/10/2016.
 */
public class Result {

    public List<DeviceHistoryData> result;

    public Result() { }
    public Result(List<DeviceHistoryData> result) {
        this.result = result;
    }
}
