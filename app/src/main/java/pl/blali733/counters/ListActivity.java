package pl.blali733.counters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import pl.blali733.counters.storage.DbStor;

public class ListActivity extends MainActivity {

    private ListView list;
    private List<CounterElement> counterElementList;
    private DbStor locStor;
    private AdView mAdView;

    private static final String TAG = "ListView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_list, frameLayoutCtx);
        Log.i(TAG,"content drawn");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCounter();
            }
        });
        //Firebase auth:
        //ad:
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("3AF148596AC5095AAF4C56253E9DB321")  //My Huawei P8 Lite
                .build();
        mAdView.loadAd(adRequest);

        //Content List:
        locStor = new DbStor(this);
        loadCounterElementList();
        loadListView();
        addClickListener();
    }

    private void loadCounterElementList(){
        String curUser;
        if(mAuth.getCurrentUser()!=null)
            curUser = mAuth.getCurrentUser().getEmail();
        else
            curUser = "localhost";
        counterElementList = locStor.displayList(curUser);
    }

    private void loadListView(){
        list = (ListView)findViewById(R.id.list_view);

        ArrayAdapter<CounterElement> adapter = new ArrayAdapter<CounterElement>(this,
                R.layout.list_item,
                counterElementList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }

                TextView label = (TextView)convertView.findViewById(R.id.item_label);
                label.setText(counterElementList.get(position).getLabel());

                TextView value = (TextView)convertView.findViewById(R.id.item_value);
                if(counterElementList.get(position).isMixed()){
                    value.setText(counterElementList.get(position).getV1()+" / "+counterElementList.get(position).getV2());
                }else{
                    value.setText(counterElementList.get(position).getV1());
                }

                return convertView;
            }
        };

        list.setAdapter(adapter);
    }

    private void addClickListener(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {
//                Intent i = manager.getLaunchIntentForPackage(apps.get(pos).name.toString());
//                AppsListActivity.this.startActivity(i);
                //load counter edit layout
            }
        });
    }

    private void addCounter(){
        startActivity(new Intent(getApplicationContext(),CreatorActivity.class));
    }
}
