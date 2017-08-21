package pl.blali733.counters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import pl.blali733.counters.storage.DbStor;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener{

    private boolean listActLoaded = false;

    private ListView list;
    private List<CounterElement> counterElementList;
    private DbStor locStor;
    private AdView mAdView;

    private GoogleApiClient mGoogleApiClient;
    private TextView nameText;
    private TextView mailText;
    private ImageView userImage;
    private DatabaseReference mDatabase;

    DrawerLayout drawerLayoutCtx;
    Toolbar toolbarCtx;
    FrameLayout frameLayoutCtx;
    NavigationView navigationViewCtx;
    FirebaseAuth mAuth;

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;

    //TODO: Implement global sync.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"Main activity drawn");
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("966591412857-6ub503fm88aq82a99ur5rv039evd7ire.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //Firebase auth:
        mAuth = FirebaseAuth.getInstance();

        toolbarCtx = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarCtx);

        frameLayoutCtx = (FrameLayout) findViewById(R.id.ContentFrame) ;

        drawerLayoutCtx = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayoutCtx, toolbarCtx, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutCtx.setDrawerListener(toggle);
        toggle.syncState();

        navigationViewCtx = (NavigationView) findViewById(R.id.nav_view);
        navigationViewCtx.setNavigationItemSelectedListener(this);
        View headerView = navigationViewCtx.getHeaderView(0);
        nameText = (TextView)headerView.findViewById(R.id.nameText);
        mailText = (TextView)headerView.findViewById(R.id.mailText);
        userImage = (ImageView)headerView.findViewById(R.id.pic);

        //Db hook:
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Dumb buggy shit TODO rework
        navigationViewCtx.getMenu().performIdentifierAction(R.id.nav_own, 0);
        //if (savedInstanceState == null) this.onNavigationItemSelected(navigationViewCtx.getMenu().findItem(R.id.nav_own));
        navigationViewCtx.setCheckedItem(R.id.nav_own);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        mAuth.signOut();
                        updateUI(null);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(MainActivity.this,R.string.AuthFail,Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "FirebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //TODO: Create data migration popup.
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, R.string.AuthFail,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Menu mMenu = navigationViewCtx.getMenu();
        if (user != null) {
            String mName = user.getDisplayName();
            nameText.setText(mName);
            mailText.setText(user.getEmail());
            Picasso.with(getApplicationContext()).load(user.getPhotoUrl()).placeholder(android.R.drawable.sym_def_app_icon).into(userImage);
            mMenu.findItem(R.id.log_in).setVisible(false);
            mMenu.findItem(R.id.log_out).setVisible(true);
        } else {
            nameText.setText(R.string.placeholderName);
            mailText.setText(R.string.placeholderMail);
            Picasso.with(getApplicationContext()).load(android.R.drawable.sym_def_app_icon).placeholder(android.R.drawable.sym_def_app_icon).into(userImage);
            mMenu.findItem(R.id.log_in).setVisible(true);
            mMenu.findItem(R.id.log_out).setVisible(false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(MainActivity.this,R.string.GAPIFatal,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutCtx.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutCtx.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //to prevent current item select over and over
        if (item.isChecked()){
            drawerLayoutCtx.closeDrawer(GravityCompat.START);
            return false;
        }

        switch(id){
            case R.id.log_in:{
                signIn();
            }break;
            case R.id.log_out:{
                signOut();
            }break;
            case R.id.nav_own:{
                //TODO Kinda hackish but it works - for now
                getLayoutInflater().inflate(R.layout.activity_list, frameLayoutCtx);
                if(!listActLoaded)
                    initListAct();
                Log.i(TAG,"ListActivity content drawn");
            }break;
            case R.id.nav_shared:{
                //TODO: Implement shared counters list.
            }break;
            case R.id.nav_share:{
                //TODO: Implement counter sharing.
            }break;
            case R.id.nav_send:{
                //TODO: Implement sending counter results.
            }break;
        }

        drawerLayoutCtx.closeDrawer(GravityCompat.START);
        return true;
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

    public void initListAct(){
        listActLoaded = true;
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
}
