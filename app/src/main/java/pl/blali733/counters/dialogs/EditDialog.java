package pl.blali733.counters.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import pl.blali733.counters.R;
import pl.blali733.counters.exceptions.TransactionException;
import pl.blali733.counters.storage.DbStor;
import pl.blali733.counters.storage.data.LocalElement;

/**
 * Class responsible for creation and servicing edit window dialog.
 * @author blali733
 * @version 1.0
 * @since 0.3
 */
public class EditDialog extends Activity {

    private String uuid;
    private int v1;
    private int v2;
    private String mixed;
    private String label;
    private NumberPicker v1p;
    private NumberPicker v2p;

    private final int minVal = -99999;
    private final int maxVal = 99999;

    private static final String TAG = "EditDialog";
    /**
     * Method used to set up dialog window.
     * @param savedInstanceState Application state.
     * @since 1.0
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);
        this.setFinishOnTouchOutside(false);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            uuid = extras.getString("uuid");
            v1 = extras.getInt("v1");
            v2 = extras.getInt("v2");
            mixed = extras.getString("mixed");
            label = extras.getString("label");
        }else{
            Log.e(TAG,"No data passed to activity");
            finish();
        }

        TextView textView = findViewById(R.id.edit_label);
        textView.setText(label);

        View handle;
        if("true".equals(mixed)){
            handle = getLayoutInflater().inflate(R.layout.dual_mod,(ViewGroup)findViewById(R.id.mod_frame),true);
            v1p = handle.findViewById(R.id.v1Picker);
            v1p.setMinValue(0);
            v1p.setMaxValue(maxVal-minVal);
            v1p.setValue(v1-minVal);
            v1p.setWrapSelectorWheel(false);
            v1p.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int i) {
                    return Integer.toString(i+minVal);
                }
            });
            try {
                Method method = v1p.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
                method.setAccessible(true);
                method.invoke(v1p, true);
            } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            v2p = handle.findViewById(R.id.v2Picker);
            v2p.setMinValue(0);
            v2p.setMaxValue(maxVal-minVal);
            v2p.setValue(v2-minVal);
            v2p.setWrapSelectorWheel(false);
            v2p.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int i) {
                    return Integer.toString(i+minVal);
                }
            });
            try {
                Method method = v2p.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
                method.setAccessible(true);
                method.invoke(v2p, true);
            } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }else{
            handle = getLayoutInflater().inflate(R.layout.single_mod,(ViewGroup)findViewById(R.id.mod_frame),true);
            v1p = handle.findViewById(R.id.numberPicker);
            v1p.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int i) {
                    return Integer.toString(i+minVal);
                }
            });
            v1p.setMinValue(0);
            v1p.setMaxValue(maxVal-minVal);
            v1p.setValue(v1-minVal);
            try {
                Method method = v1p.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
                method.setAccessible(true);
                method.invoke(v1p, true);
            } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            v1p.setWrapSelectorWheel(false);
        }

        Button button = findViewById(R.id.updateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                persist();
            }
        });

        Button buttonDel = findViewById(R.id.deleteButton);
        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbStor db = new DbStor(getApplicationContext());
                LocalElement elem = db.getLocalElement(UUID.fromString(uuid));
                db.tagDeletedLocalElement(elem);
                db.close();
                finish();
            }
        });
    }

    /**
     * Method responsible for saving changes to database(s)
     * @since 1.0
     */
    private void persist(){
        //Toast.makeText(this,"Persisting changes (not really)!"+(v1p.getValue()+minVal),Toast.LENGTH_LONG).show();
        try {
            DbStor db = new DbStor(getApplicationContext());
            LocalElement elem = db.getLocalElement(UUID.fromString(uuid));
            int dv1, dv2;
            if ("true".equals(mixed)) {
                dv1 = v1p.getValue() + minVal - elem.getV1();
                dv2 = v2p.getValue() + minVal - elem.getV2();
                elem.setV1(v1p.getValue()+minVal);
                elem.setV2(v2p.getValue()+minVal);
                elem.setV1(v1p.getValue()+minVal);
                elem.setV2(v2p.getValue()+minVal);
            }else{
                dv1 = v1p.getValue() + minVal - elem.getV1();
                dv2 = 0;
                elem.setV1(v1p.getValue()+minVal);
                elem.setV2(0);
                elem.setV1(v1p.getValue()+minVal);
                elem.setV2(0);
            }
            db.updateLocalElement(elem,dv1,dv2);
            db.close();
            finish();
        }catch(TransactionException ex){
            Crashlytics.logException(ex);
            Toast.makeText(this,"Error while saving data! Please retry."+(v1p.getValue()+minVal),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method overriding action of back key into finishing activity.
     * @since 1.0
     */
    @Override
    public void onBackPressed(){
        finish();
    }
}
