package pl.blali733.counters.storage.data;

/**
 * Class defining parameters of displayed counter element.
 * @author blali733
 * @version 1.0
 * @since 0.2 app / 1.0 package
 */
public class CounterListElement {

    /**
     * Description of counter.
     */
    private String label;

    /**
     * Primary value.
     */
    private int v1;

    /**
     * Secondary value, equal to 0 for single counters.
     */
    private int v2;

    /**
     * Defines if counter is double or single.
     */
    private boolean mixed;

    /**
     * UUID of reference in database.
     */
    private String uuid;

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
    public boolean isMixed() {
        return mixed;
    }

    /**
     * Setter.
     * @param mixed New value.
     * @since 1.0
     */
    public void setMixed(boolean mixed) {
        this.mixed = mixed;
    }

    /**
     * Getter.
     * @return Current value.
     * @since 1.0
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Setter.
     * @param uuid New value.
     * @since 1.0
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Default empty constructor.
     * @since 1.0
     */
    public CounterListElement(){}

    /**
     * Full constructor.
     * @param label Item label.
     * @param v1 Item first value.
     * @param v2 Item second value.
     * @param mixed Item mixed flag.
     * @since 1.0
     */
    public CounterListElement(String label, int v1, int v2, boolean mixed, String uuid) {
        this.label = label;
        this.v1 = v1;
        this.v2 = v2;
        this.mixed = mixed;
        this.uuid = uuid;
    }
}
