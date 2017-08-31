package pl.blali733.counters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Target;

import pl.blali733.counters.dialogs.CreatorDialog;
import pl.blali733.counters.dialogs.SettingsDialog;
import pl.blali733.counters.events.AuthFragment;
import pl.blali733.counters.fragments.ListFragment;
import pl.blali733.counters.storage.Settings;

/**
 * Class servicing MainActivity.
 * @author blali733
 * @version 0.4
 * @since 0.1
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener{

    //DOCME document class fields
    private GoogleApiClient mGoogleApiClient;
    private TextView nameText;
    private TextView mailText;
    private ImageView userImage;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    DrawerLayout drawerLayoutCtx;
    Toolbar toolbarCtx;
    NavigationView navigationViewCtx;

    /**
     * Logging tag.
     */
    private static final String TAG = "MainActivity";

    /**
     * Sign-in intent request code.
     * @see "Google apis."
     */
    private static final int RC_SIGN_IN = 9001;

    /**
     * Method responsible for creation of activity.
     * @param savedInstanceState saved instance state.
     * @since 0.1
     */
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

        Settings.getInstance().initializeSettings(this);

        toolbarCtx = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarCtx);

        drawerLayoutCtx = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayoutCtx, toolbarCtx, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutCtx.addDrawerListener(toggle);
        toggle.syncState();

        navigationViewCtx = findViewById(R.id.nav_view);
        navigationViewCtx.setNavigationItemSelectedListener(this);
        View headerView = navigationViewCtx.getHeaderView(0);
        nameText = headerView.findViewById(R.id.nameText);
        mailText = headerView.findViewById(R.id.mailText);
        userImage = headerView.findViewById(R.id.pic);

        //Db hook:
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (findViewById(R.id.ContentFrame) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            ListFragment frag = new ListFragment();
            frag.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ContentFrame, frag)
                    .commit();
            navigationViewCtx.setCheckedItem(R.id.nav_own);
        }
    }

    /**
     * Method responsible for triggering view update on each start of activity.
     * @since 0.1
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    /**
     * Sign in method.
     * @since 0.1
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Sign out method.
     * @since 0.1
     */
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

    /**
     * Method (event) servicing returned values from sub activities.
     * @param requestCode Request code.
     * @param resultCode Result code.
     * @param data Intent with return content.
     * @since 0.1
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /**
     * Method for handling sign-in procedure for obtained user authority.
     * @param result result of google sign-in procedure.
     * @since 0.1
     */
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if(acct != null)
                Log.d(TAG, "FirebaseAuthWithGoogle:" + acct.getId());
            else
                Log.d(TAG, "FirebaseAuthWithGoogle: invalid user id");

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
                                Toast.makeText(MainActivity.this, R.string.FirebaseAuthFail,
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(MainActivity.this,R.string.AuthFail,Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    /**
     * Method responsible for tailoring displayed UI elements basing on current state of authentication.
     * @param user Firebase User object or null for local client.
     * @since 0.1
     */
    private void updateUI(FirebaseUser user) {
        Menu mMenu = navigationViewCtx.getMenu();
        Target mTarget = new Target() {
            @Override
            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                RoundedBitmapDrawable round = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
                round.setCircular(true);
                round.setCornerRadius(150);
                userImage.setImageDrawable(round);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                userImage.setImageDrawable(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                userImage.setImageDrawable(placeHolderDrawable);
            }
        };
        if (user != null) {
            String mName = user.getDisplayName();
            nameText.setText(mName);
            mailText.setText(user.getEmail());
            Picasso.with(getApplicationContext()).load(user.getPhotoUrl()).placeholder(android.R.drawable.sym_def_app_icon).into(mTarget);
            mMenu.findItem(R.id.log_in).setVisible(false);
            mMenu.findItem(R.id.log_out).setVisible(true);
        } else {
            nameText.setText(R.string.placeholderName);
            mailText.setText(R.string.placeholderMail);
            Picasso.with(getApplicationContext()).load(android.R.drawable.sym_def_app_icon).placeholder(android.R.drawable.sym_def_app_icon).into(mTarget);
            mMenu.findItem(R.id.log_in).setVisible(true);
            mMenu.findItem(R.id.log_out).setVisible(false);
        }
        AuthFragment frag = (AuthFragment) getSupportFragmentManager().findFragmentById(R.id.ContentFrame);
        if(frag != null) {
            frag.onAuthChange();
            Settings.getInstance().onAuthChange();
        }
    }

    /**
     * Method (event) required for servicing critical errors in Google API integration.
     * @param connectionResult Connection result handle.
     * @since 0.1
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(MainActivity.this,R.string.GAPIFatal,Toast.LENGTH_LONG).show();
    }

    /**
     * Method (event) responsible for closing expanded drawer by pressing back key.
     * @since 0.1
     */
    @Override
    public void onBackPressed() {
        if (drawerLayoutCtx.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutCtx.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Method (event) servicing menu actions.
     * @param item Id of item selected by user.
     * @return Defines if selected item should be highlighted or not.
     * @since 0.1
     */
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

        //TODO solve problem of multiple clones on pressing back
        switch(id){
            case R.id.log_in:{
                signIn();
            }break;
            case R.id.log_out:{
                signOut();
            }break;
            case R.id.nav_own:{
                //TODO reimplement

            }break;
            case R.id.nav_shared:{
                //TODO: Implement shared counters list.
                Toast.makeText(this,"Not implemented yet.",Toast.LENGTH_SHORT).show();
                return false;
            }//break;
            case R.id.nav_share:{
                //TODO: Implement counter sharing.
                Toast.makeText(this,"Not implemented yet.",Toast.LENGTH_SHORT).show();
                return false;
            }//break;
            case R.id.nav_send:{
                //TODO: Implement sending counter results.
                Toast.makeText(this,"Not implemented yet.",Toast.LENGTH_SHORT).show();
                return false;
            }//break;
        }

        drawerLayoutCtx.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsDialog.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
