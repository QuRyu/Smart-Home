package android.intellhome.services;

import android.content.pm.FeatureInfo;
import android.intellhome.entity.DeviceHistoryData;
import android.intellhome.entity.Result;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Quentin on 19/10/2016.
 */
public class RequestService {

    //http://192.168.1.135:3000/tools/gethisData/getHis?_hserverSN=0000000000000107&begtime=20161007104353&endtime=20161008004753

    public static final String BASE_URL = "http://192.168.1.135:3000/";


    public static DeviceHistoryData getHisData(String serverSN, String startDate, String endDate) {

        return null;
    }

    public static String makeHisURL(String severSN, String startDate, String endDate) {
        StringBuffer buffer = new StringBuffer(BASE_URL);
        buffer.append("tools/gethisData/getHis?_hserverSN=");
        buffer.append(severSN);
        buffer.append("&begtime=");
        buffer.append(startDate);
        buffer.append("&endtime=");
        buffer.append(endDate);
        return buffer.toString();
    }
}
