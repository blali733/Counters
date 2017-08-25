package pl.blali733.counters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CreatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);
        //getLayoutInflater().inflate(R.layout.activity_creator,frameLayoutCtx);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
