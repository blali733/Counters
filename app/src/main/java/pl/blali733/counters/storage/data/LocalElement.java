package pl.blali733.counters.storage.data;

public class LocalElement {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getMixed() {
        return mixed;
    }

    public String getDirty() {
        return dirty;
    }

    private int id;
    private String auth;
    private String label;
    private int v1,v2;
    private String mixed;
    private String dirty;

    public LocalElement(){
        dirty = "true";
    }

    public LocalElement(int id, String auth, String label, int v1, int v2, String mixed, String dirty) {
        this.id = id;
        this.auth = auth;
        this.label = label;
        this.v1 = v1;
        this.v2 = v2;
        this.mixed = mixed;
        this.dirty = dirty;
    }

    public LocalElement(String label, int v1, int v2, String mixed) {
        this.label = label;
        this.v1 = v1;
        this.v2 = v2;
        this.mixed = mixed;
        this.dirty = "true";
    }
}
