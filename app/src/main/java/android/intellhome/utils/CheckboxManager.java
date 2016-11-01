package android.intellhome.utils;

import android.widget.CheckBox;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Quentin on 31/10/2016.
 */
public class CheckboxManager {

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
            }
            else {
                setAllOff();
                checkboxes.get(i).setChecked(true);
                currentChecked = i;
            }
        }
        else
            throw new RuntimeException("key does not exist");

    }

    public int getCurrentChecked() {
        return currentChecked;
    }

    private void setAllOff() {
        for (CheckBox cb: checkboxes.values())
            cb.setChecked(false);
    }
}
