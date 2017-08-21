package pl.blali733.counters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CreatorActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_creator,frameLayoutCtx);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
