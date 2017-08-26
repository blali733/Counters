package pl.blali733.counters.fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import pl.blali733.counters.dialogs.CreatorActivity;
import pl.blali733.counters.R;
import pl.blali733.counters.events.AuthFragment;
import pl.blali733.counters.storage.data.CounterListElement;
import pl.blali733.counters.storage.DbStor;

/**
 * Class
 * @author blali733
 * @version 1.0
 * @since 0.2
 */
public class ListFragment extends AuthFragment{

    //DOCME fields
    private ListView list;
    private List<CounterListElement> counterElementList;
    private DbStor locStor;

    /**
     * Logging tag.
     */
    private static final String TAG = "ListView";

    /**
     * Method responsible for inflation of fragment view.
     * @param inflater Layout inflater instance.
     * @param container View container which would be inflated.
     * @param savedInstanceState State of application.
     * @return Inflated view.
     * @since 1.0
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    /**
     * Method executed after creation of view, responsible for setting up fragment content.
     * @param view Root view of fragment.
     * @param savedInstanceState State of application.
     * @since 1.0
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        Log.i(TAG,"content drawn");

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCounter();
            }
        });
        //ad:
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("3AF148596AC5095AAF4C56253E9DB321")  //My Huawei P8 Lite
                .addTestDevice("8B0030D18C1EA1DFD60DE9CBA28AAD62")  //My Alcatel 6030X
                .build();
        mAdView.loadAd(adRequest);

        //Content List:
        list = view.findViewById(R.id.list_view);
        locStor = new DbStor(this.getContext());
        loadCounterElementList();
        loadListView();
        addClickListener();
    }

    /**
     * Method responsible for reaction to authority change event.
     * @see pl.blali733.counters.events.AuthFragment#onAuthChange()
     * @since 1.0
     */
    @Override
    public void onAuthChange() {
        loadCounterElementList();
        loadListView();
    }

    /**
     * Method responsible for reloading list on resume to activity.
     * @since 1.0
     */
    @Override
    public void onResume(){
        super.onResume();
        loadCounterElementList();
        loadListView();
    }

    /**
     * Method responsible for getting list of elements from database.
     * @since 1.0
     */
    private void loadCounterElementList(){
        String curUser;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null)
            curUser = mAuth.getCurrentUser().getEmail();
        else
            curUser = "localhost";
        counterElementList = locStor.displayList(curUser);
    }

    /**
     * Method responsible for population of ListView with content of obtained list.
     * @since 1.0
     */
    private void loadListView(){
        ArrayAdapter<CounterListElement> adapter = new ArrayAdapter<CounterListElement>(getActivity(),
                R.layout.list_item,
                counterElementList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }

                TextView label = convertView.findViewById(R.id.item_label);
                label.setText(counterElementList.get(position).getLabel());

                TextView value = convertView.findViewById(R.id.item_value);
                if(counterElementList.get(position).isMixed()){
                    value.setText(Integer.toString(counterElementList.get(position).getV1())+" / "+Integer.toString(counterElementList.get(position).getV2()));
                }else{
                    value.setText(Integer.toString(counterElementList.get(position).getV1()));
                }

                return convertView;
            }
        };

        list.setAdapter(adapter);
    }

    /**
     * Method adding ClickListener to items displayed on list.
     * @since 1.0
     */
    private void addClickListener(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {

            }
        });
    }

    /**
     * Method forwarding execution to counter creator activity.
     * @since 1.0
     */
    private void addCounter(){
        startActivity(new Intent(this.getContext(),CreatorActivity.class));
    }
}
