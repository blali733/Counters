package pl.blali733.counters;

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

import pl.blali733.counters.storage.DbStor;
import pl.blali733.counters.storage.data.LocalElement;

public class CreatorActivity extends AppCompatActivity {

    private EditText label;
    private Switch twoVal;
    private TextView prevLabel;
    private TextView prevVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);
        final View preview = getLayoutInflater().inflate(R.layout.list_item, (FrameLayout)findViewById(R.id.preview));

        label = findViewById(R.id.ilabel);
        twoVal = findViewById(R.id.mVal);
        Button confirm = findViewById(R.id.createButton);
        prevLabel = preview.findViewById(R.id.item_label);
        prevVal = preview.findViewById(R.id.item_value);

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

    void saveValues(){
        DbStor db = new DbStor(getApplicationContext());
        LocalElement elem = new LocalElement(label.getText().toString(),0,0,twoVal.isChecked()?"true":"false");
        db.addLocalElement(elem);
        finish();
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
