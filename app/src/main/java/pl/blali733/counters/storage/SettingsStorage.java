package pl.blali733.counters.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

import pl.blali733.counters.events.interfaces.AuthorityChange;

/**
 * Class responsible for storing settings in device memory.
 * @author blali733
 * @version 1.0
 * @since 0.5 app / 1.3 pkg
 */
public class SettingsStorage implements AuthorityChange {

    /**
     * Application context.
     */
    private Context ctx = null;

    /**
     * Handle to shared preferences object.
     */
    private SharedPreferences pref;

    /**
     * Handle to editable version of shared preferences object.
     */
    private SharedPreferences.Editor editor;

    /**
     * Private mode toggle?
     */
    private int PRIVATE_MODE = 0;

    /**
     * {@inheritDoc}
     */
    @SuppressLint("CommitPrefEdits")
    @Override
    public void onAuthChange() {
        if(ctx!=null){
            String name;
            if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                name = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            }else{
                name = "localhost";
            }
            pref = ctx.getSharedPreferences(name, PRIVATE_MODE);
            editor = pref.edit();
        }
    }

    /**
     * Sets up storage functions.
     * @param ctx Application context
     * @since 1.0
     */
    SettingsStorage(Context ctx){
        this.ctx = ctx;
        String name;
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            name = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }else{
            name = "localhost";
        }
        pref = ctx.getSharedPreferences(name, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Function persisting values.
     * @param key item key.
     * @param value item value.
     * @since 1.0
     */
    public void storeValue(String key, String value){
        editor.putString(key, value);
        editor.commit();

    }

    /**
     * Function retrieving stored values.
     * @param key item key.
     * @return item value.
     * @since 1.0
     */
    public String getStringValue(String key){
        return pref.getString(key, null);
    }

    /**
     * Function removing stored value.
     * @param key item key.
     * @since 1.0
     */
    public void removeValue(String key){
        editor.remove(key);
        editor.commit();
    }
}
