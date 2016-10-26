package android.intellhome.entity;

import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

/**
 * Wrapper class for parsing JSON data. 
 */
public final class Result {

    public List<DeviceHistoryData> result;

    public Result() { }
    public Result(List<DeviceHistoryData> result) {
        this.result = result;
    }
}
