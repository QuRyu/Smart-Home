package android.intellhome.utils;

import android.widget.CheckBox;

import java.util.Map;

/**
 * Created by Quentin on 31/10/2016.
 */
public class CheckboxManager {

    // used for identifying checkbox
    public static final int CHECKBOX_NO_SELECTION = -1;
    public static final int CHECKBOX_CURRENT = 1;
    public static final int CHECKBOX_VOLTAGE = 2;
    public static final int CHECKBOX_ELECTRICITY = 3;


    private Map<Integer, ? extends CheckBox> checkboxes;

    private int currentChecked;

    public CheckboxManager(Map<Integer, ? extends CheckBox> checkboxes) {
        this.checkboxes = checkboxes;
        currentChecked = -1;
    }

    public void checkToggle(int i) {
        if (checkboxes.containsKey(i)) {
            if (currentChecked == i) {
                // toggle branch
                currentChecked = -1;
                checkboxes.get(i).setChecked(false);
            } else {
                setAllOff();
                checkboxes.get(i).setChecked(true);
                currentChecked = i;
            }
        } else
            throw new RuntimeException("key does not exist");

    }

    public int getCurrentChecked() {
        return currentChecked;
    }

    public boolean isChecked() {
        return currentChecked != CHECKBOX_NO_SELECTION;
    }

    public String getCurrentLabel() {
        switch (currentChecked) {
            case CHECKBOX_CURRENT:
                return "Current";
            case CHECKBOX_ELECTRICITY:
                return "Electricity";
            case CHECKBOX_VOLTAGE:
                return "Voltage";
            default:
                return "Default Label";
        }
    }

    private void setAllOff() {
        for (CheckBox cb : checkboxes.values())
            cb.setChecked(false);
    }
}
