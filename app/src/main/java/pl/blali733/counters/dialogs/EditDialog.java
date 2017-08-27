package pl.blali733.counters.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import pl.blali733.counters.R;

/**
 * Class responsible for creation and servicing edit window dialog.
 * @author blali733
 * @version 1.0
 * @since 0.3
 */
public class EditDialog extends Activity {

    private int id;
    private int v1;
    private int v2;
    private String mixed;
    private NumberPicker v1p;
    private NumberPicker v2p;

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
            id = extras.getInt("uuid");
            v1 = extras.getInt("v1");
            v2 = extras.getInt("v2");
            mixed = extras.getString("mixed");
        }else{
            Log.e(TAG,"No data passed to activity");
            finish();
        }

        View handle;
        if("true".equals(mixed)){
            handle = getLayoutInflater().inflate(R.layout.dual_mod,(ViewGroup)findViewById(R.id.mod_frame),true);
            v1p = handle.findViewById(R.id.v1Picker);
            v1p.setValue(v1);
            v2p = handle.findViewById(R.id.v2Picker);
            v2p.setValue(v2);
        }else{
            handle = getLayoutInflater().inflate(R.layout.single_mod,(ViewGroup)findViewById(R.id.mod_frame),true);
            v1p = handle.findViewById(R.id.numberPicker);
            v1p.setValue(v1);
        }

        Button button = findViewById(R.id.updateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                persist();
            }
        });
    }

    /**
     * Method responsible for saving changes to database(s)
     * @since 1.0
     */
    private void persist(){
        Toast.makeText(this,"Persisting changes (not really)!",Toast.LENGTH_LONG).show();
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
