package pl.blali733.counters.storage.data;

/**
 *
 * @author blali733
 * @version 1.0
 * @since 0.2
 */
public class LocalElement {

    //DOCME fields
    private int id;
    private String auth;
    private String label;
    private int v1,v2;
    private String mixed;
    private String dirty;

    /**
     * Getter.
     * @return Current value.
     * @since 1.0
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter.
     * @param label New value.
     * @since 1.0
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter.
     * @return Current value.
     * @since 1.0
     */
    public int getV1() {
        return v1;
    }

    /**
     * Setter.
     * @param v1 New value.
     * @since 1.0
     */
    public void setV1(int v1) {
        this.v1 = v1;
    }

    /**
     * Getter.
     * @return Current value.
     * @since 1.0
     */
    public int getV2() {
        return v2;
    }

    /**
     * Setter.
     * @param v2 New value.
     * @since 1.0
     */
    public void setV2(int v2) {
        this.v2 = v2;
    }

    /**
     * Getter.
     * @return Current value.
     * @since 1.0
     */
    public int getId() {
        return id;
    }

    /**
     * Setter.
     * @param id New value.
     * @since 1.0
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter.
     * @return Current value.
     * @since 1.0
     */
    public String getAuth() {
        return auth;
    }

    /**
     * Setter.
     * @param auth New value.
     * @since 1.0
     */
    public void setAuth(String auth) {
        this.auth = auth;
    }

    /**
     * Getter.
     * @return Current value.
     * @since 1.0
     */
    public String getMixed() {
        return mixed;
    }

    /**
     * Setter.
     * @param mixed New value.
     * @since 1.0
     */
    public void setMixed(String mixed){this.mixed = mixed;}

    /**
     * Getter.
     * @return Current value.
     * @since 1.0
     */
    public String getDirty() {
        return dirty;
    }

    /**
     * Setter.
     * @param dirty New value.
     * @since 1.0
     */
    public void setDirty(String dirty){this.dirty = dirty;}

    /**
     * Default constructor, creates empty object with set dirty flag (String representation).
     * @since 1.0
     */
    public LocalElement(){
        dirty = "true";
    }

    /**
     * Full constructor, used for getting items from database.
     * @param id Item id.
     * @param auth Authority of item owner.
     * @param label Item label.
     * @param v1 Item first value.
     * @param v2 Item second value.
     * @param mixed Item mixed flag - converted to String.
     * @param dirty Item dirty flag - converted to String.
     * @since 1.0
     */
    public LocalElement(int id, String auth, String label, int v1, int v2, String mixed, String dirty) {
        this.id = id;
        this.auth = auth;
        this.label = label;
        this.v1 = v1;
        this.v2 = v2;
        this.mixed = mixed;
        this.dirty = dirty;
    }

    /**
     * Partial constructor, used to store item in database.
     * @param label Item label.
     * @param v1 Item first value.
     * @param v2 Item second value.
     * @param mixed Item mixed flag - converted to String.
     * @since 1.0
     */
    public LocalElement(String label, int v1, int v2, String mixed) {
        this.label = label;
        this.v1 = v1;
        this.v2 = v2;
        this.mixed = mixed;
        this.dirty = "true";
    }
}
