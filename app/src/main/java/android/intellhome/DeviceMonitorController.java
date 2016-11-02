package android.intellhome;

import android.intellhome.utils.CheckboxManager;
import android.os.Handler;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Quentin on 31/10/2016.
 */
public class DeviceMonitorController {

    static final String TAG = "DeviceMonitorController";

    private CheckboxManager checkboxManager;
    private LineData lineData;
    private Handler mHandler;

    private Draw drawer;
    private QueueManager dataManager;

    private RequestCurrentData<Integer> request;
    private LineDataSet lineDataSet;

    private ChartThread drawingThread;

    public DeviceMonitorController(Handler handler, Draw draw, CheckboxManager checkboxManager, LineData lineData) {
        this.checkboxManager = checkboxManager;
        this.lineData = lineData;
        this.mHandler = handler;
        this.drawer = draw;

        request = new RequestService(10);
    }

    // called when switch is turned off
    public void switchTurnOff() {

    }

    // called when switch is turned on
    public void switchTurnOn() {

    }

    public void startDrawing() {
        // first initialize data
        dataManager = new QueueManager(GlobalConfig.MAX_ITEMS_TO_SHOW);
        lineData.addDataSet(lineDataSet);

        // start to request data from server using the thread, one piece at every second
        drawingThread = new ChartThread();
        drawingThread.start();
    }

    public void stopDrawing() {

        dataManager = null;
        drawingThread.interrupt();
        drawingThread = null;
    }

    private class QueueManager {
        private LinkedList<Entry> entries;
        private int maxItems;

        public QueueManager(int maxItems) {
            this.maxItems = maxItems;

            entries = new LinkedList<>();
        }

        public List<Entry> getEntries() {
            return entries;
        }

        public void add(Entry entry) {
            if (entries.size() < maxItems)
                entries.add(entry);
            else {
                entries.remove();
                entriesDecrementByOne();
                entries.add(entry);
            }
        }

        private void entriesDecrementByOne() {
            for (Entry entry : entries) {
                entry.setX(entry.getX() - 1);
            }
        }


    }

    // now adds one pieces of data each second by random
    // later request
    private class ChartThread extends Thread {

        private boolean run = true;

        public ChartThread() {
        }

        @Override
        public synchronized void start() {
            super.start();
        }

        @Override
        public void run() {
            super.run();
            Log.i(TAG, "start: drawing thread running");

            while (run) {
                int y = request.requestData();
                dataManager.add(new Entry(GlobalConfig.MAX_ITEMS_TO_SHOW, y));
                LineDataSet dataSet = new LineDataSet(dataManager.getEntries(), "test");
                lineData.removeDataSet(0);
                lineData.addDataSet(dataSet);
                mHandler.sendEmptyMessage(DeviceMonitorActivity.HANDLER_UPDATE_CHART);

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

    }

    // for mocking
    private interface RequestCurrentData<T> {
        T requestData();
    }

    // for mocking also
    private class RequestService implements RequestCurrentData<Integer> {

        private int range;

        private Random random;

        RequestService(int range) {
            this.range = range;

            random = new Random();
        }


        @Override
        public Integer requestData() {
            return random.nextInt(range);
        }

        public void setRange(int range) {
            this.range = range;
        }
    }
}
