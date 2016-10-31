package android.intellhome;

import android.content.Intent;
import android.intellhome.utils.CheckboxManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zcw.togglebutton.ToggleButton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Quentin on 31/10/2016.
 */
public class DeviceMonitorActivity extends AppCompatActivity {

    static final int CHECKBOX_CURRENT = 1;
    static final int CHECKBOX_VOLTAGE = 2;
    static final int CHECKBOX_ELECTRICITY = 3;

    private boolean toggleOn;

    Button mBT_history;
    ToggleButton mBT_switch;

    TextView mTV_component_name;
    TextView mTV_component_id;

    CheckboxManager mCheckboxManager;

    CheckBox mCB_Current;
    CheckBox mCB_Voltage;
    CheckBox mCB_Electricity;

    DeviceMonitorController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_monitor);

        controller = new DeviceMonitorController();

        mBT_history = (Button) findViewById(R.id.bt_history);
        mBT_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeviceHistoryActivity.class));
            }
        });

        mBT_switch = (ToggleButton) findViewById(R.id.bt_switch);

        mBT_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleOn = !toggleOn;
                mBT_switch.toggle();
            }
        });

        mTV_component_id = (TextView) findViewById(R.id.tv_component_id);
        mTV_component_name = (TextView) findViewById(R.id.tv_component_name);

        mTV_component_name.setText("卧室智能开关");
        mTV_component_id.setText("IP:192");

        mCB_Current = (CheckBox) findViewById(R.id.cb_current);
        mCB_Electricity = (CheckBox) findViewById(R.id.cb_electricity);
        mCB_Voltage = (CheckBox) findViewById(R.id.cb_U);

        Map<Integer, CheckBox> map = new HashMap<>();
        map.put(CHECKBOX_CURRENT, mCB_Current);
        map.put(CHECKBOX_VOLTAGE, mCB_Voltage);
        map.put(CHECKBOX_ELECTRICITY, mCB_Electricity);

        mCheckboxManager = new CheckboxManager(map);
    }

    
}
