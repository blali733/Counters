package pl.blali733.counters;

import java.util.ArrayList;
import java.util.List;

public class CounterElement {
    String label;
    int v1,v2;
    boolean mixed;
    List<String> shared;
    boolean dirty;

    public CounterElement(){
        shared = new ArrayList<>();
        dirty = true;
    }
}
