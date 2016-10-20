package android.intellhome.entity;

import android.media.Image;

/**
 * Created by Quentin on 19/10/2016.
 */
public class DeviceHistoryData {

    public long id;
    public String device_sn;
    public String updatetime;
    public int device_state;
    public int device_U;
    public int device_I;
    public int device_P;
    public double device_electricity;
    public String remark;

    public DeviceHistoryData() {}
    //{"result":[{"id":29472,"device_sn":"0001","updatetime":"2016-10-07T07:30:55.000Z","device_state":1,"device_U":220,"device_I":1,"divice_P":0,"device_electricity":8.9,"remark":null}]}
    public DeviceHistoryData(long id, String sn, String updatetime, int state, int U, int I, int P, int electricity, String remakr) {
        this.id = id;
        this.device_sn = sn;
        this.updatetime = updatetime;
        this.device_state = state;
        this.device_U = U;
        this.device_I = I;
        this.device_P = P;
        this.device_electricity = electricity;
        this.remark = remakr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        DeviceHistoryData other = (DeviceHistoryData) o;
        return id == other.id && device_sn.equals(other.device_sn)
                && device_U == other.device_U && device_I == other.device_I
                && device_P == other.device_P && device_electricity == other.device_electricity
                && device_state == other.device_state;
    }

    @Override
    public String toString() {
        return "device id: " + id + "  U: " + device_U + "  I: " + device_I + "  P: " + device_P;
    }



}
