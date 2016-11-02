package android.intellhome.utils;

import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;
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
    private boolean [] checked;


    public CheckboxManager(Map<Integer, ? extends CheckBox> checkboxes) {
        this.checkboxes = checkboxes;
        checked = new boolean[checkboxes.size()];

        // initialize checked
        for (int i=0; i<checked.length; i++)
            checked[i] = false;
    }

    public void checkToggle(int i) {
        if (checkboxes.containsKey(i)) {
            if (checked[i]) { // the checkbox was checked, now un-check it
                checkboxes.get(i).setChecked(false);
                checked[i] = false;
            } else {
                checked[i] = true;
                checkboxes.get(i).setChecked(true);
            }
        } else
            throw new RuntimeException("key does not exist");

    }

    public boolean isChecked() {
        for (boolean b: checked)
            if (b)
                return true;
        return false;
    }

    public int [] getChecked () {
        if (!isChecked())
            return null;

        int [] result = new int[getCheckedNum()];
        int index = 0;
        for (int i=0; i<checked.length; ++i)
            if (checked[i])
                result[index++] = i;
        return result;
    }

    public int getCheckedNum() {
        int result = 0;
        for (boolean b: checked)
            ++result;
        return result;
    }
}
