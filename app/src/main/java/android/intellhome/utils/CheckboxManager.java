package android.intellhome.utils;

import android.widget.CheckBox;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Quentin on 31/10/2016.
 */
public class CheckboxManager {

    private Map<Integer, ? extends CheckBox> checkboxes;

    public CheckboxManager(Map<Integer, ? extends CheckBox> checkboxes) {
        this.checkboxes = checkboxes;
    }

    public void setChecked(int i) {
        setAllOff();
        if (checkboxes.containsKey(i))
            checkboxes.get(i).setChecked(true);
        else
            throw new RuntimeException("key does not exist");
    }

    private void setAllOff() {
        Iterator it = checkboxes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, ? extends CheckBox> pair = (Map.Entry<Integer, ? extends CheckBox>) it.next();
            pair.getValue().setChecked(false);
        }
    }
}
