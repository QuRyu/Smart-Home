package android.intellhome.utils;

import android.content.res.Resources;
import android.graphics.Color;
import android.intellhome.R;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.DESedeKeySpec;

/**
 * Created by Quentin on 31/10/2016.
 */
public class CheckboxManager {

    // used for identifying checkbox
    public static final int CHECKBOX_NO_SELECTION = -1;
    public static final int CHECKBOX_CURRENT = 0;
    public static final int CHECKBOX_VOLTAGE = 1;
    public static final int CHECKBOX_ELECTRICITY = 2;
    public static final int CHECKBOX_UNCHECKED = 3;


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
                checked[i] = false;
                checkboxes.get(i).setChecked(false);
                checkboxes.get(i).setTextColor(getCorrespondingColor(CHECKBOX_UNCHECKED));
            } else { // render the checkbox checked
                checked[i] = true;
                checkboxes.get(i).setChecked(true);
                checkboxes.get(i).setTextColor(getCorrespondingColor(i));
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
            if (b) ++result;
        return result;
    }

    public String getCorrespondingLabel(int i) {
        switch (i) {
            case CHECKBOX_CURRENT:
                return "Current";
            case CHECKBOX_ELECTRICITY:
                return "Electricity";
            case CHECKBOX_VOLTAGE:
                return "Voltage";
            default:
                return "Default";
        }
    }

    public int getCorrespondingColor(int i) {
        switch (i) {
            case CHECKBOX_CURRENT:
                return Color.parseColor("#90CAF9");
            case CHECKBOX_ELECTRICITY:
                return Color.parseColor("#FF5722");
            case CHECKBOX_VOLTAGE:
                return Color.parseColor("#7E57C2");
            case CHECKBOX_UNCHECKED:
                return Color.parseColor("#94181616");
            default:
                throw new Resources.NotFoundException("no corresponding color found");
        }
    }

    public int findDifferenceAndReturn(int [] oldChecked) {
        boolean [] prvChecked = new boolean[checkboxes.size()];
        for (int i : oldChecked) {
            prvChecked[i] = true;
        }
        for (int i = 0; i < checkboxes.size(); i++) {
            if (prvChecked[i] != checked[i])
                return i;
        }
        throw new Resources.NotFoundException("the checkbox has not changed");
    }
}
