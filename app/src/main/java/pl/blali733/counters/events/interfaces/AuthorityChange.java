package pl.blali733.counters.events.interfaces;

/**
 * Interface defining methods signaling authority changes to fragments.
 * @author blali733
 * @version 1.0
 * @since 0.3
 */
public interface AuthorityChange{
    /**
     * Method executed after user authority has changed.
     * @since 1.0
     */
    void onAuthChange();
}
