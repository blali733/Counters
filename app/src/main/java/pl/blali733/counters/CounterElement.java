package pl.blali733.counters;

public class CounterElement {
    String label;
    int v1,v2;
    boolean mixed;

    public CounterElement(){}

    public CounterElement(String label, int v1, int v2, boolean mixed) {
        this.label = label;
        this.v1 = v1;
        this.v2 = v2;
        this.mixed = mixed;
    }
}
