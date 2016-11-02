package android.intellhome;

import android.os.Handler;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.util.LinkedList;

/**
 * Created by Quentin on 31/10/2016.
 */
public class DeviceMonitorController {

    static final String TAG = "DeviceMonitorController";

    private Handler mHandler;
    private DeviceMonitorActivity.Draw drawer;

    private QueueManager dataManager;

    public DeviceMonitorController(Handler handler, DeviceMonitorActivity.Draw draw) {
        this.mHandler = handler;
        this.drawer = draw;
    }

    // called when switch is turned off
    public void switchTurnOff() {

    }

    // called when switch is turned on
    public void switchTurnOn() {

    }

    public void startDrawing() {
        dataManager = new QueueManager(GlobalConfig.MAX_ITEMS_TO_SHOW);

    }

    public void stopDrawing() {

        dataManager = null;
    }

    private class QueueManager {
        private LinkedList<Entry> entries;
        private int maxItems;

        public QueueManager(int maxItems) {
            this.maxItems = maxItems;

            entries = new LinkedList<>();
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
                entry.setX(entry.getX()-1);
            }
        }
    }



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
            // initialize lineData and lineDataSet

            while (run) {
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
}
