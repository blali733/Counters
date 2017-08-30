package pl.blali733.counters.storage;

import android.content.Context;

import pl.blali733.counters.events.interfaces.AuthorityChange;

/**
 * Settings singleton.
 * @author blali733
 * @version 1.1
 * @since 0.5 app / 1.0 pkg
 */
public class Settings implements AuthorityChange {
    /**
     * Instance of singleton.
     */
    private static final Settings ourInstance = new Settings();

    /**
     * Settings storage.
     */
    private SettingsStorage mSettingsStorage;

    /**
     * Direction of spinners.
     */
    private boolean directionUp = false;

    /**
     * Instance getter.
     * @return instance of singleton.
     * @since 1.0
     */
    public static Settings getInstance() {
        return ourInstance;
    }

    /**
     * Switcher of directionUp value.
     * @since 1.0
     */
    public void setDirectionUp(boolean directionUp){
        this.directionUp = directionUp;
        mSettingsStorage.storeValue("direction",Boolean.toString(directionUp));
    }

    /**
     * Getter of directionUp.
     * @return directionUp value.
     * @since 1.0
     */
    public boolean isDirectionUp() {
        return directionUp;
    }

    /**
     * Method persisting current settings state.
     * @since 1.1
     */
    public void initializeSettings(Context ctx){
        mSettingsStorage = new SettingsStorage(ctx);
        directionUp = Boolean.parseBoolean(mSettingsStorage.getStringValue("direction"));
    }

    /**
     * Constructor.
     * @since 1.0
     */
    private Settings(){}

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAuthChange() {
        if(mSettingsStorage != null){
            mSettingsStorage.onAuthChange();
        }
    }
}
