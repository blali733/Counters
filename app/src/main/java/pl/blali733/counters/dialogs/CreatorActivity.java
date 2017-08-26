package pl.blali733.counters.dialogs;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import pl.blali733.counters.R;
import pl.blali733.counters.storage.DbStor;
import pl.blali733.counters.storage.data.LocalElement;

/**
 * Activity responsible for creation of new counter.
 * @author blali733
 * @version 1.1
 * @since 0.3
 */
public class CreatorActivity extends Activity {

    //DOCME fields
    private EditText label;
    private Switch twoVal;
    private TextView prevLabel;
    private TextView prevVal;

    /**
     * Method populating counter creation screen.
     * @param savedInstanceState State of application.
     * @since 1.0
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);
        this.setFinishOnTouchOutside(false);

        label = findViewById(R.id.ilabel);
        twoVal = findViewById(R.id.mVal);
        Button confirm = findViewById(R.id.createButton);
        prevLabel = findViewById(R.id.item_label);
        prevVal = findViewById(R.id.item_value);

        prevLabel.setText(label.getText());
        prevVal.setText("0");

        twoVal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    prevVal.setText("0/0");
                }else{
                    prevVal.setText("0");
                }
            }
        });

        label.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevLabel.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveValues();
            }
        });
    }

    /**
     * Method responsible for persisting created counter in database(s).
     * @since 1.0
     */
    void saveValues(){
        DbStor db = new DbStor(getApplicationContext());
        LocalElement elem = new LocalElement(label.getText().toString(),0,0,twoVal.isChecked()?"true":"false");
        db.addLocalElement(elem);
        finish();
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
