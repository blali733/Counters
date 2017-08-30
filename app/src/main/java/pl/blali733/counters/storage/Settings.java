package pl.blali733.counters.storage;

/**
 * Settings singleton.
 * @author blali733
 * @version 1.0
 * @since 0.5 app / 1.0 pkg
 */
class Settings {
    /**
     * Instance of singleton.
     */
    private static final Settings ourInstance = new Settings();

    /**
     * Direction of spinners.
     */
    private static boolean direction;

    /**
     * Instance getter.
     * @return instance of singleton.
     * @since 1.0
     */
    static Settings getInstance() {
        return ourInstance;
    }

    /**
     * Switcher of direction value.
     * @since 1.0
     */
    public static void toggleDirection(){
        direction = !direction;
        persist();
    }

    /**
     * Getter of direction.
     * @return direction value.
     * @since 1.0
     */
    public static boolean isDirection() {
        return direction;
    }

    /**
     * Method persisting current settings state.
     * @since 1.0
     */
    //TODO implement
    private static void persist(){

    }

    /**
     * Constructor.
     * @since 1.0
     */
    private Settings() {
        //TODO implement restoring saved settings state
    }
}
