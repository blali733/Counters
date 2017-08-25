package pl.blali733.counters.storage.data;

public class CounterElement {
    private String label;
    private int v1,v2;
    private boolean mixed;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getV1() {
        return v1;
    }

    public void setV1(int v1) {
        this.v1 = v1;
    }

    public int getV2() {
        return v2;
    }

    public void setV2(int v2) {
        this.v2 = v2;
    }

    public boolean isMixed() {
        return mixed;
    }

    public void setMixed(boolean mixed) {
        this.mixed = mixed;
    }

    public CounterElement(){}

    public CounterElement(String label, int v1, int v2, boolean mixed) {
        this.label = label;
        this.v1 = v1;
        this.v2 = v2;
        this.mixed = mixed;
    }
}
