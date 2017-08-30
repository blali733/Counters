package pl.blali733.counters.storage;

/**
 * Settings singleton.
 * @author blali733
 * @version 1.0
 * @since 0.5 app / 1.0 pkg
 */
public class Settings {
    /**
     * Instance of singleton.
     */
    private static final Settings ourInstance = new Settings();

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
        persist();
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
     * @since 1.0
     */
    //TODO implement
    private void persist(){

    }

    /**
     * Constructor.
     * @since 1.0
     */
    private Settings() {
        //TODO implement restoring saved settings state
    }
}
